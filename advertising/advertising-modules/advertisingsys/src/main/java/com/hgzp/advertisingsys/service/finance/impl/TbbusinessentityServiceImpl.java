package com.hgzp.advertisingsys.service.finance.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.service.finance.TbbusinessentityServiceI;
import com.hgzp.core.model.Tbbusinessentity;
import com.hgzp.core.model.Tbfoldpageplan;
import com.hgzp.core.model.Tbpagesize;
import com.hgzp.core.model.Tworder;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.ad.TworderMapper;
import com.hgzp.mapper.finance.TbbusinessentityMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 经营主体 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbbusinessentityServiceImpl extends MyServiceImpl<TbbusinessentityMapper, Tbbusinessentity> implements TbbusinessentityServiceI {
    @Autowired
    TbbusinessentityMapper businessentityMapper;
    @Autowired
    TworderMapper tworderMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public void deleteBusinessentity(String ids) throws Exception{
//        String[] split = ids.split(",");
//        businessentityMapper.deleteBatchIds(Arrays.asList(split));
        String[] split = ids.split(",");
        List<Tbbusinessentity> lsItems = this.lambdaQuery()
                .in(Tbbusinessentity::getId, split)
                .list();
        String sInfo="";
        if(lsItems.size()>0){
            for(Tbbusinessentity item:lsItems){
                // 判断业务
                Long count= new LambdaQueryChainWrapper<>(tworderMapper)
                        .eq(Tworder::getBusinessentityid, item.getId())
                        .count();
                if(count==0){
                    innerInterceptor.recoredLog();
                    if(!removeById(item)){
                        throw  new RuntimeException("删除失败");
                    }
                }else {
                    sInfo +=item.getSname()+"已被使用，不能删除！"+"\r\n";
                }
            }
        }
        if(StrUtil.isNotBlank(sInfo)){
            throw  new RuntimeException("删除失败:"+sInfo);
        }
    }

    @Override
    public IPage<Tbbusinessentity> getBusinessentityPageList(Page<Tbbusinessentity> page, String querykey) {
        LambdaQueryWrapper<Tbbusinessentity> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(querykey), Tbbusinessentity::getSname, querykey);
        IPage<Tbbusinessentity> tbbusinessentityPage = businessentityMapper.selectPage(page, lqw);
        return tbbusinessentityPage;

    }

    @Override
    public boolean isExistBusinessentity(Tbbusinessentity tbbusinessentity) {
        Long count = new LambdaQueryChainWrapper<>(businessentityMapper)
                .eq(Tbbusinessentity::getSname, tbbusinessentity.getSname())
                .ne(tbbusinessentity.getId() != null, Tbbusinessentity::getId, tbbusinessentity.getId())
                .count();
        return count > 0;
    }

    @Override
    public Json doDefaultLogic(Tbbusinessentity tbbusinessentity) {
        if (tbbusinessentity.getBdefault()) {
            List<Tbbusinessentity> list = businessentityMapper.selectList(null);
            for (Tbbusinessentity t : list) {
                t.setBdefault(false);
                businessentityMapper.updateById(t);
            }
        } else {
            Long count = new LambdaQueryChainWrapper<>(businessentityMapper)
                    .ne(tbbusinessentity.getId() != null, Tbbusinessentity::getId, tbbusinessentity.getId())
                    .eq(Tbbusinessentity::getBdefault, true)
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

    /**
     * getBzEntityTypeCombo
     * 方法功能:获取经营主体类型列表
     *
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author yanz
     * @date 2024/1/25 16:00
     */
    @Override
    public List<DataCombo> getBzEntityTypeCombo() {
        List<Tbbusinessentity> bzEntityList = this.lambdaQuery()
                .eq(Tbbusinessentity::getBuse, true)
                .orderByDesc(Tbbusinessentity::getIsort)
                .list();
        List<DataCombo> comboList = bzEntityList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getSname()))
                .collect(Collectors.toList());
        return comboList;
    }
}
