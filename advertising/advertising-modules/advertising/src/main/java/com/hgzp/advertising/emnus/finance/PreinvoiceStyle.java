package com.hgzp.advertising.emnus.finance;

import com.hgzp.core.emnus.ConditionTypes;

/**
 * PreinvoiceStyle
 * 描述：开票样式
 * @author songly
 * @date 2023/10/28 11:05
 */
public enum PreinvoiceStyle {
    //开票样式： 2-普通发票 0-专用发票 51-电子发票 81-数电专票 82-数电普票
    PreinvoiceStyle_1(2, "普通发票"),
    PreinvoiceStyle_2(0, "专用发票"),
    PreinvoiceStyle_3(51, "电子发票"),
    PreinvoiceStyle_4(81, "数电专票"),
    PreinvoiceStyle_5(82, "数电普票");

    public Integer key;
    public String name;

    PreinvoiceStyle(Integer key, String name) {
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
        for (PreinvoiceStyle value : PreinvoiceStyle.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static PreinvoiceStyle getTypeByKey(Integer key) {
        for (PreinvoiceStyle value : PreinvoiceStyle.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
