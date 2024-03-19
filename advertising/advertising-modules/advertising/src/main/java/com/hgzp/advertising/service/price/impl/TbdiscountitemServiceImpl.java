package com.hgzp.advertising.service.price.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.price.vo.DiscountItemVo;
import com.hgzp.advertising.service.price.TbdiscountitemServiceI;
import com.hgzp.core.model.Tbdiscountgroup;
import com.hgzp.core.model.Tbdiscountitem;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.price.TbdiscountgroupMapper;
import com.hgzp.mapper.price.TbdiscountitemMapper;
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
 * 折扣明细表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-21
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class TbdiscountitemServiceImpl extends MyServiceImpl<TbdiscountitemMapper, Tbdiscountitem> implements TbdiscountitemServiceI {

    @Autowired
    private TbdiscountitemMapper discountitemMapper;
    @Autowired
    private TbdiscountgroupMapper discountgroupMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<Tbdiscountitem> getDiscountItemPageList(IPage<Tbdiscountitem> page, DiscountItemVo queryInfo) {
        if(queryInfo.getDiscountgroupid()==null){
            return null;
            //throw new RuntimeException("折扣总表id不可为空");
        }
        IPage<Tbdiscountitem> discountItemList = new LambdaQueryChainWrapper<>(discountitemMapper)
                .eq(Tbdiscountitem::getDiscountgroupid,queryInfo.getDiscountgroupid())
                .eq(ObjectUtil.isNotNull(queryInfo.getMediaid()), Tbdiscountitem::getMediaid, queryInfo.getMediaid())
                .eq(ObjectUtil.isNotNull(queryInfo.getAdindustryid()), Tbdiscountitem::getAdindustryid, queryInfo.getAdindustryid())
                .orderByAsc(Tbdiscountitem::getMediaid)
                .page(page);
        return discountItemList;
    }

    @Override
    public void saveDiscountItem(Tbdiscountitem tbdiscountitem) throws Exception {
        if(isExistDiscountItem(tbdiscountitem)) {
            throw new RuntimeException("折扣明细已存在!");
        }
        innerInterceptor.recoredLog();
        save(tbdiscountitem);
    }

    @Override
    public void updateDiscountItem(Tbdiscountitem tbdiscountitem) throws Exception {
        if(isExistDiscountItem(tbdiscountitem)) {
            throw new RuntimeException("折扣明细已存在!");
        }
        innerInterceptor.recoredLog();
        updateById(tbdiscountitem);
    }

    @Override
    public void deleteDiscountItem(String ids) throws Exception {
        List<String> idList = Arrays.asList(ids.split(","));
        removeBatchByIds(idList);
    }

    @Override
    public List<Tbdiscountitem> getDiscountItemList(DiscountItemVo discountItemVo) {
        List<Long> lsDiscountgroupId = new ArrayList<>();
        if(!"".equals(discountItemVo.getSyear())){
            lsDiscountgroupId = new LambdaQueryChainWrapper<>(discountgroupMapper)
                    .eq(Tbdiscountgroup::getSyear, discountItemVo.getSyear())
                    .list()
                    .stream()
                    .map(Tbdiscountgroup::getId)
                    .collect(Collectors.toList());
        }
        List<Tbdiscountitem> discountitemList = new LambdaQueryChainWrapper<>(discountitemMapper)
                .in(lsDiscountgroupId.size()>0,Tbdiscountitem::getDiscountgroupid,lsDiscountgroupId)
                .eq(ObjectUtil.isNotNull(discountItemVo.getMediaid()), Tbdiscountitem::getMediaid, discountItemVo.getMediaid())
                .eq(ObjectUtil.isNotNull(discountItemVo.getAdindustryid()), Tbdiscountitem::getAdindustryid, discountItemVo.getAdindustryid())
                .eq(discountItemVo.getBagency(), Tbdiscountitem::getBagency, discountItemVo.getBagency())
                .eq(discountItemVo.getBagent(), Tbdiscountitem::getBagent, discountItemVo.getBagency())
                .eq(discountItemVo.getBcustomer(), Tbdiscountitem::getBcustomer, discountItemVo.getBcustomer())
                .eq(discountItemVo.getBvip(), Tbdiscountitem::getBvip, discountItemVo.getBvip())
                .list();
        return discountitemList;
    }

    private boolean isExistDiscountItem(Tbdiscountitem tbdiscountitem) {
        Long count = new LambdaQueryChainWrapper<>(discountitemMapper)
                .eq(Tbdiscountitem::getDiscountgroupid, tbdiscountitem.getDiscountgroupid())
                .eq(Tbdiscountitem::getMediaid, tbdiscountitem.getMediaid())
                .eq(Tbdiscountitem::getAdindustryid, tbdiscountitem.getAdindustryid())
                .eq(Tbdiscountitem::getBagency, tbdiscountitem.getBagency())
                .eq(Tbdiscountitem::getBcustomer, tbdiscountitem.getBcustomer())
                .eq(Tbdiscountitem::getBagent, tbdiscountitem.getBagent())
                .eq(Tbdiscountitem::getBvip, tbdiscountitem.getBvip())
                .ne(tbdiscountitem.getId() != null, Tbdiscountitem::getId, tbdiscountitem.getId())
                .count();
        return count > 0;
    }
}
