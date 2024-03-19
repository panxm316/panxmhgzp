package com.hgzp.advertising.pagemodel.business.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 CustomerWorkReportsVO
 创建人：songly
 类描述：TODO
 创建日期：2024/3/7 11:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerWorkReportsVO extends BaseQueryInfo {
    /**
     * 客户ID
     */
    private Long customerid;
    /**
     * 报告类型(0-日报、1-周报、2-月报、3-年报、4-客户服务）
     */
    private Integer iworktype;
}