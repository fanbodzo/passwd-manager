package com.fanbo.mail_service.controller;

import com.fanbo.mail_service.dto.ValidateOtpRequest;
import com.fanbo.mail_service.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@RequestBody ValidateOtpRequest request) {
        try{
            otpService.validateCode(request, LocalDateTime.now());

            return ResponseEntity.ok("successfully validated");
        }catch(Exception e){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid code");
        }

    }

}
