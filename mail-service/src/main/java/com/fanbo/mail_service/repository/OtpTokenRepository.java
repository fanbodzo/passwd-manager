package com.fanbo.mail_service.repository;

import com.fanbo.mail_service.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findByEmail(String email);
    Optional<OtpToken> findByEmailAndCode(String email, String code);
}
