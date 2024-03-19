package com.hgzp.advertising.service.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hgzp.advertising.service.ad.TworderitemServiceI;
import com.hgzp.advertising.service.api.TworderitemfilesServiceI;
import com.hgzp.core.model.Tworderitem;
import com.hgzp.core.model.Tworderitemfiles;
import com.hgzp.mapper.ad.TworderitemfilesMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单明细文件表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TworderitemfilesServiceImpl extends MyServiceImpl<TworderitemfilesMapper, Tworderitemfiles> implements TworderitemfilesServiceI {

    @Autowired
    TworderitemServiceI tworderitemServiceI;

    @Override
    public List<Tworderitemfiles> getFilesByOrderItemIdForCJ(String strdate, String scontractnum) {
        List<Tworderitem> tworderitems = tworderitemServiceI.list(Wrappers.<Tworderitem>lambdaQuery().eq(Tworderitem::getScontractnum, scontractnum)
                .eq(Tworderitem::getPrestartdate, strdate)
                .eq(Tworderitem::getBuse, 1));
        if (tworderitems == null || tworderitems.size() == 0) {
            return new ArrayList<>();
        }
        Tworderitem tworderitem = tworderitems.get(0);
        if (tworderitem == null) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Tworderitemfiles> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tworderitemfiles::getOrderitemid, tworderitem.getId());
        return list(lqw);
    }
}

