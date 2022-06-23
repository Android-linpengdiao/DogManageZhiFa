package com.dog.manage.zhifa.app.activity;


import android.os.Bundle;
import android.view.View;

import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityUserHomeBinding;

public class UserHomeActivity extends BaseActivity {
    private ActivityUserHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_user_home);
    }

    public void onClickRecord(View view) {
        openActivity(EnforcementRecordActivity.class);
    }
}