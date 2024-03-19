package com.hgzp.advertising.pagemodel.finance.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 * <p>
 * 广告分摊明细表 VO 前端页面视图对象
 * </p>
 *
 * @author suny
 * @since 2023-12-18
 */
@Data
public class OrderApportiondetailVO extends BaseQueryInfo {

    /**
     * 主键
     */
    private Long id;
    /**
     * 创建者id
     */
    private Long createempid;
    /**
     * 收费表id
     */
    private Long customerchargeid;
    /**
     * 发票表id
     */
    private Long invoiceid;
    /**
     * 发票号
     */
    private String invoice;
    /**
     * 订单id
     */
    private Long orderid;
    /**
     * 刊期id
     */
    private Long orderitemid;
}
