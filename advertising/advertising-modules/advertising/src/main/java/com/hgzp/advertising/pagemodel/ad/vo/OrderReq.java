package com.hgzp.advertising.pagemodel.ad.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 OrderReq
 创建人：songly
 类描述：订单检索类
 创建日期：2023/12/9 13:03
 */
@Data
public class OrderReq extends BaseQueryInfo {

    /**资源Id*/
    private Long adresourceapplicationid;
    /**
     * 订单审批状态
     */
    private Integer iapprovestatus;

    /**
     * 发布状态
     */
    private Integer ipublishstatus;
    /**
     * 是否包含待审订单
     */
    private Boolean bshowedit;
}