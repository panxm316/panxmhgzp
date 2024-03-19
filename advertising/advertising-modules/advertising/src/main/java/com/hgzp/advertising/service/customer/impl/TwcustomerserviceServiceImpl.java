package com.hgzp.advertising.service.customer.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.customer.dto.CustomerServiceDTO;
import com.hgzp.advertising.pagemodel.customer.vo.CustomerServiceVO;
import com.hgzp.advertising.service.customer.TwcustomerserviceServiceI;
import com.hgzp.core.model.Twcustomerservice;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.customer.TwcustomerserviceMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 客户服务表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwcustomerserviceServiceImpl extends ServiceImpl<TwcustomerserviceMapper, Twcustomerservice> implements TwcustomerserviceServiceI {
    @Autowired
    TwcustomerserviceMapper twcustomerserviceMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<Twcustomerservice> getCustomerServicePageList(Page<Twcustomerservice> page, CustomerServiceVO query) throws Exception {
        LambdaQueryWrapper<Twcustomerservice> lqw = Wrappers.lambdaQuery();
        lqw.and(StrUtil.isNotBlank(query.getQueryKey()), i -> i.like(Twcustomerservice::getStitle, query.getQueryKey())
                .or()
                .like(Twcustomerservice::getCustomername, query.getQueryKey()));
        lqw.eq(query.getIservicetype() != null, Twcustomerservice::getIservicetype, query.getIservicetype());
        lqw.ge(query.getStartTime() != null, Twcustomerservice::getDcreatetime, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twcustomerservice::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        lqw.eq(Twcustomerservice::getBdelete, false)
                .eq(ObjectUtil.isNotNull(query.getCustomerid()) ,Twcustomerservice::getCustomerid, query.getCustomerid());
        return twcustomerserviceMapper.selectPage(page, lqw);
    }

    @Override
    public void saveCustomerService(CustomerServiceDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Twcustomerservice twcustomerservice = new Twcustomerservice();
        BeanUtils.copyProperties(entity, twcustomerservice);
        twcustomerservice.setId(IdUtil.getSnowflakeNextId());
        twcustomerservice.setDcreatetime(new Date());
        twcustomerservice.setBdelete(false);
        twcustomerservice.setLastoperatorid(user.getUserid());
        twcustomerservice.setLastoperator(user.getUsername());
        twcustomerservice.setOperatorid(user.getUserid());
        twcustomerservice.setOperatorname(user.getUsername());
        twcustomerservice.setDlasttime(new Date());

        innerInterceptor.recoredLog();
        twcustomerserviceMapper.insert(twcustomerservice);
    }

    @Override
    public void updateCustomerService(CustomerServiceDTO entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        Twcustomerservice twcustomerservice = new Twcustomerservice();
        BeanUtils.copyProperties(entity, twcustomerservice);
        twcustomerservice.setLastoperatorid(user.getUserid());
        twcustomerservice.setLastoperator(user.getUsername());
        twcustomerservice.setDlasttime(new Date());
        innerInterceptor.recoredLog();
        twcustomerserviceMapper.updateById(twcustomerservice);
    }

    @Override
    public void deleteCustomerService(String ids) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));
        List<Twcustomerservice> twcustomerserviceList = twcustomerserviceMapper.selectBatchIds(idList);
        for (Twcustomerservice twcustomerservice : twcustomerserviceList) {
            twcustomerservice.setBdelete(true);
            innerInterceptor.recoredLog();
            twcustomerserviceMapper.updateById(twcustomerservice);
        }
    }

    @Override
    public void updateCustomerServiceCustomer(String customerIds, Long newcustomerId, String newcustomername) throws Exception {
        try {
            String[] splitids = customerIds.split(",");
            List<Twcustomerservice> resourceas = this.lambdaQuery()
                    .in(Twcustomerservice::getCustomerid, splitids)
                    .list();
            if (resourceas.size()>0) {
                resourceas.forEach(item -> {
                    item.setCustomerid(newcustomerId);
                    item.setCustomername(newcustomername);
                });

                innerInterceptor.recoredLog();
                this.updateBatchById(resourceas);
            }
        } catch (Exception e0) {
            throw new RuntimeException("客户服务合并失败！" + e0.getMessage());
        }
    }
}
