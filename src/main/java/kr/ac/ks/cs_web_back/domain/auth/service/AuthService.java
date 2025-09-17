package kr.ac.ks.cs_web_back.domain.auth.service;

import io.jsonwebtoken.Claims;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;
import kr.ac.ks.cs_web_back.domain.auth.dto.response.AuthLoginResponse;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import kr.ac.ks.cs_web_back.global.exeption.domain.NotFoundException;
import kr.ac.ks.cs_web_back.global.exeption.domain.UnauthorizedException;
import kr.ac.ks.cs_web_back.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthLoginResponse loginMember(AuthLoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new NotFoundException(AuthExceptionCode.NOT_FOUND_USER));

        if(!passwordEncoder.matches(request.password(), member.getPassword()))
            throw new UnauthorizedException(AuthExceptionCode.UNAUTHORIZED_PASSWORD);

        String accessToken = jwtUtil.generateAccessToken(member.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(member.getEmail());

        return AuthLoginResponse.builder()
                .authorization(accessToken)
                .authorizationRefresh(refreshToken)
                .build();
    }
    public void logout(String authorizationHeader) {
        String accessToken = jwtUtil.resolveToken(authorizationHeader);
        Claims claims = jwtUtil.getClaimsFromToken(accessToken);

        long expirationTime = claims.getExpiration().getTime();
        long remainingTime = expirationTime - System.currentTimeMillis();
        if (remainingTime > 0) {
            redisTemplate.opsForValue().set(
                    accessToken,
                    "logout",
                    remainingTime,
                    TimeUnit.MILLISECONDS
            );
        }
    }
}
