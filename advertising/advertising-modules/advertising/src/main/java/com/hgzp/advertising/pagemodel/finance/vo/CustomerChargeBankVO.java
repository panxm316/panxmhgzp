package com.hgzp.advertising.pagemodel.finance.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 * CustomerChargeBankVO
 * 创建人：suny
 * 类描述：银行流水分配使用的客户收款查询VO
 * 创建日期：2023/12/20 13:11
 */
@Data
public class CustomerChargeBankVO extends BaseQueryInfo {
    /**
     * 主键
     */
    private Long id;
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
    /**
     * 客户id
     */
    private Long customerid;
}

