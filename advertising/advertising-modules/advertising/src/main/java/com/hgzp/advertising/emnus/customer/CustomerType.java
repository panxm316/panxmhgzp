package com.hgzp.advertising.emnus.customer;

import com.hgzp.core.emnus.ConditionTypes;

/**
 CustomerType
 创建人：songly
 描述：客户类型枚举
 创建日期：2023/10/28 10:49
 */
public enum CustomerType {
    //客户类型：0-直接客户、1-代理公司、2-内容生产方
    CustomerType_ZK(0, "直接客户"),
    CustomerType_Proxy(1, "代理公司"),
    CustomerType_Producer(2, "内容生产方");

    public Integer key;
    public String name;

    CustomerType(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public Integer getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public static String getNameByKey(Integer key) {
        for (CustomerType value : CustomerType.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static CustomerType getTypeByKey(Integer key) {
        for (CustomerType value : CustomerType.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}