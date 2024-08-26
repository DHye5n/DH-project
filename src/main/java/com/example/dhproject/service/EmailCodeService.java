package com.example.dhproject.service;

import com.example.dhproject.domain.EmailEntity;
import com.example.dhproject.repository.EmailCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCodeService {

    private final EmailCodeRepository emailCodeRepository;
    private static final int CODE_LENGTH = 6;

    public String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt((int) Math.pow(10, CODE_LENGTH));
        return String.format("%0" + CODE_LENGTH + "d", code);
    }

    public void saveVerificationCode(String email, String verificationCode) {
        EmailEntity emailEntity = EmailEntity.builder()
                .email(email)
                .verificationCode(verificationCode)
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();
        emailCodeRepository.save(emailEntity);
    }

    public boolean verifyCode(String email, String code) {
        Optional<EmailEntity> codeEntityOpt = emailCodeRepository.findByEmail(email);
        if (codeEntityOpt.isEmpty()) {
            return false;
        }
        EmailEntity emailEntity = codeEntityOpt.get();
        return !emailEntity.isExpired() && emailEntity.getVerificationCode().equals(code);
    }
}
