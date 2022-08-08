package com.dog.manage.zhifa.app.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PetType implements Serializable {

    /**
     * {"code":0,"data":{"pet":[{"identification":[{"english_name":"Shiba","chinese_name":"日本柴犬","confidence":0.89}]}],"petType":"dog","url":"http://ai.ybinsure.com/ocr-fs-9888/topics/s/006003/7214/2022-05-06/486003644257271808"},"message":"success","status":200,"serialNo":"486003642206257152","reqId":"be145202-cd22-24iJIYdFG"}
     */

    /**
     * status	int	状态码 200：成功；其他：业务异常
     * data	对象	执行成功时data有对象值，执行异常时不返回data
     * serialNo	string	日志ID
     * message	string	提示信息
     * code	int	业务码参考code资源表
     * reqId	string	请求ID，标识唯一请求，全流程跟踪使用
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
         * pet	对象	宠物信息
         * petType	string	宠物类型 dog=狗；cat=猫
         * url	string	图片地址
         */
        private List<PetBean> pet;
        private String petType;
        private String url;

        @NoArgsConstructor
        @Data
        public static class PetBean implements Serializable {
            private List<IdentificationBean> identification;

            @NoArgsConstructor
            @Data
            public static class IdentificationBean implements Serializable {
                /**
                 * english_name	string	品种的英文名称
                 * chinese_name	string	品种的中文名称
                 * confidence	double	置信度
                 */
                private String english_name;
                private String chinese_name;
                private Double confidence;
            }
        }
    }
}
