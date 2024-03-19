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
 * 折扣明细表
 * </p>
 *
 * @author wwk
 * @since 2023-11-21
 */
@Getter
@Setter
@LogData(alias = "折扣明细")
public class Tbdiscountitem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 折扣组id
     */
    @LogData(alias = "折扣组名称", mappedBy = "tbdiscountgroup", mappedByColumn = "discountgroupname")
    private Long discountgroupid;

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
     * 直接客户
     */
    @LogData(alias = "直接客户")
    private Boolean bcustomer;

    /**
     * 代理公司
     */
    @LogData(alias = "代理公司")
    private Boolean bagency;

    /**
     * 内容生产方
     */
    @LogData(alias = "内容生产方")
    private Boolean bagent;

    /**
     * 是否大客户
     */
    @LogData(alias = "是否大客户")
    private Boolean bvip;

    /**
     * 折扣
     */
    @LogData(alias = "折扣")
    private BigDecimal ndiscount;

    /**
     * 备注
     */
    private String sremark;
}
