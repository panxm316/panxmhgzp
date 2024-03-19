package com.hgzp.advertising.pagemodel.price.dto;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * CommissionRateItemDTO
 * 创建人：suny
 * 类描述：计提比例明细实体类
 * 创建日期：2023/11/25 9:28
 */
@Data
public class CommissionRateItemDTO extends BaseDTO {
    /**
     * 主键
     */
    @NotNull(message = "计提比例明细id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 组id
     */
    @NotNull(message = "计提比例id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long commissionrategroupid;

    /**
     * 组名称
     */
    @NotBlank(message = "名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String commissionrategroupname;

    /**
     * 媒体id
     */
    @NotNull(message = "媒体id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long mediaid;

    /**
     * 媒体名称
     */
    @NotBlank(message = "媒体名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String medianame;

    /**
     * 行业id
     */
    @NotNull(message = "行业id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long adindustryid;

    /**
     * 行业名称
     */
    @NotBlank(message = "行业名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String adindustryname;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long deptid;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String deptname;

    /**
     * 人员id
     */
    private Long employid;

    /**
     * 人员名称
     */
    private String employname;

    /**
     * 直接客户默认提成点%
     */
    private BigDecimal nrateofcustomer;

    /**
     * 代理公司默认提成点%
     */
    private BigDecimal nrateofagency;

    /**
     * 内容生产方默认提成点%
     */
    private BigDecimal nrateofagent;
}
