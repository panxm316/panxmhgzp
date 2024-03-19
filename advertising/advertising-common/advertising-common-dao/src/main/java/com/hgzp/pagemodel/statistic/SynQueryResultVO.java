package com.hgzp.pagemodel.statistic;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * SynQueryResultVO
 * 创建人：lhl
 * 类描述：综合查询结果
 * 创建日期：2024/2/26 12:54
 */
@Data
public class SynQueryResultVO {
    // 合同号 订单表
    String scontractnum;
    // 预定日期 订单表
    Date createtime;
    // 见报日期 订单明细表
    Date prestartdate;
    // 客户名称
    String customername;
    // 应收金额
    BigDecimal amountreceivable;
    // 欠款金额
    BigDecimal amountarrearage;
    // 已收金额
    BigDecimal amountreceived;
    // 广告标题
    String  sadtitle;
    // 代理名称
    String  agencyname;
    // 广告类型
    String  adtypename;
    // 广告行业
    String  adindustryname;
    // 部门
    String  deptname;
    // 业务员
    String  employname;
    // 发票号
    String  invoice;
    // 发票金额
    BigDecimal  namount;
    // 开票日期
    Date  dcreatetime;
    // 分摊日期
    Date  dapportiondate;
    // 分摊金额
    BigDecimal  namountapportion;
    // 订单备注
    String  sremark;

}


