package com.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.base.LocationBean;
import com.base.R;
import com.base.databinding.ViewMapNaviPopupBinding;
import com.base.view.BaseBottomSheetDialog;
import com.base.view.MapPopupWindow;
import com.base.view.OnClickListener;

import java.util.ArrayList;
import java.util.List;

public class MapNavigationUtil {

    // 检索地图软件
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 指定地图
     * 百度地图包名：com.baidu.BaiduMap
     * <p>
     * 高德地图包名：com.autonavi.minimap
     * <p>
     * 腾讯地图包名：com.tencent.map
     * <p>
     * 谷歌地图 com.google.android.apps.maps
     */
    public List<String> mapsList() {
        List<String> maps = new ArrayList<>();
        maps.add("com.baidu.BaiduMap");
        maps.add("com.autonavi.minimap");
        maps.add("com.tencent.map");
        return maps;
    }

    // 检索筛选后返回
    public List<String> hasMap(Context context) {
        List<String> mapsList = mapsList();
        List<String> backList = new ArrayList<>();
        for (int i = 0; i < mapsList.size(); i++) {
            boolean avilible = isAvilible(context, mapsList.get(i));
            if (avilible) {
                backList.add(mapsList.get(i));
            }
        }
        return backList;
    }


    public static void showChooseMap(final Activity activity, final LocationBean bean) {
        View contentView = View.inflate(activity, R.layout.view_map_navi_popup, null);
        final ViewMapNaviPopupBinding binding = DataBindingUtil.bind(contentView);
        final BaseBottomSheetDialog shareBottomSheetDialog = new BaseBottomSheetDialog(activity) {
            @Override
            protected View initContentView() {
                return binding.getRoot();
            }
        };
        List<String> hasMap = new MapNavigationUtil().hasMap(activity);
        if (hasMap.size() > 0) {
            for (int i = 0; i < hasMap.size(); i++) {
                if (hasMap.get(i).contains("com.autonavi.minimap")) {
                    binding.gaodeMap.setVisibility(View.VISIBLE);
                } else if (hasMap.get(i).contains("com.baidu.BaiduMap")) {
                    binding.baiduMap.setVisibility(View.VISIBLE);
                } else if (hasMap.get(i).contains("com.tencent.map")) {
                    binding.tencentMap.setVisibility(View.VISIBLE);
                }
            }
        }
        shareBottomSheetDialog.show();

        binding.baiduMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBaiduNavi(activity, bean);
                shareBottomSheetDialog.cancel();
            }
        });
        binding.gaodeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toGaodeNavi(activity, bean);
                shareBottomSheetDialog.cancel();
            }
        });
        binding.tencentMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTencentNavi(activity, bean);
                shareBottomSheetDialog.cancel();
            }
        });
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBottomSheetDialog.cancel();
            }
        });

//        MapPopupWindow mapPopupWindow = new MapPopupWindow(activity);
//        mapPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//        mapPopupWindow.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view, Object object) {
//                int id = view.getId();
//                if (id == R.id.baidu_map) {
//                    toBaiduNavi(activity, bean);
//                } else if (id == R.id.gaode_map) {
//                    toGaodeNavi(activity, bean);
//                } else if (id == R.id.tencent_map) {
//                    toTencentNavi(activity, bean);
//                }
//            }
//
//            @Override
//            public void onLongClick(View view, Object object) {
//
//            }
//        });
    }

    // 百度地图
    public static void toBaiduNavi(Context context, LocationBean bean) {
//        Intent naviIntent= new Intent("android.intent.action.VIEW", android.net.Uri.parse("baidumap://map/geocoder?location=" + bean.getLatitude() + "," + bean.getLongitude()));
//        context.startActivity(naviIntent);

        Uri uri;
        if (!CommonUtil.isBlank(bean.getQuery())) {
            uri = Uri.parse("baidumap://map/navi?query=" + bean.getQuery() + "&src=andr.baidu.openAPIdemo");
        } else {
//            uri = Uri.parse("baidumap://map/navi?location=" + bean.getLatitude() + ","+ bean.getLongitude() +"&src=andr.baidu.openAPIdemo");
            uri = Uri.parse("baidumap://map/geocoder?location=" + bean.getLatitude() + "," + bean.getLongitude());
        }
        Intent naviIntent = new Intent();
        naviIntent.setData(uri);
        context.startActivity(naviIntent);
    }

    // 高德地图
    public static void toGaodeNavi(Context context, LocationBean bean) {
//        Intent naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat=" + bean.getLatitude() + "&dlon=" + bean.getLongitude() + "&dname=目的地&dev=0&t=2"));
//        context.startActivity(naviIntent);

        Uri uri;
        if (!CommonUtil.isBlank(bean.getQuery())) {
            uri = Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat=&dlon=&dname=" + bean.getQuery() + "&dev=0&t=2");
        } else {
            uri = Uri.parse("androidamap://route?sourceApplication=appName&slat=&slon=&sname=我的位置&dlat=" + bean.getLatitude() + "&dlon=" + bean.getLongitude() + "&dname=目的地&dev=0&t=2");
        }
        Intent naviIntent = new Intent();
        naviIntent.setData(uri);
        context.startActivity(naviIntent);
    }

    // 腾讯地图
    public static void toTencentNavi(Context context, LocationBean bean) {
//        Intent naviIntent = new Intent("android.intent.action.VIEW", android.net.Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=目的地&tocoord=" + bean.getLatitude() + "," + bean.getLongitude() + "&policy=0&referer=appName"));
//        context.startActivity(naviIntent);

        Uri uri;
        if (!CommonUtil.isBlank(bean.getQuery())) {
            uri = Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=" + bean.getQuery() + "&tocoord=&referer=appName");
        } else {
            uri = Uri.parse("qqmap://map/routeplan?type=drive&from=&fromcoord=&to=目的地&tocoord=" + bean.getLatitude() + "," + bean.getLongitude() + "&policy=0&referer=appName");
        }
        Intent naviIntent = new Intent();
        naviIntent.setData(uri);
        context.startActivity(naviIntent);

    }
}
