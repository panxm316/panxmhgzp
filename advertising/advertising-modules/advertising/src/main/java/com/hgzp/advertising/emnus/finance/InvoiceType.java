package com.hgzp.advertising.emnus.finance;

import com.hgzp.core.emnus.ConditionTypes;

/**
 * InvoiceType
 * 描述：发票类型
 * @author songly
 * @date 2023/10/28 11:00
 */
public enum InvoiceType {
    //发票类型：0-手开，1-预开 2-挂开 3-冲红 4-预收费 5-预开完成
    Invoicetype_1(0, "手开"),
    Invoicetype_2(1, "预开"),
    Invoicetype_3(2, "挂开"),
    Invoicetype_4(3, "冲红"),
    Invoicetype_5(4, "预收费"),
    Invoicetype_6(5, "预开完成");

    public Integer key;
    public String name;

    InvoiceType(Integer key, String name) {
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
        for (InvoiceType value : InvoiceType.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static InvoiceType getTypeByKey(Integer key) {
        for (InvoiceType value : InvoiceType.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
