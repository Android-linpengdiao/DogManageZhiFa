package com.dog.manage.zhifa.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ItemEnforcementRecordBinding;
import com.dog.manage.zhifa.app.model.PunishRecord;

public class EnforcementRecordAdapter extends BaseRecyclerAdapter<PunishRecord, ItemEnforcementRecordBinding> {


    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private String searchContent;

    public void setContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public EnforcementRecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_enforcement_record;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void onBindItem(ItemEnforcementRecordBinding binding, PunishRecord dataBean, int position) {
        //处罚类型 1 犬只伤人 2 禁养犬只 3 未牵狗绳 4 其他
        binding.titleView.setText("处罚记录-" +
                (dataBean.getIllegalTypeId() == 1 ? "犬只伤人" :
                        dataBean.getIllegalTypeId() == 2 ? "禁养犬只" :
                                dataBean.getIllegalTypeId() == 3 ? "未牵狗绳" :
                                        dataBean.getIllegalTypeId() == 4 ? "其他" : "其他"));
        binding.illegalTypeView.setText("违法类型: " +
                (dataBean.getIllegalTypeId() == 1 ? "犬只伤人" :
                        dataBean.getIllegalTypeId() == 2 ? "禁养犬只" :
                                dataBean.getIllegalTypeId() == 3 ? "未牵狗绳" :
                                        dataBean.getIllegalTypeId() == 4 ? "其他" : "其他"));
        binding.illegalMeasureView.setText("处罚措施: " + dataBean.getIllegalMeasure());
        binding.illegalTimeView.setText("处罚时间: " + dataBean.getIllegalTime());
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean);
            }
        });
    }
}
