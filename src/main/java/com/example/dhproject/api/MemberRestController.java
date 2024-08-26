package com.example.dhproject.api;

import com.example.dhproject.dto.request.MemberRequestRegisterDto;
import com.example.dhproject.dto.response.MemberResponseDto;
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
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        String code = emailCodeService.generateVerificationCode();
        emailCodeService.saveVerificationCode(email, code);
        emailService.sendVerificationEmail(email, code);
        return ResponseEntity.ok("인증 코드가 발송되었습니다.");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid MemberRequestRegisterDto dto) {
        // 이메일 인증 코드 검증
        boolean isCodeValid = emailCodeService.verifyCode(dto.getEmail(), dto.getVerificationCode());
        if (!isCodeValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 이메일 인증 코드입니다.");
        }

        memberService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
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
    public ResponseEntity<String> checkUsernameExists(@PathVariable String username) {
        boolean exists = memberService.checkUsername(username);
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("사용자 이름이 이미 사용 중입니다.");
        }
        return ResponseEntity.ok("사용자 이름이 사용 가능합니다.");
    }
}
