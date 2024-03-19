package com.hgzp.advertising.scheduledtask;

import com.alibaba.fastjson.JSONObject;
import com.hgzp.advertising.service.system.TwmessageServiceI;
import com.hgzp.configuration.WebSocket;
import com.hgzp.core.page.WebSocketMessage;
import com.hgzp.pagemodel.system.MessageCountMapperVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 项目名称：cpsn
 * 类全名:cpsn.scheduledtask.MessageTask
 * 类描述：
 * 创建人：wangwk
 * 创建时间：2018年4月27日 上午10:27:19
 * 方法列表：
 * 修改历史：
 * 1、修改人：wangwk
 * 修改时间：2018年4月27日 上午10:27:19
 * 修改备注：
 * 2、
 *
 * @version jdk1.7
 */
@Component
public class MessageTask {


    @Autowired
    private TwmessageServiceI messageService;
    @Autowired
    private WebSocket webSocket;

    //	 @Scheduled(cron="0/10 * *  * * ? ")   //每10秒执行一次
     @Scheduled(cron = "0 0/1 *  * * ? ")   //每1分钟执行一次
//    @Scheduled(cron = "0/10 * *  * * ? ")   //每1分钟执行一次
    public void aTask() throws Exception {

        List<MessageCountMapperVo> messageList = messageService.getMessageCount();
        for (MessageCountMapperVo message : messageList) {
            WebSocketMessage webSocketMessage = new WebSocketMessage();
            webSocketMessage.setMessageId(message.getMessageId());
            webSocketMessage.setToUser(message.getReceiveEmpId());
            webSocketMessage.setFromUser(message.getCreateEmpId());
            webSocketMessage.setMessageTitle(message.getMessageTitle());
            webSocketMessage.setMessageContent(message.getMessageContent());
            webSocketMessage.setMessageCount(message.getMessageCount());
            webSocketMessage.setType(message.getMessageType().equals("TodoTask") ?
                    WebSocketMessage.FLOW_TodoTask_MSG : message.getMessageType().equals("ApprovePass") ?
                    WebSocketMessage.FLOW_ApprovePass_MSG : message.getMessageType().equals("ApproveReject") ?
                    WebSocketMessage.FLOW_ApproveReject_MSG : WebSocketMessage.SYSTEM_MSG);
            webSocketMessage.setDate(message.getMessageCreateTime());
            webSocketMessage.setProcessInstanceId(message.getMessageProcessInstanceId());
            webSocketMessage.setProcessInstanceCreate(message.getMessageProcessInstanceCreate());
            webSocket.sendOneMessage(message.getReceiveEmpId(), webSocketMessage);
        }

    }


}


