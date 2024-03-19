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
 * 广告资源文件表
 * </p>
 *
 * @author wwk
 * @since 2023-11-10
 */
@Getter
@Setter
@LogData(alias = "广告资源申请文件表")
public class Twadresourcefiles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(showColumn = "soriginalfile")
    private Long id;

    /**
     * 资源申请id
     */
    @LogData(alias = "资源申请表", mappedBy = "Twadresourceapplication", mappedByColumn = "sadtitle")
    private Long adresourceapplicationid;

    /**
     * 创建人id
     */
    private Long createempid;

    /**
     * 创建人名称
     */
    @LogData(alias = "创建人名称")
    private String createempname;

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
     * 1-客户合同协议、8-广告资源
     */
    @LogData(alias = "文件类型")
    private Integer ifilecategory;

    /**
     * 文件描述
     */
    private String sdescription;

    /**
     * 创建日期
     */
    private Date dcreatedate;

    /**
     * 媒体id
     */
    private Long mediaid;

    /**
     * 媒体名称(附件用途，可选)
     */
    @LogData(alias = "媒体名称")
    private String medianame;

    /**
     * 审批状态
     */
    @LogData(alias = "审批状态")
    private Integer iapprovestatus;
}
