package com.dog.manage.zhifa.app.utils;

import com.dog.manage.zhifa.app.Config;
import com.obs.services.ObsClient;

public class UploadFileManager {

    private static UploadFileManager mInstance;
    private ObsClient obsClient = null;

    public static UploadFileManager getInstance() {
        if (mInstance == null) {
            synchronized (UploadFileManager.class) {
                if (mInstance == null) {
                    mInstance = new UploadFileManager();
                }
            }
        }
        return mInstance;
    }

    public UploadFileManager() {
        // 创建ObsClient实例
        obsClient = new ObsClient(Config.huaweiCloudAccessKey, Config.huaweiCloudSecretKey, Config.huaweiCloudEndPoint);
    }

    public ObsClient getObsClient() {
        return obsClient;
    }
}
