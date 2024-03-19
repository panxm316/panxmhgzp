package com.hgzp.advertising.pagemodel.customer.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomerServiceVO
 * 创建人：suny
 * 类描述：客户服务VO
 * 创建日期：2023/11/9 9:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerServiceVO extends BaseQueryInfo {
    /**
     * id
     */
    private Long id;

    /**
     * 人员id
     */
    private Long employid;

    /**
     * 客户id
     */
    private Long customerid;

    /**
     * 0-客户投诉 1-客户建议 2-客户调查
     */
    private Integer iservicetype;

    /**
     * 联系人
     */
    private String scontacts;

    /**
     * 联系方式
     */
    private String sphone;

    /**
     * 操作员id
     */
    private Long operatorid;

    /**
     * 操作员
     */
    private String operatorname;

    /**
     * 状态(可选可填写：已记录、已解决、已结束等)
     */
    private String sstatus;
}
