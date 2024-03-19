package com.hgzp.advertising.pagemodel.price.dto;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * AdDisplayFormDTO
 * 创建人：suny
 * 类描述：广告形式提交对象
 * 创建日期：2023/11/6 9:56
 *
 * @测试：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@LogData(alias = "广告形式")
public class AdDisplayFormDTO extends BaseDTO {
    /**
     * 主键
     */
    @NotNull(message = "广告形式id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 广告形式名称
     */
    @NotBlank(message = "广告形式名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String sname;

    /**
     * 媒体类型key
     */
    @NotBlank(message = "媒体类型key不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String mediatypekey;

    /**
     * 媒体类型名称
     */
    @NotBlank(message = "媒体类型名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String mediatypename;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 启用标志
     */
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;
}
