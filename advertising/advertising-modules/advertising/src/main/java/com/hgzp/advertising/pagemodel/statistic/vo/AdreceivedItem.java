package com.hgzp.advertising.pagemodel.statistic.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * AdreceivedItem
 * 创建人：lhl
 * 类描述：广告实收报表项
 * 创建日期：2024/1/16 10:31
 */
@Data
public class AdreceivedItem {
    /**
     * 名称
     */
    private String name;
    /**
     * 经营公司本月数
     */
    private BigDecimal opmonthtotal;
    /**
     * 经营公司累计数
     */
    private BigDecimal optotal;
    /**
     * 广告公司本月数
     */
    private BigDecimal admonthtotal;
    /**
     * 广告公司累计数
     */
    private BigDecimal adtotal;
    /**
     * 本月合计数
     */
    private BigDecimal amountmonthtotal;
    /**
     * 累计数
     */
    private BigDecimal amounttotal;
    /**
     * 多元化
     */
    private BigDecimal dytotal;
    /**
     * 去年多元化
     */
    private BigDecimal predytotal;

    /**
     * 经营公司本月数
     */
    private String stropmonthtotal;
    /**
     * 经营公司累计数
     */
    private String stroptotal;
    /**
     * 广告公司本月数
     */
    private String stradmonthtotal;
    /**
     * 广告公司累计数
     */
    private String stradtotal;
    /**
     * 本月合计数
     */
    private String stramountmonthtotal;
    /**
     * 累计数
     */
    private String stramounttotal;

    /**
     * 经营公司本月数
     */
    List<ColumnSummary> opcolumnSummaryList;
    /**
     * 经营公司本年累计数
     */
    List<ColumnSummary> opyearcolumnSummaryList;
    /**
     * 广告公司本月数
     */
    List<ColumnSummary> adcolumnSummaryList;
    /**
     * 广告公司本年累计数
     */
    List<ColumnSummary> adyearcolumnSummaryList;
    /**
     * 广告公司、经营公司汇总本年数
     */
    List<ColumnSummary> opadyearcolumnSummaryList;
    /**
     * 广告公司、经营公司汇总本月数
     */
    List<ColumnSummary> opadcolumnSummaryList;

}


