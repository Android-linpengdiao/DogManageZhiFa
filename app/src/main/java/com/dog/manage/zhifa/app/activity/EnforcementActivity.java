package com.dog.manage.zhifa.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityEnforcementBinding;

/**
 * 执法拍照
 */
public class EnforcementActivity extends BaseActivity {

    private ActivityEnforcementBinding binding;
    private final int request_petArchive = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_enforcement);
        addActivity(this);


    }

    /**
     * 鼻纹识别扫描查询
     *
     * @param view
     */
    public void onClickSearchPetArchives(View view) {
        if (checkPermissions(PermissionUtils.CAMERA, 100)) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", CameraActivity.type_petArchives);
            openActivity(CameraActivity.class, bundle, request_petArchive);
        }
    }

    /**
     * 查询
     *
     * @param view
     */
    public void onClickSearchDogUser(View view) {
        String name = binding.nameEditText.getText().toString().trim();
        String phone = binding.phoneEditText.getText().toString().trim();
        String idNum = binding.idNumEditText.getText().toString().trim();

        Bundle bundle = new Bundle();
        if (!CommonUtil.isBlank(name)) {
            bundle.putInt("type", EnforcementDogInfoActivity.type_orgName);
            bundle.putString("content", name);

        } else if (!CommonUtil.isBlank(phone)) {
            if (!CommonUtil.isPhone(getApplicationContext(), phone)) {
                return;
            }
            bundle.putInt("type", EnforcementDogInfoActivity.type_userPhone);
            bundle.putString("content", phone);

        } else if (!CommonUtil.isBlank(idNum)) {
            bundle.putInt("type", EnforcementDogInfoActivity.type_idNum);
            bundle.putString("content", idNum);

        } else {
            ToastUtils.showShort(getApplicationContext(), "请输入要查询的内容");
            return;
        }
        openActivity(EnforcementDogInfoActivity.class, bundle);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_petArchive:
                    if (data != null) {
                        String petId = data.getStringExtra("petId");
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", EnforcementDogInfoActivity.type_noseprint);
                        bundle.putString("noseprint", petId);
                        openActivity(EnforcementDogInfoActivity.class, bundle);
                    }
                    break;
            }
        }
    }

}