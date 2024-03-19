package com.hgzp.advertising.pagemodel.system.vo;

import com.hgzp.core.annotation.LogData;
import com.hgzp.core.model.Twopinions;
import lombok.Data;

/**
 OpinionsVO
 创建人：songly
 类描述：TODO
 创建日期：2024/3/7 19:26
 */
@Data
public class OpinionsVO extends Twopinions {
    /**
     * 审批流程类型
     */
    private String sflowtypename;
}