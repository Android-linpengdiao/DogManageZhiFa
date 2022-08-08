package com.base;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.base.databinding.ItemTextBinding;
import com.base.view.OnClickListener;

public class TextAdapter extends BaseRecyclerAdapter<String, ItemTextBinding> {

    private int done = 0;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public TextAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_text;
    }

    @Override
    protected void onBindItem(ItemTextBinding binding, final String dataBean, final int position) {
        binding.titleView.setText(dataBean);
        binding.titleView.setTypeface(position == done ? Typeface.defaultFromStyle(Typeface.BOLD) : Typeface.defaultFromStyle(Typeface.NORMAL));
        binding.doneView.setVisibility(position == done ? View.VISIBLE : View.GONE);
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
