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
 * 银行流水导入历史表
 * </p>
 *
 * @author wwk
 * @since 2023-10-28
 */
@Getter
@Setter
@LogData(alias = "银行流水导入历史")
public class Twbankaccounthistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "soriginalfile")
    private Long id;

    /**
     * 操作员id
     */
    @LogData(alias = "操作员id")
    private Long employid;

    /**
     * 操作员名称
     */
    @LogData(alias = "操作员名称")
    private String employname;

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
     * 5 文件类型(银行流水bankaccount)
     */
    @LogData(alias = "文件类型")
    private Integer ifilecategory;

    /**
     * 文件描述
     */
    @LogData(alias = "文件描述")
    private String sdescription;
}
