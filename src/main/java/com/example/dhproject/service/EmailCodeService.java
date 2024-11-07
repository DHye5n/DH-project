package com.example.dhproject.service;

import com.example.dhproject.domain.EmailEntity;
import com.example.dhproject.repository.EmailCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmailCodeService {

    private final EmailCodeRepository emailCodeRepository;
    private final EmailService emailService;
    private static final int CODE_LENGTH = 6;

    public String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt((int) Math.pow(10, CODE_LENGTH));
        return String.format("%0" + CODE_LENGTH + "d", code);
    }

    @Transactional
    public void saveVerificationCode(String email, String verificationCode) {
        EmailEntity emailEntity = EmailEntity.builder()
                .email(email)
                .verificationCode(verificationCode)
                .expiryDate(LocalDateTime.now().plusMinutes(10))
                .build();
        emailCodeRepository.save(emailEntity);
    }


    @Transactional
    public boolean verifyCode(String email, String code) {

        Optional<EmailEntity> codeEntityOpt = emailCodeRepository.findByEmailAndVerificationCode(email, code);

        if (codeEntityOpt.isEmpty()) {
            log.warn("DB에 저장된 이메일: {}", email);
            return false;
        }

        EmailEntity emailEntity = codeEntityOpt.get();

        String verificationCode = emailEntity.getVerificationCode();


        log.info("이메일: {}, 입력된 인증번호: {}, DB에 저장된 인증번호: {}",
                email, code, verificationCode);

        boolean isExpired = emailEntity.isExpired();
        boolean isCodeValid = emailEntity.isCodeValid(code);


        log.info("이메일: {}, 인증 유효성: {}, 만료 여부: {}, 만료 시간: {}",
                email, isCodeValid, isExpired, emailEntity.getExpiryDate());

        if (isExpired) {
            log.warn("이메일: {} - 인증 번호 만료.", email);
            return false;
        }

        if (!isCodeValid) {
            log.warn("이메일: {} - 인증 번호가 일치하지 않습니다.", email);
            return false;
        }

        return true;
    }

    @Transactional
    public void sendNewVerificationCode(String email) {
        String code = generateVerificationCode();
        saveVerificationCode(email, code);
        emailService.sendVerificationEmail(email, code);
    }

}
