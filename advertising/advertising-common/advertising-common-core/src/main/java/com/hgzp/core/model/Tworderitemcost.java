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
 * 广告成本表
 * </p>
 *
 * @author wwk
 * @since 2023-12-04
 */
@Getter
@Setter
@LogData(alias = "广告成本表")
public class Tworderitemcost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "主键")
    private Long id;

    /**
     * 订单明细表
     */
    @LogData(alias = "订单明细", mappedBy = "tworderitem", mappedByColumn = "sordernum")
    private Long orderitemid;

    /**
     * 创建人id
     */
    @LogData(alias = "创建人id")
    private Long createempid;

    /**
     * 创建人名称
     */
    @LogData(alias = "创建人名称")
    private String createempname;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatedate;

    /**
     * 成本金额
     */
    @LogData(alias = "成本金额")
    private BigDecimal namountcost;

    /**
     * 说明
     */
    private String sdescription;

    /**
     * 状态 0-待提交 1-已提交 2-已确认 3-已退回  
     */
    @LogData(alias = "状态 ")
    private Integer istatus;
}
