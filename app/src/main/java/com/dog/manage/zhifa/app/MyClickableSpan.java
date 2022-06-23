package com.dog.manage.zhifa.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.okhttp.utils.APIUrls;

public class MyClickableSpan extends ClickableSpan {

    private Activity activity;
    private String msg;

    public MyClickableSpan(Activity activity, String msg) {
        this.activity = activity;
        this.msg = msg;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View view) {
        if (msg.equals("《用户协议》")) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(APIUrls.protocol));
            intent.setAction(Intent.ACTION_VIEW);
            activity.startActivity(intent);
        } else if (msg.equals("《隐私政策》")) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(APIUrls.service));
            intent.setAction(Intent.ACTION_VIEW);
            activity.startActivity(intent);
        }
    }
}
