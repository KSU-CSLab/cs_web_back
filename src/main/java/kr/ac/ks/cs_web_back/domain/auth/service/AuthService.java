package kr.ac.ks.cs_web_back.domain.auth.service;

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

import java.util.Date;
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
                .orElseThrow(() -> new NotFoundException(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION));

        if(!passwordEncoder.matches(request.password(), member.getPassword()))
            throw new UnauthorizedException(AuthExceptionCode.UNAUTHORIZED_PASSWORD);

        String accessToken = jwtUtil.generateAccessToken(member.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(member.getEmail());

        Date refreshTokenExpiration = jwtUtil.getExpirationDateFromToken(refreshToken);
        long remainingTime = refreshTokenExpiration.getTime() - System.currentTimeMillis();

        redisTemplate.opsForValue().set(
                "RT:"+ member.getEmail(),
                refreshToken,
                remainingTime,
                TimeUnit.MILLISECONDS
        );

        return AuthLoginResponse.builder()
                .authorization(accessToken)
                .authorizationRefresh(refreshToken)
                .build();
    }
    public void logout(String authorizationHeader) {
        String accessToken = new JwtTokenResolver().resolveToken(authorizationHeader);
        jwtUtil.validateToken(accessToken);
        String email = jwtUtil.getEmailFromToken(accessToken);

        if (redisTemplate.opsForValue().get(accessToken) != null) {
            throw new InvalidTokenException(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION);
        }

        if (redisTemplate.opsForValue().get("RT:"+email) != null) {
            redisTemplate.delete("RT:"+email);
        }

        Date expirationTime = jwtUtil.getExpirationDateFromToken(accessToken);
        long remainingTime = expirationTime.getTime() - System.currentTimeMillis();
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
