package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 AdCustomerResourceVO
 创建人：songly
 类描述：客户资源申请表VO
 创建日期：2024/1/15 10:33
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdCustomerResourceVO extends BaseQueryInfo {
    /**
     * 业务员id
     */
    private Long employid;
    /**
     * 客户id
     */
    private String  customerids;
}