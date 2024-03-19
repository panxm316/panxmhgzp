package com.hgzp.core.emnus;

/**
 * FlowTypes
 * 创建人：wangwk
 * 类描述：流程类型（流程组）
 * 创建日期：2023/10/12 11:07
 */
public enum FlowTypes {
    FLOW_CUSTOMER("flowcustomer", "客户挂牌审批流程"),
    FLOW_ADBOOKING("flowadbooking", "口头预定审批流程"),
    FLOW_ADSOURCE("flowadsource", "资源审批流程"),
    FLOW_CONTRACT("flowcontract", "电子认刊书审批流程"),
    FLOW_ORDER("floworder", "订单审批流程"),
    FLOW_PREINVOICE("flowpreinvoice", "预开发票审批流程"),
    FLOW_PROJECT("flowadproject", "项目审批流程"),
    FLOW_ORDERCHANGE("floworderchange", "改加停刊审批流程");

    public String key;
    public String name;

    FlowTypes(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public static String getNameByKey(String key) {
        for (FlowTypes value : FlowTypes.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static FlowTypes getTypeByKey(String key) {
        for (FlowTypes value : FlowTypes.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }

}
