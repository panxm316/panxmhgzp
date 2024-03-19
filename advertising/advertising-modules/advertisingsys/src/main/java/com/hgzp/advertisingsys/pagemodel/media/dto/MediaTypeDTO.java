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
 * MediaTypeDTO
 * 创建人：suny
 * 类描述：媒体类型提交对象
 * 创建日期：2023/9/20 12:58
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaTypeDTO extends BaseDTO {
    /**
     * id
     */
    @NotNull(message = "媒体id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 媒体类型名称
     */
    @NotBlank(message = "类型名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "媒体类型名称")
    private String sname;

    /**
     * 媒体类型关键字
     */
    @NotBlank(message = "类型关键字不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "媒体类型关键字")
    private String skey;

    /**
     * 描述
     */
    private String sdesc;

    /**
     * 是否默认
     */
    private Boolean bdefault;

    /**
     * 排序
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 是否启用
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
