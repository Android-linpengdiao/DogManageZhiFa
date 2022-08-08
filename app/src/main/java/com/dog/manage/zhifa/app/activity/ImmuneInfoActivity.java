package com.dog.manage.zhifa.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.base.manager.LoadingManager;
import com.base.utils.GlideLoader;
import com.base.utils.TimeUtils;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityImmuneInfoBinding;
import com.dog.manage.zhifa.app.model.ImmuneDetail;
import com.dog.manage.zhifa.app.model.LicenceInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class ImmuneInfoActivity extends BaseActivity {

    private ActivityImmuneInfoBinding binding;
    private LicenceInfo licenceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_immune_info);

        int immuneId = getIntent().getIntExtra("immuneId", 0);
        intiImmune(immuneId);
    }

    /**
     * 免疫信息
     *
     * @param immuneId
     */
    public void intiImmune(int immuneId) {
        SendRequest.getImmuneDetail(immuneId, new GenericsCallback<ResultClient<ImmuneDetail>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<ImmuneDetail> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    immuneView(response.getData());
                } else {
                    ToastUtils.showShort(ImmuneInfoActivity.this, "获取免疫证信息失败");
                }

            }
        });
    }

    private void immuneView(ImmuneDetail immuneDetail) {
        binding.immuneContainer.setVisibility(View.VISIBLE);
        binding.immuneIdNumView.setText("免疫证明编号：" + immuneDetail.getIdNum());
        binding.immuneDogNameView.setText(immuneDetail.getDogName());
        if (immuneDetail.getDogGender() != null)
            binding.immuneDogGenderView.setText(immuneDetail.getDogGender() == 0 ? "雌性" : "雄性");
        binding.immuneDogColorView.setText(immuneDetail.getDogColor());
        binding.immuneDogTypeView.setText(immuneDetail.getDogType());
        binding.hospitalNameView.setText(immuneDetail.getHospitalName());

        binding.immuneNameView.setText(immuneDetail.getImmuneName());
        binding.immuneBatchView.setText(immuneDetail.getImmuneBatch());
        binding.immuneFactoryView.setText(immuneDetail.getImmuneFactory());
        binding.immuneDataView.setText(immuneDetail.getImmuneData());
        binding.immuneNumView.setText(immuneDetail.getImmuneNum());
        binding.immuneUserView.setText(immuneDetail.getImmuneUser());
        binding.nextImmuneDataView.setText(immuneDetail.getNextImmuneData());
        binding.streetNameView.setText(immuneDetail.getStreetName());
        binding.detailedAddressView.setText(immuneDetail.getDetailedAddress());

        try {
            List<String> dogPhotos = new Gson().fromJson(immuneDetail.getDogPhoto(), new TypeToken<List<String>>() {
            }.getType());
            if (dogPhotos != null && dogPhotos.size() > 0)
                GlideLoader.LoderImage(ImmuneInfoActivity.this, dogPhotos.get(0), binding.immuneCoverView,5);
        } catch (Exception e) {
            e.getMessage();
        }

//        if (immuneDetail.getNextImmuneData() != null) {
//            long surplusDate = 365 - (System.currentTimeMillis() - TimeUtils.getTimeExamined(immuneDetail.getNextImmuneData())) / (24 * 60 * 60 * 1000);
//            binding.immuneStatusView.setText(surplusDate > 0 ? "免疫证有效" : ("免疫证逾期" + Math.abs(surplusDate) + "天"));
//            binding.immuneStatusView.setTextColor(surplusDate > 0 ? Color.parseColor("#273154") : Color.parseColor("#FF2020"));
//        }

    }
}