package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 银行流水单
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Getter
@Setter
@LogData(alias = "银行流水单")
public class Twbankaccounts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "sborroweraccount")
    private Long id;

    /**
     * 借方账号
     */
    @LogData(alias = "借方账号")
    private String sborroweraccount;

    /**
     * 借方名称
     */
    @LogData(alias = "借方名称")
    private String sborrowername;

    /**
     * 贷方账号
     */
    @LogData(alias = "贷方账号")
    private String slenderaccount;

    /**
     * 贷方名称
     */
    @LogData(alias = "贷方名称")
    private String slendername;

    /**
     * 交易日期
     */
    @LogData(alias = "交易日期")
    private Date dtradedate;

    /**
     * 凭据种类
     */
    @LogData(alias = "凭据种类")
    private String scredentialtype;

    /**
     * 导入金额
     */
    @LogData(alias = "导入金额")
    private BigDecimal namount;

    /**
     * 已分配金额
     */
    @LogData(alias = "已分配金额")
    private BigDecimal namountallocate;

    /**
     * 交易流水号
     */
    @LogData(alias = "交易流水号")
    private String stradecode;

    /**
     * 明细类型
     */
    @LogData(alias = "明细类型")
    private String sdetailtype;

    /**
     * 交易类别
     */
    @LogData(alias = "交易类别")
    private String stradetype;

    /**
     * 备注
     */
    @LogData(alias = "备注")
    private String sremark;

    /**
     * 导入历史ID
     */
    @LogData(alias = "导入历史ID")
    private Long bankaccounthistoryid;

    /**
     * 导入时间
     */
    @LogData(alias = "导入时间")
    private Date dcreatetime;

    /**
     * 最后操作员id
     */
    @LogData(alias = "最后操作员id")
    private Long lastoperatorid;

    /**
     * 最后操作员
     */
    @LogData(alias = "最后操作员")
    private String lastoperator;

    /**
     * 并发标志
     */
    @Version
    private Long version;
}
