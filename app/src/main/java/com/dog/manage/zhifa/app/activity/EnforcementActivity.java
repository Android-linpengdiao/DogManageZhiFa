package com.dog.manage.zhifa.app.activity;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_enforcement);


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
            openActivity(CameraActivity.class, bundle);
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

        } else if (!CommonUtil.isBlank(phone)) {
            if (!CommonUtil.isPhone(getApplicationContext(), phone)) {
                return;
            }
        } else if (!CommonUtil.isBlank(idNum)) {
            bundle.putString("dogLicenceNum", idNum);

        } else {
            ToastUtils.showShort(getApplicationContext(), "请输入要查询的内容");
            return;
        }
        openActivity(EnforcementDogInfoActivity.class, bundle);

    }

}