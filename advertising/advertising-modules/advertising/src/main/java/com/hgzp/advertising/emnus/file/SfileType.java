package com.hgzp.advertising.emnus.file;

import com.hgzp.core.emnus.ConditionTypes;

/**
 * FileType
 * @author songly
 * @date 2023/10/28 11:12
 */
public enum SfileType {
    //文件类别：0-电子认刊书、 1-客户合同\协议、 2-资质 、3-名片、 4-出版流程单 、5-银行流水、6-报告、7-通知指示、8-广告资源、9-成本证明
    FileType_1(0, "电子认刊书"),
    FileType_2(1, "客户合同\\协议"),
    FileType_3(2, "资质"),
    FileType_4(3, "名片"),
    FileType_5(4, "出版流程单"),
    FileType_6(5, "银行流水"),
    FileType_7(6, "报告"),
    FileType_8(7, "通知指示"),
    FileType_9(8, "广告资源"),
    FileType_10(9, "成本证明/快速预约证明");

    public Integer key;
    public String name;

    SfileType(Integer key, String name) {
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
        for (SfileType value : SfileType.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static SfileType getTypeByKey(Integer key) {
        for (SfileType value : SfileType.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}
