package kr.ac.ks.cs_web_back.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class TokenFixture {

    public static final String TEST_EMAIL = "example@ks.ac.kr";

    /**
     * 테스트용 Access Token 생성
     * @param jwtUtil JwtUtil 인스턴스
     * @return 생성된 Access Token
     */
    public static String createAccessToken(JwtUtil jwtUtil) {
        return jwtUtil.generateAccessToken(TEST_EMAIL);
    }

    public static String createAccessToken(JwtUtil jwtUtil, String email) {
        return jwtUtil.generateAccessToken(email);
    }

    /**
     * 테스트용 Refresh Token 생성
     * @param jwtUtil JwtUtil 인스턴스
     * @return 생성된 Refresh Token
     */
    public static String createRefreshToken(JwtUtil jwtUtil) {
        return jwtUtil.generateRefreshToken(TEST_EMAIL);
    }

    public static String createRefreshToken(JwtUtil jwtUtil, String email) {
        return jwtUtil.generateRefreshToken(email);
    }

    public static String createExpiredAccessToken(String secret) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        Key hmacKey = new SecretKeySpec(decodedKey, SignatureAlgorithm.HS256.getJcaName());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() - 1);
        return Jwts.builder().setSubject(TEST_EMAIL)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(hmacKey)
                .compact();
    }
}
