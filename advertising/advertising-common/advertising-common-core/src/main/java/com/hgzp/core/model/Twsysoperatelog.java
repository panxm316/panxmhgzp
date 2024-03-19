package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.hgzp.core.emnus.LogTypes;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统管理操作日志
 * </p>
 *
 * @author wwk
 * @since 2023-09-08
 */
@Getter
@Setter
public class Twsysoperatelog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 日志类型(新增、删除、修改)
     */
    private String slogtype;

    /**
     * 日志操作名称
     */
    private String slogname;

    /**
     * 操作表名
     */
    private String stablename;

    /**
     * 操作记录主表id
     */
    private Long recordmasterid;

    /**
     * 操作记录从表id
     */
    private Long recordslaveid;

    /**
     * 旧值
     */
    private String soldvalue;

    /**
     * 新值
     */
    private String snewvalue;

    /**
     * 操作员id
     */
    private Long employid;

    /**
     * 操作员名称
     */
    private String employname;

    /**
     * 操作ip
     */
    private String soperateip;

    /**
     * 操作mac
     */
    private String soperatemac;

    /**
     * 操作日期
     */
    private Date soperatetime;

    /**
     * 操作sql
     */
    private String soperatesql;

    /**
     * 操作sql参数
     */
    private String soperatesqlparam;

    /**
     * 备注
     */
    private String sremark;
}
