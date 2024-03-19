package com.hgzp.advertising.pagemodel.system.vo;

import com.hgzp.core.model.Twmessage;
import com.hgzp.core.page.BaseQueryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 MessageReq
 创建人：songly
 类描述：TODO
 创建日期：2024/1/8 17:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDataVo extends Twmessage {

    /**
     * 消息类型名称(TodoTask:工作流待办任务)
     */
    private String stypename;
}