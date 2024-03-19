package com.hgzp.advertising.emnus.customer;

/**
 EcustomerChargeType
 创建人：suny
 描述：入账类型枚举
 创建日期：2023/12/20 14:10
 */
public enum EcustomerChargeType {
    //入账类型：0-预收款 1-直接收款 2-银行流水
    EntryType_AdPay(0, "预收款"),
    EntryType_DirPay(1, "直接收款"),
    EntryType_BankPay(2, "银行流水");

    public Integer key;
    public String name;

    EcustomerChargeType(Integer key, String name) {
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
        for (EcustomerChargeType value : EcustomerChargeType.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static EcustomerChargeType getTypeByKey(Integer key) {
        for (EcustomerChargeType value : EcustomerChargeType.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
