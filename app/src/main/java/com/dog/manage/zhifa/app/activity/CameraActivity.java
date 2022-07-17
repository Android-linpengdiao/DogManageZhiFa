package com.dog.manage.zhifa.app.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.base.UserInfo;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.ToastUtils;
import com.cjt2325.camera.listener.JCameraListener;
import com.dog.manage.zhifa.app.Config;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityCameraBinding;
import com.dog.manage.zhifa.app.model.PetArchives;
import com.dog.manage.zhifa.app.model.PetType;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.callbacks.StringCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;

public class CameraActivity extends BaseActivity {

    private ActivityCameraBinding binding;
    public final static int type_petType = 0;
    public final static int type_petArchives = 1;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_camera);
        setStatusBarDarkTheme(true);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);
        binding.titleView.setText(type == type_petType ? "犬只品种" : "鼻纹采集");

        if (CommonUtil.isBlank(getUserInfo().getAccessToken())) {
            getAccessToken(this);
        }

        initCamera();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.hintView.getLayoutParams();
        layoutParams.topMargin = CommonUtil.getScreenWidth(this) / 2 + getResources().getDimensionPixelSize(R.dimen.dp_78);

    }

    @Override
    protected void onResume() {
        if (isCapture) {
            startTimer();
        }
        binding.cameraView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (isCapture) {
            stopTimer();
        }
        binding.cameraView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    private boolean isCapture = false;

    private void initCamera() {
        //CameraView监听
        binding.cameraView.setJCameraListener(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap

            }

            @Override
            public void recordSuccess(final String videoPath, final Bitmap firstFrame) {


            }
        });
    }

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Timer timer;
    private TimerTask timerTask;
    private Object object = new Object();

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Bitmap bitmap = binding.cameraView.createFirstFrameBitmap();
                Log.i(TAG, "run: bitmap " + bitmap);
                if (bitmap != null) {
                    String path = FileUtils.saveFirstFrameBitmap(bitmap);
                    Log.i(TAG, "run: path " + path);
                    if (!CommonUtil.isBlank(path)) {
                        if (type == type_petType) {
                            petType(path);

                        } else if (type == type_petArchives) {
                            createPetArchives(path);

                        }
                    }
                }
            }
        };
        timer.schedule(timerTask, 3000, 3000);

    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 开始采集
     *
     * @param view
     */
    public void onClickCapture(View view) {
        isCapture = !isCapture;
        binding.captureView.setText(isCapture ? "停止采集" : "开始采集");
        ToastUtils.showShort(CameraActivity.this, isCapture ? "开始采集" : "停止采集");
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                synchronized (object) {
                    if (isCapture) {
                        startTimer();
                    } else {
                        stopTimer();
                    }
                }
            }
        });

    }

    /**
     * 宠物狗面部+鼻纹建档
     *
     * @param filePath
     */
    private void createPetArchives(String filePath) {
        stopTimer();
        SendRequest.createPetArchives(getUserInfo().getAccessToken(), filePath,
                new GenericsCallback<PetArchives>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        startTimer();
                    }

                    @Override
                    public void onResponse(PetArchives response, int id) {
                        if (response.getStatus() == 200) {
                            if (response.getData() != null &&
                                    response.getData().getPetId() != null) {
                                Intent intent = new Intent();
                                intent.putExtra("petId", response.getData().getPetId());
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } else {
                            startTimer();
                            ToastUtils.showShort(CameraActivity.this, response.getMessage());
//                            binding.testView.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }

    /**
     * 宠物品种识别
     *
     * @param filePath
     */
    private void petType(String filePath) {
        stopTimer();
        SendRequest.petType(getUserInfo().getAccessToken(), filePath,
                new GenericsCallback<PetType>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        startTimer();
                    }

                    @Override
                    public void onResponse(PetType response, int id) {
                        if (response.getStatus() == 200) {
                            if (response.getData() != null &&
                                    response.getData().getPet() != null &&
                                    response.getData().getPet().size() > 0 &&
                                    response.getData().getPet().get(0).getIdentification() != null &&
                                    response.getData().getPet().get(0).getIdentification().size() > 0) {
                                String english_name = response.getData().getPet().get(0).getIdentification().get(0).getEnglish_name();
                                String chinese_name = response.getData().getPet().get(0).getIdentification().get(0).getChinese_name();
                                Double confidence = response.getData().getPet().get(0).getIdentification().get(0).getConfidence();
//                                ToastUtils.showShort(CameraActivity.this, chinese_name);
                                Intent intent = new Intent();
                                intent.putExtra("dogType", chinese_name);
                                setResult(RESULT_OK, intent);
                                finish();

                            } else {
                                startTimer();
                            }

                        } else {
                            startTimer();
                            ToastUtils.showShort(CameraActivity.this, response.getMessage());
                        }

                    }
                });
    }

    public void onClickTest(View view) {
        Intent intent = new Intent();
        intent.putExtra("petId", "23325059-b2c1-11eb-1Vu7hqwN61");
        setResult(RESULT_OK, intent);
        finish();
    }
}