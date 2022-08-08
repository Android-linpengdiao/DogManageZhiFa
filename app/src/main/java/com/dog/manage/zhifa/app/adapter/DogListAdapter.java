package com.dog.manage.zhifa.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.databinding.ItemTextBinding;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.model.Dog;

public class DogListAdapter extends BaseRecyclerAdapter<Dog, ItemTextBinding> {

    private int done = 0;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public DogListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return com.base.R.layout.item_text;
    }


    @Override
    protected void onBindItem(ItemTextBinding binding, Dog dataBean, int position) {
        binding.titleView.setText(dataBean.getDogType() + (dataBean.getIdNum() != null ? "  犬证-" + dataBean.getIdNum() : ""));
        binding.titleView.setTypeface(position == done ? Typeface.defaultFromStyle(Typeface.BOLD) : Typeface.defaultFromStyle(Typeface.NORMAL));
//        binding.doneView.setVisibility(position == done ? View.VISIBLE : View.GONE);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done = position;
                notifyDataSetChanged();
                if (onClickListener != null) {
                    onClickListener.onClick(view, dataBean);
                }
            }
        });
    }
}
