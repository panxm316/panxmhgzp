package com.hgzp.advertising.service.finance.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.finance.TwcustomeraccountsServiceI;
import com.hgzp.core.model.Twcustomer;
import com.hgzp.core.model.Twcustomeraccounts;
import com.hgzp.mapper.customer.TwcustomerMapper;
import com.hgzp.mapper.finance.TwcustomeraccountsMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 客户账户表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwcustomeraccountsServiceImpl extends ServiceImpl<TwcustomeraccountsMapper, Twcustomeraccounts> implements TwcustomeraccountsServiceI {

    @Autowired
    TwcustomerMapper twcustomerMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor logInterceptor;

    /**
     * 更新客户账户余额
     * 方法功能:
     * 1、首先判断客户是否存在账户，如果不存在则创建账户
     * 2、更新客户账户余额、操作员
     *
     * @param customerid      客户id
     * @param namountreceived 入账金额
     * @param logSlaveId      记录日志的子数据id
     * @return void
     * @author wangwk
     * @date 2023/10/28 15:06
     */
    @Override
    public void updateCustomeraccountsNamountbalance(Long customerid, BigDecimal namountreceived, Long logSlaveId) throws Exception {
        LoginUser loginUser = WebUtil.getLoginUser();

        //判断客户是否存在账户，如果不存在则创建账户
        Twcustomeraccounts twcustomeraccounts = this.lambdaQuery()
                .eq(Twcustomeraccounts::getCustomerid, customerid).one();

        if (twcustomeraccounts == null) {
            Twcustomer twcustomer = twcustomerMapper.selectById(customerid);
            twcustomeraccounts = new Twcustomeraccounts();
            twcustomeraccounts.setCustomerid(customerid);
            twcustomeraccounts.setCustomername(twcustomer.getSname());
            twcustomeraccounts.setIcusttype(twcustomer.getItype());
            twcustomeraccounts.setNamountbalance(namountreceived);
            twcustomeraccounts.setDcreatetime(new Date());
            twcustomeraccounts.setLastoperatorid(loginUser.getUserid());
            twcustomeraccounts.setLastoperator(loginUser.getUsername());
            logInterceptor.recoredLog(logSlaveId);
            this.save(twcustomeraccounts);
            return;
        }

        //更新客户账户余额、操作员
        twcustomeraccounts.setNamountbalance(twcustomeraccounts.getNamountbalance().add(namountreceived));
        twcustomeraccounts.setLastoperatorid(loginUser.getUserid());
        twcustomeraccounts.setLastoperator(loginUser.getUsername());
        logInterceptor.recoredLog(logSlaveId);
        this.updateById(twcustomeraccounts);

    }

    @Override
    public void updateCustomeraccountsCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception {
        try {
            String[] splitids = customerIds.split(",");
            List<Twcustomeraccounts> resourceas = this.lambdaQuery()
                    .in(Twcustomeraccounts::getCustomerid, splitids)
                    .list();
            if (resourceas.size() > 0) {
                resourceas.forEach(item -> {
                    item.setCustomerid(newcustomerId);
                    item.setCustomername(newcustomername);
                });

                logInterceptor.recoredLog();
                this.updateBatchById(resourceas);
            }
        } catch (Exception e0) {
            throw new RuntimeException("客户账户合并失败！" + e0.getMessage());
        }
    }

    @Override
    public List<Twcustomeraccounts> getCustomerAccountsByCustomerid(String customerId, String startTime, String endTime) throws Exception {
        Date starttime = null;
        Date endtime = null;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.hasText(startTime)) {
            starttime = fmt.parse(startTime);
        }
        if (StringUtils.hasText(endTime)) {
            Date date = fmt.parse(endTime);
            endtime = DateUtil.offsetDay(date, 1);
        }
        List<Twcustomeraccounts> twcustomeraccountsList = this.lambdaQuery()
                .eq(Twcustomeraccounts::getCustomerid, customerId)
                .ge(ObjectUtil.isNotNull(starttime), Twcustomeraccounts::getDcreatetime, starttime)
                .lt(ObjectUtil.isNotNull(endtime), Twcustomeraccounts::getDcreatetime, endtime)
                .orderByDesc(Twcustomeraccounts::getDcreatetime)
                .list();
        return twcustomeraccountsList;
    }
}
