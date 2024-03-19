package com.hgzp.advertising.service.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceDetailsDTO;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceFilesDTO;
import com.hgzp.advertising.pagemodel.finance.vo.InvoiceVO;
import com.hgzp.core.model.Twinvoice;
import com.hgzp.core.model.Twpreinvoiceapplication;

import java.util.List;

/**
 * <p>
 * 发票表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
public interface InvoiceServiceI extends IService<Twinvoice> {

    /**
     * 保存预开申请表信息至发票表（临时票号）
     * 方法功能:据预开发票申请id，保存id对应的预开信息至发票表（临时票号），返回保存的发票ID
     *
     * @param preinvoiceapplicationid
     * @return java.lang.Long 发票ID
     * @author yanz
     * @date 2023/12/19 14:41
     */
    Long saveInvoiceByPreInvoApplyId(Long preinvoiceapplicationid) throws Exception;

    /**
     * 获取单个发票信息
     * 方法功能:据发票id，取单个发票信息
     *
     * @param id
     * @return com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO
     * @author yanz
     * @date 2023/12/19 13:33
     */
    InvoiceDTO getInvoiceById(Long id);

    /**
     * 保存三方获取的发票号、发票编码
     * 方法功能:据传入的发票id（invoiceId），保存三方获取的发票号（invoice）、发票编码（invoiceCode）至发票表、预开发票申请表
     *
     * @param invoiceId
     * @param invoice     发票号
     * @param invoiceCode 发票编码
     * @author yanz
     * @date 2023/12/21 9:06
     */
    void saveInvoiceCode(Long invoiceId, String invoice, String invoiceCode) throws Exception;

    /**
     * 查询发票列表
     * 方法功能:查询发票列表，参数：时间段、客户id、关键字（发票号、客户名称、业务员、开票项目）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO>
     * @author yanz
     * @date 2023/12/28 10:33
     */
    IPage<InvoiceDTO> getInvoicePageList(Page<Twinvoice> page, InvoiceVO query);

    /**
     * 更新发票数据（发票打印）
     * 方法功能:以InvoiceDTO内容，更新发票数据（发票打印）
     *
     * @param invoiceDTO
     * @return void
     * @author yanz
     * @date 2024/1/2 16:53
     */
    void updateInvoiceByInvoiceDTO(InvoiceDTO invoiceDTO) throws Exception;

    /**
     * 方法功能:  批量上传发票文件保存
     *
     * @param dtoList
     * @return void
     * @author suny
     * @date 2024/1/2 9:43
     */
    void saveInfoiceFiles(List<InvoiceFilesDTO> dtoList) throws Exception;

    /**
     * 查询一段时间内的发票
     * 方法功能:认刊合同查询用。条件包括：时间范围、发票号码（queryKey）、客户名称（可填写可选择customername）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/1/5 9:03
     */
    IPage<InvoiceDTO> getInvoicesInPeriod(Page<Twinvoice> page, OrderAndItemVO query);

    /**
     * 据发票号获取发票相关详情 - （数据台账/发票台账）
     * 方法功能:根据发票id，查看预开信息（预开类型）、预收款信息（预收款类型）、核销数据（已做核销）、分配明细（已有分配）
     *
     * @param invoiceid
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.InvoiceDetailsDTO>
     * @author yanz
     * @date 2024/1/6 16:15
     */
    InvoiceDetailsDTO getInvoiceDetailsByInvoice(String invoiceid);

    /**
     * 预开发票申请POJO转DTO
     * 方法功能:有查询操作，涉及 发票表 Invoice
     *
     * @param twpreinvoiceapplications
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO>
     * @author yanz
     * @date 2024/1/6 16:20
     */
    List<PreInvoiceApplicationDTO> convertPreInvoiceApplicationToDTOs(List<Twpreinvoiceapplication> twpreinvoiceapplications);

    /**
     * 更换发票客户
     * 方法功能:
     *
     * @param customerIds
     * @param newcustomerId
     * @param newcustomername
     * @return void
     * @author songly
     * @date 2024/1/17 15:28
     */
    void updateInvoiceCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception;

    /**
     * 根据客户id获取发票列表
     * 方法功能:  根据客户id获取发票列表，关联预开发票表
     *
     * @param page
     * @param entity
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO>
     * @author suny
     * @date 2024/1/25 9:56
     */
    IPage<InvoiceDTO> getInvoiceByCustomerid(Page<Twinvoice> page, InvoiceVO entity) throws Exception;

    /**
     * 我的发票
     * 方法功能: 按发票表中开票时间范围查看我的发票，条件中可选择（仅欠款，需要关联预开发票表）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO>
     * @author suny
     * @date 2024/1/23 9:58
     */
    IPage<InvoiceDTO> getMyInvoicePageList(Page<Twinvoice> page, InvoiceVO query) throws Exception;
}
