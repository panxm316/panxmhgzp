package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 客户账户表
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Getter
@Setter
@LogData(alias = "客户账户")
public class Twcustomeraccounts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "customername")
    private Long id;

    /**
     * 客户id
     */
    @LogData(alias = "客户id")
    private Long customerid;

    /**
     * 客户名称
     */
    @LogData(alias = "客户名称")
    private String customername;

    /**
     * 直接客户、代理公司、内容生产方
     */
    @LogData(alias = "客户类型")
    private Integer icusttype;

    /**
     * 账户余额
     */
    @LogData(alias = "账户余额")
    private BigDecimal namountbalance;

    /**
     * 创建时间
     */
    @LogData(alias = "创建时间")
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
     * 备注
     */
    private String sremark;
}
