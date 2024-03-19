package com.hgzp.configuration;

import cn.hutool.core.lang.ClassScanner;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.hgzp.advertising.interceptor.HgDataChangeRecorderInnerInterceptor;
import com.hgzp.core.annotation.LogData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: mybatisplus配置
 * @author: wangwk
 * @create：2023/8/3 13:57
 */
@Configuration
public class MybatisConfig {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        interceptor.addInnerInterceptor(getHgDataChangeRecorderInnerInterceptor());
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        return interceptor;
    }

    @Bean
    public HgDataChangeRecorderInnerInterceptor getHgDataChangeRecorderInnerInterceptor(){
        HgDataChangeRecorderInnerInterceptor innerInterceptor = new HgDataChangeRecorderInnerInterceptor();
        innerInterceptor.setProperties(excludeTableColumns());
        return innerInterceptor;
    }

    public Properties excludeTableColumns() {
        ClassScanner scnnser = new ClassScanner("com.hgzp.core.model");
        Set<Class<?>> scan = scnnser.scan();
        List<String> ignoredColumnList = new ArrayList<>();
        for (Class<?> tableClass : scan) {
            String columnBuilder = "*";
            Field[] fields = ReflectUtil.getFields(tableClass);
            List<Field> ignoreFieldList = Arrays.stream(fields)
                    .filter(f -> f.getAnnotation(LogData.class) == null)
                    .collect(Collectors.toList());
            if(ignoreFieldList.size() != fields.length){
                columnBuilder = ignoreFieldList.stream()
                        .map(Field::getName)
                        .collect(Collectors.joining(","));
            }
            ignoredColumnList.add(tableClass.getSimpleName()+"."+columnBuilder);
        }

        String ignoredTableColumns = String.join(";", ignoredColumnList);
        System.out.println("*******************" + ignoredTableColumns);

        Properties properties = new Properties();
        properties.setProperty("ignoredTableColumns", ignoredTableColumns);
        return properties;
    }


}
