package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 银行流水分配表
 * </p>
 *
 * @author wwk
 * @since 2023-12-04
 */
@Getter
@Setter
@LogData(alias = "银行流水分配表")
public class Twbankaccountallocate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "invoice")
    private Long id;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id")
    private Long createempid;

    /**
     * 创建者名称
     */
    @LogData(alias = "创建者名称")
    private String createempname;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 流水表id
     */
    @LogData(alias = "流水表id")
    private Long bankaccountid;

    /**
     * 发票表id
     */
    @LogData(alias = "发票表id")
    private Long invoiceid;

    /**
     * 发票号
     */
    @LogData(alias = "发票号")
    private String invoice;

    /**
     * 分配金额
     */
    @LogData(alias = "分配金额")
    private BigDecimal namountallcate;

    /**
     * 是否指定订单
     */
    @LogData(alias = "是否指定订单")
    private Boolean bassignorder;

    /**
     * 指定订单id
     */
    @LogData(alias = "指定订单id")
    private Long orderid;

    /**
     * 合同号
     */
    @LogData(alias = "合同号")
    private String scontractnum;

    /**
     * 说明
     */
    @LogData(alias = "说明")
    private String sdescription;

    /**
     * 状态(0-待提交 1-已提交 2-已确认 3-已退回 4-已核销 ）
     */
    @LogData(alias = "状态")
    private Integer istatus;

    /**
     * 备注
     */
    @LogData(alias = "备注")
    private String sremark;

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
     * 最后修改日期
     */
    @LogData(alias = "最后修改日期")
    private Date dlastmodifydate;

    /**
     * 并发标记
     */
    @Version
    private Long version;
}
