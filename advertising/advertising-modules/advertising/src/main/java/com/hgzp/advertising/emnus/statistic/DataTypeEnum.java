package com.hgzp.advertising.emnus.statistic;

/**
 * @author suny
 * @date 2024/01/20 10:30
 * 数据类型(广告明细-orderitem，核销明细：apportiondetail)
 */
public enum DataTypeEnum {
    statistic_orderitem("orderitem", "广告明细"),
    statistic_apportiondetail("apportiondetail", "核销明细");

    public String key;
    public String name;

    DataTypeEnum(String key, String name) {
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
        for (DataTypeEnum value : DataTypeEnum.values()) {
            if (value.key.equals(key)) {
                return value.name;
            }
        }
        return null;
    }

    public static DataTypeEnum getTypeByKey(String key) {
        for (DataTypeEnum value : DataTypeEnum.values()) {
            if (value.key.equals(key)) {
                return value;
            }
        }
        return null;
    }
}
