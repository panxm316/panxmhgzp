package com.hgzp.advertising.emnus.contract;

/**
 * SalesContractType
 * 创建人：songly
 * 描述：销售合同类型
 * 创建日期：2024/3/15 17:45
 */
public enum SalesContractType {
    //销售合同类型： (0-常规合同、1-认刊书）
    SALESCONTRACT_BNORMAL(0, "常规合同"),
    SALESCONTRACT_BOOK(1, "认刊书");

    public Integer key;
    public String name;

    SalesContractType(Integer key, String name) {
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
        for (SalesContractType value : SalesContractType.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static SalesContractType getTypeByKey(Integer key) {
        for (SalesContractType value : SalesContractType.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
