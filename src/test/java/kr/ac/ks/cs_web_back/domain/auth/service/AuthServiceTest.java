package kr.ac.ks.cs_web_back.domain.auth.service;

import jakarta.transaction.Transactional;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;
import kr.ac.ks.cs_web_back.domain.auth.dto.response.AuthLoginResponse;
import kr.ac.ks.cs_web_back.domain.auth.fixture.AuthFixture;
import kr.ac.ks.cs_web_back.domain.member.fixture.MemberFixture;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import kr.ac.ks.cs_web_back.global.exeption.domain.NotFoundException;
import kr.ac.ks.cs_web_back.global.exeption.domain.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @BeforeEach // 각각의 테스트가 실행되기 직전에 항상 실행되는 메소드(테스트용 회원을 만들어 DB에 저장)
    void setUp() {
        Member testMember = MemberFixture.memberFixture();
        String rawPassword = "examplePassword1234!";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        memberRepository.save(Member.builder()
                .email(testMember.getEmail())
                .password(encodedPassword)
                .username(testMember.getUsername())
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
}
