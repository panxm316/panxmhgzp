package com.hgzp.advertising.pagemodel.schedule.vo;


import com.hgzp.core.annotation.LogData;
import lombok.Data;

import java.util.Date;

/**
 FoldPageplanReq
 创建人：songly
 类描述：版面计划检索类
 创建日期：2023/11/15 14:21
 */
@Data
public class FoldPageplanReq {
    /**
     * 媒体类型key
     */
    private String mediatypekey;

    /**
     * 媒体类型
     */
  //  private String mediatypename;
    /**
     * 媒体id
     */
    private Long mediaid;
    /**
     * 媒体名称
     */
  //  private String medianame;
    /**
     * 叠次ID
     */
    private Long foldid;
    /**
     * 叠次名称
     */
   // private String foldname;
    /**
     * 叠次版本id
     */
    private Long foldareaverid;
    /**
     * 叠次版本名称
     */
    private String foldareavername;
    /** 开始时间*/
    private Date startTime;
    /**查询结束时间*/
    private Date endTime;
    /**
     * 刊登日期
     */
    private Date publishdate;
    /**
     * 色彩id
     */
    private Long adcolorid;

    /**
     * 版面id
     */
    private Long foldpageplanid;
    /**
     * 版号
     */
    private Integer pagenum;
}