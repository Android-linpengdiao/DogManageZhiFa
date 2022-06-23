package com.dog.manage.zhifa.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.base.utils.GlideLoader;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.databinding.ActivityDogUserBinding;
import com.dog.manage.zhifa.app.model.DogUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.List;

import okhttp3.Call;

public class DogUserActivity extends BaseActivity {

    private ActivityDogUserBinding binding;

    private int dogId = 0;
    private DogUser dogUser = new DogUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_user);
        addActivity(this);

        binding.titleView.binding.itemTitle.setText("犬主信息");

        binding.radioButtonOrgan.setEnabled(false);
        binding.radioButtonPersonal.setEnabled(false);

        binding.radioButtonHaiWai.setEnabled(false);
        binding.radioButtonGanGao.setEnabled(false);
        binding.radioButtonIDCard.setEnabled(false);


        binding.organNameView.binding.itemEdit.setEnabled(false);
        binding.dogOwnerNameView.binding.itemEdit.setEnabled(false);
        binding.dogOwnerIDCardView.binding.itemEdit.setEnabled(false);

        binding.addressView.binding.itemContent.setEnabled(false);
        binding.detailedAddressView.setEnabled(false);
        binding.houseNumberView.binding.itemEdit.setEnabled(false);
        initPersonal();

        dogId = getIntent().getIntExtra("dogId", 8);
        getDogUserById();

    }

    /**
     * 犬证 获取犬主信息
     */
    private void getDogUserById() {
        SendRequest.getUserById(getUserInfo().getId(), dogId,
                new GenericsCallback<ResultClient<DogUser>>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<DogUser> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            dogUser = response.getData();
                            initDogUserView(dogUser);
                            if (dogUser.getUserType() == null) {
                                binding.certificateContainer.setVisibility(View.GONE);
                                ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                            }
                        } else {
                            ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                        }
                    }
                });
    }


    private void initDogUserView(DogUser dogUser) {
        binding.certificateContainer.setVisibility(View.VISIBLE);
        if (dogUser.getUserType() != null && (dogUser.getUserType() == DogUser.userType_personal || dogUser.getUserType() == DogUser.userType_organ)) {
            if (dogUser.getUserType() == DogUser.userType_personal) {

                //个人办理
                binding.radioButtonOrgan.setVisibility(View.GONE);
                binding.radioButtonHaiWai.setVisibility(View.GONE);
                binding.radioButtonGanGao.setVisibility(View.GONE);

                binding.dogOwnerNameView.binding.itemEdit.setText(dogUser.getOrgName());
                binding.dogOwnerNameView.binding.itemEdit.setEnabled(false);

                binding.dogOwnerIDCardView.binding.itemEdit.setText(dogUser.getIdNum());
                binding.dogOwnerIDCardView.binding.itemEdit.setEnabled(false);

                binding.IDCardFrontView.setEnabled(false);
                binding.IDCardBackView.setEnabled(false);

                try {
                    List<String> idPhotos = new Gson().fromJson(dogUser.getIdPhoto(), new TypeToken<List<String>>() {
                    }.getType());
                    if (idPhotos.size() > 0) {
                        GlideLoader.LoderImage(DogUserActivity.this,
                                idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.IDCardFrontView, 6);
                    }
                    if (idPhotos.size() > 1) {
                        GlideLoader.LoderImage(DogUserActivity.this,
                                idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.IDCardBackView, 6);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                if (dogUser.getDogType() != null)
                    dogType = dogUser.getDogType();
                //养犬类型（个人）;1导盲犬/扶助犬 2 陪伴犬
                if (dogType == 1) {
                    binding.oldManContainer.setVisibility(View.GONE);
                    binding.oldManOrDisabledCertificateHintView.setText("残疾人证");
                    GlideLoader.LoderUploadImage(DogUserActivity.this, dogUser.getAgedProve(), binding.oldManOrDisabledCertificateView, 6);
                    binding.radioButtonOldMan.setVisibility(View.GONE);

                } else if (dogType == 2) {
                    binding.radioButtonOldMan.setChecked(true);
                    binding.oldManContainer.setVisibility(View.VISIBLE);
                    binding.oldManOrDisabledCertificateHintView.setText("鳏寡老人证明");
                    //是否鳏寡老人（个人）;0：否 1：是
                    if (dogUser.getAged() == 0) {
                        binding.radioButtonOldMan0.setChecked(true);
                    } else if (dogUser.getAged() == 1) {
                        GlideLoader.LoderUploadImage(DogUserActivity.this, dogUser.getAgedProve(), binding.oldManOrDisabledCertificateView, 6);
                    }

                    //犬只类型提示
                    binding.radioButtonDisabled.setVisibility(dogType == 1 ? View.VISIBLE : View.GONE);
                    binding.radioButtonOldMan.setVisibility(dogType == 2 ? View.VISIBLE : View.GONE);
                    binding.radioButtonOldMan0.setChecked(dogUser.getAged() == 0 ? true : false);
                    binding.radioButtonOldMan0.setVisibility(dogUser.getAged() == 0 ? View.VISIBLE : View.GONE);
                    binding.radioButtonOldMan1.setChecked(dogUser.getAged() == 1 ? true : false);
                    binding.radioButtonOldMan1.setVisibility(dogUser.getAged() == 1 ? View.VISIBLE : View.GONE);

                    //犬只类型提示
                    binding.dogTypeHintView.setVisibility(View.GONE);
                    binding.oldManHintView.setVisibility(View.GONE);
                    binding.oldManOrDisabledCertificateContainer.setVisibility(dogType == 2 && dogUser.getAged() == 0 ? View.GONE : View.VISIBLE);

                }

                //居住地址（全）例：012/02/31
                binding.addressView.binding.itemContent.setText(dogUser.getAddress());
                //详细地址（全）
                binding.detailedAddressView.setText(dogUser.getDetailedAddress());

                binding.houseNumberView.binding.itemEdit.setText(dogUser.getHouseNum());
                GlideLoader.LoderUploadImage(DogUserActivity.this, dogUser.getHousePhoto(), binding.houseProprietaryCertificateView, 6);


            } else if (dogUser.getUserType() == DogUser.userType_organ) {
                binding.radioButtonOrgan.setChecked(true);

                binding.radioButtonPersonal.setVisibility(View.GONE);

                binding.organNameView.binding.itemEdit.setText(dogUser.getOrgName());
                binding.organNameView.binding.itemEdit.setEnabled(false);
                GlideLoader.LoderImage(DogUserActivity.this, dogUser.getBizLicense(), binding.businessLicenseView, 6);
                binding.businessLicenseView.setEnabled(false);

                binding.dogOwnerNameView.binding.itemEdit.setText(dogUser.getOrgName());
                binding.dogOwnerNameView.binding.itemEdit.setEnabled(false);

                binding.dogOwnerIDCardView.binding.itemEdit.setText(dogUser.getIdNum());
                binding.dogOwnerIDCardView.binding.itemEdit.setEnabled(false);


                try {
                    //法人证件照（全）
                    List<String> idPhotos = new Gson().fromJson(dogUser.getIdPhoto(), new TypeToken<List<String>>() {
                    }.getType());
                    if (idPhotos.size() > 0) {
                        GlideLoader.LoderImage(DogUserActivity.this,
                                idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.legalPersonIDCardFrontView, 6);
                    }
                    if (idPhotos.size() > 1) {
                        GlideLoader.LoderImage(DogUserActivity.this,
                                idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.legalPersonIDCardBackView, 6);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                //居住地址（全）例：012/02/31
                binding.addressView.binding.itemContent.setText(dogUser.getAddress());
                //详细地址（全）
                binding.detailedAddressView.setText(dogUser.getDetailedAddress());

                //养犬管理制度（单位）
                GlideLoader.LoderUploadImage(DogUserActivity.this, dogUser.getDogManagement(), binding.managementSystemView, 6);
                try {
                    //养犬设施图片（单位）
                    List<String> idPhotos = new Gson().fromJson(dogUser.getDogDevice(), new TypeToken<List<String>>() {
                    }.getType());
                    if (idPhotos.size() > 0) {
                        GlideLoader.LoderImage(DogUserActivity.this,
                                idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.facility1View, 6);
                    }
                    if (idPhotos.size() > 1) {
                        GlideLoader.LoderImage(DogUserActivity.this,
                                idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.facility2View, 6);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }
        }
    }

    /**
     * 个人办理
     */
    private void initPersonal() {

        binding.dogOwnerPersonalCertificateTypeView.setVisibility(View.VISIBLE);
        binding.dogOwnerPersonalCertificateContainer.setVisibility(View.VISIBLE);
        binding.dogTypeContainer.setVisibility(View.VISIBLE);
        if (binding.radioGroupDogType.getCheckedRadioButtonId() == R.id.radioButtonOldMan) {
            binding.oldManContainer.setVisibility(View.VISIBLE);
        } else {
            binding.oldManContainer.setVisibility(View.GONE);
        }
        binding.oldManOrDisabledCertificateContainer.setVisibility(View.VISIBLE);
        binding.houseNumberView.setVisibility(View.VISIBLE);
        binding.houseProprietaryCertificateContainer.setVisibility(View.VISIBLE);

        binding.organNameView.setVisibility(View.GONE);
        binding.organCertificateContainer.setVisibility(View.GONE);
        binding.dogOwnerOrganCertificateContainer.setVisibility(View.GONE);
        binding.facilityContainer.setVisibility(View.GONE);
        binding.managementSystemContainer.setVisibility(View.GONE);

        binding.dogOwnerNameView.binding.itemTitle.setText("犬主姓名");
        binding.dogOwnerNameView.binding.itemEdit.setHint("请输入犬主姓名");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("身份证号码");
        binding.dogOwnerIDCardView.binding.itemEdit.setHint("请输入身份证号码");

        binding.dogOwnerNameView.binding.itemEdit.setText("");
        binding.dogOwnerIDCardView.binding.itemEdit.setText("");
        binding.addressView.binding.itemTitle.setText("居住地址");

        binding.radioGroupIDCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonHaiWai:
                        binding.IDCardBackContainer.setVisibility(View.INVISIBLE);
                        binding.IDCardFrontHintView.setText("上传外国护照");

                        break;
                    case R.id.radioButtonGanGao:
                        binding.IDCardBackContainer.setVisibility(View.INVISIBLE);
                        binding.IDCardFrontHintView.setText("上传港澳通信证");

                        break;
                    case R.id.radioButtonIDCard:
                        binding.IDCardBackContainer.setVisibility(View.VISIBLE);
                        binding.IDCardFrontHintView.setText("上传身份证人像面");

                        break;
                    default:
                        break;
                }
            }
        });
        binding.radioGroupDogType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOldMan://陪伴犬
                        binding.oldManContainer.setVisibility(View.VISIBLE);
                        binding.oldManOrDisabledCertificateHintView.setText("鳏寡老人证明");
                        GlideLoader.LoderUploadImage(DogUserActivity.this, dogUser.getAgedProve(), binding.oldManOrDisabledCertificateView, 6);

                        break;
                    case R.id.radioButtonDisabled://导盲犬/扶助犬
                        binding.oldManContainer.setVisibility(View.GONE);
                        binding.oldManOrDisabledCertificateHintView.setText("残疾人证");
                        GlideLoader.LoderUploadImage(DogUserActivity.this, dogUser.getAgedProve(), binding.oldManOrDisabledCertificateView, 6);

                        break;
                    default:
                        break;
                }
            }
        });
        binding.radioGroupOldMan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOldMan0:
//                        binding.oldManHintView.setVisibility(View.GONE);

                        break;
                    case R.id.radioButtonOldMan1:
//                        binding.oldManHintView.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 单位办理
     */
    private void initOrganView() {

        binding.dogOwnerPersonalCertificateTypeView.setVisibility(View.GONE);
        binding.dogOwnerPersonalCertificateContainer.setVisibility(View.GONE);
        binding.dogTypeContainer.setVisibility(View.GONE);
        binding.oldManContainer.setVisibility(View.GONE);
        binding.oldManOrDisabledCertificateContainer.setVisibility(View.GONE);
        binding.houseNumberView.setVisibility(View.GONE);
        binding.houseProprietaryCertificateContainer.setVisibility(View.GONE);

        binding.organNameView.setVisibility(View.VISIBLE);
        binding.organCertificateContainer.setVisibility(View.VISIBLE);
        binding.dogOwnerOrganCertificateContainer.setVisibility(View.VISIBLE);
        binding.facilityContainer.setVisibility(View.VISIBLE);
        binding.managementSystemContainer.setVisibility(View.VISIBLE);

        binding.dogOwnerNameView.binding.itemTitle.setText("法人姓名");
        binding.dogOwnerNameView.binding.itemEdit.setHint("请输入法人姓名");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("法人身份证号码");
        binding.dogOwnerIDCardView.binding.itemEdit.setHint("请输入法人身份证号码");

        binding.dogOwnerNameView.binding.itemEdit.setText("");
        binding.dogOwnerIDCardView.binding.itemEdit.setText("");
        binding.addressView.binding.itemTitle.setText("养犬地址");


    }

    //个人
    private int dogType = 1;
}