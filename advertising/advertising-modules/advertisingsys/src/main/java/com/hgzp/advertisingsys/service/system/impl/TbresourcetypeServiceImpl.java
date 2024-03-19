package com.hgzp.advertisingsys.service.system.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertisingsys.service.system.TbresourcetypeServiceI;
import com.hgzp.core.model.Tbresourcetype;
import com.hgzp.core.page.BaseQueryInfo;
import com.hgzp.mapper.system.TbresourcetypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资源文件类型 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-10-27
 */
@Service
public class TbresourcetypeServiceImpl extends ServiceImpl<TbresourcetypeMapper, Tbresourcetype> implements TbresourcetypeServiceI {

    @Autowired
    private TbresourcetypeMapper tbresourcetypeMapper;

    @Override
    public IPage<Tbresourcetype> getResourcetypePageList(Page<Tbresourcetype> page, BaseQueryInfo query) {
        LambdaQueryWrapper<Tbresourcetype> lqw = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(query.getQueryKey())) {
            lqw.like(Tbresourcetype::getSname, query.getQueryKey())
                    .or()
                    .like(Tbresourcetype::getSfileformat, query.getQueryKey());
        }
        return tbresourcetypeMapper.selectPage(page, lqw);
    }
    @Override
    public Tbresourcetype getResourcetypeByFormat(String format) {
        // 20210102  suny 修改  获取多条中的第一条
        Tbresourcetype tbresourcetype = getOne(Wrappers.<Tbresourcetype>lambdaQuery()
                .like(Tbresourcetype::getSfileformat, format.toLowerCase()), false);
        return tbresourcetype;
    }
}
