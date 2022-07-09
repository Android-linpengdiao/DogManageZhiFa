package com.dog.manage.zhifa.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.base.BaseApplication;
import com.base.UserInfo;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.MainActivity;
import com.dog.manage.zhifa.app.MyClickableSpan;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityLoginBinding;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import okhttp3.Call;
import okhttp3.Request;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private boolean privacyState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_login);
        setStatusBarDarkTheme(true);
        addActivity(this);

        privacyState = getIntent().getBooleanExtra("privacyState", true);
        binding.checkView.setSelected(privacyState);
        binding.checkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.checkView.setSelected(!binding.checkView.isSelected());
            }
        });

        String userText = "《用户协议》";
        String yinsiText = "《隐私政策》";
        String content = "登录代表同意《用户协议》《隐私政策》";
        SpannableString spannableString = new SpannableString(content);
        // 设置字体颜色
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#5E44FF")), content.indexOf(userText), content.indexOf(userText) + userText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#5E44FF")), content.indexOf(yinsiText), content.indexOf(yinsiText) + yinsiText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体大小
//          spannableString.setSpan(new AbsoluteSizeSpan(CommonUtil.sp2px(mContext, 14)), content.indexOf(stateText), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置点击
        spannableString.setSpan(new MyClickableSpan(LoginActivity.this, userText), content.indexOf(userText), content.indexOf(userText) + userText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new MyClickableSpan(LoginActivity.this, yinsiText), content.indexOf(yinsiText), content.indexOf(yinsiText) + yinsiText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.privacyView.setText(spannableString);
        binding.privacyView.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件

    }

    /**
     * username
     * string
     * 用户名
     * password
     * string
     * 密码
     * sysType
     * integer
     * 终端类型 1 执法 2 医院
     *
     * @param view
     */
    public void onClickLogin(View view) {
        String phone = binding.phoneEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();
        if (CommonUtil.isBlank(password)) {
            ToastUtils.showShort(getApplicationContext(), "请输入账号");
            return;
        }
        if (CommonUtil.isBlank(password)) {
            ToastUtils.showShort(getApplicationContext(), "请输入密码");
            return;
        }
        if (!binding.checkView.isSelected()) {
            ToastUtils.showShort(getApplicationContext(), "请同意服务条款");
            return;
        }
        SendRequest.userLogin(phone, password,
                new GenericsCallback<ResultClient<UserInfo>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        LoadingManager.showLoadingDialog(LoginActivity.this);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        LoadingManager.hideLoadingDialog(LoginActivity.this);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<UserInfo> response, int id) {
                        if (response.isSuccess()) {
                            if (response.getData() != null) {
                                BaseApplication.getInstance().setUserInfo(response.getData());
                                openActivity(MainActivity.class);
                                finishActivity(LoginActivity.class);
                                finish();
                            } else {
                                ToastUtils.showShort(getApplicationContext(), "获取用户信息失败");
                            }

                        } else {
                            ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取用户信息失败");
                        }
                    }
                });

    }
}