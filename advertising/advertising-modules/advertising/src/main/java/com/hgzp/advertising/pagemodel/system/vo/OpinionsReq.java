package com.hgzp.advertising.pagemodel.system.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

/**
 OpinionsReq
 创建人：songly
 类描述：TODO
 创建日期：2024/3/7 19:21
 */
@Data
public class OpinionsReq extends BaseQueryInfo {
    /**
     * 审批流程类型
     */
    private String sflowtype;
    /**
     * 是否用于同意 0-否决 1 -同意
     */
    private Integer ipasstype;
}