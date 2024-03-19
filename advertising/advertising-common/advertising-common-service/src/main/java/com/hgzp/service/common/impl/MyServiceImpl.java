package com.hgzp.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgzp.service.common.IMyService;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * MyService
 * 创建人：peij
 * 类描述：通用service
 * 创建日期：2023/8/18 11:25
 */
// @Service
public class MyServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IMyService<T> {
    @Override
    public Integer getMaxSort() {
        Integer maxSort = 0;
        try {
            Type actualTypeArgument = null;
            if(this.getClass().getGenericSuperclass() instanceof ParameterizedType){
                actualTypeArgument = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            }else{
                actualTypeArgument = ((ParameterizedType) this.getClass().getSuperclass().getGenericSuperclass()).getActualTypeArguments()[1];
            }

            Class entityClass = (Class) actualTypeArgument;
            Field isort = entityClass.getDeclaredField("isort");
            if (null != isort) {
                QueryWrapper<T> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("max(isort) as maxSort");
                List<Map<String, Object>> mapList = baseMapper.selectMaps(queryWrapper);
                Optional<Map<String, Object>> opMap = mapList.stream().filter(m -> null != m).findFirst();
                maxSort = opMap.isPresent() ? (Integer.parseInt(opMap.get().get("maxSort").toString()) + 1) : 1;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return maxSort;
    }
}
