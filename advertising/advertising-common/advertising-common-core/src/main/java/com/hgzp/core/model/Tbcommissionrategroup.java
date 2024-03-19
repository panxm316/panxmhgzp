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
 * 计提比例总表
 * </p>
 *
 * @author wwk
 * @since 2023-11-23
 */
@Getter
@Setter
@LogData(alias = "计提比例总表")
public class Tbcommissionrategroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "commissionrategroupname")
    private Long id;

    /**
     * 名称
     */
    @LogData(alias = "计提比例名称")
    private String commissionrategroupname;

    /**
     * 开始日期
     */
    @LogData(alias = "开始日期")
    private Date dstartdate;

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

    /**
     * 风险金比例%
     */
    @LogData(alias = "风险金比例%")
    private BigDecimal nrateofrisk;

    /**
     * 是否有效
     */
    @LogData(alias = "是否有效")
    private Boolean buse;

    /**
     * 是否默认
     */
    @LogData(alias = "是否默认")
    private Boolean bdefault;

    /**
     * 备注
     */
    private String sremark;

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
    private Date createdate;
}
