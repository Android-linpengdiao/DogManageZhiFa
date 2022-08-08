package com.dog.manage.zhifa.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DogByNoseprint {

    private Integer immuneLicenceLogId;
    private Integer dogId;
    private String dogType;
    private String dogAge;
    private String createdTime;
    private String acceptUnit;
    private Integer unitId;
    private String immuneUser;
    private Integer immuneStatus;//疫苗状态 1 已预约（未注册） 2 已办结
    private String dogUserName;
    private Integer dogUserId;

}
