package com.example.dhproject.service;

import com.example.dhproject.domain.EmailEntity;
import com.example.dhproject.repository.EmailCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCodeService {

    private final EmailCodeRepository emailCodeRepository;
    private final EmailService emailService;
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

    @Transactional
    public boolean verifyCode(String email, String code) {
        Optional<EmailEntity> codeEntityOpt = emailCodeRepository.findByEmailAndVerificationCode(email, code);
        if (codeEntityOpt.isEmpty()) {
            log.warn("이메일 {}에 대한 인증 코드가 존재하지 않거나 코드가 일치하지 않습니다.", email);
            return false;
        }

        EmailEntity emailEntity = codeEntityOpt.get();
        boolean isExpired = emailEntity.isExpired();
        boolean isCodeValid = emailEntity.isCodeValid(code);

        log.info("검증 중: 이메일 = {}, 입력된 코드 = {}, 저장된 코드 = {}, 만료일 = {}, 코드 유효 여부 = {}, 만료 여부 = {}",
                email, code, emailEntity.getVerificationCode(), emailEntity.getExpiryDate(), isCodeValid, isExpired);

        if (isExpired || !isCodeValid) {
            log.warn("이메일 {}의 인증 코드가 유효하지 않거나 만료되었습니다.", email);
        }

        return !isExpired && isCodeValid;
    }


    public void sendNewVerificationCode(String email) {
        String code = generateVerificationCode();
        saveVerificationCode(email, code);
        emailService.sendVerificationEmail(email, code);
    }

}
