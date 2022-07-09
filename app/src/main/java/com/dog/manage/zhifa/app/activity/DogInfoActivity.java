package com.dog.manage.zhifa.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.GlideLoader;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityDogInfoBinding;
import com.dog.manage.zhifa.app.model.DogDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class DogInfoActivity extends BaseActivity {

    private ActivityDogInfoBinding binding;
    private int dogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_info);

        dogId = getIntent().getIntExtra("dogId", 0);

        getDogById();
    }

    /**
     * 犬证 获取犬只详情信息
     */
    private void getDogById() {
        SendRequest.getDogById(dogId, new GenericsCallback<ResultClient<DogDetail>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(DogInfoActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(DogInfoActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<DogDetail> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    DogDetail dogDetail = response.getData();

                    binding.dogNameView.binding.itemContent.setText(dogDetail.getDogName());
                    binding.dogColorView.binding.itemContent.setText(dogDetail.getDogColor());
                    binding.dogAgeView.binding.itemContent.setText(CommonUtil.getDogAge(dogDetail.getDogAge()));

                    //0-雌性 1-雄性
                    if (dogDetail.getDogGender() == 0) {
                        binding.radioButtonMale.setChecked(true);
                        binding.radioButtonFemale.setVisibility(View.GONE);
                    } else {
                        binding.radioButtonFemale.setChecked(true);
                        binding.radioButtonMale.setVisibility(View.GONE);
                    }

                    //是否绝育;0：否 1：是
                    if (dogDetail.getSterilization() == 0) {
                        binding.radioButtonSterilization0.setChecked(true);
                        binding.radioButtonSterilization1.setVisibility(View.GONE);
                        binding.sterilizationProveContainer.setVisibility(View.GONE);
                    } else {
                        binding.radioButtonSterilization1.setChecked(true);
                        binding.radioButtonSterilization0.setVisibility(View.GONE);
                        GlideLoader.LoderImage(DogInfoActivity.this, dogDetail.getSterilizationProve(), binding.sterilizationProveView,6);
                    }

                    try {
                        List<String> idPhotos = new Gson().fromJson(dogDetail.getDogPhoto(), new TypeToken<List<String>>() {
                        }.getType());
                        if (idPhotos.size() > 0) {
                            GlideLoader.LoderImage(DogInfoActivity.this, idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.leftFaceView,6);
                        }
                        if (idPhotos.size() > 1) {
                            GlideLoader.LoderImage(DogInfoActivity.this, idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.centerFaceView,6);
                        }
                        if (idPhotos.size() > 2) {
                            GlideLoader.LoderImage(DogInfoActivity.this, idPhotos.size() > 2 ? idPhotos.get(2) : "", binding.rightFaceView,6);
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                    binding.petTypeView.binding.itemContent.setText(dogDetail.getDogType());
                    binding.createPetArchivesView.binding.itemContent.setText("已完成采集");

                } else {
                    ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                }
            }
        });
    }
}