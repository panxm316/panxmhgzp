package com.hgzp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 方法功能: DTO实体对象加入redis缓存用于日志比较
 * @author peij
 * @date 2023/9/13 14:46
 * @param null
 * @return
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LogModelAddCache {
}
