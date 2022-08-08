package com.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.base.R;
import com.base.utils.CommonUtil;
import com.base.utils.MapNavigationUtil;

import java.util.List;

public class MapPopupWindow extends BasePopupWindow {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickEvents) {
        this.onClickListener = onClickEvents;
    }

    public MapPopupWindow(Context context) {
        super(context);
    }

    @Override
    protected int animationStyle() {
        return R.style.PopupAnimation;
    }

    @Override
    protected View initContentView() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.view_map_navi_popup, null, false);
        View viewLayout = contentView.findViewById(R.id.view_layout);
        View baiduMap = contentView.findViewById(R.id.baidu_map);
        View gaodeMap = contentView.findViewById(R.id.gaode_map);
        View tencentMap = contentView.findViewById(R.id.tencent_map);
        List<String> hasMap = new MapNavigationUtil().hasMap(context);
        if (hasMap.size()>0) {
            for (int i = 0; i < hasMap.size(); i++) {
                if (hasMap.get(i).contains("com.autonavi.minimap")) {
                    gaodeMap.setVisibility(View.VISIBLE);
                } else if (hasMap.get(i).contains("com.baidu.BaiduMap")) {
                    baiduMap.setVisibility(View.VISIBLE);
                } else if (hasMap.get(i).contains("com.tencent.map")) {
                    tencentMap.setVisibility(View.VISIBLE);
                }
            }
        }
        baiduMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v,null);
                }
            }
        });
        gaodeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v,null);
                }
            }
        });
        tencentMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v,null);
                }
            }
        });
        viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return contentView;
    }

    @Override
    protected int initHeight() {
        return CommonUtil.getScreenHeight(context);
    }

    @Override
    protected int initWidth() {
        return CommonUtil.getScreenWidth(context);
    }
}
