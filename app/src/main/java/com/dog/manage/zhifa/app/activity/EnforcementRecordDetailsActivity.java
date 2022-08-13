package com.dog.manage.zhifa.app.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.GlideLoader;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityEnforcementRecordDetailsBinding;
import com.dog.manage.zhifa.app.model.LicenceInfo;
import com.dog.manage.zhifa.app.model.PunishRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 我的执法记录详情
 */
public class EnforcementRecordDetailsActivity extends BaseActivity {
    private ActivityEnforcementRecordDetailsBinding binding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_enforcement_record_details);

        if (getIntent().hasExtra("title")){
            binding.topView.binding.itemTitle.setText(getIntent().getStringExtra("title"));
        }

        PunishRecord dataBean = (PunishRecord) getIntent().getSerializableExtra("dataBean");
        if (dataBean != null) {
            getIllegalDetails(dataBean);
            getLicenceInfo(dataBean.getDogLicenceNum());
        }

    }

    private void getIllegalDetails(PunishRecord dataBean) {
        SendRequest.getIllegalDetails(dataBean.getId(), new GenericsCallback<ResultClient<PunishRecord>>(new JsonGenericsSerializator()) {

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<PunishRecord> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    initView(response.getData());
                } else {
                    initView(dataBean);
                }
            }
        });
    }

    private void getLicenceInfo(String dogLicenceNum) {
        SendRequest.getLicenceInfo(null, dogLicenceNum,
                new GenericsCallback<ResultClient<LicenceInfo>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        LoadingManager.showLoadingDialog(EnforcementRecordDetailsActivity.this);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        LoadingManager.hideLoadingDialog(EnforcementRecordDetailsActivity.this);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<LicenceInfo> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            intiCertificate(response.getData());

                        } else {
                            binding.dogCertificateView.binding.itemContent.setText("查无此犬信息");
//                            ToastUtils.showShort(getApplicationContext(), "获取犬证信息失败");
                        }
                    }
                });
    }

    private void initView(PunishRecord dataBean) {
        binding.container.setVisibility(View.VISIBLE);

        binding.unitNameView.binding.itemContent.setText(dataBean.getUnitName());
        binding.userNameView.binding.itemContent.setText(dataBean.getUserName());
        binding.illegalTypeView.binding.itemContent.setText(
                (dataBean.getIllegalTypeId() == 1 ? "犬只伤人" :
                        dataBean.getIllegalTypeId() == 2 ? "禁养犬只" :
                                dataBean.getIllegalTypeId() == 3 ? "未牵狗绳" :
                                        dataBean.getIllegalTypeId() == 4 ? "其他" : "其他"));
        binding.illegalDescribeView.binding.itemContent.setText(dataBean.getIllegalDescribe());
        binding.illegalMeasureView.binding.itemContent.setText(dataBean.getIllegalMeasure());
        binding.illegalTimeView.binding.itemContent.setText(dataBean.getIllegalTime());

    }

    /**
     * 犬证信息
     *
     * @param licenceBean
     */
    private void intiCertificate(LicenceInfo licenceBean) {
        binding.dogCertificateContainer.setVisibility(View.VISIBLE);
        binding.idNumView.setText(licenceBean.getIdNum());
        binding.dogTypeView.setText(licenceBean.getDogType());
        binding.dogColorView.setText(licenceBean.getDogColor());
        binding.dogGenderView.setText(licenceBean.getDogGender() == 0 ? "雌性" : "雄性");
        binding.orgNameView.setText(licenceBean.getOrgName());
        binding.awardTimeView.setText(licenceBean.getAwardTime());
        binding.detailedAddressView.setText(licenceBean.getDetailedAddress());
//        GlideLoader.LoaderDogCover(EnforcementRecordDetailsActivity.this, "", binding.certificateCoverView, 5);
        try {
            List<String> dogPhotos = new Gson().fromJson(licenceBean.getDogPhoto(), new TypeToken<List<String>>() {
            }.getType());
            if (dogPhotos != null && dogPhotos.size() > 0)
                GlideLoader.LoderImage(EnforcementRecordDetailsActivity.this, dogPhotos.get(0), binding.certificateCoverView,5);
        } catch (Exception e) {
            e.getMessage();
        }

    }
}