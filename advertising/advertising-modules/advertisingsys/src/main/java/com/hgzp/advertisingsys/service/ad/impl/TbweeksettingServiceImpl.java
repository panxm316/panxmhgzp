package com.hgzp.advertisingsys.service.ad.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hgzp.advertisingsys.pagemodel.ad.vo.WeekSettingVO;
import com.hgzp.advertisingsys.service.ad.TbweeksettingServiceI;
import com.hgzp.core.model.Tbweeksetting;
import com.hgzp.mapper.ad.TbweeksettingMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 星期设置 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-08-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TbweeksettingServiceImpl extends MyServiceImpl<TbweeksettingMapper, Tbweeksetting> implements TbweeksettingServiceI {
    @Resource
    private TbweeksettingMapper mapper;

    /**
     * getWeekSettingPageList
     * 方法功能:分页：获取星期设置
     *
     * @param page
     * @param vo
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.hgzp.core.model.Tbweeksetting>
     * @author yanz
     * @date 2023/8/31 15:10
     */
    @Override
    public IPage<Tbweeksetting> getWeekSettingPageList(Page<Tbweeksetting> page, WeekSettingVO vo) {
        LambdaQueryWrapper<Tbweeksetting> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.hasText(vo.getQueryKey()), Tbweeksetting::getSname, vo.getQueryKey())
                .eq(ObjectUtil.isNotNull(vo.getBuse()), Tbweeksetting::getBuse, vo.getBuse());
        return mapper.selectPage(page, lqw);
    }

    /**
     * deleteWeekSettingByIds
     * 方法功能:据id批量删除
     *
     * @param idList
     * @return boolean
     * @author yanz
     * @date 2023/8/25 10:24
     */
    @Override
    public boolean deleteWeekSettingByIds(List<String> idList) {
        mapper.deleteBatchIds(idList);
        return true;
    }

    @Override
    public boolean isDuplicateSname(Long id, String sname) {
        LambdaQueryWrapper<Tbweeksetting> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(id != null, Tbweeksetting::getId, id);
        wrapper.eq(StringUtils.hasText(sname), Tbweeksetting::getSname, sname);
        Long count = mapper.selectCount(wrapper);
        return count > 0;
    }
}
