package com.fanbo.mail_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class CodeGenerator {
    public String generateCode(){
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }
}
