package com.hgzp.advertising.service.system.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.service.system.TbresourcetypeServiceI;
import com.hgzp.core.model.Tbresourcetype;
import com.hgzp.mapper.system.TbresourcetypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

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
    public Tbresourcetype getResourcetypeByFormat(String format) {
        // 20210102  suny 修改  获取多条中的第一条
        Tbresourcetype tbresourcetype = getOne(Wrappers.<Tbresourcetype>lambdaQuery()
                .like(Tbresourcetype::getSfileformat, format.toLowerCase()), false);
        return tbresourcetype;
    }
}
