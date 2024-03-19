package com.hgzp.mapper.system;

import java.util.Map;

/**
 * ProcedureMapper
 * 创建人：wangwk
 * 类描述： 存储过程mapper
 * 创建日期：2023/10/27 9:29
 */
public interface ProcedureMapper {


    /**
     * 获取合同序列号
     * @return
     */
    public String getSerNo(Map<String, String> map);


    /**
     * 获取发票序列号
     * @return
     */
    public String getFpNo(Map<String, String> map);

    /**
     * 获取项目序列号
     * @return
     */
    public String getXmNo(Map<String, String> map);

    /**
     * 获取订单序列号
     * @return
     */
    public String getOrderNo(Map<String, String> map);

}
