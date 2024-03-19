package com.hgzp.advertising.pagemodel.schedule.dto;
import com.hgzp.core.annotation.LogData;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.Data;

import java.util.Date;

/**
 OrderitemSupplementPublishReq
 创建人：hgsongly
 类描述：TODO
 创建日期：2023/12/23 14:53
 */
@Data
public class OrderitemSupplementPublishReq extends BaseQueryInfo {
    /**
     * 媒体类型key
     */
    private String mediatypekey;
    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 叠次id
     */
    private Long foldid;
    /**
     * 叠次版本id
     */
    private Long foldareaverid;
    /**
     * 合同号
     */
    private String scontractnum;
    /**
     * 录入方式 0-正常 1-广告预约 2-快速预约 3-补刊
     */
    private Integer ibooktype;

}