package com.dog.manage.zhifa.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.base.view.OnClickListener;
import com.bumptech.glide.Glide;
import com.dog.manage.zhifa.app.R;
import com.base.MediaFile;

import java.util.List;

import co.senab.photoview.PhotoView;
import co.senab.photoview.PhotoViewAttacher;

public class MyImageAdapter extends PagerAdapter {
    private List<MediaFile> imageUrls;
    private Activity activity;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MyImageAdapter(List<MediaFile> imageUrls, Activity activity) {
        this.imageUrls = imageUrls;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        MediaFile mediaFile = imageUrls.get(position);
        View inflate = activity.getLayoutInflater().inflate(R.layout.item_myimgage, null);
        PhotoView photoView = inflate.findViewById(R.id.phoneView);
        ImageView selectView = inflate.findViewById(R.id.selectView);
        Glide.with(activity).load(mediaFile.getPath()).into(photoView);
        container.addView(inflate);

        selectView.setSelected(mediaFile.getStatus() == 0 ? false : true);
        selectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaFile.setStatus(mediaFile.getStatus() == 0 ? 1 : 0);
                selectView.setSelected(mediaFile.getStatus() == 0 ? false : true);
                if (onClickListener != null)
                    onClickListener.onClick(view, mediaFile);
            }
        });

        PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
        attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (onClickListener != null) {
                    onClickListener.onClick(view, null);
                }
            }
        });
        return inflate;
    }

    @Override
    public int getCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
