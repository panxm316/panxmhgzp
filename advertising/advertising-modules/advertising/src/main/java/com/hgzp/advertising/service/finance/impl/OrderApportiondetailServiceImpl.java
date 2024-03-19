package com.hgzp.advertising.service.finance.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.approve.AppStatus;
import com.hgzp.advertising.emnus.customer.EcustomerChargeType;
import com.hgzp.advertising.emnus.finance.InvoiceStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO;
import com.hgzp.advertising.pagemodel.finance.vo.OrderApportiondetailVO;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.business.CommissionServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.finance.InvoiceBackHistoryServiceI;
import com.hgzp.advertising.service.finance.InvoiceServiceI;
import com.hgzp.advertising.service.finance.OrderApportiondetailServiceI;
import com.hgzp.advertising.service.finance.TwcustomerchargeServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.*;
import com.hgzp.mapper.finance.TworderapportiondetailMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 广告分摊明细表 服务实现类
 * </p>
 *
 * @author suny
 * @since 2023-12-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderApportiondetailServiceImpl extends ServiceImpl<TworderapportiondetailMapper, Tworderapportiondetail> implements OrderApportiondetailServiceI {

    @Resource
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TwcustomerchargeServiceI customerchargeServiceI;
    @Autowired
    private TworderServiceI orderServiceI;
    @Autowired
    private TworderitemServiceI orderitemServiceI;
    @Autowired
    private InvoiceServiceI invoiceServiceI;
    @Autowired
    private InvoiceBackHistoryServiceI invoiceBackHistoryServiceI;
    @Autowired
    private TwpreinvoiceapplicationServiceI preinvoiceapplicationServiceI;
    @Autowired
    private CommissionServiceI commissionServiceI;

    /**
     * 查看分摊详情列表
     * 方法功能:根据收费表id，查询广告分摊表详情，使用 OrderApportiondetailDTO，包括以下字段：
     * 合同号、经营主体、直接客户、代理公司、内容生产方、刊发日期、媒体、广告标题、签订金额、分摊金额、发票号、收费日期、分摊日期、收款种类、业务员）
     * 注：有发票号时将按发票号匹配
     *
     * @param page
     * @param query
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO>
     * @author yanz
     * @date 2023/12/25 16:42
     */
    @Override
    public IPage<OrderApportiondetailDTO> getOrderApportiondetailPageList(Page<Tworderapportiondetail> page, OrderApportiondetailVO query) {
        //根据收费表id，查询广告分摊表详情，使用 OrderApportiondetailDTO，包括以下字段：
        // 合同号、经营主体、直接客户、代理公司、内容生产方、刊发日期、媒体、广告标题、签订金额、分摊金额、发票号、收费日期、分摊日期、收款种类、业务员）

        // 取数据
        LambdaQueryWrapper<Tworderapportiondetail> lqw = Wrappers.<Tworderapportiondetail>lambdaQuery()
                .eq(query.getCustomerchargeid() != null, Tworderapportiondetail::getCustomerchargeid, query.getCustomerchargeid());
        // 关键字查询 - 发票号
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            lqw.like(Tworderapportiondetail::getInvoice, query.getQueryKey());
        }
        // 日期限制
        lqw.ge(ObjUtil.isNotNull(query.getStartTime()), Tworderapportiondetail::getDcreatetime, query.getStartTime());
        if (ObjUtil.isNotNull(query.getEndTime())) {
            lqw.lt(Tworderapportiondetail::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        List<Tworderapportiondetail> tworderapportiondetailList = list(lqw);
        if (CollectionUtil.isEmpty(tworderapportiondetailList)) {
            return new Page<>();
        }
        List<OrderApportiondetailDTO> results = convertApportiondetailToDTOs(tworderapportiondetailList);
        if (CollUtil.isNotEmpty(results)) {
            results = results.stream().sorted(Comparator.comparing(OrderApportiondetailDTO::getDcreatetime).reversed()).collect(Collectors.toList());
        }
        Page<OrderApportiondetailDTO> paged = new Page<>();
        BeanUtils.copyProperties(page, paged);
        paged.setRecords(results);
        paged.setTotal(results.size());
        return paged;
    }

    /**
     * 分摊详情（核销数据）POJO转DTO
     * 方法功能:有查询操作，涉及order、orderitem、invoice表
     *
     * @param tworderapportiondetailList
     * @return java.util.List<com.hgzp.advertising.pagemodel.finance.dto.OrderApportiondetailDTO>
     * @author yanz
     * @date 2024/1/5 14:30
     */
    public List<OrderApportiondetailDTO> convertApportiondetailToDTOs(List<Tworderapportiondetail> tworderapportiondetailList) {
        List<Long> orderIds = tworderapportiondetailList
                .stream().map(Tworderapportiondetail::getOrderid).collect(Collectors.toList());
        Map<Long, Tworder> tworderMap = new HashMap<>();
        if (CollUtil.isNotEmpty(orderIds)) {
            tworderMap = orderServiceI.lambdaQuery()
                    .in(Tworder::getId, orderIds)
                    .eq(Tworder::getIapprovestatus, AppStatus.APPRSTATUS_PASS.getKey())
                    .eq(Tworder::getBuse, true)
                    .list()
                    .stream().collect(Collectors.toMap(Tworder::getId, Function.identity()));
        }
        List<Long> orderItemIds = tworderapportiondetailList
                .stream().map(Tworderapportiondetail::getOrderitemid).collect(Collectors.toList());
        Map<Long, Tworderitem> tworderitemMap = new HashMap<>();
        if (CollUtil.isNotEmpty(orderItemIds)) {
            tworderitemMap = orderitemServiceI.lambdaQuery()
                    .in(Tworderitem::getId, orderItemIds)
                    .eq(Tworderitem::getIapprovestatus, AppStatus.APPRSTATUS_PASS.getKey())
                    .eq(Tworderitem::getBuse, true)
                    .list()
                    .stream().collect(Collectors.toMap(Tworderitem::getId, Function.identity()));

        }
        List<Long> invoiceIds = tworderapportiondetailList
                .stream().map(Tworderapportiondetail::getInvoiceid).collect(Collectors.toList());
        Map<Long, Twinvoice> twinvoiceMap = new HashMap<>();
        if (CollUtil.isNotEmpty(invoiceIds)) {
            twinvoiceMap = invoiceServiceI.lambdaQuery()
                    .in(Twinvoice::getId, invoiceIds)
                    .eq(Twinvoice::getIstatus, InvoiceStatus.VALID.getKey())
                    .list()
                    .stream().collect(Collectors.toMap(Twinvoice::getId, Function.identity()));
        }
        List<Long> customerchargeIds = tworderapportiondetailList.stream().map(Tworderapportiondetail::getCustomerchargeid).collect(Collectors.toList());
        Map<Long, Twcustomercharge> twcustomerchargeMap = customerchargeServiceI.lambdaQuery()
                .in(Twcustomercharge::getId, customerchargeIds)
                .list()
                .stream().collect(Collectors.toMap(Twcustomercharge::getId, Function.identity()));
        // 拼结果
        List<OrderApportiondetailDTO> results = new ArrayList<>();
        for (Tworderapportiondetail detail : tworderapportiondetailList) {
            Twinvoice twinvoice = twinvoiceMap.get(detail.getInvoiceid());
            Tworderitem tworderitem = tworderitemMap.get(detail.getOrderitemid());
            Tworder tworder = tworderMap.get(detail.getOrderid());
            Twcustomercharge twcustomercharge = twcustomerchargeMap.get(detail.getCustomerchargeid());
            OrderApportiondetailDTO dto = OrderApportiondetailDTO.parseToDTO(detail, twinvoice, tworderitem, tworder);
            if (ObjUtil.isNotNull(twcustomercharge)) {
                dto.setDpaydate(twcustomercharge.getDpaydate());
            }
            results.add(dto);
        }
        if (CollUtil.isNotEmpty(results)) {
            results = results.stream().sorted(Comparator.comparing(OrderApportiondetailDTO::getDcreatetime).reversed()).collect(Collectors.toList());
        }
        return results;
    }

    /**
     * 保存分摊
     * 方法功能:保存流程：
     * 1、按照时间正序对指定的ids中的广告详情分别进行分配
     * 2、每条分配添加一条tworderapportiondetail广告分摊明细记录，同时将分摊情况会写到orderitem表对应的记录，同时记录好分配出去的额度和剩余额度
     * 3、如果所有orderitem全部分配结束，仍有剩余，更新Twcustomercharge中的已用和剩余金额，不改状态标记
     * 4、如果Twcustomercharge表中的额度全部分配完，则更新Twcustomercharge中的已用和剩余金额，同时将标记更新为已核销
     *
     * @param customerChargeId
     * @param orderitemIds
     * @param dateString
     * @return void
     * @author yanz
     * @date 2023/12/25 16:57
     */
    @Override
    public void saveOrderApportiondetail(Long customerChargeId, List<Long> orderitemIds, String dateString) throws Exception {
        Date date = DateUtil.parse(dateString);
        //保存流程：
        LoginUser loginUser = WebUtil.getLoginUser();
        Twcustomercharge twcustomercharge = customerchargeServiceI.getById(customerChargeId);
        if (ObjUtil.isNull(twcustomercharge)) {
            throw new DataNotExistException("收费表记录不存在");
        }
        //1、按照时间正序对指定的ids中的广告详情分别进行分配
        List<Tworderitem> orderItems = orderitemServiceI.listByIds(orderitemIds)
                .stream().sorted(Comparator.comparing(Tworderitem::getCreatetime)).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(orderItems)) {
            throw new DataNotExistException("订单明细记录不存在");
        }

        BigDecimal namountbalance = twcustomercharge.getNamountbalance();
        BigDecimal namountbalanceTemp = namountbalance;
        BigDecimal namounspent = twcustomercharge.getNamounspent();
        BigDecimal finalSpent;
        //2、每条分配添加一条tworderapportiondetail广告分摊明细记录，同时将分摊情况回写到orderitem表对应的记录，同时记录好分配出去的额度和剩余额度
        for (Tworderitem orderItem : orderItems) {
            // 欠款直接用就可以
            BigDecimal amountarrearage = orderItem.getAmountarrearage();
            BigDecimal namountapportion;

            if (NumberUtil.isGreater(namountbalanceTemp, amountarrearage)) {
                // 如果剩余额度大于当前订单的欠款额度，则将当前订单的欠款额度全部分配给当前订单
                namountbalanceTemp = namountbalanceTemp.subtract(amountarrearage);
                namountapportion = amountarrearage;
            } else {
                // 如果剩余额度小于当前订单的欠款额度，则将剩余额度全部分配给当前订单
                namountapportion = namountbalanceTemp;
                namountbalanceTemp = BigDecimal.ZERO;
            }

            Tworderapportiondetail tworderapportiondetail = new Tworderapportiondetail(customerChargeId, orderItem, loginUser, twcustomercharge, namountapportion, date);
            innerInterceptor.recoredLog();
            if (!this.save(tworderapportiondetail)) {
                throw new DataExistException("保存分摊明细失败");
            }

            // 改对应明细表的欠款额和已收金额
            orderItem.setAmountreceived(namountapportion.add(orderItem.getAmountreceived()));
            orderItem.setAmountarrearage(amountarrearage.subtract(namountapportion));
            innerInterceptor.recoredLog();
            if (!orderitemServiceI.updateById(orderItem)) {
                throw new DataNotExistException("更新订单明细失败");
            }

            finalSpent = namountbalance.subtract(namountbalanceTemp).add(namounspent);
            //4、如果Twcustomercharge表中的额度全部分配完，则更新Twcustomercharge中的已用和剩余金额，同时将标记更新为已核销
            // 无可分配金额，停止遍历
            if (namountbalanceTemp.compareTo(BigDecimal.ZERO) == 0) {
                // 同时更新twcustomercharge表的分配和已用，同时状态改为已核销
                twcustomercharge.setNamountbalance(BigDecimal.ZERO);
                // 没有分配金额，即入账金额已全部花完，考虑‘已用金额’为：
                //      此前已用金额 + 本次已用金额(namounspent.add(namountbalance.subtract(namountbalanceTemp)))
                //      或 入账金额 twcustomercharge.getNamountreceived()
                // 暂时用已用金额 + 本次已用金额
                twcustomercharge.setNamounspent(finalSpent);
                twcustomercharge.setIstatus(AppStatus.APPRSTATUS_VERIFIED.getKey());
                innerInterceptor.recoredLog();
                if (!customerchargeServiceI.updateById(twcustomercharge)) {
                    throw new DataNotExistException("更新收费表失败");
                }
                namountbalanceTemp = BigDecimal.ZERO;
                break;
            }
        }
        //3、如果所有orderitem全部分配结束，仍有剩余，更新Twcustomercharge中的已用和剩余金额，不改状态标记
        // 考虑 此前已用金额 + 本次已用金额
        finalSpent = namountbalance.subtract(namountbalanceTemp).add(namounspent);
        if (namountbalanceTemp.compareTo(BigDecimal.ZERO) != 0) {
            twcustomercharge.setNamountbalance(namountbalanceTemp);
            twcustomercharge.setNamounspent(finalSpent);
            innerInterceptor.recoredLog();
            if (!customerchargeServiceI.updateById(twcustomercharge)) {
                throw new DataNotExistException("更新收费表失败");
            }
        }
        // 更新分摊后预开发票申请表已收金额
        if (twcustomercharge.getItype() == EcustomerChargeType.EntryType_BankPay.getKey()) {
            preinvoiceapplicationServiceI.updateReceivedAmount(twcustomercharge.getPreinvoiceapplicationid(), finalSpent);
        }
    }

    /**
     * 退回分摊
     * 方法功能:流程：根据分摊表ids，找到需要回退的分摊表list——》每一条分摊需要根据orderitemid，
     * 找到对应的订单详情，将分摊结果回退，修改已收、欠款字段——》根据收费表id，找到收费表数据，将状态、已用金额、剩余金额分别回退
     *
     * @param apportiondetailIdList
     * @param sdesc                 回退原因
     * @return void
     * @author yanz
     * @date 2023/12/26 13:22
     */
    @Override
    public void revertWriteOff(List<Long> apportiondetailIdList, String sdesc) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();
        // 流程：根据分摊表ids——》找到需要回退的分摊表list
        List<Tworderapportiondetail> tworderapportiondetailList = listByIds(apportiondetailIdList);
        if (CollectionUtil.isEmpty(tworderapportiondetailList)) {
            throw new DataNotExistException("不存在对应的分摊表记录");
        }

        // 数据准备
        List<Long> orderItemIds = tworderapportiondetailList.stream()
                .map(Tworderapportiondetail::getOrderitemid)
                .collect(Collectors.toList());
        List<Long> customerChargeIds = tworderapportiondetailList.stream()
                .map(Tworderapportiondetail::getCustomerchargeid)
                .collect(Collectors.toList());

        Map<Long, Tworderitem> tworderitemMap = orderitemServiceI.listByIds(orderItemIds)
                .stream()
                .collect(Collectors.toMap(Tworderitem::getId, Function.identity()));
        Map<Long, Twcustomercharge> twcustomerchargeMap = customerchargeServiceI.listByIds(customerChargeIds)
                .stream()
                .collect(Collectors.toMap(Twcustomercharge::getId, Function.identity()));

        for (Tworderapportiondetail apportionDetail : tworderapportiondetailList) {
            BigDecimal namountapportion = apportionDetail.getNamountapportion();
            Tworderitem tworderitem = tworderitemMap.get(apportionDetail.getOrderitemid());
            if (ObjUtil.isNull(tworderitem)) {
                throw new DataNotExistException("不存在对应的订单明细记录");
            }
            // 根据收费表id，找到收费表数据，——》将状态、已收金额、剩余金额分别回退
            Twcustomercharge twcustomercharge = twcustomerchargeMap.get(apportionDetail.getCustomerchargeid());
            if (ObjUtil.isNull(twcustomercharge)) {
                throw new DataNotExistException("不存在对应的收费表记录");
            }
            twcustomercharge.setNamountbalance(twcustomercharge.getNamountbalance().add(namountapportion));
            twcustomercharge.setNamounspent(twcustomercharge.getNamounspent().subtract(namountapportion));
            twcustomercharge.setIstatus(AppStatus.APPRSTATUS_PASS.getKey());
            innerInterceptor.recoredLog();
            if (!customerchargeServiceI.updateById(twcustomercharge)) {
                throw new DataNotExistException("更新收费表失败");
            }

            // 每一条分摊需要根据orderitemid，找到对应的订单详情，将分摊结果回退——》修改已收、欠款字段
            tworderitem.setAmountarrearage(tworderitem.getAmountarrearage().add(namountapportion));
            tworderitem.setAmountreceived(tworderitem.getAmountreceived().subtract(namountapportion));
            innerInterceptor.recoredLog();
            if (!orderitemServiceI.updateById(tworderitem)) {
                throw new DataNotExistException("更新订单明细失败");
            }

            // 更新退回后预开发票申请表已收金额
            if (twcustomercharge.getItype() == EcustomerChargeType.EntryType_BankPay.getKey()) {
                preinvoiceapplicationServiceI.updateReceivedAmount(twcustomercharge.getPreinvoiceapplicationid(), namountapportion.negate());
            }

            // 删除分摊表该记录，同时插入分摊历史表twinvoicebackhistory - suny 12-27 12:42:17
            Twinvoicebackhistory twinvoicebackhistory = new Twinvoicebackhistory(sdesc, apportionDetail, loginUser);
            innerInterceptor.recoredLog();
            if (!invoiceBackHistoryServiceI.save(twinvoicebackhistory)) {
                throw new DataNotExistException("保存分摊历史记录失败");
            }
            // 删除分摊表该记录
            innerInterceptor.recoredLog();
            if (!this.removeById(apportionDetail)) {
                throw new DataNotExistException("删除分摊表记录失败");
            }

            // 对佣金表进行冲抵或删除
            commissionServiceI.offsetCommission(apportionDetail.getOrderitemid(), sdesc);
        }

    }
}
