package com.dog.manage.zhifa.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;

import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.TitleViewBinding;

public class TitleView extends FrameLayout {

    public TitleViewBinding binding;

    public TitleView(Context context) {
        super(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.title_view, null, true);
        binding = DataBindingUtil.bind(rootView);
        addView(rootView);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
        setItemTitle(typedArray.getString(R.styleable.TitleView_tv_title));
        setItemTitleSize(typedArray.getDimensionPixelSize(R.styleable.TitleView_tv_title_size, R.dimen.dp_16));
        setItemTitleColor(typedArray.getColor(R.styleable.TitleView_tv_title_color, 0));
        setItemTitleBold(typedArray.getBoolean(R.styleable.TitleView_tv_title_bold, false));
        setItemContent(typedArray.getString(R.styleable.TitleView_tv_content));
        setItemContentSize(typedArray.getDimensionPixelSize(R.styleable.TitleView_tv_content_size, R.dimen.dp_14));
        setItemContentColor(typedArray.getColor(R.styleable.TitleView_tv_title_color, 0));

    }

    public void setItemTitleColor(int resourceId) {
        if (resourceId != 0) {
            binding.itemTitle.setTextColor(resourceId);
        }

    }

    public void setItemTitleSize(int size) {
        binding.itemTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

    }

    public void setItemTitle(String title) {
        if (title != null) {
            binding.itemTitle.setText(title);
        }
    }

    public void setItemTitleBold(boolean bold) {
        if (bold) {
//            Typeface typeface = BaseApplication.getInstance().getTypeface();
//            binding.itemTitle.setTypeface(typeface);
//            binding.itemTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
//            binding.itemTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

    public void setItemContentSize(int size) {
        binding.itemContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

    }

    public void setItemContentColor(int resourceId) {
        if (resourceId != 0) {
            binding.itemContent.setTextColor(resourceId);
        }

    }

    public void setItemContent(String content) {
        if (content != null) {
            binding.itemContent.setText(content);
        }
    }


}
