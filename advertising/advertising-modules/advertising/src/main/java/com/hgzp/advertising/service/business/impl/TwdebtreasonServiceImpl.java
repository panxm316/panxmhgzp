package com.hgzp.advertising.service.business.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.business.dto.DebtReasonDTO;
import com.hgzp.advertising.pagemodel.business.vo.DebtReasonVO;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.business.TwdebtreasonServiceI;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.Twdebtreason;
import com.hgzp.mapper.business.TwdebtreasonMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 欠款原因表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-17
 */
@Service
public class TwdebtreasonServiceImpl extends ServiceImpl<TwdebtreasonMapper, Twdebtreason> implements TwdebtreasonServiceI {
    @Autowired
    TwdebtreasonMapper twdebtreasonmapper;
    @Autowired
    TworderitemServiceI tworderitemServiceI;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<Twdebtreason> getDebtReasonPageList(Page<Twdebtreason> page, DebtReasonVO query) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        LambdaQueryWrapper<Twdebtreason> lqw = Wrappers.lambdaQuery();
        lqw.and(StrUtil.isNotBlank(query.getQueryKey()), i -> i.like(Twdebtreason::getAgencyname, query.getQueryKey())
                .or()
                .like(Twdebtreason::getCustomername, query.getQueryKey()).or()
                .like(Twdebtreason::getAgentname, query.getQueryKey()));
        lqw.and(i-> i.eq(Twdebtreason::getEmployid, user.getUserid())
                .or()
                .eq(Twdebtreason::getAgencyemployid, user.getUserid())
                .or()
                .eq(Twdebtreason::getAgentemployid, user.getUserid()));
        lqw.eq(query.getScontractnum() != null, Twdebtreason::getScontractnum, query.getScontractnum());
        lqw.eq(query.getInvoice() != null, Twdebtreason::getInvoice, query.getInvoice());
        lqw.ge(query.getStartTime() != null, Twdebtreason::getDpublishdate, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twdebtreason::getDpublishdate, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        return twdebtreasonmapper.selectPage(page, lqw);
    }

    @Override
    public void updateDebtReason(DebtReasonDTO entity) throws Exception {
        String ids = entity.getOrderitemid().toString();
        if(entity.getId() == null){
            tworderitemServiceI.pushOrderDebt(ids);
            LambdaQueryWrapper<Twdebtreason> lqw = Wrappers.lambdaQuery();
            lqw.eq(Twdebtreason::getOrderitemid, entity.getOrderitemid());
            Twdebtreason twdebtreason = twdebtreasonmapper.selectOne(lqw);
            if(twdebtreason == null){
                throw new DataNotExistException("欠款原因表存储失败");
            }
            entity.setId(twdebtreason.getId());
        }

        Twdebtreason twdebtreason = twdebtreasonmapper.selectById(entity.getId());
        BeanUtils.copyProperties(entity, twdebtreason);
        innerInterceptor.recoredLog();
        twdebtreasonmapper.updateById(twdebtreason);
    }
}
