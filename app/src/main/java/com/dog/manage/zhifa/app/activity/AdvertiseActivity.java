package com.dog.manage.zhifa.app.activity;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityAdvertiseBinding;
import com.dog.manage.zhifa.app.model.PoliciesBean;

public class AdvertiseActivity extends BaseActivity {

    private ActivityAdvertiseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_advertise);
        addActivity(this);

        if (getIntent().hasExtra("policiesBean")) {
            PoliciesBean policiesBean = (PoliciesBean) getIntent().getSerializableExtra("policiesBean");
            binding.noticeTitleView.setText(policiesBean.getNoticeTitle());
            binding.noticeTimeView.setText(policiesBean.getCreateTime());
            initWebView(policiesBean.getNoticeContent());
        }
    }

    private void initWebView(String noticeContent) {
        WebSettings wvSettings = binding.webView.getSettings();
        // 是否阻止网络图像
        wvSettings.setBlockNetworkImage(false);
        // 是否阻止网络请求
        wvSettings.setBlockNetworkLoads(false);
        // 是否加载JS
        wvSettings.setJavaScriptEnabled(true);
        wvSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //覆盖方式启动缓存
        wvSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 使用广泛视窗
        wvSettings.setUseWideViewPort(false);
        wvSettings.setLoadWithOverviewMode(true);
        wvSettings.setDomStorageEnabled(true);
        //是否支持缩放
        wvSettings.setBuiltInZoomControls(false);
        wvSettings.setSupportZoom(false);
        //不显示缩放按钮
        wvSettings.setDisplayZoomControls(false);
        wvSettings.setAllowFileAccess(true);
        wvSettings.setDatabaseEnabled(true);
        //缓存相关
        wvSettings.setAppCacheEnabled(true);
        wvSettings.setDomStorageEnabled(true);
        wvSettings.setDatabaseEnabled(true);
        //去掉右侧导航条
        binding.webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView.setHorizontalScrollBarEnabled(false);//水平不显示
        binding.webView.setVerticalScrollBarEnabled(false); //垂直不显示
        binding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                webView.loadUrl("javascript:getContent('" + noticeContent + "')");
                super.onPageFinished(webView, s);
            }
        });
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.addJavascriptInterface(new myJavascriptInterface(), "injectedObject");

        String url = "file:///android_asset/template.html";
        binding.webView.loadUrl(url);
    }

    public class myJavascriptInterface {

        @JavascriptInterface
        public void getHeight(int pageHeight) {

        }

    }
}