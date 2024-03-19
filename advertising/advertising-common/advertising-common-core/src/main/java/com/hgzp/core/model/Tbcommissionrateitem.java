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
 * 计提比例明细表
 * </p>
 *
 * @author wwk
 * @since 2023-11-23
 */
@Getter
@Setter
@LogData(alias = "计提比例明细表")
public class Tbcommissionrateitem implements Serializable {

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
    @LogData(alias = "计提比例组id")
    private Long commissionrategroupid;

    /**
     * 组名称
     */
    @LogData(alias = "计提比例组名称")
    private String commissionrategroupname;

    /**
     * 媒体id
     */
    @LogData(alias = "媒体id")
    private Long mediaid;

    /**
     * 媒体名称
     */
    @LogData(alias = "媒体名称")
    private String medianame;

    /**
     * 行业id
     */
    @LogData(alias = "行业id")
    private Long adindustryid;

    /**
     * 行业名称
     */
    @LogData(alias = "行业名称")
    private String adindustryname;

    /**
     * 部门id
     */
    @LogData(alias = "部门id")
    private Long deptid;

    /**
     * 部门名称
     */
    @LogData(alias = "部门名称")
    private String deptname;

    /**
     * 人员id
     */
    @LogData(alias = "人员id")
    private Long employid;

    /**
     * 人员名称
     */
    @LogData(alias = "人员名称")
    private String employname;

    /**
     * 直接客户默认提成点%
     */
    @LogData(alias = "直接客户默认提成点%")
    private BigDecimal nrateofcustomer;

    /**
     * 代理公司默认提成点%
     */
    @LogData(alias = "代理公司默认提成点%")
    private BigDecimal nrateofagency;

    /**
     * 内容生产方默认提成点%
     */
    @LogData(alias = "内容生产方默认提成点%")
    private BigDecimal nrateofagent;
}
