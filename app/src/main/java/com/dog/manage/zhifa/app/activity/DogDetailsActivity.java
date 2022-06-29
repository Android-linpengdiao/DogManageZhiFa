package com.dog.manage.zhifa.app.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.GlideLoader;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityDogDetailsBinding;
import com.dog.manage.zhifa.app.model.DogDetail;
import com.dog.manage.zhifa.app.utils.GlideImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.xw.banner.BannerConfig;
import com.xw.banner.Transformer;
import com.xw.banner.listener.OnBannerListener;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class DogDetailsActivity extends BaseActivity {

    private ActivityDogDetailsBinding binding;
    private int dogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_details);
        addActivity(this);

        dogId = getIntent().getIntExtra("dogId", 8);

        intBanner();
        getDogById();
    }

    private void intBanner() {
        binding.banner.setImageLoader(new GlideImageLoader(0));
        binding.banner.isAutoPlay(false);
        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        binding.banner.setBannerAnimation(Transformer.Default);
        binding.banner.setIndicatorGravity(BannerConfig.CENTER);
        binding.banner.setDelayTime(5000);
        binding.banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
    }

    /**
     * 犬证 获取犬只详情信息
     */
    private void getDogById() {
        SendRequest.getDogById(dogId, new GenericsCallback<ResultClient<DogDetail>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(DogDetailsActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(DogDetailsActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<DogDetail> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    binding.container.setVisibility(View.VISIBLE);
                    DogDetail dogDetail = response.getData();
                    binding.dogNameView.setText(dogDetail.getDogName() + "|" + dogDetail.getDogColor() + "|" + CommonUtil.getDogAge(dogDetail.getDogAge()));
                    binding.leaveCenterView.setText(dogDetail.getLeaveCenter());
                    binding.centerAddressView.setText(dogDetail.getCenterAddress());
                    binding.phoneView.setText(dogDetail.getPhone());
                    binding.idNumView.setText("犬只编号：" + dogDetail.getIdNum());
                    binding.dogGenderView.setText("犬只性别：" + (dogDetail.getDogGender() == 0 ? "雌性" : "雄性"));
                    binding.dogShapeView.setText("犬只体型：" + (dogDetail.getDogShape() == 0 ? "小型" : "中型"));
                    if (dogDetail.getImmuneStatus() == 1) {
                        binding.immuneStatus.setText("免疫情况：已免疫" + "，" + dogDetail.getImmuneExprie() + "到期");
                    } else {
                        binding.immuneStatus.setText("免疫情况：未免疫");
                    }
                    binding.sterilizationView.setText("绝育情况：" + (dogDetail.getSterilization() == 0 ? "未绝育" : "已绝育"));
//                    GlideLoader.LoderImage(DogDetailsActivity.this, dogDetail.getDogPhoto(), binding.coverView);
                    try {
                        Log.i(TAG, "onResponse: "+dogDetail.getDogPhoto());
                        List<String> imageList = new Gson().fromJson(dogDetail.getDogPhoto(), new TypeToken<List<String>>() {
                        }.getType());
                        GlideLoader.LoderImage(DogDetailsActivity.this, imageList.get(0), binding.coverView);
//                        binding.banner.setImages(imageList).start();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                }
            }
        });
    }
}