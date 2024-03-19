package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.annotation.LogData;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 成本分配明细表
 * </p>
 *
 * @author muyn
 * @since 2024-03-18
 */
@Getter
@Setter
@LogData(alias = "成本分配明细表")
public class Twcostallocate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "主键")
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
     * 成本表id
     */
    @LogData(alias = "成本表id")
    private Long costid;

    /**
     * 成本类型(employ,project)
     */
    @LogData(alias = "成本类型(employ,project)")
    private String costtype;

    /**
     * 分配金额
     */
    @LogData(alias = "分配金额")
    private BigDecimal namountallcate;

    /**
     * 订单明细id
     */
    @LogData(alias = "订单明细id")
    private Long orderitemid;

    /**
     * 说明
     */
    @LogData(alias = "说明")
    private String sdescription;

    /**
     * 状态(0-待提交 1-已提交 2-已确认）
     */
    @LogData(alias = "状态(0-待提交 1-已提交 2-已确认）")
    private Integer istatus;

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
    @LogData(alias = "并发标记")
    private Long version;
}
