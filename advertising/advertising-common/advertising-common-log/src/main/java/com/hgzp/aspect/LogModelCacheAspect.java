package com.hgzp.aspect;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import com.hgzp.annotation.LogModelAddCache;
import com.hgzp.annotation.LogModelRemoveCache;
import com.hgzp.annotation.LogRecord;
import com.hgzp.core.constant.CacheConstants;
import com.hgzp.service.RedisService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * LogCacheAspect
 * 创建人：peij
 * 类描述：TODO
 * 创建日期：2023/9/13 13:28
 */
@Aspect
@Component
public class LogModelCacheAspect {
    private static final Logger log = LoggerFactory.getLogger(LogModelCacheAspect.class);

    final String cacheField = "cacheDTOKey";
    @Autowired
    RedisService redisService;

    @AfterReturning(pointcut = "@annotation(logModelCache)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, LogModelAddCache logModelCache, Object jsonResult) throws Exception {
        handleLogModelCache(joinPoint, logModelCache, null, jsonResult);
    }

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(logModelRemoveCache)")
    public void boBefore(JoinPoint joinPoint, LogModelRemoveCache logModelRemoveCache) {
        Optional<Object> methodArg = Arrays.stream(joinPoint.getArgs()).filter(arg -> {
            Class argClass = arg.getClass().getSuperclass();
            return null != ReflectUtil.getField(argClass, cacheField);
        }).findFirst();

        if (!methodArg.isPresent()) {
            throw new IllegalStateException("缓存实体对象无缓存key列");
        }
        Object cacheDTOKey = ReflectUtil.getFieldValue(methodArg.get(), cacheField);
        if(null == cacheDTOKey){
            throw new IllegalStateException("缓存实体对象key列没有值");
        }
        Object cacheObject = redisService.getCacheObject(cacheDTOKey.toString());
        if(cacheObject == null){
            throw new IllegalStateException("请重新获取数据操作");
        }
    }

    @AfterReturning(pointcut = "@annotation(logModelRemoveCache)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, LogModelRemoveCache logModelRemoveCache, Object jsonResult) throws Exception {
        handleLogModelRemoveCache(joinPoint, logModelRemoveCache, null, jsonResult);
    }

    /**
     * handleLogModelCache
     * 方法功能: 缓存key保存到DTO实体对象中，并将实体对象放到缓存中
     * @author peij
     * @date 2023/9/13 15:39
     * @param joinPoint
     * @param logModelCache
     * @param e
     * @param jsonResult
     * @return void
     */
    protected void handleLogModelCache(final JoinPoint joinPoint, LogModelAddCache logModelCache, final Exception e, Object jsonResult) throws Exception {
        Class modelClass = jsonResult.getClass().getSuperclass();
        Field cacheDTOKeyFile = ReflectUtil.getField( modelClass, cacheField);
        if (cacheDTOKeyFile == null) {
            throw new IllegalStateException("缓存实体对象无缓存key列");
        }

        String uuid = IdUtil.simpleUUID();
        ReflectUtil.setFieldValue(jsonResult, cacheField, uuid);

        redisService.setCacheObject(uuid, jsonResult, CacheConstants.EXPIRATION, TimeUnit.SECONDS);
        System.out.println(jsonResult);
    }

    /**
     * handleLogModelRemoveCache
     * 方法功能: 通过方法参数获取到DTO实体对象，并获取key删除缓存中的对象
     * @author peij
     * @date 2023/9/13 15:39
     * @param joinPoint
     * @param logModelRemoveCache
     * @param e
     * @param jsonResult
     * @return void
     */
    protected void handleLogModelRemoveCache(final JoinPoint joinPoint, LogModelRemoveCache logModelRemoveCache, final Exception e, Object jsonResult) throws Exception {
        Optional<Object> methodArg = Arrays.stream(joinPoint.getArgs()).filter(arg -> {
            Class argClass = arg.getClass().getSuperclass();
            return null != ReflectUtil.getField(argClass, cacheField);
        }).findFirst();

        Object cacheDTOKey = ReflectUtil.getFieldValue(methodArg.get(), cacheField);
        if(null == cacheDTOKey){
            throw new IllegalStateException("缓存实体对象无缓存key列");
        }
        redisService.deleteObject(cacheDTOKey.toString());
        System.out.println(cacheDTOKey);
    }
}
