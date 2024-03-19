package com.hgzp.advertising.service.business.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.emnus.business.CommissionStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.OrderAndItemDTO;
import com.hgzp.advertising.pagemodel.commission.dto.OrderItemCommissionDTO;
import com.hgzp.advertising.pagemodel.commission.vo.OrderItemCommissionVO;
import com.hgzp.advertising.pagemodel.finance.dto.CommissionDTO;
import com.hgzp.advertising.service.ad.TworderServiceI;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.ad.TworderitembelongServiceI;
import com.hgzp.advertising.service.business.CommissionServiceI;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.Twcommission;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.model.Tworderitembelong;
import com.hgzp.mapper.finance.TwcommissionMapper;
import com.hgzp.pagemodel.business.OrderItemAndCommissionDTO;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 佣金计提明细表 服务实现类
 * </p>
 *
 * @author wangxk
 * @since 2024-01-06
 */
@Service
public class CommissionServiceImpl extends ServiceImpl<TwcommissionMapper, Twcommission> implements CommissionServiceI {

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TwcommissionMapper twcommissionMapper;
    @Autowired
    private TworderServiceI tworderServiceI;
    @Autowired
    private TworderitemServiceI tworderitemServiceI;
    @Autowired
    private TworderitembelongServiceI tworderitembelongServiceI;
    @Autowired
    private TbdeptServiceI deptServiceI;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdata(CommissionDTO commissionDTO) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Twcommission twcommission;
        if (ObjUtil.isEmpty(commissionDTO.getId())) {
            twcommission = new Twcommission();
            BeanUtils.copyProperties(commissionDTO, twcommission);
            twcommission.setOrderid(0L);
            twcommission.setOrderitemid(0L);
            twcommission.setOrderitemcode(0L);
            twcommission.setScontractnum("");
            twcommission.setCommissionrategroupid(0L);
            twcommission.setCreateempid(user.getUserid());
            twcommission.setCreateempname(user.getUsername());
            twcommission.setDcreatetime(new Date());
            twcommission.setIcommissionstatus(CommissionStatus.COMMISSION_STATUS_EDIT.getKey());
            twcommission.setBcancel(false);
            twcommission.setVersion(0L);
        } else {
            twcommission = this.getById(commissionDTO.getId());
            BeanUtils.copyProperties(commissionDTO, twcommission);
        }
        twcommission.setCustomerid(ObjectUtil.isNull(commissionDTO.getCustomerid()) ? 0L : commissionDTO.getCustomerid());
        twcommission.setCustomername(ObjectUtil.isNotEmpty(commissionDTO.getCustomername()) ? commissionDTO.getCustomername() : "");
        innerInterceptor.recoredLog();
        if (ObjectUtil.isNotEmpty(commissionDTO.getId())) {
            boolean success = updateById(twcommission);
            if (!success) {
                throw new IllegalStateException("Twcommission保存失败");
            }
        } else {
            boolean success = save(twcommission);
            if (!success) {
                throw new IllegalStateException("Twcommission保存失败");
            }
        }
    }

    @Override
    public IPage<OrderItemCommissionDTO> getCommissionList(Page<Twcommission> page, OrderItemCommissionVO query) throws Exception {
        LambdaQueryWrapper<Twcommission> lqw = Wrappers.lambdaQuery();
        List<Long> deptIds;
        if (query.getDeptid() != null) {
            deptIds = deptServiceI.getChildDeptId(query.getDeptid());
            lqw.in(Twcommission::getDeptid, deptIds);
        }
        lqw.eq(query.getEmployid() != null, Twcommission::getEmployid, query.getEmployid());
        lqw.ge(query.getStartTime() != null, Twcommission::getDcreatetime, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twcommission::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }

        //统计查询使用
        if (query.getBstatflag() != null && query.getBstatflag()) {
            lqw.and(i -> i.eq(Twcommission::getIcommissionstatus, CommissionStatus.COMMISSION_STATUS_CONFIRMED.getKey())
                    .or()
                    .eq(Twcommission::getIcommissionstatus, CommissionStatus.COMMISSION_STATUS_ISSUED.getKey()));
        } else {
            lqw.eq(query.getIcommissionstatus() != null, Twcommission::getIcommissionstatus, query.getIcommissionstatus());
        }
        // 20240318 suny 增加查询条件,查询已提交的佣金计提明细
        lqw.ne(Twcommission::getIcommissionstatus, CommissionStatus.COMMISSION_STATUS_EDIT.getKey());
        lqw.orderByDesc(Twcommission::getDcreatetime);
        IPage<Twcommission> pages = twcommissionMapper.selectPage(page, lqw);
        IPage<OrderItemCommissionDTO> resulepage = new Page<>();
        List<OrderItemCommissionDTO> result = new ArrayList<>();
        for (Twcommission record : pages.getRecords()) {
            OrderItemCommissionDTO orderItemCommissionDTO = new OrderItemCommissionDTO();
            if (record.getOrderid() == 0L) { // 20240316 suny 手动添加佣金明细查询修改
                orderItemCommissionDTO.setEmployid(record.getEmployid());
                orderItemCommissionDTO.setDeptname(record.getDeptname());
                orderItemCommissionDTO.setDeptid(record.getDeptid());
                orderItemCommissionDTO.setEmployname(record.getEmployname());
                orderItemCommissionDTO.setPrestartdate(record.getDcreatetime());
            } else {
                Tworder tworder = tworderServiceI.getById(record.getOrderid());
                Tworderitem tworderitem = tworderitemServiceI.getById(record.getOrderitemid());
                OrderAndItemDTO orderAndItemDTO = OrderAndItemDTO.from(tworder, tworderitem);

                BeanUtils.copyProperties(orderAndItemDTO, orderItemCommissionDTO);

            }
            orderItemCommissionDTO.setEmploytype(record.getEmploytype());
            orderItemCommissionDTO.setArchievementrate(record.getArchievementrate());
            orderItemCommissionDTO.setDeptname(record.getDeptname());
            orderItemCommissionDTO.setDeptid(record.getDeptid());
            orderItemCommissionDTO.setTwcommissionid(record.getId());
            orderItemCommissionDTO.setNrateofrisk(record.getNrateofrisk());
            orderItemCommissionDTO.setRiskfund(record.getAmountachievement().multiply(record.getNrateofrisk()).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
            orderItemCommissionDTO.setNcommissionrate(record.getNcommissionrate());
            orderItemCommissionDTO.setNcommission(record.getNcommission());
            orderItemCommissionDTO.setIcommissionstatus(record.getIcommissionstatus());
            orderItemCommissionDTO.setSconfirmremark(record.getSconfirmremark());
            orderItemCommissionDTO.setSpayremark(record.getSpayremark());
            orderItemCommissionDTO.setVersion(record.getVersion());
            orderItemCommissionDTO.setDpaydate(record.getDpaydate());
            orderItemCommissionDTO.setDconfirmdate(record.getDconfirmdate());
            result.add(orderItemCommissionDTO);
        }
        resulepage.setRecords(result);
        resulepage.setTotal(pages.getTotal());
        return resulepage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCommission(List<OrderItemCommissionDTO> entitylist) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        if (entitylist == null || entitylist.size() == 0) {
            throw new DataNotExistException("参数不能为空");
        }
        // 20240307 suny 改成批量更新
        for (OrderItemCommissionDTO entity : entitylist) {
            if (entity == null || entity.getTwcommissionid() == null || entity.getIcommissionstatus() == null) {
                throw new DataNotExistException("参数不能为空");
            }
            Twcommission twcommission = this.getById(entity.getTwcommissionid());
            //判断佣金计提表中业绩金额与Tworderitem表中业绩金额是否一致
            Tworderitem tworderitem = tworderitemServiceI.getById(twcommission.getOrderitemid());
            if (tworderitem.getAmountachievement().compareTo(twcommission.getAmountachievement()) != 0) {
                throw new DataNotExistException("佣金计提表中业绩金额与订单刊期表中业绩金额不一致，请重新计算");
            }
            twcommission.setIcommissionstatus(entity.getIcommissionstatus());
            if (entity.getIcommissionstatus() == CommissionStatus.COMMISSION_STATUS_CONFIRMED.getKey()) {
                twcommission.setSconfirmremark(entity.getSconfirmremark());
                twcommission.setDconfirmdate(new Date());
                twcommission.setConfirmempid(user.getUserid());
                twcommission.setConfirmempname(user.getUsername());
            } else if (entity.getIcommissionstatus() == CommissionStatus.COMMISSION_STATUS_ISSUED.getKey()) {
                twcommission.setSpayremark(entity.getSpayremark());
                twcommission.setDpaydate(new Date());
                twcommission.setPayempid(user.getUserid());
                twcommission.setPayempname(user.getUsername());
            }

            twcommission.setVersion(entity.getVersion());
            innerInterceptor.recoredLog();
            boolean success = updateById(twcommission);
            if (!success) {
                throw new IllegalStateException("Twcommission保存失败");
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCommission(String ids) throws Exception {
        if (ids == null) {
            throw new DataNotExistException("参数不能为空");
        }
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            Twcommission twcommission = this.getById(id);
            if (twcommission == null) {
                throw new DataNotExistException("佣金计提信息不存在");
            }
            if (twcommission.getIcommissionstatus() == CommissionStatus.COMMISSION_STATUS_ISSUED.getKey()) {
                throw new DataExistException("佣金计提信息已发放，不能删除");
            }
            innerInterceptor.recoredLog();
            boolean success = removeById(id);
            if (!success) {
                throw new IllegalStateException("Twcommission删除失败");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateCommission(OrderItemCommissionDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        if (entity == null) {
            throw new DataNotExistException("参数不能为空");
        }
        if (entity.getTwcommissionid() == null) {
            Tworder tworder = tworderServiceI.getById(entity.getOrderid());
            Tworderitem tworderitem = tworderitemServiceI.getById(entity.getOrderitemid());
            Tworderitembelong tworderitembelong = tworderitembelongServiceI.lambdaQuery()
                    .eq(Tworderitembelong::getOrderitemid, entity.getOrderitemid())
                    .eq(Tworderitembelong::getEmployid, entity.getEmployid()).last("limit 1").one();
            // 新增一条佣金计提明细
            Twcommission twcommission = new Twcommission();
            BeanUtils.copyProperties(entity, twcommission);
            twcommission.setOrderitemcode(tworderitem.getIitemcode());
            twcommission.setDeptid(tworderitembelong.getDeptid());
            twcommission.setDeptname(tworderitembelong.getDeptname());
            twcommission.setCreateempid(user.getUserid());
            twcommission.setCreateempname(user.getUsername());
            twcommission.setDcreatetime(new Date());
            twcommission.setEmploytype(tworderitembelong.getEmploytype());
            twcommission.setBprimary(tworderitembelong.getBprimary());
            twcommission.setCustomerid(tworder.getCustomerid());
            twcommission.setCustomername(tworder.getCustomername());
            twcommission.setAmountachievement(tworderitem.getAmountachievement());
            twcommission.setIcommissionstatus(CommissionStatus.COMMISSION_STATUS_CALCULATED.getKey());
            twcommission.setBcancel(false);
            twcommission.setVersion(0L);
            innerInterceptor.recoredLog();
            boolean success = save(twcommission);
            if (!success) {
                throw new IllegalStateException("Twcommission保存失败");
            }

        } else {
            Twcommission twcommission = this.getById(entity.getTwcommissionid());
            //判断佣金计提表中业绩金额与Tworderitem表中业绩金额是否一致
            Tworderitem tworderitem = tworderitemServiceI.getById(twcommission.getOrderitemid());
            if (tworderitem.getAmountachievement().compareTo(twcommission.getAmountachievement()) != 0) {
                throw new DataNotExistException("佣金计提表中业绩金额与订单刊期表中业绩金额不一致，请重新计算");
            }
            twcommission.setIcommissionstatus(entity.getIcommissionstatus());
            twcommission.setNcommissionrate(entity.getNcommissionrate());
            twcommission.setNcommission(entity.getNcommission());
            twcommission.setVersion(entity.getVersion());
            innerInterceptor.recoredLog();
            boolean success = updateById(twcommission);
            if (!success) {
                throw new IllegalStateException("Twcommission保存失败");
            }
        }
    }

    @Override
    public void updateCommissionCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception {
        try {
            String[] splitids = customerIds.split(",");
            List<Twcommission> resourceas = this.lambdaQuery()
                    .in(Twcommission::getCustomerid, splitids)
                    .list();
            if (resourceas.size() > 0) {
                resourceas.forEach(item -> {
                    item.setCustomerid(newcustomerId);
                    item.setCustomername(newcustomername);
                });

                innerInterceptor.recoredLog();
                this.updateBatchById(resourceas);
            }
        } catch (Exception e0) {
            throw new IllegalStateException("佣金计提合并失败！" + e0.getMessage());
        }
    }

    @Override
    public List<OrderItemCommissionDTO> getCommissionListByItemId(Long orderitemid) throws Exception {
        LambdaQueryWrapper<Twcommission> lqw = Wrappers.lambdaQuery();
        lqw.eq(Twcommission::getOrderitemid, orderitemid);
        List<Twcommission> twcommissionList = twcommissionMapper.selectList(lqw);
        List<OrderItemCommissionDTO> result = new ArrayList<>();
        twcommissionList.forEach(twcommission -> {
            OrderItemCommissionDTO orderItemCommissionDTO = new OrderItemCommissionDTO();
            BeanUtils.copyProperties(twcommission, orderItemCommissionDTO);
            result.add(orderItemCommissionDTO);
        });
        return result;
    }

    @Override
    public void offsetCommission(Long orderitemid, String sdesc) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        LambdaQueryWrapper<Twcommission> lqw = Wrappers.lambdaQuery();
        lqw.eq(Twcommission::getOrderitemid, orderitemid);
        // 未冲抵的，且佣金大于0的（小于0的代表冲抵数据，不允许处理）
        lqw.eq(Twcommission::getBcancel, false);
        lqw.gt(Twcommission::getNcommission, 0);
        List<Twcommission> twcommissionList = twcommissionMapper.selectList(lqw);
        twcommissionList.forEach(twcommission -> {
            if (twcommission.getIcommissionstatus() == CommissionStatus.COMMISSION_STATUS_ISSUED.getKey()) {
                // 已发放的需要冲抵，同时添加一条冲抵数据
                Twcommission twcommission1 = new Twcommission();
                BeanUtils.copyProperties(twcommission, twcommission1);
                Long newid = IdUtil.getSnowflakeNextId();
                twcommission1.setId(newid);
                twcommission1.setVersion(0L);
                twcommission1.setEmployid(user.getUserid());
                twcommission1.setCreateempname(user.getUsername());
                twcommission1.setDcreatetime(new Date());
                twcommission1.setIcommissionstatus(CommissionStatus.COMMISSION_STATUS_CALCULATED.getKey());
                twcommission1.setNcommission(twcommission.getNcommission().negate());
                twcommission1.setConfirmempid(null);
                twcommission1.setConfirmempname(null);
                twcommission1.setDconfirmdate(null);
                twcommission1.setSconfirmremark(null);
                twcommission1.setDpaydate(null);
                twcommission1.setPayempid(null);
                twcommission1.setPayempname(null);
                twcommission1.setSpayremark(null);
                twcommission1.setRelatedid(twcommission.getId());
                // 冲抵的数据，需要添加备注
                twcommission1.setSremark(sdesc);

                innerInterceptor.recoredLog();
                boolean success = save(twcommission1);
                if (!success) {
                    throw new RuntimeException("Twcommission保存失败");
                }

                twcommission.setRelatedid(newid);
                twcommission.setBcancel(true);
                innerInterceptor.recoredLog();
                success = updateById(twcommission);
                if (!success) {
                    throw new RuntimeException("Twcommission保存失败");
                }
            } else {
                boolean success = removeById(twcommission);
                if (!success) {
                    throw new RuntimeException("Twcommission删除失败");
                }
            }
        });
    }

    @Override
    public OrderItemCommissionDTO getMyCommissionCount(OrderItemCommissionVO vo) throws Exception {
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

        OrderItemAndCommissionDTO orderitemandcommission = twcommissionMapper.getSumCommission(vo.getStartTime(), vo.getEndTime(), deptidList, vo.getEmployid());
        OrderItemCommissionDTO orderItemCommissionDTO = new OrderItemCommissionDTO();
        if (orderitemandcommission != null) {
            BeanUtils.copyProperties(orderitemandcommission, orderItemCommissionDTO);
        }
        return orderItemCommissionDTO;
    }

    @Override
    public IPage<Twcommission> getCommissionListByAddType(Page<Twcommission> page, OrderItemCommissionVO query, String type) throws Exception {
        LambdaQueryWrapper<Twcommission> lqw = Wrappers.lambdaQuery();
        lqw.eq("add".equals(type), Twcommission::getOrderid, 0);
        List<Long> deptIds;
        if (query.getDeptid() != null) {
            deptIds = deptServiceI.getChildDeptId(query.getDeptid());
            lqw.in(Twcommission::getDeptid, deptIds);
        }
        lqw.eq(query.getEmployid() != null, Twcommission::getEmployid, query.getEmployid());
        lqw.ge(query.getStartTime() != null, Twcommission::getDcreatetime, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twcommission::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }

        //统计查询使用
        if (query.getBstatflag() != null && query.getBstatflag()) {
            lqw.and(i -> i.eq(Twcommission::getIcommissionstatus, CommissionStatus.COMMISSION_STATUS_CONFIRMED.getKey())
                    .or()
                    .eq(Twcommission::getIcommissionstatus, CommissionStatus.COMMISSION_STATUS_ISSUED.getKey()));
        } else {
            lqw.eq(query.getIcommissionstatus() != null, Twcommission::getIcommissionstatus, query.getIcommissionstatus());
        }
        lqw.orderByDesc(Twcommission::getDcreatetime);
        IPage<Twcommission> pages = twcommissionMapper.selectPage(page, lqw);
        return pages;
    }
}
