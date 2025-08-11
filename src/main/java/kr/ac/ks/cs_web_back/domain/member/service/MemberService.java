package kr.ac.ks.cs_web_back.domain.member.service;

import kr.ac.ks.cs_web_back.domain.member.controller.code.MemberExceptionCode;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberLoginRequest;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import kr.ac.ks.cs_web_back.global.exeption.domain.ConflictException;
import kr.ac.ks.cs_web_back.global.exeption.domain.NotFoundException;
import kr.ac.ks.cs_web_back.global.exeption.domain.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

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
    public String loginMember(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email()).orElseThrow(() -> new NotFoundException(MemberExceptionCode.NOT_FOUND_USER));
        if(!passwordEncoder.matches(request.password(), member.getPassword()))
            throw new UnauthorizedException(MemberExceptionCode.UNAUTHORIZED_PASSWORD);
        long tokenExpireTime = 1000L * 60 * 60;

    }
}
