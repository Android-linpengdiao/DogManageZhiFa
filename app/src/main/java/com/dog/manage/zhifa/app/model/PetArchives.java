package com.dog.manage.zhifa.app.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PetArchives implements Serializable {

    /**
     * status	int	状态码 200：成功 其他：业务异常
     * data	对象	执行成功时data有对象值，执行异常时不返回data
     * serialNo	string	日志ID
     * message	string	提示信息
     * reqId	string	请求ID，标识唯一请求，全流程跟踪使用
     * code	int	业务码参考code资源表
     */

    private Integer code;
    private DataBean data;
    private String message;
    private Integer status;
    private String serialNo;
    private String reqId;

    @NoArgsConstructor
    @Data
    public static class DataBean implements Serializable {

        /**
         * petId	string	宠物身份ID
         * compareResult	int	比对结果；0：比对成功
         * url	string	图片路径
         * imgIndex	string	图像索引；删除需校验该参数
         * id	int	图片Id；图片资源唯一Id
         */

        private Integer compareResult;
        private String url;
        private String petId;
        private String imgIndex;
        private Integer id;
    }
}
