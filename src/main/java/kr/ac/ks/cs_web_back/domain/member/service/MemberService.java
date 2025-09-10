package kr.ac.ks.cs_web_back.domain.member.service;

import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.domain.member.controller.code.MemberExceptionCode;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import kr.ac.ks.cs_web_back.global.exeption.domain.ConflictException;
import kr.ac.ks.cs_web_back.global.exeption.domain.InvalidTokenException;
import kr.ac.ks.cs_web_back.global.exeption.domain.NotFoundException;
import kr.ac.ks.cs_web_back.global.exeption.domain.UnauthorizedException;
import kr.ac.ks.cs_web_back.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long createMember(MemberCreateRequest request) {
        if (memberRepository.existsByEmail(request.email()))
            throw new ConflictException(MemberExceptionCode.CONFLICT_EMAIL);

        if (memberRepository.existsByUsername(request.username()))
            throw new ConflictException(MemberExceptionCode.CONFLICT_USERNAME);

        String encodedPassword = passwordEncoder.encode(request.password());

        Member member = Member.builder()
                .email(request.email())
                .password(encodedPassword)
                .username(request.username())
                .build();

        return memberRepository.save(member).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
