package com.dog.manage.zhifa.app.activity;

import android.os.Bundle;

import com.base.utils.GlideLoader;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityEnforcementRecordDetailsBinding;

/**
 * 我的执法记录详情
 */
public class EnforcementRecordDetailsActivity extends BaseActivity {
    private ActivityEnforcementRecordDetailsBinding binding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_enforcement_record_details);

        GlideLoader.LoderImage(EnforcementRecordDetailsActivity.this,
                "https://img0.baidu.com/it/u=3282676189,411395798&fm=253&fmt=auto&app=138&f=JPEG?w=564&h=500",
                binding.certificateCoverView, 8);

    }
}