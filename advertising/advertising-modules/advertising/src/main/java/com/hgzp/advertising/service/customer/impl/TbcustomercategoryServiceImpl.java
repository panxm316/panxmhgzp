package com.hgzp.advertising.service.customer.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.service.customer.TbcustomercategoryServiceI;
import com.hgzp.core.model.Tbcustomercategory;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.customer.TbcustomercategoryMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户分类表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbcustomercategoryServiceImpl extends MyServiceImpl<TbcustomercategoryMapper, Tbcustomercategory> implements TbcustomercategoryServiceI {
    @Autowired
    private TbcustomercategoryMapper customercategoryMapper;

    @Override
    public List<Tbcustomercategory> getCustomerCategoryList() {
        List<Tbcustomercategory> customerCategoryList = new LambdaQueryChainWrapper<>(customercategoryMapper)
                .orderByAsc(Tbcustomercategory::getIsort)
                .list();
        return customerCategoryList;
    }

    @Override
    public List<DataCombo> getCustomerCategoryCombo() {
        List<Tbcustomercategory> pagecolorList = new LambdaQueryChainWrapper<>(customercategoryMapper)
                .eq(Tbcustomercategory::getBuse, true)
                .orderByAsc(Tbcustomercategory::getIsort)
                .list();
        List<DataCombo> comboList = pagecolorList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getCategoryname()))
                .collect(Collectors.toList());
        return comboList;
    }

    @Override
    public boolean isDuplicateSname(Tbcustomercategory tbcustomercategory) {
        Long count = new LambdaQueryChainWrapper<>(customercategoryMapper)
                .eq(Tbcustomercategory::getBuse, true)
                .eq(Tbcustomercategory::getCategoryname, tbcustomercategory.getCategoryname())
                .ne(tbcustomercategory.getId() != null, Tbcustomercategory::getId, tbcustomercategory.getId())
                .count();
        return count > 0;
    }
}
