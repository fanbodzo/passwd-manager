package com.fanbo.mail_service.config;

import com.fanbo.mail_service.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OtpListener {
    private final OtpService otpService;

    @RabbitListener(queues = RabbitMQConfig.TOPIC_QUEUE_NAME)
    public void handleUserLogin(String email) {
        otpService.generateCodeAndSave(email);
    }
}
