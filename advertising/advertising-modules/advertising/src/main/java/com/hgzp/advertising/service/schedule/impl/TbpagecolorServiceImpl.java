package com.hgzp.advertising.service.schedule.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.schedule.vo.PageColorVo;
import com.hgzp.advertising.service.schedule.TbpagecolorServiceI;
import com.hgzp.core.model.Tbpagecolor;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.schedule.TbpagecolorMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.BeanUtils;
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
 * 版面色彩结构 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbpagecolorServiceImpl extends MyServiceImpl<TbpagecolorMapper, Tbpagecolor> implements TbpagecolorServiceI {

    @Autowired
    TbpagecolorMapper pageColorMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public void deletePageColorById(String ids) {
        innerInterceptor.recoredLog();
        pageColorMapper.deleteBatchIds(Arrays.asList(ids.split(",")));
    }

    @Override
    public List<DataCombo> getPageColorCombo() {
        List<Tbpagecolor> pagecolorList = new LambdaQueryChainWrapper<>(pageColorMapper)
                .eq(Tbpagecolor::getBuse, true)
                .orderByAsc(Tbpagecolor::getIsort)
                .list();
        List<DataCombo> comboList = pagecolorList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getSname()))
                .collect(Collectors.toList());
        return comboList;
    }

    @Override
    public IPage<PageColorVo> getPageColorList(Page<Tbpagecolor> page, BaseQueryInfo baseQueryInfo) {
        IPage<Tbpagecolor> pageColorList = new LambdaQueryChainWrapper<>(pageColorMapper)
                .like(StringUtils.hasText(baseQueryInfo.getQueryKey()), Tbpagecolor::getSname,
                        baseQueryInfo.getQueryKey()).page(page);

        Page<PageColorVo> reslutPage = new Page<>();
        if (pageColorList.getTotal() == 0) {
            return reslutPage;
        }
        List<PageColorVo> lsData = new ArrayList<>();
        for (Tbpagecolor record : pageColorList.getRecords()) {
            PageColorVo colorVo = new PageColorVo();
            BeanUtils.copyProperties(record, colorVo);
            if (record.getColorlist() != null && !StrUtil.isBlank(record.getColorlist())) {
                List<String> list = Arrays.asList(record.getColorlist().split(","));
                colorVo.setLsColors(list);
            }
            lsData.add(colorVo);
        }
        reslutPage.setRecords(lsData);
        reslutPage.setTotal(pageColorList.getTotal());
        return reslutPage;
    }

    @Override
    public List<PageColorVo> getPageColorAll() {
        List<Tbpagecolor> pagecolorList = new LambdaQueryChainWrapper<>(pageColorMapper)
                .eq(Tbpagecolor::getBuse, true)
                .orderByAsc(Tbpagecolor::getIsort)
                .list();
        List<PageColorVo> lsData = new ArrayList<>();
        for (Tbpagecolor record : pagecolorList) {
            PageColorVo colorVo = new PageColorVo();
            BeanUtils.copyProperties(record, colorVo);
            if (record.getColorlist() != null && !StrUtil.isBlank(record.getColorlist())) {
                List<String> list = Arrays.asList(record.getColorlist().split(","));
                colorVo.setLsColors(list);
            }
            lsData.add(colorVo);
        }
        return lsData;
    }
}
