package com.example.dhproject.api;

import com.example.dhproject.dto.response.ApiResponseDto;
import com.example.dhproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/members")
@Slf4j
public class AdminMemberRestController {

    private final MemberService memberService;


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


}