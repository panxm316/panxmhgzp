package com.hgzp.advertisingsys.service.finance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.service.finance.TbpaymethodServiceI;
import com.hgzp.core.model.Tbadprintitem;
import com.hgzp.core.model.Tbadtype;
import com.hgzp.core.model.Tbpaymethod;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.finance.TbpaymethodMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

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
public class TbpaymethodServiceImpl extends MyServiceImpl<TbpaymethodMapper, Tbpaymethod> implements TbpaymethodServiceI {
    @Autowired
    TbpaymethodMapper paymethodMapper;

    @Override
    public IPage<Tbpaymethod> getPaymethodPageList(Page<Tbpaymethod> page, String querykey) {
        LambdaQueryWrapper<Tbpaymethod> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(querykey), Tbpaymethod::getSname, querykey);
        IPage<Tbpaymethod> paymethodPage = paymethodMapper.selectPage(page, lqw);
        return paymethodPage;

    }

    @Override
    public void deletePaymethod(String ids) {
        String[] split = ids.split(",");
        paymethodMapper.deleteBatchIds(Arrays.asList(split));
    }

    @Override
    public Json doDefaultLogic(Tbpaymethod tbpaymethod) {
        if (tbpaymethod.getBdefault()) {
            List<Tbpaymethod> list = paymethodMapper.selectList(null);
            for (Tbpaymethod t : list) {
                t.setBdefault(false);
                paymethodMapper.updateById(t);
            }
        } else {
            Long count = new LambdaQueryChainWrapper<>(paymethodMapper)
                    .ne(tbpaymethod.getId() != null, Tbpaymethod::getId, tbpaymethod.getId())
                    .eq(Tbpaymethod::getBdefault, true)
                    .count();
            if (count == 0) {
                return Json.fail("至少要有一条记录默认选中！");
            }
            if (count>1) {
                return Json.fail("只能有一条记录默认选中！");
            }
        }
        return Json.success();
    }

    @Override
    public boolean isDuplicateSname(Tbpaymethod tbpaymethod) {
        Long count = new LambdaQueryChainWrapper<>(paymethodMapper)
                .ne(tbpaymethod.getId() != null, Tbpaymethod::getId, tbpaymethod.getId())
                .eq(Tbpaymethod::getSname, tbpaymethod.getSname())
                .count();
        return count>0;
    }
}
