package com.hgzp.advertising.emnus.business;

/**
 * CommissionStatus
 * 创建人：yanz
 * 类描述：佣金计提标记
 * 创建日期：2024/1/11 9:40
 */
public enum CommissionStatus {
    //标记： (0-计算、1-确认、2-发放、3-撤销、4-待提交）
    COMMISSION_STATUS_CALCULATED(0, "计算"),
    COMMISSION_STATUS_CONFIRMED(1, "确认"),
    COMMISSION_STATUS_ISSUED(2, "发放"),
    COMMISSION_STATUS_REVOKED(3, "撤销"),
    COMMISSION_STATUS_EDIT(4, "待提交");


    public Integer key;
    public String name;

    CommissionStatus(Integer key, String name) {
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
        for (CommissionStatus value : CommissionStatus.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static CommissionStatus getTypeByKey(Integer key) {
        for (CommissionStatus value : CommissionStatus.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
