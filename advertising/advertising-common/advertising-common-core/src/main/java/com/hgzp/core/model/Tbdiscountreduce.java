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
 * 折扣下点
 * </p>
 *
 * @author wwk
 * @since 2023-11-23
 */
@Getter
@Setter
@LogData(alias = "折扣下点")
public class Tbdiscountreduce implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "commissionrategroupname")
    private Long id;

    /**
     * 组id
     */
    @LogData(alias = "组id")
    private Long commissionrategroupid;

    /**
     * 组名称
     */
    @LogData(alias = "组名称")
    private String commissionrategroupname;

    /**
     * 开始折扣（9折）%
     */
    @LogData(alias = "开始折扣")
    private BigDecimal ndiscountstart;

    /**
     * 结束折扣（8折）%
     */
    @LogData(alias = "结束折扣")
    private BigDecimal ndiscountend;

    /**
     * 直接客户下点值%
     */
    @LogData(alias = "直接客户下点值%")
    private BigDecimal nrateofcustomer;

    /**
     * 代理公司下点值%
     */
    @LogData(alias = "代理公司下点值%")
    private BigDecimal nrateofagency;

    /**
     * 内容生产方下点值%
     */
    @LogData(alias = "内容生产方下点值%")
    private BigDecimal nrateofagent;
}
