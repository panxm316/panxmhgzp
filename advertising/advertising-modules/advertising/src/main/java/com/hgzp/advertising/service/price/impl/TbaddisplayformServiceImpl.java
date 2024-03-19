package com.hgzp.advertising.service.price.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.price.vo.AdDisplayFormVO;
import com.hgzp.advertising.service.price.TbaddisplayformServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbaddisplayform;
import com.hgzp.core.page.TreeModel;
import com.hgzp.mapper.price.TbaddisplayformMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 广告形式 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbaddisplayformServiceImpl extends MyServiceImpl<TbaddisplayformMapper, Tbaddisplayform> implements TbaddisplayformServiceI {
    @Autowired
    TbaddisplayformMapper tbaddisplayformMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public Page<Tbaddisplayform> getAdDisplayFormPageList(Page<Tbaddisplayform> page, AdDisplayFormVO query) throws Exception {
        LambdaQueryWrapper<Tbaddisplayform> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(query.getQueryKey()), Tbaddisplayform::getSname, query.getQueryKey());
        lqw.eq(StringUtils.hasText(query.getMediatypekey()), Tbaddisplayform::getMediatypekey, query.getMediatypekey());
        return tbaddisplayformMapper.selectPage(page, lqw);
    }

    @Override
    public void saveAdDisplayForm(Tbaddisplayform tbaddisplayform) {
        if (isExistAdDisplayForm(tbaddisplayform)) {
            throw new DataExistException(tbaddisplayform.getMediatypename() + " 下已存在同名广告形式");
        }
        tbaddisplayform.setId(IdUtil.getSnowflakeNextId());

        innerInterceptor.recoredLog();
        tbaddisplayformMapper.insert(tbaddisplayform);
    }

    @Override
    public void updateAdDisplayForm(Tbaddisplayform tbaddisplayform) {
        if (isExistAdDisplayForm(tbaddisplayform)) {
            throw new DataExistException(tbaddisplayform.getMediatypename() + " 下已存在同名广告形式");
        }
        innerInterceptor.recoredLog();
        tbaddisplayformMapper.updateById(tbaddisplayform);
    }

    public boolean isExistAdDisplayForm(Tbaddisplayform tbaddisplayform) {
        Long count = new LambdaQueryChainWrapper<>(tbaddisplayformMapper)
                .eq(tbaddisplayform.getMediatypekey() != null, Tbaddisplayform::getMediatypekey, tbaddisplayform.getMediatypekey())
                .eq(Tbaddisplayform::getSname, tbaddisplayform.getSname())
                .ne(tbaddisplayform.getId() != null, Tbaddisplayform::getId, tbaddisplayform.getId())
                .count();
        return count > 0;
    }

    @Override
    public List<TreeModel> getAdDisplayFormTreeList(String mediaType) {
        List<Tbaddisplayform> addisplayList = this.lambdaQuery()
                .eq(Tbaddisplayform::getBuse, true)
                .eq(Tbaddisplayform::getMediatypekey, mediaType)
                .orderByAsc(Tbaddisplayform::getIsort)
                .list();
        List<TreeModel> treeModels = new ArrayList<>();
        if (addisplayList.size() < 1) {
            return treeModels;
        }
        addisplayList.forEach(item -> {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(item.getId());
            treeModel.setName(item.getSname());
            treeModels.add(treeModel);
        });
        return treeModels;
    }

    @Override
    public List<Tbaddisplayform> listUsableAdDisplayForm(String mediaType) {
        return list(Wrappers.<Tbaddisplayform>lambdaQuery()
                .eq(Tbaddisplayform::getBuse, true)
                .eq(StrUtil.isNotBlank(mediaType), Tbaddisplayform::getMediatypekey, mediaType)
                .orderByAsc(Tbaddisplayform::getIsort));
    }
}
