package com.dog.manage.zhifa.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GsonUtils;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dog.manage.zhifa.app.Config;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.adapter.EnforcementImageAdapter;
import com.dog.manage.zhifa.app.adapter.TypeAdapter;
import com.dog.manage.zhifa.app.databinding.ActivityEnforcementSubmitBinding;
import com.base.MediaFile;
import com.dog.manage.zhifa.app.media.MediaSelectActivity;
import com.dog.manage.zhifa.app.media.MediaUtils;
import com.dog.manage.zhifa.app.model.LicenceInfo;
import com.dog.manage.zhifa.app.model.TypeBean;
import com.dog.manage.zhifa.app.utils.UploadFileManager;
import com.dog.manage.zhifa.app.view.GridItemDecoration;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class EnforcementSubmitActivity extends BaseActivity {

    private ActivityEnforcementSubmitBinding binding;
    private Map<String, String> paramsMap = new HashMap<>();
    private EnforcementImageAdapter imageAdapter;
    private List<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_enforcement_submit);

        licenceInfo = (LicenceInfo) getIntent().getSerializableExtra("licenceInfo");
        String paramsJson = getIntent().getStringExtra("paramsJson");
        if (!TextUtils.isEmpty(paramsJson)) {
            Gson gson = new Gson();
            paramsMap = gson.fromJson(paramsJson, new TypeToken<Map<String, Object>>() {
            }.getType());
        }


        imageAdapter = new EnforcementImageAdapter(this);
        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
        builder.color(R.color.transparent);
        builder.size(CommonUtil.dip2px(this, 2));
        binding.imageRecyclerView.addItemDecoration(new GridItemDecoration(builder));
        binding.imageRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.imageRecyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                if (object instanceof Integer) {
                    int position = (int) object;
                    if (position == (imageList.size() - 1)) {
                        if (imageList.size() < 10) {
                            onClickPicture();
                        } else {
                            ToastUtils.showShort(EnforcementSubmitActivity.this, "最多上传9张图片");
                        }
                    }
                }
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
//        imageList.add("http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/1658016764923292.jpeg");
//        imageList.add("http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/VID_20220717_005206.mp4");
//        imageList.add("http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/1658016765135393.jpeg");
        imageList.add("add");
        imageAdapter.refreshData(imageList);


//        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
        builder.color(R.color.transparent);
        builder.size(CommonUtil.dip2px(this, 10));
        binding.typeRecyclerView.addItemDecoration(new GridItemDecoration(builder));
        binding.typeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        TypeAdapter typeAdapter = new TypeAdapter(this);
        binding.typeRecyclerView.setAdapter(typeAdapter);
        typeAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                illegalTypeId = (int) object;
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
        //处罚类型 1 犬只伤人 2 禁养犬只 3 未牵狗绳 4 其他
        typeAdapter.refreshData(Arrays.asList(new TypeBean(1, "犬只伤人"),
                new TypeBean(2, "禁养犬只"),
                new TypeBean(3, "未牵狗绳"),
                new TypeBean(4, "其他")));

        binding.illegalTimeView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime();
            }
        });

    }

    /**
     * 违法时间
     */
    private void getTime() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //正确设置方式 原因：注意事项有说明
        SimpleDateFormat yearSimpleDateFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthSimpleDateFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat("dd");
        Date date = new Date(System.currentTimeMillis());
        startDate.set(2010, 11, 31);
        endDate.set(Integer.parseInt(yearSimpleDateFormat.format(date)), Integer.parseInt(monthSimpleDateFormat.format(date)) - 1, Integer.parseInt(dateSimpleDateFormat.format(date)));

        TimePickerView timePickerView = new TimePickerBuilder(EnforcementSubmitActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                illegalTime = sdr.format(date);
                binding.illegalTimeView.binding.itemContent.setText(sdr.format(date));
            }
        })
                .setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setSubCalSize(16)//滚轮文字大小
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.white))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .setLineSpacingMultiplier(2.6f)//设置间距倍数,但是只能在1.0-4.0f之间
                .build();
        timePickerView.show();
    }

    /**
     * illegalFileUrl
     * string
     * 违法文件地址
     * illegalTypeId
     * string
     * 违法类型
     * illegalDescribe
     * string
     * 事件描述
     * illegalMeasure
     * string
     * 措施
     * illegalTime
     * string
     * 违法时间 yyyy-MM-dd
     * dogLicenceNum
     * string
     * 狗证编号
     * orgName
     * string
     * 犬主名称（以下参数犬证存在可不填）
     * idNum
     * string
     * 身份证号
     * phoneNum
     * string
     * 手机号
     * detailedAddress
     * string
     * 详细地址
     * addressArea
     * string
     * 所在区域
     * villageId
     * integer
     * 小区id
     * userId
     * string
     * 用户id
     */

    private String illegalFileUrl = null;
    private int illegalTypeId = 0;
    private String illegalTime = null;
    private LicenceInfo licenceInfo;

    public void onClickConfirm(View view) {

        if (imageList.size() <= 1) {
            ToastUtils.showShort(getApplicationContext(), "请上传违法照片或视频");
            return;
        }

        if (illegalTypeId == 0) {
            ToastUtils.showShort(getApplicationContext(), "请选择违法类型");
            return;
        }

        String illegalDescribe = binding.illegalDescribeEditText.getText().toString();
        if (CommonUtil.isBlank(illegalDescribe)) {
            ToastUtils.showShort(getApplicationContext(), "请输入具体事件描述");
            return;
        }

        String illegalMeasure = binding.illegalMeasureEditText.getText().toString();
        if (CommonUtil.isBlank(illegalDescribe)) {
            ToastUtils.showShort(getApplicationContext(), "请输入处罚措施");
            return;
        }

        if (CommonUtil.isBlank(illegalTime)) {
            ToastUtils.showShort(getApplicationContext(), "请选择违法时间");
            return;
        }

        if (imageList.size() > 0 && imageList.indexOf("add") != -1) {
            imageList.remove("add");
        }
        paramsMap.put("illegalFileUrl", GsonUtils.toJson(imageList));
        paramsMap.put("illegalTypeId", String.valueOf(illegalTypeId));
        paramsMap.put("illegalDescribe", illegalDescribe);
        paramsMap.put("illegalMeasure", illegalMeasure);
        paramsMap.put("illegalTime", illegalTime);
        if (licenceInfo != null) {
            paramsMap.put("dogLicenceNum", licenceInfo.getIdNum());
            paramsMap.put("userId", licenceInfo.getUserId() + "");
        }

        SendRequest.saveIllegal(paramsMap, new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(EnforcementSubmitActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(EnforcementSubmitActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(BaseData response, int id) {
                if (response.isSuccess()) {
                    finishActivity(EnforcementActivity.class);
                    finishActivity(EnforcementDogInfoActivity.class);
                    ToastUtils.showShort(EnforcementSubmitActivity.this, "提交成功");
                    finish();

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());

                }

            }
        });


    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickClear(View view) {

    }

    private final int request_IllegalFile = 100;

    /**
     * 上传违法内容
     */
    public void onClickPicture() {
        if (checkPermissions(PermissionUtils.STORAGE, request_IllegalFile)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_ALL);
            bundle.putInt("maxNumber", 10 - imageAdapter.getList().size());
            openActivity(MediaSelectActivity.class, bundle, request_IllegalFile);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_IllegalFile:
                    compressImage(data, request_IllegalFile);

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
                    List<MediaFile> mediaFiles = gson.fromJson(imageJson, new TypeToken<List<MediaFile>>() {
                    }.getType());
                    if (mediaFiles != null && mediaFiles.size() > 0) {
                        List<String> mediaFileList = new ArrayList<>();
                        for (int i = 0; i < mediaFiles.size(); i++) {
                            MediaFile mediaFile = mediaFiles.get(i);
                            String path = mediaFile.getPath();
                            int finalI = i;
                            if (mediaFile.getType() == MediaFile.VIDEO) {
                                mediaFileList.add(path);
                                if (finalI == mediaFiles.size() - 1) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            uploadFile(mediaFileList);
                                        }
                                    }).start();
                                }
                            } else {
                                Luban.with(this)
                                        .load(path)// 传人要压缩的图片列表
                                        .ignoreBy(500)// 忽略不压缩图片的大小
                                        .setTargetDir(FileUtils.getMediaPath())// 设置压缩后文件存储位置
                                        .setCompressListener(new OnCompressListener() { //设置回调
                                            @Override
                                            public void onStart() {
                                            }

                                            @Override
                                            public void onSuccess(File file) {
                                                mediaFileList.add(file.getAbsolutePath());
                                                if (finalI == mediaFiles.size() - 1) {
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            uploadFile(mediaFileList);
                                                        }
                                                    }).start();
                                                }
//                                                }
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                            }
                                        }).launch();//启动压缩
                            }
                        }
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
     * @param mediaFileList
     */
    private void uploadFile(List<String> mediaFileList) {
        try {
            LoadingManager.showLoadingDialog(EnforcementSubmitActivity.this, "上传中...");
            for (int i = 0; i < mediaFileList.size(); i++) {
                String filePath = mediaFileList.get(i);
                Log.i(TAG, "uploadFile: filePath = " + filePath);
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                PutObjectRequest request = new PutObjectRequest();
                request.setBucketName(Config.huaweiBucketName);
                request.setObjectKey(fileName);
                request.setFile(new File(filePath));
                request.setProgressListener(new ProgressListener() {
                    @Override
                    public void progressChanged(ProgressStatus status) {
                    }
                });
                //每上传1MB数据反馈上传进度
                request.setProgressInterval(1024 * 1024L);
                PutObjectResult result = UploadFileManager.getInstance().getObsClient().putObject(request);
                String url = "http://" + Config.huaweiBucketName + "." + Config.huaweiCloudEndPoint + "/" + fileName;
                Log.i(TAG, "uploadFile: url = " + url);
                imageList.add(imageAdapter.getList().size() - 1, url);
                LoadingManager.hideLoadingDialog(EnforcementSubmitActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageAdapter.notifyDataSetChanged();
                    }
                });
            }
        } catch (Exception e) {
            ToastUtils.showShort(getApplication(), "上传失败");
        }
    }

}