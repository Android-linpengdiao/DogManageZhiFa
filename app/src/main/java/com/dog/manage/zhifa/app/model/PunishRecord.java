package com.dog.manage.zhifa.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PunishRecord implements Serializable {

    /**
     * id
     * integer
     * 处罚id
     * illegalFileUrl
     * string
     * 处罚图片
     * illegalTypeId
     * integer
     * 处罚类型 1 犬只伤人 2 犬吠 3 未牵狗绳 4 其他
     * illegalDescribe
     * string
     * 事件描述
     * illegalMeasure
     * string
     * 措施
     * illegalTime
     * string
     * 违法时间
     */

    private Integer id;
    private String illegalFileUrl;
    private Integer illegalTypeId;
    private String illegalDescribe;
    private String illegalMeasure;
    private String illegalTime;
}
