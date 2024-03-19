package com.hgzp.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.hgzp.core.annotation.LogData;
import com.hgzp.utils.model.LoginUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 广告分摊明细表
 * </p>
 *
 * @author muyn
 * @since 2023-12-18
 */
@Getter
@Setter
@NoArgsConstructor
@LogData(alias = "广告分摊明细表")
public class Tworderapportiondetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @LogData(alias = "主键")
    private Long id;

    /**
     * 创建者id
     */
    @LogData(alias = "创建者id")
    private Long createempid;

    /**
     * 创建者名称
     */
    @LogData(alias = "创建者名称")
    private String createempname;

    /**
     * 创建日期
     */
    @LogData(alias = "创建日期")
    private Date dcreatetime;

    /**
     * 收费表id
     */
    @LogData(alias = "收费表id")
    private Long customerchargeid;

    /**
     * 发票表id
     */
    @LogData(alias = "发票表id")
    private Long invoiceid;

    /**
     * 发票号
     */
    @LogData(alias = "发票号")
    private String invoice;

    /**
     * 应收金额
     */
    @LogData(alias = "应收金额")
    private BigDecimal amountreceivable;

    /**
     * 已收金额
     */
    @LogData(alias = "已收金额")
    private BigDecimal amountreceived;

    /**
     * 欠款金额
     */
    @LogData(alias = "欠款金额")
    private BigDecimal amountarrearage;

    /**
     * 分摊金额
     */
    @LogData(alias = "分摊金额")
    private BigDecimal namountapportion;

    /**
     * 订单id
     */
    @LogData(alias = "订单号", mappedBy = "tworder", mappedByColumn = "sordernum")
    private Long orderid;

    /**
     * 刊期id
     */
    @LogData(alias = "刊期id")
    private Long orderitemid;

    /**
     * 刊期编码
     */
    @LogData(alias = "刊期编码")
    private Long orderitemcode;

    /**
     * 合同号
     */
    @LogData(alias = "合同号")
    private String scontractnum;

    /**
     * 分摊日期
     */
    @LogData(alias = "分摊日期")
    private Date dapportiondate;

    /**
     * 分摊各类(0-预收款 1-直接收款 2-银行流水)
     */
    @LogData(alias = "分摊各类(0-预收款 1-直接收款 2-银行流水)")
    private Integer sapportiontype;

    /**
     * 是否回退(退款标记)
     */
    @LogData(alias = "是否回退(退款标记)")
    private Boolean brollback;

    /**
     * 并发标记
     */
    @Version
    private Long version;

    /**
     * 方法功能:保存分摊（saveOrderApportiondetail）中使用，对当前指定的广告详情，添加广告分摊明细记录
     * 其中应收、已收、欠款填分摊前的数据
     *
     * @param customerChargeId
     * @param orderItem
     * @param loginUser
     * @param twcustomercharge
     * @param namountapportion
     * @param date
     * @return
     * @author yanz
     * @date 2024/1/4 10:13
     */
    public Tworderapportiondetail(Long customerChargeId, Tworderitem orderItem, LoginUser loginUser, Twcustomercharge twcustomercharge, BigDecimal namountapportion, Date date) {
        this.setCreateempid(loginUser.getUserid());
        this.setCreateempname(loginUser.getUsername());
        this.setDcreatetime(new Date());
        this.setCustomerchargeid(customerChargeId);
        this.setInvoiceid(twcustomercharge.getInvoiceid());
        this.setInvoice(twcustomercharge.getInvoice());
        this.setNamountapportion(namountapportion);
        // 这里应收、已收、欠款填分摊前的数据
        this.setAmountreceivable(orderItem.getAmountreceivable());
        this.setAmountreceived(orderItem.getAmountreceived());
        this.setAmountarrearage(orderItem.getAmountarrearage());
        this.setOrderid(orderItem.getOrderid());
        this.setOrderitemid(orderItem.getId());
        this.setOrderitemcode(orderItem.getIitemcode());
        this.setScontractnum(orderItem.getScontractnum());
        this.setDapportiondate(date);
        this.setSapportiontype(twcustomercharge.getItype());
        this.setVersion(0L);
    }
}
