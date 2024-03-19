package com.hgzp.advertising.pagemodel.business.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 * OrderAndItemVO
 * 创建人：suny
 * 类描述：TODO 广告成本查询VO
 * 创建日期：2023/12/13 15:16
 */
@Data
public class OrderAndItemVO extends BaseQueryInfo {
    /**
     * 订单id
     */
    private String orderid;
    /**
     * 客户id
     */
    private Long customerid;
    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 客户名称
     */
    private String customername;
    /**
     * 发布状态
     */
    private Integer ipublishstatus;
    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 部门id
     */
    private Long deptid;
    /**
     * 人员id
     */
    private Long employid;
    /**
     * 是否当前人员（0：否 1：是，如果是则表示只查询当前人员）
     */
    private Boolean bcurflag;
    /**
     * 是否欠费(查询是否仅欠款的数据，0:否 1：是)
     */
    private Boolean barrearsflag;
}

