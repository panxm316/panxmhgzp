package com.hgzp.advertising.service.customer.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.emnus.approve.ApproveStatus;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerBelongVo;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerQueryVo;
import com.hgzp.advertising.service.customer.TwcustomerbelongServiceI;
import com.hgzp.advertising.service.system.TbdeptServiceI;
import com.hgzp.advertising.service.system.TbemployServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbdept;
import com.hgzp.core.model.Tbemploy;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.model.Twcustomerbelong;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.customer.TwcustomerMapper;
import com.hgzp.mapper.customer.TwcustomerbelongMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户归属业务员表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwcustomerbelongServiceImpl extends MyServiceImpl<TwcustomerbelongMapper, Twcustomerbelong> implements TwcustomerbelongServiceI {

    @Autowired
    private TwcustomerbelongMapper belongMapper;
    @Autowired
    private TbemployServiceI employService;
    @Autowired
    private TbdeptServiceI deptServiceI;
    @Autowired
    private TwcustomerMapper customerMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public List<CustomerBelongVo> getCustomerbelong(String sCustomerId) {
        List<Twcustomerbelong> belonglist = new LambdaQueryChainWrapper<>(belongMapper)
                .eq(Twcustomerbelong::getCustomerid, sCustomerId)
                .orderByAsc(Twcustomerbelong::getDcreatetime)
                .list();
        List<CustomerBelongVo> lsBelongData = new ArrayList<>();
        if (belonglist.size() > 0) {
            for (Twcustomerbelong record : belonglist) {
                CustomerBelongVo belongVo = new CustomerBelongVo();
                BeanUtils.copyProperties(record, belongVo);
                if (record.getEmployid() != null) {
                    Tbemploy empInfo = employService.getById(record.getEmployid());
                    if (empInfo != null) {
                        belongVo.setCustomerName(empInfo.getSusername());
                        belongVo.setDeptid(empInfo.getDeptid());
                        Tbdept tbdept = deptServiceI.getById(empInfo.getDeptid());
                        if (tbdept != null) {
                            belongVo.setDeptname(tbdept.getSdeptname());
                        }
                    }
                }
                lsBelongData.add(belongVo);
            }
        }
        return lsBelongData;
    }

    @Override
    public CustomerBelongVo getCustomerBelongById(String id) {
        Twcustomerbelong belongInfo = this.getById(id);
        CustomerBelongVo belongData = new CustomerBelongVo();
        if (belongInfo.getCustomerid() != null) {
            BeanUtils.copyProperties(belongInfo, belongData);
            Tbemploy empInfo = employService.getById(belongInfo.getCustomerid());
            if (empInfo != null) {
                belongData.setCustomerName(empInfo.getSusername());
            }
        }
        return belongData;
    }

    @Override
    public List<Long> getCustomerIdByEmpIds(List<Long> empIds) {
        List<Twcustomerbelong> list = this.lambdaQuery()
                .in(Twcustomerbelong::getEmployid, empIds)
                .list().stream().collect(Collectors.toList());

        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(Twcustomerbelong::getCustomerid).distinct().collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Json saveCustomerBelong(Twcustomerbelong twCustomerBelong) {
        if (isDuplicateInfo(twCustomerBelong)) {
            return Json.fail("已存在同名业务员");
        }
        if (twCustomerBelong.getDcreatetime() == null) {
            twCustomerBelong.setDcreatetime(new Date());
        }
        if (twCustomerBelong.getIapprovestatus() == null) {
            twCustomerBelong.setIapprovestatus(0);
        }
        //查询数据表中已有的业务员信息
        LambdaQueryWrapper<Twcustomerbelong> wrapperB = Wrappers.lambdaQuery();
        wrapperB.eq(Twcustomerbelong::getCustomerid, twCustomerBelong.getCustomerid())
                .eq(Twcustomerbelong::getBprimary, 1);
        Twcustomerbelong primaryDB = belongMapper.selectOne(wrapperB);
        //如果已有主业务员 新增业务员为非主业务员 不用更新客户表
        if (primaryDB != null) {
            twCustomerBelong.setBprimary(false);
            innerInterceptor.recoredLog(twCustomerBelong.getCustomerid());
            save(twCustomerBelong);
        } else {
            //没有主业务员且新增记录也非主业务员 给出提示返回
            if (!twCustomerBelong.getBprimary()) {
                return Json.fail("要有一个主业务员");
            }
            //新增记录是主业务员 需要修改客户表
            innerInterceptor.recoredLog(twCustomerBelong.getCustomerid());
            save(twCustomerBelong);
            //更新客户的主业务员
            LambdaQueryWrapper<Twcustomer> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Twcustomer::getId, twCustomerBelong.getCustomerid());
            Twcustomer twcustomer = customerMapper.selectOne(wrapper);
            twcustomer.setEmployid(twCustomerBelong.getEmployid());
            Tbemploy empInfo = employService.getById(twCustomerBelong.getEmployid());
            if (empInfo != null) {
                twcustomer.setEmployname(empInfo.getSusername());
            }
            innerInterceptor.recoredLog();
            customerMapper.updateById(twcustomer);
        }
        return Json.success();
    }

    @Override
    public void updateCustomerBelong(Twcustomerbelong twCustomerBelong) throws Exception {
        if (isDuplicateInfo(twCustomerBelong)) {
            throw new DataExistException("已存在同名业务员!");
        }
        //查询数据表中已有的主业务员信息
        LambdaQueryWrapper<Twcustomerbelong> wrapperB = Wrappers.lambdaQuery();
        wrapperB.eq(Twcustomerbelong::getCustomerid, twCustomerBelong.getCustomerid())
                .ne(Twcustomerbelong::getId, twCustomerBelong.getId())
                .eq(Twcustomerbelong::getBprimary, 1);
        Twcustomerbelong primaryDB = belongMapper.selectOne(wrapperB);

        //如果是主业务员 更新原主业务员为非主业务员 且更新客户表
        if (twCustomerBelong.getBprimary()) {
            if (ObjectUtil.isNotNull(primaryDB)) {
                primaryDB.setBprimary(false);
                innerInterceptor.recoredLog(primaryDB.getCustomerid());
                updateById(primaryDB);
            }
            innerInterceptor.recoredLog(twCustomerBelong.getCustomerid());
            belongMapper.updateById(twCustomerBelong);
            //更新客户表
            LambdaQueryWrapper<Twcustomer> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(Twcustomer::getId, twCustomerBelong.getCustomerid());
            Twcustomer twcustomer = customerMapper.selectOne(wrapper);
            twcustomer.setEmployid(twCustomerBelong.getEmployid());
            Tbemploy empInfo = employService.getById(twCustomerBelong.getEmployid());
            if (empInfo != null) {
                twcustomer.setEmployname(empInfo.getSusername());
            }
            innerInterceptor.recoredLog();
            customerMapper.updateById(twcustomer);

        } else {
//            if (primaryDB == null) {
//                throw new RuntimeException("要有一个主业务员");
//            }
            innerInterceptor.recoredLog(twCustomerBelong.getCustomerid());
            updateById(twCustomerBelong);
        }

    }

    @Override
    public void deleteCustomerBelongByIds(String ids, boolean bCustomerId) throws Exception {
        String[] split = ids.split(",");
        List<Twcustomerbelong> lsCustomerbelong = new ArrayList<>();
        if (bCustomerId) {
            lsCustomerbelong = this.lambdaQuery()
                    .in(Twcustomerbelong::getCustomerid, split)
                    .list();
        } else {
            lsCustomerbelong = this.lambdaQuery()
                    .in(Twcustomerbelong::getId, split)
                    .list();
        }
        innerInterceptor.recoredLog();
        for (Twcustomerbelong t : lsCustomerbelong) {
            if (t.getBprimary()) {
                LambdaQueryWrapper<Twcustomer> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(Twcustomer::getId, t.getCustomerid());
                Twcustomer twcustomer = customerMapper.selectOne(wrapper);
                twcustomer.setEmployid((long) 0);
                twcustomer.setEmployname("");

                innerInterceptor.recoredLog();
                customerMapper.updateById(twcustomer);
            }
            belongMapper.deleteById(t);
        }
    }

    @Override
    public boolean isDuplicateInfo(Twcustomerbelong customerBelong) {
        LambdaQueryWrapper<Twcustomerbelong> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Twcustomerbelong::getEmployid, customerBelong.getEmployid())
                .eq(Twcustomerbelong::getCustomerid, customerBelong.getCustomerid())
                .ne(customerBelong.getId() != null, Twcustomerbelong::getId, customerBelong.getId());
        return belongMapper.selectOne(wrapper) != null;
    }

    @Override
    public void combineCustomerBelong(String sMainId, String subIds) throws Exception {
        List<Twcustomerbelong> lsMainCustomerbelong = this.lambdaQuery()
                .eq(Twcustomerbelong::getCustomerid, sMainId)
                .list();

        //被合并客户资料信息
        String[] split = subIds.split(",");
        List<Twcustomerbelong> lsSubCustomerbelong = this.lambdaQuery()
                .in(Twcustomerbelong::getCustomerid, split)
                .list();

        for (Twcustomerbelong itemBelong : lsSubCustomerbelong) {
            //sfileid存在不添加
            int iCount =
                    lsMainCustomerbelong.stream().filter(s -> s.getEmployid().equals(itemBelong.getEmployid())).collect(Collectors.toList()).size();
            if (iCount > 0) {
                continue;
            }
            itemBelong.setCustomerid(Long.parseLong(sMainId));
            itemBelong.setBprimary(false);
            innerInterceptor.recoredLog();
            updateById(itemBelong);
        }
    }

    @Override
    public List<Long> getCustomerIdByApplicationId(String applicationid) {
        List<Long> lsCustomerIds = new LambdaQueryChainWrapper<>(belongMapper)
                .select(Twcustomerbelong::getCustomerid)
                .eq(Twcustomerbelong::getApplicationid, applicationid)
                .list()
                .stream()
                .map(Twcustomerbelong::getCustomerid)
                .distinct()
                .collect(Collectors.toList());
        return lsCustomerIds;
    }

    @Override
    public IPage<Twcustomer> getMyCustomerPageList(Page<Twcustomer> page, CustomerQueryVo query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Long employid;
        if (query.getBcurflag() != null && query.getBcurflag()) {
            employid = user.getUserid();
        } else {
            employid = query.getEmployid();
        }
        List<Long> deptidList = new ArrayList<>();
        String deptids = "";
        // 人员id不为空时，部门条件不生效
        if (ObjectUtil.isNotEmpty(query.getEmployid())) {
            query.setDeptid(null);
        } else {
            if (query.getDeptid() != null) {
                deptidList = deptServiceI.getChildDeptId(query.getDeptid());
            } else {
                deptidList = Arrays.stream(user.getAuthedDeptIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
            }
            if (CollUtil.isNotEmpty(deptidList)) {
                deptids = String.join("','", deptidList.stream().map(String::valueOf).collect(Collectors.toList()));
            }
        }
        if (query.getEndTime() != null) {
            query.setEndTime(DateUtil.offsetDay(query.getEndTime(), 1));
        }

        LambdaQueryWrapper<Twcustomer> lqw = Wrappers.lambdaQuery();
        lqw.ge(query.getStartTime() != null, Twcustomer::getDcreatetime, query.getStartTime())
                .lt(query.getEndTime() != null, Twcustomer::getDcreatetime, query.getEndTime())
                // 已审批通过的
                .eq(Twcustomer::getIapprovestatus, ApproveStatus.APPROVE_PASS.getKey())
                // 有效的
                .eq(Twcustomer::getBuse, true)
                .inSql(ObjectUtil.isNotNull(employid), Twcustomer::getId, "select customerid from twcustomerbelong where employid = " + employid)
                .inSql(deptidList.size() > 0, Twcustomer::getId, "select customerid from twcustomerbelong where deptid in ('" + deptids + "')")
                .orderByDesc(Twcustomer::getEmployname)
                .orderByDesc(Twcustomer::getDcreatetime);
        IPage<Twcustomer> resulepage = customerMapper.selectPage(page, lqw);

        return resulepage;
    }
}
