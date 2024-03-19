package com.hgzp.advertising.service.business.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.business.InvoiceApplyType;
import com.hgzp.advertising.emnus.file.SfileType;
import com.hgzp.advertising.emnus.schedule.PublishStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO;
import com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO;
import com.hgzp.advertising.pagemodel.business.vo.PreInvoiceApplicationVO;
import com.hgzp.advertising.pagemodel.customer.vo.ProcessInstanceVo;
import com.hgzp.advertising.service.ad.TwadprojectServiceI;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.ad.impl.TworderitemServiceImpl;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationdetailServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationfileServiceI;
import com.hgzp.advertising.service.finance.TwcustomerchargeServiceI;
import com.hgzp.advertising.service.flow.IProcessInstanceService;
import com.hgzp.advertising.service.flow.TbflowServiceI;
import com.hgzp.advertising.service.resource.TwresourcesServiceI;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.advertising.service.system.TwparameterServiceI;
import com.hgzp.advertising.service.system.impl.TbemployServiceImpl;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.business.TwpreinvoiceapplicationMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 预开发票申请表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwpreinvoiceapplicationServiceImpl extends ServiceImpl<TwpreinvoiceapplicationMapper,
        Twpreinvoiceapplication> implements TwpreinvoiceapplicationServiceI {
    @Autowired
    private TwpreinvoiceapplicationdetailServiceI detailServiceI;
    @Autowired
    private TwpreinvoiceapplicationfileServiceI fileServiceI;
    @Autowired
    private TworderServiceI orderServiceI;
    @Autowired
    private TworderitemServiceI orderitemServiceI;
    @Autowired
    private TwresourcesServiceI sourceServiceI;
    @Autowired
    private TbdeptServiceI deptServiceI;
    @Autowired
    private TbemployServiceImpl employServiceI;
    @Resource
    private IProcessInstanceService processInstanceService;
    @Autowired
    TworderServiceI tworderServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Autowired
    TworderitemServiceImpl tworderitemServiceI;
    @Autowired
    TwcustomerchargeServiceI twcustomerchargeServiceI;
    @Autowired
    TbflowServiceI flowServiceI;
    @Autowired
    TwparameterServiceI twparameterServiceI;
    @Autowired
    private TwadprojectServiceI adprojectServiceI;

    /**
     * 获取发票申请列表
     * 方法功能:获取发票申请列表，查询条件为 申请日期范围、发票号、客户名称、合同号、欠款状态（全部、仅欠款）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Twpreinvoiceapplication>
     * @author yanz
     * @date 2023/11/8 10:49
     */
    @Override
    public IPage<PreInvoiceApplicationVO> getInvoiceApplicationPageList(Page<Twpreinvoiceapplication> page,
                                                                        PreInvoiceApplicationVO query) {
        // 查询条件为 申请日期范围、仅欠款、客户名称(customername)、发票号(invoice)、合同号(contractNum)
        LambdaQueryWrapper<Twpreinvoiceapplication> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNotEmpty(query.getQueryKey())) {
            lqw.or().like(Twpreinvoiceapplication::getCustomername, query.getQueryKey());
            lqw.or().like(Twpreinvoiceapplication::getInvoice, query.getQueryKey());
//             TODO 有更好的实现劳驾叫我一下，暂时没想出别的办法，框架子查询工具试不通 - yanz
//                  相关字段，数据库变更记得修改：
//                  【twpreinvoiceapplicationdetail表】preinvoiceapplicationid、orderid；
//                  【tworder表】scontractnum
            lqw.or().apply(
                    "id IN (SELECT twpreinvoiceapplicationdetail.preinvoiceapplicationid " +
                            "FROM twpreinvoiceapplicationdetail " +
                            "WHERE twpreinvoiceapplicationdetail.orderid IN (" +
                            "    SELECT tworder.id " +
                            "    FROM tworder " +
                            "    WHERE tworder.scontractnum LIKE {0}))", "%" + query.getQueryKey() + "%");
        }
        // 日期限制
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Twpreinvoiceapplication::getDapplytime, query.getStartTime());
        if (ObjUtil.isNotNull(query.getEndTime())) {
            lqw.lt(Twpreinvoiceapplication::getDapplytime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        // “仅欠款”标记 - 当前表中 申请金额 与 已还金额 比对
        if (query.getDebtOnly()) {
//             TODO 相关字段，数据库变更记得修改：
//                  【twpreinvoiceapplication】namountapply、namountreceived；
            lqw.apply("namountapply > namountreceived");
        }

        // POJO转VO
        Page<Twpreinvoiceapplication> paged = this.page(page, lqw);
        Page<PreInvoiceApplicationVO> voPage = new Page<>();
        if (paged.getTotal() == 0) {
            return voPage;
        }
        List<PreInvoiceApplicationVO> result = paged.getRecords().stream()
                .map(item -> this.getPreinvoiceapplicationVOByIdOrObj(null, item)).collect(Collectors.toList());

        voPage.setRecords(result);
        voPage.setTotal(result.size());
        return voPage;
    }

    /**
     * 将发票申请保存为待审
     * 方法功能:将发票申请保存为待审
     * 暂时只有挂开，预开需要数据的方法没引入
     *
     * @param applyDTO
     * @return java.lang.Boolean
     * @author yanz
     * @date 2023/11/8 14:27
     */
    @Override
    public Boolean saveAsPending(PreInvoiceApplicationDTO applyDTO) throws Exception {
        // 申请时间前端锁死，做个样子，存入时间以服务器为准
        applyDTO.setDapplytime(new Date());
        LoginUser loginUser = WebUtil.getLoginUser();

        validateApplication(applyDTO, null);

        Twpreinvoiceapplication apply = copyPropertiesFromDto(applyDTO, loginUser);
        innerInterceptor.recoredLog();
        Boolean allSuccess = this.saveOrUpdate(apply);
        // 当前实现：apply中流程申请的applicationId字段留null，等流程发起时写入此字段 - 流程id是本表的applicationid，detail表和file表中很长的外键id是本表的id
        applyDTO.setId(apply.getId());
        // 文件上传
        for (Twpreinvoiceapplicationfile filesDTO : applyDTO.getApplyfiles()) {
            handleFileAdd(applyDTO, loginUser, filesDTO);
        }
        // 关联合同信息表
        Map<Long, BigDecimal> arrearagesSumMapByOrderIds =
                orderitemServiceI.getArrearagesSumMapByOrderIds(applyDTO.getOrderIds());
        if (ObjUtil.isNotEmpty(applyDTO.getOrderIds())) {
            List<Twpreinvoiceapplicationdetail> detailList = applyDTO.getOrderIds().stream().map(orderId -> {
                Twpreinvoiceapplicationdetail detail = new Twpreinvoiceapplicationdetail();
                detail.setPreinvoiceapplicationid(apply.getId());
                detail.setOrderid(orderId);
                detail.setNamountarrearage(arrearagesSumMapByOrderIds.get(orderId));
                return detail;
            }).collect(Collectors.toList());
            innerInterceptor.recoredLog();
            allSuccess = allSuccess && detailServiceI.saveBatch(detailList);

        }
        if (allSuccess) {
            return true;
        } else {
            throw new DataNotExistException("保存失败");
        }
    }


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
    @Override
    public BigDecimal getAvailableAmount(List<Long> orderIds, Boolean onlyCalculateApprovedOrders,
                                         BigDecimal currApply) {
        if (ObjUtil.isEmpty(orderIds)) {
            return BigDecimal.ZERO;
        }
        // 取得订单id对应的发票下所有的订单id：订单id - 申请id - 下所有订单id
        orderIds = orderIds.stream()
                .map(detailServiceI::getOrderIdsByOrderId)
                .flatMap(List::stream).distinct()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        // 欠款总额
        BigDecimal orderArrearagesSum = orderitemServiceI.getArrearagesSumByOrderIds(orderIds);
        BigDecimal invoiceArrearageSum = this.getInvoiceArrearageSumByOrderIds(orderIds, onlyCalculateApprovedOrders);

        // 可申请额度：将‘欠款额-已申请额度’的结果作为最终的欠款额更新到各个订单上，‘欠款额-已申请额度’为0或负数的订单从Map中去掉

        // 若onlyCalculateApprovedOrders为false，会计算所有 待审（未提交）+在审（已提交未批）+已通过（已批）的金额，这其中包含本次申请的额度
        // 暂时认为“可申请额度”截至本次提交前，计算时要去掉本次申请的数额 yanz 20240109
        if (!onlyCalculateApprovedOrders) {
            orderArrearagesSum = orderArrearagesSum.add(currApply);
        }
        return orderArrearagesSum.subtract(invoiceArrearageSum);
    }

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
    @Override
    public PreInvoiceApplicationVO getPreinvoiceapplicationVOByIdOrObj(Long preinvoiceapplicationid,
                                                                       Twpreinvoiceapplication apply) {
        if (ObjUtil.isNull(preinvoiceapplicationid) && ObjUtil.isNull(apply)) {
            throw new IllegalStateException("组装申请id和对象不能同时为空");
        }
        if (ObjUtil.isNull(apply)) {
            apply = this.getById(preinvoiceapplicationid);
        }
        return convertApplyToVO(apply);
    }

    private PreInvoiceApplicationVO convertApplyToVO(Twpreinvoiceapplication apply) {
        PreInvoiceApplicationVO vo = new PreInvoiceApplicationVO();
        BeanUtils.copyProperties(apply, vo);
        vo.setApplyfiles(fileServiceI.getFilesVOByApplicationId(vo.getId()));
        List<Long> orderIds = detailServiceI.getOrderIdsByApplicationId(apply.getId());
        if (ObjUtil.isNotEmpty(orderIds)) {
            List<Tworder> orders = orderServiceI.lambdaQuery()
                    .in(Tworder::getId, orderIds)
                    // 仅展示启用 & 未删除订单
                    .eq(Tworder::getBdelete, false)
                    .eq(Tworder::getBuse, true)
                    .list();
            vo.setContractVos(orderitemServiceI.getOrderforPreinvoapplyVOS(orders));
        }
        // 已收金额为空补一个0，方便看
        vo.setNamountreceived(apply.getNamountreceived() == null ? BigDecimal.ZERO : apply.getNamountreceived());
        //历史流程
        // TODO - 这部分功能重合，走通了再封 - 通了，等我想想着
        List<ProcessInstanceRecord> lsProcessRecord =
                processInstanceService.getHistoryProcessInstanceRecordByBussinessId("businessId",
                        apply.getId().toString());
        if (lsProcessRecord.size() > 0) {
            List<ProcessInstanceVo> lsProcessInstance = new ArrayList<>();
            for (ProcessInstanceRecord item : lsProcessRecord) {
                ProcessInstanceVo instanceVo = new ProcessInstanceVo();
                instanceVo.setProcessInstanceId(item.getProcessInstanceId());
                instanceVo.setFlowId(item.getFlowId());
                instanceVo.setCreateTime(item.getCreateTime());
                instanceVo.setResult(item.getResult());
                lsProcessInstance.add(instanceVo);
            }
            vo.setCustomerProcessInstance(lsProcessInstance);
        }
        return vo;
    }

    /**
     * 修改预开发票申请
     * 方法功能:修改预开发票申请
     *
     * @param applyDTO
     * @return java.lang.Boolean
     * @author yanz
     * @date 2023/11/13 9:20
     */
    @Override
    public Boolean updatePreinvoiceapplication(PreInvoiceApplicationDTO applyDTO) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Twpreinvoiceapplication pojo = copyPropertiesFromDto(applyDTO, user);
        validateApplication(applyDTO, null);
        // 申请合同明细 - 读旧数据、比对、插入新数据
        List<Long> newOrderIds = applyDTO.getOrderIds();
        List<Long> oriOrderIds = detailServiceI.getOrderIdsByApplicationId(applyDTO.getId());
        // 比对新旧合同，新增的插入，删除的删除
        if (ObjUtil.isNotEmpty(newOrderIds)) {
            // 新增的订单id
            List<Long> newOrderIdsToAdd = newOrderIds.stream()
                    .filter(newOrderId -> oriOrderIds.stream().noneMatch(oriOrderId -> oriOrderId.equals(newOrderId)))
                    .collect(Collectors.toList());

            // 待删除的订单id
            List<Long> oriOrderIdsToRemove = oriOrderIds.stream()
                    .filter(oriOrderId -> newOrderIds.stream().noneMatch(newOrderId -> newOrderId.equals(oriOrderId)))
                    .collect(Collectors.toList());

            // 批量新增
            List<Twpreinvoiceapplicationdetail> detailsToAdd = newOrderIdsToAdd.stream().map(newOrderId -> {
                Twpreinvoiceapplicationdetail detail = new Twpreinvoiceapplicationdetail();
                detail.setPreinvoiceapplicationid(applyDTO.getId());
                detail.setOrderid(newOrderId);
                detail.setNamountarrearage(orderitemServiceI.getArrearagesSumMapByOrderIds(Collections.singletonList(newOrderId)).get(newOrderId));
                return detail;
            }).collect(Collectors.toList());

            if (!detailsToAdd.isEmpty()) {
                innerInterceptor.recoredLog();
                detailServiceI.saveBatch(detailsToAdd);
            }

            // 批量删除
            if (!oriOrderIdsToRemove.isEmpty()) {
                innerInterceptor.recoredLog();
                detailServiceI.lambdaUpdate().in(Twpreinvoiceapplicationdetail::getOrderid, oriOrderIdsToRemove).remove();
            }
        } else if (ObjUtil.isNotEmpty(oriOrderIds)) {
            innerInterceptor.recoredLog();
            detailServiceI.lambdaUpdate().in(Twpreinvoiceapplicationdetail::getOrderid, oriOrderIds).remove();
        }

        List<Twpreinvoiceapplicationfile> oriFiles = fileServiceI.getFilesByApplicationId(applyDTO.getId());
        List<Twpreinvoiceapplicationfile> newFiles = applyDTO.getApplyfiles();
        // 比对新旧文件，新增的插入，删除的删除
        if (oriFiles.size() > 0) {
            oriFiles.forEach(oriFile -> {
                if (newFiles.stream().noneMatch(newFile -> newFile.getSfileid().equals(oriFile.getSfileid()))) {
                    innerInterceptor.recoredLog();
                    fileServiceI.removeById(oriFile.getId());
                }
            });
        }
        if (newFiles.size() > 0) {
            newFiles.forEach(newFile -> {
                if (oriFiles.stream().noneMatch(oriFile -> oriFile.getSfileid().equals(newFile.getSfileid()))) {
                    handleFileAdd(applyDTO, user, newFile);
                }
            });
        }

        innerInterceptor.recoredLog();
        if (!this.updateById(pojo)) {
            throw new DataNotExistException("更新数据失败");
        }
        return true;
    }

    /**
     * 提交申请
     * 方法功能:提交申请
     *
     * @param ids    英文逗号分隔
     * @param flowId
     * @return com.hgzp.core.page.Json
     * @author yanz
     * @date 2023/11/27 11:11
     */
    @Override
    public Json submitApply(String ids, String flowId) {
        if (StringUtils.isBlank(flowId)) {
            throw new IllegalStateException("流程id为空，申请失败");
        }
        // 根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程 - muyn 2024/2/17 08:49:29
        Tbflow flowTypeByKey = flowServiceI.getFlowTypeByKey(FlowTypes.FLOW_PREINVOICE.getKey());
        if (ObjUtil.isNull(flowTypeByKey)) {
            throw new IllegalStateException("申请失败，需要设置此审批流程");
        }
        List<String> idList = CollUtil.newArrayList(ids.split(","));
        Map<String, Object> flowParamMap = new HashMap<>();
        Twpreinvoiceapplication tmpApply;
        Json<String> started;
        for (String id : idList) {
            // businessId、businessName 必传，其中 businessId 默认当前业务表的 id 即可（预开申请就是预开申请的id，客户备案就是备案表的id），
            //  businessName 为业务单例的名称，这里放的预开申请时的开票名称，客户备案那写的是客户名称
            flowParamMap.put("businessId", id);
            tmpApply = this.getById(id);
            if (ObjUtil.isNull(tmpApply)) {
                throw new IllegalStateException("没有此申请开票单号，申请失败");
            }
            // 这里只按“审批通过”的额度计算，不含审批中的 - 就存在“发起新申请时，在审金额已达到/超过金额限制”的情况，尤其批量提交。
            // 暂未处理 - 要么提交审批时检验在审+已审金额，要么审批过程里按条件判断
            validateApplication(null, tmpApply);
            flowParamMap.put("businessName", tmpApply.getSprintname());
            // TODO 据实际流程条件补充 - 现在流程是随便写的
            flowParamMap.put("deptId", tmpApply.getDeptid());
            flowParamMap.put("businessentityid", tmpApply.getBusinessentityid());
            try {
                started = processInstanceService.startProcessInstanceByFlowTypes(flowId, flowParamMap,
                        FlowTypes.FLOW_PREINVOICE);
                if (!started.isSuccess()) {
                    throw new RuntimeException("批量操作中审批申请失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("审批申请失败" + e.getMessage());
            }
            started = null;
            flowParamMap.clear();
        }
//        更新申请状态
//        List<Twpreinvoiceapplication> list = this.lambdaQuery().in(Twpreinvoiceapplication::getApplicationid,
//        idList).list();
//        list.forEach(item -> {
//            item.setIapprovestatus(ApproveStatus.APPROVE_EDITING);
//        });
        return Json.success();

    }

    /**
     * 更新发票申请状态
     * 方法功能:更新发票申请状态，批量更新只能用在发起流程后的“更新‘审批状态’为在审”处
     *
     * @param ids
     * @param status
     * @param approveDesc
     * @return java.lang.Boolean
     * @author yanz
     * @date 2023/11/27 11:11
     */
    @Override
    public Boolean updateApplyStatus(String ids, Integer status, String approveDesc) {
        // 根据流程key获取一下tbflow，如果返回是null，则提示需要设置审批流程，如果返回的 tbflow的 bactive为0，则直接通过，不需要走审批流程，如果为1时，提交审批流程 - muyn 2024/2/17 08:49:29
        Tbflow flowTypeByKey = flowServiceI.getFlowTypeByKey(FlowTypes.FLOW_PREINVOICE.getKey());
        if (flowTypeByKey.getBactive() == false) {
            status = ApproveStatus.APPROVE_PASS.getKey();
        }
        try {
            List<String> idList = CollUtil.newArrayList(ids.split(","));
            List<Twpreinvoiceapplication> list = this.lambdaQuery()
                    .in(Twpreinvoiceapplication::getId, idList)
                    .list();
            Integer finalStatus = status;
            list.forEach(item -> {
                item.setIapprovestatus(finalStatus);
                if (StrUtil.isNotBlank(approveDesc)) {
                    item.setSapprovalopinions(approveDesc);
                }
            });
            innerInterceptor.recoredLog();
            boolean updated = this.updateBatchById(list);
            if (!updated) {
                throw new DataNotExistException("更新申请状态失败");
            }
        } catch (Exception e) {
            throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
        }
        return true;
    }

    /**
     * 按订单id取对应发票欠款额
     * 方法功能:按订单id取对应“发票欠款额”（申请额-已收额）
     *
     * @param orderIds
     * @param onlyCalculateApprovedOrders 仅计算“已通过审批”订单的欠款额
     * @return java.math.BigDecimal
     * @author yanz
     * @date 2023/12/4 13:30
     */
    @Override
    public BigDecimal getInvoiceArrearageSumByOrderIds(List<Long> orderIds, Boolean onlyCalculateApprovedOrders) {
        if (ObjUtil.isEmpty(orderIds)) {
            return null;
        }
        List<Long> applyids = orderIds.stream()
                .map(detailServiceI::getPreInvoiceApplicationIdsByOrderId).flatMap(List::stream).distinct()
                .collect(Collectors.toList());
        // 没有对应发票
        if (ObjUtil.isEmpty(applyids)) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum;
        LambdaQueryChainWrapper<Twpreinvoiceapplication> lqcw = this.lambdaQuery()
                .in(Twpreinvoiceapplication::getId, applyids);
        if (onlyCalculateApprovedOrders) {
            lqcw.eq(Twpreinvoiceapplication::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey());
        } else {
            lqcw.and(wrapper -> wrapper
                    .eq(Twpreinvoiceapplication::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                    .or()
                    .eq(Twpreinvoiceapplication::getIapprovestatus, ApproveStatus.APPROVE_EDIT.getKey())
                    .or()
                    .eq(Twpreinvoiceapplication::getIapprovestatus, ApproveStatus.APPROVE_EDITING.getKey())
            );
        }
        sum = lqcw
                .list()
                .stream()
                .map(apply -> apply.getNamountapply().subtract(apply.getNamountreceived() == null ? BigDecimal.ZERO :
                        apply.getNamountreceived()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum;
    }


    /**
     * 删除预开发票申请
     * 方法功能:删除预开发票申请
     *
     * @param ids 多个id英文逗号分隔
     * @return java.lang.Boolean
     * @throws Exception
     */
    @Override
    public Boolean deletePreinvoiceapplication(String ids) throws Exception {
        List<Long> idList =
                CollUtil.newArrayList(ids.split(",")).stream().map(Long::valueOf).collect(Collectors.toList());
        // 删关联合同记录
        innerInterceptor.recoredLog();
        if (!detailServiceI.deleteByApplyIds(idList)) {
            throw new DataNotExistException("删除关联数据失败");
        }
        // 删申请文件
        innerInterceptor.recoredLog();
        if (!fileServiceI.deleteByApplyIds(idList)) {
            throw new DataNotExistException("删除证明资料失败");
        }
        innerInterceptor.recoredLog();
        if (!this.removeByIds(idList)) {
            throw new DataNotExistException("删除预开申请失败");
        }
        //TODO 是否要删除对应的流程表数据？
        return true;
    }

    /**
     * 预开发票申请DTO转pojo
     * 方法功能:预开发票申请DTO转pojo
     *
     * @param dto
     * @param user
     * @return com.hgzp.core.model.Twpreinvoiceapplication
     * @author yanz
     * @date 2023/12/9 10:06
     */
    private Twpreinvoiceapplication copyPropertiesFromDto(PreInvoiceApplicationDTO dto, LoginUser user) {
        Twpreinvoiceapplication pojo = this.getById(dto.getId());
        Long version = 0L;
        if (ObjUtil.isNull(pojo)) {
            pojo = new Twpreinvoiceapplication();
        } else {
            version = pojo.getVersion();
        }
        BeanUtils.copyProperties(dto, pojo);
        // 操作员id、名称在dto中未包含
        pojo.setOperatorid(user.getUserid());
        pojo.setOperator(user.getUsername());
        Tbemploy empById = employServiceI.getById(pojo.getEmployid());
        pojo.setDeptid(empById.getDeptid());
        pojo.setDeptname(deptServiceI.getById(empById.getDeptid()).getSdeptname());
        pojo.setIapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
        pojo.setVersion(version);
        pojo.setNamountreceived(BigDecimal.ZERO);
        return pojo;
    }

    /**
     * 预开申请文件操作用这个
     * 方法功能:预开申请文件操作用这个
     *
     * @param dto
     * @param user
     * @param item
     * @return void
     * @author yanz
     * @date 2023/12/9 10:05
     */
    private void handleFileAdd(PreInvoiceApplicationDTO dto, LoginUser user, Twpreinvoiceapplicationfile item) {
        Twpreinvoiceapplicationfile file = new Twpreinvoiceapplicationfile();
        BeanUtils.copyProperties(item, file);
        file.setPreinvoiceapplicationid(dto.getId());
        file.setCreateempid(user.getUserid());
        file.setDcreatetime(dto.getDapplytime());
        // 文件分类(0-电子认刊书、 1-客户合同\协议、 2-资质 、3-名片、 4-出版流程单 、5-银行流水)
        // 预开申请 对应 2-资质
        file.setIfilecategory(SfileType.FileType_3.getKey());
        file.setBdelete(false);
        innerInterceptor.recoredLog();
        fileServiceI.saveOrUpdate(file);
        // 检查Twresource表，没记录加记录
        handleTwresourcesOperations(file, user);
    }

    private void handleTwresourcesOperations(Twpreinvoiceapplicationfile file, LoginUser user) {
        LambdaQueryWrapper<Twresources> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(file.getSfileid() != null, Twresources::getSfileid, file.getSfileid());
        if (sourceServiceI.count(queryWrapper) > 0) {
            return;
        }
        Twresources source = new Twresources();
        BeanUtils.copyProperties(file, source);
        source.setEmployid(user.getUserid());
        innerInterceptor.recoredLog();
        sourceServiceI.saveOrUpdate(source);
    }

    /**
     * 检查申请条件
     * 方法功能:1、挂开必须关联合同；2、预开不得超过实际金额与已开金额之差；3、预开不得超过选中订单的欠款总额；4、项目相关的申请总额不能超过项目预算金额
     *
     * @param applyDTO
     * @param apply    非空则只处理此对象 - 检查提交审批用
     * @return void
     * @author yanz
     * @date 2023/12/9 10:02
     */
    private void validateApplication(PreInvoiceApplicationDTO applyDTO, Twpreinvoiceapplication apply) {
        // 默认仅计算“已通过审批”订单的欠款额，审批时除外
        Boolean onlyCalculateApprovedOrders = true;
        if (ObjUtil.isNull(applyDTO) && ObjUtil.isNotNull(apply)) {
            onlyCalculateApprovedOrders = false;
        }
        if (ObjUtil.isNotNull(apply)) {
            applyDTO = new PreInvoiceApplicationDTO();
            PreInvoiceApplicationVO vo = getPreinvoiceapplicationVOByIdOrObj(null, apply);
            BeanUtils.copyProperties(vo, applyDTO);
        }
        // 关联合同为null认为没有合同，校验开票类型是否为"挂开"
        if (ObjUtil.isEmpty(applyDTO.getContractVos()) && applyDTO.getIapplytype() != InvoiceApplyType.PENDING_INVOICE.getKey()) {
            throw new IllegalStateException("开票名： " + applyDTO.getSprintname() + "；申请金额： " + applyDTO.getNamountapply() + "； 没有关联合同，开票类型必须为挂开");
        }
        // 挂开不限制金额
        // 预开不得超过实际金额与已开金额之差
        Map<Long, BigDecimal> arrearagesSumMapByOrderIds =
                orderitemServiceI.getArrearagesSumMapByOrderIds(applyDTO.getOrderIds());
        BigDecimal availableAmount = getAvailableAmount(applyDTO.getOrderIds(), onlyCalculateApprovedOrders,
                applyDTO.getNamountapply());

        if (applyDTO.getIapplytype() == InvoiceApplyType.PRE_INVOICE.getKey()) {
            //  注：多个合同id
            if (NumberUtil.isLess(availableAmount, applyDTO.getNamountapply())) {
                throw new IllegalStateException("申请金额超出欠款额");
            }
            if (NumberUtil.isGreater(
                    applyDTO.getNamountapply(),
                    arrearagesSumMapByOrderIds
                            .values().stream()
                            .reduce(BigDecimal.ZERO, BigDecimal::add
                            )
            )) {
                throw new IllegalStateException("申请金额超过选中订单的欠款总额");
            }
        }

        // 广告项目相关的申请总额不能超过项目预算金额
        if (ObjUtil.isNotNull(applyDTO.getAdprojectid())) {
            // 项目id
            Long projectId = applyDTO.getAdprojectid();
            // 项目预算金额
            BigDecimal projectBudget = adprojectServiceI.getAdProjectBudget(projectId);

            List<Twpreinvoiceapplication> applications = this.lambdaQuery()
                    .eq(Twpreinvoiceapplication::getAdprojectid, projectId)
                    .in(Twpreinvoiceapplication::getIapprovestatus, Arrays.asList(
                            ApproveStatus.APPROVE_PASS.getKey(),
                            ApproveStatus.APPROVE_EDIT.getKey(),
                            ApproveStatus.APPROVE_EDITING.getKey()))
                    .list();

            // 项目相关的已申请总额
            AtomicReference<BigDecimal> projectApplySum = new AtomicReference<>(applications.stream()
                    .map(Twpreinvoiceapplication::getNamountapply)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .add(applyDTO.getNamountapply()));

            Optional.ofNullable(applyDTO.getId())
                    .map(id -> applications.stream()
                            .filter(app -> Objects.equals(app.getId(), id))
                            .findFirst()
                            .orElse(null))
                    .ifPresent(curRecord -> projectApplySum.set(projectApplySum.get().subtract(curRecord.getNamountapply())));

            if (NumberUtil.isLessOrEqual(BigDecimal.ZERO, projectApplySum.get())
                    && NumberUtil.isGreater(projectApplySum.get(), projectBudget)) {
                throw new IllegalStateException("开票名： " + applyDTO.getSprintname() + "；申请金额： " + applyDTO.getNamountapply() + "元，合并已申请总额超过项目预算金额");
            }
        } else {
            if (applyDTO.getContractVos().isEmpty()) {
                throw new IllegalStateException("不选择合同时，应指定广告项目");
            }
        }
    }

    /**
     * 查询有欠款的预开发票列表，同时判断是否关联合同订单，如果关联则查询出合同欠款情况信息
     * 方法功能:  预开发票（twpreinvoiceapplication表中namountapply-namountreceived>0且状态iapprovestatus为2-已通过的，且已开具正式发票的）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO>
     * @author suny
     * @date 2023/12/12 16:21
     */
    @Override
    public IPage<PreInvoiceApplicationDTO> getDebtInvoiceApplicationPageList(Page<Twpreinvoiceapplication> page,
                                                                             BaseQueryInfo query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        LambdaQueryWrapper<Twpreinvoiceapplication> lqw = Wrappers.lambdaQuery();
        lqw.eq(Twpreinvoiceapplication::getEmployid, user.getUserid());
        lqw.ge(ObjUtil.isNotEmpty(query.getStartTime()), Twpreinvoiceapplication::getDapplytime, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twpreinvoiceapplication::getDapplytime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        lqw.and(StrUtil.isNotBlank(query.getQueryKey()), i -> i.like(Twpreinvoiceapplication::getBusinessentityname,
                        query.getQueryKey())
                .or()
                .like(Twpreinvoiceapplication::getInvoice, query.getQueryKey())
                .or()
                .inSql(Twpreinvoiceapplication::getId, "select preinvoiceapplicationid from twpreinvoiceapplicationdetail where orderid in (select id from tworder where scontractnum like '%" + query.getQueryKey() + "%'")
                .or()
                .like(Twpreinvoiceapplication::getCustomername, query.getQueryKey())
        );
        // 申请金额大于已还金额
        lqw.and(i -> i.isNull(Twpreinvoiceapplication::getNamountreceived)
                .or()
                .apply("namountapply > namountreceived"));
        // 已通过
        lqw.eq(Twpreinvoiceapplication::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey());
        // 已开正式发票（发票表twinvoice中invoicecode不能为空）
        lqw.isNotNull(Twpreinvoiceapplication::getInvoicecode);
//        lqw.apply("namountapply > namountreceived");
        Page<Twpreinvoiceapplication> paged = this.page(page, lqw);
        Page<PreInvoiceApplicationDTO> DtoPage = new Page<>();
        if (paged.getTotal() == 0) {
            return DtoPage;
        }
        List<PreInvoiceApplicationDTO> result = new ArrayList<>();
        paged.getRecords().forEach(item -> {
            // TODO 当前发票已分配金额总数
            BigDecimal invoiceAllocate = twcustomerchargeServiceI.getCustomerChargeByInvoiceId(item.getId(), null);
            // TODO 当前发票金额是否已经结账（包括所以分配状态），如果已经分配则不显示
            if (invoiceAllocate != null && item.getNamountapply().compareTo(invoiceAllocate) <= 0) {
                return;
            }
            PreInvoiceApplicationDTO applicationDTO = new PreInvoiceApplicationDTO();
            BeanUtils.copyProperties(item, applicationDTO);
            if (item.getIapplytype() == InvoiceApplyType.PRE_INVOICE.getKey()) {
                // TODO 预开申请，查询合同欠款情况
                List<Long> orderids = detailServiceI.getOrderIdsByApplicationId(item.getId());
                List<OrderDebtDTO> orderList = new ArrayList<>();
                orderids.forEach(orderid -> {
                    OrderDebtDTO orders = null;
                    try {
                        // TODO 从订单表中获取订单欠款情况
                        orders = tworderitemServiceI.getOrderDebtInfo(orderid.toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    if (orders != null) {
                        orderList.add(orders);
                    }
                });
                applicationDTO.setOrders(orderList);
            }
            result.add(applicationDTO);
        });

        DtoPage.setRecords(result);
        DtoPage.setTotal(result.size());
        return DtoPage;
    }

    /**
     * 获取预开申请信息（发票打印用）
     * 方法功能:获取预开申请信息（发票打印用），区别于发票申请处的列表查询。限制条件： “审批通过 且 未打印”
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO>
     * @author yanz
     * @date 2023/12/19 9:51
     */
    @Override
    public IPage<PreInvoiceApplicationDTO> getPreInvoiceApplicationPageList(Page<Twpreinvoiceapplication> page,
                                                                            BaseQueryInfo query) {
        LambdaQueryWrapper<Twpreinvoiceapplication> lqw = Wrappers.lambdaQuery();
        // 日期限制
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Twpreinvoiceapplication::getDapplytime, query.getStartTime());
        if (ObjUtil.isNotNull(query.getEndTime())) {
            lqw.lt(Twpreinvoiceapplication::getDapplytime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        // 限制条件： “审批通过 且 未打印”
        lqw.eq(Twpreinvoiceapplication::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey());
        // 	未打印：没发 票号，发票号为null或空白字符串 - 应该不用判空串的，但历史记录里有空串
        lqw.and(i -> i.isNull(Twpreinvoiceapplication::getInvoice)
                .or()
                .eq(Twpreinvoiceapplication::getInvoice, "")
        );


        // POJO转DTO
        Page<Twpreinvoiceapplication> paged = this.page(page, lqw);
        Page<PreInvoiceApplicationDTO> dtoPage = new Page<>();
        if (paged.getTotal() == 0) {
            return dtoPage;
        }
        List<PreInvoiceApplicationDTO> result = paged.getRecords().stream()
                .map(item -> {
                    PreInvoiceApplicationDTO dto = new PreInvoiceApplicationDTO();
                    BeanUtils.copyProperties(item, dto);
                    dto.setItype(item.getIapplytype());
                    return dto;
                }).collect(Collectors.toList());

        dtoPage.setRecords(result);
        dtoPage.setTotal(result.size());
        return dtoPage;
    }

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
    @Override
    public void rejectInvoice(Long preinvoiceapplicationId, String rejectReason) {
        Twpreinvoiceapplication twpreinvoiceapplication = this.getById(preinvoiceapplicationId);
        twpreinvoiceapplication.setSapprovalopinions(rejectReason);
        twpreinvoiceapplication.setIapprovestatus(ApproveStatus.APPROVE_REVOKE.getKey());
        innerInterceptor.recoredLog();
        if (!this.updateById(twpreinvoiceapplication)) {
            throw new RuntimeException("退回预开申请失败");
        }
    }

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
    @Override
    public PreInvoiceApplicationDTO getPreInvoiceApplyDtoById(Long preinvoiceapplicationId, Long orderId) {
        Twpreinvoiceapplication twpreinvoiceapplication = this.getById(preinvoiceapplicationId);
        PreInvoiceApplicationDTO preInvoiceApplicationDTO = new PreInvoiceApplicationDTO();
        BeanUtils.copyProperties(twpreinvoiceapplication, preInvoiceApplicationDTO);
        List<Long> orderIds = new ArrayList<>();
        if (ObjUtil.isNotNull(orderId)) {
            orderIds.add(orderId);
        } else {
            orderIds = detailServiceI.getOrderIdsByApplicationId(preinvoiceapplicationId);
        }
        if(orderIds.size() == 0){
            return preInvoiceApplicationDTO;
        }

        // 20240307 suny 参数表中 bchargeonlypublished =1时，做核销时只能核销 已发布的广告明细数据
        boolean bchargeonlypublished = false;
        Twparameter twparameter = twparameterServiceI.getOne(Wrappers.lambdaQuery(Twparameter.class)
                .eq(Twparameter::getSkey, "bchargeonlypublished "));
        if (twparameter != null && twparameter.getSvalue().equals("1")) {
            bchargeonlypublished = true;
        }

        List<Tworderitem> orderItems = orderitemServiceI.lambdaQuery()
                .in(orderIds.size() > 0 ,Tworderitem::getOrderid, orderIds)
                .ne(Tworderitem::getAmountarrearage, BigDecimal.ZERO)
                .eq(bchargeonlypublished, Tworderitem::getIpublishstatus, PublishStatus.PUBLISH_Published.getKey()) // 20240307 suny 参数表中 bchargeonlypublished =1时，做核销时只能核销 已发布的广告明细数据
                .orderByAsc(Tworderitem::getCreatetime)  // suny 按照时间正序查询
                .orderByAsc(Tworderitem::getId)
                .list();
        preInvoiceApplicationDTO.setOrderItems(orderItems);
        return preInvoiceApplicationDTO;
    }

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
    @Override
    public Boolean updateReceivedAmount(Long preinvoiceapplicationId, BigDecimal amountReceived) {
        Twpreinvoiceapplication twpreinvoiceapplication = this.getById(preinvoiceapplicationId);
        twpreinvoiceapplication.setNamountreceived(twpreinvoiceapplication.getNamountreceived().add(amountReceived));
        innerInterceptor.recoredLog();
        if (!this.updateById(twpreinvoiceapplication)) {
            throw new DataNotExistException("更新已收金额失败");
        }
        return true;
    }

    @Override
    public void updateInvoiceApplicationCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception {
        try {
            String[] splitids = customerIds.split(",");
            List<Twpreinvoiceapplication> orders = this.lambdaQuery()
                    .in(Twpreinvoiceapplication::getCustomerid, splitids)
                    .list();
            if (orders.size() > 0) {

                orders.forEach(order -> {
                    order.setCustomerid(newcustomerId);
                    order.setCustomername(newcustomername);
                });

                innerInterceptor.recoredLog();
                this.updateBatchById(orders);
            }
        } catch (Exception e0) {
            throw new RuntimeException("预开发票合并失败！" + e0.getMessage());
        }
    }
}
