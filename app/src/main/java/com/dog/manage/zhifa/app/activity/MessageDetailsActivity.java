package com.dog.manage.zhifa.app.activity;

import android.os.Bundle;

import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityMessageDetailsBinding;

public class MessageDetailsActivity extends BaseActivity {

    private ActivityMessageDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_message_details);
        addActivity(this);
    }
}