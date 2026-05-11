package com.fanbo.mail_service.service;

import com.fanbo.mail_service.entity.EmailDetails;

public interface EmailService {
    String sendEmail(EmailDetails emailDetails);
}
