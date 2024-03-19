package com.hgzp.advertising.pagemodel.business.dto;

import com.hgzp.core.page.BaseDTO;
import lombok.Data;

/**
 * TwtaskMessageDTO
 * 创建人：lhl
 * 类描述：任务额度消息通知
 * 创建日期：2023/12/29 14:10
 */
@Data
public class TwtaskMessageDTO extends BaseDTO {
    /**
     * 消息接收类型 0：部门 1：人员
     */
    private String messagetype;
    /**
     * 部门ID
     */
    private Long departid;
    /**
     * 人员ID
     */
    private Long emplyeid;
    /**
     * 消息标题
     */
    private String title;
    /**
     * 消息内容
     */
    private String content;

}


