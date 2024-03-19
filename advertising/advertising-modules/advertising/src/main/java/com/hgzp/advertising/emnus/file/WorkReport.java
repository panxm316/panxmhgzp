package com.hgzp.advertising.emnus.file;

import com.hgzp.core.emnus.ConditionTypes;

/**
 WorkReport
 创建人：songly
 类描述：工作报告类型
 创建日期：2023/10/28 11:20
 */
public enum WorkReport {
    //工作报告类型：0-日报、1-周报、2-月报、3-年报、4-客户服务
    WorkReport_1(0, "日报"),
    WorkReport_2(1, "周报"),
    WorkReport_3(2, "月报"),
    WorkReport_4(3, "年报"),
    WorkReport_5(4, "客户服务");

    public Integer key;
    public String name;

    WorkReport(Integer key, String name) {
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
        for (WorkReport value : WorkReport.values()) {
            if(value.key.equals(key)){
                return value.name;
            }
        }
        return null;
    }

    public static WorkReport getTypeByKey(Integer key) {
        for (WorkReport value : WorkReport.values()) {
            if(value.key.equals(key)){
                return value;
            }
        }
        return null;
    }
}