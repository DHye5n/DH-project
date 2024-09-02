package com.example.dhproject.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailCodeRequestDto {

    private String email;
    private String verificationCode;

    @Builder
    public EmailCodeRequestDto(String email, String verificationCode) {
        this.email = email;
        this.verificationCode = verificationCode;
    }
}
