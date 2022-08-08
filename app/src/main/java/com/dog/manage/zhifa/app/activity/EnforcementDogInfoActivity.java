package com.dog.manage.zhifa.app.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.GsonUtils;
import com.base.utils.ToastUtils;
import com.base.view.BaseBottomSheetDialog;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.zhifa.app.R;
import com.dog.manage.zhifa.app.adapter.AreaSelectAdapter;
import com.dog.manage.zhifa.app.adapter.CommunitySelectAdapter;
import com.dog.manage.zhifa.app.adapter.MainPagerAdapter;
import com.dog.manage.zhifa.app.databinding.ActivityEnforcementDogInfoBinding;
import com.dog.manage.zhifa.app.databinding.DialogAddressBinding;
import com.dog.manage.zhifa.app.databinding.DialogCommunityBinding;
import com.dog.manage.zhifa.app.fragment.EnforcementDogInfoFragment;
import com.dog.manage.zhifa.app.model.AddressBean;
import com.dog.manage.zhifa.app.model.CommunityBean;
import com.dog.manage.zhifa.app.model.Dog;
import com.dog.manage.zhifa.app.model.LicenceInfo;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 检查结果
 */
public class EnforcementDogInfoActivity extends BaseActivity implements EnforcementDogInfoFragment.OnCallEvents {

    private ActivityEnforcementDogInfoBinding binding;

    public static final int type_noseprint = 0;//
    public static final int type_orgName = 1;//
    public static final int type_userPhone = 2;//
    public static final int type_idNum = 3;//
    private int type;
    //    private String dogLicenceNum;
    private LicenceInfo licenceInfo;

    private List<Dog> dogList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_enforcement_dog_info);
        addActivity(this);

        initView();

        type = getIntent().getIntExtra("type", 0);
        if (type == type_noseprint) {
            String noseprint = getIntent().getStringExtra("noseprint");
            getLicenceInfo(noseprint);

        } else if (type == type_orgName || type == type_userPhone || type == type_idNum) {
            binding.dogCertificateView.setVisibility(View.VISIBLE);
            String content = getIntent().getStringExtra("content");
            getDogByUser(content);

        }

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogList.size() == 0) {
                    ToastUtils.showShort(getApplicationContext(), "暂无犬只");
                    return;
                }
                onClickDogCertificate(EnforcementDogInfoActivity.this,
                        dogList, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()),
                        new OnClickListener() {
                            @Override
                            public void onClick(View view, Object object) {
                                Dog dogDetail = (Dog) object;
                                binding.dogCertificateView.binding.itemContent.setText(dogDetail.getDogType());
                                getLicenceInfo(dogDetail.getNoseprint());
                            }

                            @Override
                            public void onLongClick(View view, Object object) {

                            }
                        });
            }
        });

        binding.addressView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View contentView = LayoutInflater.from(EnforcementDogInfoActivity.this).inflate(R.layout.dialog_address, null);
                addressBinding = DataBindingUtil.bind(contentView);
                BaseBottomSheetDialog bottomSheetDialog = new BaseBottomSheetDialog(EnforcementDogInfoActivity.this) {
                    @Override
                    protected View initContentView() {
                        return addressBinding.getRoot();
                    }
                };
                bottomSheetDialog.show();

                addressBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                addressBinding.recyclerView.setNestedScrollingEnabled(false);
                RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        CommonUtil.dip2px(getApplicationContext(), 0.5f),
                        Color.parseColor("#E1E1E1"));
                addressBinding.recyclerView.addItemDecoration(divider);
                areaSelectAdapter = new AreaSelectAdapter(getApplicationContext());
                addressBinding.recyclerView.setAdapter(areaSelectAdapter);

                addressBinding.refreshLayout.setEnableRefresh(false);
//                addressBinding.refreshLayout.setEnableLoadMore(false);
                addressBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshlayout) {
                        getAddressAreas(false);
                    }
                });
                getAddressAreas(true);

                addressBinding.confirmView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (areaSelectAdapter.getList().size() > 0) {
                            addressBean = areaSelectAdapter.getList().get(areaSelectAdapter.getSelect());
                            binding.addressView.binding.itemContent.setText(addressBean.getAreaName());

                            addressArea = null;
                            villageId = null;
                            detailedAddress = null;
                            binding.detailedAddressView.binding.itemContent.setText(detailedAddress);

                            bottomSheetDialog.cancel();
                        }
                    }
                });


            }
        });

        binding.detailedAddressView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View contentView = LayoutInflater.from(EnforcementDogInfoActivity.this).inflate(R.layout.dialog_community, null);
                communityBinding = DataBindingUtil.bind(contentView);
                BaseBottomSheetDialog bottomSheetDialog = new BaseBottomSheetDialog(EnforcementDogInfoActivity.this) {
                    @Override
                    protected View initContentView() {
                        return communityBinding.getRoot();
                    }
                };
                bottomSheetDialog.show();

                communityBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                communityBinding.recyclerView.setNestedScrollingEnabled(false);
                RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                        LinearLayoutManager.VERTICAL,
                        CommonUtil.dip2px(getApplicationContext(), 0.5f),
                        Color.parseColor("#E1E1E1"));
                communityBinding.recyclerView.addItemDecoration(divider);
                communitySelectAdapter = new CommunitySelectAdapter(getApplicationContext());
                communityBinding.recyclerView.setAdapter(communitySelectAdapter);

                communityBinding.refreshLayout.setEnableRefresh(false);
//                communityBinding.refreshLayout.setEnableLoadMore(false);
                communityBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore(RefreshLayout refreshlayout) {
                        getAddressList(false, "");
                    }
                });
                getAddressList(true, "");

                communityBinding.detailedAddressView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (CommonUtil.isBlank(charSequence.toString())) {
                            getAddressList(true, "");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                communityBinding.searchView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String content = communityBinding.detailedAddressView.getText().toString();
                        if (CommonUtil.isBlank(content)) {
                            ToastUtils.showShort(getApplication(), "请输入小区名称");
                        } else {
                            getAddressList(true, content);
                        }
                    }
                });
                communityBinding.confirmView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (communitySelectAdapter.getList().size() > 0) {
                            communityBean = communitySelectAdapter.getList().get(communitySelectAdapter.getSelect());
                            villageId = String.valueOf(communityBean.getId());
                            addressArea = String.valueOf(communityBean.getCommunityDept());
                            detailedAddress = communityBean.getCommunityName();
                            binding.detailedAddressView.binding.itemContent.setText(detailedAddress);
                            bottomSheetDialog.cancel();
                        }
                    }
                });


            }
        });


    }

    private DialogCommunityBinding communityBinding;
    private CommunitySelectAdapter communitySelectAdapter;
    private Pager<CommunityBean> communityPager = new Pager<>();
    private CommunityBean communityBean;

    /**
     * 详细地址
     *
     * @param isRefresh
     * @param communityName
     */
    private void getAddressList(boolean isRefresh, String communityName) {
        if (addressBean == null) {
            ToastUtils.showShort(getApplicationContext(), "请先选择居住地址");
            return;
        }
        //省110000 、市110100
        SendRequest.getAddressList(communityName, 110000, 110100, addressBean.getId(),
                communityPager.getCursor(), communityPager.getSize(),
                new GenericsCallback<Pager<CommunityBean>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        if (isRefresh) {
                            communityBinding.refreshLayout.finishRefresh();
                        } else {
                            communityBinding.refreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isRefresh) {
                            communityBinding.refreshLayout.finishRefresh(false);
                        } else {
                            communityBinding.refreshLayout.finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onResponse(Pager<CommunityBean> response, int id) {
                        communityPager = response;
                        if (response != null && response.getRows() != null) {
                            if (isRefresh) {
                                communitySelectAdapter.refreshData(response.getRows());
                            } else {
                                communitySelectAdapter.loadMoreData(response.getRows());
                                if (communitySelectAdapter.getList().size() < response.getTotal()) {
                                    communityPager.setCursor(communityPager.getCursor() + 1);
                                }
                            }
                            if (communitySelectAdapter.getList().size() == response.getTotal()) {
                                communityBinding.refreshLayout.setNoMoreData(true);
                            }
                            communityBinding.emptyView.setVisibility(communitySelectAdapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
                            communityBinding.emptyView.setText("暂无数据～");
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });
    }

    private DialogAddressBinding addressBinding;
    private AreaSelectAdapter areaSelectAdapter;
    private Pager<AddressBean> areasPager = new Pager<>();
    private AddressBean addressBean;

    /**
     * 居住地址
     *
     * @param isRefresh
     */
    private void getAddressAreas(boolean isRefresh) {
        //省110000 、市110100
        SendRequest.getAddressAreas(3, 110100,
                areasPager.getCursor(), areasPager.getSize(),
                new GenericsCallback<Pager<AddressBean>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        if (isRefresh) {
                            addressBinding.refreshLayout.finishRefresh();
                        } else {
                            addressBinding.refreshLayout.finishLoadMore();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (isRefresh) {
                            addressBinding.refreshLayout.finishRefresh(false);
                        } else {
                            addressBinding.refreshLayout.finishLoadMore(false);
                        }
                        ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                    }

                    @Override
                    public void onResponse(Pager<AddressBean> response, int id) {
                        areasPager = response;
                        if (response != null && response.getRows() != null) {
                            if (isRefresh) {
                                areaSelectAdapter.refreshData(response.getRows());
                            } else {
                                areaSelectAdapter.loadMoreData(response.getRows());
                            }
                            if (areaSelectAdapter.getList().size() < response.getTotal()) {
                                areasPager.setCursor(areasPager.getCursor() + 1);
                            }
                            if (areaSelectAdapter.getList().size() == response.getTotal()) {
                                addressBinding.refreshLayout.setNoMoreData(true);
                            }
                            addressBinding.emptyView.setVisibility(areaSelectAdapter.getList().size() > 0 ? View.GONE : View.VISIBLE);
                            addressBinding.emptyView.setText("暂无数据～");
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });
    }

    private void getDogByUser(String content) {
        SendRequest.getDogByUser(type == type_orgName ? content : null, type == type_userPhone ? content : null, type == type_idNum ? content : null,
                new GenericsCallback<ResultClient<List<Dog>>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        LoadingManager.showLoadingDialog(EnforcementDogInfoActivity.this);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        LoadingManager.hideLoadingDialog(EnforcementDogInfoActivity.this);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<List<Dog>> response, int id) {
                        if (response.isSuccess()) {
                            if (response.getData() != null) {
                                dogList = response.getData();
                                if (response.getData().size() > 0) {
                                    Dog dogDetail = response.getData().get(0);
                                    binding.dogCertificateView.binding.itemContent.setText(dogDetail.getDogType());
                                    getLicenceInfo(dogDetail.getNoseprint());
                                } else {
                                    licenceInfo = null;
                                    binding.editContainer.setVisibility(View.VISIBLE);
                                    binding.viewPager.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                licenceInfo = null;
                                binding.editContainer.setVisibility(View.VISIBLE);
                                binding.viewPager.setVisibility(View.INVISIBLE);
                                ToastUtils.showShort(getApplicationContext(), "获取犬证信息失败");
                            }

                        } else {
                            licenceInfo = null;
                            binding.editContainer.setVisibility(View.VISIBLE);
                            binding.viewPager.setVisibility(View.INVISIBLE);
                            ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取信息失败");
                        }
                    }
                });
    }

    private void getLicenceInfo(String noseprint) {
        SendRequest.getLicenceInfo(noseprint, null,
                new GenericsCallback<ResultClient<LicenceInfo>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        LoadingManager.showLoadingDialog(EnforcementDogInfoActivity.this);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        LoadingManager.hideLoadingDialog(EnforcementDogInfoActivity.this);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<LicenceInfo> response, int id) {
                        if (response.isSuccess()) {
                            if (response.getData() != null) {
                                licenceInfo = response.getData();

                                binding.viewPager.setVisibility(View.VISIBLE);
                                binding.editContainer.setVisibility(View.INVISIBLE);
                                certificateFragment.intiCertificate(response.getData());
                                immuneFragment.intiImmune(response.getData().getImmuneLicenceId() != null ? response.getData().getImmuneLicenceId() : 0);

                            } else {
                                licenceInfo = null;
                                binding.editContainer.setVisibility(View.VISIBLE);
                                binding.viewPager.setVisibility(View.INVISIBLE);
                                ToastUtils.showShort(getApplicationContext(), response.getMsg());

                            }

                        } else if (response.getCode() == -1 || response.getCode() == -100044) {
                            licenceInfo = null;
                            binding.editContainer.setVisibility(View.VISIBLE);
                            binding.viewPager.setVisibility(View.INVISIBLE);
                            ToastUtils.showShort(getApplicationContext(), response.getMsg());

                        } else {
                            licenceInfo = null;
                            binding.editContainer.setVisibility(View.VISIBLE);
                            binding.viewPager.setVisibility(View.INVISIBLE);
                            ToastUtils.showShort(getApplicationContext(), response.getMsg());

                        }
                    }
                });
    }

    private EnforcementDogInfoFragment certificateFragment;
    private EnforcementDogInfoFragment immuneFragment;

    private void initView() {

        binding.dogOwnerPhoneView.binding.itemEdit.setInputType(InputType.TYPE_CLASS_PHONE);
        binding.dogOwnerPhoneView.binding.itemEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        certificateFragment = EnforcementDogInfoFragment.getInstance(EnforcementDogInfoFragment.type_certificate);
        immuneFragment = EnforcementDogInfoFragment.getInstance(EnforcementDogInfoFragment.type_immune);
        mainPagerAdapter.addFragment("犬证信息", certificateFragment);
        mainPagerAdapter.addFragment("免疫信息", immuneFragment);
        binding.viewPager.setAdapter(mainPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(4);
        binding.viewPager.setCurrentItem(0);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.dogCertificateView.setBackgroundColor(position == 0 ? Color.parseColor("#FAFAFA") : Color.parseColor("#ffffff"));
                binding.viewLayout.setBackgroundColor(position == 0 ? Color.parseColor("#ffffff") : Color.parseColor("#FAFAFA"));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private String orgName = null;
    private String phoneNum = null;
    private String idNum = null;
    private String addressArea = null;
    private String detailedAddress = null;
    private String villageId = null;
    private String userId = null;

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        if (licenceInfo != null) {
            bundle.putSerializable("licenceInfo", licenceInfo);

        } else {

            Map<String, String> paramsMap = new HashMap<>();

            orgName = binding.dogOwnerName.binding.itemEdit.getText().toString().trim();
            if (!CommonUtil.isBlank(orgName)) {
                paramsMap.put("orgName", orgName);
            } else {
                ToastUtils.showShort(getApplicationContext(), "请输入犬主姓名");
                return;
            }

            phoneNum = binding.dogOwnerPhoneView.binding.itemEdit.getText().toString().trim();
            if (!CommonUtil.isBlank(phoneNum)) {
                paramsMap.put("phoneNum", phoneNum);
            } else {
                ToastUtils.showShort(getApplicationContext(), "请输入手机号码");
                return;
            }

            idNum = binding.idNumView.getText().toString().trim();
            if (!CommonUtil.isBlank(idNum)) {
                paramsMap.put("idNum", idNum);
            } else {
                ToastUtils.showShort(getApplicationContext(), "请输入证件号码");
                return;
            }

            if (!CommonUtil.isBlank(addressArea)) {
                paramsMap.put("addressArea", addressArea);
            } else {
                ToastUtils.showShort(getApplicationContext(), "请选择省市区");
                return;
            }

            if (!CommonUtil.isBlank(detailedAddress)) {
                paramsMap.put("detailedAddress", detailedAddress);
            } else {
                paramsMap.put("villageId", villageId);
                ToastUtils.showShort(getApplicationContext(), "请选择详细地址");
                return;
            }
            bundle.putString("paramsJson", GsonUtils.toJson(paramsMap));

        }
        openActivity(EnforcementSubmitActivity.class, bundle);

    }

    @Override
    public void onSubmit(Map<String, String> map) {

    }
}