package com.example.dhproject.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {


    private final JavaMailSender javaMailSender;


    public void sendVerificationEmail(String to, String verificationCode) {
        String subject = "이메일 인증 코드";
        String text = "회원가입을 위한 인증 코드: " + verificationCode;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setFrom("sai4875@naver.com");
        message.setText(text);
        try {
            javaMailSender.send(message);
            log.info("Verification email sent to: {}", to);
        } catch (MailException e) {
            log.error("Error sending verification email to: {}", to, e);
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }

}
