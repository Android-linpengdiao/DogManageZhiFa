package com.dog.manage.zhifa.app.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.base.utils.GlideLoader;
import com.base.utils.GsonUtils;
import com.base.utils.TimeUtils;
import com.base.utils.ToastUtils;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.activity.DogDetailsActivity;
import com.dog.manage.zhifa.app.activity.DogInfoActivity;
import com.dog.manage.zhifa.app.activity.DogUserActivity;
import com.dog.manage.zhifa.app.databinding.FragmentEnforcementDogInfoBinding;
import com.dog.manage.zhifa.app.model.ImmuneDetail;
import com.dog.manage.zhifa.app.model.LicenceInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 记录
 */
public class EnforcementDogInfoFragment extends BaseFragment {

    private FragmentEnforcementDogInfoBinding binding;

    public static final int type_certificate = 0;//犬证信息
    public static final int type_immune = 1;//免疫信息
    private int type; // 0-犬证信息  ;1-免疫信息

    public static EnforcementDogInfoFragment getInstance(int type) {
        EnforcementDogInfoFragment fragment = new EnforcementDogInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;

    }

    private OnCallEvents mOnCallEvents;

    public interface OnCallEvents {
        void onSubmit(Map<String, String> map);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnCallEvents = (OnCallEvents) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_enforcement_dog_info, container, false);
        if (getArguments() != null) {

            type = getArguments().getInt("type");

//            licenceBean = (LicenceInfo) getArguments().getSerializable("data");
//            if (type == type_certificate) {
//                intiCertificate();
//
//            } else if (type == type_immune) {
//                if (licenceBean.getImmuneLicenceId() != null)
//                    intiImmune(licenceBean.getImmuneLicenceId());
//            }

        }

        return binding.getRoot();
    }

    /**
     * 犬证信息
     */
    public void intiCertificate(LicenceInfo licenceBean) {
        binding.certificateContainer.setVisibility(View.VISIBLE);
        if (licenceBean != null) {
            binding.idNumView.setText(licenceBean.getIdNum());
            binding.dogTypeView.setText(licenceBean.getDogType());
            binding.dogColorView.setText(licenceBean.getDogColor());
            binding.dogGenderView.setText(licenceBean.getDogGender() == 0 ? "雌性" : "雄性");
            binding.orgNameView.setText(licenceBean.getOrgName());
            binding.awardTimeView.setText(licenceBean.getAwardTime());
            binding.detailedAddressView.setText(licenceBean.getDetailedAddress());
//            GlideLoader.LoaderDogCover(getActivity(), "", binding.certificateCoverView, 5);
            try {
                List<String> dogPhotos = new Gson().fromJson(licenceBean.getDogPhoto(), new TypeToken<List<String>>() {
                }.getType());
                if (dogPhotos != null && dogPhotos.size() > 0)
                    GlideLoader.LoderImage(getActivity(), dogPhotos.get(0), binding.certificateCoverView,5);
            } catch (Exception e) {
                e.getMessage();
            }

            //1无效2有效
            binding.immuneLicenceStatusView.setText(licenceBean.getImmuneLicenceStatus() == 1 ? "免疫证状态:无效" : "免疫证状态:有效");
            if (licenceBean.getSurplusDate() != null) {
                long surplusDate = licenceBean.getSurplusDate();
                binding.licenceStatusView.setText(surplusDate > 0 ? "犬证有效" : ("犬证逾期" + Math.abs(surplusDate) + "天"));
                binding.licenceStatusView.setTextColor(surplusDate > 0 ? Color.parseColor("#273154") : Color.parseColor("#FF2020"));
            }

            binding.dogOwnerInfoView.binding.itemContent.setText(licenceBean.getOrgName());
            binding.dogDetailsView.binding.itemContent.setText(licenceBean.getDogType());

            binding.dogOwnerInfoView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (licenceBean == null) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", licenceBean.getUserId());
                    bundle.putInt("dogId", licenceBean.getDogId());
                    openActivity(DogUserActivity.class, bundle);
                }
            });
            binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (licenceBean == null) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("dogId", licenceBean.getDogId());
                    openActivity(DogInfoActivity.class, bundle);
                }
            });

        } else {

        }


    }

    /**
     * 免疫信息
     *
     * @param immuneId
     */
    public void intiImmune(int immuneId) {
        if (immuneId == 0) {
            immuneView(null);
        }
        binding.immuneContainer.setVisibility(View.GONE);
        SendRequest.getImmuneDetail(immuneId, new GenericsCallback<ResultClient<ImmuneDetail>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<ImmuneDetail> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    immuneView(response.getData());
                } else {
                    immuneView(null);
//                    ToastUtils.showShort(getActivity(), "获取免疫信息失败");
                }

            }
        });
    }

    private void immuneView(ImmuneDetail immuneDetail) {
        if (immuneDetail != null) {
            binding.emptyView.setVisibility(View.GONE);
            binding.immuneContainer.setVisibility(View.VISIBLE);
            binding.immuneIdNumView.setText("免疫证明编号：" + immuneDetail.getIdNum());
            binding.immuneDogNameView.setText(immuneDetail.getDogName());
            if (immuneDetail.getDogGender() != null)
                binding.immuneDogGenderView.setText(immuneDetail.getDogGender() == 0 ? "雌性" : "雄性");
            binding.immuneDogColorView.setText(immuneDetail.getDogColor());
            binding.immuneDogTypeView.setText(immuneDetail.getDogType());
            binding.hospitalNameView.setText(immuneDetail.getHospitalName());

            binding.immuneNameView.setText(immuneDetail.getImmuneName());
            binding.immuneBatchView.setText(immuneDetail.getImmuneBatch());
            binding.immuneFactoryView.setText(immuneDetail.getImmuneFactory());
            binding.immuneDataView.setText(immuneDetail.getImmuneData());
            binding.immuneNumView.setText(immuneDetail.getImmuneNum());
            binding.immuneUserView.setText(immuneDetail.getImmuneUser());
            binding.nextImmuneDataView.setText(immuneDetail.getNextImmuneData());

            binding.streetNameView.setText(immuneDetail.getStreetName());
            binding.immuneDetailedAddressView.setText(immuneDetail.getDetailedAddress());

            try {
                List<String> dogPhotos = new Gson().fromJson(immuneDetail.getDogPhoto(), new TypeToken<List<String>>() {
                }.getType());
                if (dogPhotos != null && dogPhotos.size() > 0)
                    GlideLoader.LoderImage(getActivity(), dogPhotos.get(0), binding.immuneCoverView,5);
            } catch (Exception e) {
                e.getMessage();
            }

            if (immuneDetail.getNextImmuneData() != null) {
                long surplusDate = 365 - (System.currentTimeMillis() - TimeUtils.getTimeExamined(immuneDetail.getNextImmuneData())) / (24 * 60 * 60 * 1000);
                binding.immuneStatusView.setText(surplusDate > 0 ? "免疫证有效" : ("免疫证逾期" + Math.abs(surplusDate) + "天"));
                binding.immuneStatusView.setTextColor(surplusDate > 0 ? Color.parseColor("#273154") : Color.parseColor("#FF2020"));
            }

        } else {
            binding.emptyView.setVisibility(View.VISIBLE);
            binding.immuneContainer.setVisibility(View.GONE);
        }
    }


}
