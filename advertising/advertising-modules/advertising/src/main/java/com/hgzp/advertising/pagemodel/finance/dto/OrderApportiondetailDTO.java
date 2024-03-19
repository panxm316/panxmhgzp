package com.hgzp.advertising.pagemodel.finance.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.model.Twinvoice;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.model.Tworderapportiondetail;
import com.hgzp.core.model.Tworderitem;
import lombok.*;
import com.hgzp.core.page.BaseDTO;

import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 广告分摊明细表 DTO 数据传输对象
 * </p>
 *
 * @author suny
 * @since 2023-12-18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderApportiondetailDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
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
     * 收费表id
     */
    private Long customerchargeid;

    /**
     * 发票表id
     */
    private Long invoiceid;
    /**
     * 发票号
     */
    private String invoice;
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
     * 分摊金额
     */
    private BigDecimal namountapportion;
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
     * 分摊日期
     */
    private Date dapportiondate;
    /**
     * 分摊各类(0-预收款 1-直接收款 2-银行流水)
     */
    private Integer sapportiontype;
    /**
     * 并发标记
     */
    private Long version;
    /**
     * 经营主体名称（关联发票表找到）
     */
    private String businessentityname;
    /**
     * 客户名称(关联订单表)
     */
    private String customername;

    /**
     * 代理公司名称(关联订单表)
     */
    private String agencyname;

    /**
     * 内容生产方(关联订单表)
     */
    private String agentname;
    /**
     * 业务员id(关联订单表)
     */
    private Long employid;

    /**
     * 业务员名称(关联订单表)
     */
    private String employname;
    /**
     * 媒体名称(关联订单明细表)
     */
    private String medianame;
    /**
     * 广告标题(关联订单明细表)
     */
    private String sadtitle;
    /**
     * 预见报结束日期(关联订单明细表)--刊发日期
     */
    private Date prestartdate;
    /**
     * 收费日期
     */
    private Date dpaydate;


    public static OrderApportiondetailDTO parseToDTO(Tworderapportiondetail entity) {
        OrderApportiondetailDTO orderapportiondetailDTO = new OrderApportiondetailDTO();
        BeanUtils.copyProperties(entity, orderapportiondetailDTO);
        return orderapportiondetailDTO;
    }

    public static OrderApportiondetailDTO parseToDTO(Tworderapportiondetail entity, Twinvoice twinvoice, Tworderitem tworderitem, Tworder tworder) {
        if (entity == null) {
            return null;
        }
        OrderApportiondetailDTO dto = new OrderApportiondetailDTO();
        BeanUtils.copyProperties(entity, dto);
        if (twinvoice != null) {
            dto.setBusinessentityname(twinvoice.getBusinessentityname());
            dto.setEmployid(twinvoice.getEmployid());
            dto.setEmployname(twinvoice.getEmployname());
        }
        if (tworder != null) {
            dto.setCustomername(tworder.getCustomername());
            dto.setAgencyname(tworder.getAgencyname());
            dto.setAgentname(tworder.getAgentname());
        }
        if (tworderitem != null) {
            dto.setMedianame(tworderitem.getMedianame());
            dto.setSadtitle(tworderitem.getSadtitle());
            dto.setPrestartdate(tworderitem.getPrestartdate());
        }
        return dto;
    }

    public static Tworderapportiondetail parseToEntity(OrderApportiondetailDTO dto) {
        Tworderapportiondetail entity = new Tworderapportiondetail();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
