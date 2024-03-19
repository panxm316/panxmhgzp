package com.hgzp.advertising.service.finance.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.pagemodel.finance.vo.BankAccountHistoryVO;
import com.hgzp.advertising.service.finance.TwbankaccounthistoryServiceI;
import com.hgzp.core.model.Twbankaccounthistory;
import com.hgzp.mapper.finance.TwbankaccounthistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 银行流水导入历史表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TwbankaccounthistoryServiceImpl extends ServiceImpl<TwbankaccounthistoryMapper, Twbankaccounthistory> implements TwbankaccounthistoryServiceI {
    @Autowired
    TwbankaccounthistoryMapper twbankaccounthistoryMapper;

    @Override
    public IPage<Twbankaccounthistory> getBankAccountHistoryPageList(Page<Twbankaccounthistory> page, BankAccountHistoryVO query) throws Exception {
        LambdaQueryWrapper<Twbankaccounthistory> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(query.getSoriginalfile()),Twbankaccounthistory::getSoriginalfile, query.getSoriginalfile());
        lqw.ge(query.getStartTime() != null, Twbankaccounthistory::getDcreatetime, query.getStartTime());
        if (query.getEndTime() != null) {
            lqw.lt(Twbankaccounthistory::getDcreatetime, DateUtil.offsetDay(query.getEndTime(), 1));
        }
        lqw.orderByDesc(Twbankaccounthistory::getDcreatetime);
        return twbankaccounthistoryMapper.selectPage(page, lqw);
    }
}
