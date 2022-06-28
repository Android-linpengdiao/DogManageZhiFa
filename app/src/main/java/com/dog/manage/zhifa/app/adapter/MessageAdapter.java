package com.dog.manage.zhifa.app.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ItemMessageBinding;
import com.dog.manage.zhifa.app.model.Message;

public class MessageAdapter extends BaseRecyclerAdapter<Message, ItemMessageBinding> {


    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MessageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_message;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void onBindItem(ItemMessageBinding binding, Message dataBean, int position) {
        binding.noticeTitleView.setText(dataBean.getNoticeTitle());
        binding.noticeContentView.setText(Html.fromHtml(dataBean.getNoticeContent()));
        binding.createTimeView.setText(dataBean.getCreateTime());
        binding.unreadMessageView.setVisibility(dataBean.getReadStatus() == 0 ? View.VISIBLE : View.GONE);
        GlideLoader.LoderMessageImage(mContext, dataBean.getImageUrl(), binding.coverView);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBean.setReadStatus(1);
                notifyDataSetChanged();
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean);
            }
        });
    }
}
