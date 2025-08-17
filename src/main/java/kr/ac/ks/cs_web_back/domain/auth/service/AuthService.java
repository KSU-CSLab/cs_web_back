package kr.ac.ks.cs_web_back.domain.auth.service;

import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import kr.ac.ks.cs_web_back.global.exeption.domain.NotFoundException;
import kr.ac.ks.cs_web_back.global.exeption.domain.UnauthorizedException;
import kr.ac.ks.cs_web_back.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String loginMember(AuthLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(AuthExceptionCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(request.password(), member.getPassword()))
            throw new UnauthorizedException(AuthExceptionCode.UNAUTHORIZED_PASSWORD);

        long tokenExpireTime = 1000L * 60 * 60;
        return jwtUtil.generateToken(member.getEmail(), tokenExpireTime);
    }
}
