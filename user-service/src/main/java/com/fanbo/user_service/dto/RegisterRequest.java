package com.fanbo.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Your name cant be empty")
    @Size(min = 2, message = "Please provide your name")
    private String name;

    @NotBlank(message = "Your password cant be empty")
    @Size(min=8, message = "Password must be at least 8 letters long")
    private String password;

    @NotBlank(message = "Email cant be empty")
    @Email(message = "Email format is incorrect")
    private String email;

}
