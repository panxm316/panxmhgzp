package com.hgzp.configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyBatisPlusConfig implements MetaObjectHandler {
    /**
     * 配置mybatis plus 拦截器
     * @return
     */
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor(){
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        //添加分页插件
//        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
//        innerInterceptor.setMaxLimit(50L);
//        interceptor.addInnerInterceptor(innerInterceptor);
//        return interceptor;
//    }

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        Date date = new Date();
        this.strictInsertFill(metaObject, "createTime", Date.class, date); // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "updateTime", Date.class, date); // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "delFlag", Boolean.class, false); // 起始版本 3.3.0(推荐使用)

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        Date date = new Date();

        this.strictUpdateFill(metaObject, "updateTime", Date.class,date); // 起始版本 3.3.0(推荐)
    }
}
