package com.dog.manage.zhifa.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ItemEnforrcementImageBinding;


public class EnforcementImageAdapter extends BaseRecyclerAdapter<String, ItemEnforrcementImageBinding> {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public EnforcementImageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) { //应该在此根viewType 选择不同布局的,但设计图上给的差距不大.就整合成一个布局了
        return R.layout.item_enforrcement_image;
    }

    private static final String TAG = "ReleaseImageAdapter";

    @Override
    protected void onBindItem(final ItemEnforrcementImageBinding binding, String url, int position) {
        Log.i(TAG, "onBindItem: position = " + position);
        Log.i(TAG, "onBindItem: url = " + url);
        if (position == mList.size() - 1) {
            binding.updateContainer.setVisibility(View.VISIBLE);
            binding.deleteImageView.setVisibility(View.GONE);
            binding.playView.setVisibility(View.GONE);
            GlideLoader.LoderImage(mContext, null, binding.imageImageView, 6);
        } else {
            binding.updateContainer.setVisibility(View.GONE);
            binding.deleteImageView.setVisibility(View.VISIBLE);
            binding.playView.setVisibility(url != null && url.endsWith("mp4") ? View.VISIBLE : View.GONE);
            GlideLoader.LoderImage(mContext, url, binding.imageImageView, 6);
        }
        binding.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
        binding.viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(view, position);
                }
            }
        });

    }
}
