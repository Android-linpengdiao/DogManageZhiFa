package com.dog.manage.zhifa.app.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.base.utils.GlideLoader;
import com.dog.manage.zhifa.app.R;
import com.xw.banner.loader.ImageLoader;

public class GlideImageLoader extends ImageLoader {

    private int round;

    public GlideImageLoader(int round) {
        this.round = round;
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        String url = (String) path;
        if (!TextUtils.isEmpty(url) && url.startsWith("http")) {
            GlideLoader.LoderBannerImage(context, url, imageView, round);
        } else {
            if (round == 0){
                GlideLoader.LoderDrawable(context, R.drawable.banner, imageView);
            }else {
                GlideLoader.LoderDrawable(context, R.drawable.banner_1, imageView,round);
            }
        }
    }
}
