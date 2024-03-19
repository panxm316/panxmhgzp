package com.hgzp.advertising.service.ad.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.service.ad.TbadtypeServiceI;
import com.hgzp.core.model.Tbadtype;
import com.hgzp.mapper.ad.TbadtypeMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TbadtypeServiceImpl
 * 创建人：songly
 * 类描述：TODO
 * 创建日期：2023/12/5 15:58
 */
@Service
public class TbadtypeServiceImpl extends MyServiceImpl<TbadtypeMapper, Tbadtype> implements TbadtypeServiceI {
    @Autowired
    private TbadtypeMapper tbadtypeMapper;

    @Override
    public List<Tbadtype> getAdTypeList() {
        List<Tbadtype> lsAdType = new LambdaQueryChainWrapper<>(tbadtypeMapper)
                .eq(Tbadtype::getBuse, true)
                .orderByAsc(Tbadtype::getIsort)
                .list();
        return lsAdType;
    }
}