package com.hgzp.advertising.service.customer.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.service.customer.TbcustomercreditServiceI;
import com.hgzp.core.model.Tbcustomercredit;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.customer.TbcustomercreditMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户信誉度表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbcustomercreditServiceImpl extends MyServiceImpl<TbcustomercreditMapper, Tbcustomercredit> implements TbcustomercreditServiceI {
    @Autowired
    private TbcustomercreditMapper customercreditMapper;

    @Override
    public List<Tbcustomercredit> getCustomerCreditList() {
        List<Tbcustomercredit> customerCreditList = new LambdaQueryChainWrapper<>(customercreditMapper)
                .orderByAsc(Tbcustomercredit::getIsort)
                .list();
        return customerCreditList;
    }

    @Override
    public List<DataCombo> getCustomerCreditCombo() {
        List<Tbcustomercredit> pagecolorList = new LambdaQueryChainWrapper<>(customercreditMapper)
                .eq(Tbcustomercredit::getBuse, true)
                .orderByAsc(Tbcustomercredit::getIsort)
                .list();
        List<DataCombo> comboList = pagecolorList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getCreditname()))
                .collect(Collectors.toList());
        return comboList;
    }

    @Override
    public boolean isDuplicateSname(Tbcustomercredit tbcustomercredit) {
        Long count = new LambdaQueryChainWrapper<>(customercreditMapper)
                .eq(Tbcustomercredit::getBuse, true)
                .eq(Tbcustomercredit::getCreditname, tbcustomercredit.getCreditname())
                .ne(tbcustomercredit.getId() != null, Tbcustomercredit::getId, tbcustomercredit.getId())
                .count();
        return count > 0;
    }

}
