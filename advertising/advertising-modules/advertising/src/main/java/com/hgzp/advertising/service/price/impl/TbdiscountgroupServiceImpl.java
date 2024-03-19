package com.hgzp.advertising.service.price.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertising.pagemodel.price.vo.DiscountItemVo;
import com.hgzp.advertising.service.price.TbdiscountgroupServiceI;
import com.hgzp.core.exception.DataExistException;
import com.hgzp.core.model.*;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.price.TbdiscountgroupMapper;
import com.hgzp.mapper.price.TbdiscountitemMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import com.hgzp.utils.WebUtil;
import com.hgzp.utils.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 折扣总表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbdiscountgroupServiceImpl extends MyServiceImpl<TbdiscountgroupMapper, Tbdiscountgroup> implements TbdiscountgroupServiceI {
    @Autowired
    private TbdiscountgroupMapper discountgroupMapper;
    @Autowired
    private TbdiscountitemMapper discountitemMapper;
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;

    @Override
    public IPage<Tbdiscountgroup> getDiscountGroupPageList(IPage<Tbdiscountgroup> page, DiscountItemVo queryInfo) {
        IPage<Tbdiscountgroup> discountgroupList = new LambdaQueryChainWrapper<>(discountgroupMapper)
                .like(StringUtils.hasText(queryInfo.getQueryKey()), Tbdiscountgroup::getDiscountgroupname,
                        queryInfo.getQueryKey())
                .eq(StringUtils.hasText(queryInfo.getSyear()), Tbdiscountgroup::getSyear, queryInfo.getSyear()).page(page);
        return discountgroupList;
    }

    @Override
    public void saveDiscountGroup(Tbdiscountgroup tbdiscountgroup) throws Exception {
        if (isExistDiscountGroup(tbdiscountgroup)) {
            throw new DataExistException("折扣总表名称已存在!");
        }
        LoginUser user = WebUtil.getLoginUser();
        tbdiscountgroup.setCreateempid(user.getUserid());
        tbdiscountgroup.setCreateempname(user.getUsername());
        tbdiscountgroup.setCreatedate(DateUtil.date());

        innerInterceptor.recoredLog();
        save(tbdiscountgroup);
    }

    @Override
    public void updateDiscountGroup(Tbdiscountgroup tbdiscountgroup) throws Exception {
        if (isExistDiscountGroup(tbdiscountgroup)) {
            throw new DataExistException("折扣总表名称已存在!");
        }
        LoginUser user = WebUtil.getLoginUser();
        tbdiscountgroup.setCreateempid(user.getUserid());
        tbdiscountgroup.setCreateempname(user.getUsername());
        tbdiscountgroup.setCreatedate(DateUtil.date());

        innerInterceptor.recoredLog();
        updateById(tbdiscountgroup);
        //计算折扣明细？
    }

    @Override
    public void deleteDiscountGroup(String ids) throws Exception {
        //判断有明细 不能删除;
        String[] split = ids.split(",");
        List<Long> deleteIdList = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        //执行删除记录数
        int iDelCount = 0;
        for (Long id : deleteIdList) {
            Long count = new LambdaQueryChainWrapper<>(discountitemMapper)
                    .eq(Tbdiscountitem::getDiscountgroupid, id)
                    .count();
            if (count > 0) {
                continue;
            }
            innerInterceptor.recoredLog();
            discountgroupMapper.deleteById(id);
            iDelCount++;
        }
        if (iDelCount != deleteIdList.size()) {
            throw new DataExistException("存在折扣明细，不能删除!");
        }
    }

    @Override
    public List<Tbdiscountgroup> getDiscountGroupList(String sYear) {
        List<Tbdiscountgroup> discountitemList = new LambdaQueryChainWrapper<>(discountgroupMapper)
                .eq(StringUtils.hasText(sYear), Tbdiscountgroup::getSyear, sYear)
                .eq(Tbdiscountgroup::getBuse, true)
                .list();

        return discountitemList;
    }

    @Override
    public String getNdiscountByYear(DiscountItemVo discountItemVo) throws Exception {
        Tbdiscountgroup discountGroup = new LambdaQueryChainWrapper<>(discountgroupMapper)
                .eq(StringUtils.hasText(discountItemVo.getSyear()), Tbdiscountgroup::getSyear,
                        discountItemVo.getSyear())
                .eq(Tbdiscountgroup::getBuse, true)
                .one();
        if (discountGroup == null) {
            return "0";
        }
        Tbdiscountitem discountitem = new LambdaQueryChainWrapper<>(discountitemMapper)
                .eq(Tbdiscountitem::getDiscountgroupid, discountGroup.getId())
                .eq(ObjectUtil.isNotNull(discountItemVo.getMediaid()), Tbdiscountitem::getMediaid,
                        discountItemVo.getMediaid())
                .eq(ObjectUtil.isNotNull(discountItemVo.getAdindustryid()), Tbdiscountitem::getAdindustryid,
                        discountItemVo.getAdindustryid())
                .eq(discountItemVo.getBagency(), Tbdiscountitem::getBagency, discountItemVo.getBagency())
                .eq(discountItemVo.getBagent(), Tbdiscountitem::getBagent, discountItemVo.getBagency())
                .eq(discountItemVo.getBcustomer(), Tbdiscountitem::getBcustomer, discountItemVo.getBcustomer())
                .eq(discountItemVo.getBvip(), Tbdiscountitem::getBvip, discountItemVo.getBvip())
                .one();
        String sNdiscount = "0";
        if (ObjectUtil.isNotNull(discountitem)) {
            sNdiscount = discountitem.getNdiscount().toString();
        } else {
            //取Group中数据
            BigDecimal[] arr = new BigDecimal[]{discountGroup.getNcustomerdiscount(),
                    discountGroup.getNagencydiscount(), discountGroup.getNagentdiscount()};
            Arrays.sort(arr);
            if (discountGroup.getBhighest()) {
                sNdiscount = arr[2].toString();
            }else{
                sNdiscount=arr[0].toString();
            }
        }

        return sNdiscount;
    }

    private boolean isExistDiscountGroup(Tbdiscountgroup tbdiscountgroup) {
        Long count = new LambdaQueryChainWrapper<>(discountgroupMapper)
                .eq(Tbdiscountgroup::getBuse, true)
                .eq(Tbdiscountgroup::getDiscountgroupname, tbdiscountgroup.getDiscountgroupname())
                .ne(tbdiscountgroup.getId() != null, Tbdiscountgroup::getId, tbdiscountgroup.getId())
                .count();
        return count > 0;
    }
}
