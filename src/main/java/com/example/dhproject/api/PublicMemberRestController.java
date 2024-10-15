package com.example.dhproject.api;

import com.example.dhproject.dto.request.EmailCodeRequestDto;
import com.example.dhproject.dto.request.MemberRequestRegisterDto;
import com.example.dhproject.dto.response.ApiResponseDto;
import com.example.dhproject.service.EmailCodeService;
import com.example.dhproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/members")
@Slf4j
public class PublicMemberRestController {

    private final MemberService memberService;
    private final EmailCodeService emailCodeService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<ApiResponseDto> sendVerificationCode(@RequestParam String email) {
        emailCodeService.sendNewVerificationCode(email); // 무조건 새로운 인증 코드 생성 및 전송
        return ResponseEntity.ok(new ApiResponseDto(true, "새로운 인증 코드가 발송되었습니다.", null));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponseDto> verifyCode(@RequestBody EmailCodeRequestDto dto) {
        boolean isCodeValid = emailCodeService.verifyCode(dto.getEmail(), dto.getVerificationCode());
        if (isCodeValid) {
            return ResponseEntity.ok(new ApiResponseDto(true, "인증 코드가 확인되었습니다.", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponseDto(false, "인증 코드가 일치하지 않습니다.", null));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@Valid @RequestBody MemberRequestRegisterDto dto) {
        ApiResponseDto response = memberService.register(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDto> checkEmailExists(@RequestParam String email) {
        ApiResponseDto response = memberService.checkEmailExists(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}/exists")
    public ResponseEntity<ApiResponseDto> checkUsernameExists(@PathVariable String username) {
        log.info("Checking availability for username: {}", username);
        ApiResponseDto response = memberService.checkUsername(username);
        return ResponseEntity.ok(response);
    }
}
