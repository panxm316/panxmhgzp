package com.hgzp.advertising.emnus.contract;

/**
 * SalesContractType
 * 创建人：songly
 * 描述：用章类型
 * 创建日期：2024/3/15 18:09
 */
public enum StampType {
    //用章类型： (0-公章、1-合同专用章、2-经营合同专用章、3-法人章）
    STAMP_OFFICIAL(0, "公章"),
    STAMP_CONTRACTUAL(1, "合同专用章"),
    STAMP_SPECIAL(2, "经营合同专用章"),
    STAMP_CORPORATE(3, "法人章");

    public Integer key;
    public String name;

    StampType(Integer key, String name) {
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
        for (StampType value : StampType.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static StampType getTypeByKey(Integer key) {
        for (StampType value : StampType.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
