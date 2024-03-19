package com.hgzp.advertising.pagemodel.schedule.vo;

import lombok.Data;

import java.util.Date;

/**
 FoldPagePlaneTreeReq
 创建人：songly
 类描述：用于版面计划树检索条件类
 创建日期：2023/12/21 10:54
 */
@Data
public class FoldPagePlaneTreeReq {
    /**
     * 媒体类型key
     */
    private String mediatypekey;
    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 刊登日期
     */
    private Date publishdate;
    /**
     * 是否检索版面
     */
    private Boolean isShowPagenum;
}