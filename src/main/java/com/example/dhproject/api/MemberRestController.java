package com.example.dhproject.api;

import com.example.dhproject.domain.MemberEntity;
import com.example.dhproject.dto.request.MemberRequestRegisterDto;
import com.example.dhproject.dto.response.MemberResponseDto;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid MemberRequestRegisterDto dto) {
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
