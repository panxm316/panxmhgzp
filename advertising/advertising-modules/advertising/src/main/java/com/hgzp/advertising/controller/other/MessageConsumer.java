package com.hgzp.advertising.controller.other;

import com.alibaba.fastjson.JSONObject;
import com.hgzp.configuration.WebSocket;
import com.hgzp.core.page.WebSocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MessageConsumer  {

	
	private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
	@Autowired
	WebSocket socket;

	@RabbitHandler
	@RabbitListener(queues = "${spring.rabbitmq.queue}")
	public void onMessage(String receivemsg) {
		logger.info("rabbitmq Received:"+receivemsg);
	}

	@RabbitHandler
	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(durable = "true"),
			exchange = @Exchange(value = WebSocket.SOCKET_EXCHANGE, type = ExchangeTypes.FANOUT)
	))
	public void socketReceiver(String message) {
		logger.info("队列接收到了消息: [{}]", message);
		WebSocketMessage webSocketMessage = JSONObject.parseObject(message, WebSocketMessage.class);
		if(StringUtils.hasText(webSocketMessage.getToUser())){
			socket.sendMoreMessage(webSocketMessage.getToUser().split(","), webSocketMessage);
		}
	}
}