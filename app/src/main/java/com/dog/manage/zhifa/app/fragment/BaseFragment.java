package com.dog.manage.zhifa.app.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.base.BaseApplication;
import com.base.Constants;
import com.base.UserInfo;
import com.base.utils.CommonUtil;
import com.base.utils.MsgCache;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.activity.Callback;
import com.dog.manage.zhifa.app.activity.LoginActivity;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import okhttp3.Call;

public class BaseFragment extends Fragment {

    public String TAG = this.getClass().getName();

    @SuppressLint("NewApi")
    public void setStatusBarHeight(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getActivity().getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            if (view.findViewById(R.id.status_bar) != null) {
                view.findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
                view.findViewById(R.id.status_bar).getLayoutParams().height = CommonUtil.getStatusBarHeight(getActivity());
            }
        }
    }

    public void setTypeface(TextView textView) {
        Typeface typeface = BaseApplication.getInstance().getTypeface();
        textView.setTypeface(typeface);
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

    public void setUserInfo(UserInfo userInfo) {
        MsgCache.get(getActivity()).put(Constants.USER_INFO, userInfo);
    }

    public UserInfo getUserInfo() {
        try {
            UserInfo userinfo = (UserInfo) MsgCache.get(getActivity()).getAsObject(Constants.USER_INFO);
            if (!CommonUtil.isBlank(userinfo)) {
                return userinfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new UserInfo();
        }
        return new UserInfo();
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
                        if (callback != null) {
                            callback.onResponse(response.isSuccess(), id);
                        }
                    }
                });
    }

    public void openActivity(Class<?> mClass) {
        openActivity(mClass, null);
    }

    public void openActivity(Class<?> mClass, int requestCode) {
        Intent intent = new Intent(getActivity(), mClass);
        startActivityForResult(intent, requestCode);
    }

    public void openActivity(Class<?> mClass, Bundle mBundle) {
        Intent intent = new Intent(getActivity(), mClass);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivity(intent);
    }

    public void openActivity(Class<?> mClass, Bundle mBundle, int requestCode) {
        Intent intent = new Intent(getActivity(), mClass);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivityForResult(intent, requestCode);
    }

}
