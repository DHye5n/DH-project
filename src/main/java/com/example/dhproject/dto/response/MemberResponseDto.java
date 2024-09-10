package com.example.dhproject.dto.response;

import com.example.dhproject.domain.MemberEntity;
import lombok.Builder;
import lombok.Getter;


@Getter
public class MemberResponseDto {

    private final Long memberId;
    private final String email;
    private final String username;
    private final String phone;


    @Builder
    public MemberResponseDto(
            Long memberId, String email, String username, String phone) {
        this.memberId = memberId;
        this.email = email;
        this.username = username;
        this.phone = phone;


    }

    public static MemberResponseDto fromEntity(MemberEntity member) {
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .username(member.getUsername())
                .phone(member.getPhone())
                .build();
    }
}

