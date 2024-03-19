package com.hgzp.pagemodel.api;

import com.hgzp.core.page.BaseDTO;
import lombok.Data;

/**
 * CJAdPrinDTO
 * 创建人：suny
 * 类描述：超捷使用的打印排版对象DTO
 * 创建日期：2024/2/28 10:23
 */
@Data
public class CJAdPrinDTO extends BaseDTO {
    /**
     * 广告刊期主键
     */
    private String inforid;
    /**
     * 客户id
     */
    private String custid;
    /**
     * 客户名称
     */
    private String custname;
    /**
     * 广告行业id
     */
    private String kinddetid;
    /**
     * 广告行业名称
     */
    private String adkind;
    /**
     * 广告发布日期
     */
    private String publdate;
    /**
     * 刊期类型，默认'指定刊期'
     */
    private String pubrequest;
    /**
     * 文件制作时间，默认''
     */
    private String filedate;
    /**
     * 符合条件的广告个数
     */
    private String foretimes;
    /**
     * ’安排‘的广告个数
     */
    private String arrtimes;
    /**
     * ’发布‘的广告个数
     */
    private String pubtimes;
    private String remark;
}

