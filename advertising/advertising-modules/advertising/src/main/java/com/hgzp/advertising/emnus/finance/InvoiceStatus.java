package com.hgzp.advertising.emnus.finance;

import com.hgzp.core.emnus.ConditionTypes;

/**
 * InvoiceStatus
 * 创建人：songly
 * 描述：发票状态枚举
 * @date 2023/10/28 10:55
 */
public enum InvoiceStatus {
    //发票状态：0-无效、1-有效、2-冲红退回
    INVALID(0, "无效"),
    VALID(1, "有效"),
    IBACK(2, "冲红退回");

    private Integer key;
    private String name;

    InvoiceStatus(Integer key, String name) {
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
        for (InvoiceStatus value : InvoiceStatus.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static InvoiceStatus getTypeByKey(Integer key) {
        for (InvoiceStatus value : InvoiceStatus.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
