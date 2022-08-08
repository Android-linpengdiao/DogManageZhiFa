package com.dog.manage.zhifa.app.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UploadPetImageFindPeytId {

    /**
     * code : 0
     * data : {"urls":["http://ai.ybinsure.com/txy-ocr-fs/006001/2022-08-04/518509321210822656.jpg"],"scores":["0.9098037007395325"],"petIds":["04997155-139f-11ed-26vb9lJAo"]}
     * message : success
     * status : 200
     * serialNo : 3a08519041472117342208
     * reqId : 6bcd1272-14c6-26xpDGtV6
     */

    private Integer code;
    private DataBean data;
    private String message;
    private Integer status;
    private String serialNo;
    private String reqId;

    @NoArgsConstructor
    @Data
    public static class DataBean {
        private List<String> urls;
        private List<String> scores;
        private List<String> petIds;
    }
}
