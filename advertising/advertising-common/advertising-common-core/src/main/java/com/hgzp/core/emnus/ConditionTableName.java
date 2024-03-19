package com.hgzp.core.emnus;

/**
 ConditionTableName
 创建人：songly
 类描述：条件类型表名枚举(对应ConditionTypes中的key值)
 创建日期：2023/10/20 10:37
 */
public enum ConditionTableName {
    ConditionTable_Media("Media", "媒体"),
    ConditionTable_ADType("AdType", "广告类型"),
    ConditionTable_BusinessEntity("Businessentity", "经营主体");


    public String key;
    public String name;

    ConditionTableName(String key, String name) {
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
        for (ConditionTableName value : ConditionTableName.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static ConditionTableName getTypeByKey(String key) {
        for (ConditionTableName value : ConditionTableName.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
