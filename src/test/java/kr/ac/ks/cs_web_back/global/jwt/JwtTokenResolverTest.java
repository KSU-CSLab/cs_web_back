package kr.ac.ks.cs_web_back.global.jwt;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class JwtTokenResolverTest {

    @Autowired
    private JwtTokenResolver jwtTokenResolver;

    @Test
    @DisplayName("resolveToken: Authorization 헤더가 null인 경우 null을 반환한다.")
    void shouldReturnNullWhenAuthorizationNull() {
        //given
        String authorizationHeader = null;

        // when & then
        String result = jwtTokenResolver.resolveToken(authorizationHeader);
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("resolveToken: 헤더가 Bearer로 시작하지 않는 경우 null을 반환한다.")
    void shouldReturnNullWhenHeaderNotStartBearer() {
        //given
        String invalidHeader = "Basics iamstupid";

        // when & then
        String result = jwtTokenResolver.resolveToken(invalidHeader);
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("resolveToken: Authorization 헤더가 null인 경우 null을 반환한다.")
    void shouldReturnNullWhenAuthorizationBlank() {
        //given
        String emptyHeader = "";

        // when & then
        String result = jwtTokenResolver.resolveToken(emptyHeader);
        Assertions.assertNull(result);
    }
}
