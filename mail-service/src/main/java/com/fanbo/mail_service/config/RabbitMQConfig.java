package com.fanbo.mail_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String TOPIC_EXCHANGE_NAME = "User_Mail";
    public static final String TOPIC_QUEUE_NAME = "OTP_user";
    public static final String TOPIC_ROUTING_KEY = "otp.send";

    //poczta glowna
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    //skrzynka odbiorcza
    @Bean
    public Queue queue() {
        return new Queue(TOPIC_QUEUE_NAME, true);
    }

    //polaczenie gdzie znajduje sie skrzynka
    @Bean
    public Binding binding(TopicExchange topicExchange, Queue queue) {
        return BindingBuilder.bind(queue).to(topicExchange).with(TOPIC_ROUTING_KEY);
    }
}
