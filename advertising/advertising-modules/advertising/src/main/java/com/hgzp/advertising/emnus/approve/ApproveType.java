package com.hgzp.advertising.emnus.approve;

/**
 * @author new wei
 * @date 2023/11/30 15:45
 * 审批类型：（0-预约、1-加刊、2-改刊、3-停刊、4-折扣）
 */
public enum ApproveType {
    //审批类型：（0-预约、1-加刊、2-改刊、3-停刊、4-折扣）
    APPROVE_Pre(0, "预约"),
    APPROVE_Add(1, "加刊"),
    APPROVE_Change(2, "改刊"),
    APPROVE_Stop(3, "停刊"),
    APPROVE_Discount(4, "折扣"),
    APPROVE_Order(5, "订单");

    public Integer key;
    public String name;

    ApproveType(Integer key, String name) {
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
        for (ApproveType value : ApproveType.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static ApproveType getTypeByKey(Integer key) {
        for (ApproveType value : ApproveType.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
