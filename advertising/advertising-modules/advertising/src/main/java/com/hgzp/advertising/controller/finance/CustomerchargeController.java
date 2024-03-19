package com.hgzp.advertising.controller.finance;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO;
import com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO;
import com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeDTO;
import com.hgzp.advertising.pagemodel.finance.vo.BankAccountVO;
import com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeBankVO;
import com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeVO;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.finance.TwbankaccountsServiceI;
import com.hgzp.advertising.service.finance.TwcustomerchargeServiceI;
import com.hgzp.core.constant.ValidateParam;
import com.hgzp.core.model.Twbankaccounts;
import com.hgzp.core.model.Twcustomercharge;
import com.hgzp.core.model.Twpreinvoiceapplication;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static com.hgzp.core.constant.ValidateParam.add;
import static com.hgzp.core.constant.ValidateParam.edit;

/**
 * CustomerchargeController
 * 创建人：wangwk
 * 类描述：客户预收费接口
 * 创建日期：2023/10/28 16:13
 *
 * @folder finance/CustomerchargeController
 */
@RestController
@RequestMapping("/finance/customercharge")
public class CustomerchargeController extends BaseController {

    @Autowired
    TwcustomerchargeServiceI twcustomerchargeService;
    @Autowired
    TwbankaccountsServiceI twbankaccountsServiceI;
    @Autowired
    TwpreinvoiceapplicationServiceI twpreinvoiceapplicationServiceI;


    /**
     * 预收款明细分页查询
     * 方法功能: 预收款明细分页查询
     *
     * @param pageRequest 分页参数
     * @param query       查询参数
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeVO>>
     * @author wangwk
     * @date 2023/10/31 9:01
     */
    @RequestMapping("/getCustomerChargePageList")
    public Json<List<CustomerChargeVO>> getCustomerChargePageList(PageRequest pageRequest, CustomerChargeVO query) {
        Page page = getPage(pageRequest);
        IPage<CustomerChargeVO> customerChargePageList = twcustomerchargeService.getCustomerChargePageList(query, page);
        return Json.success(customerChargePageList);
    }

    /**
     * 获取收款明细详情
     * 方法功能:  获取收款明细详情
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeVO>
     * @author wangwk
     * @date 2023/11/1 10:56
     */
    @RequestMapping("/getCustomerChargeDetail")
    public Json<CustomerChargeVO> getCustomerChargeDetail(Long id) {
        CustomerChargeVO customerChargeDetail = twcustomerchargeService.getCustomerChargeDetail(id);
        return Json.success(customerChargeDetail);
    }


    /**
     * 保存客户预收费
     * 方法功能:
     * 1、判断客户是否存在账户，如果不存在则创建账户
     * 2、保存客户预收费
     * 3、保存发票
     *
     * @param customerChargeDTO
     * @return com.hgzp.core.page.Json
     * @throws Exception
     */
    @RequestMapping("/saveCustomerCharge")
    public Json saveCustomerCharge(@Validated(value = add.class) @RequestBody CustomerChargeDTO customerChargeDTO) throws Exception {
        Long invoiceId = twcustomerchargeService.saveCustomerCharge(customerChargeDTO);
        return Json.success(invoiceId);
    }


    /**
     * 修改收费明细
     * 方法功能:
     * 1、修改账户总额
     * 2、更新发票表
     * 3、更新收费明细表
     *
     * @param customerChargeDTO
     * @return void
     * @author wangwk
     * @date 2023/11/1 13:40
     */
    @RequestMapping("/updateCustomerCharge")
    public Json updateCustomerCharge(@Validated(value = edit.class) @RequestBody CustomerChargeDTO customerChargeDTO) throws Exception {
        twcustomerchargeService.updateCustomerCharge(customerChargeDTO);
        return Json.success();
    }

    /**
     * 删除收费明细
     * 方法功能:
     * 1、修改账户总额
     * 2、删除发票表
     * 3、删除收费明细表
     *
     * @param id
     * @return com.hgzp.core.page.Json
     * @author wangwk
     * @date 2023/11/1 15:24
     */
    @RequestMapping("/deleteCustomerCharge")
    public Json deleteCustomerCharge(Long id) throws Exception {
        twcustomerchargeService.deleteCustomerCharge(id);
        return Json.success();
    }

    //------------------------以下是银行流水分配使用的客户收款查询------------------------
    //<editor-fold desc="以下是银行流水分配使用的客户收款查询">

    /**
     * 查询银行流水客户收费分配分页列表
     * 方法功能:  根据查询条件获取银行流水客户收费分配分页列表
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>>
     * @author suny
     * @date 2023/12/20 13:56
     */
    @GetMapping("/getBankwCustomerChargePageList")
    public Json<List<CustomerChargeBankDTO>> getBankwCustomerChargePageList(PageRequest pageRequest, CustomerChargeBankVO query) throws Exception {
        Page<Twcustomercharge> page = getPage(pageRequest);
        IPage<CustomerChargeBankDTO> pageList = twcustomerchargeService.getBankwCustomerChargePageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 获取未分配完金额的银行流水
     * 方法功能: 获取未分配完金额的银行流水
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.model.Twbankaccounts>>
     * @author suny
     * @date 2023/12/20 13:56
     */
    @GetMapping("/getBalanceBankAccountPageList")
    public Json<List<Twbankaccounts>> getBalanceBankAccountPageList(PageRequest pageRequest, BankAccountVO query) throws Exception {
        Page<Twbankaccounts> page = getPage(pageRequest);
        IPage<Twbankaccounts> pageList = twbankaccountsServiceI.getBalanceBankAccountPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 查询有欠款的预开发票列表
     * 方法功能: 查询有欠款的预开发票列表，同时判断是否关联合同订单，如果关联则查询出合同欠款情况信息
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO>>
     * @author suny
     * @date 2023/12/20 13:57
     */
    @GetMapping("/getDebtInvoiceApplicationPageList")
    public Json<List<PreInvoiceApplicationDTO>> getDebtInvoiceApplicationPageList(PageRequest pageRequest, BaseQueryInfo query) throws Exception {
        Page<Twpreinvoiceapplication> page = getPage(pageRequest);
        IPage<PreInvoiceApplicationDTO> pageList = twpreinvoiceapplicationServiceI.getDebtInvoiceApplicationPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 新增保存银行流水分配到客户收费表
     * 方法功能: 新增保存银行流水分配到客户收费表
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/20 13:57
     */
    @PostMapping(value = "/saveBankCustomerCharge")
    public Json saveBankCustomerCharge(@Validated(value = {ValidateParam.add.class}) @RequestBody CustomerChargeBankDTO entity) throws Exception {
        twcustomerchargeService.saveBankCustomerCharge(entity);
        return Json.success();
    }

    /**
     * 修改保存银行流水分配到客户收费表
     * 方法功能: 修改保存银行流水分配到客户收费表
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/20 13:57
     */
    @PostMapping(value = "/updateBankCustomerCharge")
    public Json updateBankCustomerCharge(@Validated(value = {ValidateParam.edit.class}) @RequestBody CustomerChargeBankDTO entity) throws Exception {
        twcustomerchargeService.updateBankCustomerCharge(entity);
        return Json.success();
    }

    /**
     * 根据ID删除银行流水分配到客户收费表数据
     * 方法功能: 根据ID删除银行流水分配表，同时更新银行流水到客户收费表的分配金额
     *
     * @param ids
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/20 13:58
     */
    @PostMapping(value = "/deleteBankCustomerCharge")
    public Json deleteBankCustomerCharge(@NotNull(message = "ID不可为空") String ids) throws Exception {
        twcustomerchargeService.deleteBankCustomerCharge(ids);
        return Json.success();
    }

    /**
     * 根据id更新银行流水分配到客户收费表数据状态
     * 方法功能: 根据id更新银行流水分配到客户收费表数据状态
     * (该方法暂时不用，因为银行流水分配到客户收费表数据状态改变时需要对数据进行校验，所以改为调用updateBankCustomerCharge方法)
     *
     * @param id
     * @param istatus
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2023/12/20 13:58
     */
    @PostMapping(value = "/updateCustomerChargeStatus")
    public Json updateCustomerChargeStatus(@NotNull(message = "ID不可为空") String id, int istatus) throws Exception {
        twcustomerchargeService.updateCustomerChargeStatus(id, istatus);
        return Json.success();
    }
    //</editor-fold>

    /**
     * 获取核销记录列表 - 预开发票核销
     * 方法功能:获取核销记录列表，条件限制：预开申请id不为空、已提交&已确认的数据、只取“预开”类型的预开发票申请记录，“挂开”类型的不显示
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>>
     * @author yanz
     * @date 2023/12/22 12:43
     */
    @GetMapping("/getInvoiceChargePageList")
    public Json<List<CustomerChargeBankDTO>> getInvoiceChargePageList(PageRequest pageRequest, BaseQueryInfo query) throws Exception {
        Page<Twcustomercharge> page = getPage(pageRequest);
        IPage<CustomerChargeBankDTO> pageList = twcustomerchargeService.getInvoiceChargePageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 保存核销结果/核销确认结果
     * 方法功能:保存核销结果/核销确认结果、意见
     *
     * @param id
     * @param comments
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/22 14:22
     */
    @PostMapping("/submitConfirmation")
    public Json submitConfirmation(Long id, Integer iStatus, String comments) throws Exception {
        if (twcustomerchargeService.submitConfirmation(id, iStatus, comments)) {
            return Json.success();
        }
        return Json.fail("保存核销结果失败");
    }

    /**
     * 核销预开发票
     * 方法功能:核销预开发票
     *
     * @param customerChargeId
     * @param orderitemIds     英文逗号分隔
     * @param dateString       传入分摊日期（字符串），yyyy-MM-dd 格式
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/23 10:24
     */
    @PostMapping("/writeOffPreinvoiceapplication")
    public Json writeOffPreinvoiceapplication(Long customerChargeId, String orderitemIds, String dateString) throws Exception {
        List<Long> idList = CollUtil.newArrayList(orderitemIds.split(",")).stream().map(Long::valueOf).distinct().collect(Collectors.toList());
        twcustomerchargeService.writeOffPreinvoiceapplication(customerChargeId, idList, dateString);
        return Json.success();
    }

    /**
     * 查询收费表中 有余额的 客户预收费项目
     * 方法功能:根据客户名称或者发票号，查询收费表中 有余额的 客户预收费项目
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/25 10:56
     */
    @GetMapping("/getCustomerChargeBankDTOList")
    public Json getCustomerChargeBankDTOList(PageRequest pageRequest, CustomerChargeBankVO query) throws Exception {
        Page<Twcustomercharge> page = getPage(pageRequest);
        IPage<CustomerChargeBankDTO> pageList = twcustomerchargeService.getCustomerChargeBankDTOList(page, query);
        return Json.success(pageList);
    }

    /**
     * 根据银行流水id获取客户收费信息
     * 方法功能:根据银行流水id获取客户收费信息
     *
     * @param bankid
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO>>
     * @author suny
     * @date 2024/3/6 18:42
     */
    @RequestMapping("/getCustomerChargeByBankid")
    public Json<List<CustomerChargeBankDTO>> getCustomerChargeByBankid(Long bankid) throws Exception {
        List<CustomerChargeBankDTO> twcustomerchargeList = twcustomerchargeService.getCustomerChargeByBankid(bankid);
        return Json.success(twcustomerchargeList);
    }

    /**
     * 财务人员直接对银行流水进行核销
     * 方法功能:  财务人员直接对银行流水进行核销
     *
     * @param entity
     * @return com.hgzp.core.page.Json
     * @author suny
     * @date 2024/3/15 16:54
     */
    @PostMapping(value = "/bankAcountWriteOff")
    public Json bankAcountWriteOff(@Validated(value = {ValidateParam.add.class}) @RequestBody CustomerChargeBankDTO entity) throws Exception {
        if (StringUtils.hasText(entity.getItemids())) {
            return Json.fail("银行流水核销必须选择广告刊期");
        }
        twcustomerchargeService.bankAcountWriteOff(entity);
        return Json.success();
    }
}
