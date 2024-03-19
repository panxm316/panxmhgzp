package com.hgzp.advertising.service.finance.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.service.finance.TbpaymethodServiceI;
import com.hgzp.core.model.Tbpaymethod;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.finance.TbpaymethodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 付款方式 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbpaymethodServiceImpl extends ServiceImpl<TbpaymethodMapper, Tbpaymethod> implements TbpaymethodServiceI {
    @Autowired
    TbpaymethodMapper paymethodMapper;


    @Override
    public List<DataCombo> getPaymethodCombo() {
        List<Tbpaymethod> list = this.lambdaQuery()
                .eq(Tbpaymethod::getBuse, true)
                .orderByAsc(Tbpaymethod::getIsort)
                .list();
        List<DataCombo> dataComboList = list.stream()
                .map(tbpaymethod -> new DataCombo(tbpaymethod.getId().toString(), tbpaymethod.getSname(), tbpaymethod.getBdefault()))
                .collect(Collectors.toList());
        return dataComboList;
    }



}
