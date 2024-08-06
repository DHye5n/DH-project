package com.example.dhproject.dto.response;

import com.example.dhproject.domain.MemberEntity;
import com.example.dhproject.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private final String email;
    private final String username;
    private final String phone;
    private final Role role;

    @Builder
    public MemberResponseDto(String email, String username, String phone, Role role) {
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.role = role;
    }

    public static MemberResponseDto fromEntity(MemberEntity member) {
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .phone(member.getPhone())
                .role(member.getRole())
                .build();
    }
}

