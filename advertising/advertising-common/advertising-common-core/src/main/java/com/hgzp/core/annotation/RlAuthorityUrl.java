package com.hgzp.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (C), 2014-2016, 国家复合出版系统工程         
 *    
 * 项目名称：reslib  
 * 类全名:reslib.annotation.RlAuthorityUrl    
 * 类描述：    07包权限url配置共用
 * 创建人：Peij    
 * 创建时间：2017年2月14日 上午8:54:15
 * 方法列表： 
 * 修改历史：   
 * 	 1、修改人：Peij    
 * 		修改时间：2017年2月14日 上午8:54:15    
 * 		修改备注：  
 *   2、
 *
 * @version  jdk1.7
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RlAuthorityUrl {
	String url();
}


