package com.hgzp.advertising.service.ad.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.emnus.business.CommissionStatus;
import com.hgzp.advertising.emnus.customer.CustomerType;
import com.hgzp.advertising.emnus.schedule.PublishStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.business.vo.OrderAndItemVO;
import com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO;
import com.hgzp.advertising.pagemodel.commission.vo.OrderItemCommissionVO;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.ad.TworderitembelongServiceI;
import com.hgzp.advertising.service.business.CommissionServiceI;
import com.hgzp.advertising.service.business.TwpreinvoiceapplicationServiceI;
import com.hgzp.advertising.service.price.TbcommissionrategroupServiceI;
import com.hgzp.advertising.service.price.TbcommissionrateitemServiceI;
import com.hgzp.advertising.service.price.TbdiscountreduceServiceI;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.ad.TworderitemMapper;
import com.hgzp.mapper.ad.TworderitembelongMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 订单刊期归属表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TworderitembelongServiceImpl extends MyServiceImpl<TworderitembelongMapper, Tworderitembelong> implements TworderitembelongServiceI {
    @Autowired
    private TworderServiceI orderServiceI;

    @Autowired
    private TworderitemServiceI orderitemServiceI;

    @Autowired
    private TworderitembelongMapper orderitembelongMapper;
    @Autowired
    private TworderitemMapper orderitemMapper;

    @Autowired
    private TwpreinvoiceapplicationServiceI preinvoServiceI;

    @Autowired
    private CommissionServiceI commissionServiceI;

    @Autowired
    private TbcommissionrategroupServiceI commissionrategroupServiceI;

    @Autowired
    private TbcommissionrateitemServiceI commissionrateitemServiceI;

    @Autowired
    private TbdiscountreduceServiceI discountreduceServiceI;

    @Autowired
    private TbdeptServiceI deptServiceI;

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public List<Tworderitembelong> getOrderItemBelongByOrderItemId(String orderItemIds) throws Exception {
        String[] idList = orderItemIds.split(",");
        List<Tworderitembelong> orderitembelongList = new LambdaQueryChainWrapper<>(orderitembelongMapper)
                .in(Tworderitembelong::getOrderitemid, idList)
                .list();
        return orderitembelongList;
    }

    /**
     * 据业务员id、客户id获取所有合同VO
     * 方法功能:据业务员id、客户id获取所有合同VO，限 已审批通过&欠款>0
     *
     * @param employeeid
     * @param customerid
     * @param queryKey   合同号
     * @return java.util.List<com.hgzp.advertising.pagemodel.ad.vo.OrderforPreinvoapplyVO>
     * @author yanz
     * @date 2023/12/1 10:12
     */
    @Override
    public List<OrderforPreinvoapplyVO> getOrderforPreinvoapplyVOByEmployId(Long employeeid, Long customerid, String queryKey) {
        // 获取订单id
        List<Tworder> orders = getOrdersByEmployeeId(employeeid).stream()
                //装vo前对一下客户id（3种）
                .filter(order ->
                        Stream.of(order.getAgencytid(), order.getAgentid(), order.getCustomerid())
                                .filter(Objects::nonNull)
                                .anyMatch(customerid::equals))
                .filter(order -> order.getIapprovestatus() == ApproveStatus.APPROVE_PASS.getKey())
                .filter(order -> orderitemServiceI.getArrearagesSumMapByOrderIds(Collections.singletonList(order.getId())).get(order.getId()).compareTo(BigDecimal.ZERO) > 0)
                // 合同号
                .filter(order -> ObjUtil.isEmpty(queryKey) || order.getScontractnum().contains(queryKey))
                .collect(Collectors.toList());
        List<OrderforPreinvoapplyVO> orderVOs = orderitemServiceI.getOrderforPreinvoapplyVOS(orders);
        if (orderVOs == null) {
            return null;
        }
        return orderVOs;
    }


    /**
     * 据业务员id获取所有订单id
     * 方法功能:据业务员id获取所有订单id
     *
     * @param employeeid
     * @return java.util.List<java.lang.String>
     * @author yanz
     * @date 2023/12/7 8:45
     */
    private List<Tworder> getOrdersByEmployeeId(Long employeeid) {
        // orderBelong表只有 orderItem的id， orderItem表只有order的Id，想获取order，要跳3次
        // 这里使用三个表都有的合同号获取order
        List<String> contractNums = this.lambdaQuery()
                .eq(Tworderitembelong::getEmployid, employeeid)
                // 只取getScontractnum不为null的数据 - 为null的还不算合同
                .isNotNull(Tworderitembelong::getScontractnum)
                .ne(Tworderitembelong::getScontractnum, "")
                .select(Tworderitembelong::getScontractnum)
                .list()
                .stream()
                .map(Tworderitembelong::getScontractnum)
//                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
        Map<String, Tworder> orders = orderServiceI.getOrdersByContractNums(contractNums);

        return ObjUtil.isNotEmpty(orders) ?
                orders.values().stream().filter(Objects::nonNull).collect(Collectors.toList()) :
                Collections.emptyList();
    }

    @Override
    public Json deleteOrderItemBelong(String orderItemIds) throws Exception {
        String[] split = orderItemIds.split(",");
        List<Tworderitembelong> orderitembelongList = new LambdaQueryChainWrapper<>(orderitembelongMapper)
                .in(Tworderitembelong::getOrderitemid, split)
                .list();
        if (orderitembelongList.size() > 0) {
            removeBatchByIds(orderitembelongList.stream().map(Tworderitembelong::getId).collect(Collectors.toList()));
        }
        return Json.success();
    }

    @Override
    public List<DataCombo> getMainEmpinfo(Long orderItemId) {
        List<Tworderitembelong> orderitembelong = this.lambdaQuery()
                .eq(Tworderitembelong::getOrderitemid, orderItemId)
                .eq(Tworderitembelong::getBprimary, true)
                .list();
        List<DataCombo> lsResult = new ArrayList<>();
        for (Tworderitembelong belong : orderitembelong) {
            DataCombo dataCombo = new DataCombo();
            dataCombo.setId(belong.getEmployid().toString());
            dataCombo.setName(belong.getEmployname());
            lsResult.add(dataCombo);
        }
        return lsResult;
    }

    /**
     * 获取刊期归属列表（编辑业绩对象）
     * 方法功能: 据刊期id，获取归属列表
     *
     * @param orderitemId
     * @return java.util.List<com.hgzp.core.model.Tworderitembelong>
     * @author yanz
     * @date 2024/1/11 12:33
     */
    @Override
    public List<Tworderitembelong> getOrderBelongListByOrderitemId(Long orderitemId) {
        List<Tworderitembelong> orderitembelongList = this.lambdaQuery()
                .eq(Tworderitembelong::getOrderitemid, orderitemId)
                .list();
        return orderitembelongList;
    }

    /**
     * 保存业绩对象
     * 方法功能: 将入参DTO中的orderitem业绩金额和日期更新至对应刊期详情，更新修改后的刊期归属信息（List<Tworderitembelong>）
     *
     * @param commissionData
     * @return boolean
     * @author yanz
     * @date 2024/1/11 12:46
     */
    @Override
    public boolean saveCommisstionData(OrderAndItemDTO commissionData) {
        // 更新刊期详情 - 业绩金额、业绩日期
        Long orderitemid = commissionData.getOrderitemid();
        if (orderitemid == null) {
            throw new DataNotExistException("刊期id不存在");
        }
        Tworderitem tworderitem = orderitemServiceI.getById(orderitemid);
        if (tworderitem == null) {
            throw new DataNotExistException("刊期不存在");
        }
        tworderitem.setAmountachievement(commissionData.getAmountachievement());
        tworderitem.setDachievementdate(commissionData.getDachievementdate());
        innerInterceptor.recoredLog();
        if (!orderitemServiceI.updateById(tworderitem)) {
            throw new IllegalStateException("更新刊期失败");
        }
        // 保存刊期归属信息
        List<Tworderitembelong> orderitembelongList = commissionData.getOrderitembelongList();
        if (CollUtil.isNotEmpty(orderitembelongList)) {
            innerInterceptor.recoredLog();
            if (!saveOrUpdateBatch(orderitembelongList)) {
                throw new IllegalStateException("保存刊期归属信息失败");
            }
        }
        return true;
    }

    /**
     * 查询订单明细相关综合对象列表(佣金计提)
     * 方法功能: 查询 佣金计提 用到的 订单明细相关综合对象列表
     *
     * @param page
     * @param vo
     * @return java.util.List<com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO>
     * @author yanz
     * @date 2024/1/12 13:21
     */
    @Override
    public IPage<OrderItemCommissionDTO> getOrderAndItemDTOListForCommission(Page<OrderItemCommissionDTO> page, OrderItemCommissionVO vo) {
        Page<OrderItemCommissionDTO> pageResult = new Page<>();
        // 条件：
        //（OrderItemCommissionVO）时间范围、媒体、部门、计算状态（bcountflag默认false）
        //（ipublishstatus=4-见报或5-已发布，且核销完成，没有欠款amountarrearage=0，且业绩金额namountachievement>0）
        // 根据时间范围、媒体、ipublishstatus=4-见报或5-已发布，且核销完成，没有欠款amountarrearage=0，且业绩金额namountachievement>0 查询Tworderitem表，查询TworderitemList
        LambdaQueryWrapper<Tworderitem> lqw = Wrappers.lambdaQuery();
        // 日期限制 - 仅截止日期
        if (ObjUtil.isNotNull(vo.getEndTime())) {
            lqw.le(Tworderitem::getPrestartdate, vo.getEndTime());
        }
        // 媒体
        lqw.eq(vo.getMediaid() != null, Tworderitem::getMediaid, vo.getMediaid());
        // 发布状态：已发布、见报
        lqw.in(Tworderitem::getIpublishstatus,  PublishStatus.PUBLISH_Published.getKey());
        // 核销完成（没有欠款amountarrearage=0）
        lqw.eq(Tworderitem::getAmountarrearage, BigDecimal.ZERO);
        // 业绩金额namountachievement>0
        lqw.gt(Tworderitem::getAmountachievement, BigDecimal.ZERO);
        // 倒序
        lqw.orderByDesc(Tworderitem::getCreatetime).orderByDesc(Tworderitem::getId);
        List<Tworderitem> orderitems = orderitemServiceI.list(lqw);
        if (CollUtil.isEmpty(orderitems)) {
            return pageResult;
        }
        // 数据准备
        Map<Long, Tworderitem> orderitemidToEntity = orderitems.stream().collect(Collectors.toMap(Tworderitem::getId, Function.identity()));
        // 根据TworderitemList的itemid查询tworderitembelong表，查询tworderitembelongList
        List<Long> orderitemIds = orderitems.stream().map(Tworderitem::getId).collect(Collectors.toList());

        List<Long> deptIds = null;
        if (vo.getDeptid() != null) {
            deptIds = deptServiceI.getChildDeptId(vo.getDeptid());
        }
        List<Tworderitembelong> orderitembelongs = this.lambdaQuery()
                .in(Tworderitembelong::getOrderitemid, orderitemIds)
                .in(deptIds != null, Tworderitembelong::getDeptid, deptIds)
                .list();

        if (CollUtil.isEmpty(orderitembelongs)) {
            return pageResult;
        }
        List<Long> orderIds = orderitems.stream().map(Tworderitem::getOrderid).distinct().collect(Collectors.toList());
        List<Tworder> orders = orderServiceI.listByIds(orderIds);
        if (CollUtil.isEmpty(orders)) {
            return pageResult;
        }
        Map<Long, Tworder> orderidToEntity = orders.stream().collect(Collectors.toMap(Tworder::getId, Function.identity()));

        List<OrderItemCommissionDTO> results = new ArrayList<>();
        // 查找tbcommissionrategroup表中默认一条记录，
        Tbcommissionrategroup defaultGroup = commissionrategroupServiceI.lambdaQuery().eq(Tbcommissionrategroup::getBdefault, true).one();

        List<Twcommission> commissions = commissionServiceI.lambdaQuery()
                .in(Twcommission::getOrderitemid, orderitembelongs.stream().map(Tworderitembelong::getOrderitemid).collect(Collectors.toList()))
                .orderByDesc(Twcommission::getDcreatetime)
                .orderByDesc(Twcommission::getId)
                .list();

        OrderItemCommissionDTO paramDto = new OrderItemCommissionDTO();
        // 写入标记：true为不写入
        AtomicBoolean shouldContinue = new AtomicBoolean(false);
        Twcommission commission;
        for (Tworderitembelong orderitembelong : orderitembelongs) {
            paramDto.resetParamdto();
            shouldContinue.set(false);
            commission = null;

            Tworderitem currOrderitem = orderitemidToEntity.get(orderitembelong.getOrderitemid());
            Tworder currOrder = orderidToEntity.get(currOrderitem.getOrderid());

            paramDto.setNrateofrisk(defaultGroup.getNrateofrisk());
            paramDto.setCommissionrategroupid(defaultGroup.getId());
            paramDto.setTwcommissionid(null);
            paramDto.setDeptid(orderitembelong.getDeptid());
            paramDto.setDeptname(orderitembelong.getDeptname());
            paramDto.setEmployid(orderitembelong.getEmployid());
            paramDto.setEmployname(orderitembelong.getEmployname());
            paramDto.setEmploytype(orderitembelong.getEmploytype());

            if (CollUtil.isNotEmpty(commissions)) {
                Optional<Twcommission> first = commissions.stream().filter(c ->
                        c.getOrderitemid().equals(orderitembelong.getOrderitemid())
                                && c.getEmployid().equals(orderitembelong.getEmployid())
                                && c.getEmploytype() == orderitembelong.getEmploytype()
                ).findFirst();
                if (first.isPresent()) {
                    commission = first.get();
                }
            }
            // 根据tworderitembelongList分别判断：
            // 如果twcommission表中orderitemid与employid+employtype最新的一条，如果没找到对应数据，则写入OrderItemCommissionDTO对象数组，
            // 如果找到数据且bcancel=1，则写入OrderItemCommissionDTO对象数组。
            // 如果找到数据且 bcancel=0 & 计提金额 > 0
            //	如果查询条件中计算状态bcountflag=false，不需要写入OrderItemCommissionDTO对象数组，
            //	如果bcountflag=true，且icommissionstatus=0【计算】或=3【撤销】的，则写入OrderItemCommissionDTO对象数组
            // dto赋值twcommissionid：
            // 佣金计提表主键（如果bcountflag=false，则设null，如果为true，判断下列条件）
            // twcommission表中orderitemid与employid+employtype最新的一条，没有记录设置为false，
            // 有记录且bcancel=0且计提金额>0且icommissionstatus=0【计算】或=3【撤销】的，设为Twcommission表的id
            Boolean bcountflag = vo.getBcountflag() == null ? false : vo.getBcountflag();
            if (commission != null) {
                // 是否是冲抵状态
                boolean isCancel = commission.getBcancel();
                // 计提金额是否大于0
                boolean isCommissionGtZero = commission.getNcommission().compareTo(BigDecimal.ZERO) == 1;
                // 是否是已计算或已撤销状态
                boolean isCalculatedOrRevoked = commission.getIcommissionstatus() == CommissionStatus.COMMISSION_STATUS_CALCULATED.getKey() ||
                        commission.getIcommissionstatus() == CommissionStatus.COMMISSION_STATUS_REVOKED.getKey();

                // shouldContinue，false即写入
                if (isCancel || (isCommissionGtZero && bcountflag && isCalculatedOrRevoked)) {
                    shouldContinue.set(false);
                } else if (!isCancel && isCommissionGtZero && !bcountflag) {
                    shouldContinue.set(true);
                }

//                if (bcountflag && !isCancel && isCommissionGtZero && isCalculatedOrRevoked) {
                if (!isCancel && isCommissionGtZero && isCalculatedOrRevoked) {  // 20240307 suny 不需要判断bcountflag，只要是已计算或已撤销状态就写入
                    paramDto.setTwcommissionid(commission.getId());
                    paramDto.setVersion(commission.getVersion());
                }
            }
            if (shouldContinue.get()) {
                continue;
            }

            // 然后根据媒体（orderitem）、行业（order）、部门（Tworderitembelong）查找tbcommissionrateitem表中是否有相关记录，
            List<Tbcommissionrateitem> rateitems = commissionrateitemServiceI.lambdaQuery()
                    .eq(Tbcommissionrateitem::getCommissionrategroupid, defaultGroup.getId())
                    .eq(Tbcommissionrateitem::getMediaid, currOrderitem.getMediaid())
                    .eq(Tbcommissionrateitem::getAdindustryid, currOrder.getAdindustyid())
                    .eq(Tbcommissionrateitem::getDeptid, orderitembelong.getDeptid())
                    .orderByDesc(Tbcommissionrateitem::getId)
                    .list();
            // 如果有再查询是否有当前人员，
            //	如果有人员，则按照当前人员的属性（直客、代理、内容生产方）的折扣计算，
            //	如果没有人员则按照媒体（orderitem）、行业（order）、部门（Tworderitembelong）查出结果中属性（直客、代理、内容生产方）符合orderbelong表人员类型（直客、代理、内容生产方）的计算，
            //  折扣要先匹配所在折扣范围，再匹配客户类型
            Optional<Tbcommissionrateitem> firstEmp = rateitems.stream()
                    .filter(rateitem -> rateitem.getEmployid() == orderitembelong.getEmployid())
                    .sorted(Comparator.comparing(Tbcommissionrateitem::getId).reversed()).findFirst();

            //去tbdiscountreduce表中查询是否有符合条件的折扣下点，
            // 折扣下点注意匹配当前组id、媒体、行业、部门，当前折扣率所在折扣区间
            //如果有则进行计算【提成金额=业绩金额*（业绩比例%）*（提成比例-折扣下点）%】，填充对应的字段：（佣金参数组id、佣金参数明细id、计提比例、计提金额、折扣降点）
            Optional<Tbdiscountreduce> discountreduce = discountreduceServiceI.lambdaQuery()
                    .eq(Tbdiscountreduce::getCommissionrategroupid, defaultGroup.getId())
                    .le(Tbdiscountreduce::getNdiscountstart, currOrderitem.getNdiscountrate())
                    .ge(Tbdiscountreduce::getNdiscountend, currOrderitem.getNdiscountrate())
                    .orderByDesc(Tbdiscountreduce::getId)
                    .list().stream().findFirst();

            // (按人员条件)如果没查到结果，则按照查找tbcommissionrategroup表中默认一条记录组表中默认的人员的属性（直客、代理、内容生产方）提成比例计算，再去tbdiscountreduce表中查询是否有符合条件的折扣下点，
            Optional<Tbcommissionrateitem> rateitemForcalc = firstEmp;
            if (!firstEmp.isPresent() && CollUtil.isNotEmpty(rateitems)) {
                rateitemForcalc = rateitems.stream()
                        .filter(rateitem -> rateitem.getEmployid() == null)
                        .max(Comparator.comparing(Tbcommissionrateitem::getId));
            }
            if (rateitemForcalc.isPresent()) {
                paramDto.setCommissionrateitemid(rateitemForcalc.get().getId());
            }
            // 若查询已计算，不进行计算，数值从数据表中取
            if (bcountflag && commission != null) {
                calculateRateAndDiscount(orderitembelong, rateitemForcalc, discountreduce, defaultGroup, paramDto);
                // 避免覆盖
                paramDto.setRiskfund(commission.getAmountachievement().multiply(commission.getNrateofrisk().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
                paramDto.setNcommission(commission.getNcommission().setScale(2, RoundingMode.HALF_UP));
                paramDto.setNcommissionrate(commission.getNcommissionrate());
            } else {
                calculateCommission(orderitembelong, currOrderitem, rateitemForcalc, discountreduce, defaultGroup, paramDto);
            }


            //  拼结果
            OrderAndItemDTO orderAndItemDTO = OrderAndItemDTO.forCommissionDTO(
                    currOrder,
                    currOrderitem,
                    "");
            paramDto.setArchievementrate(orderitembelong.getArchievementrate());
            OrderItemCommissionDTO result = OrderItemCommissionDTO.from(orderAndItemDTO, defaultGroup, paramDto);
            results.add(result);
        }

        Long current = page.getCurrent();
        Long size = page.getSize();
        Long skip = (current - 1) * size;
        pageResult.setSize(size);
        pageResult.setCurrent(current);
        pageResult.setTotal(results.size());

        // 人工分页
        results = results.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(OrderItemCommissionDTO::getPrestartdate).reversed())
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());
        pageResult.setRecords(results);

        return pageResult;
    }

    /**
     * 中间步骤
     * 方法功能:计算提成金额，并将相关属性存入拼dto用的参数对象paramDto
     *
     * @param orderitembelong 当前循环的订单明细归属
     * @param currOrderitem   当前明细归属对应的订单明细
     * @param rateItemOpt     当前明细归属对应的计提比例明细
     * @param discountreduce  当前明细归属对应的折扣下点
     * @param defaultGroup    默认的计提比例组
     * @param paramDto        当前循环的拼DTO属性用 佣金计提对象
     * @return void
     * @author yanz
     * @date 2024/1/15 16:51
     */
    private void calculateCommission(Tworderitembelong orderitembelong,
                                     Tworderitem currOrderitem,
                                     Optional<Tbcommissionrateitem> rateItemOpt,
                                     Optional<Tbdiscountreduce> discountreduce,
                                     Tbcommissionrategroup defaultGroup,
                                     OrderItemCommissionDTO paramDto) {
        calculateRateAndDiscount(orderitembelong, rateItemOpt, discountreduce, defaultGroup, paramDto);

        BigDecimal rate = paramDto.getNcommissionrate();
        BigDecimal discount = paramDto.getDiscountreduce();

        // 【提成金额=业绩金额*（业绩比例%）*（提成比例-折扣下点）%】
        BigDecimal commissionAmount = currOrderitem.getAmountachievement()
                .multiply(orderitembelong.getArchievementrate().divide(new BigDecimal("100")))
                .multiply((rate.subtract(discount)).divide(new BigDecimal("100")));

        paramDto.setRiskfund(currOrderitem.getAmountachievement().multiply(defaultGroup.getNrateofrisk().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
        paramDto.setNcommission(commissionAmount.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * 中间步骤
     * 方法功能:设置计提比例、折扣点数，并将相关属性存入拼dto用的参数对象paramDto
     *
     * @param orderitembelong
     * @param rateItemOpt
     * @param discountreduce
     * @param defaultGroup
     * @param paramDto
     * @return void
     * @author yanz
     * @date 2024/1/17 10:20
     */
    private void calculateRateAndDiscount(Tworderitembelong orderitembelong,
                                          Optional<Tbcommissionrateitem> rateItemOpt,
                                          Optional<Tbdiscountreduce> discountreduce,
                                          Tbcommissionrategroup defaultGroup,
                                          OrderItemCommissionDTO paramDto) {
        BigDecimal rateOfCustomer = rateItemOpt.map(Tbcommissionrateitem::getNrateofcustomer).orElse(defaultGroup.getNrateofcustomer());
        BigDecimal rateOfAgency = rateItemOpt.map(Tbcommissionrateitem::getNrateofagency).orElse(defaultGroup.getNrateofagency());
        BigDecimal rateOfAgent = rateItemOpt.map(Tbcommissionrateitem::getNrateofagent).orElse(defaultGroup.getNrateofagent());

        BigDecimal rate;
        Function<Tbdiscountreduce, BigDecimal> discountFunction;
        switch (CustomerType.getTypeByKey(orderitembelong.getEmploytype())) {
            case CustomerType_ZK:
                rate = rateOfCustomer;
                discountFunction = Tbdiscountreduce::getNrateofcustomer;
                break;
            case CustomerType_Proxy:
                rate = rateOfAgency;
                discountFunction = Tbdiscountreduce::getNrateofagency;
                break;
            case CustomerType_Producer:
                rate = rateOfAgent;
                discountFunction = Tbdiscountreduce::getNrateofagent;
                break;
            default:
                throw new IllegalArgumentException("不支持的客户类型");
        }

        BigDecimal discount = discountreduce.map(discountFunction).orElse(BigDecimal.ZERO);
        paramDto.setNcommissionrate(rate);
        paramDto.setDiscountreduce(discount);
    }

    @Override
    public IPage<OrderAndItemDTO> getMyAchievementPageList(Page<Tworderitembelong> page, OrderAndItemVO vo) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        List<Long> deptidList = new ArrayList<>();
        //如果只查当前人员时，部门条件不生效
        if (vo.getBcurflag() != null && vo.getBcurflag()) {
            vo.setEmployid(user.getUserid());
            vo.setDeptid(null);
        }
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
        } else {
            vo.setEndTime(DateUtil.offsetDay(new Date(), 1));
        }
        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日期
        String startTime = dateFm.format(vo.getStartTime());
        String endTime = dateFm.format(vo.getEndTime());
        LambdaQueryWrapper<Tworderitembelong> lqw = Wrappers.lambdaQuery();
        lqw.eq(vo.getEmployid() != null, Tworderitembelong::getEmployid, vo.getEmployid())
                .in(deptidList.size() > 0, Tworderitembelong::getDeptid, deptidList)
                .inSql(Tworderitembelong::getOrderitemid, "select id from tworderitem where prestartdate >='" + startTime + "' and prestartdate <'" + endTime + "'")
                .orderByDesc(Tworderitembelong::getEmployname)
                .orderByDesc(Tworderitembelong::getCreatetime);
        IPage<Tworderitembelong> pagebelong = orderitembelongMapper.selectPage(page, lqw);
        List<Tworderitembelong> orderitembelongList = pagebelong.getRecords();

        List<OrderAndItemDTO> orderAndItemDTOList = new ArrayList<>();
        orderitembelongList.forEach(orderitembelong -> {
            Tworderitem orderitem = orderitemServiceI.getById(orderitembelong.getOrderitemid());
            Tworder order = orderServiceI.getById(orderitem.getOrderid());
            OrderAndItemDTO orderAndItemDTO = OrderAndItemDTO.from(order, orderitem);
            orderAndItemDTO.setEmployid(orderitembelong.getEmployid());
            orderAndItemDTO.setEmployname(orderitembelong.getEmployname());
            orderAndItemDTO.setEmploytype(orderitembelong.getEmploytype());
            // 业绩金额：根据归属表中业绩比例与广告明细表中的业绩金额计算
            orderAndItemDTO.setAmountachievement(orderitem.getAmountachievement().multiply(orderitembelong.getArchievementrate().divide(new BigDecimal("100"))));
            orderAndItemDTOList.add(orderAndItemDTO);
        });

        List<OrderAndItemDTO> orderAndItemDTOList1 = orderAndItemDTOList.stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(OrderAndItemDTO::getPrestartdate).reversed())
                .collect(Collectors.toList());
        IPage<OrderAndItemDTO> pageResult = new Page<>();
        pageResult.setTotal(pagebelong.getTotal());
        pageResult.setRecords(orderAndItemDTOList1);
        return pageResult;
    }

    @Override
    public Tworderitem getMyAchievementCount(OrderAndItemVO vo) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        List<Long> deptidList = new ArrayList<>();
        //如果只查当前人员时，部门条件不生效
        if (vo.getBcurflag() != null && vo.getBcurflag()) {
            vo.setEmployid(user.getUserid());
            vo.setDeptid(null);
        }
        // 人员id不为空时，部门条件不生效
        if (ObjectUtil.isNotEmpty(vo.getEmployid())) {
            vo.setDeptid(null);
        }
        if (vo.getDeptid() != null) {
            deptidList = deptServiceI.getChildDeptId(vo.getDeptid());
        } else {
            deptidList = Arrays.stream(user.getAuthedDeptIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
        }
        if (vo.getEndTime() != null) {
            vo.setEndTime(DateUtil.offsetDay(vo.getEndTime(), 1));
        } else {
            vo.setEndTime(DateUtil.offsetDay(new Date(), 1));
        }
        if (vo.getCustomerid() != null) {
            // 如果客户id不为空，业务员id、部门id不生效
            vo.setEmployid(null);
            vo.setDeptid(null);
            deptidList = new ArrayList<>();
            // 查询客户业绩
            Tworderitem tworderitem = orderitemMapper.getSumCustomerAchievement(vo.getStartTime(), vo.getEndTime(), vo.getMediaid(), vo.getCustomerid());
            return tworderitem;
        } else {
            // 查询人员业绩
            Tworderitem tworderitem = orderitemMapper.getSumAchievement(vo.getStartTime(), vo.getEndTime(), vo.getMediaid(), deptidList, vo.getEmployid(), vo.getCustomerid());
            return tworderitem;
        }
    }
}
