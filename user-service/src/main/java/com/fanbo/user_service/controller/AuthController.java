package com.fanbo.user_service.controller;

import com.fanbo.user_service.dto.LoginRequest;
import com.fanbo.user_service.dto.RegisterRequest;
import com.fanbo.user_service.dto.ValidateOtpRequest;
import com.fanbo.user_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try{
            authService.register(registerRequest);
            return ResponseEntity.ok("User registered succesfully");
        }catch(IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        try{
            String token = authService.login(loginRequest);
            return ResponseEntity.ok(token);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid email or password");
        }
    }

    @PostMapping("/login/verify")
    public ResponseEntity<String> loginVerify(@RequestBody ValidateOtpRequest validateOtpRequest) {
        try{
            String token = authService.loginVerify(validateOtpRequest);
            return ResponseEntity.ok(token);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid or expired token");
        }
    }
}
