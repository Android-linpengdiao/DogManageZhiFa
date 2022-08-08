package com.dog.manage.zhifa.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VipOrderBean implements Serializable {

    private Integer assemble;
    private Integer assembleId;
    private Integer assembleItemId;
    private Integer bargain;
    private Integer bargainId;
    private Integer bargainItemId;
    private BusinessInfoBean businessInfo;
    private Long ctime;
    private String desc;
    private String ext;
    private Integer id;
    private Integer oid;
    private Long overtime;
    private Integer payDay;
    private Integer payModel;
    private Integer payPrice;
    private Long payTime;
    private Integer payType;
    private Integer pid;
    private Integer price;
    private Long refundDayTime;
    private Integer statisticsStatus;
    private Integer status;
    private Integer subid;
    private Integer type;
    private Integer uid;
    private Long utime;

    @NoArgsConstructor
    @Data
    public static class BusinessInfoBean {
    }
}
