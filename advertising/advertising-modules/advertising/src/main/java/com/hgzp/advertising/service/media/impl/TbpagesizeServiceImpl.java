package com.hgzp.advertising.service.media.impl;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hgzp.advertising.service.media.TbpagesizeServiceI;
import com.hgzp.core.model.Tbpagesize;
import com.hgzp.mapper.media.TbpagesizeMapper;
import com.hgzp.service.common.impl.MyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 TbpagesizeServiceImpl
 创建人：songly
 类描述：TODO
 创建日期：2023/11/25 10:12
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class TbpagesizeServiceImpl extends MyServiceImpl<TbpagesizeMapper, Tbpagesize> implements TbpagesizeServiceI {
    @Resource
    private TbpagesizeMapper tbpagesizeMapper;
    @Override
    public List<Tbpagesize> getPageSizeList() {
        LambdaQueryWrapper<Tbpagesize> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tbpagesize::getBuse, true)
                .orderByAsc(Tbpagesize::getIsort);
        List<Tbpagesize> lstbpagesize = tbpagesizeMapper.selectList(queryWrapper);
        return lstbpagesize;
    }
}