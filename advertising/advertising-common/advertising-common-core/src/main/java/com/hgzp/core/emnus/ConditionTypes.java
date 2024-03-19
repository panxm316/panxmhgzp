package com.hgzp.core.emnus;
/**
 ConditionTypes
 创建人：songly
 类描述：条件类型枚举
 创建日期：2023/10/20 10:37
 */
public enum ConditionTypes {
    Condition_Money("Number", "数字"),
    Condition_Dept("SelectDept", "部门"),
    Condition_User("SelectUser", "用户"),
    Condition_SELECT("MultiSelect", "多选择"),
    Condition_INPUT("MultiInput", "多输入"),
    Condition_RELATION("ConditionRelation", "组条件");

    public String key;
    public String name;

    ConditionTypes(String key, String name) {
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
        for (ConditionTypes value : ConditionTypes.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static ConditionTypes getTypeByKey(String key) {
        for (ConditionTypes value : ConditionTypes.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}