package com.dog.manage.zhifa.app;

import android.annotation.SuppressLint;
import android.content.Context;

import com.base.BaseApplication;
import com.base.utils.LogUtil;
import com.okhttp.utils.HttpsUtils;
import com.okhttp.utils.OkHttpUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        initSmartRefresh();
        rxJava();
        initOkHttp();

        if (getSharedPreferences("sp_data", Context.MODE_PRIVATE).getBoolean("appService", false)) {

        }

    }

    private void initOkHttp() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

                    @Override
                    public void log(String message) {
                        LogUtil.i("okhttp:message", message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .hostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    private void rxJava() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {

            }
        });
    }

    private void initSmartRefresh() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @SuppressLint("ResourceAsColor")
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {

                //下面示例中的值等于默认值
//                ClassicsHeader header = new ClassicsHeader(context);
//                header.setAccentColor(android.R.color.white);//设置强调颜色
//                header.setPrimaryColor(R.color.colorPrimary);//设置主题颜色
//                header.setTextSizeTitle(16);//设置标题文字大小（sp单位）
//                header.setTextSizeTitle(16, TypedValue.COMPLEX_UNIT_SP);//同上（1.1.0版本删除）
//                header.setTextSizeTime(10);//设置时间文字大小（sp单位）
//                header.setTextSizeTime(10, TypedValue.COMPLEX_UNIT_SP);//同上（1.1.0版本删除）
//                header.setTextTimeMarginTop(10);//设置时间文字的上边距（dp单位）
//                header.setTextTimeMarginTopPx(10);//同上-像素单位（1.1.0版本删除）
//                header.setEnableLastTime(false);//是否显示时间
//                header.setFinishDuration(500);//设置刷新完成显示的停留时间（设为0可以关闭停留功能）
//                header.setDrawableSize(20);//同时设置箭头和图片的大小（dp单位）
//                header.setDrawableArrowSize(20);//设置箭头的大小（dp单位）
//                header.setDrawableProgressSize(20);//设置图片的大小（dp单位）
//                header.setDrawableMarginRight(20);//设置图片和箭头和文字的间距（dp单位）
//                header.setDrawableSizePx(20);//同上-像素单位
//                header.setDrawableArrowSizePx(20);//同上-像素单位（1.1.0版本删除）
//                header.setDrawableProgressSizePx(20);//同上-像素单位（1.1.0版本删除）
//                header.setDrawableMarginRightPx(20);//同上-像素单位（1.1.0版本删除）
//                header.setArrowBitmap(bitmap);//设置箭头位图（1.1.0版本删除）
//                header.setArrowDrawable(drawable);//设置箭头图片
//                header.setArrowResource(R.drawable.ic_arrow);//设置箭头资源
//                header.setProgressBitmap(bitmap);//设置图片位图（1.1.0版本删除）
//                header.setProgressDrawable(drawable);//设置图片
//                header.setProgressResource(R.drawable.ic_loading);//设置图片资源
//                header.setTimeFormat(new DynamicTimeFormat("上次更新 %s"));//设置时间格式化（时间会自动更新）
//                header.setLastUpdateText("上次更新 3秒前");//手动更新时间文字设置（将不会自动更新时间）
//                header.setSpinnerStyle(SpinnerStyle.Translate);//设置移动样式（不支持：MatchLayout）
//                return header;

                return new ClassicsHeader(context)
//                        .setTextSizeTitle(0)
//                        .setTextSizeTime(0)
//                        .setDrawableMarginRightPx(getResources().getDimensionPixelSize(R.dimen.dp_m_12))
//                        .setDrawableSizePx(getResources().getDimensionPixelSize(R.dimen.dp_24))
//                        .setProgressResource(R.drawable.ic_loading)
//                        .setArrowResource(R.drawable.ic_loading)
                        .setSpinnerStyle(SpinnerStyle.Translate);
            }

        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new ClassicsFooter(context)
                        .setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }
}

