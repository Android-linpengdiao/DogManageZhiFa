package com.dog.manage.zhifa.app.activity;

import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.adapter.MainPagerAdapter;
import com.dog.manage.zhifa.app.databinding.ActivityEnforcementDogInfoBinding;
import com.dog.manage.zhifa.app.fragment.EnforcementDogInfoFragment;
import com.dog.manage.zhifa.app.model.Dog;
import com.dog.manage.zhifa.app.model.LicenceInfo;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 检查结果
 */
public class EnforcementDogInfoActivity extends BaseActivity implements EnforcementDogInfoFragment.OnCallEvents {

    private ActivityEnforcementDogInfoBinding binding;

    public static final int type_noseprint = 0;//
    public static final int type_orgName = 1;//
    public static final int type_userPhone = 2;//
    public static final int type_idNum = 3;//
    private int type;
    private String dogLicenceNum;

    private List<Dog> dogList = new ArrayList<>();
    private Dog dogDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_enforcement_dog_info);
        addActivity(this);

        initView();

        type = getIntent().getIntExtra("type", 0);
        if (type == type_noseprint) {
            String noseprint = getIntent().getStringExtra("noseprint");
            getLicenceInfo(noseprint);

        } else if (type == type_orgName || type == type_userPhone || type == type_idNum) {
            binding.dogCertificateView.setVisibility(View.VISIBLE);
            String content = getIntent().getStringExtra("content");
            getDogByUser(content);

        }

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogList.size() == 0) {
                    ToastUtils.showShort(getApplicationContext(), "暂无犬只");
                    return;
                }
                onClickDogCertificate(EnforcementDogInfoActivity.this,
                        dogList, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()),
                        new OnClickListener() {
                            @Override
                            public void onClick(View view, Object object) {
                                dogDetail = (Dog) object;
                                binding.dogCertificateView.binding.itemContent.setText(dogDetail.getDogType());
                                getLicenceInfo(dogDetail.getNoseprint());
                            }

                            @Override
                            public void onLongClick(View view, Object object) {

                            }
                        });
            }
        });

    }

    private void getDogByUser(String content) {
        SendRequest.getDogByUser(type == type_orgName ? content : null, type == type_userPhone ? content : null, type == type_idNum ? content : null,
                new GenericsCallback<ResultClient<List<Dog>>>(new JsonGenericsSerializator()) {

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
                    public void onResponse(ResultClient<List<Dog>> response, int id) {
                        if (response.isSuccess()) {
                            if (response.getData() != null) {
                                dogList = response.getData();
                                if (response.getData().size() > 0) {
                                    dogDetail = response.getData().get(0);
                                    binding.dogCertificateView.binding.itemContent.setText(dogDetail.getDogType());
                                    getLicenceInfo(dogDetail.getNoseprint());
                                }
                            } else {
                                ToastUtils.showShort(getApplicationContext(), "获取犬证信息失败");
                            }

                        } else {
                            ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取信息失败");
                        }
                    }
                });
    }

    private void getLicenceInfo(String noseprint) {
        SendRequest.getLicenceInfo(noseprint, null,
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
                        if (response.isSuccess()) {
                            if (response.getData() != null) {
                                binding.viewPager.setVisibility(View.VISIBLE);
                                binding.editContainer.setVisibility(View.INVISIBLE);
                                dogLicenceNum = response.getData().getIdNum();
                                certificateFragment.intiCertificate(response.getData());
                                immuneFragment.intiImmune(response.getData().getImmuneLicenceId() != null ? response.getData().getImmuneLicenceId() : 0);

                            } else {
                                ToastUtils.showShort(getApplicationContext(), "获取犬证信息失败");
                            }

                        } else if (response.getCode() == -1) {
                            binding.editContainer.setVisibility(View.VISIBLE);
                            binding.viewPager.setVisibility(View.INVISIBLE);
                            ToastUtils.showShort(getApplicationContext(), "获取犬证信息失败");

                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMsg());
                        }
                    }
                });
    }

    private EnforcementDogInfoFragment certificateFragment;
    private EnforcementDogInfoFragment immuneFragment;

    private void initView() {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        certificateFragment = EnforcementDogInfoFragment.getInstance(EnforcementDogInfoFragment.type_certificate);
        immuneFragment = EnforcementDogInfoFragment.getInstance(EnforcementDogInfoFragment.type_immune);
        mainPagerAdapter.addFragment("犬证信息", certificateFragment);
        mainPagerAdapter.addFragment("免疫信息", immuneFragment);
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

    }
}