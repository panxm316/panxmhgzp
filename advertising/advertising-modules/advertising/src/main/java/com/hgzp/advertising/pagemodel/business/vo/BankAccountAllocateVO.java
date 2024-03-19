package com.hgzp.advertising.pagemodel.business.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 * BankAccountAllocateVO
 * 创建人：suny
 * 类描述：银行流水分配表 查询实体对象
 * 创建日期：2023/12/9 8:39
 */
@Data
public class BankAccountAllocateVO extends BaseQueryInfo {
    /**
     * 部门id
     */
    private Long deptid;
    /**
     * 主业务员id
     */
    private Long employid;
    /**
     * 代理公司业务员id
     */
    private Long agencyemployid;
    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 发票号
     */
    private String invoice;
    /**
     * 流水表id
     */
    private Long bankaccountid;

    /**
     * 发票表id
     */
    private Long invoiceid;
    /**
     * 指定订单id
     */
    private String orderid;
}

