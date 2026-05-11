package com.fanbo.mail_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name="otp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    private LocalDateTime expireTime =  LocalDateTime.now();

    private Boolean used;
}
