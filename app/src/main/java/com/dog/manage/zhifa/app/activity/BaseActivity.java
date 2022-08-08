package com.dog.manage.zhifa.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.base.BaseApplication;
import com.base.Constants;
import com.base.UserInfo;
import com.base.utils.CommonUtil;
import com.base.utils.MsgCache;
import com.base.utils.PermissionUtils;
import com.base.utils.StatusBarUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.Config;
import com.dog.manage.zhifa.app.DogDialogManager;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.model.AddressBean;
import com.dog.manage.zhifa.app.model.Dog;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.callbacks.StringCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class BaseActivity extends AppCompatActivity {

    public String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDarkTheme(false);

    }

    public void onClickBack(View view) {
        finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    public <T extends ViewDataBinding> T getViewData(int layoutId) {
        T binding = DataBindingUtil.setContentView(this, layoutId);
//        if (findViewById(R.id.titleView) != null) {
//            setTypeface(findViewById(R.id.titleView));
//        }
        return binding;
    }

    public void setStatusBarDarkTheme(boolean dark) {
        if (!StatusBarUtil.setStatusBarDarkTheme(this, dark)) {
            StatusBarUtil.setStatusBarColor(this, dark ? R.color.black : R.color.white);
        }
    }

    @SuppressLint("NewApi")
    public void setStatusBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            if (findViewById(R.id.status_bar) != null) {
                findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
                findViewById(R.id.status_bar).getLayoutParams().height = CommonUtil.getStatusBarHeight(getApplication());
            }
        }
    }

    public void setTypeface(TextView textView) {
        Typeface typeface = BaseApplication.getInstance().getTypeface();
        textView.setTypeface(typeface);
    }

    public void onClickDogCertificate(Activity activity, List<Dog> list, int index, OnClickListener listener) {
        DogDialogManager.getInstance().showDogListDialog(activity, list, index,
                new DogDialogManager.OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        if (listener != null)
                            listener.onClick(view, object);
                    }
                });
    }

    public void updateAddressView(TextView addressView, String addressId) {
        //省110000 、市110100
        SendRequest.getAddressAreas(3, 110100, 1, 100,
                new GenericsCallback<Pager<AddressBean>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(Pager<AddressBean> response, int id) {
                        if (response != null && response.getRows() != null && response.getRows().size() > 0) {
                            for (AddressBean bean : response.getRows()) {
                                if (addressId != null && addressId.indexOf(bean.getId() + "") != -1) {
                                    addressView.setText(bean.getAreaName());
                                    return;
                                }
                            }
                        }
                    }
                });
    }

    public void getAccessToken(Activity activity) {
        SendRequest.getAccessToken(Config.yueBaoAccessKey, Config.yueBaoSecretKey,
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        try {
                            JSONObject object = new JSONObject(response);
                            int status = object.optInt("status");
                            if (status == 200) {
                                String accessToken = object.optJSONObject("data").optString("access_token");
                                UserInfo userInfo = getUserInfo();
                                userInfo.setAccessToken(accessToken);
                                setUserInfo(userInfo);

                            } else if (status == 401) {
                                ToastUtils.showShort(activity, object.optString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public boolean checkUserRank(Context context) {
        return checkUserRank(context, false);
    }

    public boolean checkUserRank(Context context, boolean login) {
        UserInfo user = getUserInfo();
        //游客模式
        if (user == null || TextUtils.isEmpty(user.getAuthorization())) {
            if (login) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            return false;
        }
        return true;
    }

    public void updateUserInfo() {
        updateUserInfo(null);
    }

    public void updateUserInfo(Callback callback) {
        SendRequest.userLoad(getUserInfo().getAuthorization(), getUserInfo().getId(),
                new GenericsCallback<ResultClient<UserInfo>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (callback != null)
                            callback.onError();
                    }

                    @Override
                    public void onResponse(ResultClient<UserInfo> response, int id) {
                        if (response.isSuccess()) {
                            response.getData().setAuthorization(getUserInfo().getAuthorization());
                            setUserInfo(response.getData());
                        }
                        if (callback != null)
                            callback.onResponse(response.isSuccess(), id);
                    }
                });
    }

    public void setUserInfo(UserInfo userInfo) {
        MsgCache.get(this).put(Constants.USER_INFO, userInfo);
    }

    public UserInfo getUserInfo() {
        try {
            UserInfo userinfo = (UserInfo) MsgCache.get(this).getAsObject(Constants.USER_INFO);
            if (!CommonUtil.isBlank(userinfo)) {
                return userinfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new UserInfo();
        }
        return new UserInfo();
    }

    public void openActivity(Class<?> mClass) {
        openActivity(mClass, null);
    }

    public void openActivity(Class<?> mClass, int requestCode) {
        Intent intent = new Intent(this, mClass);
        startActivityForResult(intent, requestCode);
    }

    public void openActivity(Class<?> mClass, Bundle mBundle) {
        Intent intent = new Intent(this, mClass);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivity(intent);
    }

    public void openActivity(Class<?> mClass, Bundle mBundle, int requestCode) {
        Intent intent = new Intent(this, mClass);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivityForResult(intent, requestCode);
    }

    private static List<Activity> activityStack = new ArrayList<Activity>();

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    public static void finishActivity(Class mClass) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && null != mClass
                    && mClass.getSimpleName().equals(activityStack.get(i).getClass().getSimpleName())) {
                activityStack.get(i).finish();
            }
        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public boolean checkPermissions(String type, int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isAllGranted = PermissionUtils.checkPermissionAllGranted(this, type);
            if (!isAllGranted) {
                PermissionUtils.requestPermissions(this, type, code);
                return false;
            }
        }
        return true;
    }
}
