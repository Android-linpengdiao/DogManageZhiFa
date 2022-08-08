package com.dog.manage.zhifa.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ItemAreaSelectBinding;
import com.dog.manage.zhifa.app.model.CommunityBean;

public class CommunitySelectAdapter extends BaseRecyclerAdapter<CommunityBean, ItemAreaSelectBinding> {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private int select = 0;

    public int getSelect() {
        return select;
    }

    public CommunitySelectAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_area_select;
    }

    @Override
    protected void onBindItem(ItemAreaSelectBinding binding, CommunityBean dataBean, int position) {
        binding.titleView.setText(dataBean.getCommunityName());
        binding.selectedView.setVisibility(select == position ? View.VISIBLE : View.GONE);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                notifyDataSetChanged();
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean);
            }
        });
    }
}
