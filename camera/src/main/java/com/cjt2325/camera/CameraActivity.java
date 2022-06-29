package com.cjt2325.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.base.utils.CommonUtil;
import com.base.utils.StatusBarUtil;
import com.cjt2325.cameralibrary.R;
import com.cjt2325.camera.listener.ClickListener;
import com.cjt2325.camera.listener.ErrorListener;
import com.cjt2325.camera.listener.JCameraListener;
import com.cjt2325.camera.util.FileUtil;

import java.io.File;;

public class CameraActivity extends Activity {

    private JCameraView jCameraView;

    public static final String MAX_TIME = "max_time";
    public static final String MIN_TIME = "min_time";
    public static final String COLOR = "color";
    public static final String STATE_TYPE = "state_type";
    public static final String TIP = "Tip";


    public static void startCameraActivity(Activity activity, int minTime, int maxTime, String color, int stateType, int requestCode, String tip) {
        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(CameraActivity.MIN_TIME, minTime * 1000);
        intent.putExtra(CameraActivity.MAX_TIME, maxTime * 1000);
        intent.putExtra(CameraActivity.COLOR, color);
        intent.putExtra(CameraActivity.STATE_TYPE, stateType);
        intent.putExtra(CameraActivity.TIP, tip);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_camera);
        setStatusBarDarkTheme(false);
        jCameraView = (JCameraView) findViewById(R.id.jcameraview);
        //设置视频保存路径
        jCameraView.setSaveVideoPath(getExternalFilesDir(null) + File.separator + "JCamera");
        jCameraView.setFeatures(getIntent().getIntExtra(STATE_TYPE, JCameraView.BUTTON_STATE_BOTH));
        if (!TextUtils.isEmpty(getIntent().getStringExtra(TIP))) {
            jCameraView.setTip(getIntent().getStringExtra(TIP));
        }
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
        jCameraView.setDuration(getIntent().getIntExtra(MAX_TIME, 60 * 1000));
        jCameraView.setMinDuration(getIntent().getIntExtra(MIN_TIME, 3 * 1000));
        jCameraView.setColor(getIntent().getStringExtra(COLOR));
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                Log.i("JCameraView", "onError: camera error");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
                Toast.makeText(CameraActivity.this, "给点录音权限可以?", Toast.LENGTH_SHORT).show();
            }
        });
        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i("JCameraView", "captureSuccess: bitmap = " + bitmap.getWidth());
                String path = FileUtil.saveBitmap("JCamera", bitmap);
                Intent intent = new Intent();
                intent.putExtra("type", "capture");
                intent.putExtra("picturePath", path);
                intent.putExtra("thumbnailPath", path);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void recordSuccess(final String videoPath, final Bitmap firstFrame) {
                //获取视频路径
                int duration = CommonUtil.getLocalVideoDuration(videoPath);
                String coverPath = FileUtil.saveBitmap("JCamera", firstFrame);

            }
        });

        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(CameraActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.closeView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    public void setStatusBarDarkTheme(boolean dark) {
        if (!StatusBarUtil.setStatusBarDarkTheme(this, dark)) {
            StatusBarUtil.setStatusBarColor(this, dark ? R.color.black : R.color.white);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        //全屏显示
//        if (Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        } else {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(option);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }
}
