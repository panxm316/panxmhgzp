package com.hgzp.pagemodel.ad;

import lombok.Data;

/**
 * AdCustomerYearItem
 * 创建人：lhl
 * 类描述：区域年度用户数量
 * 创建日期：2024/3/12 10:23
 */
@Data
public class AdCustomerYearItem {
    // 年度
    String year;
    /**
     * 客户数量
     */
    private long customercount;

}


