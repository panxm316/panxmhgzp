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
 * 成本表(项目)
 * </p>
 *
 * @author muyn
 * @since 2024-03-15
 */
@Getter
@Setter
@LogData(alias = "成本表(项目)")
public class Twcostproject implements Serializable {

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
     * 项目代码
     */
    @LogData(alias = "项目代码")
    private String sprojectcode;

    /**
     * 活动名称
     */
    @LogData(alias = "活动名称")
    private String sprojectname;

    /**
     * 科目名称
     */
    @LogData(alias = "科目名称")
    private String ssubjectname;

    /**
     * 科目代码
     */
    @LogData(alias = "科目代码")
    private String ssubjectcode;

    /**
     * 期初借方
     */
    @LogData(alias = "期初借方")
    private String sfirstborrower;

    /**
     * 期初贷方
     */
    @LogData(alias = "期初贷方")
    private String sfirstlender;

    /**
     * 本期借方
     */
    @LogData(alias = "本期借方")
    private BigDecimal ncurrentborrower;

    /**
     * 分配金额
     */
    @LogData(alias = "分配金额")
    private BigDecimal namountallcate;

    /**
     * 本期贷方
     */
    @LogData(alias = "本期贷方")
    private BigDecimal ncurrentlender;

    /**
     * 本年借方
     */
    @LogData(alias = "本年借方")
    private BigDecimal nyearborrower;

    /**
     * 本年贷方
     */
    @LogData(alias = "本年贷方")
    private BigDecimal nyearlender;

    /**
     * 期末借方
     */
    @LogData(alias = "期末借方")
    private BigDecimal nendborrower;

    /**
     * 期末贷方
     */
    @LogData(alias = "期末贷方")
    private BigDecimal nendlender;

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
