package com.hgzp.advertising.service.customer.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.service.customer.TbcustomerstatusServiceI;
import com.hgzp.core.model.Tbcustomerstatus;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.customer.TbcustomerstatusMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户状态表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbcustomerstatusServiceImpl extends MyServiceImpl<TbcustomerstatusMapper, Tbcustomerstatus> implements TbcustomerstatusServiceI {

    @Autowired
    private TbcustomerstatusMapper customerstatusMapper;

    @Override
    public List<Tbcustomerstatus> getCustomerStatusList() {
        List<Tbcustomerstatus> customerStatusList = new LambdaQueryChainWrapper<>(customerstatusMapper)
                .orderByAsc(Tbcustomerstatus::getIsort)
                .list();
        return customerStatusList;
    }

    @Override
    public List<DataCombo> getCustomerStatusCombo() {
        List<Tbcustomerstatus> pagecolorList = new LambdaQueryChainWrapper<>(customerstatusMapper)
                .eq(Tbcustomerstatus::getBuse, true)
                .orderByAsc(Tbcustomerstatus::getIsort)
                .list();
        List<DataCombo> comboList = pagecolorList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getStatusname()))
                .collect(Collectors.toList());
        return comboList;
    }

    @Override
    public boolean isDuplicateSname(Tbcustomerstatus tbcustomerstatus) {
        Long count = new LambdaQueryChainWrapper<>(customerstatusMapper)
                .eq(Tbcustomerstatus::getBuse, true)
                .eq(Tbcustomerstatus::getStatusname, tbcustomerstatus.getStatusname())
                .ne(tbcustomerstatus.getId() != null, Tbcustomerstatus::getId, tbcustomerstatus.getId())
                .count();
        return count > 0;
    }
}
