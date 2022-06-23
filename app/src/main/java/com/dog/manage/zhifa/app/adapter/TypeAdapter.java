package com.dog.manage.zhifa.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ItemTypeBinding;
import com.dog.manage.zhifa.app.model.TypeBean;

public class TypeAdapter extends BaseRecyclerAdapter<TypeBean, ItemTypeBinding> {

    private int select = -1;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public TypeAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_type;
    }

    @Override
    protected void onBindItem(ItemTypeBinding binding, TypeBean dataBean, int position) {
        binding.titleView.setText(dataBean.getTitle());
        binding.titleView.setTextColor(select == position ? Color.parseColor("#FFFFFF") : Color.parseColor("#999999"));
        binding.titleView.setBackgroundResource(select == position ? R.drawable.button_theme : R.drawable.button_gray);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                notifyDataSetChanged();
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean.getId());
            }
        });
    }
}
