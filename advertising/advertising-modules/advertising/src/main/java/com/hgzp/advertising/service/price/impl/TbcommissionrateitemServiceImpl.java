package com.hgzp.advertising.service.price.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.price.vo.CommissionRateItemVO;
import com.hgzp.advertising.service.price.TbcommissionrateitemServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbcommissionrategroup;
import com.hgzp.core.model.Tbcommissionrateitem;
import com.hgzp.mapper.price.TbcommissionrategroupMapper;
import com.hgzp.mapper.price.TbcommissionrateitemMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 计提比例明细表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-23
 */
@Service
public class TbcommissionrateitemServiceImpl extends ServiceImpl<TbcommissionrateitemMapper, Tbcommissionrateitem> implements TbcommissionrateitemServiceI {
    @Autowired
    TbcommissionrateitemMapper tbcommissionrateitemMapper;
    @Autowired
    TbcommissionrategroupMapper tbcommissionrategroupMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public Page<Tbcommissionrateitem> getCommissionRateItemPageList(Page<Tbcommissionrateitem> page, CommissionRateItemVO query) throws Exception {
        LambdaQueryWrapper<Tbcommissionrateitem> lqw = Wrappers.lambdaQuery();
        lqw.eq(query.getCommissionrategroupid() != null, Tbcommissionrateitem::getCommissionrategroupid, query.getCommissionrategroupid());
        lqw.eq(query.getMediaid() != null, Tbcommissionrateitem::getMediaid, query.getMediaid());
        lqw.eq(query.getAdindustryid() != null, Tbcommissionrateitem::getAdindustryid, query.getAdindustryid());
        lqw.eq(query.getDeptid() != null, Tbcommissionrateitem::getDeptid, query.getDeptid());
        lqw.eq(query.getEmployid() != null, Tbcommissionrateitem::getEmployid, query.getEmployid());
        return tbcommissionrateitemMapper.selectPage(page, lqw);
    }

    @Override
    public void saveCommissionRateItem(Tbcommissionrateitem entity) throws Exception {
        ValidateExist(entity);
        entity.setId(IdUtil.getSnowflakeNextId());

        innerInterceptor.recoredLog();
        tbcommissionrateitemMapper.insert(entity);
    }

    @Override
    public void updateCommissionRateItem(Tbcommissionrateitem entity) throws Exception {
        ValidateExist(entity);
        innerInterceptor.recoredLog();
        tbcommissionrateitemMapper.updateById(entity);
    }

    @Override
    public void deleteCommissionRateItem(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        innerInterceptor.recoredLog();
        tbcommissionrateitemMapper.deleteBatchIds(idList);
    }

    /**
     * 验证计提比例明细是否存在
     * 方法功能:验证计提比例明细是否存在
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/25 9:52
     */
    private void ValidateExist(Tbcommissionrateitem entity) {
        LambdaQueryWrapper<Tbcommissionrateitem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(entity.getId() != null, Tbcommissionrateitem::getId, entity.getId())
                .eq(Tbcommissionrateitem::getCommissionrategroupid, entity.getCommissionrategroupid())
                .eq(Tbcommissionrateitem::getMediaid, entity.getMediaid())
                .eq(Tbcommissionrateitem::getAdindustryid, entity.getAdindustryid())
                .eq(Tbcommissionrateitem::getDeptid, entity.getDeptid())
                .eq(entity.getEmployid() != null, Tbcommissionrateitem::getEmployid, entity.getEmployid())
                .isNull(entity.getEmployid() == null, Tbcommissionrateitem::getEmployid);
        List<Tbcommissionrateitem> tbcommissionrateitemList = tbcommissionrateitemMapper.selectList(queryWrapper);
        if (tbcommissionrateitemList.size() > 0) {
            throw new DataExistException("该计提比例明细已存在");
        }
    }

    @Override
    public void SaveCopyCommissionRateItem(Long oldpricegroupid, Tbcommissionrategroup tbcommissionrategroup) {
        if (tbcommissionrategroup == null) {
            throw new DataExistException("该计提比例组不存在");
        }
        LambdaQueryWrapper<Tbcommissionrateitem> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbcommissionrateitem::getCommissionrategroupid, oldpricegroupid);

        List<Tbcommissionrateitem> tbcommissionrateitems = tbcommissionrateitemMapper.selectList(lqw);
        for (Tbcommissionrateitem tbcommissionrateitem : tbcommissionrateitems) {
            Tbcommissionrateitem tbcommissionrateitem1 = new Tbcommissionrateitem();
            BeanUtils.copyProperties(tbcommissionrateitem, tbcommissionrateitem1);
            tbcommissionrateitem1.setCommissionrategroupid(tbcommissionrategroup.getId());
            tbcommissionrateitem1.setCommissionrategroupname(tbcommissionrategroup.getCommissionrategroupname());
            tbcommissionrateitem1.setId(IdUtil.getSnowflakeNextId());
            ValidateExist(tbcommissionrateitem1);

            innerInterceptor.recoredLog();
            tbcommissionrateitemMapper.insert(tbcommissionrateitem1);
        }
    }
}
