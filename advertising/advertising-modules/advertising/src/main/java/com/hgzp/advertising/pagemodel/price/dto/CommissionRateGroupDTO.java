package com.hgzp.advertising.pagemodel.price.dto;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * CommissionRateGroupDTO
 * 创建人：suny
 * 类描述：计提比例总表DTO
 * 创建日期：2023/11/24 10:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionRateGroupDTO extends BaseDTO {
    /**
     * 主键
     */
    @NotNull(message = "计提比例id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String commissionrategroupname;

    /**
     * 开始日期
     */
    private Date dstartdate;

    /**
     * 直接客户默认提成点%
     */
    @NotNull(message = "直接客户默认提成点不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private BigDecimal nrateofcustomer;

    /**
     * 代理公司默认提成点%
     */
    @NotNull(message = "代理公司默认提成点不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private BigDecimal nrateofagency;

    /**
     * 内容生产方默认提成点%
     */
    @NotNull(message = "内容生产方默认提成点不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private BigDecimal nrateofagent;
    /**
     * 风险金比例%
     */
    @NotNull(message = "风险金比例不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private BigDecimal nrateofrisk;
    /**
     * 是否有效
     */
    private Boolean buse;

    /**
     * 是否默认
     */
    private Boolean bdefault;

    /**
     * 备注
     */
    private String sremark;

    /**
     * 创建者id
     */
    private Long createempid;

    /**
     * 创建者名称
     */
    private String createempname;

    /**
     * 创建日期
     */
    private Date createdate;
    /**
     * 复制标识
     */
    private Boolean copyFlag;
}
