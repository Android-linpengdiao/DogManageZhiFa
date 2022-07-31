package com.dog.manage.zhifa.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.base.view.OnClickListener;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.adapter.MyImageAdapter;
import com.base.MediaFile;

import java.util.List;

public class DialogUtil {
    private static final String TAG = "DialogUtil";
    private static DialogUtil instance;

    private DialogUtil() {

    }

    public static DialogUtil getInstance() {
        if (instance == null) {
            synchronized (DialogUtil.class) {
                if (instance == null) {
                    instance = new DialogUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 显示图片或者视频数据
     *
     * @param activity  activity
     * @param imageList 图片数据
     */
    @SuppressLint("SetTextI18n")
    public AlertDialog showMoreImageView(Activity activity, List<MediaFile> imageList, int current, OnClickListener onClickListener) {

        AlertDialog dialog = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK).create();
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.getDecorView().setBackgroundColor(activity.getResources().getColor(R.color.transparent));
        window.setAttributes(lp);
        window.setContentView(R.layout.video_image_view);


        ImageView back = window.findViewById(R.id.back);
        TextView nowNumTextView = window.findViewById(R.id.nowNumTextView);
        TextView allNumTextView = window.findViewById(R.id.allNumTextView);
        ViewPager viewPager = window.findViewById(R.id.viewPager);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        nowNumTextView.setText(String.valueOf(current + 1));
        allNumTextView.setText("/" + imageList.size());

        MyImageAdapter adapter = new MyImageAdapter(imageList, activity);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(current);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                nowNumTextView.setText(String.valueOf(i + 1));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                switch (view.getId()) {
                    case R.id.selectView:
                        if (onClickListener != null) {
                            onClickListener.onClick(view, object);
                        }
                        break;
                    default:
                        dialog.cancel();
                        break;
                }
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
        return dialog;

    }


}
