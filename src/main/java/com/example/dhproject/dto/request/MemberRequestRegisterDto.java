package com.example.dhproject.dto.request;


import com.example.dhproject.domain.MemberEntity;
import com.example.dhproject.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class MemberRequestRegisterDto {

    @NotBlank
    @Email(message = "이메일 형식에 맞춰주세요.")
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}", message = "Password must contain at least one digit, one lowercase, and one uppercase character")
    private String password;


    @NotBlank
    private String username;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "핸드폰 번호는 11자리입니다.")
    private String phone;

    @NotBlank
    private String zonecode;

    @NotBlank
    private String address;

    @NotBlank
    private String addressDetail;



    @Builder
    public MemberRequestRegisterDto(String email, String password, String username, String phone,
                             String zonecode, String address, String addressDetail) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phone = phone;
        this.zonecode = zonecode;
        this.address = address;
        this.addressDetail = addressDetail;

    }



    public static MemberEntity toEntity(String email, String encodedPassword, String username, String phone,
                                        String zonecode, String address, String addressDetail, Role role) {
        return MemberEntity.builder()
                .email(email)
                .password(encodedPassword)
                .username(username)
                .phone(phone)
                .zonecode(zonecode)
                .address(address)
                .addressDetail(addressDetail)
                .role(role)
                .build();
    }
}
