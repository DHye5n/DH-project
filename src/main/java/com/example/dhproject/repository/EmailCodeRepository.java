package com.example.dhproject.repository;

import com.example.dhproject.domain.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCodeRepository extends JpaRepository<EmailEntity, Long> {

    Optional<EmailEntity> findByEmail(String email);

    Optional<EmailEntity> findByEmailAndVerificationCode(String email, String verificationCode);

}
