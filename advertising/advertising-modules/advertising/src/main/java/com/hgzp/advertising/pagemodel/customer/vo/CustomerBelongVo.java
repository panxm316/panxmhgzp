package com.hgzp.advertising.pagemodel.customer.vo;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Twcustomerbelong;
import lombok.Data;

/**
 CustomerBelongVo
 创建人：songly
 类描述：TODO
 创建日期：2023/10/26 10:48
 */
@Data
public class CustomerBelongVo extends Twcustomerbelong {
    /** 业务员名称 */
    private  String customerName;
    /** 申请表名称 */
    private  String applicationName;
    /**
     * 人员id
     */
    private Long employid;
    /**
     * 是否当前人员（0：否 1：是，如果是则表示只查询当前人员）
     */
    private Boolean bcurflag;
}