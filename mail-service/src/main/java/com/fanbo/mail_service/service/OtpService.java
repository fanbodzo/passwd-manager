package com.fanbo.mail_service.service;

import com.fanbo.mail_service.dto.ValidateOtpRequest;
import com.fanbo.mail_service.entity.EmailDetails;
import com.fanbo.mail_service.entity.OtpToken;
import com.fanbo.mail_service.repository.OtpTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpTokenRepository otpTokenRepository;
    private final CodeGenerator codeGenerator;
    private final EmailServiceImpl emailService;

    public void generateCodeAndSave(String email) {
        OtpToken token = OtpToken.builder()
                .email(email)
                .code(codeGenerator.generateCode())
                .expireTime(LocalDateTime.now().plusMinutes(2))
                .used(false)
                .build();
        otpTokenRepository.save(token);

        EmailDetails emailDetails = new EmailDetails(email,
                "Login Code",
                "Your login code:" +
                        token.getCode() + " expiration time 2 minutes." );

        //POMOCNICZE DO TESTU
        System.out.println(emailDetails);

        emailService.sendEmail(emailDetails);
    }

    public Boolean validateCode(ValidateOtpRequest request, LocalDateTime validationTime) {
        try{
            OtpToken token = otpTokenRepository.findByEmailAndCode(request.getEmail(), request.getCode())
                    .orElseThrow(() -> new Exception("Invalid code"));

            if(token.getUsed()) {
                throw new Exception("Code already used");
            }

            if(validationTime.isAfter(token.getExpireTime())) {
                throw new Exception("Code expired");
            }

            token.setUsed(true);
            otpTokenRepository.save(token);

            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());

            return false;
        }
    }
}
