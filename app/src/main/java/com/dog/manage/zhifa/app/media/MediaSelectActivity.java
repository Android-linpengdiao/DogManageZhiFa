package com.dog.manage.zhifa.app.media;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.base.MediaFile;
import com.base.utils.CommonUtil;
import com.base.utils.GsonUtils;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.activity.BaseActivity;
import com.dog.manage.zhifa.app.adapter.MediaFileAdapter;
import com.dog.manage.zhifa.app.databinding.ActivityMediaSelectBinding;
import com.dog.manage.zhifa.app.utils.DialogUtil;
import com.dog.manage.zhifa.app.view.GridItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MediaSelectActivity extends BaseActivity {

    private ActivityMediaSelectBinding binding;
    private MediaFileAdapter adapter;

    public static final int MEDIA_TYPE_PHOTO = 0;
    public static final int MEDIA_TYPE_VIDEO = 1;
    public static final int MEDIA_TYPE_ALL = 2;

    private int maxNumber = 1;
    private int mediaType = 1;

    private List<MediaFile> selects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_media_select);
        addActivity(this);

        maxNumber = getIntent().getIntExtra("maxNumber", 1);
        mediaType = getIntent().getIntExtra("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);

        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
        builder.color(R.color.transparent);
        builder.size(CommonUtil.dip2px(this, 1));
        binding.recyclerView.addItemDecoration(new GridItemDecoration(builder));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new MediaFileAdapter(getApplication());
        binding.recyclerView.setAdapter(adapter);
        adapter.setMaxNumber(maxNumber);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                MediaFile mediaFile = (MediaFile) object;
                switch (view.getId()) {
                    case R.id.selectView:
                        if (mediaFile.getStatus() == 1) {
                            selects.add(mediaFile);
                        } else {
                            selects.remove(mediaFile);
                        }
                        binding.titleView.binding.itemContent.setText("发送" + (selects.size() > 0 ? "(" + selects.size() + "/" + maxNumber + ")" : ""));
                        adapter.setComplete(maxNumber > selects.size() ? false : true);

                        break;
                    case R.id.coverView:
                        DialogUtil.getInstance().showMoreImageView(MediaSelectActivity.this, Arrays.asList(mediaFile), 0, null);

                        break;
                }


            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });

        MediaUtils.getLocalMedia(getApplicationContext(), mediaType,
                new MediaUtils.LocalMediaCallback() {
                    @Override
                    public void onLocalMediaFileUpdate(final List<MediaFile> mediaFiles) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adapter != null) {
                                    adapter.loadMoreData(mediaFiles);
                                }
                            }
                        });
                    }
                });

        binding.titleView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selects.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("imageJson", GsonUtils.toJson(selects));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showShort(MediaSelectActivity.this, "请选择照片");
                }
            }
        });
    }

    public void onClickPreview(View view) {
        DialogUtil.getInstance().showMoreImageView(MediaSelectActivity.this, selects, 0, new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                if (object instanceof MediaFile) {
                    MediaFile mediaFile = (MediaFile) object;
                    switch (view.getId()) {
                        case R.id.selectView:
                            if (adapter.getList().indexOf(mediaFile) != -1) {
                                adapter.notifyItemInserted(adapter.getList().indexOf(mediaFile));
                                selects.remove(mediaFile);
                                adapter.setComplete(maxNumber > selects.size() ? false : true);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });

    }
}