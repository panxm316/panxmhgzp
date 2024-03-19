package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 人员假期表，假期期间工作移交给其他人员
 * </p>
 *
 * @author wwk
 * @since 2023-10-23
 */
@Getter
@Setter
@LogData(alias = "人员休假信息")
public class Twempholidays implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "employname")
    private Long id;

    /**
     * 人员ID
     */
    @LogData(alias = "休假人员id")
    private Long employid;

    /**
     * 人员名称
     */
    @LogData(alias = "休假人员名称")
    private String employname;

    /**
     * 开始日期
     */
    @LogData(alias = "休假开始时间")
    private Date dstartdate;

    /**
     * 结束日期
     */
    @LogData(alias = "休假结束时间")
    private Date denddate;

    /**
     * 接手人员ID
     */
    @LogData(alias = "接手人员id")
    private Long newemployid;

    /**
     * 接手人员名称
     */
    @LogData(alias = "接手人员名称")
    private String newemployname;

    /**
     * 操作员ID
     */
    @LogData(alias = "操作人员id")
    private Long operatorid;

    /**
     * 操作员名称
     */
    @LogData(alias = "操作人员名称")
    private String operatorname;

    /**
     * 创建日期
     */
    private Date dcreatedate;

    /**
     * 是否启用
     */
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;
}
