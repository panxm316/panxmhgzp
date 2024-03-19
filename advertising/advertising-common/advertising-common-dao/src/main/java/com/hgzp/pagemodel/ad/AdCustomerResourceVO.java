package com.hgzp.pagemodel.ad;

import lombok.Data;
import java.util.List;

/**
 * AdCustomerResourceVO
 * 创建人：lhl
 * 类描述：客户来源统计
 * 创建日期：2024/3/12 9:41
 */
@Data
public class AdCustomerResourceVO {
    /**
     * 区域ID
     */
    private Long id;

    /**
     * 区域名称
     */
    private String areaname;
    /**
     * 子区域名称
     */
    private String childname;
    /**
     * 年度用户数量
     */
    List<AdCustomerYearItem>  customerYearItemList;
    /**
     * 用户数量
     */
    long  totalcount;
}


