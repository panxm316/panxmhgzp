package com.hgzp.advertising.pagemodel.media.dto;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * FoldDto
 * 创建人：suny
 * 类描述：叠次提交对象
 * 创建日期：2023/9/20 9:40
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@LogData(alias = "叠次")
public class FoldDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull(message = "叠次id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    @LogData(showColumn = "sname")
    private Long id;

    /**
     * 叠次名称
     */
    @NotBlank(message = "叠次名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "叠次名称")
    private String sname;

    /**
     * 媒体id
     */
    @NotNull(message = "媒体id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @LogData(alias = "媒体名称", mappedBy = "tbmedia", mappedByColumn = "sname")
    private Long mediaid;

    /**
     * 版数
     */
    @NotNull(message = "版数不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    @Pattern(regexp = "^([1-9])[0-9]{2}$", message = "只能在1-100直接")
    @LogData(alias = "版数")
    private Integer pagecount;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 序号
     */
    @LogData(alias = "序号")
    private Integer isort;

    /**
     * 启用标志
     */
    @LogData(alias = "启用")
    private Boolean buse;
}
