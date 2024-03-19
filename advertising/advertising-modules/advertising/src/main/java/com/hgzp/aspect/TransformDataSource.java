package com.hgzp.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.sql.DataSource;
import java.util.Map;


@Aspect // FOR AOP
@Configuration // 配置类
public class TransformDataSource {

    @Pointcut("execution( * com.hgzp.advertising.controller..*.*(..))")
    /**
     * 这个方法的方法名要和下面注解方法名一致
     */
    public void doPointcut() {
    }

//    @Autowired
//    DynamicDataSourceProvider dynamicDataSourceProvider;

    @Before("doPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 请求开始时间
//        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        String headValue = sra.getRequest().getHeader("clientId");
//        System.out.println("--------------clientId:"+ headValue);
//        DynamicDataSourceContextHolder.poll();
//        DynamicDataSourceContextHolder.push(headValue);
//        Map<String, DataSource> stringDataSourceMap = dynamicDataSourceProvider.loadDataSources();
//        for (String s : stringDataSourceMap.keySet()) {
//            System.out.println(s);
//        }

    }

    @After("doPointcut()")
    public void doAfter() {
        System.out.println("==doAfter==");
    }

}