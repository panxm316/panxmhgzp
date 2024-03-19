package com.hgzp.advertising.service.system.impl;

import com.hgzp.advertising.service.system.ProduceServiceI;
import com.hgzp.mapper.system.ProcedureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * ProduceServiceImpl
 * 创建人：wangwk
 * 类描述：存储过程service
 * 创建日期：2023/10/27 11:06
 */
@Service
public class ProduceServiceImpl implements ProduceServiceI {

    @Autowired
    ProcedureMapper procedureMapper;

    /**
     * 获取序列号
     * 方法功能: 获取合同号
     * @author wangwk
     * @date 2023/10/27 11:13
     * @param
     * @return java.lang.String
     */
    @Override
    public synchronized String getSerNo() {
        Map<String, String> map = new HashMap<>();
        procedureMapper.getSerNo(map);
        return map.get("serNo");
    }

    /**
     * 获取发票序列号
     * 方法功能: 获取发票序列号
     * @param
     * @return java.lang.String
     * @date 2023/10/27 11:13
     * @throws
     */
    @Override
    public synchronized String getFpNo() {
        Map<String, String> map = new HashMap<>();
        procedureMapper.getFpNo(map);
        return map.get("serNo");
    }

    /**
     * 获取项目序列号
     * 方法功能: 获取项目序列号
     * @param
     * @return java.lang.String
     * @date 2023/10/27 11:13
     * @throws
     */
    @Override
    public synchronized String getXmNo() {
        Map<String, String> map = new HashMap<>();
        procedureMapper.getXmNo(map);
        return map.get("serNo");
    }

    /**
     * 获取订单序列号
     * 方法功能: 获取订单序列号
     * @param
     * @return java.lang.String
     * @date 2023/10/27 11:13
     * @throws
     */
    @Override
    public synchronized String getOrderNo() {
        Map<String, String> map = new HashMap<>();
        procedureMapper.getOrderNo(map);
        return map.get("serNo");
    }



}
