package kr.ac.ks.cs_web_back.domain.auth.service;

import jakarta.transaction.Transactional;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;
import kr.ac.ks.cs_web_back.domain.auth.dto.response.AuthLoginResponse;
import kr.ac.ks.cs_web_back.domain.auth.fixture.AuthFixture;
import kr.ac.ks.cs_web_back.domain.member.fixture.MemberFixture;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import kr.ac.ks.cs_web_back.global.exeption.domain.InvalidTokenException;
import kr.ac.ks.cs_web_back.global.exeption.domain.NotFoundException;
import kr.ac.ks.cs_web_back.global.exeption.domain.UnauthorizedException;
import kr.ac.ks.cs_web_back.global.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private Member testMember;

    @BeforeEach // 각각의 테스트가 실행되기 직전에 항상 실행되는 메소드(테스트용 회원을 만들어 DB에 저장)
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();

        Member memberFixture = MemberFixture.memberFixture();
        String rawPassword = "examplePassword1234!";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        memberRepository.save(Member.builder()
                .email(memberFixture.getEmail())
                .password(encodedPassword)
                .username(memberFixture.getUsername())
                .build()
        );
    }

    @Test
    @DisplayName("로그인 성공: 올바른 이메일과 비밀번호로 로그인 시 200 OK와 토큰을 반환한다.")
    void shouldLoginSuccessfullyAndReturnToken() {
        // given
        AuthLoginRequest request = AuthFixture.successLoginRequest();

        // when
        AuthLoginResponse response = authService.loginMember(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.authorization()).isNotNull().isNotBlank();
        assertThat(response.authorizationRefresh()).isNotNull().isNotBlank();

        String savedRefreshToken = redisTemplate.opsForValue().get("RT:"+request.email());
        assertThat(savedRefreshToken).isNotNull();
        assertThat(savedRefreshToken).isEqualTo(response.authorizationRefresh());
    }

    @Test
    @DisplayName("로그인 실패: 잘못된 비밀번호로 로그인 시 예외가 발생한다.")
    void shouldThrowExceptionWhenLoginWithWrongPassword() {
        // given
        AuthLoginRequest request = AuthFixture.wrongPasswordLoginRequest();

        // when & then
        assertThatThrownBy(() -> authService.loginMember(request))
                .isInstanceOf(UnauthorizedException.class)
                .satisfies(e -> {
                    UnauthorizedException exception = (UnauthorizedException) e;
                    assertThat(exception.getExceptionCode()).isEqualTo(AuthExceptionCode.UNAUTHORIZED_PASSWORD);
                });
    }

    @Test
    @DisplayName("로그인 실패: 존재하지 않는 이메일로 로그인 시 예외가 발생한다.")
    void shouldThrowExceptionWhenLoginWithNonExistentEmail() {
        // given
        AuthLoginRequest request = AuthFixture.NonExistentEmailLoginRequest();

        // when & then
        assertThatThrownBy(() -> authService.loginMember(request))
                .isInstanceOf(NotFoundException.class)
                .satisfies(e -> {
                    NotFoundException exception = (NotFoundException) e;
                    assertThat(exception.getExceptionCode()).isEqualTo(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION);
                });
    }

    @Nested
    @DisplayName("로그아웃 API 테스트")
    class LogoutTests {
        private final String REFRESH_TOKEN_PREFIX = "RT:";

        @Test
        @DisplayName("로그아웃 성공: 유효한 토큰으로 로그아웃 시 200 OK를 반환한다.")
        void shouldLogoutSuccessfullyWithValidToken() {
            // given
            String accessToken = jwtUtil.generateAccessToken(testMember.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(testMember.getEmail());
            redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX+testMember.getEmail(),refreshToken);

            String authorizationHeader = "Bearer " + accessToken;

            // when
            authService.logout(authorizationHeader);

            // then
            String refreshInRedis = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX+testMember.getEmail());
            assertThat(refreshInRedis).isNull();

            String accessInRedis = redisTemplate.opsForValue().get(accessToken);
            assertThat(accessInRedis).isEqualTo("logout");
        }

        @Test
        @DisplayName("로그아웃 실패: 이미 블랙리스트에 등록된 토큰으로 요청시 InvalidTokenException 예외가 발생한다.")
        void shouldThrowExceptionWhenLogoutWithBlacklistedToken() {
            //given
            String accessToken = jwtUtil.generateAccessToken(testMember.getEmail());
            String authorizationHeader = "Bearer " + accessToken;

            redisTemplate.opsForValue().set(accessToken, "logout", 60, TimeUnit.SECONDS);

            // when & then
            assertThatThrownBy(() -> authService.logout(authorizationHeader))
                    .isInstanceOf(InvalidTokenException.class)
                    .satisfies(e ->{
                        InvalidTokenException exception = (InvalidTokenException) e;
                        assertThat(exception.getExceptionCode()).isEqualTo(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION);
                    });
        }

        @Test
        @DisplayName("로그아웃 실패: 만료된 토큰으로 요청 시 InvalidTokenException이 발생한다.")
        void logoutFailWithExpiredToken() throws InterruptedException {
            // given
            String expiredToken = jwtUtil.generateTestToken(testMember.getEmail(), 1L);
            String authorizationHeader = "Bearer " + expiredToken;

            // 토큰이 확실히 만료되도록 잠시 대기
            Thread.sleep(5);

            // when & then
            assertThatThrownBy(() -> authService.logout(authorizationHeader))
                    .isInstanceOf(InvalidTokenException.class)
                    .satisfies(e -> {
                        InvalidTokenException exception = (InvalidTokenException) e;
                        assertThat(exception.getExceptionCode()).isEqualTo(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION);
                    });
        }

        @Test
        @DisplayName("로그아웃 실패: 서명이 유효하지 않은 토큰으로 요청 시 InvalidTokenException이 발생한다.")
        void logoutFailWithInvalidSignatureToken() {
            // given
            // 임의의 잘못된 토큰 문자열
            String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyNDI2MjJ9.invalid-signature";
            String authorizationHeader = "Bearer " + invalidToken;

            // when & then
            // JwtUtil의 현재 구현에 따라 UNAUTHORIZED_INVALID_TOKEN(8001) 코드를 검증
            assertThatThrownBy(() -> authService.logout(authorizationHeader))
                    .isInstanceOf(InvalidTokenException.class)
                    .satisfies(e -> {
                        InvalidTokenException exception = (InvalidTokenException) e;
                        assertThat(exception.getExceptionCode()).isEqualTo(AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN);
                    });
        }
    }
}
