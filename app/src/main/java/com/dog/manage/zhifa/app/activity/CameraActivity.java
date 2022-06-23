package com.dog.manage.zhifa.app.activity;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
//            getAccessToken();
        }

        initCamera();

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
                }
            }
        };
        timer.schedule(timerTask, 0, 2000);

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
        binding.captureView.setText(isCapture ? "停止查询" : "开始查询");
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


    private void getAccessToken() {
        SendRequest.getAccessToken(Config.yueBaoAccessKey, Config.yueBaoSecretKey,
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        try {
                            JSONObject object = new JSONObject(response);
                            int status = object.optInt("status");
                            if (status == 200) {
                                String accessToken = object.optJSONObject("data").optString("access_token");
                                UserInfo userInfo = getUserInfo();
                                userInfo.setAccessToken(accessToken);
                                setUserInfo(userInfo);

                            } else if (status == 401) {
                                ToastUtils.showShort(CameraActivity.this, object.optString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
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

                    }

                    @Override
                    public void onResponse(PetArchives response, int id) {
                        if (response.getStatus() == 200 && response.getData() != null) {

                        } else {
                            ToastUtils.showShort(CameraActivity.this, response.getMessage());
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
                                ToastUtils.showShort(CameraActivity.this, chinese_name);

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
}