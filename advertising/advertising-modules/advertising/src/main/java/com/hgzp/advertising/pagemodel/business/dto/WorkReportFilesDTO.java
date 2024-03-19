package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * WorkReportFilesDTO
 * 创建人：suny
 * 类描述：工作报告相关附件实体对象
 * 创建日期：2023/10/30 10:08
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkReportFilesDTO extends BaseDTO {
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
    @NotNull(message = "工作报告文件id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 工作报告
     */
    @NotNull(message = "工作报告id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long workreportid;

    /**
     * 创建人id
     */
    private Long createempid;

    /**
     * 文件格式
     */
    @NotBlank(message = "文件格式不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sfileformat;

    /**
     * 统一文件ID
     */
    @NotBlank(message = "统一文件ID不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sfileid;

    /**
     * 文件大小
     */
    @NotBlank(message = "文件大小不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sfilesize;

    /**
     * 源文件名
     */
    @NotBlank(message = "源文件名不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
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
     * 文件分类(0-电子认刊书、 1-客户合同\协议、 2-资质 、3-名片、 4-出版流程单 、5-银行流水)
     */
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
