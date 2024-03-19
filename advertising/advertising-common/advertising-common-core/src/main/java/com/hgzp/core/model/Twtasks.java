package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 任务额度表
 * </p>
 *
 * @author wwk
 * @since 2023-10-31
 */
@Getter
@Setter
public class Twtasks implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 操作员id
     */
    private Long operatorid;

    /**
     * 操作员
     */
    private String operatorname;

    /**
     * 任务分类：年度、月度
     */
    private String stasktype;

    /**
     * 人员分类：部门、人员
     */
    private String spersonaltype;

    /**
     * 部门id
     */
    private Long deptid;

    /**
     * 部门
     */
    private String deptname;

    /**
     * 人员id
     */
    private Long employid;

    /**
     * 人员
     */
    private String employname;

    /**
     * 媒体id
     */
    private Long mediaid;

    /**
     * 媒体
     */
    private String medianame;

    /**
     * 任务日期 年：2023 月2023-01
     */
    private String staskdate;

    /**
     * 任务额度（万元）
     */
    private BigDecimal ntaskamount;

    /**
     * 创建日期
     */
    private Date dcreatetime;

    /**
     * 启用标记
     */
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;
}
