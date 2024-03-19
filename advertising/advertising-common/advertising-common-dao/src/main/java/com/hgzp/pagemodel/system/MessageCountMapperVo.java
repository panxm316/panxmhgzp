package com.hgzp.pagemodel.system;

import lombok.Data;

import java.util.Date;

/**
 * MessageCount
 * 创建人：peij
 * 类描述：消息提醒
 * 创建日期：2023/10/25 16:14
 */
@Data
public class MessageCountMapperVo {
    /**
     * 消息id
     */
    private String messageId;
    /**
     * 创建者id
     */
    private String createEmpId;
    /**
     * 接收者id
     */
    private String receiveEmpId;
    /**
     * 未读消息条数
     */
    private Long messageCount;
    /**
     * 最新消息提醒标题
     */
    private String messageTitle;
    /**
     * 最新消息提醒内容
     */
    private String messageContent;
    /**
     * 消息类型
     */
    private String messageType;
    /**
     * 流程实例id
     */
    private String messageProcessInstanceId;
    /**
     * 流程实例创建时间
     */
    private Date messageProcessInstanceCreate;
    /**
     * 创建时间
     */
    private Date messageCreateTime;
}
