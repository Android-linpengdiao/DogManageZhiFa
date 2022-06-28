package com.dog.manage.zhifa.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Message implements Serializable {


    /**
     * searchValue : null
     * createBy : admin
     * createTime : 2022-04-25 13:48:25
     * updateBy :
     * updateTime : 2022-06-26 21:48:47
     * remark : 管理员
     * params : {}
     * noticeId : 2
     * noticeTitle : 维护通知：2018-07-01 系统凌晨维护
     * imageUrl : null
     * noticeType : 1
     * noticeContent : 维护内容
     * status : 0
     * userId : null
     * sonType : null
     * readStatus : 1
     */

    private String searchValue;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String updateTime;
    private String remark;
    private Integer noticeId;
    private String noticeTitle;
    private String imageUrl;
    private String noticeType;
    private String noticeContent;
    private String status;
    private Integer userId;
    private Integer sonType;//1 犬证办理 2 免疫证办理 3 犬证年审提醒 4 犬只过户 5 领养 6 犬只注销 7 犬只变更
    private Integer readStatus;//0 是未读
}
