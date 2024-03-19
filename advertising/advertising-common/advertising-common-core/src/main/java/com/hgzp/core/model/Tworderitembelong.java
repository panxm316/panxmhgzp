package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.annotation.LogData;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单明细归属表
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Accessors(chain = true)
@Getter
@Setter
@LogData(alias = "订单明细归属表")
public class Tworderitembelong implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单明细id
     */
    @LogData(alias = "订单id", mappedBy = "tworderitem", mappedByColumn = "sordernum")
    private Long orderitemid;

    /**
     * 订单号
     */
    @LogData(alias = "订单号")
    private String sordernum;

    /**
     * 合同号
     */
    @LogData(alias = "合同号")
    private String scontractnum;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id", mappedBy = "tbemploy", mappedByColumn = "susername")
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
    private Date createtime;

    /**
     * 部门id
     */
    @LogData(alias = "部门id" , mappedBy = "tbdept", mappedByColumn = "sdeptname")
    private Long deptid;

    /**
     * 部门名称
     */
    private String deptname;

    /**
     * 业务id
     */
    @LogData(alias = "业务id", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long employid;

    /**
     * 业务员名称
     */
    @LogData(alias = "业务员名称")
    private String employname;

    /**
     * 类型（0-直客业务员 1-代理业务员 2-内容生产方业务员）
     */
    @LogData(alias = "类型")
    private Integer employtype;

    /**
     * 业绩比例
     */
    @LogData(alias = "业绩比例")
    private BigDecimal archievementrate;

    /**
     * 任务比例
     */
    @LogData(alias = "任务比例")
    private BigDecimal taskrate;

    /**
     * 佣金比例
     */
    @LogData(alias = "佣金比例")
    private BigDecimal commissionrate;

    /**
     * 是否主业务员
     */
    private Boolean bprimary;

    /**
     * 备注
     */
    private String sremark;
}
