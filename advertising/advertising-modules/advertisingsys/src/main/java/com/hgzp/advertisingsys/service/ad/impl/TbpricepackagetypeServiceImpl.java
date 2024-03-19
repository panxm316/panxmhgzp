package com.hgzp.advertisingsys.service.ad.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.ad.vo.PricePackageTypeVO;
import com.hgzp.advertisingsys.service.ad.TbpricepackagetypeServiceI;
import com.hgzp.core.model.Tbpricepackagetype;
import com.hgzp.mapper.ad.TbpricepackagetypeMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 打包类型表	 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbpricepackagetypeServiceImpl extends MyServiceImpl<TbpricepackagetypeMapper, Tbpricepackagetype> implements TbpricepackagetypeServiceI {
    @Resource
    private TbpricepackagetypeMapper mapper;

    /**
     * getPricePackageTypePageList
     * 方法功能:分页：获取打包类型
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbpricepackagetype>
     * @author yanz
     * @date 2023/8/31 15:11
     */
    @Override
    public IPage<Tbpricepackagetype> getPricePackageTypePageList(Page<Tbpricepackagetype> page, PricePackageTypeVO vo) {
        LambdaQueryWrapper<Tbpricepackagetype> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(vo.getQueryKey()), Tbpricepackagetype::getSname, vo.getQueryKey())
                .eq(ObjectUtil.isNotNull(vo.getBuse()), Tbpricepackagetype::getBuse, vo.getBuse());
        return mapper.selectPage(page, lqw);
    }

    /**
     * deletePricePackageTypeByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:21
     */
    @Override
    public boolean deletePricePackageTypeByIds(List<String> idList) {
        mapper.deleteBatchIds(idList);
        return true;
    }

    /**
     * isDuplicateSname
     * 方法功能:重名判断
     *
     * @param id
     * @param sname
     * @return boolean
     * @author yanz
     * @date 2024/2/27 14:19
     */
    @Override
    public boolean isDuplicateSname(Long id, String sname) {
        LambdaQueryWrapper<Tbpricepackagetype> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(id != null, Tbpricepackagetype::getId, id);
        wrapper.eq(StringUtils.hasText(sname), Tbpricepackagetype::getSname, sname);
        Long count = mapper.selectCount(wrapper);
        return count > 0;
    }
}
