package com.fanbo.user_service.service;

import com.fanbo.user_service.Security.JwtUtils;
import com.fanbo.user_service.client.MailServiceClient;
import com.fanbo.user_service.dto.LoginRequest;
import com.fanbo.user_service.dto.RegisterRequest;
import com.fanbo.user_service.dto.ValidateOtpRequest;
import com.fanbo.user_service.dto.VerifyOtpRequest;
import com.fanbo.user_service.entity.User;
import com.fanbo.user_service.event.OtpEventPublisher;
import com.fanbo.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final OtpEventPublisher eventPublisher;
    private final MailServiceClient mailServiceClient;

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalStateException("Email already in use");
        }

        User user = User.builder().name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(user);
        eventPublisher.publishOtpEvent(user.getEmail());

        //dodac event register ze sie udalo zarejestrowac uzytkownika

    }
    @Transactional
    public String login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        eventPublisher.publishOtpEvent(loginRequest.getEmail());
        return "Check your Email for OTP code";
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = jwtUtils.generateJwtToken(authentication);
//
//        return jwt;
    }
    public String loginVerify(ValidateOtpRequest validateOtpRequest) {
        mailServiceClient.validateOtp(new ValidateOtpRequest(validateOtpRequest.getEmail(), validateOtpRequest.getCode()));

        return jwtUtils.generateJwtToken(validateOtpRequest.getEmail());
    }
}
