package com.dog.manage.zhifa.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ItemFrameBinding;

import java.util.Arrays;

public class FrameItemAdapter extends BaseRecyclerAdapter<String, ItemFrameBinding> {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public FrameItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_frame;
    }

    @Override
    protected void onBindItem(ItemFrameBinding binding, String dataBean, int position) {

        if (getUserInfo().getSysType() == 1) {
            GlideLoader.LoderDrawable(mContext,
                    position == 0 ? R.drawable.icon01 :
                            position == 1 ? R.drawable.icon02 :
                                    position == 2 ? R.drawable.icon03 :
                                            position == 3 ? R.drawable.icon04 :
                                                    position == 4 ? R.drawable.icon05 :
                                                            R.drawable.icon05, binding.iconView);
        } else if (getUserInfo().getSysType() == 2) {
            GlideLoader.LoderDrawable(mContext,
                    position == 0 ? R.drawable.icon00 : R.drawable.icon05, binding.iconView);

        }

        binding.titleView.setText(dataBean);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
    }
}
