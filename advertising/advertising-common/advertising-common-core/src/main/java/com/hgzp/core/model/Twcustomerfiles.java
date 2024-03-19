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
 * 客户文件表
 * </p>
 *
 * @author wwk
 * @since 2023-10-28
 */
@Getter
@Setter
@LogData(alias = "客户资料")
public class Twcustomerfiles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData()
    private Long id;

    /**
     * 客户id
     */
    @LogData(alias = "客户名称", mappedBy = "twcustomer", mappedByColumn = "sname")
    private Long customerid;

    /**
     * 创建人id
     */
    @LogData(alias = "创建人", mappedBy = "tbemploy", mappedByColumn = "susername")
    private Long createempid;

    /**
     * 文件格式
     */
    private String sfileformat;

    /**
     * 统一文件ID
     */
    @LogData(alias = "统一文件ID")
    private String sfileid;

    /**
     * 文件大小
     */
    private String sfilesize;

    /**
     * 源文件名
     */
    private String soriginalfile;

    /**
     * 创建日期
     */
    private Date dcreatetime;

    /**
     * 文件格式类型(Photo、Video、Audio、Office、Application)
     */
    private String sfiletype;

    /**
     * 文件分类(资质certificate、名片card、出版流程单pageprocessorder、认刊书contract)
     */
    private Integer ifilecategory;

    /**
     * 是否删除
     */
    @LogData(alias = "删除")
    private Boolean bdelete;

    /**
     * 文件描述
     */
    private String sdescription;
}
