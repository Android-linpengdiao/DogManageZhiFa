package com.okhttp.utils;

public class APIUrls {

    public final static String URL_DOMAIN = "http://124.70.40.92:9204";

    public final static String protocol = URL_DOMAIN + "/protocol.html";
    public final static String service = URL_DOMAIN + "/protocol.html";


    //APP登录
    public final static String userLogin = URL_DOMAIN + "/user/login";
    //发送短信验证
    public final static String sendMessageUser = URL_DOMAIN + "/user/sendMessageUser";
    //获得游客身份
    public final static String getVisitor = URL_DOMAIN + "/web/user/getVisitor";
    //获取用户信息
    public final static String userLoad = URL_DOMAIN + "/web/user/load";
    public final static String getLawUser = URL_DOMAIN + "/user/law/getLawUser";
    public final static String updatePwd = URL_DOMAIN + "/user/updatePwd";


    // ==================================== 犬只管理医院端 =============================================


    //医院端-根据鼻纹获取犬只信息
    public final static String getDogByNoseprint = URL_DOMAIN + "/hospital/dog/getDogByNoseprint";
    //医院端-保存疫苗信息
    public final static String updateImmuneLicenceLog = URL_DOMAIN + "/hospital/dog/updateImmuneLicenceLog";


    // ==================================== 犬只管理执法端 =============================================


    //执法端-获取犬证
    public final static String getLicenceInfo = URL_DOMAIN + "/user/dog/getLicenceInfo";
    //执法端-根据用户身份获取犬只信息
    public final static String getDogByUser = URL_DOMAIN + "/user/law/getDogByUser";
    //我的-免疫证详情
    public final static String getImmuneDetail = URL_DOMAIN + "/user/dog/getImmuneDetail";
    //执法端-保存
    public final static String saveIllegal = URL_DOMAIN + "/user/saveIllegal";
    //执法端-处罚记录
    public final static String getIllegalList = URL_DOMAIN + "/user/law/getIllegalList";
    //执法端-处罚记录详情
    public final static String getIllegalDetails = URL_DOMAIN + "/user/law/getIllegalDetails";

    //保存犬主-获取省市区
    public final static String addressAreas = URL_DOMAIN + "/user/address/areas";
    //保存犬主-根据省市区查询社区列表
    public final static String addressList = URL_DOMAIN + "/user/address/list";

    /**
     * 系统消息
     */
    //系统消息
    public final static String sysAnnounceList = URL_DOMAIN + "/sysNotice/sysAnnounceList";
    public final static String sysNoticeList = URL_DOMAIN + "/sysNotice/sysNoticeList";
    public final static String getSysNoticeById = URL_DOMAIN + "/notice/getSysNoticeById";










    // ==================================== 犬只管理 =============================================

    public final static String testUrl = URL_DOMAIN + "/testUrl";

    //政策法规
    public final static String noticeList = URL_DOMAIN + "/notice/list";
    //政策法规详情
    public final static String getNoticeById = URL_DOMAIN + "/notice/getNoticeById";
    //获取banner图片列表
    public final static String bannerInfoList = URL_DOMAIN + "/bannerInfo/list";
    //禁养犬宣传接口
    public final static String getForbiddenById = URL_DOMAIN + "/forbidden/getForbiddenById";

    /**
     * 办理犬证
     */
    //验证用户是否填写 个人信息
    public final static String checkingDogUser = URL_DOMAIN + "/user/checkingDogUser";
    //获取犬主信息
    public final static String getDogUser = URL_DOMAIN + "/user/getDogUser";
    //保存犬主信息
    public final static String editDogUser = URL_DOMAIN + "/user/editDogUser";
    //保存犬只
    public final static String savaDog = URL_DOMAIN + "/user/dog/savaDog";
    //验证犬只是否可养
    public final static String verificationDog = URL_DOMAIN + "/user/dog/verificationDog";
    //获取价格、手里单位信息接口
    public final static String getHandleInfo = URL_DOMAIN + "/user/dog/getHandleInfo";
    //犬证提交审核
    public final static String approveDogLicence = URL_DOMAIN + "/user/dog/approveDogLicence";


    /**
     * 犬只免疫办理
     */
    //根据坐标获取宠物医院信息
    public final static String getHospitalPosition = URL_DOMAIN + "/user/dog/getHospitalPosition";
    //免疫证 提交免疫证办理
    public final static String saveImmune = URL_DOMAIN + "/user/dog/saveImmune";


    //获取犬只信息列表
    public final static String getDogList = URL_DOMAIN + "/user/dog/getDogList";


    /**
     * 犬证年审
     */
    //犬证年审-选择我的犬只详情
    public final static String getDogLicenseDetail = URL_DOMAIN + "/user/dog/getDogLicenseDetail";

    /**
     * 犬只注销-提交
     */
    //犬证年审-选择我的犬只详情
    public final static String saveCancelDogInfo = URL_DOMAIN + "/user/canelDog/saveCancelDogInfo";



    /**
     * 信息变更
     */
    //信息变更-保存变更地址
    public final static String saveCancelAddress = URL_DOMAIN + "/user/canelDog/saveCancelAddress";
    //信息变更 -提交审核
    public final static String approveCancelAddress = URL_DOMAIN + "/user/canelDog/approveCancelAddress";


    /**
     * 我的
     */
    //获取犬证列表
    public final static String getDogLicenceList = URL_DOMAIN + "/user/dog/getDogLicenceList";
    //获取个人犬只免疫列表
    public final static String getDogImmuneList = URL_DOMAIN + "/user/dog/getDogImmuneList";
    //犬证办理记录
    public final static String getUserDogLicence = URL_DOMAIN + "/user/dog/getUserDogLincence";
    //犬证办理记录详情
    public final static String getDogLicenceDetail = URL_DOMAIN + "/user/dog/getDogLincenceDetail";
    //犬证 获取犬主信息
    public final static String getUserById = URL_DOMAIN + "/user/dog/getUserById";
    //犬证 获取犬只详情信息
    public final static String getDogById = URL_DOMAIN + "/user/dog/getDogById";

    //免疫证办理记录列表
    public final static String getDogImmuneStatusList = URL_DOMAIN + "/user/dog/getDogImmuneStatusList";
    //免疫证办理记录详情
    public final static String getDogImmuneApproveDetail = URL_DOMAIN + "/user/dog/getDogImmuneApproveDetail";

    //设置-变更手机号 - 获取用户手机号
    public final static String getUserPhone = URL_DOMAIN + "/user/getUserPhone";
    //变更手机号-验证当前手机号
    public final static String verifyUserPhone = URL_DOMAIN + "/user/verifyUserPhone";
    //变更手机号-修改登录手机号
    public final static String editUserPhone = URL_DOMAIN + "/user/editUserPhone";

//    //处罚记录
//    public final static String getIllegalList = URL_DOMAIN + "/user/getIllegalList";
//    //处罚-处罚记录详情
//    public final static String getIllegalDetails = URL_DOMAIN + "/user/getIllegalDetails";

    /**
     * 犬只领养
     */
    //处罚-处罚记录列表
    public final static String getLeaveDogPageList = URL_DOMAIN + "/user/dog/getLeaveDogPageList";
    //犬只领养-犬只详情
    public final static String getLeaveDogDetail = URL_DOMAIN + "/user/dog/getLeaveDogDetail";


    /**
     * 悦保
     */
    public final static String getAccessToken = "https://ai.ybinsure.com/s/api/getAccessToken";
    public final static String createPetArchives = "https://ai.ybinsure.com/s/api/createPetArchives";
    public final static String petType = "https://ai.ybinsure.com/s/api/petType";
    public final static String uploadPetImageFindPeytId = "https://ai.ybinsure.com/s/api/uploadPetImageFindPeytId";

    /**
     * 获取OBS信息
     */
    public final static String getObsFormInfo = URL_DOMAIN + "/obs/getObsFormInfo";

    /**
     * 支付
     */
    public final static String viporder_createOrder = URL_DOMAIN + "/viporder/createOrder";//记录邀请信息
    //支付宝_VIP订单_生成支付订单
    public final static String orderCreateAliOrderAboutVip = URL_DOMAIN + "/order/createAliOrderAboutVip";
    //微信_VIP订单_生成支付订单
    public final static String orderCreateWxOrderAboutVip = URL_DOMAIN + "/order/createWxOrderAboutVip";
    public final static String order_aliOrderPayParam = URL_DOMAIN + "/order/aliOrderPayParam";//支付包支付 oid
    public final static String order_wxOrderPayParam = URL_DOMAIN + "/order/wxOrderPayParam";//微信支付 oid

    // ==================================== 犬只管理 =============================================

}
