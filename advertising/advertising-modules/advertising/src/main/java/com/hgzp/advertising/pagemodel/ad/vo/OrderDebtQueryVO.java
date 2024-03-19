package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 * OrderDebtQueryVO
 * 创建人：suny
 * 类描述：欠款统计查询对象
 * 创建日期：2023/11/21 11:10
 */
@Data
public class OrderDebtQueryVO extends BaseQueryInfo {
    /**
     * 部门id
     */
    private Long deptid;
}
