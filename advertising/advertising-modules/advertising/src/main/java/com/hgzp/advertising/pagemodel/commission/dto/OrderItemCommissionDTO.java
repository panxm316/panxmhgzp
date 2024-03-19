package com.hgzp.advertising.pagemodel.commission.dto;

import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Tbcommissionrategroup;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * OrderItemCommissionDTO
 * 创建人：suny
 * 类描述：佣金计提对象
 * 创建日期：2024/1/12 10:54
 */
@Data
public class OrderItemCommissionDTO extends OrderAndItemDTO {
    /**
     * 风险金比例% - tbcommissionrategroup计提总表默认值
     */
    private BigDecimal nrateofrisk;
    /**
     * 风险金 - 计算得出 风险金=业绩金额*风险金比例
     */
    private BigDecimal riskfund;
    /**
     * 佣金参数组id - 默认的tbcommissionrategroup计提总表id
     */
    private Long commissionrategroupid;

    /**
     * 佣金参数明细id - 查询到的要使用的佣金参数明细表id
     */
    private Long commissionrateitemid;

    /**
     * 计提比例 - 查询到的tbcommissionrategroup或者tbcommissionrateitem中要用到的那条记录的计提比例
     */
    private BigDecimal ncommissionrate;

    /**
     * 计提金额 - 计算得出【提成金额=业绩金额（tworderitem）*业绩比例（tworderitembelong）*（提成比例-折扣下点）】
     */
    private BigDecimal ncommission;
    /**
     * 折扣降点 - 默认的tbcommissionrategroup计提总表对应的tbdiscountreduce折扣降点表中根据计提比例找到对应范围数据的折扣降点
     */
    private BigDecimal discountreduce;
    /**
     * 佣金计算标记（twcommission表中orderitemid与employid最新的一条，没有记录设置为false，有记录且bcancel=0且icommissionstatus=0【计算】的设置为true）
     * - 暂时不用
     */
    private Boolean bcountflag;
    /**
     * 佣金计提表主键（如果bcountflag=false，则为null，如果为true，则为Twcommission表的id）
     * 如果为true，判断 “twcommission表中orderitemid与employid最新的一条” 是否存在：不存在，则为null；存在，并满足 “bcancel=0且icommissionstatus=0【计算】”，则为twcommission表的id
     */
    private Long twcommissionid;
    /**
     * 标记(0-计算1-确认 2-发放 3-撤销)
     */
    private Integer icommissionstatus;
    /**
     * 确认说明
     */
    private String sconfirmremark;
    /**
     * 确认日期
     */
    private Date dconfirmdate;
    /**
     * 发放说明
     */
    private String spayremark;
    /**
     * 发放日期
     */
    private Date dpaydate;

    /**
     * 并发标记
     */
    private Long version;
    /**
     * 业绩比例（取自tworderitembelong）
     */
    private BigDecimal archievementrate;
    /**
     * 部门id
     */
    private Long deptid ;
    /**
     * 部门
     */
    private String deptname;
    /**
     * 业务员类型
     */
    private Integer employtype;

    /**
     * 以 OrderAndItemDTO、Tbcommissionrategroup 组装 OrderItemCommissionDTO
     * 方法功能:
     *
     * @param orderAndItemDTO
     * @param rategroup          默认的tbcommissionrategroup计提总表对应的tbdiscountreduce折扣降点表中根据计提比例找到对应范围数据的折扣降点
     * @param paramCommissionDTO 包含下列参数的对象:commissionrateitemid、riskfund、ncommissionrate、ncommission、discountreduce、bcountflag、twcommissionid
     * @return com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO
     * @author yanz
     * @date 2024/1/15 9:37
     */
    public static OrderItemCommissionDTO from(OrderAndItemDTO orderAndItemDTO, Tbcommissionrategroup rategroup, OrderItemCommissionDTO paramCommissionDTO) {
        if (orderAndItemDTO == null) {
            return null;
        }
        OrderItemCommissionDTO orderItemCommissionDTO = new OrderItemCommissionDTO();
        // Not mapped OrderAndItemDTO fields:
        // orderid
        // adprojectname
        // scontractnum
        // customername
        // agencyname
        // agentname
        // adindustryname
        // orderitemid
        // mediatypename
        // medianame
        // prestartdate
        // amountreceivable
        // amountreceived
        // amountarrearage
        // namountcost
        // amountachievement
        // dachievementdate
        // foldname
        // foldareavername
        // addisplayformname
        // pagesortname
        // adcolorname
        // adspecname
        // nwidth
        // nheight
        // weeksettingname
        // sadtitle
        // foldpageplanname
        // orderItemCostDTOList
        // businessentityname
        // invoices
        // iitemcode
        // ipublishstatus
        // bEditFlag
        // orderitembelongList
        BeanUtils.copyProperties(orderAndItemDTO, orderItemCommissionDTO);


        // Not mapped OrderItemCommissionDTO fields:
        // nrateofrisk
        // riskfund
        // commissionrategroupid
        // commissionrateitemid
        // ncommissionrate
        // ncommission
        // discountreduce
        // bcountflag
        // twcommissionid
        if (rategroup != null) {
            orderItemCommissionDTO.setNrateofrisk(rategroup.getNrateofrisk());
            orderItemCommissionDTO.setCommissionrategroupid(rategroup.getId());
        }
        // employid
        // employname
        // employtype
        if (paramCommissionDTO != null) {
            orderItemCommissionDTO.setEmployid(paramCommissionDTO.getEmployid());
            orderItemCommissionDTO.setEmployname(paramCommissionDTO.getEmployname());
            orderItemCommissionDTO.setEmploytype(paramCommissionDTO.getEmploytype());
            orderItemCommissionDTO.setTwcommissionid(paramCommissionDTO.getTwcommissionid());
            orderItemCommissionDTO.setRiskfund(paramCommissionDTO.getRiskfund());
            orderItemCommissionDTO.setCommissionrateitemid(paramCommissionDTO.getCommissionrateitemid());
            orderItemCommissionDTO.setNcommissionrate(paramCommissionDTO.getNcommissionrate());
            orderItemCommissionDTO.setNcommission(paramCommissionDTO.getNcommission());
            orderItemCommissionDTO.setDiscountreduce(paramCommissionDTO.getDiscountreduce());
            orderItemCommissionDTO.setBcountflag(paramCommissionDTO.getBcountflag());
            orderItemCommissionDTO.setTwcommissionid(paramCommissionDTO.getTwcommissionid());
            orderItemCommissionDTO.setIcommissionstatus(paramCommissionDTO.getIcommissionstatus());
            orderItemCommissionDTO.setArchievementrate(paramCommissionDTO.getArchievementrate());
            orderItemCommissionDTO.setVersion(paramCommissionDTO.getVersion());
            orderItemCommissionDTO.setDeptid(paramCommissionDTO.getDeptid());
            orderItemCommissionDTO.setDeptname(paramCommissionDTO.getDeptname());
        }
        return orderItemCommissionDTO;
    }

    public void resetParamdto() {
        this.setNrateofrisk(null);
        this.setRiskfund(null);
        this.setCommissionrategroupid(null);
        this.setCommissionrateitemid(null);
        this.setNcommissionrate(null);
        this.setNcommission(null);
        this.setDiscountreduce(null);
        this.setBcountflag(null);
        this.setTwcommissionid(null);
        this.setIcommissionstatus(null);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderItemCommissionDTO that = (OrderItemCommissionDTO) o;
        return Objects.equals(nrateofrisk, that.nrateofrisk) && Objects.equals(riskfund, that.riskfund) && Objects.equals(commissionrategroupid, that.commissionrategroupid) && Objects.equals(commissionrateitemid, that.commissionrateitemid) && Objects.equals(ncommissionrate, that.ncommissionrate) && Objects.equals(ncommission, that.ncommission) && Objects.equals(discountreduce, that.discountreduce) && Objects.equals(bcountflag, that.bcountflag) && Objects.equals(twcommissionid, that.twcommissionid) && Objects.equals(icommissionstatus, that.icommissionstatus) && Objects.equals(sconfirmremark, that.sconfirmremark) && Objects.equals(spayremark, that.spayremark) && Objects.equals(version, that.version) && Objects.equals(archievementrate, that.archievementrate) && Objects.equals(deptid, that.deptid) && Objects.equals(deptname, that.deptname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nrateofrisk, riskfund, commissionrategroupid, commissionrateitemid, ncommissionrate, ncommission, discountreduce, bcountflag, twcommissionid, icommissionstatus, sconfirmremark, spayremark, version, archievementrate, deptid, deptname);
    }
}

