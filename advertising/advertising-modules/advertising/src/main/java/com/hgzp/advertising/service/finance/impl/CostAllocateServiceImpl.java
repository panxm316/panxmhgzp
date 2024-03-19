package com.hgzp.advertising.service.finance.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.finance.CostAllocateServiceI;
import com.hgzp.core.model.Twcostallocate;
import com.hgzp.mapper.finance.TwcostallocateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 成本分配明细表 服务实现类
 * </p>
 *
 * @author muyn
 * @since 2024-03-18
 */
@Service
public class CostAllocateServiceImpl extends ServiceImpl<TwcostallocateMapper, Twcostallocate> implements CostAllocateServiceI {

    @Autowired
    private HgDataChangeRecorderInnerInterceptor innerInterceptor;
 }
