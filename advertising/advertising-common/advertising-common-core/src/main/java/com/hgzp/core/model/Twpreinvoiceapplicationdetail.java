package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 预开发票申请关联合同表
 * </p>
 *
 * @author wwk
 * @since 2023-11-08
 */
@Getter
@Setter
@LogData(alias = "预开发票关联合同表")
public class Twpreinvoiceapplicationdetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 预开发票申请id
     */
    @LogData(alias = "预开发票申请id")
    private Long preinvoiceapplicationid;

    /**
     * 订单表id
     */
    @LogData(alias = "订单号", mappedBy = "tworder", mappedByColumn = "sordernum")
    private Long orderid;

    /**
     * 欠款金额(订单当时的欠款总额)
     */
    @LogData(alias = "欠款金额")
    private BigDecimal namountarrearage;
}
