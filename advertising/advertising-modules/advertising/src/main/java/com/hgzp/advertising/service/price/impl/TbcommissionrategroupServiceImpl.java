package com.hgzp.advertising.service.price.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.price.TbcommissionrategroupServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.exception.DataNotExistException;
import com.hgzp.core.model.Tbcommissionrategroup;
import com.hgzp.core.model.Tbcommissionrateitem;
import com.hgzp.core.model.Tbdiscountreduce;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.price.TbcommissionrategroupMapper;
import com.hgzp.mapper.price.TbcommissionrateitemMapper;
import com.hgzp.mapper.price.TbdiscountreduceMapper;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 计提比例总表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-23
 */
@Service
public class TbcommissionrategroupServiceImpl extends ServiceImpl<TbcommissionrategroupMapper, Tbcommissionrategroup> implements TbcommissionrategroupServiceI {
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    TbcommissionrategroupMapper tbcommissionrategroupMapper;
    @Autowired
    TbcommissionrateitemMapper tbcommissionrateitemMapper;
    @Autowired
    TbdiscountreduceMapper tbdiscountreduceMapper;

    @Override
    public Page<Tbcommissionrategroup> getCommissionRateGroupPageList(Page<Tbcommissionrategroup> page, BaseQueryInfo query) throws Exception {
        LambdaQueryWrapper<Tbcommissionrategroup> lqw = Wrappers.lambdaQuery();
        return tbcommissionrategroupMapper.selectPage(page, lqw);
    }

    @Override
    public void saveCommissionRateGroup(Tbcommissionrategroup entity) throws Exception {
        LoginUser user = WebUtil.getLoginUser();
        entity.setId(null);
        ValidateDefault(entity);
        ValidateExist(entity);
        entity.setId(IdUtil.getSnowflakeNextId());
        entity.setCreatedate(new Date());
        entity.setCreateempid(user.getUserid());
        entity.setCreateempname(user.getUsername());

        innerInterceptor.recoredLog();
        tbcommissionrategroupMapper.insert(entity);
    }

    @Override
    public void updateCommissionRateGroup(Tbcommissionrategroup entity) throws Exception {
        ValidateDefault(entity);
        ValidateExist(entity);
        innerInterceptor.recoredLog();
        tbcommissionrategroupMapper.updateById(entity);
    }

    @Override
    public void deleteCommissionRateGroup(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        LambdaQueryWrapper<Tbcommissionrategroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Tbcommissionrategroup::getId, idList)
                .eq(Tbcommissionrategroup::getBdefault, true);
        List<Tbcommissionrategroup> tbcommissionrategroupList = tbcommissionrategroupMapper.selectList(queryWrapper);
        if (tbcommissionrategroupList.size() > 0) {
            throw new DataExistException("默认计提比例不能删除");
        }
        // 删除计提比例明细表
        LambdaQueryWrapper<Tbcommissionrateitem> lqw = Wrappers.lambdaQuery();
        lqw.in(Tbcommissionrateitem::getCommissionrategroupid, idList);

        List<Tbcommissionrateitem> tbcommissionrateitems = tbcommissionrateitemMapper.selectList(lqw);
        for (Tbcommissionrateitem tbcommissionrateitem : tbcommissionrateitems) {
            innerInterceptor.recoredLog();
            tbcommissionrateitemMapper.deleteById(tbcommissionrateitem);
        }
        // 删除折扣下点表
        LambdaQueryWrapper<Tbdiscountreduce> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.in(Tbdiscountreduce::getCommissionrategroupid, idList);
        List<Tbdiscountreduce> tbdiscountreduces = tbdiscountreduceMapper.selectList(lambdaQuery);
        for (Tbdiscountreduce tbdiscountreduce : tbdiscountreduces) {
            innerInterceptor.recoredLog();
            tbdiscountreduceMapper.deleteById(tbdiscountreduce);
        }
        innerInterceptor.recoredLog();
        tbcommissionrategroupMapper.deleteBatchIds(idList);
    }

    /**
     * 方法功能:  验证是否有默认价格组
     *
     * @param tbpricegroup
     * @return void
     * @author suny
     * @date 2023/12/1 9:38
     */
    private void ValidateDefault(Tbcommissionrategroup tbpricegroup) throws Exception {
        Tbcommissionrategroup old = this.lambdaQuery()
                .ne(tbpricegroup.getId() != null, Tbcommissionrategroup::getId, tbpricegroup.getId())
                .eq(Tbcommissionrategroup::getBuse, true)
                .eq(Tbcommissionrategroup::getBdefault, true)
                .one();
        if (old == null) {
            if (tbpricegroup.getBdefault() == null || !tbpricegroup.getBdefault()) {
                throw new DataNotExistException("必须有默认价格组");
            }
        } else {
            if (tbpricegroup.getBdefault()) {
                old.setBdefault(false);
                innerInterceptor.recoredLog();
                tbcommissionrategroupMapper.updateById(old);
            }
        }
    }

    /**
     * 方法功能: 验证计提比例总表是否重名
     *
     * @param entity
     * @return void
     * @author suny
     * @date 2023/12/1 9:38
     */
    private void ValidateExist(Tbcommissionrategroup entity) {
        LambdaQueryWrapper<Tbcommissionrategroup> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(entity.getId() != null, Tbcommissionrategroup::getId, entity.getId())
                .eq(Tbcommissionrategroup::getCommissionrategroupname, entity.getCommissionrategroupname());
        List<Tbcommissionrategroup> tbcommissionrategroupList = tbcommissionrategroupMapper.selectList(queryWrapper);
        if (tbcommissionrategroupList.size() > 0) {
            throw new DataExistException("该计提比例总表名称已存在");
        }
    }
}
