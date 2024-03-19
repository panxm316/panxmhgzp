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
 * PriceItemVO
 * 创建人：CGD
 * 类描述：价格详情vo
 * 创建日期：2023/11/10 14:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceItemDTO extends BaseDTO {

    /**
     * 主键
     */
    @NotNull(message = "价格详情id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 价格表id
     */
    @NotNull(message = "价格表组id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Long pricegroupid;

    /**
     * 价格表名称
     */
    @NotBlank(message = "价格表组名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String pricegroupname;

    /**
     * 媒体类型id
     */
    @NotBlank(message = "媒体类型id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String mediatypekey;

    /**
     * 媒体类型
     */
    @NotBlank(message = "媒体类型名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String mediatypename;

    /**
     * 媒体id
     */
    @NotNull(message = "媒体id不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Long mediaid;

    /**
     * 媒体名称
     */
    @NotBlank(message = "媒体名称不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private String medianame;

    /**
     * 叠次id
     */
    private Long foldid;

    /**
     * 叠次名称
     */
    private String foldname;

    /**
     * 叠次版本ID
     */
    private Long foldareaid;

    /**
     * 叠次版本名称
     */
    private String foldareaname;

    /**
     * 广告形式id
     */
    private Long addisplayformid;

    /**
     * 广告形式名称
     */
    private String addisplayformname;

    /**
     * 版面类别id
     */
    private Long pagesortid;

    /**
     * 版面类别名称
     */
    private String pagesortname;

    /**
     * 色彩id
     */
    private Long adcolorid;

    /**
     * 色彩名称
     */
    private String adcolorname;

    /**
     * 规格id
     */
    private Long adspecid;

    /**
     * 规格名称
     */
    private String adspecname;

    /**
     * 星期id
     */
    private Long weeksettingid;

    /**
     * 星期名称
     */
    private String weeksettingname;

    /**
     * 价格
     */
    @NotNull(message = "价格不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private BigDecimal baseprice;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空", groups = {ValidateParam.edit.class, ValidateParam.add.class})
    private Date dstartdate;

    /**
     * 结束日期
     */
    private Date denddate;

    /**
     * 序号
     */
    private Integer isort;

    /**
     * 是否有效
     */
    private Boolean buse;

    /**
     * 备注
     */
    private String sremark;
}
