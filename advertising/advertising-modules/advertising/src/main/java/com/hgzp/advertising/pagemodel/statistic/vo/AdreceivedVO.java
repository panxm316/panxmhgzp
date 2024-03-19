package com.hgzp.advertising.pagemodel.statistic.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * AdreceivedVO
 * 创建人：lhl
 * 类描述：广告实收明细表数据
 * 创建日期：2024/1/16 10:24
 */
@Data
public class AdreceivedVO {
    /**
     * 报表标题
     */
    private String title;
    /**
     * 编制单位
     */
    private String company;
    /**
     * 报表日期
     */
    private String reportDate;
    /**
     * 今年报表日期
     */
    private String thisYearReportDate;
    /**
     * 年度
     */
    private String thisYear;
    /**
     * 去年报表日期
     */
    private String lastYearReportDate;
    /**
     * 汇总项
     */
    List<AdreceivedItem>  itemList;
}


