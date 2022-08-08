package com.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.base.R;
import com.base.view.GlideBlurTransformation;
import com.base.view.GlideRoundTransform;
import com.base.view.RoundedCornersTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GlideLoader {

    public static void LoderImage(Context context, String url, ImageView view) {
        LoderImage(context, url, view, 0);
    }

    public static void LoderImage(Context context, String url, ImageView view, int round) {
        try {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .transform(new GlideRoundTransform(context, round))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void LoaderDogCover(Context context, String url, ImageView view, int round) {
        try {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .transform(new GlideRoundTransform(context, round))
                    .placeholder(R.drawable.ic_dog_cover)
                    .error(R.drawable.ic_dog_cover)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void LoderUploadImage(Context context, String url, ImageView view, int round) {
        try {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .transform(new GlideRoundTransform(context, round))
                    .placeholder(R.color.transparent)
                    .error(R.color.transparent)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void LoderMessageImage(Context context, String url, ImageView view) {
        try {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .transform(new GlideRoundTransform(context, 100))
                    .placeholder(R.drawable.notice)
                    .error(R.drawable.notice)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } catch (Exception e) {
            e.getMessage();
        }
    }


    public static void LoderBannerImage(Context context, String url, ImageView view, int round) {
        try {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .transform(new GlideRoundTransform(context, round))
                    .placeholder(round == 0 ? R.color.app_background : R.drawable.placeholder)
                    .error(round == 0 ? R.color.app_background : R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void LoderCircleImage(Context context, String url, ImageView view) {
        try {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .transform(new GlideRoundTransform(context, 100))
                    .placeholder(R.drawable.head)
                    .error(R.drawable.head)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void LoderRoundedImage(Context context, String url, ImageView view, int round) {
        try {
            RoundedCornersTransform transform = new RoundedCornersTransform(context, CommonUtil.dip2px(context, round));
            transform.setNeedCorner(true, true, false, false);
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.button_top_gray)
                    .error(R.drawable.button_top_gray)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(transform)
                    .into(view);
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public static void LoderMediaImage(Context context, String url, ImageView view) {
        try {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.color.gray)
                    .error(R.color.gray)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void LoderDrawable(Context context, int drawable, ImageView view) {
        Glide.with(context)
                .load(drawable)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void LoderDrawable(Context context, int drawable, ImageView view, int round) {
        Glide.with(context)
                .load(drawable)
                .fitCenter()
                .transform(new GlideRoundTransform(context, round))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static Bitmap load(Context context, String url) {
        try {
            return Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
