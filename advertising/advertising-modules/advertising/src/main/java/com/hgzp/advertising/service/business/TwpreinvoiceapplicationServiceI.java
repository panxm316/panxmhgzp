package com.hgzp.advertising.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO;
import com.hgzp.advertising.pagemodel.business.vo.PreInvoiceApplicationVO;
import com.hgzp.core.model.Twpreinvoiceapplication;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 预开发票申请表 服务类
 * </p>
 *
 * @author wwk
 * @since 2023-11-08
 */
public interface TwpreinvoiceapplicationServiceI extends IService<Twpreinvoiceapplication> {

    /**
     * 获取发票申请列表
     * 方法功能:
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twpreinvoiceapplication>
     * @author yanz
     * @date 2023/11/8 10:49
     */
    IPage<PreInvoiceApplicationVO> getInvoiceApplicationPageList(Page<Twpreinvoiceapplication> page,
                                                                 PreInvoiceApplicationVO query);

    /**
     * 将发票申请保存为待审
     * 方法功能:将发票申请保存为待审
     *
     * @param applyDTO
     * @return java.lang.Boolean
     * @author yanz
     * @date 2023/11/8 14:27
     */
    Boolean saveAsPending(PreInvoiceApplicationDTO applyDTO) throws Exception;

    /**
     * 据订单id取得可申请额
     * 方法功能:据订单id取得可申请额
     *
     * @param orderIds
     * @param onlyCalculateApprovedOrders 仅计算“已通过审批”订单的欠款额
     * @param currApply                   当前申请额度，onlyCalculateApprovedOrders为false时需要
     * @return java.util.Map<java.lang.Long, java.math.BigDecimal>
     * @author yanz
     * @date 2023/12/8 10:43
     */
    BigDecimal getAvailableAmount(List<Long> orderIds, Boolean onlyCalculateApprovedOrders, BigDecimal currApply);

    /**
     * 获取预开发票申请VO（详情）
     * 方法功能:id和对象传一个就行，都传只转VO
     *
     * @param preinvoiceapplicationid
     * @param apply
     * @return com.hgzp.advertising.pagemodel.business.vo.TwpreinvoiceapplicationVO
     * @author yanz
     * @date 2023/11/13 9:06
     */
    PreInvoiceApplicationVO getPreinvoiceapplicationVOByIdOrObj(Long preinvoiceapplicationid,
                                                                Twpreinvoiceapplication apply);

    /**
     * 修改预开发票申请
     * 方法功能:修改预开发票申请
     *
     * @param applyDTO
     * @return java.lang.Boolean
     * @author yanz
     * @date 2023/11/13 9:20
     */
    Boolean updatePreinvoiceapplication(PreInvoiceApplicationDTO applyDTO) throws Exception;

    /**
     * 审批发票申请
     * 方法功能:审批发票申请
     *
     * @param ids    英文逗号分隔
     * @param flowId
     * @return boolean
     * @author yanz
     * @date 2023/11/25 10:30
     */
    Json submitApply(String ids, String flowId);

    /**
     * 更新发票申请状态
     * 方法功能:更新发票申请状态
     *
     * @param ids         id串，英文逗号分隔
     * @param status
     * @param approveDesc
     * @return java.lang.Boolean
     * @author yanz
     * @date 2023/11/27 10:54
     */
    Boolean updateApplyStatus(String ids, Integer status, String approveDesc);

    /**
     * 按订单id取对应发票欠款额
     * 方法功能:按订单id取对应“发票欠款额”（申请额-已收额）
     *
     * @param orderIds
     * @param onlyCalculateApprovedOrders
     * @return java.util.Map<java.lang.String, java.math.BigDecimal>
     * @author yanz
     * @date 2023/12/5 15:35
     */
    BigDecimal getInvoiceArrearageSumByOrderIds(List<Long> orderIds, Boolean onlyCalculateApprovedOrders);

    /**
     * 删除预开发票申请
     * 方法功能:删除预开发票申请
     *
     * @param ids 英文逗号分隔
     * @return java.lang.Boolean
     * @throws Exception
     */
    Boolean deletePreinvoiceapplication(String ids) throws Exception;

    /**
     * 方法功能: 查询有欠款的预开发票列表，同时判断是否关联合同订单，如果关联则查询出合同欠款情况信息
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.vo.PreInvoiceApplicationVO>
     * @author suny
     * @date 2023/12/5 10:21
     */
    IPage<PreInvoiceApplicationDTO> getDebtInvoiceApplicationPageList(Page<Twpreinvoiceapplication> page,
                                                                      BaseQueryInfo query) throws Exception;

    /**
     * 获取预开申请信息（发票打印用）
     * 方法功能:获取预开申请信息（发票打印用），区别于发票申请处的列表查询，这里查询有 “审批通过 且 未打印”的限制
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO>
     * @author yanz
     * @date 2023/12/19 9:51
     */
    IPage<PreInvoiceApplicationDTO> getPreInvoiceApplicationPageList(Page<Twpreinvoiceapplication> page,
                                                                     BaseQueryInfo query);

    /**
     * 退回预开发票申请
     * 方法功能:根据预开申请id，填写退回意见，退回预开发票申请
     *
     * @param preinvoiceapplicationId
     * @param rejectReason
     * @return void
     * @author yanz
     * @date 2023/12/21 13:50
     */
    void rejectInvoice(Long preinvoiceapplicationId, String rejectReason);

    /**
     * 预开查询核销列表（据预开申请id，获取预开申请详情DTO）
     * 方法功能:据预开申请id，获取预开申请详情DTO，内含预开发票申请对应的全部订单（orderid）的订单刊期（orderitem）
     *
     * @param preinvoiceapplicationId
     * @param orderId                 指定查询的订单，若不指定则查询全部相关订单
     * @return com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO
     * @author yanz
     * @date 2023/12/22 15:20
     */
    PreInvoiceApplicationDTO getPreInvoiceApplyDtoById(Long preinvoiceapplicationId, Long orderId);

    /**
     * 更新预开发票申请的已收金额（分摊/核销）
     * 方法功能:预开申请id和新收金额，更新预开发票申请的已收金额
     *
     * @param preinvoiceapplicationId
     * @param amountReceived          金额为负就减去
     * @return java.lang.Boolean
     * @author yanz
     * @date 2024/1/10 9:51
     */
    Boolean updateReceivedAmount(Long preinvoiceapplicationId, BigDecimal amountReceived);

    /**
     * 更新预开发票客户（合并客户用）
     * 方法功能:
     *
     * @param customerIds
     * @param newcustomerId
     * @param newcustomername
     * @return com.hgzp.core.page.Json
     * @author songly
     * @date 2024/1/17 15:58
     */
    void updateInvoiceApplicationCustomer(String customerIds, Long newcustomerId, String newcustomername)throws Exception;
}
