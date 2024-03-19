package com.hgzp.advertising.controller.finance;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceDetailsDTO;
import com.hgzp.advertising.pagemodel.finance.dto.InvoiceFilesDTO;
import com.hgzp.advertising.pagemodel.finance.vo.InvoiceVO;
import com.hgzp.advertising.service.finance.InvoiceFilesServiceI;
import com.hgzp.advertising.service.finance.InvoiceServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twinvoice;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * InvoiceController
 * 创建人：muyn
 * 类描述：发票相关 前端控制器
 * 创建日期：2023/12/15 12:48
 *
 * @folder finance/InvoiceController
 */
@Validated
@RestController
@RequestMapping("/finance/invoice")
public class InvoiceController extends BaseController {

    @Autowired
    private InvoiceServiceI invoiceService;

    @Autowired
    private InvoiceFilesServiceI invoiceFilesServiceI;

    /**
     * 保存预开申请表信息至发票表（临时票号）
     * 方法功能:据预开发票申请id，保存id对应的预开信息至发票表（临时票号），返回保存的发票ID
     *
     * @param preinvoiceapplicationId
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/19 14:43
     */
    @PostMapping("/saveInvoiceByPreInvoApplyId")
    public Json<Long> saveInvoiceByPreInvoApplyId(Long preinvoiceapplicationId) throws Exception {
        Long invoiceId = invoiceService.saveInvoiceByPreInvoApplyId(preinvoiceapplicationId);
        return Json.success(invoiceId);
    }

    /**
     * 获取单个发票信息
     * 方法功能:据发票id，取单个发票信息
     *
     * @param id
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/19 10:28
     */
    @GetMapping("/getInvoiceById")
    public Json<InvoiceDTO> getInvoiceById(Long id) {
        return Json.success(invoiceService.getInvoiceById(id));
    }

    /**
     * 保存三方获取的发票号、发票编码
     * 方法功能:据传入的发票id（invoiceId），保存三方获取的发票号（invoice）、发票编码（invoiceCode）至发票表、预开发票申请表
     *
     * @param invoice
     * @param invoiceCode
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/21 9:03
     */
    @PostMapping("/saveInvoiceCode")
    public Json saveInvoiceCode(Long invoiceId, String invoice, String invoiceCode) throws Exception {
        invoiceService.saveInvoiceCode(invoiceId, invoice, invoiceCode);
        return Json.success();
    }

    /**
     * 查询发票列表
     * 方法功能:查询发票列表，参数：时间段、客户id、关键字（发票号、客户名称、业务员、开票项目）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.dto.InvoiceDTO>>
     * @author yanz
     * @date 2023/12/28 10:17
     */
    @GetMapping("/getInvoicePageList")
    public Json<List<InvoiceDTO>> getInvoicePageList(PageRequest pageRequest, InvoiceVO query) {
        Page<Twinvoice> page = getPage(pageRequest);
        IPage<InvoiceDTO> pageList = invoiceService.getInvoicePageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 更新发票数据（发票打印）
     * 方法功能:以InvoiceDTO内容，更新发票数据（发票打印）
     *
     * @param invoiceDTO
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/28 14:25
     */
    @PostMapping("/updateInvoiceByInvoiceDTO")
    public Json updateInvoiceByInvoiceDTO(@Validated(value = {ValidateParam.edit.class}) @RequestBody InvoiceDTO invoiceDTO) throws Exception {
        invoiceService.updateInvoiceByInvoiceDTO(invoiceDTO);
        return Json.success();
    }

    /**
     * 删除发票关联文件
     * 方法功能:删除发票关联文件
     *
     * @param ids 英文逗号分隔
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/28 17:06
     */
    @PostMapping("/deleteInvoiceFilesByIds")
    public Json deleteInvoiceFilesByIds(@NotNull(message = "ID不可为空") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        invoiceFilesServiceI.removeByIds(idList);
        return Json.success();
    }

    /**
     * 批量上传发票文件保存
     * 方法功能: 批量上传发票文件保存，根据文件名中的发票号、发票编码进行自动对应
     *
     * @param invoiceFileDTO
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/1/2 9:42
     */
    @PostMapping("/saveInfoiceFiles")
    public Json saveInfoiceFiles(@RequestBody List<InvoiceFilesDTO> invoiceFileDTO) throws Exception {
        invoiceService.saveInfoiceFiles(invoiceFileDTO);
        return Json.success();
    }

    /**
     * 查询一段时间内的发票
     * 方法功能:认刊合同查询用。条件包括：时间范围、发票号码（queryKey）、客户名称（可填写可选择customername）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>>
     * @author yanz
     * @date 2024/1/5 9:02
     */
    @GetMapping("/getInvoicesInPeriod")
    public Json<List<InvoiceDTO>> getInvoicesInPeriod(PageRequest pageRequest, OrderAndItemVO query) throws Exception {
        Page<Twinvoice> page = getPage(pageRequest);
        IPage<InvoiceDTO> invoicesInPeriod = invoiceService.getInvoicesInPeriod(page, query);
        return Json.success(invoicesInPeriod);
    }

    /**
     * 据发票号获取发票相关详情 - （数据台账/发票台账）
     * 方法功能:根据发票号，查看预开信息（预开类型）、预收款信息（预收款类型）、核销数据（已做核销）、分配明细（已有分配）
     *
     * @param invoiceid
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.dto.InvoiceDetailsDTO>>
     * @author yanz
     * @date 2024/1/6 16:14
     */
    @GetMapping("/getInvoiceDetailsByInvoice")
    public Json<InvoiceDetailsDTO> getInvoiceDetailsByInvoice(String invoiceid) {
        InvoiceDetailsDTO invoiceDetailsByInvoice = invoiceService.getInvoiceDetailsByInvoice(invoiceid);
        return Json.success(invoiceDetailsByInvoice);
    }
}
