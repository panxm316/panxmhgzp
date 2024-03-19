package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.hgzp.core.annotation.LogData;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 预开发票申请文件表
 * </p>
 *
 * @author wwk
 * @since 2023-11-08
 */
@Getter
@Setter
@LogData(alias = "预开发票申请文件表")
public class Twpreinvoiceapplicationfile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 预开发票申请id
     */
    @LogData(alias = "预开发票申请id")
    private Long preinvoiceapplicationid;

    /**
     * 创建人id
     */
    @LogData(alias = "创建者id", mappedBy = "tbemploy", mappedByColumn = "susername")
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
     * 2-资质
     */
    @LogData(alias = "文件类型")
    private Integer ifilecategory;

    /**
     * 是否删除
     */
    @LogData(alias = "是否删除")
    private Boolean bdelete;

    /**
     * 文件描述
     */
    @LogData(alias = "文件描述")
    private String sdescription;
}
