package com.hgzp.advertisingsys.pagemodel.media.dto;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * MediaDTO
 * 创建人：suny
 * 类描述：TODO
 * 创建日期：2023/9/20 12:38
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaDTO extends BaseDTO {
    @NotNull(message = "媒体id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 媒体名称
     */
    @NotBlank(message = "媒体名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "媒体名称")
    private String sname;

    /**
     * 媒体类型key
     */
    @NotBlank(message = "媒体类型key不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    // @LogData(alias = "媒体类型key")
    private String mediatypekey;

    /**
     * 媒体类型
     */
    @NotBlank(message = "媒体类型不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "媒体类型")
    private String mediatypename;

    /**
     * 媒体编码
     */
    @NotBlank(message = "媒体编码不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "媒体编码")
    private String scode;

    /**
     * 媒体序列号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 启用标志
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
