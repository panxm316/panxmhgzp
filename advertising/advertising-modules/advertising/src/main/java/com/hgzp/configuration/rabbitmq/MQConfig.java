package com.hgzp.configuration.rabbitmq;

import com.hgzp.configuration.WebSocket;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 项目名：hgcb-parent
 * 类全名：com.hgzp.test.configuration.rabbitmq.MQConfig
 * 创建人：wangwk
 * 类描述：rabbitmq 消息队列配置
 * 创建日期：2022/10/10 15:37
 */

@Configuration
public class MQConfig {


    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.queue}")
    private String queue;


    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder.fanoutExchange(exchange).durable(true).build();
    }

    @Bean
    public Queue fanoutQueue() {
        return QueueBuilder.durable(queue).build();
    }
    @Bean
    public Binding bindingExchangeA() {
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }

    //==================websocket ================================
    @Bean
    public FanoutExchange webscoketfanoutExchange() {
        return new FanoutExchange(WebSocket.SOCKET_EXCHANGE);
    }

    @Bean
    public AnonymousQueue queueForWebSocket() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding bindingSingle(FanoutExchange webscoketfanoutExchange, AnonymousQueue queueForWebSocket) {
        return BindingBuilder.bind(queueForWebSocket).to(webscoketfanoutExchange);
    }



}
