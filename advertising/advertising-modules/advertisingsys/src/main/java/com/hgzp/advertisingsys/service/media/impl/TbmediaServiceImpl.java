package com.hgzp.advertisingsys.service.media.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.media.dto.MediaDTO;
import com.hgzp.advertisingsys.service.media.TbmediaServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.TreeModel;
import com.hgzp.core.page.TreeQuery;
import com.hgzp.mapper.ad.TwadresourcefilesMapper;
import com.hgzp.mapper.ad.TworderitemMapper;
import com.hgzp.mapper.business.TwtasksMapper;
import com.hgzp.mapper.media.TbadspecMapper;
import com.hgzp.mapper.media.TbfoldMapper;
import com.hgzp.mapper.media.TbmediaMapper;
import com.hgzp.mapper.media.TbmediapublicnumMapper;
import com.hgzp.mapper.price.TbcommissionrateitemMapper;
import com.hgzp.mapper.price.TbdiscountitemMapper;
import com.hgzp.mapper.price.TbpriceitemMapper;
import com.hgzp.mapper.schedule.TbfoldpageplanMapper;
import com.hgzp.mapper.schedule.TworderitemarrangeMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 媒体信息 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbmediaServiceImpl extends MyServiceImpl<TbmediaMapper, Tbmedia> implements TbmediaServiceI {
    @Autowired
    private TbmediaMapper tbmediaMapper;
    @Autowired
    private TbfoldMapper tbfoldMapper;
    @Autowired
    private TbadspecMapper tbadspecMapper;
    @Autowired
    private TbmediapublicnumMapper tbmediapublicnumMapper;
    @Autowired
    private TworderitemMapper tworderitemMapper;
    @Autowired
    private TbcommissionrateitemMapper tbcommissionrateitemMapper;
    @Autowired
    private TbdiscountitemMapper tbdiscountitemMapper;
    @Autowired
    private TbfoldpageplanMapper tbfoldpageplanMapper;
    @Autowired
    private TbpriceitemMapper tbpriceitemMapper;
    @Autowired
    private TwadresourcefilesMapper twadresourcefilesMapper;
    @Autowired
    private TworderitemarrangeMapper tworderitemarrangeMapper;
    @Autowired
    private TwtasksMapper twtasksMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<Tbmedia> getMediaPageList(Page<Tbmedia> page, BaseQueryInfo query) {
        LambdaQueryWrapper<Tbmedia> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(query.getQueryKey()), Tbmedia::getSname, query.getQueryKey());
        return tbmediaMapper.selectPage(page, lqw);
    }

    @Override
    public void saveMedia(MediaDTO media) {
        if (isExistFold(media)) {
            throw new DataExistException("已存在同名媒体或相同编码");
        }
        Tbmedia tbmedia = new Tbmedia();
        BeanUtils.copyProperties(media, tbmedia);
        tbmedia.setId(IdUtil.getSnowflakeNextId());

        innerInterceptor.recoredLog();
        tbmediaMapper.insert(tbmedia);
    }

    @Override
    public void updateMedia(MediaDTO media) {
        if (isExistFold(media)) {
            throw new DataExistException("已存在同名媒体或相同编码");
        }
        Tbmedia tbmedia = new Tbmedia();
        BeanUtils.copyProperties(media, tbmedia);
        innerInterceptor.recoredLog();
        tbmediaMapper.updateById(tbmedia);
        //需更新下面表中的媒体名称
        Tbmedia mediaInfo=getById(media.getId());
        if(!media.getSname().trim().equals(mediaInfo.getSname().trim())) {
            //Tbmediapublicnum  媒体刊期
            List<Tbmediapublicnum> lsMediaPublicNum = new LambdaQueryChainWrapper<>(tbmediapublicnumMapper)
                    .eq(Tbmediapublicnum::getMediaid, media.getId())
                    .list();
            if(lsMediaPublicNum.size()>0){
                lsMediaPublicNum.forEach(item->{
                    item.setMedianame(media.getSname());
                    innerInterceptor.recoredLog();
                    tbmediapublicnumMapper.updateById(item);
                });
            }
            //Tbcommissionrateitem  计提比例明细表
            List<Tbcommissionrateitem> lsCommissionRateItem = new LambdaQueryChainWrapper<>(tbcommissionrateitemMapper)
                    .eq(Tbcommissionrateitem::getMediaid, media.getId())
                    .list();
            if(lsCommissionRateItem.size()>0){
                lsCommissionRateItem.forEach(item->{
                    item.setMedianame(media.getSname());
                    innerInterceptor.recoredLog();
                    tbcommissionrateitemMapper.updateById(item);
                });
            }
            //Tbdiscountitem  折扣明细
            List<Tbdiscountitem> lsDiscountItem = new LambdaQueryChainWrapper<>(tbdiscountitemMapper)
                    .eq(Tbdiscountitem::getMediaid, media.getId())
                    .list();
            if(lsDiscountItem.size()>0){
                lsDiscountItem.forEach(item->{
                    item.setMedianame(media.getSname());
                    innerInterceptor.recoredLog();
                    tbdiscountitemMapper.updateById(item);
                });
            }
            //Tbfoldpageplan 版面计划
            List<Tbfoldpageplan> lsFoldPagePlan = new LambdaQueryChainWrapper<>(tbfoldpageplanMapper)
                    .eq(Tbfoldpageplan::getMediaid, media.getId())
                    .list();
            if(lsFoldPagePlan.size()>0){
                lsFoldPagePlan.forEach(item->{
                    item.setMedianame(media.getSname());
                    innerInterceptor.recoredLog();
                    tbfoldpageplanMapper.updateById(item);
                });
            }
            //Tbpriceitem 价格明细
            List<Tbpriceitem> lsPriceItem = new LambdaQueryChainWrapper<>(tbpriceitemMapper)
                    .eq(Tbpriceitem::getMediaid, media.getId())
                    .list();
            if(lsPriceItem.size()>0){
                lsPriceItem.forEach(item->{
                    item.setMedianame(media.getSname());
                    innerInterceptor.recoredLog();
                    tbpriceitemMapper.updateById(item);
                });
            }
            //Twadresourcefiles 广告资源申请文件
            List<Twadresourcefiles> lsAdResourceFiles = new LambdaQueryChainWrapper<>(twadresourcefilesMapper)
                    .eq(Twadresourcefiles::getMediaid, media.getId())
                    .list();
            if(lsAdResourceFiles.size()>0){
                lsAdResourceFiles.forEach(item->{
                    item.setMedianame(media.getSname());
                    innerInterceptor.recoredLog();
                    twadresourcefilesMapper.updateById(item);
                });
            }
            //Tworderitem 订单明细
            List<Tworderitem> lsOrderItem = new LambdaQueryChainWrapper<>(tworderitemMapper)
                    .eq(Tworderitem::getMediaid, media.getId())
                    .list();
            if(lsOrderItem.size()>0){
                lsOrderItem.forEach(item->{
                    item.setMedianame(media.getSname());
                    innerInterceptor.recoredLog();
                    tworderitemMapper.updateById(item);
                });
            }
            //Tworderitemarrange 安排
            List<Tworderitemarrange> lsOrderItemArrange = new LambdaQueryChainWrapper<>(tworderitemarrangeMapper)
                    .eq(Tworderitemarrange::getMediaid, media.getId())
                    .list();
            if(lsOrderItemArrange.size()>0){
                lsOrderItemArrange.forEach(item->{
                    item.setMedianame(media.getSname());
                    innerInterceptor.recoredLog();
                    tworderitemarrangeMapper.updateById(item);
                });
            }
            //Twtasks 任务额度
            List<Twtasks> lsTasks = new LambdaQueryChainWrapper<>(twtasksMapper)
                    .eq(Twtasks::getMediaid, media.getId())
                    .list();
            if(lsTasks.size()>0){
                lsTasks.forEach(item->{
                    item.setMedianame(media.getSname());
                    innerInterceptor.recoredLog();
                    twtasksMapper.updateById(item);
                });
            }
        }
    }

    @Override
    public void deleteMedia(String ids) throws Exception {
        String[] split = ids.split(",");
        List<Tbmedia> lsMediaItems = this.lambdaQuery()
                .in(Tbmedia::getId, split)
                .list();
        String sInfo="";
        if(lsMediaItems.size()>0){
            for(Tbmedia item:lsMediaItems){
                // 判断业务
                Long count= new LambdaQueryChainWrapper<>(tworderitemMapper)
                        .eq(Tworderitem::getMediaid, item.getId())
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

    /**
     * isExistFold
     * 方法功能:  判断是否有重名
     *
     * @param tbmedia
     * @return boolean
     * @author suny
     * @date 2023/8/25 9:17
     */
    public boolean isExistFold(MediaDTO tbmedia) {
        Long count = new LambdaQueryChainWrapper<>(tbmediaMapper)
                .and(m->m.eq(Tbmedia::getSname, tbmedia.getSname())
                .or()
                .eq(Tbmedia::getScode, tbmedia.getScode()))
                .ne(tbmedia.getId() != null, Tbmedia::getId, tbmedia.getId())
                .count();
        return count > 0;
    }

    @Override
    public List<TreeModel> getSysMediaTree(TreeQuery query, boolean showUnuseDept) {
        List<Tbmedia> mediaList = this.lambdaQuery()
                .and(StrUtil.isNotBlank(query.getQueryKey()), m ->
                        m.like(Tbmedia::getSname, query.getQueryKey())
                                .or()
                                .in(StrUtil.isNotBlank(query.getContainsIds()), Tbmedia::getId, query.getContainsIds().split(",")))
                .eq(!showUnuseDept, Tbmedia::getBuse, true)
                .orderByAsc(Tbmedia::getIsort)
                .list();
        List<TreeModel> treeModelList = mediaList.stream().map(m -> {
            TreeModel treeModel = new TreeModel();
            treeModel.setId(m.getId());
            treeModel.setIconSkin(TreeModel.UNIT);
            treeModel.setName(m.getSname());
            treeModel.setNocheck(false);
            return treeModel;
        }).collect(Collectors.toList());
        return treeModelList;
    }

    @Override
    public List<DataCombo> getMediaDataCombo() {
        List<Tbmedia> mediaList = this.lambdaQuery()
                .eq(Tbmedia::getBuse, true)
                .orderByDesc(Tbmedia::getIsort)
                .list();
        List<DataCombo> comboList = mediaList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getSname()))
                .collect(Collectors.toList());
        return comboList;
    }

    @Override
    public String getMediaChild(String ids) {
        String[] split = ids.split(",");
        LambdaQueryWrapper<Tbfold> lqw = Wrappers.lambdaQuery();
        lqw.in(Tbfold::getMediaid, split);
        Long folds = tbfoldMapper.selectCount(lqw);
        if (folds > 0) {
            return "媒体下存在叠次信息";
        }
        LambdaQueryWrapper<Tbadspec> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.in(Tbadspec::getMediaid, split);
        Long pagesorts = tbadspecMapper.selectCount(lambdaQuery);
        if (pagesorts > 0) {
            return "媒体下存在广告规格";
        }
        LambdaQueryWrapper<Tbmediapublicnum> lqw_mediap = Wrappers.lambdaQuery();
        lqw_mediap.in(Tbmediapublicnum::getMediaid, split);
        Long mediaps = tbmediapublicnumMapper.selectCount(lqw_mediap);
        if (mediaps > 0) {
            return "媒体下存在媒体刊期";
        }
        return "";
    }
}
