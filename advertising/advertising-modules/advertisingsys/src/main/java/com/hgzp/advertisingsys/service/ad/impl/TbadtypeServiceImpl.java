package com.hgzp.advertisingsys.service.ad.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.advertisingsys.pagemodel.ad.vo.AdTypeVO;
import com.hgzp.advertisingsys.service.ad.TbadtypeServiceI;
import com.hgzp.core.model.Tbadtype;
import com.hgzp.core.page.DataCombo;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.ad.TbadtypeMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 广告类型 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadtypeServiceImpl extends MyServiceImpl<TbadtypeMapper, Tbadtype> implements TbadtypeServiceI {
    @Autowired
    HgDataChangeRecorderInnerInterceptor innerInterceptor;
    @Autowired
    private TbadtypeMapper mapper;
    /**
     * getAdTypePageList
     * 方法功能:分页：获取广告类型
     * @author yanz
     * @date 2023/8/31 15:12
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbadtype>
     */
    @Override
    public IPage<Tbadtype> getAdTypePageList(Page<Tbadtype> page, AdTypeVO vo) {
        LambdaQueryWrapper<Tbadtype> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(vo.getQueryKey()), Tbadtype::getSname, vo.getQueryKey())
                .eq(ObjectUtil.isNotNull(vo.getBuse()), Tbadtype::getBuse, vo.getBuse());
        return mapper.selectPage(page, lqw);
    }

    /**
     * isDuplicateSname
     * 方法功能:是否存在同名对象
     *
     * @param id
     * @param sname
     * @return boolean
     * @author yanz
     * @date 2023/8/25 9:51
     */
    @Override
    public boolean isDuplicateSname(Long id, String sname) {
        LambdaQueryWrapper<Tbadtype> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(id != null, Tbadtype::getId, id);
        wrapper.eq(StringUtils.hasText(sname), Tbadtype::getSname, sname);
        Long count=mapper.selectCount(wrapper);
        return count>0;
    }

    /**
     * deleteAdTypeByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:11
     */
    @Override
    public boolean deleteAdTypeByIds(List<String> idList) {
        innerInterceptor.recoredLog();
        mapper.deleteBatchIds(idList);
        return true;
    }

    @Override
    public Json doDefaultLogic(Tbadtype tbadtype) {
        if (tbadtype.getBdefault()) {
            List<Tbadtype> list = mapper.selectList(null);
            for (Tbadtype t : list) {
                t.setBdefault(false);
                mapper.updateById(t);
            }
        } else {
            Long count = new LambdaQueryChainWrapper<>(mapper)
                    .ne(tbadtype.getId() != null, Tbadtype::getId, tbadtype.getId())
                    .eq(Tbadtype::getBdefault, true)
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

    @Override
    public List<DataCombo> getAdTypeCombo() {
        List<Tbadtype> adtypeList = this.lambdaQuery()
                .eq(Tbadtype::getBuse, true)
                .orderByDesc(Tbadtype::getIsort)
                .list();
        List<DataCombo> comboList = adtypeList.stream()
                .map(item -> new DataCombo(item.getId().toString(), item.getSname()))
                .collect(Collectors.toList());
        return comboList;
    }
}
