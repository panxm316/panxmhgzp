package com.hgzp.advertising.service.ad.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.approve.ApproveType;
import com.hgzp.advertising.emnus.business.CommissionStatus;
import com.hgzp.advertising.emnus.schedule.PublishStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.dto.OrderDebtDTO;
import com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO;
import com.hgzp.advertising.pagemodel.api.CJOrderItemDTO;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.vo.DebtReasonVO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO;
import com.hgzp.advertising.pagemodel.finance.vo.CustomerChargeBankVO;
import com.hgzp.advertising.pagemodel.finance.vo.OrderApportiondetailVO;
import com.hgzp.advertising.pagemodel.flow.TwapplicationrelationsVO;
import com.hgzp.advertising.pagemodel.flow.dto.TwapplicationrelationsDTO;
import com.hgzp.advertising.service.ad.*;
import com.hgzp.advertising.service.business.CommissionServiceI;
import com.hgzp.advertising.service.finance.OrderApportiondetailServiceI;
import com.hgzp.advertising.service.finance.TwcustomerchargeServiceI;
import com.hgzp.advertising.service.flow.IProcessInstanceService;
import com.hgzp.advertising.service.flow.TwapplicationrelationsServiceI;
import com.hgzp.advertising.service.media.TbadspecServiceI;
import com.hgzp.advertising.service.media.TbmediaServiceI;
import com.hgzp.advertising.service.media.TbmediapublicnumServiceI;
import com.hgzp.advertising.service.system.TbadindustryServiceI;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.advertising.service.system.TwmessageServiceI;
import com.hgzp.common.flowable.dto.third.MessageDto;
import com.hgzp.core.emnus.FlowTypes;
import com.hgzp.core.emnus.MessageTypeEnum;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.ad.TworderMapper;
import com.hgzp.mapper.ad.TworderitemMapper;
import com.hgzp.mapper.business.TwdebtreasonMapper;
import com.hgzp.mapper.business.TwpreinvoiceapplicationMapper;
import com.hgzp.mapper.finance.TwinvoiceMapper;
import com.hgzp.mapper.finance.TworderapportiondetailMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.file.UfileUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单刊期表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TworderitemServiceImpl extends ServiceImpl<TworderitemMapper, Tworderitem> implements TworderitemServiceI {
    @Autowired
    TworderitemMapper tworderitemMapper;
    @Autowired
    TworderMapper tworderMapper;
    @Autowired
    TwdebtreasonMapper twdebtreasonMapper;
    @Autowired
    TworderapportiondetailMapper tworderapportiondetailMapper;
    @Autowired
    TwpreinvoiceapplicationMapper twpreinvoiceapplicationMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Resource
    private TwmessageServiceI messageService;
    @Autowired
    private TworderitembelongServiceI orderItembelongServiceI;
    @Resource
    private IProcessInstanceService processInstanceService;
    @Autowired
    private TwapplicationrelationsServiceI applicationrelationsServiceI;
    @Autowired
    private TbmediaServiceI mediaServiceI;
    @Autowired
    private CommissionServiceI commissionServiceI;

    @Autowired
    TwcustomerchargeServiceI twcustomerchargeServiceI;
    @Autowired
    private TbdeptServiceI deptServiceI;
    @Autowired
    private TbadindustryServiceI tbadindustryServiceI;
    @Autowired
    private TbadtypeServiceI tbadtypeServiceI;
    @Autowired
    private TworderServiceI tworderService;
    @Autowired
    private TwadprojectServiceI twadprojectServiceI;
    @Autowired
    TwadresourceapplicationServiceI twadresourceapplicationServiceI;
    @Autowired
    private TwinvoiceMapper invoiceMapper;
    @Autowired
    TbadspecServiceI tbadspecServiceI;
    @Autowired
    TbmediapublicnumServiceI tbmediapublicnumServiceI;
    @Autowired
    OrderApportiondetailServiceI orderApportiondetailServiceI;

    @Override
    public IPage<OrderDebtDTO> getOrderDebtPageList(Page<Tworderitem> page, DebtReasonVO query) throws Exception {
        LambdaQueryWrapper<Tworderitem> lqw = Wrappers.lambdaQuery();
        // 已见报、已发布
//        lqw.and(item -> item.eq(Tworderitem::getIpublishstatus, PublishStatus.PUBLISH_End.getKey())
//                .or()
//                .eq(Tworderitem::getIpublishstatus, PublishStatus.PUBLISH_Published.getKey()));
        lqw.eq(Tworderitem::getIpublishstatus, PublishStatus.PUBLISH_Published.getKey());
        // 欠款金额>0
        lqw.gt(Tworderitem::getAmountarrearage, 0);
        Long deptid = query.getDeptid() != null ? query.getDeptid() : 0L;
        Date StartTime = query.getStartTime() != null ? query.getStartTime() : new Date();
        Date EndTime = query.getEndTime() != null ? DateUtil.offsetDay(query.getEndTime(), 1) : new Date();
        lqw.and(query.getDeptid() != null, i -> i.inSql(Tworderitem::getOrderid,
                ("select orderid from tworder where deptid = " + deptid + " and buse = 1 and bdelete = 0 " +
                        "and createtime >= '" + DateUtil.format(StartTime, "yyyy-MM-dd") +
                        "' and createtime < '" + DateUtil.format(EndTime, "yyyy-MM-dd") + "'")));
        lqw.ge(query.getStartTime() != null, Tworderitem::getCreatetime, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Tworderitem::getCreatetime, EndTime);
        }
        // 20240307 suny 是否填报欠款原因(是否推送) 默认0 否 1是
        lqw.eq(query.getBreportreason() != null && query.getBreportreason(), Tworderitem::getBreportreason, query.getBreportreason());
        // 20240307 suny 合同号
        lqw.eq(query.getScontractnum() != null, Tworderitem::getScontractnum, query.getScontractnum());
        // 20240307 suny 关键字
        lqw.inSql(StrUtil.isNotBlank(query.getQueryKey()), Tworderitem::getOrderid,
                "select id from tworder where customername like '%" + query.getQueryKey() + "%' " +
                        "or agencyname like '%" + query.getQueryKey() + "%' or agentname like '%" + query.getQueryKey() + "%'");
        // 20240307 suny 发票号
        lqw.inSql(query.getInvoice() != null, Tworderitem::getOrderid,
                "SELECT b.orderid FROM twpreinvoiceapplication a INNER JOIN twpreinvoiceapplicationdetail b on a.id=b.preinvoiceapplicationid WHERE a.invoice = '" + query.getInvoice() + "'");
        lqw.orderByDesc(Tworderitem::getCreatetime);
        IPage<Tworderitem> pages = tworderitemMapper.selectPage(page, lqw);
        IPage<OrderDebtDTO> resulepage = new Page<>();
        List<OrderDebtDTO> result = new ArrayList<>();
        for (Tworderitem record : pages.getRecords()) {
            OrderDebtDTO orderDebtDTO = new OrderDebtDTO();
            Tworder tworder = tworderMapper.selectById(record.getOrderid());
            BeanUtils.copyProperties(tworder, orderDebtDTO);
            BeanUtils.copyProperties(record, orderDebtDTO);
            orderDebtDTO.setOrderitemid(record.getId());
            LambdaQueryWrapper<Twdebtreason> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Twdebtreason::getOrderitemid, record.getId())
                    .eq(query.getBfinance() != null && query.getBfinance(), Twdebtreason::getBconfirm, true); // 财务人员已确认的数据才能被查询到
            List<Twdebtreason> twdebtreasonList = twdebtreasonMapper.selectList(queryWrapper);
            if (twdebtreasonList.size() > 0) {
                orderDebtDTO.setSnoticecontent(twdebtreasonList.get(0).getSnoticecontent());
                orderDebtDTO.setSdebtreason(twdebtreasonList.get(0).getSdebtreason());
                orderDebtDTO.setScategory(twdebtreasonList.get(0).getScategory());
                orderDebtDTO.setSrepaymentdate(twdebtreasonList.get(0).getSrepaymentdate());
                orderDebtDTO.setSrisklevel(twdebtreasonList.get(0).getSrisklevel());
                orderDebtDTO.setBlegal(twdebtreasonList.get(0).getBlegal());
                orderDebtDTO.setDebtreasonid(twdebtreasonList.get(0).getId()); // 欠款原因表id
                orderDebtDTO.setBconfirm(twdebtreasonList.get(0).getBconfirm());
            }
            // 发票相关信息
            LambdaQueryWrapper<Twpreinvoiceapplication> lqw1 = Wrappers.lambdaQuery();
            lqw1.inSql(Twpreinvoiceapplication::getId, "select preinvoiceapplicationid from " +
                    "twpreinvoiceapplicationdetail where orderid = " + tworder.getId());
            List<Twpreinvoiceapplication> twpreinvoiceapplicationList = twpreinvoiceapplicationMapper.selectList(lqw1);
            List<String> invoiceList = new ArrayList<>();
            BigDecimal namountinvoice = BigDecimal.ZERO;
            for (Twpreinvoiceapplication twpreinvoiceapplication : twpreinvoiceapplicationList) {
                namountinvoice = namountinvoice.add(twpreinvoiceapplication.getNamountapply());
                if (StringUtils.isNotEmpty(twpreinvoiceapplication.getInvoice())) {
                    invoiceList.add(twpreinvoiceapplication.getInvoice());
                }
            }
            //---------------发票金额------------------
            orderDebtDTO.setNapplyamount(namountinvoice);
            orderDebtDTO.setInvoice(invoiceList.size() > 0 ? String.join(",", invoiceList) : null);
            result.add(orderDebtDTO);
        }
        resulepage.setRecords(result);
        resulepage.setTotal(pages.getTotal());
        return resulepage;
    }

    @Override
    public void pushOrderDebt(String ids) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            Tworderitem tworderitem = tworderitemMapper.selectById(id);
            Tworder tworder = tworderMapper.selectById(tworderitem.getOrderid());
            LambdaQueryWrapper<Twdebtreason> lambdaQuery = Wrappers.lambdaQuery();
            lambdaQuery.eq(Twdebtreason::getOrderitemid, tworderitem.getId());
            lambdaQuery.in(Twdebtreason::getScontractnum, tworder.getScontractnum());
            Long pagesorts = twdebtreasonMapper.selectCount(lambdaQuery);
            if (pagesorts > 0) {
                throw new DataExistException("合同 " + tworder.getScontractnum() + " 已存在催款记录");
            }
            Twdebtreason twdebtreason = new Twdebtreason();
            // 订单相关信息
            twdebtreason.setDeptid(tworder.getDeptid());
            twdebtreason.setDeptname(tworder.getDeptname());
            twdebtreason.setEmployid(tworder.getEmployid());
            twdebtreason.setEmployname(tworder.getEmployname());
            twdebtreason.setAgencyemployid(tworder.getAgencyemployid());
            twdebtreason.setAgencyemployname(tworder.getAgencyemployname());
            twdebtreason.setAgentemployid(tworder.getAgentemployid());
            twdebtreason.setAgentemployname(tworder.getAgentemployname());
            twdebtreason.setCustomername(tworder.getCustomername());
            twdebtreason.setAgencyname(tworder.getAgencyname());
            twdebtreason.setAgentname(tworder.getAgentname());
            twdebtreason.setOrderid(tworder.getId());
            // 推送信息
            twdebtreason.setCreateempid(user.getUserid());
            twdebtreason.setCreateempname(user.getUsername());
            twdebtreason.setCreatedate(new Date());
            // 刊期相关信息
            twdebtreason.setOrderitemid(tworderitem.getId());
            twdebtreason.setScontractnum(tworderitem.getScontractnum());
            twdebtreason.setDpublishdate(tworderitem.getPrestartdate()); // 刊期开始日期
            twdebtreason.setSadtitle(tworderitem.getSadtitle());
            twdebtreason.setAmountreceivable(tworderitem.getAmountreceivable());
            twdebtreason.setAmountreceived(tworderitem.getAmountreceived());
            twdebtreason.setAmountarrearage(tworderitem.getAmountarrearage());
            // 发票相关信息
            LambdaQueryWrapper<Twpreinvoiceapplication> lqw = Wrappers.lambdaQuery();
            lqw.inSql(Twpreinvoiceapplication::getId, "select preinvoiceapplicationid from " +
                    "twpreinvoiceapplicationdetail where orderid = " + tworder.getId());
            List<Twpreinvoiceapplication> twpreinvoiceapplicationList = twpreinvoiceapplicationMapper.selectList(lqw);
            List<String> invoiceList = new ArrayList<>();
            BigDecimal namountinvoice = BigDecimal.ZERO;
            for (Twpreinvoiceapplication twpreinvoiceapplication : twpreinvoiceapplicationList) {
                namountinvoice = namountinvoice.add(twpreinvoiceapplication.getNamountapply());
                if (StringUtils.isNotEmpty(twpreinvoiceapplication.getInvoice())) {
                    invoiceList.add(twpreinvoiceapplication.getInvoice());
                }
            }
            //---------------发票金额------------------
            twdebtreason.setNamountinvoice(namountinvoice);
            twdebtreason.setInvoice(invoiceList.size() > 0 ? String.join(",", invoiceList) : null);
            twdebtreason.setVersion(0l);
            twdebtreason.setBlegal(false);
            twdebtreason.setBconfirm(false);
            innerInterceptor.recoredLog();
            twdebtreasonMapper.insert(twdebtreason);

            tworderitem.setBreportreason(true);
            innerInterceptor.recoredLog();
            tworderitemMapper.updateById(tworderitem);

            // 给业务员发送消息
            if (tworder.getEmployid() != null) {
                // 主业务员
                MessageDto messageDto = MessageDto.builder()
                        .readed(false)
                        .userId(tworder.getEmployid().toString()).build();
                String content = StrUtil.format("您有一条欠款推送，合同号：【{}】，签发时间：【{}】",
                        tworderitem.getScontractnum(),
                        DateUtil.formatDateTime(tworderitem.getPrestartdate()));
                String title = StrUtil.format("您有一条欠款推送需要处理【{}】", tworderitem.getScontractnum());

                messageDto.setTitle(title);
                messageDto.setContent(content);
                messageDto.setType(MessageTypeEnum.TODO_DEBTREASON.getType());
                messageDto.setProcessInstanceCreate(tworderitem.getPrestartdate());
                messageService.saveMessage(messageDto);
            }
            if (tworder.getAgencyemployid() != null) {
                // 给代理公司业务员发送消息
                MessageDto messageDto_agency = MessageDto.builder()
                        .readed(false)
                        .userId(tworder.getAgencyemployid().toString()).build();
                String content_agency = StrUtil.format("您有一条欠款推送，合同号：【{}】，签发时间：【{}】",
                        tworderitem.getScontractnum(),
                        DateUtil.formatDateTime(tworderitem.getPrestartdate()));
                String title_agency = StrUtil.format("您有一条欠款推送需要处理【{}】", tworderitem.getScontractnum());

                messageDto_agency.setTitle(title_agency);
                messageDto_agency.setContent(content_agency);
                messageDto_agency.setType(MessageTypeEnum.TODO_DEBTREASON.getType());
                messageDto_agency.setProcessInstanceCreate(tworderitem.getPrestartdate());
                messageService.saveMessage(messageDto_agency);
            }
            if (tworder.getAgentemployid() != null) {
                // 给内容生产方业务员发送消息
                MessageDto messageDto_agent = MessageDto.builder()
                        .readed(false)
                        .userId(tworder.getAgentemployid().toString()).build();
                String content_agent = StrUtil.format("您有一条欠款推送，合同号：【{}】，签发时间：【{}】",
                        tworderitem.getScontractnum(),
                        DateUtil.formatDateTime(tworderitem.getPrestartdate()));
                String title_agent = StrUtil.format("您有一条欠款推送需要处理【{}】", tworderitem.getScontractnum());

                messageDto_agent.setTitle(title_agent);
                messageDto_agent.setContent(content_agent);
                messageDto_agent.setType(MessageTypeEnum.TODO_DEBTREASON.getType());
                messageDto_agent.setProcessInstanceCreate(tworderitem.getPrestartdate());
                messageService.saveMessage(messageDto_agent);
            }
        }
    }

    @Override
    public List<Tworderitem> getOrderItemByOrderId(String orderId) throws Exception {
        List<Tworderitem> orderitem = tworderitemMapper.selectList(new LambdaQueryWrapper<>(Tworderitem.class)
                .eq(Tworderitem::getOrderid, orderId)
                .eq(Tworderitem::getBdelete, false)
                .orderByDesc(Tworderitem::getPrestartdate));
        //.orderByAsc(Tworderitem::getIitemcode));
        return orderitem;
    }

    @Override
    public Json saveOrderItem(Tworderitem orderItem) throws Exception {
        if (orderItem.getMediatypekey() == null) {
            //媒体类型
            Tbmedia tbmedia = mediaServiceI.getById(orderItem.getMediaid());
            if (tbmedia != null) {
                orderItem.setMediatypekey(tbmedia.getMediatypekey());
                orderItem.setMediatypename(tbmedia.getMediatypename());
            }
        }
        innerInterceptor.recoredLog();
        save(orderItem);
        return Json.success();
    }

    @Override
    public Json updateOrderItem(Tworderitem orderItem) throws Exception {
        if (orderItem.getMediatypekey() == null) {
            //媒体类型
            Tbmedia tbmedia = mediaServiceI.getById(orderItem.getMediaid());
            if (tbmedia != null) {
                orderItem.setMediatypekey(tbmedia.getMediatypekey());
                orderItem.setMediatypename(tbmedia.getMediatypename());
            }
        }
        innerInterceptor.recoredLog();
        if (tworderitemMapper.updateById(orderItem) == 0) {
            throw new OptimisticLockingFailureException("订单明细修改失败");
        }
        return Json.success();
    }

    @Override
    public Json deleteOrderItemById(String ids) throws Exception {
        String[] split = ids.split(",");
        List<Tworderitem> lsOrderItems = this.lambdaQuery()
                .in(Tworderitem::getId, split)
                .list();
        List<String> orderIds =
                lsOrderItems.stream().map(w -> String.valueOf(w.getId())).distinct().collect(Collectors.toList());
        int iCount = 0;
        String sInfo = "";
        if (lsOrderItems.size() > 0) {
            innerInterceptor.recoredLog();
            for (Tworderitem item : lsOrderItems) {
                if (item.getIpublishstatus() == 1) {
                    sInfo += item.getFoldname() + item.getFoldareavername() + " 在审,订单不能删除";
                }
                // 删除订单明细归属
                orderItembelongServiceI.deleteOrderItemBelong(item.getId().toString());

                item.setBdelete(true);
                item.setBuse(false);
                if (tworderitemMapper.updateById(item) == 0) {
                    throw new OptimisticLockingFailureException("订单明细删除失败");
                }
                iCount++;
            }
        }
        //整理订单状态
        for (String itemid : orderIds) {
            updateOrderStatus(itemid);
        }
        if (iCount == lsOrderItems.size()) {
            return Json.success();
        } else {
            return Json.fail(sInfo);
        }
    }

    @Override
    public Json deleteOrderItem(String orderids) throws Exception {
        String[] split = orderids.split(",");
        List<Tworderitem> lsOrderItems = this.lambdaQuery()
                .in(Tworderitem::getOrderid, split)
                .list();
        int iCount = 0;
        String sInfo = "";
        if (lsOrderItems.size() > 0) {
            for (Tworderitem item : lsOrderItems) {
                if (item.getIpublishstatus() == 1) {
                    sInfo += item.getFoldname() + item.getFoldareavername() + " 在审,订单不能删除";
                }
                // 删除订单明细归属
                orderItembelongServiceI.deleteOrderItemBelong(item.getId().toString());
                // 删除订单明细
                item.setBdelete(true);
                item.setBuse(false);
                innerInterceptor.recoredLog();
                if (tworderitemMapper.updateById(item) == 0) {
                    throw new OptimisticLockingFailureException("订单明细删除失败");
                }
                iCount++;
            }
        }

        if (iCount == lsOrderItems.size()) {
            return Json.success();
        } else {
            return Json.fail(sInfo);
        }
    }


    /**
     * 取得所有订单id对应的总欠款金额
     * 方法功能:对所有订单id，取得关联订单刊期（orderItem），计算总欠款金额；仅计算“未被删除 & 启用/有效”的
     *
     * @param orderIds
     * @return java.math.BigDecimal
     * @author yanz
     * @date 2023/12/8 15:55
     */
    @Override
    public BigDecimal getArrearagesSumByOrderIds(List<Long> orderIds) {
        Map<Long, BigDecimal> arrearagesSumMap = getArrearagesSumMapByOrderIds(orderIds);
        if (arrearagesSumMap == null) {
            return null;
        }
        BigDecimal sum = arrearagesSumMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum;
    }

    /**
     * 取得订单id对应的总欠款金额Map
     * 方法功能:取得订单id对应的总欠款金额Map，key是订单id，value是总欠款金额；订单id与合同号1对1；仅计算“未被删除 & 启用/有效”的
     *
     * @param orderIds
     * @return java.util.Map<java.lang.String, java.math.BigDecimal>
     * @author yanz
     * @date 2023/11/30 16:22
     */
    @Override
    public Map<Long, BigDecimal> getArrearagesSumMapByOrderIds(List<Long> orderIds) {
        if (ObjUtil.isEmpty(orderIds)) {
            return null;
        }
        // 取得订单id对应的所有刊期 - 限 启用 & 未删除 & 已审批通过
        List<Tworderitem> tworderitems = this.lambdaQuery()
                .in(Tworderitem::getOrderid, orderIds)
                .eq(Tworderitem::getBuse, true)
                .eq(Tworderitem::getBdelete, false)
                .eq(Tworderitem::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                .list();


        // 计算总欠款金额
        ConcurrentMap<Long, BigDecimal> map = tworderitems.stream()
                .filter(Tworderitem::getBuse)
                .collect(Collectors.toConcurrentMap(
                        Tworderitem::getOrderid,
                        item -> item.getAmountarrearage() != null ? item.getAmountarrearage() : BigDecimal.ZERO,
                        BigDecimal::add
                ));

        for (Long orderid : orderIds) {
            map.putIfAbsent(orderid, BigDecimal.ZERO);
        }
        return map;
    }

    /**
     * 订单（order）对象转订单VO（预开发票用）
     * 方法功能:订单（order）对象转订单VO（预开发票用）
     *
     * @param orders
     * @return java.util.List<com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO>
     * @author yanz
     * @date 2023/12/9 9:09
     */
    @Override
    public List<OrderforPreinvoapplyVO> getOrderforPreinvoapplyVOS(List<Tworder> orders) {
        List<Long> orderIds = orders.stream().map(Tworder::getId).collect(Collectors.toList());
        Map<Long, BigDecimal> arrearageSumsMap = getArrearagesSumMapByOrderIds(orderIds);
        if (ObjUtil.isEmpty(orderIds) || ObjUtil.isEmpty(arrearageSumsMap)) {
            return null;
        }

        List<OrderforPreinvoapplyVO> orderVOs = orders.stream()
                .filter(Objects::nonNull)
                .map(order -> {
                    OrderforPreinvoapplyVO orderforPreinvoapplyVO = new OrderforPreinvoapplyVO();
                    BeanUtils.copyProperties(order, orderforPreinvoapplyVO);
                    orderforPreinvoapplyVO.setOrderid(order.getId());
                    orderforPreinvoapplyVO.setAdindustryid(order.getAdindustyid());
                    orderforPreinvoapplyVO.setAmountarrearage(arrearageSumsMap.get(order.getId()));
                    return orderforPreinvoapplyVO;
                })
                .collect(Collectors.toList());
        return orderVOs;
    }

    @Override
    public OrderDebtDTO getOrderDebtInfo(String orderid) throws Exception {
        OrderDebtDTO orderDebtDTO = new OrderDebtDTO();
        Tworder tworder = tworderMapper.selectById(orderid);
        if (tworder == null) {
            return null;
        }

        BeanUtils.copyProperties(tworder, orderDebtDTO);

        List<Tworderitem> tworderitemList = this.lambdaQuery()
                .eq(Tworderitem::getOrderid, orderid)
                .eq(Tworderitem::getBuse, 1)
                .eq(Tworderitem::getBdelete, 0)
                .eq(Tworderitem::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                .list();
        BigDecimal amountreceivable =
                tworderitemList.stream().map(Tworderitem::getAmountreceivable).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal amountarrearage =
                tworderitemList.stream().map(Tworderitem::getAmountarrearage).reduce(BigDecimal.ZERO, BigDecimal::add);
        // TODO 当前订单已分配金额总数
        BigDecimal invoiceAllocate = twcustomerchargeServiceI.getCustomerChargeByInvoiceId(null, tworder.getId());
        // TODO 如果应收金额小于等于已分配金额则不显示
        if (invoiceAllocate != null && amountreceivable.compareTo(invoiceAllocate) <= 0) {
            return null;
        }
        orderDebtDTO.setAmountreceivable(amountreceivable);
        orderDebtDTO.setAmountarrearage(amountarrearage);

        return orderDebtDTO;
    }

    //订单审批
    @Override
    public Json<String> approveOrderItem(String orderItemId, String flowId, Integer iapproveType) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            Tworderitem tworderitem = this.getById(orderItemId);
            String sOpinion = "订单";
            if (iapproveType == ApproveType.APPROVE_Add.getKey()) {
                sOpinion = ApproveType.APPROVE_Add.name;
            }
            if (iapproveType == ApproveType.APPROVE_Change.getKey()) {
                sOpinion = ApproveType.APPROVE_Change.name;
            }
            if (iapproveType == ApproveType.APPROVE_Stop.getKey()) {
                sOpinion = ApproveType.APPROVE_Stop.name;
            }
            if (iapproveType == ApproveType.APPROVE_Pre.getKey()) {
                sOpinion = ApproveType.APPROVE_Pre.name;
            }
            if (iapproveType == ApproveType.APPROVE_Discount.getKey()) {
                sOpinion = ApproveType.APPROVE_Discount.name;
            }
            // 申请审批
            if (StrUtil.isNotBlank(flowId)) {
                //获取流程条件
                Map<String, Object> flowParamMap = new HashMap<>();
                flowParamMap.put("businessId", orderItemId);
                flowParamMap.put("businessName", "[" + tworderitem.getScontractnum() + "]" + tworderitem.getSadtitle());
                // flowParamMap.put("approvetype", iapproveType);
                flowParamMap.put("deptId", user.getDeptid().toString());
                return processInstanceService.startProcessInstanceByFlowTypes(flowId, flowParamMap,
                        FlowTypes.FLOW_ORDER);
            }
        } catch (Exception e) {
            return Json.fail("审批申请失败" + e.getMessage());
        }
        return Json.fail("流程id为空");
    }

    @Override
    public Json updateOrderItemApprovalopinions(String applicationid, String sOrderItemId, boolean bUpdateopinion,
                                                String approveDesc, Integer iapproveType, Integer iapproveStatus) {
        try {
            LoginUser user = WebUtil.getLoginUser();
            Tworderitem orderitemInfo = this.getById(sOrderItemId);
            if (orderitemInfo == null) {
                return Json.fail("未找到订单信息！");
            }
            String sOpinion = "订单";
            if (iapproveType == ApproveType.APPROVE_Add.getKey()) {
                orderitemInfo.setIaddapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Add.name;
            }
            if (iapproveType == ApproveType.APPROVE_Change.getKey()) {
                orderitemInfo.setIchangeapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Change.name;
            }
            if (iapproveType == ApproveType.APPROVE_Stop.getKey()) {
                orderitemInfo.setIstopapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Stop.name;
            }
            if (iapproveType == ApproveType.APPROVE_Discount.getKey()) {
                orderitemInfo.setIdiscountapprovestatus(iapproveStatus);
                sOpinion = ApproveType.APPROVE_Discount.name;
            }
            //更新订单审批状态
            if (iapproveStatus == ApproveStatus.APPROVE_PASS.getKey()) {
                if (orderitemInfo.getIaddapprovestatus() == ApproveStatus.APPROVE_EDITING.getKey() ||
                        orderitemInfo.getIchangeapprovestatus() == ApproveStatus.APPROVE_EDITING.getKey() ||
                        orderitemInfo.getIstopapprovestatus() == ApproveStatus.APPROVE_EDITING.getKey() ||
                        orderitemInfo.getIdiscountapprovestatus() == ApproveStatus.APPROVE_EDITING.getKey()) {
                    orderitemInfo.setIapprovestatus(ApproveStatus.APPROVE_EDITING.getKey());
                } else if (orderitemInfo.getIaddapprovestatus() == ApproveStatus.APPROVE_REJECT.getKey() ||
                        orderitemInfo.getIchangeapprovestatus() == ApproveStatus.APPROVE_REJECT.getKey() ||
                        orderitemInfo.getIstopapprovestatus() == ApproveStatus.APPROVE_REJECT.getKey() ||
                        orderitemInfo.getIdiscountapprovestatus() == ApproveStatus.APPROVE_REJECT.getKey()) {
                    orderitemInfo.setIapprovestatus(ApproveStatus.APPROVE_REJECT.getKey());
                } else if (orderitemInfo.getIaddapprovestatus() == ApproveStatus.APPROVE_PASS.getKey() &&
                        orderitemInfo.getIchangeapprovestatus() == ApproveStatus.APPROVE_PASS.getKey() &&
                        orderitemInfo.getIstopapprovestatus() == ApproveStatus.APPROVE_PASS.getKey() &&
                        orderitemInfo.getIdiscountapprovestatus() == ApproveStatus.APPROVE_PASS.getKey()) {
                    orderitemInfo.setIapprovestatus(ApproveStatus.APPROVE_PASS.getKey());
                } else {
                    orderitemInfo.setIapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
                }
            } else {
                orderitemInfo.setIapprovestatus(iapproveStatus);
            }

            innerInterceptor.recoredLog();
            if (tworderitemMapper.updateById(orderitemInfo) == 0) {
                throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
            }
            //更新审批流程关联表
            if (StrUtil.isNotBlank(applicationid)) {
                TwapplicationrelationsDTO applicationrelationsDTO = new TwapplicationrelationsDTO();
                applicationrelationsDTO.setApplicationid(applicationid);
                applicationrelationsDTO.setMainid(orderitemInfo.getOrderid());
                applicationrelationsDTO.setChildid(orderitemInfo.getId());
                applicationrelationsDTO.setFlowtype(FlowTypes.FLOW_ORDER.getKey());

                List<TwapplicationrelationsVO> lsApplicationRelations =
                        applicationrelationsServiceI.getApplicationRelationsList(applicationrelationsDTO);
                if (lsApplicationRelations.size() > 0) {
                    for (Twapplicationrelations item : lsApplicationRelations) {
                        item.setIapprovestatus(iapproveStatus);
                        if (bUpdateopinion) {
                            item.setSapprovalopinions(sOpinion + "审核意见:" + approveDesc);
                        }
                        applicationrelationsServiceI.updateById(item);
                    }
                }
            }

            //更新订单状态
            updateOrderStatus(orderitemInfo.getOrderid().toString());

            return Json.success("操作成功！");
        } catch (Exception e) {
            return Json.fail("操作失败！ " + e.getMessage());
        }
    }

    //更新订单状态：明细状态中有在审的，那么订单对应状态为在审；订单总状态为在审
    @Override
    public void updateOrderStatus(String sOrderId) {
        Tworder tworder = tworderMapper.selectById(sOrderId);
        List<Tworderitem> lsItems = this.lambdaQuery()
                .eq(Tworderitem::getOrderid, sOrderId)
                .list();
        if (lsItems.size() > 0) {
            //加刊在审状态
            boolean bEditingExist =
                    lsItems.stream().anyMatch(i -> i.getIaddapprovestatus().equals(ApproveStatus.APPROVE_EDITING.getKey()));
            //拒绝状态
            boolean bRejectExist =
                    lsItems.stream().anyMatch(i -> i.getIaddapprovestatus().equals(ApproveStatus.APPROVE_REJECT.getKey()));
            int iPassCount =
                    lsItems.stream().filter(s -> s.getIaddapprovestatus().equals(ApproveStatus.APPROVE_PASS.getKey())).collect(Collectors.toList()).size();
            if (bEditingExist) {
                tworder.setIaddapprovestatus(ApproveStatus.APPROVE_EDITING.getKey());
            } else if (bRejectExist) {
                tworder.setIaddapprovestatus(ApproveStatus.APPROVE_REJECT.getKey());
            } else if (iPassCount == lsItems.size()) {
                tworder.setIaddapprovestatus(ApproveStatus.APPROVE_PASS.getKey());
            } else {
                tworder.setIaddapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
            }
            //改刊状态
            bEditingExist =
                    lsItems.stream().anyMatch(i -> i.getIchangeapprovestatus().equals(ApproveStatus.APPROVE_EDITING.getKey()));
            bRejectExist =
                    lsItems.stream().anyMatch(i -> i.getIchangeapprovestatus().equals(ApproveStatus.APPROVE_REJECT.getKey()));
            iPassCount =
                    lsItems.stream().filter(s -> s.getIchangeapprovestatus().equals(ApproveStatus.APPROVE_PASS.getKey())).collect(Collectors.toList()).size();
            if (bEditingExist) {
                tworder.setIchangeapprovestatus(ApproveStatus.APPROVE_EDITING.getKey());
            } else if (bRejectExist) {
                tworder.setIchangeapprovestatus(ApproveStatus.APPROVE_REJECT.getKey());
            } else if (iPassCount == lsItems.size()) {
                tworder.setIchangeapprovestatus(ApproveStatus.APPROVE_PASS.getKey());
            } else {
                tworder.setIchangeapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
            }
            //停刊状态
            bEditingExist =
                    lsItems.stream().anyMatch(i -> i.getIstopapprovestatus().equals(ApproveStatus.APPROVE_EDITING.getKey()));
            bRejectExist =
                    lsItems.stream().anyMatch(i -> i.getIstopapprovestatus().equals(ApproveStatus.APPROVE_REJECT.getKey()));
            iPassCount =
                    lsItems.stream().filter(s -> s.getIstopapprovestatus().equals(ApproveStatus.APPROVE_PASS.getKey())).collect(Collectors.toList()).size();
            if (bEditingExist) {
                tworder.setIstopapprovestatus(ApproveStatus.APPROVE_EDITING.getKey());
            } else if (bRejectExist) {
                tworder.setIstopapprovestatus(ApproveStatus.APPROVE_REJECT.getKey());
            } else if (iPassCount == lsItems.size()) {
                tworder.setIstopapprovestatus(ApproveStatus.APPROVE_PASS.getKey());
            } else {
                tworder.setIstopapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
            }
            //折扣审批状态
            bEditingExist =
                    lsItems.stream().anyMatch(i -> i.getIdiscountapprovestatus().equals(ApproveStatus.APPROVE_EDITING.getKey()));
            bRejectExist =
                    lsItems.stream().anyMatch(i -> i.getIdiscountapprovestatus().equals(ApproveStatus.APPROVE_REJECT.getKey()));
            iPassCount =
                    lsItems.stream().filter(s -> s.getIdiscountapprovestatus().equals(ApproveStatus.APPROVE_PASS.getKey())).collect(Collectors.toList()).size();
            if (bEditingExist) {
                tworder.setIdiscountapprovestatus(ApproveStatus.APPROVE_EDITING.getKey());
            } else if (bRejectExist) {
                tworder.setIdiscountapprovestatus(ApproveStatus.APPROVE_REJECT.getKey());
            } else if (iPassCount == lsItems.size()) {
                tworder.setIdiscountapprovestatus(ApproveStatus.APPROVE_PASS.getKey());
            } else {
                tworder.setIdiscountapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
            }
            //订单状态
            bEditingExist =
                    lsItems.stream().anyMatch(i -> i.getIapprovestatus().equals(ApproveStatus.APPROVE_EDITING.getKey()));
            bRejectExist =
                    lsItems.stream().anyMatch(i -> i.getIapprovestatus().equals(ApproveStatus.APPROVE_REJECT.getKey()));
            iPassCount =
                    lsItems.stream().filter(s -> s.getIapprovestatus().equals(ApproveStatus.APPROVE_PASS.getKey())).collect(Collectors.toList()).size();
            if (bEditingExist) {
                tworder.setIapprovestatus(ApproveStatus.APPROVE_EDITING.getKey());
            } else if (bRejectExist) {
                tworder.setIapprovestatus(ApproveStatus.APPROVE_REJECT.getKey());
            } else if (iPassCount == lsItems.size()) {
                tworder.setIapprovestatus(ApproveStatus.APPROVE_PASS.getKey());
            } else {
                tworder.setIapprovestatus(ApproveStatus.APPROVE_EDIT.getKey());
            }

            innerInterceptor.recoredLog();
            if (tworderMapper.updateById(tworder) == 0) {
                throw new OptimisticLockingFailureException("数据已改变，请刷新后再重新操作！");
            }
        }

    }

    /**
     * 据客户id，查询所有有欠款的订单的广告明细
     * 方法功能:据客户id，查询所有有欠款的订单的广告明细。限制：有欠款、“审核状态”通过；可选限制：刊发时间(预见报开始日期)或合同号
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2023/12/25 14:23
     */
    @Override
    public IPage<OrderAndItemDTO> getOrderAndItemByCustomerid(Page<OrderAndItemDTO> page, CustomerChargeBankVO query) {
        //按照刊发时间(预见报开始日期- 查TworderItem表)或者合同号（可选限制），根据传递的客户id，查询订单表中的直接客户、代理公司或者内容生产方包customerid的所有 有欠款 的订单的广告明细（包含以下字段）
        LambdaQueryWrapper<Tworder> orderQuery = Wrappers.lambdaQuery();
        //合同号、经营主体、直接客户、代理公司、内容生产方、刊发日期 - 查Tworder表
        orderQuery.and(i -> i.eq(Tworder::getCustomerid, query.getCustomerid())
                        .or().eq(Tworder::getAgencytid, query.getCustomerid())
                        .or().eq(Tworder::getAgentid, query.getCustomerid()))
                // 合同号
                .eq(StrUtil.isNotBlank(query.getQueryKey()), Tworder::getScontractnum, query.getQueryKey())
                // 订单已通过
                .eq(Tworder::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey());
        List<Tworder> orders = tworderMapper.selectList(orderQuery);

        List<OrderAndItemDTO> result = new ArrayList<>();
        for (Tworder order : orders) {
            // 媒体、广告标题、应收金额、已收金额、欠款金额 - 查TworderItem表
            LambdaQueryWrapper<Tworderitem> itemQuery = Wrappers.lambdaQuery();
            itemQuery.eq(Tworderitem::getOrderid, order.getId())
                    // 有欠款
                    .gt(Tworderitem::getAmountarrearage, 0)
                    // 时间用的都是	startTime endTime，不会用到数据表中的字段 - suny 12-25 14:08:09
                    .ge(ObjUtil.isNotNull(query.getStartTime()), Tworderitem::getPrestartdate, query.getStartTime());
            List<Tworderitem> items = tworderitemMapper.selectList(itemQuery);
            for (Tworderitem item : items) {
                OrderAndItemDTO dto = OrderAndItemDTO.from(order, item);
                result.add(dto);
            }
        }
        // 结果
        Page<OrderAndItemDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(page, dtoPage);
        dtoPage.setRecords(result);
        dtoPage.setTotal(page.getTotal());
        return dtoPage;
    }

    @Override
    public Long getMaxItemCode() {
        QueryWrapper<Tworderitem> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("max(iitemcode) as maxItemcode");
        List<Map<String, Object>> mapList = baseMapper.selectMaps(queryWrapper);
        Optional<Map<String, Object>> opMap = mapList.stream().filter(m -> null != m).findFirst();
        Long iitemcode = opMap.isPresent() ? (Long.parseLong(opMap.get().get("maxItemcode").toString()) + 1) : 1;
        return iitemcode;
    }

    /**
     * 查询业绩列表（业绩金额）
     * 方法功能:列表查询tworderitem表。
     * 条件：时间范围、媒体、刊发状态（iapprovestatus=2通过且buse=1有效）
     * 返回：订单明细相关综合对象（OrderAndItemDTO）
     * 注：以orderitem 订单刊期/订单详情 数据为主
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/1/10 16:13
     */
    @Override
    public IPage<OrderAndItemDTO> getPerformanceRatio(Page<Tworderitem> page, OrderAndItemVO query) {
        // 条件：时间范围、媒体、刊发状态（iapprovestatus=2通过且buse=1有效）
        LambdaQueryWrapper<Tworderitem> lqw = Wrappers.lambdaQuery();
        // 日期限制
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Tworderitem::getCreatetime, query.getStartTime());
        if (ObjUtil.isNotNull(query.getEndTime())) {
            lqw.lt(Tworderitem::getCreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        // 媒体
        lqw.eq(query.getMediaid() != null, Tworderitem::getMediaid, query.getMediaid());
        // 刊发状态
        lqw.eq(Tworderitem::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                .eq(Tworderitem::getBuse, true);

        Page<Tworderitem> paged = this.page(page, lqw);
        Map<Long, List<Tworderitem>> orderidToOrderItem = paged.getRecords().stream().collect(Collectors.groupingBy(Tworderitem::getOrderid));
        if (CollUtil.isEmpty(orderidToOrderItem)) {
            return new Page<>();
        }
        List<Long> orderids = orderidToOrderItem.keySet().stream().collect(Collectors.toList());
        Map<Long, Tworder> orderidToOrder = tworderMapper.selectBatchIds(orderids).stream()
                .collect(Collectors.toMap(Tworder::getId, Function.identity()));

        // 返回：订单明细相关综合对象（OrderAndItemDTO）
        // 字段：合同编号、广告号、经营主体、直接客户、业务员、代理公司、内容生成、
        // 刊发日期、刊发状态、广告标题、叠次、叠次版本、版面类别、广告形式、广告规格、应收金额、已收金额、
        // 欠款金额、业绩金额、成本金额、可编辑状态

        List<OrderAndItemDTO> result = orderidToOrderItem.entrySet().stream()
                .flatMap(entry ->
                        entry.getValue().stream().map(tworderitem -> {
                            Tworder tworder = orderidToOrder.get(entry.getKey());
                            OrderAndItemDTO dto = OrderAndItemDTO.forGetOrdersInPeriod(tworder, tworderitem, "");
                            // 可编辑状态（默认为true，以下情况为false：
                            // 据orderid查询，取 创建日期最新 & amountachievement业绩金额非负 的一条数据，
                            //	bcancel=0 & icommissionstatus =1或2 （不可编辑）
                            //	其他 （可编辑）
                            dto.setBEditFlag(true);
                            commissionServiceI.lambdaQuery()
                                    .eq(Twcommission::getOrderitemid, tworderitem.getId())
                                    .ge(Twcommission::getAmountachievement, BigDecimal.ZERO)
                                    .list()
                                    .stream()
                                    .max(Comparator.comparing(Twcommission::getId))
                                    .ifPresent(commission -> {
                                        boolean isNotCancelled = !commission.getBcancel();
                                        boolean isConfirmedOrIssued = commission.getIcommissionstatus() == CommissionStatus.COMMISSION_STATUS_CONFIRMED.getKey()
                                                || commission.getIcommissionstatus() == CommissionStatus.COMMISSION_STATUS_ISSUED.getKey();
                                        if (isNotCancelled && isConfirmedOrIssued) {
                                            dto.setBEditFlag(false);
                                        }
                                    });
                            return dto;
                        }))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(OrderAndItemDTO::getIitemcode).reversed())
                .collect(Collectors.toList());


        Page<OrderAndItemDTO> dtoPage = new Page<>();
        dtoPage.setRecords(result);
        dtoPage.setTotal(result.size());
        return dtoPage;
    }

    @Override
    public IPage<OrderAndItemDTO> getOrderItemListByCustomerId(Page<OrderAndItemDTO> page, OrderAndItemVO query) throws Exception {
        if (query.getEndTime() != null) {
            query.setEndTime(DateUtil.offsetDay(query.getEndTime(), 1));
        } else {
            query.setEndTime(DateUtil.offsetDay(new Date(), 1));
        }
        Long pagenumb = page.getCurrent() <= 1 ? 0 : (page.getCurrent() - 1) * page.getSize();
        List<com.hgzp.pagemodel.business.OrderAndItemDTO> lsOrderAndItemDTO = tworderitemMapper.selectOrderAndItemList(query.getStartTime(),
                query.getEndTime(), query.getMediaid(), query.getCustomerid(), pagenumb, page.getSize());
        List<OrderAndItemDTO> resultlist = new ArrayList<>();
        lsOrderAndItemDTO.forEach(item -> {
            OrderAndItemDTO orderAndItemDTO = new OrderAndItemDTO();
            BeanUtils.copyProperties(item, orderAndItemDTO);
            resultlist.add(orderAndItemDTO);
        });

        Long orderAndItemCount = tworderitemMapper.getOrderAndItemCount(query.getStartTime(), query.getEndTime(), query.getMediaid(), query.getCustomerid());
        Page<OrderAndItemDTO> resultpage = new Page<>();
        resultpage.setRecords(resultlist);
        resultpage.setTotal(orderAndItemCount);
        return resultpage;
    }

    @Override
    public IPage<OrderAndItemDTO> getMyOrderItemPageList(Page<Tworderitem> page, OrderAndItemVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        List<String> deptids = new ArrayList<>();
        //如果只查当前人员时，部门条件不生效
        if (query.getBcurflag() != null && query.getBcurflag()) {
            query.setEmployid(user.getUserid());
        }
        // 人员id不为空时，部门条件不生效
        if (ObjectUtil.isNotEmpty(query.getEmployid())) {
            query.setDeptid(null);
        }
        if (query.getDeptid() != null) {
            List<Long> deptidList = deptServiceI.getChildDeptId(query.getDeptid());
            deptids = deptidList.stream().map(String::valueOf).collect(Collectors.toList());
        } else {
            deptids = user.getAuthedDeptIds() == null ? new ArrayList<>() : Arrays.stream(user.getAuthedDeptIds().split(",")).collect(Collectors.toList());
        }
        if (query.getEndTime() == null) {
            query.setEndTime(DateUtil.offsetDay(new Date(), 1));
        } else {
            query.setEndTime(DateUtil.offsetDay(query.getEndTime(), 1));
        }

        LambdaQueryWrapper<Tworderitem> lqw = Wrappers.lambdaQuery();
        // 合同号
//        lqw.eq(StrUtil.isNotBlank(query.getQueryKey()), Tworderitem::getScontractnum, query.getQueryKey());
        // 合同号与项目名
        List<Tworder> tworders = tworderService.lambdaQuery()
                .like(Tworder::getAdprojectname, query.getQueryKey())
                .list();
        List<Long> orderIds = tworders.stream()
                .map(Tworder::getId)
                .collect(Collectors.toList());
//        lqw.like(StrUtil.isNotBlank(query.getQueryKey()), Tworderitem::getScontractnum, query.getQueryKey())
//                .or().in(CollUtil.isNotEmpty(orderIds), Tworderitem::getOrderid, orderIds);
        lqw.and(StrUtil.isNotBlank(query.getQueryKey()), i -> i.like(Tworderitem::getScontractnum, query.getQueryKey())
                .or().in(CollUtil.isNotEmpty(orderIds), Tworderitem::getOrderid, orderIds));
        // 有欠款
        lqw.gt(query.getBarrearsflag() != null && query.getBarrearsflag(), Tworderitem::getAmountarrearage, 0);
        // 查人员归属
        lqw.inSql(query.getEmployid() != null, Tworderitem::getId, "select orderitemid from tworderitembelong where employid=" + query.getEmployid());
        // 查部门归属
        lqw.inSql(deptids.size() > 0, Tworderitem::getOrderid, "select id from tworder where deptid in ('" + String.join("','", deptids) + "')");
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Tworderitem::getPrestartdate, query.getStartTime());
        lqw.lt(ObjUtil.isNotNull(query.getEndTime()), Tworderitem::getPrestartdate, query.getEndTime());
        // 发布状态
        lqw.eq(query.getIpublishstatus() != null, Tworderitem::getIpublishstatus, query.getIpublishstatus());
        lqw.eq(Tworderitem::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey());
//        lqw.eq(StrUtil.isNotBlank(query.getQueryKey()), Tworderitem::getScontractnum, query.getQueryKey());
        lqw.orderByDesc(Tworderitem::getPrestartdate);
        IPage<Tworderitem> pages = tworderitemMapper.selectPage(page, lqw);

        List<OrderAndItemDTO> result = new ArrayList<>();
        pages.getRecords().forEach(item -> {
            Tworder tworder = tworderMapper.selectById(item.getOrderid());
            OrderAndItemDTO dto = OrderAndItemDTO.from(tworder, item);
            dto.setAdprojectid(tworder.getAdprojectid());
            dto.setIorderaddapprovestatus(tworder.getIaddapprovestatus());
            dto.setIorderchangeapprovestatus(tworder.getIchangeapprovestatus());
            dto.setIorderstopapprovestatus(tworder.getIstopapprovestatus());
            Twadproject twadproject = twadprojectServiceI.getById(tworder.getAdprojectid());
            if (ObjectUtil.isNotNull(twadproject)) {
                dto.setProjectcode(twadproject.getProjectcode());
            }
            result.add(dto);
        });
        // 结果
        Page<OrderAndItemDTO> dtoPage = new Page<>();
        BeanUtils.copyProperties(page, dtoPage);
        dtoPage.setRecords(result);
        dtoPage.setTotal(pages.getTotal());
        return dtoPage;
    }

    @Override
    public Json updateItemByCJ(String sordernum, String path, String filename, String width, String height) {
        List<Tworderitem> tworderitemList = this.lambdaQuery()
                .eq(Tworderitem::getSordernum, sordernum)
                .ge(Tworderitem::getPrestartdate, new Date())
                .list();
        if (tworderitemList.size() == 0) {
            return Json.fail("未找到订单明细！");
        }
        for (Tworderitem tworderitem : tworderitemList) {
//            Tworderitem tworderitem = tworderitemMapper.selectById(id);
            if (tworderitem == null) {
                return Json.fail("未找到订单明细！");
            }
            tworderitem.setSfilename(filename);
            tworderitem.setNwidth(new BigDecimal(width));
            tworderitem.setNheight(new BigDecimal(height));
            innerInterceptor.recoredLog();
            if (tworderitemMapper.updateById(tworderitem) == 0) {
                return Json.fail("数据已改变，请刷新后再重新操作！");
            }
        }
        return Json.success();
    }


    @Override
    public List<CJOrderItemDTO> getOrderItemForCJ(Tworderitem entiry) {
        List<Long> tbadindustryids = tbadindustryServiceI.getTbAdIndustryListForCJ().stream().map(Tbadindustry::getId).collect(Collectors.toList());
        List<String> ids = tbadindustryids.stream().map(String::valueOf).collect(Collectors.toList());
        LambdaQueryWrapper<Tworderitem> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tworderitem::getAddisplayformname, "分类广告"); //20240312 去掉，不用此字段判断，修改为根据行业是否是分类广告判定
//        lqw.inSql(Tworderitem::getOrderid, "select id from tworder where iapprovestatus=2 and buse=1 and adindustyid in (" + String.join(",", ids) + ")");
        lqw.eq(Tworderitem::getBuse, 1);
        lqw.eq(Tworderitem::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey());
        lqw.ne(Tworderitem::getIpublishstatus, PublishStatus.PUBLISH_Published.getKey());
        // 订单号
        lqw.eq(ObjUtil.isNotNull(entiry.getSordernum()), Tworderitem::getSordernum, entiry.getSordernum());
        // 时间
        lqw.eq(ObjUtil.isNotNull(entiry.getCreatetime()), Tworderitem::getPrestartdate, entiry.getCreatetime());
        // 合同号
        lqw.eq(ObjUtil.isNotNull(entiry.getScontractnum()), Tworderitem::getScontractnum, entiry.getScontractnum());
        // 时间段
        lqw.between(ObjUtil.isNotNull(entiry.getPrestartdate()) && ObjUtil.isNotNull(entiry.getPreenddate()), Tworderitem::getPrestartdate, entiry.getPrestartdate(), entiry.getPreenddate());
        lqw.between(ObjUtil.isNotNull(entiry.getPrestartdate()) && ObjUtil.isNull(entiry.getPreenddate()), Tworderitem::getPrestartdate, entiry.getPrestartdate(), entiry.getPrestartdate());
        // 叠次
        lqw.eq(ObjUtil.isNotNull(entiry.getFoldid()), Tworderitem::getFoldid, entiry.getFoldid());
        // 版本
        lqw.eq(ObjUtil.isNotNull(entiry.getFoldareaverid()), Tworderitem::getFoldareaverid, entiry.getFoldareaverid());
        List<Tworderitem> list = tworderitemMapper.selectList(lqw);
        List<CJOrderItemDTO> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Tbadtype tbadtype = tbadtypeServiceI.getAdTypeList().stream().filter(item -> item.getSname().equals("分类广告")).findFirst().get();
        if (tbadtype == null) {
            return result;
        }
        list.forEach(item -> {
            Tworder tworder = tworderMapper.selectById(item.getOrderid());
            if (tworder == null) {
                return;
            }
            Tbadindustry tbadindustry = tbadindustryServiceI.getById(tworder.getAdindustyid());
            if (tbadindustry == null) {
                return;
            }
            Tbadspec tbadspec = tbadspecServiceI.getById(item.getAdspecid());
            if (tbadspec == null) {
                return;
            }
            CJOrderItemDTO dto = new CJOrderItemDTO();
            dto.setSmallad(tbadspec.getBbigclassified() ? "是" : "否");
            dto.setPagestatus(PublishStatus.getNameByKey(item.getIpublishstatus()));
            dto.setCustid(tworder.getCustomerid().toString());
            dto.setCustname(tworder.getCustomername());
            dto.setInforid(item.getSordernum().toString());
            dto.setPubldate(sdf.format(item.getPrestartdate()));
            dto.setKinddetid(tworder.getAdindustyid().toString());
            dto.setKinddetail(tworder.getAdindustryname());
            dto.setFold(item.getFoldname());
            dto.setFoldid(item.getFoldid().toString());
            dto.setAdaxb(item.getAdspecname());
            dto.setAdaxbid(item.getAdspecid().toString());
            dto.setFoldedition(item.getFoldareavername());
            dto.setFoldedid(item.getFoldareaverid().toString());
            dto.setAdcolor(item.getAdcolorname());
            dto.setAdtitle(item.getSadtitle());
            dto.setCompactid(tworder.getScontractnum());
            // 根据媒体和日期查找刊期表++++++++++++++++++++++
            dto.setPublnum(tbmediapublicnumServiceI.getMediaPublishNO(item.getMediaid(), item.getPrestartdate()));//-----------------------------刊期
            dto.setAdPath(item.getSfilename());//-----------------------------广告路径
            dto.setFilecontent(item.getSadcontent());
            List<Twadresourcefiles> adresourcefiles = twadresourceapplicationServiceI.getResourceFilesByOrderIdForCJ(tworder.getId().toString());
            List<String> picturepath = adresourcefiles.stream().map(twadresourcefiles -> {
                return UfileUtil.getStaticUrl(twadresourcefiles.getSfileid(), twadresourcefiles.getSfileformat());
            }).collect(Collectors.toList());
            dto.setPicturepath(String.join(";", picturepath));//-----------------------------图片路径
            dto.setParentid(tbadindustry.getParentid().toString());
            dto.setLocalId(tbadindustry.getId().toString());
            dto.setAdkind(tbadtype.getSname());
            dto.setAdkindid(tbadtype.getId().toString());
            dto.setPaperid(item.getMediaid().toString());
            dto.setPaperver("1");//-----------------------------报纸地域
            dto.setAdVer(item.getMedianame());
            dto.setEngagetime(sdf.format(item.getCreatetime()));
            dto.setRemark(item.getSremark());
            dto.setEditorSet(StringUtils.isNotEmpty(item.getFoldpageplanname()) ? item.getFoldpageplanname() : "非指定版");
            dto.setHeight(StringUtils.isNotEmpty(item.getNheight().toString()) ? item.getNheight().toString() : "0");
            dto.setWidth(StringUtils.isNotEmpty(item.getNwidth().toString()) ? item.getNwidth().toString() : "0");
            // 规格表中查找分类广告格数++++++++++++++++
            dto.setPknum(tbadspec.getIpknum().toString());//-----------------------------数量
            // 根据是否有资源，有资源是1++++++++++++++++
            dto.setPpflg(adresourcefiles.size() > 0 ? "1" : "0");//-----------------------------价格计算标志/是否上传图片070114牟加
            dto.setPricetype("按格");//-----------------------------价格类型
            result.add(dto);
        });
        return result;
    }

    /**
     * 我的订单(明细)汇总
     * 方法功能:我的订单(明细)汇总
     *
     * @param vo
     * @return com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO
     * @author yanz
     * @date 2024/3/6 9:26
     */
    @Override
    public OrderAndItemDTO getMyOrderItemCount(OrderAndItemVO vo) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        if (vo.getBcurflag() != null && vo.getBcurflag()) {
            vo.setEmployid(user.getUserid());
            //如果只查当前人员时，部门条件不生效
            vo.setDeptid(null);
        }
        List<Long> deptidList = new ArrayList<>();
        // 人员id不为空时，部门条件不生效
        if (ObjectUtil.isNotEmpty(vo.getEmployid())) {
            vo.setDeptid(null);
        } else {
            if (vo.getDeptid() != null) {
                deptidList = deptServiceI.getChildDeptId(vo.getDeptid());
            } else {
                deptidList = Arrays.stream(user.getAuthedDeptIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            }
        }
        if (vo.getEndTime() != null) {
            vo.setEndTime(DateUtil.offsetDay(vo.getEndTime(), 1));
        }

        com.hgzp.pagemodel.business.OrderAndItemDTO sumMyOrderItem = tworderitemMapper.getSumMyOrderItem(vo.getStartTime(), vo.getEndTime(),
                deptidList, vo.getEmployid(),
                vo.getQueryKey(), vo.getBarrearsflag(), vo.getIpublishstatus());
        OrderAndItemDTO dto = new OrderAndItemDTO();
        if (ObjUtil.isNotNull(sumMyOrderItem)) {
            BeanUtils.copyProperties(sumMyOrderItem, dto);
        }
        return dto;
    }

    @Override
    public List<Tworderitem> getListByOrderIdForCJ(String orderId) {
        List<Long> tbadindustryids = tbadindustryServiceI.getTbAdIndustryListForCJ().stream().map(Tbadindustry::getId).collect(Collectors.toList());
        List<String> ids = tbadindustryids.stream().map(String::valueOf).collect(Collectors.toList());
        List<Tworderitem> list = this.lambdaQuery()
                .eq(Tworderitem::getOrderid, orderId)
                .eq(Tworderitem::getBuse, 1)
                .eq(Tworderitem::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                .eq(Tworderitem::getAddisplayformname, "分类广告") // 20240312 去掉，不用此字段判断，修改为根据行业是否是分类广告判定
//                .inSql(Tworderitem::getOrderid, "select id from tworder where iapprovestatus=2 and buse=1 and adindustyid in (" + String.join(",", ids) + ")")
                .list();
        return list;
    }

    @Override
    public List<Tworderitem> getFilePathBySordernum(String sordernum, String strdate) {
        List<Tworderitem> tworderitems = this.lambdaQuery()
                .eq(Tworderitem::getSordernum, sordernum)
                .eq(Tworderitem::getPrestartdate, strdate)
                .eq(Tworderitem::getBuse, 1)
                .list();
        return tworderitems;
    }

    @Override
    public BigDecimal getAmountreceivableCountByprojectId(String adprojectid) {
        List<Tworderitem> lsorderitems=this.lambdaQuery()
                .eq(Tworderitem::getBuse,1)
                .inSql(Tworderitem::getOrderid,("select id from tworder where adprojectid = " + adprojectid) )
                .list();
        BigDecimal bResult=lsorderitems.stream().map(Tworderitem::getAmountreceivable)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return bResult;
    }

    /**
     * 核销数据汇总成 “人员-已收总额”
     * 方法功能:获取时间范围内核销数据汇总，按主页面部门、人员检索
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/3/14 17:18
     */
    @Override
    public IPage<OrderAndItemDTO> getMyReceivedSummariesPageList(Page<Tworderitem> page, OrderAndItemVO query) throws Exception {

        Page pageNew = new Page();
        OrderApportiondetailVO queryNew = new OrderApportiondetailVO();
        BeanUtils.copyProperties(query,queryNew);
        IPage orderApportiondetailPageList = orderApportiondetailServiceI.getOrderApportiondetailPageList(pageNew, queryNew);
        List<OrderApportiondetailDTO> apportiondetailResult = orderApportiondetailPageList.getRecords();
        List<OrderAndItemDTO> result = new ArrayList<>();

        if (apportiondetailResult.size() > 0) {
            // 取result的所有 orderitemid
            List<Long> orderItemIds = apportiondetailResult.stream().map(OrderApportiondetailDTO::getOrderitemid).collect(Collectors.toList());
            orderItembelongServiceI.lambdaQuery()
                    .in(Tworderitembelong::getOrderitemid, orderItemIds)
                    .eq(query.getEmployid() != null, Tworderitembelong::getEmployid, query.getEmployid())
                    .list()
                    .stream().distinct()
                    // 通过 Tworderitembelong 取每个 orderitemid 对应的所有业务员信息
                    // 对每个业务员取 orderitemid ，累计业务员下的所有 orderitemid 的已收金额
                    .forEach(orderItemBelong -> {
                        Long orderitemid = orderItemBelong.getOrderitemid();
                        Long employid = orderItemBelong.getEmployid();
                        OrderAndItemDTO dto = result.stream().filter(item -> item.getEmployid().equals(employid)).findFirst().orElse(null);
                        if (dto == null) {
                            dto = new OrderAndItemDTO();
                            dto.setEmployid(employid);
                            dto.setEmployname(orderItemBelong.getEmployname());
                            dto.setNamountapportion(BigDecimal.ZERO);
                            result.add(dto);
                        }
                        BigDecimal amount = apportiondetailResult.stream().filter(item -> item.getOrderitemid().equals(orderitemid)).map(OrderApportiondetailDTO::getNamountapportion).reduce(BigDecimal.ZERO, BigDecimal::add);
                        dto.setNamountapportion(dto.getNamountapportion().add(amount));
                    });
        }


        Page<OrderAndItemDTO> voPage = new Page<>();
        if (result.isEmpty()) {
            return voPage;
        }
        voPage.setRecords(result);
        voPage.setTotal(result.size());

        return voPage;
    }

    /**
     * 我的实收 列表
     * 方法功能:获取时间范围内与“我”有关的核销数据，可按部门、人员检索，
     * 亦用于展示汇总明细
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/3/15 10:48
     */
    @Override
    public IPage<OrderAndItemDTO> getMyReceivedPageList(Page<Tworderitem> page, OrderAndItemVO query) throws Exception {
        Page pageNew = new Page();
        OrderApportiondetailVO queryNew = new OrderApportiondetailVO();
        BeanUtils.copyProperties(query,queryNew);
        IPage orderApportiondetailPageList = orderApportiondetailServiceI.getOrderApportiondetailPageList(pageNew, queryNew);
        List<OrderApportiondetailDTO> apportiondetailResult = orderApportiondetailPageList.getRecords();
        List<OrderAndItemDTO> result = new ArrayList<>();
        LoginUser user = WebUtil.getLoginUser();
        List<String> deptids = new ArrayList<>();
        if (query.getDeptid() != null) {
            List<Long> deptidList = deptServiceI.getChildDeptId(query.getDeptid());
            deptids = deptidList.stream().map(String::valueOf).collect(Collectors.toList());
        } else {
            deptids = user.getAuthedDeptIds() == null ? new ArrayList<>() : Arrays.stream(user.getAuthedDeptIds().split(",")).collect(Collectors.toList());
        }
        if (apportiondetailResult.size() > 0) {
            // 取result的所有 orderitemid
            List<Long> orderItemIds = apportiondetailResult.stream().map(OrderApportiondetailDTO::getOrderitemid).collect(Collectors.toList());
            List<Tworderitembelong> totalbelongs = orderItembelongServiceI.lambdaQuery()
                    .in(Tworderitembelong::getOrderitemid, orderItemIds)
                    .eq(query.getEmployid() != null, Tworderitembelong::getEmployid, query.getEmployid())
                    .in(query.getDeptid() != null, Tworderitembelong::getDeptid, deptids)
                    .list();
            apportiondetailResult.forEach(apportiondetail -> {
                Long orderitemid = apportiondetail.getOrderitemid();
                List<Tworderitembelong> orderitembelong = totalbelongs.stream().filter(item -> item.getOrderitemid().equals(orderitemid)).collect(Collectors.toList());
                if (orderitembelong.size() > 0) {
                    orderitembelong.forEach(item -> {
                        OrderAndItemDTO dto = new OrderAndItemDTO();
                        BeanUtils.copyProperties(apportiondetail, dto);
                        Twinvoice twinvoice = invoiceMapper.selectById(apportiondetail.getInvoiceid());
                        Twcustomercharge twcustomercharge = twcustomerchargeServiceI.getById(apportiondetail.getCustomerchargeid());
                        if (ObjUtil.isNotNull(twcustomercharge)) {
                            dto.setDpaydate(twcustomercharge.getDpaydate());
                        }
                        if (ObjUtil.isNotNull(twinvoice)) {
                            dto.setLastoperator(twinvoice.getLastoperator());
                            dto.setInvoices(twinvoice.getInvoice());
                        }
                        dto.setEmployid(item.getEmployid());
                        dto.setEmployname(item.getEmployname());
                        result.add(dto);
                    });
                }
            });
        }

        result.forEach(r -> {
            Tworder tworder = tworderMapper.selectById(r.getOrderid());
            Tworderitem tworderitem = tworderitemMapper.selectById(r.getOrderitemid());

            r.setAgentname(tworder.getAgentname());
            r.setAgencyname(tworder.getAgencyname());
            r.setCustomername(tworder.getCustomername());
            r.setBusinessentityname(tworder.getBusinessentityname());
            r.setAdindustryname(tworder.getAdindustryname());
            r.setFoldname(tworderitem.getFoldname());
            r.setFoldpageplanname(tworderitem.getFoldpageplanname());
            r.setMedianame(tworderitem.getMedianame());
            r.setPrestartdate(tworderitem.getPrestartdate());
        });

        Page<OrderAndItemDTO> voPage = new Page<>();
        if (result.isEmpty()) {
            return voPage;
        }

        Long current = page.getCurrent();
        Long size = page.getSize();
        Long skip = (current - 1) * size;
        List<OrderAndItemDTO> results = result.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(OrderAndItemDTO::getDpaydate).reversed())
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());
        voPage.setRecords(results);
        voPage.setTotal(result.size());

        return voPage;

    }

    //    @Override
//    public List<Long> getOrderIdByItemId(String orderItemIds) {
//        List<String> idList = Arrays.asList(orderItemIds.split(","));
//        List<Long> orderIds = new LambdaQueryChainWrapper<>(tworderitemMapper)
//                .in(Tworderitem::getId, idList)
//                .list()
//                .stream()
//                .map(Tworderitem::getOrderid)
//                .distinct()
//                .collect(Collectors.toList());
//        return orderIds;
//    }
}
