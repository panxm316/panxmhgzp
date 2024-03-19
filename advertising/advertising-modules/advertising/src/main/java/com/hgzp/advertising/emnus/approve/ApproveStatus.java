package com.hgzp.advertising.emnus.approve;

/**
 * ApproveStatus
 * 创建人：songly
 * 描述：审批状态
 * 创建日期：2023/10/18 11:07
 */
public enum ApproveStatus {
    //审批状态： (0-待审、1-在审、2-通过、3-否决、4-撤销、5-无效）
    APPROVE_EDIT(0, "待审"),
    APPROVE_EDITING(1, "在审"),
    APPROVE_PASS(2, "通过"),
    APPROVE_REJECT(3, "否决"),
    APPROVE_REVOKE(4, "撤销"),
    APPROVE_INVALID(5, "无效");

    public Integer key;
    public String name;

    ApproveStatus(Integer key, String name) {
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
        for (ApproveStatus value : ApproveStatus.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static ApproveStatus getTypeByKey(Integer key) {
        for (ApproveStatus value : ApproveStatus.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
