package com.example.dhproject.service;

import com.example.dhproject.domain.EmailEntity;
import com.example.dhproject.domain.MemberEntity;
import com.example.dhproject.dto.request.MemberRequestRegisterDto;
import com.example.dhproject.dto.response.MemberResponseDto;
import com.example.dhproject.exception.MemberException;
import com.example.dhproject.repository.EmailCodeRepository;
import com.example.dhproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailCodeRepository emailCodeRepository;
    private final EmailService emailService;

    @Transactional
    public MemberEntity register(MemberRequestRegisterDto dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            log.error("이미 사용중인 이메일입니다. {}", dto.getEmail());
            throw new MemberException("이미 사용중인 이메일입니다.", HttpStatus.CONFLICT);
        }

        if (!dto.isPasswordMatching()) {
            log.error("비밀번호가 일치하지 않습니다. {}", dto.getPassword());
            throw new MemberException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        MemberEntity member = MemberRequestRegisterDto.toEntity(
                dto.getEmail(),
                encodedPassword,
                dto.getUsername(),
                dto.getPhone(),
                dto.getZonecode(),
                dto.getAddress(),
                dto.getAddressDetail(),
                dto.getRole()
        );

        return memberRepository.save(member);
    }

    @Transactional
    public MemberResponseDto findByEmail(String email) {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException("해당 이메일로 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return MemberResponseDto.fromEntity(member);
    }

    @Transactional
    public List<MemberResponseDto> getAllMembers() {
        List<MemberEntity> members = memberRepository.findAll();
        return members.stream()
                .map(MemberResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberResponseDto getMemberById(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException("해당 ID로 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return MemberResponseDto.fromEntity(member);
    }

    @Transactional
    public boolean checkUsername(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);

        return memberRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("User with username '%s' not found", username)));
    }
}
