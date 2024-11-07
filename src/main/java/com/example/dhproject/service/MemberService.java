package com.example.dhproject.service;

import com.example.dhproject.domain.MemberEntity;
import com.example.dhproject.dto.request.member.MemberRequestRegisterDto;
import com.example.dhproject.dto.response.ApiResponseDto;
import com.example.dhproject.dto.response.MemberResponseDto;
import com.example.dhproject.exception.ErrorException;
import com.example.dhproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailCodeService emailCodeService;


    @Transactional
    public ApiResponseDto register(MemberRequestRegisterDto dto) {
        log.info("회원가입 요청 - 이메일: {}, 코드: {}", dto.getEmail(), dto.getVerificationCode());
        // 이메일 인증 코드 검증
        validateVerificationCode(dto.getEmail(), dto.getVerificationCode());

        if (memberRepository.existsByEmail(dto.getEmail())) {
            log.error("이미 사용중인 이메일입니다. {}", dto.getEmail());
            throw new ErrorException("이미 사용중인 이메일입니다.", HttpStatus.CONFLICT);
        }

        if (memberRepository.existsByUsername(dto.getUsername())) {
            log.error("이미 사용 중인 사용자 이름입니다. {}", dto.getUsername());
            throw new ErrorException("이미 사용 중인 사용자 아이디입니다.", HttpStatus.CONFLICT);
        }

        if (!dto.isPasswordMatching()) {
            log.error("비밀번호가 일치하지 않습니다. {}", dto.getPassword());
            throw new ErrorException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        MemberEntity member = dto.toEntity(encodedPassword);

        memberRepository.save(member);

        return new ApiResponseDto(true, "회원가입이 완료되었습니다.", null);
    }


    private void validateVerificationCode(String email, String code) {
        log.info("회원가입 중 이메일: {}, 코드: {}", email, code);
        boolean isCodeValid = emailCodeService.verifyCode(email, code);
        if (!isCodeValid) {
            log.error("잘못된 이메일 인증 코드입니다. 이메일: {}, 코드: {}", email, code);
            throw new ErrorException("잘못된 이메일 인증 코드입니다.", HttpStatus.BAD_REQUEST);
        }
    }


    public ApiResponseDto findByEmail(String email) {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ErrorException("해당 이메일로 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return new ApiResponseDto(true, "회원 정보가 조회되었습니다.", MemberResponseDto.fromEntity(member));
    }


    public ApiResponseDto checkEmailExists(String email) {
        boolean exists = memberRepository.existsByEmail(email);
        String message = exists ? "이메일이 이미 사용 중입니다." : "이메일을 사용할 수 있습니다.";
        return new ApiResponseDto(!exists, message, null);
    }


    public ApiResponseDto getAllMembers(int page, int size, String sortBy, String sortOrder) {

        Pageable pageable = PageRequest.of(page, size,
                sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        Page<MemberEntity> members = memberRepository.findAll(pageable);
        List<MemberResponseDto> memberDtos = members.stream()
                .map(MemberResponseDto::fromEntity)
                .collect(Collectors.toList());
        return new ApiResponseDto(true, "회원 목록이 조회되었습니다.", memberDtos);
    }


    public ApiResponseDto getMemberById(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorException("해당 ID로 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return new ApiResponseDto(true, "회원 정보가 조회되었습니다.", MemberResponseDto.fromEntity(member));
    }


    public ApiResponseDto checkUsername(String username) {

        log.info("Checking availability for username: {}", username);

        boolean exists = memberRepository.existsByUsername(username);
        String message = exists ? "사용자 이름이 이미 사용 중입니다." : "사용자 이름이 사용 가능합니다.";
        return new ApiResponseDto(!exists, message, null);
    }
}
