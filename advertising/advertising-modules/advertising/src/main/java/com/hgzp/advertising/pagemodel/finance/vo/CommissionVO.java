package com.hgzp.advertising.pagemodel.finance.vo;

import com.hgzp.core.model.Twcommission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 佣金计提明细表 VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2024-01-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionVO {


    /**
     * 主键
     */
    private Long id;
    /**
     * 订单id
     */
    private Long orderid;
    /**
     * 刊期id
     */
    private Long orderitemid;
    /**
     * 刊期编码
     */
    private Long orderitemcode;
    /**
     * 合同号
     */
    private String scontractnum;
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
    private Date dcreatetime;
    /**
     * 业务员部门id
     */
    private Long deptid;
    /**
     * 部门名称
     */
    private String deptname;
    /**
     * 业务员id
     */
    private Long employid;
    /**
     * 业务员名称
     */
    private String employname;
    /**
     * 类型（0-直客业务员 1-代理业务员 2-内容生产方业务员）
     */
    private Integer employtype;
    /**
     * 是否主业务员
     */
    private Boolean bprimary;
    /**
     * 客户id(与业务员相关的)
     */
    private Long customerid;
    /**
     * 客户名称
     */
    private String customername;
    /**
     * 业绩金额
     */
    private BigDecimal amountachievement;
    /**
     * 业绩比例
     */
    private BigDecimal archievementrate;
    /**
     * 成本金额
     */
    private BigDecimal namountcost;
    /**
     * 风险金比例%
     */
    private BigDecimal nrateofrisk;
    /**
     * 佣金参数组id
     */
    private Long commissionrategroupid;
    /**
     * 佣金参数明细id
     */
    private Long commissionrateitemid;
    /**
     * 计提比例
     */
    private BigDecimal ncommissionrate;
    /**
     * 计提金额
     */
    private BigDecimal ncommission;
    /**
     * 备注
     */
    private String sremark;
    /**
     * 标记(0-计算1-确认 2-发放 3-撤销)
     */
    private Integer icommissionstatus;
    /**
     * 是否冲抵0否 1是
     */
    private Boolean bcancel;
    /**
     * 如果冲抵，则冲抵记录的id
     */
    private Long relatedid;
    /**
     * 确认日期
     */
    private Date dconfirmdate;
    /**
     * 确认者
     */
    private Long confirmempid;
    /**
     *
     */
    private String confirmempname;
    /**
     * 确认说明
     */
    private String sconfirmremark;
    /**
     * 发放日期
     */
    private Date dpaydate;
    /**
     * 发放者
     */
    private Long payempid;
    /**
     *
     */
    private String payempname;
    /**
     * 发放说明
     */
    private String spayremark;
    /**
     * 并发标记
     */
    private Long version;

    public static CommissionVO parseToVO(Twcommission entity) {
        CommissionVO commissionVO = new CommissionVO();
        BeanUtils.copyProperties(entity, commissionVO);
        return commissionVO;
    }

    public static Twcommission parseToEntity(CommissionVO vo) {
        Twcommission entity = new Twcommission();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
