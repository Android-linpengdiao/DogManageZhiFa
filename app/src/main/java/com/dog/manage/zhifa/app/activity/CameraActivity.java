package com.dog.manage.zhifa.app.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.cjt2325.camera.listener.JCameraListener;
import com.dog.manage.zhifa.app.Config;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityCameraBinding;
import com.base.MediaFile;
import com.dog.manage.zhifa.app.media.MediaSelectActivity;
import com.dog.manage.zhifa.app.media.MediaUtils;
import com.dog.manage.zhifa.app.model.PetArchives;
import com.dog.manage.zhifa.app.model.PetType;
import com.dog.manage.zhifa.app.model.UploadPetImageFindPeytId;
import com.dog.manage.zhifa.app.utils.UploadFileManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.obs.services.model.ProgressListener;
import com.obs.services.model.ProgressStatus;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

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

        getAccessToken(this);

        initCamera();

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.hintView.getLayoutParams();
        layoutParams.topMargin = CommonUtil.getScreenWidth(this) / 2 + getResources().getDimensionPixelSize(R.dimen.dp_78);

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonVideo:
                        binding.cameraView.onResume();

                        binding.videoContainer.setVisibility(View.VISIBLE);
                        binding.pictureContainer.setVisibility(View.GONE);
                        binding.demonstrationView.setText("视频采集流程");

                        break;
                    case R.id.radioButtonPicture:
                        isCapture = false;
                        binding.captureView.setText("开始采集");
                        stopTimer();
                        binding.cameraView.onPause();

                        binding.videoContainer.setVisibility(View.GONE);
                        binding.pictureContainer.setVisibility(View.VISIBLE);
                        binding.demonstrationView.setText("图片采集流程");

                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
//        if (isCapture) {
//            startTimer();
//        }
        binding.cameraView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        isCapture = false;
        binding.captureView.setText("开始采集");
        stopTimer();
        binding.cameraView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        timer = null;
        timerTask = null;
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
        timer = null;
        timerTask = null;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Bitmap bitmap = binding.cameraView.createFirstFrameBitmap();
                Log.i(TAG, "run: bitmap " + bitmap);
                if (bitmap != null) {
                    String path = FileUtils.saveFirstFrameBitmap(bitmap);
                    Log.i(TAG, "run: path " + path);
                    Luban.with(CameraActivity.this)
                            .load(path)
                            .ignoreBy(1800)
                            .setTargetDir(FileUtils.getMediaPath())
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    String filePath = file.getAbsolutePath();
                                    if (!CommonUtil.isBlank(filePath)) {
//                                        if (type == type_petType) {
//                                            petType(filePath);
//
//                                        } else
                                        if (type == type_petArchives) {
//                                            createPetArchives(filePath);
                                            uploadPetImageFindPeytId(filePath);

                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                }
                            }).launch();//启动压缩
                }
            }
        };
        timer.schedule(timerTask, 1000, 2000);

    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    /**
     * 开始采集
     *
     * @param view
     */
    public void onClickCapture(View view) {

        int sexCheckedRadioButtonId = binding.radioGroup.getCheckedRadioButtonId();
        if (sexCheckedRadioButtonId == R.id.radioButtonVideo) {//视频采集
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

        } else if (sexCheckedRadioButtonId == R.id.radioButtonPicture) {//图片采集
            if (CommonUtil.isBlank(leftFace) && CommonUtil.isBlank(centerFace) && CommonUtil.isBlank(rightFace) &&
                    CommonUtil.isBlank(archivesOne) && CommonUtil.isBlank(archivesTwo) && CommonUtil.isBlank(archivesThree)) {
                ToastUtils.showShort(CameraActivity.this, "请上传正脸照或鼻纹照");
                return;
            }
            if (!CommonUtil.isBlank(archivesOne)) {
                uploadPetImageFindPeytId(archivesOne, request_ArchivesOne);

            } else if (!CommonUtil.isBlank(archivesTwo)) {
                uploadPetImageFindPeytId(archivesOne, request_ArchivesTwo);

            } else if (!CommonUtil.isBlank(archivesThree)) {
                uploadPetImageFindPeytId(archivesOne, request_ArchivesThree);

            } else if (!CommonUtil.isBlank(leftFace)) {
                uploadPetImageFindPeytId(leftFace, request_LeftFace);

            } else if (!CommonUtil.isBlank(centerFace)) {
                uploadPetImageFindPeytId(centerFace, request_CenterFace);

            } else if (!CommonUtil.isBlank(rightFace)) {
                uploadPetImageFindPeytId(rightFace, request_RightFace);

            }


        }

    }

    /**
     * N 查找宠物
     *
     * @param filePath
     */
    private void uploadPetImageFindPeytId(String filePath, int requestCode) {
        //recogType	int	识别种类：0（鼻子）、1（正脸），不传默认为0（鼻子）
        int recogType = 0;
        if (CommonUtil.isBlank(leftFace) || CommonUtil.isBlank(centerFace) || CommonUtil.isBlank(rightFace)) {
            recogType = 1;
        } else if (CommonUtil.isBlank(archivesOne) || CommonUtil.isBlank(archivesTwo) || CommonUtil.isBlank(archivesThree)) {
            recogType = 0;
        }
        SendRequest.uploadPetImageFindPeytId(getUserInfo().getAccessToken(), recogType, filePath,
                new GenericsCallback<UploadPetImageFindPeytId>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (requestCode == request_ArchivesOne) {
                            if (!CommonUtil.isBlank(archivesTwo)) {
                                uploadPetImageFindPeytId(archivesOne, request_ArchivesTwo);

                            } else if (!CommonUtil.isBlank(archivesThree)) {
                                uploadPetImageFindPeytId(archivesOne, request_ArchivesThree);

                            } else if (!CommonUtil.isBlank(leftFace)) {
                                uploadPetImageFindPeytId(leftFace, request_LeftFace);

                            } else if (!CommonUtil.isBlank(centerFace)) {
                                uploadPetImageFindPeytId(centerFace, request_CenterFace);

                            } else if (!CommonUtil.isBlank(rightFace)) {
                                uploadPetImageFindPeytId(rightFace, request_RightFace);

                            }

                        } else if (requestCode == request_ArchivesTwo) {
                            if (!CommonUtil.isBlank(archivesThree)) {
                                uploadPetImageFindPeytId(archivesOne, request_ArchivesThree);

                            } else if (!CommonUtil.isBlank(leftFace)) {
                                uploadPetImageFindPeytId(leftFace, request_LeftFace);

                            } else if (!CommonUtil.isBlank(centerFace)) {
                                uploadPetImageFindPeytId(centerFace, request_CenterFace);

                            } else if (!CommonUtil.isBlank(rightFace)) {
                                uploadPetImageFindPeytId(rightFace, request_RightFace);

                            }

                        } else if (requestCode == request_ArchivesThree) {
                            if (!CommonUtil.isBlank(leftFace)) {
                                uploadPetImageFindPeytId(leftFace, request_LeftFace);

                            } else if (!CommonUtil.isBlank(centerFace)) {
                                uploadPetImageFindPeytId(centerFace, request_CenterFace);

                            } else if (!CommonUtil.isBlank(rightFace)) {
                                uploadPetImageFindPeytId(rightFace, request_RightFace);

                            }

                        } else if (requestCode == request_LeftFace) {
                            if (!CommonUtil.isBlank(centerFace)) {
                                uploadPetImageFindPeytId(centerFace, request_CenterFace);

                            } else if (!CommonUtil.isBlank(rightFace)) {
                                uploadPetImageFindPeytId(rightFace, request_RightFace);

                            }

                        } else if (requestCode == request_CenterFace) {
                            if (!CommonUtil.isBlank(rightFace)) {
                                uploadPetImageFindPeytId(rightFace, request_RightFace);

                            }

                        }
                    }

                    @Override
                    public void onResponse(UploadPetImageFindPeytId response, int id) {
                        if (response.getStatus() == 200) {
                            if (response.getData() != null &&
                                    response.getData().getPetIds() != null &&
                                    response.getData().getPetIds().size() > 0) {
                                Intent intent = new Intent();
                                intent.putExtra("petId", response.getData().getPetIds().get(0));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } else {
                            ToastUtils.showShort(CameraActivity.this, response.getMessage());
                            if (requestCode == request_ArchivesOne) {
                                if (!CommonUtil.isBlank(archivesTwo)) {
                                    uploadPetImageFindPeytId(archivesOne, request_ArchivesTwo);

                                } else if (!CommonUtil.isBlank(archivesThree)) {
                                    uploadPetImageFindPeytId(archivesOne, request_ArchivesThree);

                                } else if (!CommonUtil.isBlank(leftFace)) {
                                    uploadPetImageFindPeytId(leftFace, request_LeftFace);

                                } else if (!CommonUtil.isBlank(centerFace)) {
                                    uploadPetImageFindPeytId(centerFace, request_CenterFace);

                                } else if (!CommonUtil.isBlank(rightFace)) {
                                    uploadPetImageFindPeytId(rightFace, request_RightFace);

                                }

                            } else if (requestCode == request_ArchivesTwo) {
                                if (!CommonUtil.isBlank(archivesThree)) {
                                    uploadPetImageFindPeytId(archivesOne, request_ArchivesThree);

                                } else if (!CommonUtil.isBlank(leftFace)) {
                                    uploadPetImageFindPeytId(leftFace, request_LeftFace);

                                } else if (!CommonUtil.isBlank(centerFace)) {
                                    uploadPetImageFindPeytId(centerFace, request_CenterFace);

                                } else if (!CommonUtil.isBlank(rightFace)) {
                                    uploadPetImageFindPeytId(rightFace, request_RightFace);

                                }

                            } else if (requestCode == request_ArchivesThree) {
                                if (!CommonUtil.isBlank(leftFace)) {
                                    uploadPetImageFindPeytId(leftFace, request_LeftFace);

                                } else if (!CommonUtil.isBlank(centerFace)) {
                                    uploadPetImageFindPeytId(centerFace, request_CenterFace);

                                } else if (!CommonUtil.isBlank(rightFace)) {
                                    uploadPetImageFindPeytId(rightFace, request_RightFace);

                                }

                            } else if (requestCode == request_LeftFace) {
                                if (!CommonUtil.isBlank(centerFace)) {
                                    uploadPetImageFindPeytId(centerFace, request_CenterFace);

                                } else if (!CommonUtil.isBlank(rightFace)) {
                                    uploadPetImageFindPeytId(rightFace, request_RightFace);

                                }

                            } else if (requestCode == request_CenterFace) {
                                if (!CommonUtil.isBlank(rightFace)) {
                                    uploadPetImageFindPeytId(rightFace, request_RightFace);

                                }

                            }
                        }

                    }
                });
    }

    /**
     * N 查找宠物
     *
     * @param filePath
     */
    private void uploadPetImageFindPeytId(String filePath) {
        //int	识别种类：0为鼻纹，1为面部
        int recogType = 0;
        SendRequest.uploadPetImageFindPeytId(getUserInfo().getAccessToken(), recogType, filePath,
                new GenericsCallback<UploadPetImageFindPeytId>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        startTimer();
                    }

                    @Override
                    public void onResponse(UploadPetImageFindPeytId response, int id) {
                        if (response.getStatus() == 200) {
                            if (response.getData() != null &&
                                    response.getData().getPetIds() != null &&
                                    response.getData().getPetIds().size() > 0) {
                                Intent intent = new Intent();
                                intent.putExtra("petId", response.getData().getPetIds().get(0));
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        } else {
//                            startTimer();
                            ToastUtils.showShort(CameraActivity.this, response.getMessage());
//                            binding.testView.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }


//    /**
//     * 宠物狗面部+鼻纹建档 - 图片采集
//     *
//     * @param filePath
//     */
//    private void createPetArchives(String filePath, int requestCode) {
//        //recogType	int	识别种类：0（鼻子）、1（正脸），不传默认为0（鼻子）
//        int recogType = 0;
//        if (CommonUtil.isBlank(leftFace) || CommonUtil.isBlank(centerFace) || CommonUtil.isBlank(rightFace)) {
//            recogType = 1;
//        } else if (CommonUtil.isBlank(archivesOne) || CommonUtil.isBlank(archivesTwo) || CommonUtil.isBlank(archivesThree)) {
//            recogType = 0;
//        }
//        SendRequest.createPetArchives(getUserInfo().getAccessToken(), recogType, filePath,
//                new GenericsCallback<PetArchives>(new JsonGenericsSerializator()) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        if (requestCode == request_ArchivesOne) {
//                            if (!CommonUtil.isBlank(archivesTwo)) {
//                                createPetArchives(archivesOne, request_ArchivesTwo);
//
//                            } else if (!CommonUtil.isBlank(archivesThree)) {
//                                createPetArchives(archivesOne, request_ArchivesThree);
//
//                            } else if (!CommonUtil.isBlank(leftFace)) {
//                                createPetArchives(leftFace, request_LeftFace);
//
//                            } else if (!CommonUtil.isBlank(centerFace)) {
//                                createPetArchives(centerFace, request_CenterFace);
//
//                            } else if (!CommonUtil.isBlank(rightFace)) {
//                                createPetArchives(rightFace, request_RightFace);
//
//                            }
//
//                        } else if (requestCode == request_ArchivesTwo) {
//                            if (!CommonUtil.isBlank(archivesThree)) {
//                                createPetArchives(archivesOne, request_ArchivesThree);
//
//                            } else if (!CommonUtil.isBlank(leftFace)) {
//                                createPetArchives(leftFace, request_LeftFace);
//
//                            } else if (!CommonUtil.isBlank(centerFace)) {
//                                createPetArchives(centerFace, request_CenterFace);
//
//                            } else if (!CommonUtil.isBlank(rightFace)) {
//                                createPetArchives(rightFace, request_RightFace);
//
//                            }
//
//                        } else if (requestCode == request_ArchivesThree) {
//                            if (!CommonUtil.isBlank(leftFace)) {
//                                createPetArchives(leftFace, request_LeftFace);
//
//                            } else if (!CommonUtil.isBlank(centerFace)) {
//                                createPetArchives(centerFace, request_CenterFace);
//
//                            } else if (!CommonUtil.isBlank(rightFace)) {
//                                createPetArchives(rightFace, request_RightFace);
//
//                            }
//
//                        } else if (requestCode == request_LeftFace) {
//                            if (!CommonUtil.isBlank(centerFace)) {
//                                createPetArchives(centerFace, request_CenterFace);
//
//                            } else if (!CommonUtil.isBlank(rightFace)) {
//                                createPetArchives(rightFace, request_RightFace);
//
//                            }
//
//                        } else if (requestCode == request_CenterFace) {
//                            if (!CommonUtil.isBlank(rightFace)) {
//                                createPetArchives(rightFace, request_RightFace);
//
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    public void onResponse(PetArchives response, int id) {
//                        if (response.getStatus() == 200) {
//                            if (response.getData() != null &&
//                                    response.getData().getPetId() != null) {
//                                Intent intent = new Intent();
//                                intent.putExtra("petId", response.getData().getPetId());
//                                setResult(RESULT_OK, intent);
//                                finish();
//                            }
//                        } else {
//                            ToastUtils.showShort(CameraActivity.this, response.getMessage());
//                            if (requestCode == request_ArchivesOne) {
//                                if (!CommonUtil.isBlank(archivesTwo)) {
//                                    createPetArchives(archivesOne, request_ArchivesTwo);
//
//                                } else if (!CommonUtil.isBlank(archivesThree)) {
//                                    createPetArchives(archivesOne, request_ArchivesThree);
//
//                                } else if (!CommonUtil.isBlank(leftFace)) {
//                                    createPetArchives(leftFace, request_LeftFace);
//
//                                } else if (!CommonUtil.isBlank(centerFace)) {
//                                    createPetArchives(centerFace, request_CenterFace);
//
//                                } else if (!CommonUtil.isBlank(rightFace)) {
//                                    createPetArchives(rightFace, request_RightFace);
//
//                                }
//
//                            } else if (requestCode == request_ArchivesTwo) {
//                                if (!CommonUtil.isBlank(archivesThree)) {
//                                    createPetArchives(archivesOne, request_ArchivesThree);
//
//                                } else if (!CommonUtil.isBlank(leftFace)) {
//                                    createPetArchives(leftFace, request_LeftFace);
//
//                                } else if (!CommonUtil.isBlank(centerFace)) {
//                                    createPetArchives(centerFace, request_CenterFace);
//
//                                } else if (!CommonUtil.isBlank(rightFace)) {
//                                    createPetArchives(rightFace, request_RightFace);
//
//                                }
//
//                            } else if (requestCode == request_ArchivesThree) {
//                                if (!CommonUtil.isBlank(leftFace)) {
//                                    createPetArchives(leftFace, request_LeftFace);
//
//                                } else if (!CommonUtil.isBlank(centerFace)) {
//                                    createPetArchives(centerFace, request_CenterFace);
//
//                                } else if (!CommonUtil.isBlank(rightFace)) {
//                                    createPetArchives(rightFace, request_RightFace);
//
//                                }
//
//                            } else if (requestCode == request_LeftFace) {
//                                if (!CommonUtil.isBlank(centerFace)) {
//                                    createPetArchives(centerFace, request_CenterFace);
//
//                                } else if (!CommonUtil.isBlank(rightFace)) {
//                                    createPetArchives(rightFace, request_RightFace);
//
//                                }
//
//                            } else if (requestCode == request_CenterFace) {
//                                if (!CommonUtil.isBlank(rightFace)) {
//                                    createPetArchives(rightFace, request_RightFace);
//
//                                }
//
//                            }
//                        }
//
//                    }
//                });
//    }
//
//    /**
//     * 宠物狗面部+鼻纹建档 - 视频采集
//     *
//     * @param filePath
//     */
//    private void createPetArchives(String filePath) {
////        stopTimer();
//        SendRequest.createPetArchives(getUserInfo().getAccessToken(), filePath,
//                new GenericsCallback<PetArchives>(new JsonGenericsSerializator()) {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
////                        startTimer();
//                    }
//
//                    @Override
//                    public void onResponse(PetArchives response, int id) {
//                        if (response.getStatus() == 200) {
//                            if (response.getData() != null &&
//                                    response.getData().getPetId() != null) {
//                                Intent intent = new Intent();
//                                intent.putExtra("petId", response.getData().getPetId());
//                                setResult(RESULT_OK, intent);
//                                finish();
//                            }
//                        } else {
////                            startTimer();
//                            ToastUtils.showShort(CameraActivity.this, response.getMessage());
////                            binding.testView.setVisibility(View.VISIBLE);
//                        }
//
//                    }
//                });
//    }
//
//    /**
//     * 宠物品种识别
//     *
//     * @param filePath
//     */
//    private void petType(String filePath) {
//        stopTimer();
//        SendRequest.petType(getUserInfo().getAccessToken(), filePath,
//                new GenericsCallback<PetType>(new JsonGenericsSerializator()) {
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        startTimer();
//                    }
//
//                    @Override
//                    public void onResponse(PetType response, int id) {
//                        if (response.getStatus() == 200) {
//                            if (response.getData() != null &&
//                                    response.getData().getPet() != null &&
//                                    response.getData().getPet().size() > 0 &&
//                                    response.getData().getPet().get(0).getIdentification() != null &&
//                                    response.getData().getPet().get(0).getIdentification().size() > 0) {
//                                String english_name = response.getData().getPet().get(0).getIdentification().get(0).getEnglish_name();
//                                String chinese_name = response.getData().getPet().get(0).getIdentification().get(0).getChinese_name();
//                                Double confidence = response.getData().getPet().get(0).getIdentification().get(0).getConfidence();
////                                ToastUtils.showShort(CameraActivity.this, chinese_name);
//                                Intent intent = new Intent();
//                                intent.putExtra("dogType", chinese_name);
//                                setResult(RESULT_OK, intent);
//                                finish();
//
//                            } else {
//                                startTimer();
//                            }
//
//                        } else {
//                            startTimer();
//                            ToastUtils.showShort(CameraActivity.this, response.getMessage());
//                        }
//
//                    }
//                });
//    }

    public void onClickTest(View view) {
        Intent intent = new Intent();
        intent.putExtra("petId", "23325059-b2c1-11eb-1Vu7hqwN6");
        setResult(RESULT_OK, intent);
        finish();
    }

    private String leftFace = null;
    private String centerFace = null;
    private String rightFace = null;
    private String archivesOne = null;
    private String archivesTwo = null;
    private String archivesThree = null;


    private final int request_LeftFace = 100;
    private final int request_CenterFace = 200;
    private final int request_RightFace = 300;
    private final int request_ArchivesOne = 400;
    private final int request_ArchivesTwo = 500;
    private final int request_ArchivesThree = 600;

    private final int request_FullOne = 700;
    private final int request_FullTwo = 800;

    /**
     * 全身照-1
     *
     * @param view
     */
    public void onClickFullOne(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_FullOne)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_FullOne);
        }
    }

    /**
     * 全身照-2
     *
     * @param view
     */
    public void onClickFullTwo(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_FullTwo)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_FullTwo);
        }
    }

    /**
     * 鼻纹照-1
     *
     * @param view
     */
    public void onClickArchivesOne(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_ArchivesOne)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_ArchivesOne);
        }
    }

    /**
     * 鼻纹照-2
     *
     * @param view
     */
    public void onClickArchivesTwo(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_ArchivesTwo)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_ArchivesTwo);
        }
    }

    /**
     * 鼻纹照-3
     *
     * @param view
     */
    public void onClickArchivesThree(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_ArchivesThree)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_ArchivesThree);
        }
    }

    /**
     * 上传左侧面照片
     *
     * @param view
     */
    public void onClickLeftFace(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_LeftFace)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_LeftFace);
        }
    }

    /**
     * 上传正面照片
     *
     * @param view
     */
    public void onClickCenterFace(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_CenterFace)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_CenterFace);
        }
    }

    /**
     * 上右侧面照片
     *
     * @param view
     */
    public void onClickRightFace(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_RightFace)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_RightFace);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_LeftFace:
                    compressImage(data, request_LeftFace);

                    break;
                case request_CenterFace:
                    compressImage(data, request_CenterFace);

                    break;
                case request_RightFace:
                    compressImage(data, request_RightFace);

                    break;
                case request_ArchivesOne:
                    compressImage(data, request_ArchivesOne);

                    break;
                case request_ArchivesTwo:
                    compressImage(data, request_ArchivesTwo);

                    break;
                case request_ArchivesThree:
                    compressImage(data, request_ArchivesThree);

                    break;
                case request_FullOne:
                    compressImage(data, request_FullOne);

                    break;
                case request_FullTwo:
                    compressImage(data, request_FullTwo);

                    break;
            }
        }
    }

    private void compressImage(Intent data, int requestCode) {
        try {
            if (data != null) {
                String imageJson = data.getStringExtra("imageJson");
                if (!TextUtils.isEmpty(imageJson)) {
                    Gson gson = new Gson();
                    List<MediaFile> imageList = gson.fromJson(imageJson, new TypeToken<List<MediaFile>>() {
                    }.getType());
                    if (imageList != null && imageList.size() > 0) {
                        String path = imageList.get(0).getPath();
                        Luban.with(this)
                                .load(path)// 传人要压缩的图片列表
                                .ignoreBy(1800)// 忽略不压缩图片的大小
                                .setTargetDir(FileUtils.getMediaPath())// 设置压缩后文件存储位置
                                .setCompressListener(new OnCompressListener() { //设置回调
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {

                                        if (requestCode == request_LeftFace) {
                                            leftFace = file.getAbsolutePath();
                                            GlideLoader.LoderImage(CameraActivity.this, leftFace, binding.leftFaceView, 6);

                                        } else if (requestCode == request_CenterFace) {
                                            centerFace = file.getAbsolutePath();
                                            GlideLoader.LoderImage(CameraActivity.this, centerFace, binding.centerFaceView, 6);

                                        } else if (requestCode == request_RightFace) {
                                            rightFace = file.getAbsolutePath();
                                            GlideLoader.LoderImage(CameraActivity.this, rightFace, binding.rightFaceView, 6);

                                        } else if (requestCode == request_ArchivesOne) {
                                            archivesOne = file.getAbsolutePath();
                                            GlideLoader.LoderImage(CameraActivity.this, archivesOne, binding.archivesOneView, 6);

                                        } else if (requestCode == request_ArchivesTwo) {
                                            archivesTwo = file.getAbsolutePath();
                                            GlideLoader.LoderImage(CameraActivity.this, archivesTwo, binding.archivesTwoView, 6);

                                        } else if (requestCode == request_ArchivesThree) {
                                            archivesThree = file.getAbsolutePath();
                                            GlideLoader.LoderImage(CameraActivity.this, archivesThree, binding.archivesThreeView, 6);

                                        } else if (requestCode == request_FullOne) {
                                            String fullOne = file.getAbsolutePath();
                                            GlideLoader.LoderImage(CameraActivity.this, fullOne, binding.fullOneView, 6);

                                        } else if (requestCode == request_FullTwo) {
                                            String fullTwo = file.getAbsolutePath();
                                            GlideLoader.LoderImage(CameraActivity.this, fullTwo, binding.fullTwoView, 6);

                                        }

//                                        new Thread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                uploadFile(requestCode, file.getAbsolutePath());
//                                            }
//                                        }).start();

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }
                                }).launch();//启动压缩
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * 华为云 上传文件
     *
     * @param requestCode
     * @param filePath
     */
    private void uploadFile(int requestCode, String filePath) {
        LoadingManager.showLoadingDialog(CameraActivity.this, "上传中...");
        Log.i(TAG, "uploadFile: filePath = " + filePath);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        Log.i(TAG, "uploadFile: fileName = " + fileName);
        PutObjectRequest request = new PutObjectRequest();
        request.setBucketName(Config.huaweiBucketName);
        request.setObjectKey(fileName);
        request.setFile(new File(filePath));
        request.setProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressStatus status) {
                // 获取上传平均速率
                Log.i(TAG, "uploadFile: AverageSpeed:" + status.getAverageSpeed());
                // 获取上传进度百分比
                Log.i(TAG, "uploadFile: TransferPercentage:" + status.getTransferPercentage());
            }
        });
        //每上传1MB数据反馈上传进度
        request.setProgressInterval(1024 * 1024L);
        PutObjectResult result = UploadFileManager.getInstance().getObsClient().putObject(request);
        Log.i(TAG, "uploadFile: getObjectUrl = " + result.getObjectUrl());
        String url = "http://" + Config.huaweiBucketName + "." + Config.huaweiCloudEndPoint + "/" + fileName;
        Log.i(TAG, "uploadFile: url = " + url);
        LoadingManager.hideLoadingDialog(CameraActivity.this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (requestCode == request_LeftFace) {
                    leftFace = url;
                    GlideLoader.LoderImage(CameraActivity.this, leftFace, binding.leftFaceView, 6);

                } else if (requestCode == request_CenterFace) {
                    centerFace = url;
                    GlideLoader.LoderImage(CameraActivity.this, centerFace, binding.centerFaceView, 6);

                } else if (requestCode == request_RightFace) {
                    rightFace = url;
                    GlideLoader.LoderImage(CameraActivity.this, rightFace, binding.rightFaceView, 6);

                }

            }
        });


    }

    public void onClickDemonstration(View view) {
        int sexCheckedRadioButtonId = binding.radioGroup.getCheckedRadioButtonId();
        if (sexCheckedRadioButtonId == R.id.radioButtonVideo) {//视频采集
            Bundle bundle = new Bundle();
            bundle.putInt("type", 0);
            openActivity(VideoPlayActivity.class, bundle);

        } else if (sexCheckedRadioButtonId == R.id.radioButtonPicture) {//图片采集
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            openActivity(VideoPlayActivity.class, bundle);

        }

    }
}