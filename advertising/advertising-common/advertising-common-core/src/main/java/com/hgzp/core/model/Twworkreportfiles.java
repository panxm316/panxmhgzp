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
 * 工作报告文件表
 * </p>
 *
 * @author wwk
 * @since 2023-10-28
 */
@Getter
@Setter
@LogData(alias = "工作报告文件")
public class Twworkreportfiles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 工作报告
     */
    @LogData(alias = "工作报告", mappedBy = "Twworkreports", mappedByColumn = "employname")
    private Long workreportid;

    /**
     * 创建人id
     */
    private Long createempid;

    /**
     * 文件格式
     */
    @LogData(alias = "文件格式")
    private String sfileformat;

    /**
     * 统一文件ID
     */
    @LogData(alias = "统一文件ID")
    private String sfileid;

    /**
     * 文件大小
     */
    @LogData(alias = "文件大小")
    private String sfilesize;

    /**
     * 源文件名
     */
    @LogData(alias = "源文件名")
    private String soriginalfile;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 文件格式类型(Photo、Video、Audio、Office、Application)
     */
    @LogData(alias = "文件格式类型")
    private String sfiletype;

    /**
     * 文件分类(0-电子认刊书、 1-客户合同\协议、 2-资质 、3-名片、 4-出版流程单 、5-银行流水)
     */
    @LogData(alias = "文件分类")
    private Integer ifilecategory;

    /**
     * 是否删除
     */
    private Boolean bdelete;

    /**
     * 文件描述
     */
    private String sdescription;
}
