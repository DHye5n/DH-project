package com.example.dhproject.dto.request;

import com.example.dhproject.domain.EmailEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCodeRequestDto {

    @NotNull
    private String email;
    @NotNull
    private String verificationCode;


    @Builder
    public EmailCodeRequestDto(String email, String verificationCode, LocalDateTime expiryDate) {
        this.email = email;
        this.verificationCode = verificationCode;
    }


    public EmailEntity toEntity() {
        return EmailEntity.builder()
                .email(email)
                .verificationCode(verificationCode)
                .build();
    }

}
