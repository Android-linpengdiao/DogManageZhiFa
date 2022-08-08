package com.dog.manage.zhifa.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.base.BaseApplication;
import com.base.utils.CommonUtil;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ViewItemLayoutBinding;

public class ViewItemLayout extends FrameLayout {
    private static final String TAG = "ViewItemLayout";
    public ViewItemLayoutBinding binding;

    public ViewItemLayout(Context context) {
        super(context);
    }

    public ViewItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public ViewItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.view_item_layout, null, true);
        binding = DataBindingUtil.bind(rootView);
        addView(rootView);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ViewItemLayout);
        setItemTitle(typedArray.getString(R.styleable.ViewItemLayout_item_title));
        setItemTitleSize(typedArray.getDimensionPixelSize(R.styleable.ViewItemLayout_item_title_size, R.dimen.dp_16));
        setItemTitleColor(typedArray.getColor(R.styleable.ViewItemLayout_item_title_color, 0));
        setItemTitleBold(typedArray.getBoolean(R.styleable.ViewItemLayout_item_title_bold, false));

        setItemContent(typedArray.getString(R.styleable.ViewItemLayout_item_content));
        setItemContentSize(typedArray.getDimensionPixelSize(R.styleable.ViewItemLayout_item_content_size, R.dimen.dp_12));
        setItemContentColor(typedArray.getColor(R.styleable.ViewItemLayout_item_content_color, 0));
        setItemContentBold(typedArray.getBoolean(R.styleable.ViewItemLayout_item_content_bold, false));

        setItemEdit(typedArray.getString(R.styleable.ViewItemLayout_item_edit));
        setItemEditHint(typedArray.getString(R.styleable.ViewItemLayout_item_edit_hint));
        setItemEditSize(typedArray.getDimensionPixelSize(R.styleable.ViewItemLayout_item_edit_size, R.dimen.dp_12));
        setItemEditColor(typedArray.getColor(R.styleable.ViewItemLayout_item_edit_color, 0));
        setItemEditVisible(typedArray.getBoolean(R.styleable.ViewItemLayout_item_edit_visible, false));

        setItemInfo(typedArray.getString(R.styleable.ViewItemLayout_item_info));
        setItemInfoSize(typedArray.getDimensionPixelSize(R.styleable.ViewItemLayout_item_info_size, R.dimen.dp_12));
        setItemInfoColor(typedArray.getColor(R.styleable.ViewItemLayout_item_info_color, 0));
        setItemInfoVisible(typedArray.getBoolean(R.styleable.ViewItemLayout_item_info_visible, false));
        setItemInfoWidth(typedArray.getDimensionPixelSize(R.styleable.ViewItemLayout_item_info_width, 0));

        setItemDesc(typedArray.getString(R.styleable.ViewItemLayout_item_desc));
        setItemDescSize(typedArray.getDimensionPixelSize(R.styleable.ViewItemLayout_item_desc_size, R.dimen.dp_12));
        setItemDescColor(typedArray.getColor(R.styleable.ViewItemLayout_item_desc_color, 0));
        setItemDescVisible(typedArray.getBoolean(R.styleable.ViewItemLayout_item_desc_visible, false));

        setItemContainerHeight(typedArray.getDimensionPixelSize(R.styleable.ViewItemLayout_item_container_height, 0));
        setItemContainerMarginLeft(typedArray.getDimensionPixelSize(R.styleable.ViewItemLayout_item_container_margin_left, 0));
        setItemContainerMarginRight(typedArray.getDimensionPixelSize(R.styleable.ViewItemLayout_item_container_margin_right, 0));

        setItemIconLeft(typedArray.getResourceId(R.styleable.ViewItemLayout_item_icon_left, 0));
        setItemIconLeftWidth(typedArray.getInt(R.styleable.ViewItemLayout_item_icon_left_width, 0));
        setItemIconLeftVisible(typedArray.getBoolean(R.styleable.ViewItemLayout_item_icon_left_visible, false));

        setItemIconRight(typedArray.getResourceId(R.styleable.ViewItemLayout_item_icon_right, 0));
        setItemIconRightVisible(typedArray.getBoolean(R.styleable.ViewItemLayout_item_icon_right_visible, false));

        setItemSwitch(typedArray.getResourceId(R.styleable.ViewItemLayout_item_switch, 0));
        setItemSwitchVisible(typedArray.getBoolean(R.styleable.ViewItemLayout_item_switch_visible, false));

        setItemArrowVisible(typedArray.getBoolean(R.styleable.ViewItemLayout_item_arrow_visible, false));
        setItemDividerVisible(typedArray.getBoolean(R.styleable.ViewItemLayout_item_divider_visible, false));

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
            Typeface typeface = BaseApplication.getInstance().getTypeface();
            binding.itemTitle.setTypeface(typeface);
//            binding.itemTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            binding.itemTitle.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
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
    public void setItemContentBold(boolean bold) {
        if (bold) {
            Typeface typeface = BaseApplication.getInstance().getTypeface();
            binding.itemContent.setTypeface(typeface);
//            binding.itemTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            binding.itemContent.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }


    public void setItemContent(String content) {
        if (content != null) {
            binding.itemContent.setText(content);
        }
    }

    public void setItemEditSize(int size) {
        binding.itemEdit.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

    }

    public void setItemEditColor(int resourceId) {
        if (resourceId != 0) {
            binding.itemEdit.setTextColor(resourceId);
        }

    }

    public void setItemEditVisible(boolean visible) {
        if (visible) {
            binding.itemEdit.setVisibility(VISIBLE);
            binding.itemContent.setVisibility(GONE);
        }
    }

    public void setItemEdit(String edit) {
        if (edit != null) {
            binding.itemEdit.setText(edit);
        }
    }

    public void setItemEditHint(String hint) {
        if (hint != null) {
            binding.itemEdit.setHint(hint);
        }
    }

    public void setItemInfoSize(int size) {
        binding.itemInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

    }

    public void setItemInfoColor(int resourceId) {
        if (resourceId != 0) {
            binding.itemInfo.setTextColor(resourceId);
        }

    }

    public void setItemInfoVisible(boolean visible) {
        if (visible) {
            binding.itemInfo.setVisibility(VISIBLE);
        }
    }

    public void setItemInfo(String info) {
        if (info != null) {
            binding.itemInfo.setText(info);
        }
    }

    public void setItemInfoWidth(int width) {
        Log.i(TAG, "setItemInfoWidth: " + width);
        if (width > 0) {
            binding.itemInfo.getLayoutParams().width = width;
        }
    }


    public void setItemDescSize(int size) {
        binding.itemDesc.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);

    }

    public void setItemDescColor(int resourceId) {
        if (resourceId != 0) {
            binding.itemDesc.setTextColor(resourceId);
        }

    }

    public void setItemDescVisible(boolean visible) {
        if (visible) {
            binding.itemDesc.setVisibility(VISIBLE);
        }
    }

    public void setItemDesc(String info) {
        if (info != null) {
            binding.itemDesc.setText(info);
        }
    }

    public void setItemContainerHeight(int height) {
        if (height > 0) {
            binding.itemContainer.getLayoutParams().height = height;
        }
    }

    public void setItemContainerMarginLeft(int left) {
        if (left > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.itemContainer.getLayoutParams();
            layoutParams.leftMargin = left;
        }
    }

    public void setItemContainerMarginRight(int right) {
        if (right > 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.itemContainer.getLayoutParams();
            layoutParams.rightMargin = right;
        }
    }

    public void setItemArrowVisible(boolean visible) {
        if (visible) {
            binding.itemArrow.setVisibility(VISIBLE);
        } else {
            binding.itemArrow.setVisibility(GONE);
        }
    }

    public void setItemDividerVisible(boolean visible) {
        if (visible) {
            binding.itemDivider.setVisibility(VISIBLE);
        } else {
            binding.itemDivider.setVisibility(GONE);
        }
    }

    public void setItemSwitchVisible(boolean visible) {
        if (visible) {
//            binding.itemSwitch.setVisibility(VISIBLE);
        }
    }

    public void setItemSwitch(int resourceId) {
        if (resourceId != 0) {
//            binding.itemSwitch.setImageResource(resourceId);
        }
    }

    public void setItemIconRightVisible(boolean visible) {
        if (visible) {
//            binding.itemIconRight.setVisibility(VISIBLE);
        }
    }

    public void setItemIconRight(int resourceId) {
        if (resourceId != 0) {
//            binding.itemIconRight.setImageResource(resourceId);
        }
    }

    public void setItemIconLeftVisible(boolean visible) {
        if (visible) {
            binding.itemIconLeft.setVisibility(VISIBLE);
        }
    }

    public void setItemIconLeftWidth(int width) {
        if (width != 0) {
            binding.itemIconLeft.getLayoutParams().width = CommonUtil.dip2px(getContext(), width);
        }
    }

    public void setItemIconLeft(int resourceId) {
        if (resourceId != 0) {
            binding.itemIconLeft.setImageResource(resourceId);
        }
    }


}
