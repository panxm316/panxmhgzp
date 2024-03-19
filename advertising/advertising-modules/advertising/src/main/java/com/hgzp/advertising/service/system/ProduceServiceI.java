package com.hgzp.advertising.service.system;

/**
 * ProduceServiceI
 * 创建人：wangwk
 * 类描述：存储过程service
 * 创建日期：2023/10/27 11:07
 */
public interface ProduceServiceI {

    /**
     * 获取序列号
     * 方法功能: 获取序列号
     * @author wangwk
     * @date 2023/10/27 11:13
     * @param
     * @return java.lang.String
     */
    String getSerNo();

    /**
     * 获取发票序列号
     * 方法功能: 获取发票序列号
     * @param
     * @return java.lang.String
     * @date 2023/10/27 11:13
     * @throws
     */
    String getFpNo();

    /**
     * 获取项目序列号
     * 方法功能: 获取项目序列号
     * @param
     * @return java.lang.String
     * @date 2023/10/27 11:13
     * @throws
     */
    String getXmNo();

    String getOrderNo();
}
