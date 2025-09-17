package kr.ac.ks.cs_web_back.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.global.exeption.domain.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessTokenExpireTime}")
    private Long accessTokenExpireTime;

    @Value("${jwt.refreshTokenExpireTime}")
    private Long refreshTokenExpireTime;

    private Key hmacKey;

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.hmacKey = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());
    }
    public String generateAccessToken(String email) {
        return generateToken(email, accessTokenExpireTime);
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, refreshTokenExpireTime);
    }

    public String generateToken(String email, Long expireTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime);
        return Jwts.builder().setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(hmacKey)
                .compact();
    }
    //Bearer을 제외한 토큰부분만 추출
    public String resolveToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new UnauthorizedException(AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN);
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException | IllegalArgumentException | SignatureException | MalformedJwtException e) {
            throw new UnauthorizedException(AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN);
        }
    }
}