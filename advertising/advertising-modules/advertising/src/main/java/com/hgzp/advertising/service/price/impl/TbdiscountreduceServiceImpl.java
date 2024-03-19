package com.hgzp.advertising.service.price.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.price.vo.DiscountReduceVO;
import com.hgzp.advertising.service.price.TbdiscountreduceServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbcommissionrategroup;
import com.hgzp.core.model.Tbdiscountreduce;
import com.hgzp.mapper.price.TbcommissionrategroupMapper;
import com.hgzp.mapper.price.TbdiscountreduceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 折扣下点 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-23
 */
@Service
public class TbdiscountreduceServiceImpl extends ServiceImpl<TbdiscountreduceMapper, Tbdiscountreduce> implements TbdiscountreduceServiceI {
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    TbdiscountreduceMapper tbdiscountreduceMapper;
    @Autowired
    TbcommissionrategroupMapper tbcommissionrategroupMapper;

    @Override
    public Page<Tbdiscountreduce> getDiscountReducePageList(Page<Tbdiscountreduce> page, DiscountReduceVO query) throws Exception {
        LambdaQueryWrapper<Tbdiscountreduce> lqw = Wrappers.lambdaQuery();
        lqw.eq(query.getCommissionrategroupid() != null, Tbdiscountreduce::getCommissionrategroupid, query.getCommissionrategroupid());
        return tbdiscountreduceMapper.selectPage(page, lqw);
    }

    @Override
    public void saveDiscountReduce(Tbdiscountreduce entity) throws Exception {
        ValidateExist(entity);
        entity.setId(IdUtil.getSnowflakeNextId());

        innerInterceptor.recoredLog();
        tbdiscountreduceMapper.insert(entity);
    }

    @Override
    public void updateDiscountReduce(Tbdiscountreduce entity) throws Exception {
        ValidateExist(entity);
        innerInterceptor.recoredLog();
        tbdiscountreduceMapper.updateById(entity);
    }

    @Override
    public void deleteDiscountReduce(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        innerInterceptor.recoredLog();
        tbdiscountreduceMapper.deleteBatchIds(idList);
    }

    /**
     * 方法功能: 折扣下点存在校验
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/11/30 14:59
     */
    private void ValidateExist(Tbdiscountreduce entity) {
        LambdaQueryWrapper<Tbdiscountreduce> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(entity.getId() != null, Tbdiscountreduce::getId, entity.getId())
                .eq(Tbdiscountreduce::getCommissionrategroupid, entity.getCommissionrategroupid())
                .and(i -> i
                        .between(Tbdiscountreduce::getNdiscountstart, entity.getNdiscountstart(), entity.getNdiscountend())
                        .or()
                        .between(Tbdiscountreduce::getNdiscountend, entity.getNdiscountstart(), entity.getNdiscountend())
                        .or(j -> j
                                .lt(Tbdiscountreduce::getNdiscountstart, entity.getNdiscountstart())
                                .gt(Tbdiscountreduce::getNdiscountend, entity.getNdiscountend()))
                );
        List<Tbdiscountreduce> tbdiscountreduceList = tbdiscountreduceMapper.selectList(queryWrapper);
        if (tbdiscountreduceList.size() > 0) {
            throw new DataExistException("折扣下点范围已存在");
        }
    }

    @Override
    public void SaveCopyDiscountReduce(Long oldpricegroupid, Tbcommissionrategroup tbcommissionrategroup) {
        LambdaQueryWrapper<Tbdiscountreduce> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbdiscountreduce::getCommissionrategroupid, oldpricegroupid);
        List<Tbdiscountreduce> tbdiscountreduces = tbdiscountreduceMapper.selectList(lqw);
        for (Tbdiscountreduce tbdiscountreduce : tbdiscountreduces) {
            tbdiscountreduce.setId(IdUtil.getSnowflakeNextId());
            tbdiscountreduce.setCommissionrategroupid(tbcommissionrategroup.getId());
            tbdiscountreduce.setCommissionrategroupname(tbcommissionrategroup.getCommissionrategroupname());
            ValidateExist(tbdiscountreduce);

            innerInterceptor.recoredLog();
            tbdiscountreduceMapper.insert(tbdiscountreduce);
        }
    }
}
