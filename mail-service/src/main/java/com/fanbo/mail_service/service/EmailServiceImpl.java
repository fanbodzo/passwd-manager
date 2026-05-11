package com.fanbo.mail_service.service;

import com.fanbo.mail_service.entity.EmailDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    //tutaj dodac , n razei test
    //    @Value("${spring.mail.username}")
    private String sender = "noreply@passwdmanager.com";

    @Override
    public String sendEmail(EmailDetails emailDetails) {
        try{

            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(sender);
            message.setTo(emailDetails.getEmail());
            message.setSubject(emailDetails.getSubject());
            message.setText(emailDetails.getContent());

            mailSender.send(message);

            //dac pozniej ianczej to ale no
            return "Mail sent successfully";
        }catch (Exception e){
            return "Error sending email";
        }
    }
}
