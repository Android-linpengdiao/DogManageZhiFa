package com.dog.manage.zhifa.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RecordImmune {

    //犬证办理
    /**
     * "lincenceId":6,
     * "licenceStatus":1,
     * "dogType":"柴犬",
     * "dogAge":1,
     * "createdTime":"2022-05-09"
     */
    private Integer lincenceId;
    private Integer licenceStatus;
    private String createdTime;

    //犬只免疫证
    /**
     * immuneId : 1
     * dogType : 哈士奇
     * createTime : null
     * dogAge : 1
     * lincenceStatus : 2
     */

    private Integer immuneId;
    private String dogType;
    private String createTime;
    private String dogAge;
    private Integer lincenceStatus;
}
