package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twdebtreason;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DebtReasonDTO
 * 创建人：suny
 * 类描述：欠款原因表 实体对象
 * 创建日期：2023/11/17 10:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtReasonDTO extends BaseDTO {
    /**
     * 主键
     */
    @NotNull(message = "欠款原因表id不能为空", groups = {ValidateParam.edit.class, ValidateParam.detail.class})
    private Long id;

    /**
     * 部门id
     */
    private Long deptid;

    /**
     * 部门名称
     */
    private String deptname;

    /**
     * 主业务员id
     */
    private Long employid;

    /**
     * 主业务员名称
     */
    private String employname;

    /**
     * 代理公司业务员id
     */
    private Long agencyemployid;

    /**
     * 代理公司业务员名称
     */
    private String agencyemployname;

    /**
     * 内容生产方业务员id
     */
    private Long agentemployid;

    /**
     * 内空生产方业务员名称
     */
    private String agentemployname;

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
     * 客户名称
     */
    private String customername;

    /**
     * 代理公司
     */
    private String agencyname;

    /**
     * 内容生产方名称
     */
    private String agentname;

    /**
     * 广告表id
     */
    private Long orderid;

    /**
     * 刊期表id
     */
    private Long orderitemid;

    /**
     * 合同号
     */
    private String scontractnum;

    /**
     * 刊发日期
     */
    private Date dpublishdate;

    /**
     * 广告标题
     */
    private String sadtitle;

    /**
     * 应收金额
     */
    private BigDecimal amountreceivable;

    /**
     * 已收金额
     */
    private BigDecimal amountreceived;

    /**
     * 欠款金额
     */
    private BigDecimal amountarrearage;

    /**
     * 发票金额
     */
    private BigDecimal namountinvoice;

    /**
     * 发票号
     */
    private String invoice;

    /**
     * 催告情况
     */
    private String snoticecontent;

    /**
     * 欠款原因
     */
    private String sdebtreason;

    /**
     * 归类原因
     */
    private String scategory;

    /**
     * 计划回款时间
     */
    private String srepaymentdate;

    /**
     * 风险等级
     */
    private String srisklevel;

    /**
     * 法务介入 默认0 否 1是
     */
    private Boolean blegal;

    /**
     * 填报日期
     */
    private Date dreportdate;

    /**
     * 是否确认 默认0 否 1是
     */
    private Boolean bconfirm;

    /**
     * 并发标志
     */
    private Long version;

    public DebtReasonDTO(Twdebtreason twdebtreason) {
        BeanUtils.copyProperties(twdebtreason, this);
    }

}
