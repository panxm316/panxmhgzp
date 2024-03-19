package com.hgzp.advertising.service.price.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.service.price.TbadcolorServiceI;
import com.hgzp.core.model.Tbadcolor;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.ad.TbadcolorMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 广告色彩 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadcolorServiceImpl extends MyServiceImpl<TbadcolorMapper, Tbadcolor> implements TbadcolorServiceI {
    @Autowired
    TbadcolorMapper adcolorMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public void deleteAdcolorById(String ids) {
        innerInterceptor.recoredLog();
        adcolorMapper.deleteBatchIds(Arrays.asList(ids.split(",")));
    }

    @Override
    public IPage<Tbadcolor> getAdcolorList(Page<Tbadcolor> page, BaseQueryInfo baseQueryInfo, String mediatypekey) {
        IPage<Tbadcolor> adcolorList = new LambdaQueryChainWrapper<>(adcolorMapper)
                .eq(StringUtils.hasText(mediatypekey), Tbadcolor::getMediatypekey, mediatypekey)
                .like(StringUtils.hasText(baseQueryInfo.getQueryKey()), Tbadcolor::getSname, baseQueryInfo.getQueryKey()).page(page);
        return adcolorList;
    }

    public List<TreeModel> getAdColorTreeList(String mediaType) {
        List<TreeModel> treeList = new ArrayList<>();
        if (StringUtils.hasText(mediaType)) {
            treeList = this.lambdaQuery()
                    .eq(Tbadcolor::getBuse, true)
                    .eq(Tbadcolor::getMediatypekey, mediaType)
                    .orderByAsc(Tbadcolor::getIsort)
                    .list()
                    .stream()
                    .map(adcolor -> {
                        TreeModel treeModel = new TreeModel();
                        treeModel.setId(adcolor.getId());
                        treeModel.setName(adcolor.getSname());
                        return treeModel;
                    }).collect(Collectors.toList());
        }

        return treeList;
    }

    @Override
    public List<Tbadcolor> getAdColorListValid(String mediatypekey) {
        List<Tbadcolor> adcolorList = new LambdaQueryChainWrapper<>(adcolorMapper)
                .eq(StringUtils.hasText(mediatypekey), Tbadcolor::getMediatypekey, mediatypekey)
                .eq(Tbadcolor::getBuse, true)
                .orderByAsc(Tbadcolor::getIsort)
                .list();
        return adcolorList;
    }
}
