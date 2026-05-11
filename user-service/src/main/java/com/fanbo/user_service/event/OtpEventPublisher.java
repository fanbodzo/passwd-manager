package com.fanbo.user_service.event;


import com.fanbo.user_service.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OtpEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishOtpEvent(String email){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TOPIC_EXCHANGE_NAME,
                RabbitMQConfig.TOPIC_ROUTING_KEY,
                email
        );
    }
}
