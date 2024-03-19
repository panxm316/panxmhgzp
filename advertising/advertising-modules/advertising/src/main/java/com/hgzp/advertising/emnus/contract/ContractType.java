package com.hgzp.advertising.emnus.contract;


/**
 * ContractType
 * 创建人：songly
 * 描述：合同类型
 * 创建日期：2024/3/15 17:18
 */
public enum ContractType {
    //合同类型： (0-销售合同、1-采购合同、2-互换合同、3-框架合同）
    CONTRACT_SALES(0, "销售合同"),
    CONTRACT_BUY(1, "采购合同"),
    CONTRACT_EXCHANGE(2, "互换合同"),
    CONTRACT_FRAME(3, "框架合同");

    public Integer key;
    public String name;

    ContractType(Integer key, String name) {
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
        for (ContractType value : ContractType.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static ContractType getTypeByKey(Integer key) {
        for (ContractType value : ContractType.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
