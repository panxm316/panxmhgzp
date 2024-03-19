package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.model.Tworderitembelong;
import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * orderAndItemDTO
 * 创建人：suny
 * 类描述：TODO 成本核对相关订单明细DTO
 * 创建日期：2023/12/13 14:52
 */
@Data
public class OrderAndItemDTO extends BaseDTO {
    // <editor-fold desc="order字段">
    /**
     * 订单id
     */
    private Long orderid;
    /**
     * 广告项目id
     */
    private Long adprojectid;
    /**
     * 广告项目名称
     */
    private String adprojectname;
    /**
     * 广告项目编码
     */
    private String projectcode;
    /**
     * 合同号
     */
    private String scontractnum;
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
     * 客户名称
     */
    private String customername;
    /**
     * 客户id
     */
    private Long customerid;

    /**
     * 代理公司名称
     */
    private String agencyname;

    /**
     * 内容生产方
     */
    private String agentname;
    /**
     * 行业名称
     */
    private String adindustryname;
    // </editor-fold>
    // <editor-fold desc="orderitem字段">
    /**
     * 订单明细id
     */
    private Long orderitemid;
    /**
     * 媒体类型名称
     */
    private String mediatypename;
    /**
     * 媒体名称
     */
    private String medianame;
    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 预见报开始日期（刊发日期）
     */
    private Date prestartdate;
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
     * 成本金额
     */
    private BigDecimal namountcost;
    /**
     * 业绩金额
     */
    private BigDecimal amountachievement;
    /**
     * 分摊金额
     */
    private BigDecimal namountapportion;
    /**
     * 业绩时间(用于统计)
     */
    private Date dachievementdate;
    /**
     * 叠次名称
     */
    private String foldname;

    /**
     * 叠次版本名称
     */
    private String foldareavername;
    /**
     * 广告形式名称
     */
    private String addisplayformname;
    /**
     * 版面类别名称
     */
    private String pagesortname;
    /**
     * 色彩名称
     */
    private String adcolorname;
    /**
     * 规格名称
     */
    private String adspecname;
    /**
     * 宽
     */
    private BigDecimal nwidth;

    /**
     * 高
     */
    private BigDecimal nheight;

    /**
     * 星期名称
     */
    private String weeksettingname;
    /**
     * 广告标题
     */
    private String sadtitle;
    /**
     * 版面名称
     */
    private String foldpageplanname;
    // </editor-fold>

    /**
     * 成本核对明细
     */
    private List<OrderItemCostDTO> orderItemCostDTOList;

    /**
     * 经营主体名称（关联发票表找到）
     */
    private String businessentityname;
    /**
     * 关联发票号(英文逗号分隔)
     */
    private String invoices;
    /**
     * 刊期编号(自增列,广告号)
     */
    private Long iitemcode;
    /**
     * 发布状态
     */
    private Integer ipublishstatus;
    /**
     * 是否可编辑
     */
    private Boolean bEditFlag;
    /**
     * 刊期归属数据
     */
    private List<Tworderitembelong> orderitembelongList;
    /**
     * 订单加刊审批状态
     */
    private Integer iorderaddapprovestatus;

    /**
     * 订单改刊审批状态
     */
    private Integer iorderchangeapprovestatus;

    /**
     * 订单停刊审批状态
     */
    private Integer iorderstopapprovestatus;
    /**
     * 加刊审批状态
     */
    private Integer iaddapprovestatus;

    /**
     * 改刊审批状态
     */
    private Integer ichangeapprovestatus;

    /**
     * 停刊审批状态
     */
    private Integer istopapprovestatus;

    /**
     * 最后操作员名
     */
    private String lastoperator;

    /**
     * 付款日期
     */
    private Date dpaydate;

    /**
     * 分摊日期
     */
    private Date dapportiondate;

    /**
     * Tworder和Twoderitem组成DTO（用于：查询所有有欠款的订单的广告明细）
     * 方法功能:注：不含“成本核对明细”orderItemCostDTOList 和 “关联发票列表”invoicelist
     *
     * @param tworder
     * @param tworderitem
     * @return com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO
     * @author yanz
     * @date 2023/12/27 9:39
     */
    public static OrderAndItemDTO from(Tworder tworder, Tworderitem tworderitem) {
        if (tworder == null) {
            return null;
        }
        OrderAndItemDTO orderAndItemDTO = new OrderAndItemDTO();
        orderAndItemDTO.setOrderid(tworder.getId());
        orderAndItemDTO.setAdprojectname(tworder.getAdprojectname());
        orderAndItemDTO.setScontractnum(tworder.getScontractnum());
        orderAndItemDTO.setEmployid(tworder.getEmployid());
        orderAndItemDTO.setEmployname(tworder.getEmployname());
        orderAndItemDTO.setCustomername(tworder.getCustomername());
        orderAndItemDTO.setCustomerid(tworder.getCustomerid());
        orderAndItemDTO.setMediaid(tworder.getCustomerid());
        orderAndItemDTO.setAgencyname(tworder.getAgencyname());
        orderAndItemDTO.setAgentname(tworder.getAgentname());
        orderAndItemDTO.setAdindustryname(tworder.getAdindustryname());
//        orderAndItemDTO.setSadtitle(tworder.getSadtitle());
        orderAndItemDTO.setBusinessentityname(tworder.getBusinessentityname());

        if (tworderitem == null) {
            return null;
        }
        orderAndItemDTO.setOrderitemid(tworderitem.getId());
        orderAndItemDTO.setMediatypename(tworderitem.getMediatypename());
        orderAndItemDTO.setMedianame(tworderitem.getMedianame());
        orderAndItemDTO.setPrestartdate(tworderitem.getPrestartdate());
        orderAndItemDTO.setAmountreceivable(tworderitem.getAmountreceivable());
        orderAndItemDTO.setAmountreceived(tworderitem.getAmountreceived());
        orderAndItemDTO.setAmountarrearage(tworderitem.getAmountarrearage());
        orderAndItemDTO.setNamountcost(tworderitem.getNamountcost());
        orderAndItemDTO.setSadtitle(tworderitem.getSadtitle());
        orderAndItemDTO.setAmountachievement(tworderitem.getAmountachievement());
        orderAndItemDTO.setDachievementdate(tworderitem.getDachievementdate());
        orderAndItemDTO.setFoldname(tworderitem.getFoldname());
        orderAndItemDTO.setFoldareavername(tworderitem.getFoldareavername());
        orderAndItemDTO.setAddisplayformname(tworderitem.getAddisplayformname());
        orderAndItemDTO.setPagesortname(tworderitem.getPagesortname());
        orderAndItemDTO.setAdcolorname(tworderitem.getAdcolorname());
        orderAndItemDTO.setAdspecname(tworderitem.getAdspecname());
        orderAndItemDTO.setNwidth(tworderitem.getNwidth());
        orderAndItemDTO.setNheight(tworderitem.getNheight());
        orderAndItemDTO.setWeeksettingname(tworderitem.getWeeksettingname());
        orderAndItemDTO.setFoldpageplanname(tworderitem.getFoldpageplanname());
        orderAndItemDTO.setIitemcode(tworderitem.getIitemcode());
        orderAndItemDTO.setIpublishstatus(tworderitem.getIpublishstatus());
        orderAndItemDTO.setIaddapprovestatus(tworderitem.getIaddapprovestatus());
        orderAndItemDTO.setIchangeapprovestatus(tworderitem.getIchangeapprovestatus());
        orderAndItemDTO.setIstopapprovestatus(tworderitem.getIstopapprovestatus());

        return orderAndItemDTO;
    }

    /**
     * 方法功能:"查询订单明细相关综合对象列表(佣金计提)"拼dto
     * order表数据为主，orderitem数据补充，“应收金额、已收金额、欠款金额、业绩金额”取相关的orderitem数据中计算出的值（单传一个参数对象）
     * 注：可编辑状态 bEditFlag 需手动赋值
     *
     * @param tworder
     * @param tworderitem 基本tworderitem对象属性
     * @param invoices    关联发票取发票号，英文逗号分隔成string
     * @return com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO
     * @author yanz
     * @date 2024/1/5 9:56
     */
    public static OrderAndItemDTO forCommissionDTO(Tworder tworder, Tworderitem tworderitem, String invoices) {
        OrderAndItemDTO orderAndItemDTO = from(tworder, tworderitem);
        if (orderAndItemDTO == null) {
            return null;
        }
        // 应收金额、已收金额、欠款金额、业绩金额、关联发票（前台显示多个发票号拼接）
        orderAndItemDTO.setOrderid(tworder.getId());
        orderAndItemDTO.setInvoices(invoices);
        orderAndItemDTO.setIpublishstatus(tworderitem.getIpublishstatus());
        return orderAndItemDTO;
    }

    public static OrderAndItemDTO forGetOrdersInPeriod(Tworder tworder, Tworderitem tworderitem, String invoices) {
        OrderAndItemDTO orderAndItemDTO = from(tworder, tworderitem);
        if (orderAndItemDTO == null) {
            return null;
        }
        // 应收金额、已收金额、欠款金额、业绩金额、关联发票（前台显示多个发票号拼接）
        orderAndItemDTO.setOrderid(tworder.getId());
        orderAndItemDTO.setInvoices(invoices);
        orderAndItemDTO.setIpublishstatus(tworderitem.getIpublishstatus());
        return orderAndItemDTO;
    }

    /**
     * 生成forGetOrdersInPeriod的参数2赋值对象
     * 方法功能:计算传入的订单明细，汇总“应收金额、已收金额、欠款金额、业绩金额、成本金额”成一个对象
     *
     * @param items
     * @return com.hgzp.core.model.Tworderitem
     * @author yanz
     * @date 2024/1/10 17:11
     */
    public static Tworderitem getOrderitemForDtoConversion(List<Tworderitem> items) {
        Tworderitem itemForDto = new Tworderitem();

        BigDecimal totalAmountReceivable = items.stream()
                .map(Tworderitem::getAmountreceivable)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmountReceived = items.stream()
                .map(Tworderitem::getAmountreceived)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmountArrearage = items.stream()
                .map(Tworderitem::getAmountarrearage)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmountAchievement = items.stream()
                .map(Tworderitem::getAmountachievement)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAmountCost = items.stream()
                .map(Tworderitem::getNamountcost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        itemForDto.setAmountreceivable(Optional.ofNullable(itemForDto.getAmountreceivable()).orElse(BigDecimal.ZERO).add(totalAmountReceivable));
        itemForDto.setAmountreceived(Optional.ofNullable(itemForDto.getAmountreceived()).orElse(BigDecimal.ZERO).add(totalAmountReceived));
        itemForDto.setAmountarrearage(Optional.ofNullable(itemForDto.getAmountarrearage()).orElse(BigDecimal.ZERO).add(totalAmountArrearage));
        itemForDto.setAmountachievement(Optional.ofNullable(itemForDto.getAmountachievement()).orElse(BigDecimal.ZERO).add(totalAmountAchievement));
        itemForDto.setNamountcost(Optional.ofNullable(itemForDto.getNamountcost()).orElse(BigDecimal.ZERO).add(totalAmountCost));
        return itemForDto;
    }

}

