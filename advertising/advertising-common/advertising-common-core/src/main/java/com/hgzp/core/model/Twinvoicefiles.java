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
 * 发票文件表(用于存放电子发票)
 * </p>
 *
 * @author muyn
 * @since 2023-12-26
 */
@Getter
@Setter
@LogData(alias = "发票文件表(用于存放电子发票)")
public class Twinvoicefiles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "主键")
    private Long id;

    /**
     * 发票表id
     */
    @LogData(alias = "发票表id")
    private Long invoiceid;

    /**
     * 发票编码
     */
    @LogData(alias = "发票编码")
    private String invoicecode;

    /**
     * 创建人id
     */
    @LogData(alias = "创建人id")
    private Long createempid;

    /**
     * 创建人名称
     */
    @LogData(alias = "创建人名称")
    private String createempname;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

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
     * 发票号
     */
    @LogData(alias = "发票号")
    private String invoice;

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
     * 文件格式类型(Photo、Video、Audio、Office、Application)
     */
    @LogData(alias = "文件格式类型(Photo、Video、Audio、Office、Application)")
    private String sfiletype;
}
