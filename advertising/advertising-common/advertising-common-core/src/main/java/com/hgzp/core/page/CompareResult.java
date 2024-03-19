package com.hgzp.core.page;

import lombok.Data;

/**
 * 创建人：wangwk
 * 类描述：数据比较结果
 * 创建日期：2023/8/7$ 14:24
 */
@Data
public class CompareResult {

    private String fieldName;
    private String oldValue;
    private String newValue;
    private String businessId;
    private String valueChange;




}
