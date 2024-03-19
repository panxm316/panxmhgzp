package com.hgzp.advertising.pagemodel.finance.dto;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * BankAccountDTO
 * 创建人：suny
 * 类描述：银行流水单对象实体
 * 创建日期：2023/10/26 13:14
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@LogData(alias = "银行流水单")
public class BankAccountDTO extends BaseDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 借方账号
     */
    @Alias(value = "账号")
    private String sborroweraccount;

    /**
     * 借方名称
     */
    @Alias(value = "账户名称")
    private String sborrowername;

    /**
     * 贷方账号
     */
    @Alias(value = "对方账号")
    private String slenderaccount;

    /**
     * 贷方名称
     */
    @Alias(value = "对方户名")
    private String slendername;

    /**
     * 交易日期
     */
    @Alias(value = "交易日期")
    private String dtradedate;

    /**
     * 凭据种类
     */
    @Alias(value = "凭证种类")
    private String scredentialtype;

    /**
     * 导入金额
     */
    @Alias(value = "贷方发生额")
    private BigDecimal namount;

    /**
     * 已分配金额
     */
//    private BigDecimal namountallocate;

    /**
     * 交易流水号
     */
    @Alias(value = "明细编号—交易流水号")
    private String stradecode;

    /**
     * 明细类型
     */
    @Alias(value = "明细类型")
    private String sdetailtype;

    /**
     * 交易类别
     */
    @Alias(value = "交易类别")
    private String stradetype;

    /**
     * 备注
     */
    @Alias(value = "备注")
    private String sremark;

    /**
     * 导入历史ID
     */
    private Long bankaccounthistoryid;

    /**
     * 导入时间
     */
    private Date dcreatetime;

    /**
     * 最后操作员id
     */
    private Long lastoperatorid;

    /**
     * 最后操作员
     */
    private String lastoperator;

    /**
     * 并发标志
     */
    @Version
    private Long version;
}
