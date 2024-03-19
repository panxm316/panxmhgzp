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
 * 成本表(人员)
 * </p>
 *
 * @author muyn
 * @since 2024-03-15
 */
@Getter
@Setter
@LogData(alias = "成本表(人员)")
public class Twcostemploy implements Serializable {

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
     * 导入时间
     */
    @LogData(alias = "导入时间")
    private Date dcreatetime;

    /**
     * 记账日期
     */
    @LogData(alias = "记账日期")
    private Date daccountdate;

    /**
     * 业务日期
     */
    @LogData(alias = "业务日期")
    private Date dbusinessdate;

    /**
     * 凭据单号
     */
    @LogData(alias = "凭据单号")
    private String scredential;

    /**
     * 摘要
     */
    @LogData(alias = "摘要")
    private String sdescription;

    /**
     * 借方名称
     */
    @LogData(alias = "借方名称")
    private String sborrowername;

    /**
     * 贷方名称
     */
    @LogData(alias = "贷方名称")
    private String slendername;

    /**
     * 金额类型
     */
    @LogData(alias = "金额类型")
    private String scredentialtype;

    /**
     * 金额
     */
    @LogData(alias = "金额")
    private BigDecimal namount;

    /**
     * 分配金额
     */
    @LogData(alias = "分配金额")
    private BigDecimal namountallcate;

    /**
     * 导入历史ID
     */
    @LogData(alias = "导入历史ID")
    private Long bankaccounthistoryid;

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
    private Date dlastoperatedate;

    /**
     * 并发标记
     */
    @LogData(alias = "并发标记")
    private Long version;
}
