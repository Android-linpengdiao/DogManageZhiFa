package com.dog.manage.zhifa.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddressBean {

    /**
     * searchValue : null
     * createBy : null
     * createTime : null
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * id : 110101
     * areaName : 东城区
     * parentId : 110100
     * shortName :
     * level : 3
     * flag : 1
     * wgs84Lng : 116
     * wgs84Lat : 39
     * gcj02Lng : 116
     * gcj02Lat : 39
     * bd09Lng : 116
     * bd09Lat : 39
     */

    private Object searchValue;
    private Object createBy;
    private Object createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBean params;
    private Integer id;
    private String areaName;
    private Integer parentId;
    private String shortName;
    private Integer level;
    private Integer flag;
    private Integer wgs84Lng;
    private Integer wgs84Lat;
    private Integer gcj02Lng;
    private Integer gcj02Lat;
    private Integer bd09Lng;
    private Integer bd09Lat;

    @NoArgsConstructor
    @Data
    public static class ParamsBean {
    }
}
