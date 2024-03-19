package com.hgzp.advertising.service.price.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.price.dto.PriceItemDTO;
import com.hgzp.advertising.pagemodel.price.vo.PriceGroupVO;
import com.hgzp.advertising.pagemodel.price.vo.PriceItemVO;
import com.hgzp.advertising.service.price.TbpricegroupServiceI;
import com.hgzp.advertising.service.price.TbpriceitemServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.Tbdiscountgroup;
import com.hgzp.core.model.Tbdiscountitem;
import com.hgzp.core.model.Tbpricegroup;
import com.hgzp.core.model.Tbpriceitem;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.price.TbpriceitemMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 价格明细表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-09
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbpriceitemServiceImpl extends MyServiceImpl<TbpriceitemMapper, Tbpriceitem> implements TbpriceitemServiceI {
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    TbpriceitemMapper tbpriceitemMapper;
    @Autowired
    TbpricegroupServiceI tbpricegroupServiceI;

    @Override
    public Page<PriceItemVO> getPriceItemPageList(Page<Tbpriceitem> page, PriceItemVO query) {
        Page<PriceItemVO> reslutPage = new Page<>();
        // 查询媒体类型下默认价格组id
        List<Long> grouids = tbpricegroupServiceI.getQueryGroupId(query.getMediatypekey());
        Date endDate = query.getDenddate() != null ? DateUtil.offsetDay(query.getDenddate(), 1): new Date();
        Page<Tbpriceitem> priceitemPage = this.lambdaQuery()
                .eq(query.getPricegroupid() != null, Tbpriceitem::getPricegroupid, query.getPricegroupid())
                .eq(StrUtil.isNotBlank(query.getMediatypekey()), Tbpriceitem::getMediatypekey, query.getMediatypekey())
                .eq(query.getFoldid() != null, Tbpriceitem::getFoldid, query.getFoldid())
                .eq(query.getFoldareaid() != null, Tbpriceitem::getFoldareaid, query.getFoldareaid())
                .eq(query.getPagesortid() != null, Tbpriceitem::getPagesortid, query.getPagesortid())
                .eq(query.getAdcolorid() != null, Tbpriceitem::getAdcolorid, query.getAdcolorid())
                .eq(query.getAdspecid() != null, Tbpriceitem::getAdspecid, query.getAdspecid())
                .eq(query.getAddisplayformid() != null, Tbpriceitem::getAddisplayformid, query.getAddisplayformid())
                .eq(query.getWeeksettingid() != null, Tbpriceitem::getWeeksettingid, query.getWeeksettingid())
                .eq(query.getMediaid() != null, Tbpriceitem::getMediaid, query.getMediaid())
                .ge(query.getDstartdate() != null, Tbpriceitem::getDstartdate, query.getDstartdate())

                .and(query.getDenddate() != null, i -> i.isNull(Tbpriceitem::getDenddate)
                        .or()
                        .le(Tbpriceitem::getDenddate, endDate)
                )
                .eq(Tbpriceitem::getBuse, true)
                // 如果组id为空，则只查询当前媒体类型下默认价格明细
                .in(query.getPricegroupid() == null, Tbpriceitem::getPricegroupid, grouids)
                .page(page);
        if (priceitemPage.getTotal() == 0) {
            return reslutPage;
        }
        BeanUtils.copyProperties(priceitemPage, reslutPage);
        return reslutPage;
    }

    @Override
    public void savePriceItem(PriceItemDTO priceItemDTO) {
        Tbpriceitem tbpriceitem = new Tbpriceitem();
        priceItemDTO.setId(IdUtil.getSnowflakeNextId());
        BeanUtils.copyProperties(priceItemDTO, tbpriceitem);
        verifySavePriceItem(tbpriceitem);
        // 添加时序号自动递增
        Integer maxSort = this.getMaxSort();
        tbpriceitem.setIsort(maxSort);
        innerInterceptor.recoredLog();
        tbpriceitemMapper.insert(tbpriceitem);
    }

    @Override
    public void updatePriceItem(PriceItemDTO priceItemDTO) {
        Tbpriceitem tbpriceitem = new Tbpriceitem();
        BeanUtils.copyProperties(priceItemDTO, tbpriceitem);
        verifySavePriceItem(tbpriceitem);
        innerInterceptor.recoredLog();
        this.lambdaUpdate()
                .eq(Tbpriceitem::getId, priceItemDTO.getId())
                .set(Tbpriceitem::getDstartdate, priceItemDTO.getDstartdate())
                .set(Tbpriceitem::getDenddate, priceItemDTO.getDenddate())
                .update(tbpriceitem);

//        tbpriceitemMapper.updateById(tbpriceitem);
    }

    @Override
    public void deletePriceItem(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        innerInterceptor.recoredLog();
        tbpriceitemMapper.deleteBatchIds(idList);
//        this.removeByIds(idList);
    }

    /**
     * 校验价格明细是否能保存
     * 方法功能: 如果查询出条件一样的价格则不能重复添加
     * （七个条件并且不在同一时间段内的，开始日期结束日期都没有的表示公共的同样条件的只能有一个）只判断启用的
     *
     * @param tbpriceitem
     * @return void
     * @author CGD
     * @date 2023/11/15 16:28
     */
    private void verifySavePriceItem(Tbpriceitem tbpriceitem) {
        List<Tbpriceitem> priceitemList = this.lambdaQuery()
                .ne(tbpriceitem.getId() != null, Tbpriceitem::getId, tbpriceitem.getId())
                .eq(Tbpriceitem::getBuse, true)
                .eq(tbpriceitem.getPricegroupid() != null, Tbpriceitem::getPricegroupid, tbpriceitem.getPricegroupid())
                .eq(tbpriceitem.getMediatypekey() != null, Tbpriceitem::getMediatypekey, tbpriceitem.getMediatypekey())
                .eq(tbpriceitem.getFoldid() != null, Tbpriceitem::getFoldid, tbpriceitem.getFoldid())
                .eq(tbpriceitem.getFoldareaid() != null, Tbpriceitem::getFoldareaid, tbpriceitem.getFoldareaid())
                .eq(tbpriceitem.getPagesortid() != null, Tbpriceitem::getPagesortid, tbpriceitem.getPagesortid())
                .eq(tbpriceitem.getAddisplayformid() != null, Tbpriceitem::getAddisplayformid, tbpriceitem.getAddisplayformid())
                .eq(tbpriceitem.getAdspecid() != null, Tbpriceitem::getAdspecid, tbpriceitem.getAdspecid())
                .eq(tbpriceitem.getAdcolorid() != null, Tbpriceitem::getAdcolorid, tbpriceitem.getAdcolorid())
                .eq(tbpriceitem.getWeeksettingid() != null, Tbpriceitem::getWeeksettingid, tbpriceitem.getWeeksettingid())
                // 开始日期默认必须有的情况下
                // 如果只输入了开始日期(T-F) 则判断 （end is null ) || ( end >= T-F)
                // 如果输入了开始(T-F)和结束(T-E) 则判断 ((end >= T-F  || end is null) && fir <= T-E)
                .and(i -> i.and(tbpriceitem.getDenddate() != null && tbpriceitem.getDstartdate() != null,
                                        a -> a.le(Tbpriceitem::getDstartdate, tbpriceitem.getDenddate())
                                                .and(p -> p.ge(Tbpriceitem::getDenddate, tbpriceitem.getDstartdate())
                                                        .or()
                                                        .isNull(Tbpriceitem::getDenddate)
                                                )
                                )
                                .or()
                                .and(tbpriceitem.getDenddate() == null && tbpriceitem.getDstartdate() != null,
                                        b -> b.isNull(Tbpriceitem::getDenddate)
                                                .or()
                                                .ge(Tbpriceitem::getDenddate, tbpriceitem.getDstartdate()))
                )
                .list();
        if (priceitemList.size() > 0) {
            throw new DataExistException("相应价格明细已存在");
        }
    }

    @Override
    public void batchUpdatePriceItem(String ids, BaseQueryInfo queryInfo) {
        List<String> idList = Arrays.asList(ids.split(","));
        List<Tbpriceitem> tbpriceitems = tbpriceitemMapper.selectBatchIds(idList);
        for (Tbpriceitem tbpriceitem : tbpriceitems) {
            tbpriceitem.setDenddate(queryInfo.getEndTime());
            verifySavePriceItem(tbpriceitem);

            innerInterceptor.recoredLog();
            tbpriceitemMapper.updateById(tbpriceitem);
        }
    }

    @Override
    public List<Tbpriceitem> getPriceItemList(PriceItemVO query)  {

        List<Long> grouids=new ArrayList<>();
        if(StrUtil.isNotBlank( query.getSyear())) {
            PriceGroupVO priceGroupVO = new PriceGroupVO();
            priceGroupVO.setBuse(true);
            priceGroupVO.setSyear(query.getSyear());
            priceGroupVO.setMediatypekey(query.getMediatypekey());
            try {
                List<Tbpricegroup> tbpricegroups = tbpricegroupServiceI.getPriceGroupList(priceGroupVO);
                grouids = tbpricegroups.stream().map(Tbpricegroup::getId).collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Tbpriceitem> priceitemList = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(query.getMediatypekey()), Tbpriceitem::getMediatypekey, query.getMediatypekey())
                .eq(query.getFoldid() != null, Tbpriceitem::getFoldid, query.getFoldid())
                .eq(query.getFoldareaid() != null, Tbpriceitem::getFoldareaid, query.getFoldareaid())
                .eq(query.getPagesortid() != null, Tbpriceitem::getPagesortid, query.getPagesortid())
                .eq(query.getAdcolorid() != null, Tbpriceitem::getAdcolorid, query.getAdcolorid())
                .eq(query.getAdspecid() != null, Tbpriceitem::getAdspecid, query.getAdspecid())
                .eq(query.getAddisplayformid() != null, Tbpriceitem::getAddisplayformid, query.getAddisplayformid())
                .eq(query.getWeeksettingid() != null, Tbpriceitem::getWeeksettingid, query.getWeeksettingid())
                .eq(query.getMediaid() != null, Tbpriceitem::getMediaid, query.getMediaid())
                .ge(query.getDstartdate() != null, Tbpriceitem::getDstartdate, query.getDstartdate())
                .and(query.getDenddate() != null, i -> i.isNull(Tbpriceitem::getDenddate)
                        .or()
                        .le(Tbpriceitem::getDenddate, DateUtil.offsetDay(query.getDenddate(), 1))
                )
                .eq(Tbpriceitem::getBuse, true)
                .in(grouids.size()>0, Tbpriceitem::getPricegroupid, grouids)
                .list();
        return priceitemList;
    }

    @Override
    public void SaveCopyPriceItem(Long oldpricegroupid, Long newpricegroupid, BaseQueryInfo queryInfo) {
        LambdaQueryWrapper<Tbpriceitem> lqw = Wrappers.lambdaQuery();
        lqw.eq(Tbpriceitem::getPricegroupid, oldpricegroupid);
        lqw.eq(Tbpriceitem::getBuse, true);

        List<Tbpriceitem> tbpriceitems = tbpriceitemMapper.selectList(lqw);
        for (Tbpriceitem tbpriceitem : tbpriceitems) {
            Tbpriceitem tbpriceitem1 = new Tbpriceitem();
            BeanUtils.copyProperties(tbpriceitem, tbpriceitem1);
            tbpriceitem1.setPricegroupid(newpricegroupid);
            tbpriceitem1.setId(IdUtil.getSnowflakeNextId());
            tbpriceitem1.setDstartdate(queryInfo.getStartTime());
            tbpriceitem1.setDenddate(queryInfo.getEndTime());
            tbpriceitem1.setIsort(this.getMaxSort());
            verifySavePriceItem(tbpriceitem1);

            innerInterceptor.recoredLog();
            tbpriceitemMapper.insert(tbpriceitem1);
        }
    }
}
