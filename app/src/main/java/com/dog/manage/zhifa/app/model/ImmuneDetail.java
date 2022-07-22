package com.dog.manage.zhifa.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ImmuneDetail implements Serializable {

    /**
     * immuneId : 15
     * dogType : 金毛寻回犬
     * createTime : 2022-06-01 18:20:21
     * dogAge : 0
     * lincenceStatus : 2
     * userId : 19
     * userName : 李先生
     * hospitalId : 108
     * hospitalName : 太平宠物医院
     * hospitalAddress : 北京市
     * hospitalPhone : 15888888888
     * longitudeLatitude : 116.22519738192517,40.21952491712514
     * dogId : 38
     * immuneName : null
     * immuneBatch : null
     * immuneFactory : null
     * immuneData : null
     * immuneNum : null
     * immuneUser : null
     * nextImmuneData : null
     */

    private Integer immuneId;
    private String dogType;
    private String dogName;
    private String dogColor;
    private Integer dogGender = 0;//犬只性别;0:雌性， 1：雄性
    private String dogAge;
    private String idNum;
    private Integer lincenceStatus;
    private Integer userId;
    private String userName;
    private Integer hospitalId;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalPhone;
    private String longitudeLatitude;
    private Integer dogId;
    private String createTime;
    private String immuneName;
    private String immuneBatch;
    private String immuneFactory;
    private String immuneData;
    private String immuneNum;
    private String immuneUser;
    private String nextImmuneData;
    private String detailedAddress;
    private String streetName;
    private String dogPhoto;
}
