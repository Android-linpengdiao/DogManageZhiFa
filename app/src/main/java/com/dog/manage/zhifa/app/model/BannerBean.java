package com.dog.manage.zhifa.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BannerBean implements Serializable {


    /**
     * searchValue : null
     * createBy : null
     * createTime : null
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * id : 1
     * status : 0
     * sqeNum : 1
     * imageUrl : 121
     * createdBy : 1
     * createdTime : 2022-04-29
     * updatedBy : null
     * updatedTime : null
     */

    private Object searchValue;
    private Object createBy;
    private Object createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBean params;
    private Integer id;
    private Integer status;
    private Integer sqeNum;
    private String imageUrl;
    private Integer createdBy;
    private String createdTime;
    private Object updatedBy;
    private Object updatedTime;

    @NoArgsConstructor
    @Data
    public static class ParamsBean {
    }
}
