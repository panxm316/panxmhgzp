package com.hgzp.advertising.pagemodel.price.dto;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DiscountReduceDTO
 * 创建人：suny
 * 类描述：折扣下点实体类
 * 创建日期：2023/11/30 14:43
 */
@Data
public class DiscountReduceDTO extends BaseDTO {
    /**
     * 主键
     */
    @NotNull(message = "折扣下点id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 组id
     */
    @NotNull(message = "计提比例组id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long commissionrategroupid;

    /**
     * 组名称
     */
    @NotBlank(message = "组名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String commissionrategroupname;

    /**
     * 开始折扣（9折）%
     */
    private BigDecimal ndiscountstart;

    /**
     * 结束折扣（8折）%
     */
    private BigDecimal ndiscountend;

    /**
     * 直接客户下点值%
     */
    private BigDecimal nrateofcustomer;

    /**
     * 代理公司下点值%
     */
    private BigDecimal nrateofagency;

    /**
     * 内容生产方下点值%
     */
    private BigDecimal nrateofagent;
}

