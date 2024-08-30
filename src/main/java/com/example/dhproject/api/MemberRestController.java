package com.example.dhproject.api;

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
    private final EmailService emailService;

    @PostMapping("/send-verification-code")
    public ResponseEntity<ApiResponseDto> sendVerificationCode(@RequestParam String email) {
        try {
            emailCodeService.resendVerificationCode(email);
            return ResponseEntity.ok(new ApiResponseDto(true, "새로운 인증 코드가 발송되었습니다.", null));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(false, e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody @Valid MemberRequestRegisterDto dto) {
        try {
            memberService.register(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponseDto(true, "회원가입이 완료되었습니다.", null));
        } catch (MemberException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponseDto(false, e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<MemberResponseDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable Long memberId) {
        MemberResponseDto memberResponseDto = memberService.getMemberById(memberId);
        return ResponseEntity.ok(memberResponseDto);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MemberResponseDto> getMemberByEmail(@PathVariable String email) {
        MemberResponseDto memberResponseDto = memberService.findByEmail(email);
        return ResponseEntity.ok(memberResponseDto);
    }

    @GetMapping("/username/{username}/exists")
    public ResponseEntity<ApiResponseDto> checkUsernameExists(@PathVariable String username) {
        boolean exists = memberService.checkUsername(username);
        String message = exists ? "사용자 이름이 이미 사용 중입니다." : "사용자 이름이 사용 가능합니다.";
        return ResponseEntity.ok(new ApiResponseDto(!exists, message, null));
    }
}
