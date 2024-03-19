package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.annotation.LogData;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 数据轧账总表
 * </p>
 *
 * @author muyn
 * @since 2024-01-19
 */
@Getter
@Setter
@LogData(alias = "数据轧账总表")
public class Twdatabackupgroup implements Serializable {

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
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 名称
     */
    @LogData(alias = "名称")
    private String databackupname;

    /**
     * 汇总类型(广告明细-orderitem，核销明细：apportiondetail)
     */
    @LogData(alias = "汇总类型(广告明细-orderitem，核销明细：apportiondetail)")
    private String datatype;

    /**
     * 开始日期
     */
    @LogData(alias = "开始日期")
    private Date dstartdate;

    /**
     * 结束日期
     */
    @LogData(alias = "结束日期")
    private Date denddate;

    /**
     * 记录数
     */
    @LogData(alias = "记录数")
    private Integer nrecordsize;

    /**
     * 明细表名
     */
    @LogData(alias = "明细表名")
    private String sdetailtablename;

    /**
     * 说明
     */
    @LogData(alias = "说明")
    private String sdescription;
}
