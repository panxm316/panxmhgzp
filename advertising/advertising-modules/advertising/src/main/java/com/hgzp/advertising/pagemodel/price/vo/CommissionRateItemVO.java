package com.hgzp.advertising.pagemodel.price.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 * CommissionRateItemVO
 * 创建人：suny
 * 类描述：计提比例明细查询实体类
 * 创建日期：2023/11/25 9:26
 */
@Data
public class CommissionRateItemVO extends BaseQueryInfo {
    /**
     * 组id
     */
    private Long commissionrategroupid;
    /**
     * 媒体id
     */
    private Long mediaid;

    /**
     * 行业id
     */
    private Long adindustryid;
    /**
     * 部门id
     */
    private Long deptid;
    /**
     * 人员id
     */
    private Long employid;

}
