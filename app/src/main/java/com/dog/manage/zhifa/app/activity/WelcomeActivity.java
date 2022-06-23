package com.dog.manage.zhifa.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.base.manager.DialogManager;
import com.dog.manage.zhifa.app.MainActivity;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityWelcomeBinding;
import com.okhttp.utils.APIUrls;

public class WelcomeActivity extends BaseActivity {

    private ActivityWelcomeBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_welcome);
        setStatusBarHeight();
        setStatusBarDarkTheme(true);

        sharedPreferences = getSharedPreferences("sp_data", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("appService", false)) {
            appService();
        } else {
            init();
        }

    }

    private void appService() {
        DialogManager.showServiceDialog(WelcomeActivity.this, new DialogManager.OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                switch (view.getId()) {
                    case R.id.tv_confirm:
                        sharedPreferences.edit().putBoolean("appService", true).apply();
                        init();

                        break;
                    case R.id.tv_cancel:
                        System.exit(1);

                        break;
                }
            }
        }, new DialogManager.Listener() {
            @Override
            public void onItemLeft() {
                Intent intent = new Intent();
                intent.setData(Uri.parse(APIUrls.protocol));
                intent.setAction(Intent.ACTION_VIEW);
//                startActivity(intent);
            }

            @Override
            public void onItemRight() {
                Intent intent = new Intent();
                intent.setData(Uri.parse(APIUrls.service));
                intent.setAction(Intent.ACTION_VIEW);
//                startActivity(intent);
            }
        });
    }

    private void init() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(MainActivity.class);
                finish();
            }
        }, 1000);

//        if (CommonUtil.isBlank(getUserInfo()) || CommonUtil.isBlank(getUserInfo().getAuthorization())) {
//            SendRequest.getVisitor(new GenericsCallback<ResultClient<UserInfo>>(new JsonGenericsSerializator()) {
//                @Override
//                public void onError(Call call, Exception e, int id) {
//
//                }
//
//                @Override
//                public void onResponse(ResultClient<UserInfo> response, int id) {
//                    if (response.isSuccess() && response.getData() != null) {
//                        BaseApplication.getInstance().setUserInfo(response.getData());
//                        openActivity(MainActivity.class);
//                        finish();
//                    } else {
//                        ToastUtils.showShort(WelcomeActivity.this, response.getMessage());
//                    }
//                }
//            });
//        } else {
//            openActivity(MainActivity.class);
//            finish();
//        }
    }
}