package kr.ac.ks.cs_web_back.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@PropertySource("classpath:env.properties")
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperties jwtProperties;

    @Bean(name = "accessTokenProvider")
    public TokenProvider accessTokenProvider() {
        return new TokenProvider(jwtProperties.accessSecret(), jwtProperties.accessTokenExpireTime());
    }

    @Bean(name = "refreshTokenProvider")
    public TokenProvider refreshTokenProvider() {
        return new TokenProvider(jwtProperties.refreshSecret(), jwtProperties.refreshTokenExpireTime());
    }
}
