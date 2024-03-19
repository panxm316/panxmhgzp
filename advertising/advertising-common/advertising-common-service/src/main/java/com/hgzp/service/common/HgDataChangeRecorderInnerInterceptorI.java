package com.hgzp.service.common;

/**
 * HgDataChangeRecorderInnerInterceptorI
 * 创建人：wangwk
 * 类描述：自定义 数据变化拦截器 接口
 * 创建日期：2023/9/14 16:50
 */
public interface HgDataChangeRecorderInnerInterceptorI {

    /**
     * 记录日志
     */
    public void recoredLog();

    default void recoredLog(Long slaveId){};

}
