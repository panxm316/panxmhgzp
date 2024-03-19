package com.hgzp.advertising.controller.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.business.dto.BankAccountAllocateDTO;
import com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO;
import com.hgzp.advertising.pagemodel.business.vo.BankAccountAllocateVO;
import com.hgzp.advertising.pagemodel.finance.vo.BankAccountVO;
import com.hgzp.advertising.service.business.TwbankaccountallocateServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.finance.TwbankaccountsServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twbankaccountallocate;
import com.hgzp.core.model.Twbankaccounts;
import com.hgzp.core.model.Twpreinvoiceapplication;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * BankAccountAllocateController
 * 创建人：suny
 * 类描述：银行流水分配表 前端控制器（银行流水分配表已经作废，功能全部作废）
 * 创建日期：2023/12/6 14:33
 *
 * @folder business/BankAccountAllocateController
 */
@RestController
@RequestMapping("/business/bankaccountallocate")
public class BankAccountAllocateController extends BaseController {
    @Autowired
    TwbankaccountsServiceI twbankaccountsServiceI;
    @Autowired
    TwpreinvoiceapplicationServiceI twpreinvoiceapplicationServiceI;
    @Autowired
    TwbankaccountallocateServiceI twbankaccountallocateServiceI;

    /**
     * 查询银行流水分配表分页列表
     * 方法功能:  根据查询条件获取银行流水分配表分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.BankAccountAllocateDTO>>
     * @author suny
     * @date 2023/12/11 8:59
     */
//    @GetMapping("/getBankAccountAllocatePageList")
//    public Json<List<BankAccountAllocateDTO>> getBankAccountAllocatePageList(PageRequest pageRequest, BankAccountAllocateVO query) throws Exception {
//        Page<Twbankaccountallocate> page = getPage(pageRequest);
//        IPage<BankAccountAllocateDTO> pageList = twbankaccountallocateServiceI.getBankAccountAllocatePageList(page, query);
//        return Json.success(pageList);
//    }

    /**
     * 获取未分配完金额的银行流水
     * 方法功能: 获取未分配完金额的银行流水
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twbankaccounts>>
     * @author suny
     * @date 2023/12/5 9:53
     */
//    @GetMapping("/getBalanceBankAccountPageList")
//    public Json<List<Twbankaccounts>> getBalanceBankAccountPageList(PageRequest pageRequest, BankAccountVO query) throws Exception {
//        Page<Twbankaccounts> page = getPage(pageRequest);
//        IPage<Twbankaccounts> pageList = twbankaccountsServiceI.getBalanceBankAccountPageList(page, query);
//        return Json.success(pageList);
//    }

    /**
     * 查询有欠款的预开发票列表
     * 方法功能: 查询有欠款的预开发票列表，同时判断是否关联合同订单，如果关联则查询出合同欠款情况信息
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO>>
     * @author suny
     * @date 2023/12/6 10:05
     */
//    @GetMapping("/getDebtInvoiceApplicationPageList")
//    public Json<List<PreInvoiceApplicationDTO>> getDebtInvoiceApplicationPageList(PageRequest pageRequest, BaseQueryInfo query) throws Exception {
//        Page<Twpreinvoiceapplication> page = getPage(pageRequest);
//        IPage<PreInvoiceApplicationDTO> pageList = twpreinvoiceapplicationServiceI.getDebtInvoiceApplicationPageList(page, query);
//        return Json.success(pageList);
//    }

    /**
     * 新增保存银行流水分配表
     * 方法功能: 新增保存银行流水分配表
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/7 10:52
     */
//    @PostMapping(value = "/saveDebtAllocate")
//    public Json saveDebtAllocate(@Validated(value = {ValidateParam.add.class}) @RequestBody BankAccountAllocateDTO entity) throws Exception {
//        twbankaccountallocateServiceI.saveBankAccountAllocate(entity);
//        return Json.success();
//    }

    /**
     * 修改保存银行流水分配表
     * 方法功能: 修改保存银行流水分配表
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/7 10:49
     */
//    @PostMapping(value = "/updateDebtAllocate")
//    public Json updateDebtAllocate(@Validated(value = {ValidateParam.edit.class}) @RequestBody BankAccountAllocateDTO entity) throws Exception {
//        twbankaccountallocateServiceI.updateBankAccountAllocate(entity);
//        return Json.success();
//    }


    /**
     * 根据ID删除银行流水分配表
     * 方法功能: 根据ID删除银行流水分配表，同时更新银行流水表的分配金额
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/12 10:38
     */
//    @PostMapping(value = "/deleteBankAccountAllocateById")
//    public Json deleteBankAccountAllocateById(@NotNull(message = "ID不可为空") String ids) throws Exception {
//        twbankaccountallocateServiceI.deleteBankAccountAllocate(ids);
//        return Json.success();
//    }

    /**
     * 根据id更新银行流水分配状态
     * 方法功能: 根据id更新银行流水分配状态
     *
     * @param id
     * @param istatus
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/12 16:57
     */
//    @PostMapping(value = "/updateBankAccountAllocateStatus")
//    public Json updateBankAccountAllocateStatus(@NotNull(message = "ID不可为空") String id, int istatus) throws Exception {
//        twbankaccountallocateServiceI.updateBankAccountAllocateStatus(id, istatus);
//        return Json.success();
//    }
}

