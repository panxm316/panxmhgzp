package com.hgzp.advertising.service.ad.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.AppStatus;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.approve.ApproveType;
import com.hgzp.advertising.emnus.customer.EcustomerChargeType;
import com.hgzp.advertising.emnus.schedule.PublishStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.dto.*;
import com.hgzp.advertising.pagemodel.ad.vo.*;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.dto.OrderContractDetailDTO;
import com.hgzp.advertising.pagemodel.business.dto.OrderItemCostDTO;
import com.hgzp.advertising.pagemodel.business.dto.PreInvoiceApplicationDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.customer.vo.ProcessInstanceVo;
import com.hgzp.advertising.pagemodel.finance.dto.CustomerChargeBankDTO;
import com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO;
import com.hgzp.advertising.pagemodel.flow.TwapplicationrelationsVO;
import com.hgzp.advertising.pagemodel.flow.dto.TwapplicationrelationsDTO;
import com.hgzp.advertising.pagemodel.system.dto.EmpAuthorityDTO;
import com.hgzp.advertising.service.ad.*;
import com.hgzp.advertising.service.finance.TworderitemcostServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationdetailServiceI;
import com.hgzp.advertising.service.customer.TwcustomerServiceI;
import com.hgzp.advertising.service.finance.InvoiceServiceI;
import com.hgzp.advertising.service.finance.OrderApportiondetailServiceI;
import com.hgzp.advertising.service.finance.TwcustomerchargeServiceI;
import com.hgzp.advertising.service.flow.IProcessInstanceService;
import com.hgzp.advertising.service.flow.TbflowconditionServiceI;
import com.hgzp.advertising.service.flow.TwapplicationrelationsServiceI;
import com.hgzp.advertising.service.media.TbadspecServiceI;
import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.advertising.service.media.TbmediapublicnumServiceI;
import com.hgzp.advertising.service.schedule.OrderitemarrangeServiceI;
import com.hgzp.advertising.service.schedule.TbfoldpageplanServiceI;
import com.hgzp.advertising.service.system.ProduceServiceI;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.advertising.service.system.TbemployServiceI;
import com.hgzp.advertising.utils.DataUtil;
import com.hgzp.advertising.utils.PageUtils;
import com.hgzp.common.flowable.constants.FormTypeEnum;
import com.hgzp.common.flowable.dto.ProcessInstanceParamDto;
import com.hgzp.core.constant.SecurityConstants;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.ad.TworderMapper;
import com.hgzp.pagemodel.api.CJAdPrinDTO;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hgzp.advertising.emnus.approve.ApproveStatus.*;
import static com.hgzp.core.emnus.FlowTypes.FLOW_ADSOURCE;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TworderServiceImpl extends MyServiceImpl<TworderMapper, Tworder> implements TworderServiceI {
    @Autowired
    TworderMapper orderMapper;
    @Autowired
    TworderitemServiceI orderitemService;
    @Autowired
    TworderitembelongServiceI orderitembelongServiceI;
    @Autowired
    private ProduceServiceI produceServiceI;
    @Autowired
    private TwadresourceadorderServiceI adresourceadorderServiceI;
    @Resource
    private IProcessInstanceService processInstanceService;
    @Autowired
    private TbflowconditionServiceI flowconditionServiceI;
    @Autowired
    private TbfoldpageplanServiceI foldpageplanServiceI;
    @Autowired
    private TwapplicationrelationsServiceI applicationrelationsServiceI;
    @Autowired
    private TbdeptServiceI deptServiceI;
    @Autowired
    private TbmediaServiceI mediaServiceI;
    @Autowired
    private TwcustomerServiceI customerServiceI;
    @Autowired
    private TbmediapublicnumServiceI mediapublicnumServiceI;
    @Autowired
    private OrderitemarrangeServiceI orderitemarrangeServiceI;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private TwpreinvoiceapplicationdetailServiceI preinvoiceapplicationdetailServiceI;
    @Autowired
    private TwpreinvoiceapplicationServiceI preinvoiceapplicationServiceI;
    @Autowired
    private OrderApportiondetailServiceI orderApportiondetailServiceI;
    @Autowired
    private TworderitemcostServiceI orderitemcostServiceI;
    @Autowired
    private TwcustomerchargeServiceI customerchargeServiceI;
    @Autowired
    private TwadresourceapplicationServiceI adresourceapplicationServiceI;
    @Autowired
    private InvoiceServiceI invoiceServiceI;
    @Autowired
    private TbemployServiceI employServiceI;
    @Autowired
    private TbadspecServiceI tbadspecServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<OrderDTO> getOrderPageList(IPage<Tworder> page, OrderReq query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        HttpServletRequest request = WebUtil.getHttpServletRequest();
        List<Long> lsEmployIds = new ArrayList<>();
        // 如果是领导 查询负责的部门人员，如果结果为0，则是全部
        EmpAuthorityDTO empAuthorityDTO = (EmpAuthorityDTO) request.getAttribute(SecurityConstants.ROLE_PERMISSION);
        if (user.getBlead()) {
            // 如果list中size等于零则是全部，不用控制;否则查权限范围内的部门
            if (empAuthorityDTO.getDeptIdList().size() > 0) {
                List<Long> deptlist = empAuthorityDTO.getDeptIdList();
                //获取人员
                lsEmployIds = employServiceI.getEmpIdsByDeptIds(deptlist);
                //添加自己
                if (!lsEmployIds.contains(user.getUserid())) {
                    lsEmployIds.add(user.getUserid());
                }
            }
        } else {
            lsEmployIds = empAuthorityDTO.getEmpIdList();
        }

        //根据资源Id获取对应的订单编号
        List<Long> lsOrderIds = new ArrayList<>();
        if (ObjectUtil.isNotNull(query.getAdresourceapplicationid())) {
            lsOrderIds =
                    adresourceadorderServiceI.getOrderIdByresourceIds(query.getAdresourceapplicationid().toString());
            if (lsOrderIds.size() == 0) {
                return new Page<OrderDTO>();
            }
        }
        Date EndTime = query.getEndTime() != null ? DateUtil.offsetDay(query.getEndTime(), 1) : new Date();
        LambdaQueryWrapper<Tworder> lqw = Wrappers.lambdaQuery();
        //业务员
        lqw.in(lsEmployIds.size() > 0, Tworder::getCreateempid, lsEmployIds);

        //多个关键字以空格间隔  广告标题，内容，订单编号，合同号，项目名称，客户名称，经营主体 ，业务员
        if (ObjectUtil.isNotNull(query.getQueryKey())) {
            String[] keywords = query.getQueryKey().trim().split(" ");
            lqw.and(item -> {
                for (String skey : keywords) {
                    item.or(StrUtil.isNotBlank(skey), i -> i.like(Tworder::getSadtitle, skey)
                            .or().like(Tworder::getSadcontent, skey)
                            .or().like(Tworder::getSordernum, skey)
                            .or().like(Tworder::getScontractnum, skey)
                            .or().like(Tworder::getAdprojectname, skey)
                            .or().like(Tworder::getCustomername, skey)
                            .or().like(Tworder::getBusinessentityname, skey)
                            .or().like(Tworder::getEmployname, skey)
                    );
                }
            });
        }
        if (ObjectUtil.isNotNull(query.getAdresourceapplicationid())) {
            lqw.in(Tworder::getId, lsOrderIds);
        }
        lqw.eq(Tworder::getBdelete, false)
                .and(i -> i.eq(Tworder::getIbooktype, 0).or().eq(Tworder::getIbooktype, 3));
        lqw.ge(ObjectUtil.isNotNull(query.getStartTime()), Tworder::getCreatetime, query.getStartTime())
                .le(ObjectUtil.isNotNull(query.getEndTime()), Tworder::getCreatetime, EndTime);
        //负责人根据部门权限查看且不能查看待审的数据
        lqw.ne(!query.getBshowedit(), Tworder::getIdiscountapprovestatus, APPROVE_EDIT.getKey());

        IPage<Tworder> lsResult = orderMapper.selectPage(page, lqw);
        Page<OrderDTO> reslutPage = new Page<OrderDTO>();
        if (lsResult.getTotal() == 0) {
            return reslutPage;
        }
        List<OrderDTO> lsData = new ArrayList<>();
        for (Tworder record : lsResult.getRecords()) {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(record, orderDTO);
            //资源Id
            List<Long> lsRresourceIds = adresourceadorderServiceI.getResourceaIdbyOrderId(record.getId().toString());
            orderDTO.setAdresourceapplicationid(lsRresourceIds);
            //客户Vip
            if (ObjectUtil.isNotNull(record.getCustomerid())) {
                Twcustomer twcustomer = customerServiceI.getById(record.getCustomerid());
                if (ObjectUtil.isNotNull(twcustomer)) {
                    orderDTO.setBcustomeVip(twcustomer.getBvip());
                }
            }
            //代理公司Vip
            if (ObjectUtil.isNotNull(record.getAgencytid())) {
                Twcustomer twcustomer = customerServiceI.getById(record.getAgencytid());
                if (ObjectUtil.isNotNull(twcustomer)) {
                    orderDTO.setBagencyVip(twcustomer.getBvip());
                }
            }
            //内容生产方Vip
            if (ObjectUtil.isNotNull(record.getAgentid())) {
                Twcustomer twcustomer = customerServiceI.getById(record.getAgentid());
                if (ObjectUtil.isNotNull(twcustomer)) {
                    orderDTO.setBagentVip(twcustomer.getBvip());
                }
            }
            //订单明细
            List<Tworderitem> orderitem = orderitemService.getOrderItemByOrderId(record.getId().toString());
            if (orderitem.size() > 0) {
                List<OrderItemVO> orderItemVOList = new ArrayList<>();
                for (Tworderitem item : orderitem) {
                    OrderItemVO orderItemVO = new OrderItemVO();
                    BeanUtils.copyProperties(item, orderItemVO);
                    List<Tworderitembelong> orderitembelong =
                            orderitembelongServiceI.getOrderItemBelongByOrderItemId(item.getId().toString());
                    orderItemVO.setOrderitembelong(orderitembelong);
                    orderItemVOList.add(orderItemVO);
                }
                orderDTO.setOrderitem(orderItemVOList);
            }
            //历史流程
            List<ProcessInstanceRecord> lsProcessRecord =
                    processInstanceService.getHistoryProcessInstanceRecordByBussinessId("businessId",
                            record.getId().toString());
            if (lsProcessRecord.size() > 0) {
                //订单审批流程历史Id
                List<ProcessInstanceRecord> lsOrderProcessRecord = lsProcessRecord.stream()
                        .filter(item -> item.getGroupId().equals(FlowTypes.FLOW_ORDER.getKey()) || item.getGroupId().equals(FlowTypes.FLOW_ORDERCHANGE.getKey()))
                        .sorted(Comparator.comparing(ProcessInstanceRecord::getCreateTime).reversed())
                        .collect(Collectors.toList());
                if (lsOrderProcessRecord.size() > 0) {
                    List<ProcessInstanceVo> lsProcessInstance = new ArrayList<>();
                    for (ProcessInstanceRecord item : lsOrderProcessRecord) {
                        ProcessInstanceVo instanceVo = new ProcessInstanceVo();
                        instanceVo.setProcessInstanceId(item.getProcessInstanceId());
                        instanceVo.setFlowId(item.getFlowId());
                        instanceVo.setCreateTime(item.getCreateTime());
                        instanceVo.setResult(item.getResult());
                        lsProcessInstance.add(instanceVo);
                    }
                    orderDTO.setOrderProcessInstanceid(lsProcessInstance);
                }
                //折扣审批流程历史Id
                List<ProcessInstanceRecord> lsdiscountProcessRecord = lsProcessRecord.stream()
                        .filter(item -> item.getGroupId().equals(FlowTypes.FLOW_CONTRACT.getKey()))
                        .sorted(Comparator.comparing(ProcessInstanceRecord::getCreateTime).reversed())
                        .collect(Collectors.toList());

                if (lsdiscountProcessRecord.size() > 0) {
                    List<ProcessInstanceVo> lsProcessInstance = new ArrayList<>();
                    for (ProcessInstanceRecord item : lsdiscountProcessRecord) {
                        ProcessInstanceVo instanceVo = new ProcessInstanceVo();
                        instanceVo.setProcessInstanceId(item.getProcessInstanceId());
                        instanceVo.setFlowId(item.getFlowId());
                        instanceVo.setCreateTime(item.getCreateTime());
                        instanceVo.setResult(item.getResult());
                        lsProcessInstance.add(instanceVo);
                    }
                    orderDTO.setDiscountProcessInstanceid(lsProcessInstance);
                }
            }

            lsData.add(orderDTO);
        }
        reslutPage.setRecords(lsData);
        reslutPage.setTotal(lsResult.getTotal());
        return reslutPage;
    }

    @Override
    public OrderDTO getOrderInfo(String id) throws Exception {
        Tworder order = orderMapper.selectById(id);
        OrderDTO orderDTO = new OrderDTO();
        if (ObjectUtil.isNotNull(order)) {
            BeanUtils.copyProperties(order, orderDTO);
            //资源Id
            List<Long> lsRresourceIds = adresourceadorderServiceI.getResourceaIdbyOrderId(id);
            orderDTO.setAdresourceapplicationid(lsRresourceIds);

            List<Tworderitem> orderitem = orderitemService.getOrderItemByOrderId(id);

            if (orderitem.size() > 0) {
                List<OrderItemVO> orderItemVOList = new ArrayList<>();
                for (Tworderitem item : orderitem) {
                    OrderItemVO orderItemVO = new OrderItemVO();
                    BeanUtils.copyProperties(item, orderItemVO);
                    List<Tworderitembelong> orderitembelong =
                            orderitembelongServiceI.getOrderItemBelongByOrderItemId(item.getId().toString());
                    orderItemVO.setOrderitembelong(orderitembelong);
                    orderItemVOList.add(orderItemVO);
                }
                orderDTO.setOrderitem(orderItemVOList);
            }

            //历史流程
            List<ProcessInstanceRecord> lsProcessRecord =
                    processInstanceService.getHistoryProcessInstanceRecordByBussinessId("businessId",
                            order.getId().toString());
            if (lsProcessRecord.size() > 0) {
                //订单审批流程历史Id
                List<ProcessInstanceRecord> lsOrderProcessRecord = lsProcessRecord.stream()
                        .filter(item -> item.getGroupId().equals(FlowTypes.FLOW_ORDER.getKey()) || item.getGroupId().equals(FlowTypes.FLOW_ORDERCHANGE.getKey()))
                        .sorted(Comparator.comparing(ProcessInstanceRecord::getCreateTime).reversed())
                        .collect(Collectors.toList());
                if (lsOrderProcessRecord.size() > 0) {
                    List<ProcessInstanceVo> lsProcessInstance = new ArrayList<>();
                    for (ProcessInstanceRecord item : lsOrderProcessRecord) {
                        ProcessInstanceVo instanceVo = new ProcessInstanceVo();
                        instanceVo.setProcessInstanceId(item.getProcessInstanceId());
                        instanceVo.setFlowId(item.getFlowId());
                        instanceVo.setCreateTime(item.getCreateTime());
                        instanceVo.setResult(item.getResult());
                        lsProcessInstance.add(instanceVo);
                    }
                    orderDTO.setOrderProcessInstanceid(lsProcessInstance);
                }
                //折扣审批流程历史Id
                List<ProcessInstanceRecord> lsdiscountProcessRecord = lsProcessRecord.stream()
                        .filter(item -> item.getGroupId().equals(FlowTypes.FLOW_CONTRACT.getKey()))
                        .sorted(Comparator.comparing(ProcessInstanceRecord::getCreateTime).reversed())
                        .collect(Collectors.toList());

                if (lsdiscountProcessRecord.size() > 0) {
                    List<ProcessInstanceVo> lsProcessInstance = new ArrayList<>();
                    for (ProcessInstanceRecord item : lsdiscountProcessRecord) {
                        ProcessInstanceVo instanceVo = new ProcessInstanceVo();
                        instanceVo.setProcessInstanceId(item.getProcessInstanceId());
                        instanceVo.setFlowId(item.getFlowId());
                        instanceVo.setCreateTime(item.getCreateTime());
                        instanceVo.setResult(item.getResult());
                        lsProcessInstance.add(instanceVo);
                    }
                    orderDTO.setDiscountProcessInstanceid(lsProcessInstance);
                }
            }

        }
        return orderDTO;
    }

    @Override
    public Json saveOrder(OrderDTO orderDTO) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        String serNoIc = produceServiceI.getSerNo();
        String orderNoIc = produceServiceI.getOrderNo();

        Integer idefaultApproveStatus = APPROVE_EDIT.getKey();
        Tworder tworder = new Tworder();
        BeanUtils.copyProperties(orderDTO, tworder);
        tworder.setScontractnum(serNoIc);
        tworder.setSordernum(orderNoIc);
        tworder.setIapprovestatus(idefaultApproveStatus);
        tworder.setIaddapprovestatus(idefaultApproveStatus);
        tworder.setIchangeapprovestatus(idefaultApproveStatus);
        tworder.setIdiscountapprovestatus(idefaultApproveStatus); //折扣审批状态
        tworder.setIpreapprovestatus(idefaultApproveStatus);
        tworder.setIstopapprovestatus(idefaultApproveStatus);
        tworder.setCreateempid(user.getUserid());
        tworder.setCreateempname(user.getUsername());
        tworder.setDeptid(user.getDeptid());
        tworder.setDeptname(user.getDeptname());
        tworder.setCreatetime(new Date());
        innerInterceptor.recoredLog();
        save(tworder);

        //添加明细信息
        if (orderDTO.getOrderitem().size() > 0) {
            for (OrderItemVO item : orderDTO.getOrderitem()) {
                //判断刊期是否存在
                if (ObjectUtil.isNotNull(item.getPrestartdate()) && "paper".equals(item.getMediatypekey())) {
                    String sPublishNo = mediapublicnumServiceI.getMediaPublishNO(item.getMediaid(),
                            item.getPrestartdate());
                    if (StrUtil.isBlank(sPublishNo)) {
                        continue;
                    }
                }
                Tworderitem orderitem = new Tworderitem();
                BeanUtils.copyProperties(item, orderitem);
                Long orderItemId = IdUtil.getSnowflakeNextId();
                orderitem.setId(orderItemId);
                orderitem.setOrderid(tworder.getId());
                orderitem.setScontractnum(serNoIc);
                orderitem.setSordernum(orderNoIc);
                orderitem.setCreateempid(user.getUserid());
                orderitem.setCreateempname(user.getUsername());
                orderitem.setCreatetime(new Date());
                orderitem.setIapprovestatus(idefaultApproveStatus);
                orderitem.setIaddapprovestatus(idefaultApproveStatus);
                orderitem.setIchangeapprovestatus(idefaultApproveStatus);
                orderitem.setIdiscountapprovestatus(idefaultApproveStatus);
                orderitem.setIstopapprovestatus(idefaultApproveStatus);

                orderitem.setIpublishstatus(PublishStatus.PUBLISH_Reserve.getKey());

                //媒体类型
                Tbmedia tbmedia = mediaServiceI.getById(item.getMediaid());
                if (tbmedia != null) {
                    orderitem.setMediatypekey(tbmedia.getMediatypekey());
                    orderitem.setMediatypename(tbmedia.getMediatypename());
                }

                innerInterceptor.recoredLog();
                orderitemService.save(orderitem);
                //添加明细归属信息
                if (item.getOrderitembelong().size() > 0) {
                    for (Tworderitembelong belong : item.getOrderitembelong()) {
                        belong.setId(IdUtil.getSnowflakeNextId());
                        belong.setScontractnum(serNoIc);
                        belong.setSordernum(orderNoIc);
                        belong.setOrderitemid(orderItemId);
                        belong.setCreateempid(user.getUserid());
                        belong.setCreateempname(user.getUsername());
                        belong.setCreatetime(new Date());
                        if (ObjectUtil.isNull(belong.getDeptname())) {
                            Tbdept tbdept = deptServiceI.getById(belong.getDeptid());
                            if (tbdept != null) {
                                belong.setDeptname(tbdept.getSdeptname());
                            }
                        }
                    }
                    innerInterceptor.recoredLog();
                    orderitembelongServiceI.saveBatch(item.getOrderitembelong());
                }
            }
        }
        //添加资源与订单的关联
        if (orderDTO.getAdresourceapplicationid().size() > 0) {
            for (Long resourceId : orderDTO.getAdresourceapplicationid()) {
                Twadresourceadorder twadresourceadorder = new Twadresourceadorder();
                twadresourceadorder.setId(IdUtil.getSnowflakeNextId());
                twadresourceadorder.setAdresourceapplicationid(resourceId);
                twadresourceadorder.setAdorderid(orderDTO.getId());
                twadresourceadorder.setCreateempid(user.getUserid());
                twadresourceadorder.setCreateempname(user.getUsername());
                twadresourceadorder.setDcreatetime(new Date());

                innerInterceptor.recoredLog();
                adresourceadorderServiceI.save(twadresourceadorder);
            }
        }

        return Json.success();
    }

    @Override
    public Json updateOrder(OrderDTO orderDTO) throws Exception {
        LoginUser user = WebUtil.getLoginUser();

        Tworder tworder = new Tworder();
        BeanUtils.copyProperties(orderDTO, tworder);
        tworder.setIapprovestatus(0);
        tworder.setCreateempid(user.getUserid());
        tworder.setCreateempname(user.getUsername());
        //tworder.setCreatetime(new Date());
        innerInterceptor.recoredLog();
        if (orderMapper.updateById(tworder) == 0) {
            throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
        }
        //删除不存在的明细及归属信息
        List<Long> lsOrderItemIdsDel = new ArrayList<>();
        List<Tworderitem> lsOrderItems = orderitemService.getOrderItemByOrderId(orderDTO.getId().toString());
        if (orderDTO.getOrderitem().size() > 0) {
            List<Long> lsOrderItemIds = lsOrderItems.stream().map(Tworderitem::getId).collect(Collectors.toList());
            List<Long> lsOrderItemIdsNew =
                    orderDTO.getOrderitem().stream().map(Tworderitem::getId).collect(Collectors.toList());
            lsOrderItemIdsDel = lsOrderItemIds.stream().filter(item -> !lsOrderItemIdsNew.contains(item))
                    .collect(Collectors.toList());
        } else {
            //删除明细归属信息
            if (lsOrderItems.size() > 0) {
                lsOrderItemIdsDel =
                        lsOrderItems.stream().map(Tworderitem::getId).collect(Collectors.toList());
            }
        }
        if (lsOrderItemIdsDel.size() > 0) {
            for (Long orderItemId : lsOrderItemIdsDel) {
                //删除明细归属信息
                List<Tworderitembelong> lsOrderItemBelong =
                        orderitembelongServiceI.getOrderItemBelongByOrderItemId(orderItemId.toString());
                if (lsOrderItemBelong.size() > 0) {
                    String ids =
                            lsOrderItemBelong.stream().map(Tworderitembelong::getId).map(Object::toString).collect(Collectors.joining(","));
                    orderitembelongServiceI.deleteOrderItemBelong(ids);
                }
                //删除明细信息
                orderitemService.deleteOrderItemById(orderItemId.toString());
            }
        }

        //获取自增列最大值
        Long orderitemCodeMax = orderitemService.getMaxItemCode() + 1;
        //修改明细信息
        if (orderDTO.getOrderitem().size() > 0) {
            for (OrderItemVO item : orderDTO.getOrderitem()) {
                //判断刊期是否存在
                if (ObjectUtil.isNotNull(item.getPrestartdate()) && "paper".equals(item.getMediatypekey())) {
                    String sPublishNo = mediapublicnumServiceI.getMediaPublishNO(item.getMediaid(),
                            item.getPrestartdate());
                    if (StrUtil.isBlank(sPublishNo)) {
                        continue;
                    }
                }
                Tworderitem orderitem = new Tworderitem();
                BeanUtils.copyProperties(item, orderitem);
                if (ObjectUtil.isNull(orderitem.getOrderid())) {
                    orderitem.setOrderid(tworder.getId());
                }
                orderitem.setIpublishstatus(PublishStatus.PUBLISH_Reserve.getKey());

                Long orderitemId = 0L;
                if (ObjectUtil.isNull(orderitem.getId())) {
                    orderitemId = IdUtil.getSnowflakeNextId();
                    orderitem.setId(orderitemId);
                    orderitem.setCreateempid(user.getUserid());
                    orderitem.setCreateempname(user.getUsername());
                    orderitem.setCreatetime(new Date());
                    orderitem.setIitemcode(orderitemCodeMax);
                    orderitemCodeMax++;
                    innerInterceptor.recoredLog();
                    orderitemService.saveOrderItem(orderitem);
                } else {
                    orderitemId = item.getId();
                    innerInterceptor.recoredLog();
                    orderitemService.updateOrderItem(orderitem);
                }
                //修改明细归属信息
                if (item.getOrderitembelong().size() > 0) {
                    for (Tworderitembelong belong : item.getOrderitembelong()) {
                        if (belong.getId() == null) {
                            belong.setId(IdUtil.getSnowflakeNextId());
                            belong.setOrderitemid(orderitemId);
                            belong.setCreateempid(user.getUserid());
                            belong.setCreateempname(user.getUsername());
                            belong.setCreatetime(new Date());
                        }
                        if (ObjectUtil.isNull(belong.getDeptname())) {
                            Tbdept tbdept = deptServiceI.getById(belong.getDeptid());
                            if (tbdept != null) {
                                belong.setDeptname(tbdept.getSdeptname());
                            }
                        }
                    }
                    innerInterceptor.recoredLog();
                    orderitembelongServiceI.saveOrUpdateBatch(item.getOrderitembelong());
                }
                //如已安排，同步更新安排信息
                List<Tworderitemarrange> lstworderitemarrange =
                        orderitemarrangeServiceI.getOrderitemarrangeByItemId(item.getOrderid(), item.getId());
                if (lstworderitemarrange.size() > 0) {
                    for (Tworderitemarrange tworderitemarrange : lstworderitemarrange) {
                        tworderitemarrange.setMediatypekey(item.getMediatypekey());
                        tworderitemarrange.setMediatypename(item.getMediatypename());
                        tworderitemarrange.setMediaid(item.getMediaid());
                        tworderitemarrange.setMedianame(item.getMedianame());
                        tworderitemarrange.setDpublishdate(item.getPrestartdate());
                        tworderitemarrange.setFoldid(item.getFoldid());
                        tworderitemarrange.setFoldname(item.getFoldname());
                        tworderitemarrange.setFoldareaverid(item.getFoldareaverid());
                        tworderitemarrange.setFoldareavername(item.getFoldareavername());
                        tworderitemarrange.setAddisplayformid(item.getAddisplayformid());
                        tworderitemarrange.setAddisplayformname(item.getAddisplayformname());
                        tworderitemarrange.setPagesortid(item.getPagesortid());
                        tworderitemarrange.setPagesortname(item.getPagesortname());
                        tworderitemarrange.setAdcolorid(item.getAdcolorid());
                        tworderitemarrange.setAdcolorname(item.getAdcolorname());
                        tworderitemarrange.setAdspecid(item.getAdspecid());
                        tworderitemarrange.setAdspecname(item.getAdspecname());
                        tworderitemarrange.setNwidth(item.getNwidth());
                        tworderitemarrange.setNheight(item.getNheight());
                        tworderitemarrange.setFoldpageplanid(item.getFoldpageplanid());
                        if (StrUtil.isNotBlank(item.getFoldpageplanname())) {
                            tworderitemarrange.setFoldpageplanname(Integer.valueOf(item.getFoldpageplanname()));
                        }
                        //查询刊期
                        if ("paper".equals(item.getMediatypekey())) {
                            String sPublishNo = mediapublicnumServiceI.getMediaPublishNO(item.getMediaid(),
                                    item.getPrestartdate());
                            if (StrUtil.isNotBlank(sPublishNo)) {
                                tworderitemarrange.setPublishnum(Integer.valueOf(sPublishNo));
                            }
                        }
                        tworderitemarrange.setSadtitle(item.getSadtitle());
                        tworderitemarrange.setSadcontent(item.getSadcontent());
                        tworderitemarrange.setLastoperatorid(user.getUserid());
                        tworderitemarrange.setLastoperator(user.getUsername());
                        tworderitemarrange.setDlastmodifydate(DateUtil.date());
                        orderitemarrangeServiceI.updateOrderitemarrange(tworderitemarrange);
                    }
                }
            }
        }
        //修改资源与订单的关联
        if (orderDTO.getAdresourceapplicationid().size() > 0) {
            adresourceadorderServiceI.doReSetResourceAdOrder(orderDTO.getId(), orderDTO.getAdresourceapplicationid());
        }

        return Json.success();
    }

    @Override
    public Json deleteOrder(String ids) throws Exception {
        List<String> orderIds = CollUtil.newArrayList(ids.split(","));
        String sInfo = "";
        for (String id : orderIds) {
            //删除订单信息
            Tworder tworder = orderMapper.selectById(id);
            // 广告预约和快速预约,已经提交审核的数据不能删除
            if (Arrays.asList(1, 2).contains(tworder.getIbooktype())
                    && Arrays.asList(APPROVE_EDITING.getKey(), APPROVE_PASS.getKey()).contains(tworder.getIpreapprovestatus())) {
                sInfo += "订单编号：" + tworder.getSordernum() + "已提交审核，不能删除！";
                continue;
            }
            //删除订单明细信息
            Json jsonItem = orderitemService.deleteOrderItem(id);
            if (!jsonItem.isSuccess()) {
                sInfo += jsonItem.getMsg();
                continue;
            }

            tworder.setBdelete(true);
            tworder.setBuse(false);
            innerInterceptor.recoredLog();
            if (orderMapper.updateById(tworder) == 0) {
                throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
            }
        }
        if (StrUtil.isNotBlank(sInfo)) {
            return Json.fail(sInfo);
        }
        //处理订单和明细状态

        return Json.success();
    }

    //订单审批
    @Override
    public Json<String> approveOrder(String orderId, String flowId) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            Tworder tworder = this.getById(orderId);
            String orderNum = "[" + tworder.getScontractnum() + "]" + tworder.getSadtitle();
            // 申请审批
            if (StrUtil.isNotBlank(flowId)) {
                //获取流程条件
                List<Tbflowcondition> flowConditionList =
                        flowconditionServiceI.getFlowConditionList(FlowTypes.FLOW_ORDER.getKey());
                if (CollUtil.isEmpty(flowConditionList)) {
                    return Json.fail("流程条件为空");
                }
                //获取流程条件
                Map<String, Object> flowParamMap = new HashMap<>();
                flowParamMap.put("businessId", orderId);
                flowParamMap.put("businessName", orderNum);
                for (Tbflowcondition tbflowcondition : flowConditionList) {
                    if ("deptid".equals(tbflowcondition.getSconditionkey().toLowerCase())) {
                        flowParamMap.put(tbflowcondition.getSconditionkey(), user.getDeptid().toString());
                        break;
                    }
                }
                return processInstanceService.startProcessInstanceByFlowTypes(flowId, flowParamMap,
                        FlowTypes.FLOW_ORDER);
            }
        } catch (Exception e) {
            return Json.fail("审批申请失败" + e.getMessage());
        }
        return Json.fail("流程id为空");
    }

    //折扣审批
    @Override
    public Json<String> discountApproveOrder(String orderId, String flowId) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            Tworder tworder = this.getById(orderId);
            String sordernum = tworder.getSordernum();
            // 申请审批
            if (!StrUtil.isNotBlank(flowId)) {
                return Json.fail("流程id为空");
            }
            //广告明细
            List<Tworderitem> lsorderitems = orderitemService.getOrderItemByOrderId(orderId);

            //去重
            lsorderitems =
                    lsorderitems.stream().collect(
                            Collectors.collectingAndThen(
                                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(item -> item.getFoldid() + "_" + item.getFoldpageplanid() + "_" + item.getNdiscountrate()))), ArrayList::new)
                    );

            if (lsorderitems.size() == 0) {
                return Json.fail("订单明细为空");
            }
            //获取流程条件
            List<Tbflowcondition> flowConditionList =
                    flowconditionServiceI.getFlowConditionList(FlowTypes.FLOW_CONTRACT.getKey());
            if (CollUtil.isEmpty(flowConditionList)) {
                return Json.fail("流程条件为空");
            }
            //
            Map<String, Object> flowMap = new HashMap<>();
            Object businessId = orderId;
            Object businessName = "[" + tworder.getScontractnum() + "]" + tworder.getSadtitle();
            if (StrUtil.isBlankIfStr(businessId) || StrUtil.isBlankIfStr(businessName)) {
                throw new DataNotExistException("业务id或业务名称不能为空");
            }
            flowMap.put("businessId", businessId);
            flowMap.put("businessName", businessName);
            //过滤特殊
            Tbflowcondition flowCondition =
                    flowConditionList.stream().filter(item -> "special".equals(item.getSconditionkey().toLowerCase())).findFirst().get();
            if (ObjectUtil.isNotNull(flowCondition)) {
                flowMap.put(flowCondition.getSconditionkey(), tworder.getBspecial());
                flowConditionList.remove(flowCondition);
            }
            //过滤部门
            List<Tbflowcondition> lsDeptCondition =
                    flowConditionList.stream().filter(item -> FormTypeEnum.SELECT_DEPT.getType().equals(item.getSconditiontype())).collect(Collectors.toList());
            if (lsDeptCondition.size() > 0) {
                List<Map<String, String>> deptList = new ArrayList<>();
                for (Tbflowcondition flowC : lsDeptCondition) {
                    Map<String, String> deptMap = new HashMap<>();
                    deptMap.put("id", user.getDeptid().toString());
                    deptMap.put("name", user.getDeptname());
                    deptMap.put("type", "dept");
                    deptList.add(deptMap);
                    flowMap.put(flowC.getSconditionkey(), deptList);
                    flowConditionList.remove(flowC);
                }
            }
            //contractDetails 复合条件
            flowCondition =
                    flowConditionList.stream().filter(item -> "ConditionRelation".equals(item.getSconditiontype())).findFirst().get();
            if (ObjectUtil.isNotNull(flowCondition)) {
                //广告类型
                Tbflowcondition adTypeCondition =
                        flowConditionList.stream().filter(item -> "adtype".equals(item.getSconditionkey().toLowerCase())).findFirst().get();
                //媒体
                Tbflowcondition mediaCondition =
                        flowConditionList.stream().filter(item -> "mediaids".equals(item.getSconditionkey().toLowerCase())).findFirst().get();
                //版面
                Tbflowcondition foldpageCondition =
                        flowConditionList.stream().filter(item -> "foldpage".equals(item.getSconditionkey().toLowerCase())).findFirst().get();
                //折扣
                Tbflowcondition discountCondition =
                        flowConditionList.stream().filter(item -> "discount".equals(item.getSconditionkey().toLowerCase())).findFirst().get();

                List<Map<String, String>> relationList = new ArrayList<>();
                for (Tworderitem item : lsorderitems) {
                    Map<String, String> relationMap = new HashMap<>();
                    if (ObjectUtil.isNotNull(adTypeCondition)) {
                        relationMap.put(adTypeCondition.getSconditionkey(), tworder.getAdtypeid().toString());
                    }
                    if (ObjectUtil.isNotNull(mediaCondition)) {
                        relationMap.put(mediaCondition.getSconditionkey(), item.getMediaid().toString());
                    }
                    if (ObjectUtil.isNotNull(foldpageCondition)) {
                        Tbfoldpageplan foldpageplan = foldpageplanServiceI.getById(item.getFoldpageplanid());
                        if (ObjectUtil.isNotNull(foldpageplan)) {
                            relationMap.put(foldpageCondition.getSconditionkey(),
                                    foldpageplan.getFoldname() + "0" + foldpageplan.getPagenum());
                        }
                    }
                    if (ObjectUtil.isNotNull(discountCondition)) {
                        relationMap.put(discountCondition.getSconditionkey(), item.getNdiscountrate().toString());
                    }
                    relationList.add(relationMap);
                }
                flowMap.put(flowCondition.getSconditionkey(), relationList);
            }

            ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
            processInstanceParamDto.setFlowId(flowId);
            processInstanceParamDto.setParamMap(flowMap);
            Json jsonret = processInstanceService.startProcessInstance(processInstanceParamDto);
//            String applicationid = String.valueOf(jsonret.getObj());
//            return Json.success("",applicationid);
            return jsonret;

        } catch (Exception e) {
            return Json.fail("折扣审批申请失败" + e.getMessage());
        }
    }

    @Override
    public Json updateOrderApprovalopinions(String applicationid, String sOrderId, boolean bUpdateopinion,
                                            String approveDesc, Integer iapproveType, Integer iapproveStatus) {
        return updateOrderApprovalopinions(applicationid, sOrderId, bUpdateopinion, approveDesc, iapproveType,
                iapproveStatus, "");
    }

    //审核结束后更新订单状态
    @Override
    public Json updateOrderApprovalopinions(String applicationid, String sOrderId, boolean bUpdatepinion,
                                            String approveDesc
            , Integer iapproveType, Integer iapproveStatus, String sOrderitemId) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            Tworder orderInfo = this.getById(sOrderId);
            if (orderInfo == null) {
                return Json.fail("未找到订单信息！");
            }
            //如果状态是自动拒绝 就不更新状态
            if (bUpdatepinion && iapproveType == ApproveType.APPROVE_Discount.getKey() && orderInfo.getIchangeapprovestatus() == ApproveStatus.APPROVE_REJECT.key
                    && iapproveStatus == ApproveStatus.APPROVE_REJECT.key) {
                return Json.fail("操作失败！该订单折扣审批已被拒绝！");
            }
            if (bUpdatepinion && iapproveType == ApproveType.APPROVE_Order.getKey() && orderInfo.getIapprovestatus() == ApproveStatus.APPROVE_REJECT.key
                    &&iapproveStatus==ApproveStatus.APPROVE_REJECT.key) {
                return Json.fail("操作失败！该订单审批已被拒绝！");
            }

            //折扣状态是在审，订单状态是在审；折扣通过，订单状态是待审；折扣不通过，订单状态是同折扣状态
            //订单审核时只更改订单状态， 订单状态通过的可以加刊、改刊等，加刊时，如需折扣审批则走上面的流程，如不需要折扣审批，则订单状态是待审，需走订单审批
            //预约、改刊、停刊走订单审批流程,订单状态审批通过则对应状态通过
            String sOpinion = "订单";
            if (iapproveType == ApproveType.APPROVE_Add.getKey()) {
                orderInfo.setIaddapprovestatus(iapproveStatus);
                orderInfo.setIapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Add.name;
            }
            if (iapproveType == ApproveType.APPROVE_Change.getKey()) {
                orderInfo.setIchangeapprovestatus(iapproveStatus);
                orderInfo.setIapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Change.name;
            }
            if (iapproveType == ApproveType.APPROVE_Stop.getKey()) {
                orderInfo.setIstopapprovestatus(iapproveStatus);
                orderInfo.setIapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Stop.name;
            }
            if (iapproveType == ApproveType.APPROVE_Pre.getKey()) {
                orderInfo.setIpreapprovestatus(iapproveStatus);
                orderInfo.setIapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Pre.name;
            }
            //折扣修改状态时，折扣通过，订单状态是待审；其他状态订单状态是同折扣状态
            if (iapproveType == ApproveType.APPROVE_Discount.getKey()) {
                orderInfo.setIdiscountapprovestatus(iapproveStatus);
                if (iapproveStatus == APPROVE_PASS.getKey()) {
                    orderInfo.setIapprovestatus(APPROVE_EDIT.getKey());
                } else {
                    orderInfo.setIapprovestatus(iapproveStatus);
                }
                sOpinion = ApproveType.APPROVE_Discount.name;
            }
            //预约、改刊、停刊走订单审批流程,订单状态审批通过则对应状态通过
            if (iapproveType == ApproveType.APPROVE_Order.getKey()) {
                orderInfo.setIapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Order.name;
            }
            if (iapproveType == ApproveType.APPROVE_Stop.getKey()) {
                orderInfo.setIapprovestatus(iapproveStatus);
                orderInfo.setIstopapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Stop.name;
            }
            if (iapproveType == ApproveType.APPROVE_Add.getKey()) {
                orderInfo.setIaddapprovestatus(iapproveStatus);
                orderInfo.setIapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Add.name;
            }
            if (iapproveType == ApproveType.APPROVE_Change.getKey()) {
                orderInfo.setIapprovestatus(iapproveStatus);
                orderInfo.setIchangeapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Change.name;
            }
            if (StrUtil.isNotBlank(approveDesc)) {
                orderInfo.setSopinion(sOpinion + "审核意见:" + approveDesc);
            }

            innerInterceptor.recoredLog();
            if (orderMapper.updateById(orderInfo) == 0) {
                throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
            }
            //更新明细状态
            List<Tworderitem> lsItems = orderitemService.getOrderItemByOrderId(sOrderId);
            if (StrUtil.isNotBlank(sOrderitemId)) {
                lsItems =
                        lsItems.stream().filter(item -> item.getId().equals(Long.valueOf(sOrderitemId))).collect(Collectors.toList());
            }
            if (lsItems.size() > 0) {
                lsItems.forEach(item -> {
                    item.setCreateempid(user.getUserid());
                    item.setCreateempname(user.getUsername());
                    item.setCreatetime(new Date());
                    if (iapproveType == ApproveType.APPROVE_Add.getKey()) {
                        item.setIapprovestatus(iapproveStatus);
                        item.setIaddapprovestatus(iapproveStatus);
                    }
                    if (iapproveType == ApproveType.APPROVE_Change.getKey()) {
                        item.setIapprovestatus(iapproveStatus);
                        item.setIchangeapprovestatus(iapproveStatus);
                        //审批通过后 该明细无效
                        if (StrUtil.isNotBlank(sOrderitemId) && sOrderitemId.equals(item.getId().toString()) && iapproveStatus == APPROVE_PASS.getKey()) {
                            item.setBuse(false);
                        }
                    }
                    if (iapproveType == ApproveType.APPROVE_Stop.getKey()) {
                        item.setIapprovestatus(iapproveStatus);
                        item.setIstopapprovestatus(iapproveStatus);
                        //审批通过后 该明细无效
                        if (StrUtil.isNotBlank(sOrderitemId) && sOrderitemId.equals(item.getId().toString()) && iapproveStatus == APPROVE_PASS.getKey()) {
                            item.setBuse(false);
                        }
                    }
                    //折扣修改状态时，折扣通过，订单状态是待审；其他状态订单状态是同折扣状态
                    if (iapproveType == ApproveType.APPROVE_Discount.getKey()) {
                        item.setIdiscountapprovestatus(iapproveStatus);
                        if (iapproveStatus == ApproveStatus.APPROVE_PASS.getKey()) {
                            item.setIapprovestatus(APPROVE_EDIT.getKey());
                        } else {
                            item.setIapprovestatus(iapproveStatus);
                        }
                    }
                    if (iapproveType == ApproveType.APPROVE_Order.getKey()) {
                        item.setIapprovestatus(iapproveStatus);
                    }
                });
                innerInterceptor.recoredLog();
                orderitemService.updateBatchById(lsItems);
            }
            //更新审批流程关联表
            if (StrUtil.isNotBlank(applicationid)) {
                TwapplicationrelationsDTO applicationrelationsDTO = new TwapplicationrelationsDTO();
                applicationrelationsDTO.setApplicationid(applicationid);
                applicationrelationsDTO.setMainid(orderInfo.getId());
                applicationrelationsDTO.setFlowtype(iapproveType.toString());//FlowTypes.FLOW_ORDER.getKey());

                List<TwapplicationrelationsVO> lsApplicationRelations =
                        applicationrelationsServiceI.getApplicationRelationsList(applicationrelationsDTO);
                if (lsApplicationRelations.size() > 0) {
                    for (Twapplicationrelations item : lsApplicationRelations) {
                        item.setIapprovestatus(iapproveStatus);
                        if (bUpdatepinion) {
                            item.setSapprovalopinions(sOpinion + "审核意见:" + approveDesc);
                        }
                        applicationrelationsServiceI.updateById(item);
                    }
                }
            }
            return Json.success("操作成功！");
        } catch (Exception e) {
            return Json.fail("操作失败！ " + e.getMessage());
        }
    }

    /**
     * 据合同号获取订单Map
     * 方法功能:据合同号获取订单Map，key是合同号，value是Tworder；仅返回“未被删除&启用中&审批通过”的订单
     *
     * @param contractNums
     * @return java.util.Map<java.lang.String, com.hgzp.core.model.Tworder>
     * @author yanz
     * @date 2023/12/1 10:13
     */
    @Override
    public Map<String, Tworder> getOrdersByContractNums(List<String> contractNums) {
        if (ObjUtil.isEmpty(contractNums)) {
            return null;
        }
        return this.lambdaQuery()
                .in(Tworder::getScontractnum, contractNums)
                .eq(Tworder::getBuse, true)
                .eq(Tworder::getBdelete, false)
                .eq(Tworder::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                .list()
                .stream()
                .collect(Collectors.toMap(Tworder::getScontractnum, Function.identity()));
    }

    @Override
    public void savePreOrder(PreOrderDTO preOrderDTO) {
        LoginUser loginUser = WebUtil.getSafeLoginUser();
        Long userid = loginUser.getUserid();
        String username = loginUser.getUsername();
        // 订单和订单明细
        TworderDTO orderDTO = preOrderDTO.getOrder();
        Integer ibooktype = orderDTO.getIbooktype();
        Assert.isTrue(ibooktype != null && (ibooktype == 1 || ibooktype == 2), "仅限广告预约和快速预约");
        Long adResourceApplicationId = preOrderDTO.getAdResourceApplicationId();
        Long approvedResourceApplicationId = preOrderDTO.getApprovedResourceApplicationId();
        // 快速预约
        if (ibooktype == 2 && adResourceApplicationId == null && approvedResourceApplicationId == null) {
            throw new IllegalArgumentException("快速预约关联的广告资源申请id不可为空");
        }
        String orderNum = StrUtil.isBlank(orderDTO.getSordernum())
                ? produceServiceI.getOrderNo() : orderDTO.getSordernum();
        Date createTime = orderDTO.getCreatetime() == null ? new Date() : orderDTO.getCreatetime();
        Tworder order = TworderDTO.parseToEntity(orderDTO).toBuilder()
                .sordernum(orderNum)
                .createempid(userid)
                .createempname(username)
                .createtime(createTime)
                .build();
        Long orderId = order.getId();
        List<Tworderitembelong> orderItemBelongs = new ArrayList<>();
        List<Long> needDeleteBelongIds = new ArrayList<>();
        List<Tworderitem> orderItems = preOrderDTO.getOrderItems().stream()
                .map((dto) -> {
                    BigDecimal curWidth = dto.getNwidth();
                    BigDecimal curHeight = dto.getNheight();
                    if (dto.getNwidth() == null) {
                        Tbadspec tbadspec = tbadspecServiceI.getById(dto.getAdspecid());
                        if (tbadspec != null) {
                            curWidth = tbadspec.getNwidth();
                            curHeight = tbadspec.getNheight();
                        }
                    }
                    Tworderitem orderItem = TworderitemDTO.parseToEntity(dto)
                            .setOrderid(orderId)
                            .setSordernum(orderNum)
                            .setCreateempid(userid)
                            .setCreateempname(username)
                            .setCreatetime(createTime)
                            .setNwidth(curWidth)
                            .setNheight(curHeight);

                    if (orderItem.getBdelete()) {
                        needDeleteBelongIds.addAll(dto.getOrderitembelong().stream()
                                .map(TworderitembelongDTO::getId).collect(Collectors.toList()));
                    } else {
                        BigDecimal hundred = new BigDecimal("100");
                        // 预约只有一个归属业务员
                        orderItemBelongs.add(TworderitembelongDTO.parseToEntity(dto.getOrderitembelong().get(0))
                                .setOrderitemid(orderItem.getId())
                                .setSordernum(orderNum)
                                .setCreatetime(createTime)
                                .setArchievementrate(hundred)
                                .setTaskrate(hundred)
                                .setCommissionrate(hundred)
                                .setBprimary(true));
                    }
                    return orderItem;
                }).collect(Collectors.toList());
        // 如果此快速预约订单已经存在关联的广告资源申请,则不再保存；否则保存
        // 需要保存的广告资源申请
        List<Twadresourceadorder> needSavedAdResourceAdOrders = new ArrayList<>();
        // 需要删除的广告资源申请
        List<Long> needDeleteAdResourceAdOrderIds = new ArrayList<>();
        if (ibooktype == 2) {
            List<Long> giveResourceApplicationIds = ListUtil.toList(adResourceApplicationId,
                    approvedResourceApplicationId);
            giveResourceApplicationIds.removeIf(Objects::isNull);
            // 数据库中已经存在的资源申请
            List<Twadresourceadorder> existedResourceOrders = adresourceadorderServiceI.list(
                    Wrappers.<Twadresourceadorder>lambdaQuery().eq(Twadresourceadorder::getAdorderid, orderId));
            // 数据库中已经存在的资源申请ids
            List<Long> existedResourceApplicationIds = existedResourceOrders.stream()
                    .map(Twadresourceadorder::getAdresourceapplicationid).collect(Collectors.toList());
            // 数据库中没有,则新建
            for (Long id : giveResourceApplicationIds) {
                if (!existedResourceApplicationIds.contains(id)) {
                    Twadresourceadorder adResourceAdOrder = Twadresourceadorder.builder()
                            .adresourceapplicationid(id)
                            .adorderid(orderId)
                            .createempid(userid)
                            .createempname(username)
                            .dcreatetime(createTime)
                            .build();
                    needSavedAdResourceAdOrders.add(adResourceAdOrder);
                }
            }
            // 本次提交时无此resourceApplicationId,则删除
            for (Twadresourceadorder resourceOrder : existedResourceOrders) {
                if (!giveResourceApplicationIds.contains(resourceOrder.getAdresourceapplicationid())) {
                    needDeleteAdResourceAdOrderIds.add(resourceOrder.getId());
                }
            }
        }

        // 保存订单
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            innerInterceptor.recoredLog();
            this.saveOrUpdate(order);
            innerInterceptor.recoredLog();
            orderitemService.saveOrUpdateBatch(orderItems);
            innerInterceptor.recoredLog();
            orderitembelongServiceI.removeByIds(needDeleteBelongIds);
            innerInterceptor.recoredLog();
            orderitembelongServiceI.saveOrUpdateBatch(orderItemBelongs);
            if (ibooktype == 2) {
                if (CollectionUtil.isNotEmpty(needSavedAdResourceAdOrders)) {
                    innerInterceptor.recoredLog();
                    adresourceadorderServiceI.saveBatch(needSavedAdResourceAdOrders);
                }
                if (CollectionUtil.isNotEmpty(needDeleteAdResourceAdOrderIds)) {
                    innerInterceptor.recoredLog();
                    adresourceadorderServiceI.removeByIds(needDeleteAdResourceAdOrderIds);
                }
            }
        });
    }

    @Override
    public IPage<TworderVO> getPreOrderPageList(IPage<Tworder> page, PreOrderQueryDTO query) {
        String bookType = query.getBookType();
        String queryKey = query.getQueryKey();
        boolean hasQueryKey = StrUtil.isNotBlank(queryKey);
        Date startTime = query.getStartTime();
        Date endTime = query.getEndTime();
        boolean isFilterDate = startTime != null && endTime != null;
        Set<Long> orderIds;
        if (isFilterDate) {
            orderIds = orderitemService.list(Wrappers.<Tworderitem>lambdaQuery()
                    .eq(Tworderitem::getIbooktype, bookType)
                    .eq(Tworderitem::getBuse, true)
                    .eq(Tworderitem::getBdelete, false)
                    .between(Tworderitem::getCreatetime, startTime, endTime)
            ).stream().map(Tworderitem::getOrderid).filter(Objects::nonNull).collect(Collectors.toSet());
        } else {
            orderIds = new HashSet<>();
        }

        LoginUser loginUser = WebUtil.getSafeLoginUser();
        Boolean isLeader = loginUser.getBlead();
        List<Long> rolePermDeptIds = DataUtil.getRolePermDeptIds();
        IPage<Tworder> p = page(page, Wrappers.<Tworder>lambdaQuery()
                .eq(Tworder::getIbooktype, bookType)
                .eq(Tworder::getBuse, true)
                .eq(Tworder::getBdelete, false)
                // 只过滤创建日期或预刊日期
                .and(isFilterDate, i -> i.between(Tworder::getCreatetime, startTime, endTime).or()
                        .in(CollectionUtil.isNotEmpty(orderIds), Tworder::getId, orderIds))
                .and(hasQueryKey, i -> i.like(Tworder::getSadtitle, queryKey).or()
                        .like(Tworder::getCustomername, queryKey).or()
                        .like(Tworder::getAgencyname, queryKey).or()
                        .like(Tworder::getAgentname, queryKey))
                // 领导,看自己部门的数据,rolePermDeptIds为空则查看所有的数据
                .in(isLeader && CollectionUtil.isNotEmpty(rolePermDeptIds), Tworder::getDeptid, rolePermDeptIds)
                // 不是领导,只能看自己的数据
                .eq(!isLeader, Tworder::getCreateempid, loginUser.getUserid())
                .orderByDesc(Tworder::getCreatetime)
        );
        //2024.02.19 关联补刊订单的问题
        p.getRecords().forEach(item -> {
            List<Long> lsIds = new LambdaQueryChainWrapper<>(orderMapper)
                    .eq(Tworder::getBuse, true).eq(Tworder::getBdelete, false)
                    .eq(Tworder::getRelateorderid, item.getId())
                    .list()
                    .stream()
                    .map(Tworder::getId)
                    .distinct()
                    .collect(Collectors.toList());
            if (lsIds.size() > 0) {
                item.setRelateorderid(lsIds.get(0));
            }
        });
        return PageUtils.parse(p, TworderVO::parseToVO);
    }

    @Override
    public TworderVO getPreOrder(Long orderId) {
        TworderVO tworderVO = TworderVO.parseToVO(getById(orderId));
        List<Tworderitem> tworderitems = orderitemService.list(Wrappers.<Tworderitem>lambdaQuery()
                .eq(Tworderitem::getOrderid, orderId)
                .eq(Tworderitem::getBuse, true)
                .eq(Tworderitem::getBdelete, false)
        );
        List<Tworderitembelong> belongs = orderitembelongServiceI.list(Wrappers.<Tworderitembelong>lambdaQuery()
                .in(Tworderitembelong::getOrderitemid,
                        tworderitems.stream().map(Tworderitem::getId).collect(Collectors.toList())));
        List<TworderitemVO> orderItems = tworderitems.stream().map(i -> {
            TworderitemVO item = TworderitemVO.parseToVO(i);
            belongs.stream().filter(b -> b.getOrderitemid().equals(i.getId())).findFirst()
                    // 预约时每个明细只有一个归属
                    .ifPresent(b -> item.setOrderitembelong(ListUtil.toList(TworderitembelongVO.parseToVO(b))));
            return item;
        }).collect(Collectors.toList());
        tworderVO.setOrderitem(orderItems);
        return tworderVO;
    }

    /**
     * 查询一段时间内的认刊合同
     * 方法功能:认刊合同查询用。条件包括：时间范围、发票号码或合同号（queryKey）、客户名称（可填写可选择customername）
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/1/4 14:29
     */
    @Override
    public IPage<OrderAndItemDTO> getOrdersInPeriod(Page<Tworder> page, OrderAndItemVO query) {
        //返回列表包括列：
        //合同号、项目名称、经营主体、直接客户、业务员、代理公司、内容生成方、广告行业、广告标题、应收金额、已收金额、欠款金额、业绩金额、关联发票（前台显示多个发票号拼接）

        // 条件包括：时间范围、合同号（queryKey）、客户名称（可填写可选择customername）
        LambdaQueryWrapper<Tworder> orderQuery = Wrappers.lambdaQuery();
        // 日期限制
        orderQuery.ge(ObjUtil.isNotNull(query.getStartTime()), Tworder::getCreatetime, query.getStartTime());
        if (ObjUtil.isNotNull(query.getEndTime())) {
            orderQuery.lt(Tworder::getCreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        //客户名称（3个字段） - order
        orderQuery.and(StrUtil.isNotBlank(query.getCustomername()), i -> i.like(Tworder::getCustomername,
                        query.getCustomername())
                .or().like(Tworder::getAgencyname, query.getCustomername())
                .or().like(Tworder::getAgentname, query.getCustomername()));
        // 有效 & 通过
        orderQuery.eq(Tworder::getBuse, true)
                .eq(Tworder::getIapprovestatus, APPROVE_PASS.getKey());
        // 合同号
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            orderQuery.like(Tworder::getScontractnum, query.getQueryKey());
        }
        // 排序
        orderQuery.orderByDesc(Tworder::getCreatetime);

        List<Tworder> orders = orderMapper.selectList(orderQuery);

        List<OrderAndItemDTO> result = new ArrayList<>();
        for (Tworder order : orders) {
            LambdaQueryWrapper<Tworderitem> itemQuery = Wrappers.lambdaQuery();
            itemQuery.eq(Tworderitem::getOrderid, order.getId()).eq(Tworderitem::getBuse, true)
                    .eq(Tworderitem::getIapprovestatus, APPROVE_PASS.getKey());
            List<Tworderitem> items = orderitemService.list(itemQuery);

            Tworderitem itemForDto = OrderAndItemDTO.getOrderitemForDtoConversion(items);

            // 当前orderid -> 预开发票申请id -> 发票id -> 发票
            List<Long> preInvoiceApplicationIds =
                    preinvoiceapplicationdetailServiceI.getPreInvoiceApplicationIdsByOrderId(order.getId());
            // 存的数据可能对不上
            if (CollUtil.isEmpty(preInvoiceApplicationIds)) {
                Optional.ofNullable(OrderAndItemDTO.forGetOrdersInPeriod(order, itemForDto, ""))
                        .ifPresent(result::add);
                continue;
            }
            List<Twpreinvoiceapplication> twpreinvoiceapplications =
                    preinvoiceapplicationServiceI.listByIds(preInvoiceApplicationIds);
            // 以英文逗号分隔，拼成字符串
            String invoices = twpreinvoiceapplications.stream()
                    .filter(a -> a.getIapprovestatus() == APPROVE_PASS.getKey())
                    .map(Twpreinvoiceapplication::getInvoice)
                    .filter(a -> ObjUtil.isNotNull(a))
                    .collect(Collectors.joining(","));
            Optional.ofNullable(OrderAndItemDTO.forGetOrdersInPeriod(order, itemForDto, invoices))
                    .ifPresent(result::add);
        }

        // 结果
        Page<OrderAndItemDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(page, dtoPage);
        dtoPage.setRecords(result);
        dtoPage.setTotal(page.getTotal());
        return dtoPage;
    }

    /**
     * 查询订单合同详情
     * 方法功能:按订单id，查询订单合同详情（纯List）
     *
     * @param orderId
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.dto.OrderContractDetailDTO>
     * @author yanz
     * @date 2024/1/5 13:41
     */
    @Override
    public OrderContractDetailDTO getOrderContractDetail(Long orderId) {
        OrderContractDetailDTO dto = new OrderContractDetailDTO();

        // 订单基本信息
        Tworder tworder = this.lambdaQuery()
                .eq(Tworder::getId, orderId)
                .eq(Tworder::getBuse, true)
                .eq(Tworder::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                .one();
        if (ObjUtil.isNull(tworder)) {
            return dto;
        }
        // 存入
        dto.setOrder(tworder);

        // 广告明细
        List<Tworderitem> tworderitems = orderitemService.lambdaQuery()
                .eq(Tworderitem::getOrderid, orderId)
                .eq(Tworderitem::getBuse, true)
                .eq(Tworderitem::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                .list();
        if (CollUtil.isNotEmpty(tworderitems)) {
            // 存入
            dto.setOrderitemList(tworderitems);

            // 成本明细
            List<Long> orderitemIds = tworderitems.stream().map(Tworderitem::getId).collect(Collectors.toList());
            List<Tworderitemcost> Tworderitemcosts =
                    orderitemcostServiceI.lambdaQuery()
                            .in(Tworderitemcost::getOrderitemid, orderitemIds)
                            .eq(Tworderitemcost::getIstatus, AppStatus.APPRSTATUS_PASS.getKey())
                            .list();
            if (CollUtil.isNotEmpty(Tworderitemcosts)) {
                List<OrderItemCostDTO> orderItemCostDTOS =
                        orderitemcostServiceI.convertToOrderItemCostDTOs(Tworderitemcosts);
                // 存入
                dto.setOrderItemCostDTOList(orderItemCostDTOS);
            }
        }

        // 预开发票列表
        List<Twpreinvoiceapplicationdetail> twpreinvoiceapplicationdetails =
                preinvoiceapplicationdetailServiceI.lambdaQuery()
                        .eq(Twpreinvoiceapplicationdetail::getOrderid, orderId)
                        .list();
        if (CollUtil.isNotEmpty(twpreinvoiceapplicationdetails)) {
            List<Long> preinvoapplyIds =
                    twpreinvoiceapplicationdetails.stream().map(Twpreinvoiceapplicationdetail::getPreinvoiceapplicationid).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(preinvoapplyIds)) {
                List<Twpreinvoiceapplication> twpreinvoiceapplications = preinvoiceapplicationServiceI.lambdaQuery()
                        .in(Twpreinvoiceapplication::getId, preinvoapplyIds)
                        .eq(Twpreinvoiceapplication::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                        .list();
                if (CollUtil.isNotEmpty(twpreinvoiceapplications)) {
                    List<PreInvoiceApplicationDTO> preInvoiceApplicationDTOList =
                            invoiceServiceI.convertPreInvoiceApplicationToDTOs(twpreinvoiceapplications);
                    // 存入
                    dto.setPreInvoiceApplicationDTOList(preInvoiceApplicationDTOList);
                }
            }
        }

        // 已分摊明细
        List<Tworderapportiondetail> tworderapportiondetails =
                orderApportiondetailServiceI.lambdaQuery().eq(Tworderapportiondetail::getOrderid, orderId).list();
        if (CollUtil.isNotEmpty(tworderapportiondetails)) {
            List<OrderApportiondetailDTO> orderApportiondetailDTOS =
                    orderApportiondetailServiceI.convertApportiondetailToDTOs(tworderapportiondetails);
            // 存入
            dto.setOrderApportiondetailDTOList(orderApportiondetailDTOS);
        }

        // 与此订单关联的客户的预收款信息 - 3种客户
        List<Long> customerIds = Arrays.asList(tworder.getAgentid(), tworder.getAgencytid(), tworder.getCustomerid());
        List<Twcustomer> tbcustomers = customerServiceI.lambdaQuery()
                .in(Twcustomer::getId, customerIds)
                .eq(Twcustomer::getBuse, true)
                .eq(Twcustomer::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                .list();
        if (CollUtil.isNotEmpty(tbcustomers)) {
            customerIds = tbcustomers.stream().map(Twcustomer::getId).collect(Collectors.toList());
            List<Twcustomercharge> twcustomercharges = customerchargeServiceI.lambdaQuery()
                    .in(Twcustomercharge::getCustomerid, customerIds)
                    .eq(Twcustomercharge::getItype, EcustomerChargeType.EntryType_AdPay.getKey())
                    .list();
            if (CollUtil.isNotEmpty(twcustomercharges)) {
                List<CustomerChargeBankDTO> customerChargeBankDTOS =
                        customerchargeServiceI.convertToCustomerChargeBankDtosForToPrePaymentInfo(twcustomercharges);
                // 存入
                dto.setCustomerChargeBankDTOList(customerChargeBankDTOS);
            }
        }

        // 广告资源 - 要把所有的资源文件带出来，要查看文件详情的(AdResourceApplication 的 adorderid 列)
        List<Twadresourceadorder> twadresourceadorders =
                adresourceadorderServiceI.lambdaQuery().eq(Twadresourceadorder::getAdorderid, orderId).list();
        if (CollUtil.isNotEmpty(twadresourceadorders)) {
            List<Long> adResourceApplicationIds =
                    twadresourceadorders.stream().map(Twadresourceadorder::getAdresourceapplicationid).collect(Collectors.toList());
            List<Twadresourceapplication> twadresourceapplications = adresourceapplicationServiceI.lambdaQuery()
                    .in(Twadresourceapplication::getId, adResourceApplicationIds)
                    .eq(Twadresourceapplication::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                    .list();
            if (CollUtil.isNotEmpty(twadresourceapplications)) {
                List<AdResourceApplicationDTO> adResourceApplicationDTOS =
                        adresourceapplicationServiceI.convertAdResourceApplicationToDTOs(twadresourceapplications);
                // 存入
                dto.setResourceApplicationDTOList(adResourceApplicationDTOS);
            }
        }

        return dto;
    }

    @Override
    public void submitPreOrder(String orderId) {
        Tworder order = getById(orderId);
        Assert.isTrue(order != null, "预约订单不存在");
        Assert.isTrue(Arrays.asList(1, 2).contains(order.getIbooktype()), "非预约订单");
        Assert.isTrue(
                !Arrays.asList(APPROVE_EDITING.getKey(), APPROVE_PASS.getKey()).contains(order.getIpreapprovestatus()),
                "预约订单的审核状态不能是在审或审核通过"
        );
        order.setIpreapprovestatus(APPROVE_EDITING.getKey()).setSopinion("");
        innerInterceptor.recoredLog();
        updateById(order);
    }

    @Override
    public void approvePreOrder(String orderId, String status, String comment) {
        Tworder order = getById(orderId);
        Assert.isTrue(order != null, "预约订单不存在");
        Assert.isTrue(Arrays.asList(1, 2).contains(order.getIbooktype()), "非预约订单");
        Assert.isTrue(Objects.equals(order.getIpreapprovestatus(), APPROVE_EDITING.getKey()), "预约订单的审核状态不是在审");
        int statusInt = Integer.parseInt(status);
        if (Objects.equals(statusInt, APPROVE_REJECT.getKey()) && StrUtil.isBlank(comment)) {
            throw new IllegalArgumentException("审核不通过需要填写审核意见");
        }
        order.setIpreapprovestatus(statusInt).setSopinion(comment);
        innerInterceptor.recoredLog();
        updateById(order);
        // 如果审核通过,且是快速预约,则直接对广告资源进行提交审核,进行后续的审批流程
        if (Objects.equals(statusInt, APPROVE_PASS.getKey()) && Objects.equals(order.getIbooktype(), 2)) {
            Twadresourceadorder adResourceAdOrder =
                    adresourceadorderServiceI.getOne(Wrappers.<Twadresourceadorder>lambdaQuery()
                            .eq(Twadresourceadorder::getAdorderid, orderId));
            Assert.isTrue(adResourceAdOrder != null, "快速预约订单没有关联的广告资源申请");
            Long resourceApplicationId = adResourceAdOrder.getAdresourceapplicationid();
            // 如果广告资源已经审批通过(或在审),则不再进行后续的审批流程
            Twadresourceapplication adResourceApplication =
                    adresourceapplicationServiceI.getById(resourceApplicationId);
            if (adResourceApplication != null && Arrays.asList(APPROVE_EDITING.getKey(), APPROVE_PASS.getKey())
                    .contains(adResourceApplication.getIapprovestatus())) {
                return;
            }
            List<DataCombo> flowComboByFlowType = processInstanceService.getFlowComboByFlowType(FLOW_ADSOURCE.getKey());
            Assert.isTrue(CollUtil.isNotEmpty(flowComboByFlowType), "广告资源审批流程不存在");
            String flowId = null;
            for (DataCombo dataCombo : flowComboByFlowType) {
                if (dataCombo.getName().contains("广告资源预审")) {
                    flowId = dataCombo.getId();
                }
            }
            if (flowId == null) {
                flowId = flowComboByFlowType.get(0).getId();
            }
            try {
                adresourceapplicationServiceI.submitCheck(resourceApplicationId + "", flowId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateOrderCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception {
        try {
            String[] splitids = customerIds.split(",");
            //客户Id
            List<Tworder> orders = this.lambdaQuery()
                    .in(Tworder::getCustomerid, splitids)
                    .list();
            if (orders.size() > 0) {

                orders.forEach(order -> {
                    order.setCustomerid(newcustomerId);
                    order.setCustomername(newcustomername);
                });
                innerInterceptor.recoredLog();
                this.updateBatchById(orders);
            }
            //代理公司更新
            List<Tworder> agencyts = this.lambdaQuery()
                    .in(Tworder::getAgencytid, splitids)
                    .list();
            if (agencyts.size() > 0) {
                agencyts.forEach(order -> {
                    order.setAgencytid(newcustomerId);
                    order.setAgencyname(newcustomername);
                });
                innerInterceptor.recoredLog();
                this.updateBatchById(agencyts);
            }
            //内容生成方
            List<Tworder> agents = this.lambdaQuery()
                    .in(Tworder::getAgentid, splitids)
                    .list();
            if (agents.size() > 0) {
                agents.forEach(order -> {
                    order.setAgentid(newcustomerId);
                    order.setAgentname(newcustomername);
                });
                innerInterceptor.recoredLog();
                this.updateBatchById(agents);
            }
        } catch (Exception e0) {
            throw new RuntimeException("订单合并更新失败！" + e0.getMessage());
        }
    }

    @Override
    public OrderDTO getOrderInfoByContractNum(String contractNum) throws Exception {
        Tworder order = this.lambdaQuery()
                .eq(Tworder::getScontractnum, contractNum)
                .one();
        if (order == null) {
            return null;
        }
        OrderDTO orderDTO = new OrderDTO();
        if (ObjectUtil.isNotNull(order)) {
            BeanUtils.copyProperties(order, orderDTO);
            //资源Id
            List<Long> lsRresourceIds = adresourceadorderServiceI.getResourceaIdbyOrderId(order.getId().toString());
            orderDTO.setAdresourceapplicationid(lsRresourceIds);

            List<Tworderitem> orderitem = orderitemService.getOrderItemByOrderId(order.getId().toString());

            if (orderitem.size() > 0) {
                List<OrderItemVO> orderItemVOList = new ArrayList<>();
                for (Tworderitem item : orderitem) {
                    OrderItemVO orderItemVO = new OrderItemVO();
                    BeanUtils.copyProperties(item, orderItemVO);
                    List<Tworderitembelong> orderitembelong =
                            orderitembelongServiceI.getOrderItemBelongByOrderItemId(item.getId().toString());
                    orderItemVO.setOrderitembelong(orderitembelong);
                    orderItemVOList.add(orderItemVO);
                }
                orderDTO.setOrderitem(orderItemVOList);
            }

            //历史流程
            List<ProcessInstanceRecord> lsProcessRecord =
                    processInstanceService.getHistoryProcessInstanceRecordByBussinessId("businessId",
                            order.getId().toString());
            if (lsProcessRecord.size() > 0) {
                //订单审批流程历史Id
                List<ProcessInstanceRecord> lsOrderProcessRecord = lsProcessRecord.stream()
                        .filter(item -> item.getGroupId().equals(FlowTypes.FLOW_ORDER.getKey()) || item.getGroupId().equals(FlowTypes.FLOW_ORDERCHANGE.getKey()))
                        .sorted(Comparator.comparing(ProcessInstanceRecord::getCreateTime).reversed())
                        .collect(Collectors.toList());
                if (lsOrderProcessRecord.size() > 0) {
                    List<ProcessInstanceVo> lsProcessInstance = new ArrayList<>();
                    for (ProcessInstanceRecord item : lsOrderProcessRecord) {
                        ProcessInstanceVo instanceVo = new ProcessInstanceVo();
                        instanceVo.setProcessInstanceId(item.getProcessInstanceId());
                        instanceVo.setFlowId(item.getFlowId());
                        instanceVo.setCreateTime(item.getCreateTime());
                        instanceVo.setResult(item.getResult());
                        lsProcessInstance.add(instanceVo);
                    }
                    orderDTO.setOrderProcessInstanceid(lsProcessInstance);
                }
                //折扣审批流程历史Id
                List<ProcessInstanceRecord> lsdiscountProcessRecord = lsProcessRecord.stream()
                        .filter(item -> item.getGroupId().equals(FlowTypes.FLOW_CONTRACT.getKey()))
                        .sorted(Comparator.comparing(ProcessInstanceRecord::getCreateTime).reversed())
                        .collect(Collectors.toList());

                if (lsdiscountProcessRecord.size() > 0) {
                    List<ProcessInstanceVo> lsProcessInstance = new ArrayList<>();
                    for (ProcessInstanceRecord item : lsdiscountProcessRecord) {
                        ProcessInstanceVo instanceVo = new ProcessInstanceVo();
                        instanceVo.setProcessInstanceId(item.getProcessInstanceId());
                        instanceVo.setFlowId(item.getFlowId());
                        instanceVo.setCreateTime(item.getCreateTime());
                        instanceVo.setResult(item.getResult());
                        lsProcessInstance.add(instanceVo);
                    }
                    orderDTO.setDiscountProcessInstanceid(lsProcessInstance);
                }
            }

        }
        return orderDTO;
    }

    @Override
    public List<CJAdPrinDTO> getCJAdPrinListForCJ(String startTime, String endTime, String FoldId, String FoldVer,
                                                  String FoldPage) {
        List<CJAdPrinDTO> lsCJAdPrinDTO = orderMapper.selectOrderAndItemListForCJ(startTime, endTime, FoldId, FoldVer
                , FoldPage,
                PublishStatus.PUBLISH_Arrange.getKey(), PublishStatus.PUBLISH_Published.getKey());
        return lsCJAdPrinDTO;
    }

    @Override
    public List<Tworder> getOrdersBySordernumForCJ(String sordernum) {
        List<Tworder> lsOrder = this.lambdaQuery()
                .eq(ObjectUtil.isNotEmpty(sordernum), Tworder::getSordernum, sordernum)
                .eq(Tworder::getBuse, true)
                .list();
        return lsOrder;
    }

    //改加停刊审批流程
    @Override
    public Json<String> approveOrderItemAddChangeStop(String orderItemId, String flowId, Integer iApproveType) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            Tworderitem tworderitem = orderitemService.getById(orderItemId);
            String orderNum = "[" + tworderitem.getScontractnum() + "]" + tworderitem.getSadtitle();
            // 申请审批
            if (StrUtil.isNotBlank(flowId)) {
                //获取流程条件
                List<Tbflowcondition> flowConditionList =
                        flowconditionServiceI.getFlowConditionList(FlowTypes.FLOW_ORDERCHANGE.getKey());
                if (CollUtil.isEmpty(flowConditionList)) {
                    return Json.fail("流程条件为空");
                }
                //获取流程条件
                Map<String, Object> flowParamMap = new HashMap<>();
                flowParamMap.put("businessId", tworderitem.getOrderid().toString());
                flowParamMap.put("businessName", orderNum);
                for (Tbflowcondition tbflowcondition : flowConditionList) {
                    if ("deptid".equals(tbflowcondition.getSconditionkey())) {
                        flowParamMap.put(tbflowcondition.getSconditionkey(), user.getDeptid().toString());
                        break;
                    }
                }
                return processInstanceService.startProcessInstanceByFlowTypes(flowId, flowParamMap,
                        FlowTypes.FLOW_ORDERCHANGE);
            }
        } catch (Exception e) {
            return Json.fail("审批申请失败" + e.getMessage());
        }
        return Json.fail("流程id为空");
    }
}

