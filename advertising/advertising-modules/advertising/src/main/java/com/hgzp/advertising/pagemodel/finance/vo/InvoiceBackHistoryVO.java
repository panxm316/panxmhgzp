package com.hgzp.advertising.pagemodel.finance.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.hgzp.core.model.Twinvoicebackhistory;
import lombok.*;
import lombok.experimental.Accessors;
import com.hgzp.core.annotation.LogData;
import static com.hgzp.core.constant.ValidateParam.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * 发票(收款)回退历史表 VO 前端页面视图对象
 * </p>
 *
 * @author muyn
 * @since 2023-12-26
 */
@Data
public class InvoiceBackHistoryVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
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
     * 分摊明细表id
     */
    @LogData(alias = "分摊明细表id")
    private Long orderapportiondetailid;
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
     * 原分摊金额(退回金额)
     */
    @LogData(alias = "原分摊金额(退回金额)")
    private BigDecimal namountapportion;
    /**
     * 订单id
     */
    @LogData(alias = "订单id")
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
     * 分摊种类(0-预收款 1-直接收款 2-银行流水)
     */
    @LogData(alias = "分摊种类(0-预收款 1-直接收款 2-银行流水)")
    private Integer sapportiontype;
    /**
     * 说明
     */
    @LogData(alias = "说明")
    private String sdescription;

    public static InvoiceBackHistoryVO parseToVO(Twinvoicebackhistory entity) {
        InvoiceBackHistoryVO invoicebackhistoryVO = new InvoiceBackHistoryVO();
        BeanUtils.copyProperties(entity, invoicebackhistoryVO);
        return invoicebackhistoryVO;
    }

    public static Twinvoicebackhistory parseToEntity(InvoiceBackHistoryVO vo) {
        Twinvoicebackhistory entity = new Twinvoicebackhistory();
        BeanUtils.copyProperties(vo, entity);
        return entity;
    }
}
