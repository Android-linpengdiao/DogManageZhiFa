package com.dog.manage.zhifa.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PunishRecord implements Serializable {


    private Object searchValue;
    private Object createBy;
    private Object createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private Integer id;
    private String illegalFileUrl;
    private Integer illegalTypeId;
    private String illegalDescribe;
    private String illegalMeasure;
    private String illegalTime;
    private Integer createdBy;
    private String createdTime;
    private Object updatedBy;
    private String updatedTime;
    private Integer userId;
    private String dogLicenceNum;
    private Integer creatorUnitId;
    private String orgName;
    private String idNum;
    private String phoneNum;
    private String loginPhoneNum;
    private String detailedAddress;
    private Integer addressArea;
    private Integer villageId;
    private Integer unitId;
    private String acceptUnit;
    private String unitName;
    private String userName;
}
