package com.dog.manage.zhifa.app.activity;

import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.adapter.MainPagerAdapter;
import com.dog.manage.zhifa.app.databinding.ActivityEnforcementDogInfoBinding;
import com.dog.manage.zhifa.app.fragment.EnforcementDogInfoFragment;
import com.dog.manage.zhifa.app.model.LicenceInfo;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 检查结果
 */
public class EnforcementDogInfoActivity extends BaseActivity implements EnforcementDogInfoFragment.OnCallEvents {

    private ActivityEnforcementDogInfoBinding binding;
    private String noseprint;
    private String dogLicenceNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_enforcement_dog_info);
        addActivity(this);

        noseprint = getIntent().getStringExtra("noseprint");
        dogLicenceNum = getIntent().getStringExtra("dogLicenceNum");
        getLicenceInfo();

    }


    private void getLicenceInfo() {
        SendRequest.getLicenceInfo(noseprint, dogLicenceNum,
                new GenericsCallback<ResultClient<LicenceInfo>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        LoadingManager.showLoadingDialog(EnforcementDogInfoActivity.this);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        LoadingManager.hideLoadingDialog(EnforcementDogInfoActivity.this);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<LicenceInfo> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            initView(response.getData());

                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });
    }

    private void initView(LicenceInfo data) {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.addFragment("犬证信息", EnforcementDogInfoFragment.getInstance(EnforcementDogInfoFragment.type_certificate, data));
        mainPagerAdapter.addFragment("免疫信息", EnforcementDogInfoFragment.getInstance(EnforcementDogInfoFragment.type_immune, data));
        binding.viewPager.setAdapter(mainPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(4);
        binding.viewPager.setCurrentItem(0);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.dogCertificateView.setBackgroundColor(position == 0 ? Color.parseColor("#FAFAFA") : Color.parseColor("#ffffff"));
                binding.viewLayout.setBackgroundColor(position == 0 ? Color.parseColor("#ffffff") : Color.parseColor("#FAFAFA"));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("dogLicenceNum", dogLicenceNum);
        openActivity(EnforcementSubmitActivity.class, bundle);

    }

    @Override
    public void onSubmit(Map<String, String> map) {
        openActivity(EnforcementSubmitActivity.class);

    }
}