package com.hgzp.advertising.pagemodel.price.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PriceGroupVO
 * 创建人：suny
 * 类描述：价格组VO
 * 创建日期：2023/11/10 14:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceGroupVO extends BaseQueryInfo {

    /**
     * 媒体类型key
     */
    private String mediatypekey;

    /**
     * 媒体类型名称
     */
    private String mediatypename;

    /**
     * 年份
     */
    private String syear;

    /**
     * 是否默认
     */
    private Boolean bdefault;


    /**
     * 启用标志
     */
    private Boolean buse;
}
