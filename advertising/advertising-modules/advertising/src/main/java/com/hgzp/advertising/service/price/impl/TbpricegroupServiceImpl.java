package com.hgzp.advertising.service.price.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.price.vo.PriceGroupVO;
import com.hgzp.advertising.service.price.TbpricegroupServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.exception.DataNotSupportException;
import com.hgzp.core.model.Tbpricegroup;
import com.hgzp.core.page.Menu;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.mapper.price.TbpricegroupMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 价格表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbpricegroupServiceImpl extends MyServiceImpl<TbpricegroupMapper, Tbpricegroup> implements TbpricegroupServiceI {
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    TbpricegroupMapper tbpricegroupMapper;

    @Override
    public Page<Tbpricegroup> getPriceGroupPageList(Page<Tbpricegroup> page, PriceGroupVO query) throws Exception {
        LambdaQueryWrapper<Tbpricegroup> lqw = Wrappers.lambdaQuery();
        lqw.and(StrUtil.isNotBlank(query.getQueryKey()), i -> i.like(Tbpricegroup::getSname, query.getQueryKey())
                .or()
                .like(Tbpricegroup::getSremark, query.getQueryKey()));
        lqw.eq(query.getMediatypekey() != null, Tbpricegroup::getMediatypekey, query.getMediatypekey());
        lqw.eq(query.getSyear() != null, Tbpricegroup::getSyear, query.getSyear());
        return tbpricegroupMapper.selectPage(page, lqw);
    }

    @Override
    public List<TreeModel> getPriceGroupTree(TreeQuery query) throws Exception {
        List<TreeModel> result = new ArrayList<>();
        List<Tbpricegroup> tbpricegroupList = this.lambdaQuery()
                .eq(Tbpricegroup::getBuse, true)
                .orderByAsc(Tbpricegroup::getIsort)
                .list();
        List<TreeModel> treeModelList = priceGroupConvertTreeModel(tbpricegroupList, TreeModel.UNIT);
        result.addAll(treeModelList);
        return null;
    }

    @Override
    public void savePriceGroup(Tbpricegroup tbpricegroup) throws Exception {
        if (isExistPriceGroupName(tbpricegroup)) {
            throw new DataExistException("已存在同名价格组");
        }
        tbpricegroup.setId(null);
        ValidateDefault(tbpricegroup);
        tbpricegroup.setId(IdUtil.getSnowflakeNextId());

        innerInterceptor.recoredLog();
        tbpricegroupMapper.insert(tbpricegroup);
    }

    @Override
    public void updatePriceGroup(Tbpricegroup tbpricegroup) throws Exception {
        if (isExistPriceGroupName(tbpricegroup)) {
            throw new DataExistException("已存在同名价格组");
        }
        ValidateDefault(tbpricegroup);
        innerInterceptor.recoredLog();
        tbpricegroupMapper.updateById(tbpricegroup);
    }

    /**
     * 方法功能: 验证是否有默认价格组
     *
     * @param tbpricegroup
     * @return void
     * @author suny
     * @date 2023/11/17 15:06
     */
    private void ValidateDefault(Tbpricegroup tbpricegroup) throws Exception {
        Tbpricegroup old = this.lambdaQuery()
                .ne(tbpricegroup.getId() != null, Tbpricegroup::getId, tbpricegroup.getId())
                .eq(Tbpricegroup::getMediatypekey, tbpricegroup.getMediatypekey())
                .eq(Tbpricegroup::getBdefault, true)
                .one();
        if (old == null) {
            if (tbpricegroup.getBdefault() == null || !tbpricegroup.getBdefault()) {
                throw new DataNotSupportException("必须有默认价格组");
            }
        } else {
            if (tbpricegroup.getBdefault() != null && tbpricegroup.getBdefault()) {
                old.setBdefault(false);
                innerInterceptor.recoredLog();
                tbpricegroupMapper.updateById(old);
            }
        }
    }

    @Override
    public void deletePriceGroup(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        innerInterceptor.recoredLog();
        tbpricegroupMapper.deleteBatchIds(idList);
    }

    /**
     * 方法功能:  将价格分组转换成树形结构的数据模型
     *
     * @param tbpricegroups
     * @param iconSkin
     * @return java.util.List<com.hgzp.core.page.TreeModel>
     * @author suny
     * @date 2023/11/10 13:34
     */
    private List<TreeModel> priceGroupConvertTreeModel(List<Tbpricegroup> tbpricegroups, String iconSkin) {
        List<TreeModel> treeModelList = new ArrayList<>();
        for (Tbpricegroup tbpricegroup : tbpricegroups) {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(tbpricegroup.getId());
            treeModel.setIconSkin(iconSkin);
            treeModel.setParentId(0L);
            treeModel.setName(tbpricegroup.getSname());
            treeModel.setNocheck(true);
            treeModelList.add(treeModel);
        }
        //添加一个根节点
        TreeModel root = new TreeModel();
        root.setId(0L);
        root.setName("所有分组");
        root.setChecked(false);
        root.setNocheck(false);
        root.setIconSkin(TreeModel.UNIT);
        treeModelList.add(root);
        return treeModelList;
    }

    /**
     * 方法功能: 验证是否存在同名的价格组
     *
     * @param tbpricegroup
     * @return boolean
     * @author suny
     * @date 2023/11/24 15:08
     */
    public boolean isExistPriceGroupName(Tbpricegroup tbpricegroup) {
        Long count = new LambdaQueryChainWrapper<>(tbpricegroupMapper)
                .eq(Tbpricegroup::getSname, tbpricegroup.getSname())
                .ne(tbpricegroup.getId() != null, Tbpricegroup::getId, tbpricegroup.getId())
                .count();
        return count > 0;
    }

    @Override
    public List<Long> getQueryGroupId(String mediatypekey) {
        List<Long> idList = this.lambdaQuery()
                .eq(mediatypekey != null, Tbpricegroup::getMediatypekey, mediatypekey)
                .eq(Tbpricegroup::getBuse, true)
                .eq(Tbpricegroup::getBdefault, true)
                .list()
                .stream()
                .map(Tbpricegroup::getId)
                .collect(Collectors.toList());
        return idList;
    }

    @Override
    public List<Tbpricegroup> getPriceGroupList(PriceGroupVO query) throws Exception {
        LambdaQueryWrapper<Tbpricegroup> lqw = Wrappers.lambdaQuery();
        lqw.eq(query.getMediatypekey() != null, Tbpricegroup::getMediatypekey, query.getMediatypekey());
        lqw.eq(query.getBuse() != null, Tbpricegroup::getBuse, query.getBuse());
        lqw.orderByAsc(Tbpricegroup::getIsort);
        List<Tbpricegroup> tbpricegroups = tbpricegroupMapper.selectList(lqw);

        List<Tbpricegroup> lspricegroups = new ArrayList<>();
        //先过滤年份价格组
        if (ObjectUtil.isNotNull(query.getSyear())) {
            lspricegroups = tbpricegroups.stream()
                    .filter(i -> i.getSyear().equals(query.getSyear()))
                    .collect(Collectors.toList());
            //如果没有找到就用默认的价格组
            if (lspricegroups.size() == 0) {
                lspricegroups = tbpricegroups.stream()
                        .filter(i -> i.getBdefault().equals(true))
                        .collect(Collectors.toList());
            }
        } else {
            lspricegroups = tbpricegroups;
        }
        return lspricegroups;
    }
}
