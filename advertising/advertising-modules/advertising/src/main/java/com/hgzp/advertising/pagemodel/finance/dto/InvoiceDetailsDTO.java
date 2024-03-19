package com.hgzp.advertising.pagemodel.finance.dto;

import com.hgzp.advertising.pagemodel.business.dto.BankAccountAllocateDTO;
import com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO;
import com.hgzp.core.model.Twcustomercharge;
import com.hgzp.core.page.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * InvoiceDetailsDTO
 * 创建人：yanz
 * 类描述：获取发票详情DTO（台账用） - getInvoiceDetailsByInvoice用：发票号相关的预开信息（预开类型）、预收款信息/预收款类型（客户收费表）、核销数据（已做核销）、银行流水分配明细（已有分配）
 * 创建日期：2024/1/6 16:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailsDTO extends BaseDTO {
    /**
     * 预开信息（预开类型）（预开发票申请表 preinvoiceapplication）
     */
    private List<PreInvoiceApplicationDTO> preInvoiceApplicationDTOList;
    /**
     * 预收款信息/预收款类型（客户收费表 twcustomercharge）
     */
    private List<CustomerChargeBankDTO> customerChargeBankDTOList;
    /**
     * 核销数据（已做核销）（分配明细表 orderapportiondetail）
     */
    private List<OrderApportiondetailDTO> orderApportiondetailDTOList;
    /**
     * 银行流水分配明细（已有分配）（客户收费表 twcustomercharge）
     */
    private List<CustomerChargeBankDTO> allocationDetails;
}

