package com.dog.manage.zhifa.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LicenceInfo implements Serializable {

    /**
     * idNum
     * string
     * 犬证
     * lincenceId
     * integer
     * 犬证id
     * dogType
     * string
     * 犬只类型
     * awardTime
     * string
     * 发证时间
     * dogColor
     * string
     * 犬只颜色
     * dogGender
     * integer
     * 犬只性别;0:雌性， 1：雄性
     * orgName
     * string
     * 犬主名称
     * userId
     * integer
     * 用户id
     * addressId
     * string
     * 住址id
     * dogId
     * integer
     * 犬只id
     * surplusDate
     * integer
     * 剩余时间
     * immuneLicenceStatus
     * integer
     * 办理状态
     * detailedAddress
     * string
     * 详细地址
     */

    private String idNum;
    private Integer lincenceId = 0;
    private String userName;
    private String dogType;
    private String awardTime;
    private String dogColor;
    private Integer dogGender = 0;
    private String orgName;
    private Integer userId = 0;
    private Integer addressId = 0;
    private Integer dogId = 0;
    private Integer surplusDate = 0;
    private Integer immuneLicenceId = 0;
    private Integer immuneLicenceStatus = 0;//1无效2有效
    private String detailedAddress;
    private String dogPhoto;


}
