package com.hgzp.advertising.controller.emnus;

import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.approve.ApproveType;
import com.hgzp.advertising.emnus.contract.ContractType;
import com.hgzp.advertising.emnus.contract.SalesContractType;
import com.hgzp.advertising.emnus.contract.StampType;
import com.hgzp.advertising.emnus.customer.CustomerType;
import com.hgzp.advertising.emnus.file.SfileType;
import com.hgzp.advertising.emnus.file.WorkReport;
import com.hgzp.advertising.emnus.finance.InvoiceStatus;
import com.hgzp.advertising.emnus.finance.InvoiceType;
import com.hgzp.advertising.emnus.finance.PreinvoiceStyle;
import com.hgzp.core.emnus.MessageTypeEnum;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.core.web.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EmnusController
 * 创建人：songly
 * 类描述：TODO
 * 创建日期：2023/10/28 12:35
 *
 * @folder emnus/EmnusController
 */
@Validated
@RestController
@RequestMapping("/enums/emnusset")
public class EmnusSetController extends BaseController {
    /**
     * 获取审批状态
     * 方法功能:获取审批状态
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2023/10/28 12:39
     */
    @GetMapping("/getApproveStatusCombo")
    public Json<List<DataCombo>> getApproveStatusCombo() {
        List<ApproveStatus> enumList = Arrays.asList(ApproveStatus.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.key.toString(), item.name))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 获取客户类型
     * 方法功能: 获取客户类型
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author hgsongly
     * @date 2023/10/28 12:41
     */
    @GetMapping("/getCustomerTypeCombo")
    public Json<List<DataCombo>> getCustomerTypeCombo() {
        List<CustomerType> enumList = Arrays.asList(CustomerType.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.key.toString(), item.name))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 获取发票状态
     * 方法功能: 获取发票状态
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2023/10/28 12:42
     */
    @GetMapping("/getInvoiceStatusCombo")
    public Json<List<DataCombo>> getInvoiceStatusCombo() {
        List<InvoiceStatus> enumList = Arrays.asList(InvoiceStatus.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.getKey().toString(), item.getName()))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 获取发票类型
     * 方法功能: 获取发票类型
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author hgsongly
     * @date 2023/10/28 12:44
     */
    @GetMapping("/getInvoiceTypeCombo")
    public Json<List<DataCombo>> getInvoiceTypeCombo() {
        List<InvoiceType> enumList = Arrays.asList(InvoiceType.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.key.toString(), item.name))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 获取开票样式
     * 方法功能: 获取开票样式
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2023/10/28 12:45
     */
    @GetMapping("/getPreinvoiceStyleCombo")
    public Json<List<DataCombo>> getPreinvoiceStyleCombo() {
        List<PreinvoiceStyle> enumList = Arrays.asList(PreinvoiceStyle.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.key.toString(), item.name))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 获取文件类别
     * 方法功能:    获取文件类别
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2023/10/28 12:46
     */
    @GetMapping("/getFileTypeCombo")
    public Json<List<DataCombo>> getFileTypeCombo() {
        List<SfileType> enumList = Arrays.asList(SfileType.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.key.toString(), item.name))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 获取工作报告类型
     * 方法功能:  获取工作报告类型
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author hgsongly
     * @date 2023/10/28 12:47
     */
    @GetMapping("/getWorkReportCombo")
    public Json<List<DataCombo>> getWorkReportCombo() {
        List<WorkReport> enumList = Arrays.asList(WorkReport.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.key.toString(), item.name))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 获取审批类型：
     * 方法功能: （0-预约、1-加刊、2-改刊、3-停刊、4-折扣）
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2023/12/1 9:32
     */
    @GetMapping("/getApproveTypeCombo")
    public Json<List<DataCombo>> getApproveTypeCombo() {
        List<ApproveType> enumList = Arrays.asList(ApproveType.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.key.toString(), item.name))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 获取消息类型
     * 方法功能:获取消息类型
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2024/1/8 10:26
     */
    @GetMapping("/getMessageTypeCombo")
    public Json<List<DataCombo>> getMessageTypeCombo() {
        List<MessageTypeEnum> enumList = Arrays.asList(MessageTypeEnum.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.getType().toString(), item.getName()))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 合同类型下拉数据
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2024/3/15 18:26
     */
    @GetMapping("/getContractTypeCombo")
    public Json<List<DataCombo>> getContractTypeCombo() {
        List<ContractType> enumList = Arrays.asList(ContractType.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.getKey().toString(), item.getName()))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 销售合同类型下拉数据
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2024/3/15 18:26
     */
    @GetMapping("/getSalesContractTypeCombo")
    public Json<List<DataCombo>> getSalesContractTypeCombo() {
        List<SalesContractType> enumList = Arrays.asList(SalesContractType.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.getKey().toString(), item.getName()))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }

    /**
     * 用章类型下拉数据
     * 方法功能:
     *
     * @param
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.core.page.DataCombo>>
     * @author songly
     * @date 2024/3/15 18:26
     */
    @GetMapping("/getStampTypeCombo")
    public Json<List<DataCombo>> getStampTypeCombo() {
        List<StampType> enumList = Arrays.asList(StampType.values());
        List<DataCombo> comboList = enumList.stream()
                .map(item -> new DataCombo(item.getKey().toString(), item.getName()))
                .collect(Collectors.toList());
        return Json.success(comboList);
    }
}