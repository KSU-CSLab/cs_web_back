package kr.ac.ks.cs_web_back.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
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
}