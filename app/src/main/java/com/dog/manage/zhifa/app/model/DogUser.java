package com.dog.manage.zhifa.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DogUser {

    ////用户类型（全）;1：个人 2 ：单位
    public final static int userType_personal = 1;
    public final static int userType_organ = 2;


    /**
     * userType
     * integer
     * 用户类型（全）;1：个人 2 ：单位
     * userName
     * string
     * 法人姓名（单位）
     * idType
     * integer
     * 证件类型（全）;1:身份证 2 营业执照
     * idPhoto
     * string
     * 证件照片/法人证件照（全）格式 ：["XXXX.png"，“xxx.png”]
     * idNum
     * string
     * 身份证号（个人）/法人身份证号
     * dogType
     * integer
     * 养犬类型（个人）;1导盲犬/扶助犬 2 陪伴犬
     * agedProve
     * string
     * 鳏寡老人证明（个人）/残疾证明
     * aged
     * integer
     * 是否鳏寡老人（个人）;0：否 1：是
     * contactPhoneNum
     * string
     * 联系电话（全）
     * address
     * string
     * 居住地址（全）例：012/02/31
     * detailedAddress
     * string
     * 详细地址（全）
     * houseNum
     * string
     * 房本编号（个人）
     * housePhoto
     * string
     * 房产证或租赁合同照片（个人）
     * bizLicense
     * string
     * 单位营业执照（单位）
     * dogManagement
     * string
     * 养犬管理制度（单位）
     * dogDevice
     * string
     * 养犬设施图片（单位）
     * orgName
     * string
     * 个人/企业名称（全）
     * busTypeId
     * string
     * 业务类型 1 个人信息 0 犬证、疫苗
     */
    private Integer id;
    private Integer userType = 0;
    private String bizLicense;
    private String userName;
    private String orgName;
    private Integer idType;
    private String idPhoto;
    private String idNum;
    private Integer dogType;
    private Integer aged = 0;
    private String agedProve;
    private String contactPhoneNum;
    private String address;
    private String detailedAddress;
    private String houseNum;
    private String housePhoto;
    private String dogManagement;
    private String dogDevice;

    private Integer addressId;
}
