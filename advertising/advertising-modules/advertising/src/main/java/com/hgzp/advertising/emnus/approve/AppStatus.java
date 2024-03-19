package com.hgzp.advertising.emnus.approve;

/**
 * AppStatus
 * 创建人：suny
 * 描述：提交审核状态
 * 创建日期：2023/12/14 09:07
 */
public enum AppStatus {
    //状态 0-待提交 1-已提交 2-已确认 3-已退回
    APPRSTATUS_EDIT(0, "待提交"),
    APPRSTATUS_EDITING(1, "已提交"),
    APPRSTATUS_PASS(2, "已确认"),
    APPRSTATUS_REJECT(3, "已退回"),
    APPRSTATUS_VERIFIED(4, "已核销");


    public Integer key;
    public String name;

    AppStatus(Integer key, String name) {
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
        for (AppStatus value : AppStatus.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static AppStatus getTypeByKey(Integer key) {
        for (AppStatus value : AppStatus.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
