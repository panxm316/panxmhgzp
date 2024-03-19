package com.hgzp.advertising.pagemodel.system.vo;

import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MessageVo
 * 创建人：peij
 * 类描述：TODO
 * 创建日期：2023/11/10 12:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageVo extends BaseQueryInfo {
    /**
     * 消息id
     */
    private Long messageId;
    /**
     * 已读
     */
    private Boolean breaded;
    /**
     * 消息类型(TodoTask:工作流待办任务)
     */
    private String stype;

}
