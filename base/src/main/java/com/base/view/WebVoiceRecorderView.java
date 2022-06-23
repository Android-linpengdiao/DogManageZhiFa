package com.base.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.R;
import com.base.utils.LogUtil;

/**
 * Created by Administrator on 2017/9/22.
 */

public class WebVoiceRecorderView extends RelativeLayout {

    private static final String TAG = "VoiceRecorderView";

    private Context context;
    private View recordOpen;
    private View recordClose;
    private ImageView micImage;
    private TextView recordingHint;
    protected Drawable[] micImages;


    public WebVoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public WebVoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WebVoiceRecorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void startRecorder(int what) {
        LogUtil.d(TAG, "startRecorder: ");
        micImage.setImageDrawable(micImages[what]);
    }

    public void showReleaseToCancelHint() {
        recordingHint.setText(context.getString(R.string.ReleaseToCancel));
        recordingHint.setBackgroundResource(R.drawable.web_bg_recording_text_hint);
        recordOpen.setVisibility(GONE);
        recordClose.setVisibility(VISIBLE);
    }

    public void showMoveUpToCancelHint() {
        recordingHint.setText("手指上滑，取消发送");
        recordingHint.setBackgroundColor(Color.TRANSPARENT);
        recordOpen.setVisibility(VISIBLE);
        recordClose.setVisibility(GONE);

    }

    private void init(Context context) {
        LogUtil.d(TAG, "init: ");
        this.context = context;
        int LayoutId = R.layout.view_widget_sound_recorder;
        int mic_imageId = R.id.mic_image;
        int recording_hintId = R.id.recording_hint;
        View view = LayoutInflater.from(context).inflate(LayoutId, this);
        recordOpen = view.findViewById(R.id.record_open);
        recordClose = view.findViewById(R.id.record_close);
        micImage = view.findViewById(mic_imageId);
        micImage = view.findViewById(mic_imageId);
        recordingHint = view.findViewById(recording_hintId);
        showMoveUpToCancelHint();

        micImages = new Drawable[]{
                getResources().getDrawable(R.mipmap.web_record_animate_01),
                getResources().getDrawable(R.mipmap.web_record_animate_02),
                getResources().getDrawable(R.mipmap.web_record_animate_03),
                getResources().getDrawable(R.mipmap.web_record_animate_04),
                getResources().getDrawable(R.mipmap.web_record_animate_05),
                getResources().getDrawable(R.mipmap.web_record_animate_06),
                getResources().getDrawable(R.mipmap.web_record_animate_07)
        };
    }

}
