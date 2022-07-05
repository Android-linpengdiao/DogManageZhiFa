package com.dog.manage.zhifa.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CommunityBean {

    /**
     * searchValue : null
     * createBy : null
     * createTime : null
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * id : 1
     * communityName : 天桥小区
     * communityAddress : 北京市西城区天桥艺术中心1层
     * communityDept : 103
     * createdBy : 1
     * createdTime : 2022-05-21
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
    private String communityName;
    private String communityAddress;
    private Integer communityDept;
    private Integer createdBy;
    private String createdTime;
    private Object updatedBy;
    private Object updatedTime;

    @NoArgsConstructor
    @Data
    public static class ParamsBean {
    }
}
