package com.dog.manage.zhifa.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PoliciesBean implements Serializable {

    /**
     * searchValue : null
     * createBy : admin
     * createTime : 2022-04-25 13:48:25
     * updateBy : admin
     * updateTime : 2022-04-29 18:15:14
     * remark : 管理员
     * params : {}
     * noticeId : 3
     * noticeTitle : 维护通知：2018-07-01 若依系统凌晨维护
     * imageUrl : 1112121
     * noticeType : 3
     * noticeContent : 维护内容
     * status : 0
     */

    private Object searchValue;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String updateTime;
    private String remark;
    private ParamsBean params;
    private Integer noticeId;
    private String noticeTitle;
    private String imageUrl;
    private String noticeType;
    private String noticeContent;
    private String status;

    @NoArgsConstructor
    @Data
    public static class ParamsBean implements Serializable{
    }
}
