package com.example.dhproject.api;

import com.example.dhproject.dto.request.EmailCodeRequestDto;
import com.example.dhproject.dto.request.MemberRequestRegisterDto;
import com.example.dhproject.dto.response.ApiResponseDto;
import com.example.dhproject.dto.response.MemberResponseDto;
import com.example.dhproject.exception.MemberException;
import com.example.dhproject.service.EmailCodeService;
import com.example.dhproject.service.EmailService;
import com.example.dhproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberRestController {

    private final MemberService memberService;
    private final EmailCodeService emailCodeService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<ApiResponseDto> sendVerificationCode(@RequestParam String email) {
        try {
            emailCodeService.sendNewVerificationCode(email); // 무조건 새로운 인증 코드 생성 및 전송
            return ResponseEntity.ok(new ApiResponseDto(true, "새로운 인증 코드가 발송되었습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "인증 코드 전송에 실패했습니다.", null));
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponseDto> verifyCode(@RequestBody EmailCodeRequestDto dto) {
        try {
            boolean isCodeValid = emailCodeService.verifyCode(dto.getEmail(), dto.getVerificationCode());
            if (isCodeValid) {
                return ResponseEntity.ok(new ApiResponseDto(true, "인증 코드가 확인되었습니다.", null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponseDto(false, "인증 코드가 일치하지 않습니다.", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponseDto(false, "인증 코드 확인 중 오류가 발생했습니다.", null));
        }
    }


//    @PostMapping("/resend-verification-code")
//    public ResponseEntity<ApiResponseDto> resendVerificationCode(@RequestParam String email) {
//        try {
//            emailCodeService.resendVerificationCode(email);
//            return ResponseEntity.ok(new ApiResponseDto(true, "새로운 인증 코드가 발송되었습니다.", null));
//        } catch (IllegalStateException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new ApiResponseDto(false, e.getMessage(), null));
//        }
//    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody @Valid MemberRequestRegisterDto dto) {
        ApiResponseDto response = memberService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDto> checkEmailExists(@RequestParam String email) {
        ApiResponseDto response = memberService.checkEmailExists(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto> getAllMembers() {
        ApiResponseDto response = memberService.getAllMembers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponseDto> getMemberById(@PathVariable Long memberId) {
        ApiResponseDto response = memberService.getMemberById(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponseDto> getMemberByEmail(@PathVariable String email) {
        ApiResponseDto response = memberService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}/exists")
    public ResponseEntity<ApiResponseDto> checkUsernameExists(@PathVariable String username) {
        ApiResponseDto response = memberService.checkUsername(username);
        return ResponseEntity.ok(response);
    }
}
