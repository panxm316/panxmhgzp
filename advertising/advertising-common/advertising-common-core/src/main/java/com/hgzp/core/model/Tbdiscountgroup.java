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
 * 折扣总表
 * </p>
 *
 * @author wwk
 * @since 2023-11-21
 */
@Getter
@Setter
@LogData(alias = "折扣组")
public class Tbdiscountgroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @LogData(showColumn = "discountgroupname")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @LogData(alias = "名称")
    private String discountgroupname;

    /**
     * 年份
     */
    @LogData(alias = "年份")
    private String syear;

    /**
     * 直接客户折扣
     */
    @LogData(alias = "直接客户折扣")
    private BigDecimal ncustomerdiscount;

    /**
     * 代理公司折扣
     */
    @LogData(alias = "代理公司折扣")
    private BigDecimal nagencydiscount;

    /**
     * 内容生产方折扣
     */
    @LogData(alias = "内容生产方折扣")
    private BigDecimal nagentdiscount;

    /**
     * 是否按最高
     */
    @LogData(alias = "是否按最高")
    private Boolean bhighest;

    /**
     * 是否有效
     */
    @LogData(alias = "是否有效")
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 创建者id
     */
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
