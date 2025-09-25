package kr.ac.ks.cs_web_back.global.jwt;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.global.exeption.domain.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.accessSecret}")
    private String aSecret;

    @Value("${jwt.refreshSecret}")
    private String rSecret;

    @Value("${jwt.accessTokenExpireTime}")
    private Long accessTokenExpireTime;

    @Value("${jwt.refreshTokenExpireTime}")
    private Long refreshTokenExpireTime;

    private Key hmacAccessKey;
    private Key hmacRefreshKey;

    @PostConstruct
    public void init() {
        byte[] accessDecodedKey = Base64.getDecoder().decode(aSecret);
        this.hmacAccessKey = new SecretKeySpec(accessDecodedKey, SignatureAlgorithm.HS256.getJcaName());

        byte[] refreshDecodedKey = Base64.getDecoder().decode(rSecret);
        this.hmacRefreshKey = new SecretKeySpec(refreshDecodedKey, SignatureAlgorithm.HS256.getJcaName());
    }

    private String generateToken(String email, Long expireTime, Key key) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime);
        return Jwts.builder().setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public String generateAccessToken(String email) {
        return generateToken(email, accessTokenExpireTime, hmacAccessKey);
    }

    public String generateRefreshToken(String email) {
        return generateToken(email, refreshTokenExpireTime, hmacRefreshKey);
    }

    public String generateTestToken(String email, Long expireTime) {
        return generateToken(email, expireTime, hmacAccessKey);
    }

    private Claims getAllClaimsFromToken(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getExpirationDateFromAccessToken(String token) {
        return getAllClaimsFromToken(token, hmacAccessKey).getExpiration();
    }

    public Date getExpirationDateFromRefreshToken(String token) {
        return getAllClaimsFromToken(token, hmacRefreshKey).getExpiration();
    }

    public String getEmailFromAccessToken(String token) {
        return getAllClaimsFromToken(token, hmacAccessKey).getSubject();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(hmacAccessKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN);
        }
    }
}