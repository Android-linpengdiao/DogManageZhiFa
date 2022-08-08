package com.dog.manage.zhifa.app.activity;

import android.os.Bundle;
import android.view.View;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityUpdatePhoneBinding;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.Call;
import okhttp3.Request;

public class UpdatePhoneActivity extends BaseActivity {

    private ActivityUpdatePhoneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_update_phone);
        addActivity(this);
    }

    public void onClickConfirm(View view) {
        String oldPassword = binding.passwordEditText.getText().toString().trim();
        String newPassword = binding.newPasswordEditText.getText().toString().trim();
        if (CommonUtil.isBlank(oldPassword)) {
            ToastUtils.showShort(getApplicationContext(), "请输入旧密码");
            return;
        }
        if (CommonUtil.isBlank(newPassword)) {
            ToastUtils.showShort(getApplicationContext(), "请输入新密码");
            return;
        }
        SendRequest.updatePwd(oldPassword, newPassword, new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(UpdatePhoneActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(UpdatePhoneActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(BaseData response, int id) {
                if (response.isSuccess()) {
                    ToastUtils.showShort(getApplicationContext(), "修改成功");
                    finish();
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }

            }
        });
    }
}