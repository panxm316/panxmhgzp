package com.hgzp.advertising.controller.business;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO;
import com.hgzp.advertising.pagemodel.business.vo.PreInvoiceApplicationVO;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationdetailServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationfileServiceI;
import com.hgzp.core.model.Twpreinvoiceapplication;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.core.page.PageRequest;
import com.hgzp.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TwpreinvoiceapplicationController
 * 创建人：yanz
 * 类描述：预开发票申请表 前端控制器
 * 创建日期：2023/11/10 12:48
 *
 * @folder business/Twpreinvoiceapplication
 */
@RestController
@RequestMapping("/business/twpreinvoiceapplication")
public class TwpreinvoiceapplicationController extends BaseController {
    @Autowired
    TwpreinvoiceapplicationServiceI preinvoapplyServiceI;
    @Autowired
    TwpreinvoiceapplicationdetailServiceI preinvoapplydetailServiceI;
    @Autowired
    TwpreinvoiceapplicationfileServiceI fileServiceI;


    /**
     * 获取发票申请列表
     * 方法功能:获取发票申请列表，查询条件为 申请日期范围、发票号、客户名称、合同号、欠款状态（全部、仅欠款）
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.vo.PreInvoiceApplicationVO>>
     * @author yanz
     * @date 2023/11/10 15:43
     */
    @GetMapping("/getInvoiceApplicationPageList")
    public Json<List<PreInvoiceApplicationVO>> getInvoiceApplicationPageList(PageRequest pageRequest, PreInvoiceApplicationVO query) throws Exception {
        Page<Twpreinvoiceapplication> page = getPage(pageRequest);
        IPage<PreInvoiceApplicationVO> pageList = preinvoapplyServiceI.getInvoiceApplicationPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 获取预开申请列表（发票打印用）
     * 方法功能:获取预开申请列表（发票打印用），区别于发票申请处的列表查询，这里查询有 “审批通过 且 未打印”的限制
     *
     * @param pageRequest
     * @param query
     * @return com.hgzp.core.page.Json<java.util.List < com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO>>
     * @author yanz
     * @date 2023/12/19 9:49
     */
    @GetMapping("/getPreinvoiceapplicationPageList")
    public Json<List<PreInvoiceApplicationDTO>> getPreInvoiceApplicationPageList(PageRequest pageRequest, BaseQueryInfo query) {
        Page<Twpreinvoiceapplication> page = getPage(pageRequest);
        IPage<PreInvoiceApplicationDTO> pageList = preinvoapplyServiceI.getPreInvoiceApplicationPageList(page, query);
        return Json.success(pageList);
    }

    /**
     * 添加新的发票申请（待审）
     * 方法功能:提交新的发票申请。请求体应包含业务员ID、客户ID、开票金额、开票类型、合同ID（如果是预开）、开票项目、
     * 客户识别号、地址电话、银行及账户、经营主体ID和附件（如果是挂开）
     *
     * @param applyDTO
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/11/8 13:41
     */
    @PostMapping("/savePreinvoapplyAsPending")
    public Json savePreinvoapplyAsPending(@RequestBody PreInvoiceApplicationDTO applyDTO) throws Exception {
        return preinvoapplyServiceI.saveAsPending(applyDTO) ? Json.success() : Json.fail();
    }

    /**
     * 获取预开发票申请VO（详情）
     * 方法功能:获取预开发票申请VO（详情）
     *
     * @param id
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.business.vo.PreInvoiceApplicationVO>
     * @author yanz
     * @date 2023/11/13 9:15
     */
    @GetMapping("/getPreinvoiceapplicationById")
    public Json<PreInvoiceApplicationVO> getPreinvoiceapplicationVOById(Long id) throws Exception {
        PreInvoiceApplicationVO preinvoiceapply = preinvoapplyServiceI.getPreinvoiceapplicationVOByIdOrObj(id, null);
        return ObjUtil.isNotNull(preinvoiceapply) ? Json.success(preinvoiceapply) : Json.fail("未找到对应申请");
    }

    /**
     * 修改预开发票申请
     * 方法功能:修改预开发票申请
     *
     * @param preInvoiceApplicationDTO
     * @return com.hgzp.core.page.Json
     * @throws Exception
     */
    @PostMapping("/updatePreinvoiceapplication")
    public Json updatePreinvoiceapplication(@RequestBody PreInvoiceApplicationDTO preInvoiceApplicationDTO) throws Exception {
        Boolean updated = preinvoapplyServiceI.updatePreinvoiceapplication(preInvoiceApplicationDTO);
        return updated ? Json.success() : Json.fail();
    }

    /**
     * 删除预开发票申请
     * 方法功能:删除预开发票申请
     *
     * @param ids 多个id英文逗号分隔
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/12 14:46
     */
    @PostMapping("/deletePreinvoiceapplication")
    public Json deletePreinvoiceapplication(String ids) throws Exception {
        return preinvoapplyServiceI.deletePreinvoiceapplication(ids) ? Json.success() : Json.fail();
    }

    /**
     * 预开发票申请发起审批
     * 方法功能: 预开发票申请发起审批
     *
     * @param ids
     * @param flowId
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/11/25 10:28
     */
    @PostMapping("/approvePreinvoiceapplication")
    public Json approvePreinvoiceapplication(String ids, String flowId) {
        // 申请
        Json submitted = null;
        try {
            submitted = preinvoapplyServiceI.submitApply(ids, flowId);
        } catch (Exception e) {
            return Json.fail(e.getMessage());
        }
        if (submitted.isSuccess()) {
            // 更新流程状态
            try {
                preinvoapplyServiceI.updateApplyStatus(ids, ApproveStatus.APPROVE_EDITING.getKey(), null);
            } catch (Exception e) {
                return Json.fail("申请状态更新失败");
            }
            return submitted;
        }
        return Json.fail("申请失败");
    }

    /**
     * 退回预开发票申请
     * 方法功能:根据预开申请id，填写退回意见，退回预开发票申请
     *
     * @param preinvoiceapplicationId 预开申请id
     * @param rejectReason            退回意见
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/12/21 14:06
     */
    @PostMapping("/rejectInvoice")
    public Json rejectInvoice(Long preinvoiceapplicationId, String rejectReason) throws Exception {
        preinvoapplyServiceI.rejectInvoice(preinvoiceapplicationId, rejectReason);
        return Json.success();
    }

    /**
     * 预开查询核销列表（据预开申请id，获取预开申请详情DTO）
     * 方法功能:据预开申请id，获取预开申请详情DTO，内含预开发票申请对应的全部订单（orderid）的订单刊期（orderitem）
     *
     * @param preinvoiceapplicationId
     * @param orderId 指定查询的订单，若不指定则查询全部相关订单
     * @return com.hgzp.core.page.Json<com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO>
     * @author yanz
     * @date 2023/12/22 15:10
     */
    @GetMapping("/getPreInvoiceApplyDtoById")
    public Json<PreInvoiceApplicationDTO> getPreInvoiceApplyDtoById(Long preinvoiceapplicationId, Long orderId) {
        PreInvoiceApplicationDTO preInvoiceApplicationDTO = preinvoapplyServiceI.getPreInvoiceApplyDtoById(preinvoiceapplicationId, orderId);
        return Json.success(preInvoiceApplicationDTO);
    }
}