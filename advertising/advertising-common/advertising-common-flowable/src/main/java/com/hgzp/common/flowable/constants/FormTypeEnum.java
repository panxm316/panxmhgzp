package com.hgzp.common.flowable.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

/**
 * 表单类型枚举
 */
@Getter
@AllArgsConstructor
public enum FormTypeEnum {

    INPUT("Input","单行文本",""),
    TEXTAREA("Textarea","多行文本",""),
    NUMBER("Number","数字",null),
    DATE("Date","日期",null),
    DATE_TIME("DateTime","日期时间",null),
    LAYOUT("Layout","明细",null),
    TIME("Time","时间",null),
    MONEY("Money","金额",null),
    SINGLE_SELECT("SingleSelect","单选",new ArrayList<>()),
    SELECT_DEPT("SelectDept","部门",new ArrayList<>()),
    SELECT_USER("SelectUser","用户",new ArrayList<>()),
    SELECT_MULTI_DEPT("SelectMultiDept","多部门",new ArrayList<>()),
    SELECT_MULTI_USER("SelectMultiUser","多用户",new ArrayList<>()),

    /**
     * 设置条件时，多选下拉框
     */
    MULTI_SELECT("MultiSelect", "多选下拉框", new ArrayList<>()),

    /**
     * 设置条件时，输入框可以输入多个值，用逗号分隔
     */
    MULTI_INPUT("MultiInput", "多个输入", new ArrayList<>()),

    /**
     * 自由组合条件，条件件的关系为“且”，条件之间是同一条数据的不同字段
     */
    CONDITION_RELATION("ConditionRelation", "关联的条件组合", new ArrayList<>())
    ;





    private String type;

    private String name;
    private Object defaultValue;

    public static String getNameByType(String type){
        for (FormTypeEnum value : FormTypeEnum.values()) {
            if(value.type.equals(type)){
                return value.name;
            }
        }
        return null;
    }


}
