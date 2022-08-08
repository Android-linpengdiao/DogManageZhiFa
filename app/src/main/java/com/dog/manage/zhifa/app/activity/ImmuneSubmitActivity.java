package com.dog.manage.zhifa.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityImmuneSubmitBinding;
import com.dog.manage.zhifa.app.model.DogByNoseprint;
import com.dog.manage.zhifa.app.model.LicenceInfo;
import com.dog.manage.zhifa.app.model.UpdateImmuneLicenceLog;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

public class ImmuneSubmitActivity extends BaseActivity {

    private ActivityImmuneSubmitBinding binding;
    private DogByNoseprint dogByNoseprint;
    private String noseprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_immune_submit);

        binding.dogOwnerInfoView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogByNoseprint == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("userId", dogByNoseprint.getDogUserId());
                bundle.putInt("dogId", dogByNoseprint.getDogId());
                openActivity(DogUserActivity.class, bundle);
            }
        });
        binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogByNoseprint == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", dogByNoseprint.getDogId());
                openActivity(DogInfoActivity.class, bundle);
            }
        });

        noseprint = getIntent().getStringExtra("noseprint");
        getDogByNoseprint(noseprint);
    }

    private void getDogByNoseprint(String noseprint) {
        SendRequest.getDogByNoseprint(noseprint,
                new GenericsCallback<ResultClient<DogByNoseprint>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        LoadingManager.showLoadingDialog(ImmuneSubmitActivity.this);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        LoadingManager.hideLoadingDialog(ImmuneSubmitActivity.this);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<DogByNoseprint> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            intiCertificate(response.getData());
                        } else {
                            binding.emptyView.setText(!CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取信息失败");
                            binding.emptyView.setVisibility(View.VISIBLE);
//                            ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取信息失败");
                        }
                    }
                });
    }

    private void intiCertificate(DogByNoseprint dogByNoseprint) {
        this.dogByNoseprint = dogByNoseprint;

        binding.container.setVisibility(View.VISIBLE);
        binding.bottomView.setVisibility(View.VISIBLE);

        binding.contentView.setText(dogByNoseprint.getDogType() + "-" + CommonUtil.getDogAge(dogByNoseprint.getDogAge()));
        binding.createTimeView.setText(dogByNoseprint.getCreatedTime());
        //疫苗状态 1 已预约（未注册） 2 已办结
        binding.immuneStatusView.setText(dogByNoseprint.getImmuneStatus() == 1 ? "已预约" : dogByNoseprint.getImmuneStatus() == 2 ? "已办结" : "");

        binding.dogOwnerInfoView.binding.itemContent.setText(dogByNoseprint.getDogUserName());
        binding.dogDetailsView.binding.itemContent.setText(dogByNoseprint.getDogType());

        binding.acceptUnitView.binding.itemContent.setText(dogByNoseprint.getAcceptUnit());
        binding.immuneUserView.binding.itemContent.setText(dogByNoseprint.getImmuneUser());

    }

    /**
     * immuneName
     * string
     * immuneName
     * immuneBatch
     * string
     * 疫苗批次号
     * immuneFactory
     * string
     * immuneFactory
     * immuneNum
     * string
     * 免疫标识号
     *
     * @param view
     */

    public void onClickConfirm(View view) {
        if (dogByNoseprint == null) {
            return;
        }

        Map<String, String> paramsMap = new HashMap<>();

        String immuneName = binding.immuneNameView.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(immuneName)) {
            ToastUtils.showShort(getApplicationContext(), "请输入疫苗名称");
            return;
        }
        String immuneBatch = binding.immuneBatchView.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(immuneBatch)) {
            ToastUtils.showShort(getApplicationContext(), "请输入疫苗批号");
            return;
        }
        String immuneFactory = binding.immuneFactoryView.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(immuneFactory)) {
            ToastUtils.showShort(getApplicationContext(), "请输入疫苗厂家");
            return;
        }
        String immuneNum = binding.immuneNumView.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(immuneNum)) {
            ToastUtils.showShort(getApplicationContext(), "请输入免疫标识号");
            return;
        }

        paramsMap.put("id", dogByNoseprint.getImmuneLicenceLogId() + "");
        paramsMap.put("immuneName", immuneName);
        paramsMap.put("immuneBatch", immuneBatch);
        paramsMap.put("immuneFactory", immuneFactory);
        paramsMap.put("immuneNum", immuneNum);

        SendRequest.updateImmuneLicenceLog(paramsMap, new GenericsCallback<ResultClient<UpdateImmuneLicenceLog>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(ImmuneSubmitActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(ImmuneSubmitActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(ResultClient<UpdateImmuneLicenceLog> response, int id) {
                if (response.isSuccess()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("immuneId", !CommonUtil.isBlank(response.getData()) ? response.getData().getImmuneLicenceId() : 0);
                    openActivity(ImmuneInfoActivity.class, bundle);
                    ToastUtils.showShort(ImmuneSubmitActivity.this, "提交成功");
                    finish();

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());

                }

            }
        });
    }
}