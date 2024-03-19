package com.hgzp.advertising.pagemodel.finance.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * BankAccountVO
 * 创建人：suny
 * 类描述：银行流水单查询对象实体
 * 创建日期：2023/10/26 16:14
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountVO extends BaseQueryInfo {
    /**
     * 借方账号
     */
    private String sborroweraccount;

    /**
     * 借方名称
     */
    private String sborrowername;

    /**
     * 贷方账号
     */
    private String slenderaccount;

    /**
     * 贷方名称
     */
    private String slendername;

    /**
     * 交易日期
     */
    private Date dtradedate;

    /**
     * 凭据种类
     */
    private String scredentialtype;

    /**
     * 导入金额
     */
    private BigDecimal namount;

    /**
     * 已分配金额
     */
    private BigDecimal namountallocate;

    /**
     * 交易流水号
     */
    private String stradecode;
}
