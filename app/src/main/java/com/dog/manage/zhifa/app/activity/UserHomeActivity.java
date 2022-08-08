package com.dog.manage.zhifa.app.activity;


import android.os.Bundle;
import android.view.View;

import com.base.BaseApplication;
import com.base.UserInfo;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityUserHomeBinding;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import okhttp3.Call;

public class UserHomeActivity extends BaseActivity {
    private ActivityUserHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_user_home);

        if (!CommonUtil.isBlank(getUserInfo().getPhone()) && getUserInfo().getPhone().length() >= 11) {
            String phone = getUserInfo().getPhone();
            binding.userNameView.setText(phone.substring(0, 3) + "****" + phone.substring(7));
        } else {
            binding.userNameView.setText(getUserInfo().getLoginPhone());
        }

        SendRequest.getLawUser(new GenericsCallback<ResultClient<UserInfo>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<UserInfo> response, int id) {
                if (response.isSuccess()) {
                    if (response.getData() != null) {
                        binding.unitNameView.setText("所属单位：" + response.getData().getUnitName());
                        binding.unitUserNameView.setText("姓名：" + response.getData().getUserName());
                        binding.phoneView.setText("电话：" + response.getData().getPhone());
                        binding.createdTimeView.setText("开通时间：" + response.getData().getCreatedTime());
                        binding.statusView.setText("账号状态：" + (response.getData().getStatus() == 0 ? "启用" : "停用"));
                    } else {
                        ToastUtils.showShort(getApplicationContext(), "获取用户信息失败");
                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
                }
            }
        });
    }

    public void onClickRecord(View view) {
        openActivity(EnforcementRecordActivity.class);
    }
}