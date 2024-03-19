package com.hgzp.advertising.pagemodel.commission.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 * OrderItemCommissionVO
 * 创建人：suny
 * 类描述：TODO 佣金计提查询VO
 * 创建日期：2024/1/12 13:02
 */
@Data
public class OrderItemCommissionVO extends BaseQueryInfo {
    /**
     * 业务员id
     */
    @LogData(alias = "业务员id")
    private Long employid;
    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 业务员部门id
     */
    private Long deptid;
    /**
     * 计算状态（是否查询已计算过的佣金的数据）
     */
    private Boolean bcountflag;
    /**
     * 标记(0-计算1-确认 2-发放 3-撤销)
     */
    @LogData(alias = "标记(0-计算1-确认 2-发放 3-撤销) ")
    private Integer icommissionstatus;
    /**
     * 是否当前人员（0：否 1：是，如果是则表示只查询当前人员）
     */
    private Boolean bcurflag;
    /**
     * 是否统计查询（统计查询时只查询确认/发放的数据）
     */
    private Boolean bstatflag;
}

