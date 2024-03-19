package com.hgzp.advertising.pagemodel.schedule.vo;

import lombok.Data;

import java.util.Date;

/**
 MediaPagePlanVo
 创建人：songly
 类描述：媒体版面计划检索类
 创建日期：2023/11/16 11:03
 */
@Data
public class MediaPagePlanVo {
    /**
     * 媒体id
     */
    private Long mediaid;

    /**
     * 媒体名称
     */
    private String medianame;

    /**
     * 叠次ID
     */
    private Long foldid;

    /**
     * 叠次名称
     */
    private String foldname;

    /**
     * 叠次版本id
     */
    private Long foldareaverid;

    /**
     * 叠次版本名称
     */
    private String foldareavername;

    /**
     * 刊登日期
     */
    private Date publishdate;
    /**
     * 刊期
     */
    private Integer publishnum;

}