package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import java.util.Date;

/**
 * OrderItemCostFilesDTO
 * 创建人：suny
 * 类描述：TODO 广告成本证明文件表DTO
 * 创建日期：2023/12/13 15:11
 */
@Data
public class OrderItemCostFilesDTO extends BaseDTO {
    /**
     * 是否删除
     */
    private Boolean bdelete;
    /**
     * 文件统一文件地址
     */
    private String url;
    /**
     * 文件下载url  视频转码播放地址
     */
    private String durl;
    /**
     * 主键
     */
    private Long id;

    /**
     * 资源申请id
     */
    private Long orderitemcostid;

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
     * 9-成本证明
     */
    private Integer ifilecategory;

    /**
     * 文件描述
     */
    private String sdescription;
}

