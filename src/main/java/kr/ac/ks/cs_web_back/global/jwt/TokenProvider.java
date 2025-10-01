package kr.ac.ks.cs_web_back.global.jwt;

import io.jsonwebtoken.*;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.global.exeption.domain.InvalidTokenException;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class TokenProvider {

    private final Key hmacKey;
    private final Long expireTime;

    public TokenProvider(String secret, Long expireTime) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.hmacKey = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());
        this.expireTime = expireTime;
    }

    public String generateToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expireTime);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(hmacKey)
                .compact();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Date getExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException(AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN);
        }
    }
}
