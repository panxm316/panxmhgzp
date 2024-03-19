package com.hgzp.advertising.pagemodel.finance.dto;

import com.hgzp.core.model.Twinvoicebackhistory;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 发票(收款)回退历史表 DTO 数据传输对象
 * </p>
 *
 * @author muyn
 * @since 2023-12-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceBackHistoryDTO extends BaseDTO {

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
     * 分摊明细表id
     */
    private Long orderapportiondetailid;
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
     * 原分摊金额(退回金额)
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
     * 分摊种类(0-预收款 1-直接收款 2-银行流水)
     */
    private Integer sapportiontype;
    /**
     * 说明
     */
    private String sdescription;

    /**
     * 预见报开始日期(刊期开始日期，关联orderitem表)
     */
    private Date prestartdate;
    /**
     * 广告标题（关联orderitem表）
     */
    private String sadtitle;
    /**
     * 媒体名称（关联orderitem表）
     */
    private String medianame;

    public static InvoiceBackHistoryDTO parseToDTO(Twinvoicebackhistory entity, Tworderitem orderitem) {
        InvoiceBackHistoryDTO TwinvoicebackhistoryDTO = new InvoiceBackHistoryDTO();
        BeanUtils.copyProperties(entity, TwinvoicebackhistoryDTO);
        TwinvoicebackhistoryDTO.setPrestartdate(orderitem.getPrestartdate());
        TwinvoicebackhistoryDTO.setSadtitle(orderitem.getSadtitle());
        TwinvoicebackhistoryDTO.setMedianame(orderitem.getMedianame());
        return TwinvoicebackhistoryDTO;
    }

    public static Twinvoicebackhistory parseToEntity(InvoiceBackHistoryDTO dto) {
        Twinvoicebackhistory entity = new Twinvoicebackhistory();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
