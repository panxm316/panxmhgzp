package com.hgzp.advertising.pagemodel.statistic.vo;

import lombok.Data;

/**
 * AdIncomeYearItem
 * 创建人：lhl
 * 类描述：广告实收明细表（年度业务）明细数据
 * 创建日期：2024/1/26 9:01
 */
@Data
public class AdIncomeYearItem {
    /**
     * 见报年份
     */
    private String year;
    /**
     * 汇总公司
     */
    private String companyName;

    /**
     * 版面广告本月汇总金额
     */
    private String monthBMTotal;
    /**
     * 版面广告年汇总金额
     */
    private String yearBMTotal;
    /**
     * 南方+本月汇总金额
     */
    private String monthNFTotal;
    /**
     * 南方+年汇总金额
     */
    private String yearNFTotal;
    /**
     * 网站本月汇总金额
     */
    private String monthWZTotal;
    /**
     * 网站年汇总金额
     */
    private String yearWZTotal;
    /**
     * 双微本月汇总金额
     */
    private String monthSWTotal;
    /**
     * 双微年汇总金额
     */
    private String yearSWTotal;
    /**
     * 活动本月汇总金额
     */
    private String monthHDTotal;
    /**
     * 活动年汇总金额
     */
    private String yearHDTotal;
    /**
     * 策划制作本月汇总金额
     */
    private String monthCHTotal;
    /**
     * 策划年汇总金额
     */
    private String yearCHTotal;
    /**
     * 本月合计
     */
    private String monthTotal;
    /**
     * 本年合计
     */
    private String yearTotal;
}


