package com.dog.manage.zhifa.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DogDetail {

    /**
     * id
     * integer
     * 主键
     * idNum
     * string
     * 编号
     * dogName
     * string
     * 犬只名称
     * dogColor
     * string
     * 犬只颜色
     * dogGender
     * integer
     * 犬只性别;0:雌性， 1：雄性
     * sterilization
     * integer
     * 是否绝育;0：否 1：是
     * sterilizationProve
     * null
     * 绝育证明
     * immuneStatus
     * integer
     * 免疫状态 0未免疫 1 已免疫
     * immuneExprie
     * string
     * 免疫到期时间
     * dogPhoto
     * string
     * 犬只照片
     * dogType
     * string
     * 犬只品种
     * dogShape
     * integer
     * 犬只体型 0 小型 1 中型
     * leaveCenter
     * string
     * 留检所信息
     * status
     * integer
     * 状态;1待领养、2办理中、3已领养
     * dogAge
     * null
     * 犬只年龄;记录月份
     * leaveCenterId
     * integer
     * 留检所机构id
     * phone
     * string
     * 手机号
     * centerAddress
     * string
     * 留监所地址
     */

    private Object searchValue;
    private Object createBy;
    private String createTime;
    private Object updateBy;
    private String updateTime;
    private String remark;
    private ParamsBean params;
    private Integer id;
    private String idNum;
    private String dogName;
    private String dogColor;
    private Integer dogGender = 0;//犬只性别;0:雌性， 1：雄性
    private Integer sterilization = 0;//是否绝育;0：否 1：是
    private String sterilizationProve;
    private String dogAge;
    private String dogPhoto;
    private String dogType;
    private String noseprint;
    private Integer status;
    private Integer createdBy;
    private String createdTime;
    private Integer updatedBy;
    private String updatedTime;

    private String phone;//手机号
    private String leaveCenter;//留检所信息
    private String centerAddress;//留监所地址
    private Integer dogShape = 0;//犬只体型 0 小型 1 中型
    private Integer immuneStatus = 0;//免疫状态 0未免疫 1 已免疫
    private String immuneExprie;//免疫到期时间

    @NoArgsConstructor
    @Data
    public static class ParamsBean {
    }
}
