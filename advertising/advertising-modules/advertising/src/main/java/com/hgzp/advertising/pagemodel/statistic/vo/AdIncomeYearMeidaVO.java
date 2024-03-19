package com.hgzp.advertising.pagemodel.statistic.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * AdIncomeYearMeidaVO
 * 创建人：lhl
 * 类描述：广告实收明细表（年度业务）输出类
 * 创建日期：2024/1/26 8:41
 */
@Data
public class AdIncomeYearMeidaVO {
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
     * 经营公司行数
     */
    private int oprow;
    /**
     * 广告公司行数
     */
    private int adrow;
    /**
     * 汇总行数
     */
    private int sumrow;

    /**
     * 汇总项
     */
    List<AdIncomeYearItem> itemList;
}


