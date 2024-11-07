package com.example.dhproject.dto.response;

import com.example.dhproject.domain.EmailEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EmailCodeResponseDto {

    private final String email;
    private final String verificationCode;
    private final LocalDateTime expiryDate;

    @Builder
    public EmailCodeResponseDto(String email, String verificationCode, LocalDateTime expiryDate) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.expiryDate = expiryDate;
    }

    public static EmailCodeResponseDto fromEntity(EmailEntity emailEntity) {
        return EmailCodeResponseDto.builder()
                .email(emailEntity.getEmail())
                .verificationCode(emailEntity.getVerificationCode())
                .expiryDate(emailEntity.getExpiryDate())
                .build();
    }

}
