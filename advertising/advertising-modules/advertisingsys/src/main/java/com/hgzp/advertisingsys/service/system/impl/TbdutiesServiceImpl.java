package com.hgzp.advertisingsys.service.system.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.system.vo.DutiesVO;
import com.hgzp.advertisingsys.service.system.TbdutiesServiceI;
import com.hgzp.core.model.Tbduties;
import com.hgzp.core.page.DataCombo;
import com.hgzp.mapper.system.TbdutiesMapper;
import com.hgzp.service.system.impl.BaseTbdutiesServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 人员职务表 服务实现类
 * </p>
 *
 * @author yanz
 * @since 2023-08-28
 */
@Service
public class TbdutiesServiceImpl extends BaseTbdutiesServiceImpl implements TbdutiesServiceI {

    @Resource
    private TbdutiesMapper mapper;

    /**
     * getDutyPageList
     * 方法功能:分页：获取人员职务
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbduties>
     * @author yanz
     * @date 2023/8/31 15:35
     */
    @Override
    public IPage<Tbduties> getDutyPageList(Page<Tbduties> page, DutiesVO vo) {
        LambdaQueryWrapper<Tbduties> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(vo.getQueryKey()), Tbduties::getSname, vo.getQueryKey())
                .eq(ObjectUtil.isNotNull(vo.getBuse()), Tbduties::getBuse, vo.getBuse());
        return mapper.selectPage(page, lqw);
    }

    /**
     * deleteDutyByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/28 10:34
     */
    @Override
    public boolean deleteDutyByIds(List<String> idList) {
        mapper.deleteBatchIds(idList);
        return true;
    }

    /**
     * isDuplicateSname
     * 方法功能:是否存在同名对象
     *
     * @param id
     * @param sname
     * @return boolean
     * @author yanz
     * @date 2023/8/28 10:33
     */
    @Override
    public boolean isDuplicateSname(Long id, String sname) {
        LambdaQueryWrapper<Tbduties> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(id != null, Tbduties::getId, id);
        wrapper.eq(StringUtils.hasText(sname), Tbduties::getSname, sname);
        Long count=mapper.selectCount(wrapper);
        return count>0;
    }


    /**
     * getDutiesCombo
     * 方法功能:获取人员职务下拉列表（PS: Tbduties 没有 default 属性，按 iSort 从小到大）
     *
     * @param
     * @return java.util.List<com.hgzp.core.page.DataCombo>
     * @author yanz
     * @date 2023/9/1 11:25
     */
    @Override
    public List<DataCombo> getDutiesCombo() throws Exception {
        LambdaQueryWrapper<Tbduties> lqw = Wrappers.lambdaQuery();
        lqw.ge(Tbduties::getBuse, true);
        lqw.orderByAsc(Tbduties::getIsort);
        List<Tbduties> list = mapper.selectList(lqw);
        List<DataCombo> combo = new ArrayList<>();
        // Tbduties 没有 default 属性，按iSort排
        Collections.sort(list, Comparator.comparingInt(Tbduties::getIsort));
        if (list != null && list.size() > 0) {
            for (Tbduties type : list) {
                DataCombo d = new DataCombo();
                d.setId(type.getId().toString());
                d.setName(type.getSname());
                if (type == list.get(0)) {
                    d.setBdefault(true);
                }
                combo.add(d);
            }
        }
        return combo;
    }
}
