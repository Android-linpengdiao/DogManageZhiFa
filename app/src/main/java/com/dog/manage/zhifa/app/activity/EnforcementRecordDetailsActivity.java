package com.dog.manage.zhifa.app.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.base.utils.CommonUtil;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityEnforcementRecordDetailsBinding;
import com.dog.manage.zhifa.app.model.PunishRecord;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.Arrays;

import okhttp3.Call;

/**
 * 我的执法记录详情
 */
public class EnforcementRecordDetailsActivity extends BaseActivity {
    private ActivityEnforcementRecordDetailsBinding binding;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_enforcement_record_details);

        PunishRecord dataBean = (PunishRecord) getIntent().getSerializableExtra("dataBean");
        if (dataBean != null) {
            getIllegalDetails(dataBean);
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

    private void initView(PunishRecord dataBean) {
        binding.illegalTypeView.binding.itemContent.setText(
                (dataBean.getIllegalTypeId() == 1 ? "犬只伤人" :
                        dataBean.getIllegalTypeId() == 2 ? "犬吠" :
                                dataBean.getIllegalTypeId() == 3 ? "未牵狗绳" :
                                        dataBean.getIllegalTypeId() == 4 ? "其他" : "其他"));
        binding.illegalDescribeView.binding.itemContent.setText(dataBean.getIllegalDescribe());
        binding.illegalMeasureView.binding.itemContent.setText(dataBean.getIllegalMeasure());
        binding.illegalTimeView.binding.itemContent.setText(dataBean.getIllegalTime());

//        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
//        builder.color(R.color.transparent);
//        builder.size(CommonUtil.dip2px(this, 12));
//        binding.recyclerView.addItemDecoration(new GridItemDecoration(builder));
//        binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
//        VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext());
//        binding.recyclerView.setAdapter(videoAdapter);
//        videoAdapter.refreshData(Arrays.asList(dataBean.getIllegalFileUrl()));
//        videoAdapter.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view, Object object) {
//
//
//            }
//
//            @Override
//            public void onLongClick(View view, Object object) {
//
//            }
//        });
    }
}