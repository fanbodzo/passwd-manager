package com.fanbo.user_service.client;

import com.fanbo.user_service.dto.ValidateOtpRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="mail-service", url="${MAIL_SERVICE_URI:http://localhost:8082}/api/otp")
public interface MailServiceClient {

    @PostMapping("/validate")
    void validateOtp(@RequestBody ValidateOtpRequest request);
}
