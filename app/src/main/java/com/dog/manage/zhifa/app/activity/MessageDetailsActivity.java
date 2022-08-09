package com.dog.manage.zhifa.app.activity;

import android.os.Bundle;
import android.text.Html;

import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityMessageDetailsBinding;
import com.dog.manage.zhifa.app.model.Message;
import com.dog.manage.zhifa.app.model.PoliciesBean;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import okhttp3.Call;

public class MessageDetailsActivity extends BaseActivity {

    private ActivityMessageDetailsBinding binding;
    private Message dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_message_details);
        addActivity(this);
        dataBean = (Message) getIntent().getSerializableExtra("dataBean");
        if (dataBean != null) {
            loadData(dataBean.getNoticeId());

        }
    }

    public void loadData(Integer noticeId) {
        SendRequest.getSysAnnounceById(noticeId,
                new GenericsCallback<ResultClient<PoliciesBean>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(ResultClient<PoliciesBean> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
//                            binding.noticeTitleView.setText(dataBean.getNoticeTitle());
                            binding.noticeTimeView.setText(dataBean.getCreateTime());
                            binding.noticeContentView.setText(Html.fromHtml(dataBean.getNoticeContent()));
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });

    }
}