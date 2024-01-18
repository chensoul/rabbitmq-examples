package cn.itcast.mq.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;

// @Configuration
public class CommonConfig {
    @Bean
    public DirectExchange simpleDirect(){
        return new DirectExchange("simple.direct");
    }
    @Bean
    public Queue simpleQueue(){
        return QueueBuilder.durable("simple.queue").build();
    }
}
