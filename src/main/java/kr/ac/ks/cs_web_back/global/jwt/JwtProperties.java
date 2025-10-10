package kr.ac.ks.cs_web_back.global.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String accessSecret,
        String refreshSecret,
        Long accessTokenExpireTime,
        Long refreshTokenExpireTime
) {
}
