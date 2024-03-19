package com.hgzp.advertisingsys.service.finance.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.finance.vo.AdPrintItemVO;
import com.hgzp.advertisingsys.service.finance.TbadprintitemServiceI;
import com.hgzp.core.model.Tbadprintitem;
import com.hgzp.core.page.Json;
import com.hgzp.mapper.finance.TbadprintitemMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 开票项目 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbadprintitemServiceImpl extends MyServiceImpl<TbadprintitemMapper, Tbadprintitem> implements TbadprintitemServiceI {
    @Resource
    private TbadprintitemMapper mapper;

    /**
     * getAdPrintItemPageList
     * 方法功能:分页：获取开票项目
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbadprintitem>
     * @author yanz
     * @date 2023/8/31 15:06
     */
    @Override
    public IPage<Tbadprintitem> getAdPrintItemPageList(Page<Tbadprintitem> page, AdPrintItemVO vo) {
        LambdaQueryWrapper<Tbadprintitem> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(vo.getQueryKey()), Tbadprintitem::getSname, vo.getQueryKey())
                .eq(ObjectUtil.isNotNull(vo.getBuse()), Tbadprintitem::getBuse, vo.getBuse());
        return mapper.selectPage(page, lqw);
    }

    /**
     * deleteAdPrintItemByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:31
     */
    @Override
    public boolean deleteAdPrintItemByIds(List<String> idList) {
        mapper.deleteBatchIds(idList);
        return true;
    }

    @Override
    public Json doDefaultLogic(Tbadprintitem tbadprintitem) {
        if (tbadprintitem.getBdefault()) {
            List<Tbadprintitem> list = mapper.selectList(null);
            for (Tbadprintitem t : list) {
                t.setBdefault(false);
                mapper.updateById(t);
            }
        } else {
            Long count = new LambdaQueryChainWrapper<>(mapper)
                    .ne(tbadprintitem.getId() != null, Tbadprintitem::getId, tbadprintitem.getId())
                    .eq(Tbadprintitem::getBdefault, true)
                    .count();
            if (count == 0||count>1) {
                return Json.fail("至少要有一条记录默认选中！");
            }
            if (count>1) {
                return Json.fail("只能有一条记录默认选中！");
            }
        }
        return Json.success();
    }

    @Override
    public boolean isDuplicateSname(Tbadprintitem tbadprintitem) {
        Long count = new LambdaQueryChainWrapper<>(mapper)
                .ne(tbadprintitem.getId() != null, Tbadprintitem::getId, tbadprintitem.getId())
                .eq(Tbadprintitem::getSname, tbadprintitem.getSname())
                .count();
        return count > 0;
    }
}
