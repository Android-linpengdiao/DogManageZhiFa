package com.dog.manage.zhifa.app.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.adapter.MessageAdapter;
import com.dog.manage.zhifa.app.databinding.ActivityMessageBinding;
import com.dog.manage.zhifa.app.model.Message;
import com.okhttp.Pager;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;


import okhttp3.Call;

public class MessageActivity extends BaseActivity {

    private ActivityMessageBinding binding;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_message);
        addActivity(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                CommonUtil.dip2px(getApplicationContext(), 1),
                Color.parseColor("#FAFAFA"));
        binding.recyclerView.addItemDecoration(divider);

        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new MessageAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                Message dataBean = (Message) object;
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean", dataBean);
                openActivity(MessageDetailsActivity.class, bundle);
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });

        setRefresh();

    }

    private Pager<Message> pager = new Pager<>();

    private void setRefresh() {
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pager = new Pager<>();
                loadData(true);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadData(false);

            }
        });
        binding.refreshLayout.autoRefresh();

    }

    public void loadData(boolean isRefresh) {
        SendRequest.sysAnnounceList(pager.getCursor(), pager.getSize(),
                new GenericsCallback<Pager<Message>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        if (isRefresh) {
                            binding.refreshLayout.finishRefresh();
                        } else {
                            binding.refreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isRefresh) {
                            binding.refreshLayout.finishRefresh(false);
                        } else {
                            binding.refreshLayout.finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onResponse(Pager<Message> response, int id) {
                        pager = response;
                        if (response != null && response.getRows() != null) {
                            if (isRefresh) {
                                adapter.refreshData(response.getRows());
                            } else {
                                adapter.loadMoreData(response.getRows());
                            }
                            if (adapter.getList().size() < response.getTotal()) {
                                pager.setCursor(pager.getCursor() + 1);
                            }
                            if (adapter.getList().size() == response.getTotal()) {
                                binding.refreshLayout.setNoMoreData(true);
                            }
                            binding.emptyView.setVisibility(adapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
                            binding.emptyView.setText("暂无消息～");
                        }else {
                            binding.emptyView.setVisibility(View.VISIBLE);
                            binding.emptyView.setText("暂无消息～");
                        }
                    }
                });

    }
}