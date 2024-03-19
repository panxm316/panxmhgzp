package com.hgzp.advertising.pagemodel.ad.dto;

import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * AdResourceFilesDTO
 * 创建人：suny
 * 类描述：广告资源文件表DTO
 * 创建日期：2023/11/11 14:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdResourceFilesDTO extends BaseDTO {
    /**
     * 文件统一文件地址
     */
    private String url;
    /**
     * 文件下载url  视频转码播放地址
     */
    private String durl;
    /**
     * 是否删除
     */
    private Boolean bdelete;
    /**
     * 主键
     */
    private Long id;

    /**
     * 资源申请id
     */
    private Long adresourceapplicationid;

    /**
     * 创建人id
     */
    private Long createempid;

    /**
     * 创建人名称
     */
    private String createempname;

    /**
     * 文件格式
     */
    private String sfileformat;

    /**
     * 统一文件ID
     */
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
     * 1-客户合同协议、8-广告资源
     */
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
    private String medianame;

    /**
     * 审批状态
     */
    private Integer iapprovestatus;
}
