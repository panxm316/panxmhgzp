package com.hgzp.advertising.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.advertising.service.system.TwparameterServiceI;
import com.hgzp.core.model.Twparameter;
import com.hgzp.mapper.system.TwparameterMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统参数表 服务实现类
 * </p>
 *
 * @author wwk
 * @since 2023-11-15
 */
@Service
public class TwparameterServiceImpl extends ServiceImpl<TwparameterMapper, Twparameter> implements TwparameterServiceI {
    @Override
    public String getParameterByKey(String key) {
        LambdaQueryWrapper<Twparameter> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Twparameter::getSkey, key).eq(Twparameter::getBuse, true);
        Twparameter parameter = this.baseMapper.selectOne(wrapper);
        return parameter == null ? "" : parameter.getSvalue();
    }
}
