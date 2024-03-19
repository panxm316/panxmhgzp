package com.hgzp.advertising.pagemodel.price.dto;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * PriceGroupDTO
 * 创建人：suny
 * 类描述：价格组DTO
 * 创建日期：2023/11/10 14:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceGroupDTO extends BaseDTO {
    /**
     * 主键
     */
    @NotNull(message = "价格组id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 价格表名称
     */
    @NotBlank(message = "价格组名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
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
     * 年份
     */
    @NotBlank(message = "年份不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String syear;

    /**
     * 是否默认
     */
    private Boolean bdefault;

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
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime; // 查询结束时间
    /**
     * 复制标识
     */
    private Boolean copyFlag;
}
