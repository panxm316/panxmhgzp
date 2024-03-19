package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.advertising.pagemodel.ad.dto.AdResourceApplicationDTO;
import com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO;
import com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.page.BaseDTO;
import lombok.Data;

import java.util.List;

/**
 * OrderContractDetailDTO
 * 创建人：yanz
 * 类描述：查看订单合同详情DTO
 * 创建日期：2024/1/5 12:43
 */
@Data
public class OrderContractDetailDTO extends BaseDTO {
    /**
     * 订单基本信息
     */
    private Tworder order;
    /**
     * 广告明细
     */
    private List<Tworderitem> orderitemList;
    /**
     * 预开发票列表
     */
    private List<PreInvoiceApplicationDTO> preInvoiceApplicationDTOList;
    /**
     * 已分摊明细
     */
    private List<OrderApportiondetailDTO> orderApportiondetailDTOList;
    /**
     * 成本明细
     */
    private List<OrderItemCostDTO> orderItemCostDTOList;
    /**
     * 与此订单关联的客户的预收款信息
     */
    private List<CustomerChargeBankDTO> customerChargeBankDTOList;
    /**
     * 广告资源 - 要把所有的资源文件带出来，要查看文件详情的(AdResourceApplication 的 adorderid 列)
     */
    private List<AdResourceApplicationDTO> resourceApplicationDTOList;
}

