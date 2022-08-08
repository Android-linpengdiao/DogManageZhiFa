package com.dog.manage.zhifa.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.activity.AdvertiseActivity;
import com.dog.manage.zhifa.app.activity.BaseActivity;
import com.dog.manage.zhifa.app.activity.CameraActivity;
import com.dog.manage.zhifa.app.activity.EnforcementActivity;
import com.dog.manage.zhifa.app.activity.EnforcementDogInfoActivity;
import com.dog.manage.zhifa.app.activity.EnforcementRecordActivity;
import com.dog.manage.zhifa.app.activity.EnforcementSubmitActivity;
import com.dog.manage.zhifa.app.activity.ImmuneSubmitActivity;
import com.dog.manage.zhifa.app.activity.LoginActivity;
import com.dog.manage.zhifa.app.activity.MessageActivity;
import com.dog.manage.zhifa.app.activity.SettingsActivity;
import com.dog.manage.zhifa.app.activity.UserHomeActivity;
import com.dog.manage.zhifa.app.adapter.FrameItemAdapter;
import com.dog.manage.zhifa.app.databinding.ActivityMainBinding;
import com.dog.manage.zhifa.app.model.BannerBean;
import com.dog.manage.zhifa.app.model.PoliciesBean;
import com.dog.manage.zhifa.app.utils.GlideImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.xw.banner.BannerConfig;
import com.xw.banner.Transformer;
import com.xw.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_main);
        setStatusBarHeight();
        addActivity(this);

        initView();
        noticeList();

    }

    private void initView() {

        intBanner();

        binding.frameItemRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        binding.frameItemRecyclerView.setNestedScrollingEnabled(false);
        FrameItemAdapter frameItemAdapter = new FrameItemAdapter(getApplicationContext());
        binding.frameItemRecyclerView.setAdapter(frameItemAdapter);
        if (getUserInfo().getSysType() == 1) {
            frameItemAdapter.refreshData(Arrays.asList("执法记录", "历史记录", "通知公告", "个人中心", "设置"));
        } else if (getUserInfo().getSysType() == 2) {
            frameItemAdapter.refreshData(Arrays.asList("疫苗注射", "设置"));
        }
        frameItemAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                int position = (int) object;
                if (position == 0) {
                    if (checkUserRank(getApplicationContext(), true)) {
                        if (getUserInfo().getSysType() == 1) {
                            openActivity(EnforcementActivity.class);

                        } else if (getUserInfo().getSysType() == 2) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString("noseprint", "8c3c9249-f4e2-11ec-25xGihUUR");
//                            bundle.putString("noseprint", "23325059-b2c1-11eb-1Vu7hqwN635");
//                            bundle.putString("noseprint", "23325059-b2c1-11eb-1Vu7hqwN6M6");
//                            openActivity(ImmuneSubmitActivity.class, bundle);

                            if (checkPermissions(PermissionUtils.CAMERA, 100)) {
                                Bundle bundle = new Bundle();
                                bundle.putInt("type", CameraActivity.type_petArchives);
                                openActivity(CameraActivity.class, bundle, request_petArchive);
                            }

                        }
                    }

                } else if (position == 1) {
                    if (checkUserRank(getApplicationContext(), true)) {
                        if (getUserInfo().getSysType() == 1) {
                            Bundle bundle = new Bundle();
                            bundle.putString("title", "历史记录");
                            openActivity(EnforcementRecordActivity.class, bundle);

                        } else if (getUserInfo().getSysType() == 2) {
                            openActivity(SettingsActivity.class);

                        }
                    }

                } else if (position == 2) {
                    openActivity(MessageActivity.class);

                } else if (position == 3) {
                    if (checkUserRank(getApplicationContext(), true)) {
                        openActivity(UserHomeActivity.class);
                    }

                } else if (position == 4) {
                    openActivity(SettingsActivity.class);

                }

            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
    }

    private final int request_petArchive = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_petArchive:
                    if (data != null) {
                        String petId = data.getStringExtra("petId");
                        Bundle bundle = new Bundle();
//                        bundle.putString("noseprint", "8c3c9249-f4e2-11ec-25xGihUUR");
//                        bundle.putString("noseprint", "23325059-b2c1-11eb-1Vu7hqwN635");
//                        bundle.putString("noseprint", "23325059-b2c1-11eb-1Vu7hqwN6M6");
                        bundle.putString("noseprint", petId);
                        openActivity(ImmuneSubmitActivity.class, bundle);

                    }
                    break;
            }
        }
    }

    private void intBanner() {
        binding.mainBanner.setImageLoader(new GlideImageLoader(0));
        binding.mainBanner.isAutoPlay(true);
        binding.mainBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        binding.mainBanner.setBannerAnimation(Transformer.Default);
        binding.mainBanner.setIndicatorGravity(BannerConfig.CENTER);
        binding.mainBanner.setDelayTime(5000);
        binding.mainBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });

        binding.banner.setImageLoader(new GlideImageLoader(8));
        binding.banner.isAutoPlay(true);
        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        binding.banner.setBannerAnimation(Transformer.Default);
        binding.banner.setIndicatorGravity(BannerConfig.CENTER);
        binding.banner.setDelayTime(5000);
//        binding.banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
////                openActivity(AdvertiseActivity.class);
//            }
//        });

        bannerInfoList();
        getForbiddenById();
    }

    public void bannerInfoList() {
        SendRequest.bannerInfoList(0, 20, new GenericsCallback<Pager<BannerBean>>(new JsonGenericsSerializator()) {


            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Pager<BannerBean> response, int id) {
                if (response != null && response.getRows() != null && response.getRows().size() > 0) {
                    List<String> imageList = new ArrayList<>();
                    for (BannerBean bannerBean : response.getRows()) {
                        imageList.add(bannerBean.getImageUrl());
                    }
                    binding.mainBanner.setImages(imageList).start();
                } else {
                    binding.mainBanner.setImages(Arrays.asList("")).start();
                }
            }
        });

    }

    public void getForbiddenById() {
        SendRequest.getForbiddenById(new GenericsCallback<ResultClient<PoliciesBean>>(new JsonGenericsSerializator()) {


            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<PoliciesBean> response, int id) {
                if (response != null && response.getData() != null) {
                    binding.banner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("policiesBean", response.getData());
                            openActivity(AdvertiseActivity.class, bundle);
                        }
                    });
                    List<String> imageList = new ArrayList<>();
                    imageList.add(response.getData().getImageUrl());
                    binding.banner.setImages(imageList).start();
//                    try {
//                        List<String> imageList = new Gson().fromJson(response.getData().getImageUrl(), new TypeToken<List<String>>() {
//                        }.getType());
//                        binding.banner.setImages(imageList).start();
//                    } catch (Exception e) {
//                        e.getMessage();
//                    }
                }
            }
        });

    }

    public void noticeList() {
        SendRequest.noticeList(1, 10,
                new GenericsCallback<Pager<PoliciesBean>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(Pager<PoliciesBean> response, int id) {
                        if (response != null && response.getRows() != null && response.getRows().size() > 0) {
                            StringBuffer buffer = new StringBuffer();
                            for (PoliciesBean policiesBean : response.getRows()) {
                                buffer.append(policiesBean.getNoticeTitle()).append("       ");
                            }
                            binding.noticeView.setText(buffer);
                        }
                    }
                });

    }

    public void onClickMessage(View view) {
        if (checkUserRank(getApplicationContext(), true)) {
            openActivity(MessageActivity.class);
//            openActivity(UpdateDogOwnerInfoActivity.class);
        }
    }

    public void onClickUser(View view) {
        if (checkUserRank(getApplicationContext(), true)) {
//            openActivity(UserHomeActivity.class);
        }

    }

    private long lastTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - lastTime >= 3000) {
                lastTime = System.currentTimeMillis();
                ToastUtils.showShort(MainActivity.this, "再按一次退出应用");
                return true;
            }
//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
        }
        return super.onKeyDown(keyCode, event);
    }
}