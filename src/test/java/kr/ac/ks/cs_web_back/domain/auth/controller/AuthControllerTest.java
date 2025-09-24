package kr.ac.ks.cs_web_back.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthSuccessCode;
import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;
import kr.ac.ks.cs_web_back.domain.auth.dto.response.AuthLoginResponse;
import kr.ac.ks.cs_web_back.domain.auth.fixture.AuthFixture;
import kr.ac.ks.cs_web_back.domain.auth.service.AuthService;
import kr.ac.ks.cs_web_back.global.exeption.domain.InvalidTokenException;
import kr.ac.ks.cs_web_back.global.exeption.domain.NotFoundException;
import kr.ac.ks.cs_web_back.global.exeption.domain.UnauthorizedException;
import kr.ac.ks.cs_web_back.global.jwt.JwtTokenResolver;
import kr.ac.ks.cs_web_back.global.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtTokenResolver jwtTokenResolver;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("로그인 성공: 올바른 이메일과 비밀번호로 로그인 시 200 OK와 토큰을 반환한다.")
    void loginSuccessReturns200OkWithToken() throws Exception {
        // given
        AuthLoginRequest request = AuthFixture.successLoginRequest();
        AuthLoginResponse response = AuthLoginResponse.builder()
                .authorization("fake-access-token")
                .authorizationRefresh("fake-refresh-token")
                .build();

        when(authService.loginMember(any(AuthLoginRequest.class)))
                .thenReturn(response);

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(AuthSuccessCode.LOGIN_SUCCESS.getCode()))
                .andExpect(jsonPath("$.message").value(AuthSuccessCode.LOGIN_SUCCESS.getMessage()))
                .andExpect(jsonPath("$.data.authorization").value("fake-access-token"))
                .andExpect(jsonPath("$.data.authorizationRefresh").value("fake-refresh-token"));
    }

    @Test
    @DisplayName("로그인 실패: 요청 본문에 비밀번호가 없으면 400 Bad Request를 반환한다.")
    void loginWithoutPasswordReturns400BadRequest() throws Exception {
        // given
        AuthLoginRequest request = new AuthLoginRequest("example@ks.ac.kr", "");

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 실패: 요청 본문에 이메일이 없으면 400 Bad Request를 반환한다.")
    void loginWithoutEmailReturns400BadRequest() throws Exception {
        // given
        AuthLoginRequest request = new AuthLoginRequest("", "examplePassword1234!");

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인 실패: 비밀번호가 일치하지 않으면 401 Unauthorized를 반환한다.")
    void loginWithWrongPasswordReturns401Unauthorized() throws Exception {
        // given
        AuthLoginRequest request = AuthFixture.wrongPasswordLoginRequest();

        when(authService.loginMember(any(AuthLoginRequest.class)))
                .thenThrow(new UnauthorizedException(AuthExceptionCode.UNAUTHORIZED_PASSWORD));

        // when & then
        mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(AuthExceptionCode.UNAUTHORIZED_PASSWORD.getCode()))
                .andExpect(jsonPath("$.message").value(AuthExceptionCode.UNAUTHORIZED_PASSWORD.getMessage()));
    }

    @Test
    @DisplayName("로그인 실패: 존재하지 않는 이메일이면 404 Not Found를 반환한다.")
    void loginWithNonExistentEmailReturns404NotFound() throws Exception {
        // given
        AuthLoginRequest request = AuthFixture.NonExistentEmailLoginRequest();

        when(authService.loginMember(any(AuthLoginRequest.class)))
                .thenThrow(new NotFoundException(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION));

        // when & then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION.getCode()))
                .andExpect(jsonPath("$.message").value(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION.getMessage()));
    }

    @Nested
    @DisplayName("로그아웃 API 테스트")
    class LogoutTest {
        final String authorization = "Bearer valid-fake-access-token";
        @Test
        @DisplayName("로그아웃 성공: 유효한 토큰으로 로그아웃 시 200 OK를 반환한다.")
        void logoutSuccessReturns200Ok() throws Exception {
            // given
            // void 반환하니, 예외 없으면 성공으로 간주
            // when & then
            mockMvc.perform(post("/auth/logout")
                            .header("Authorization", authorization))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(AuthSuccessCode.LOGOUT_SUCCESS.getCode()))
                    .andExpect(jsonPath("$.message").value(AuthSuccessCode.LOGOUT_SUCCESS.getMessage()));
        }

        @Test
        @DisplayName("로그아웃 실패: Authorization 헤더가 없으면 400 Bad Request를 반환한다.")
        void logoutWithoutTokenReturns400BadRequest() throws Exception {
            // given
            // 헤더 없이 요청을 보냄
            // when & then
            mockMvc.perform(post("/auth/logout"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("로그아웃 실패: 유효하지 않은 토큰이면 401 Unauthorized를 반환한다.")
        void logoutWithInvalidTokenReturns401() throws Exception {
            // 토큰이 물리적으로 유효하지 않은 모든 경우 (서명불일치, 형식 오류)
            // given
            // 가짜 Authservice가 InvalidTokenException을 던지도록 설정
            doThrow(new InvalidTokenException(AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN))
                    .when(authService).logout(anyString());

            // when & then
            mockMvc.perform(post("/auth/logout")
                        .header("Authorization", authorization))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.code").value(AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN.getCode()))
                    .andExpect(jsonPath("$.message").value(AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN.getMessage()));
        }

        @Test
        @DisplayName("로그아웃 실패: 이미 로그아웃/만료된 토큰이면 401 Unauthorized를 반환한다.")
        void logoutWithAlreadyLoggedOutTokenReturns401() throws Exception {
            // given
            doThrow(new InvalidTokenException(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION))
                    .when(authService).logout(anyString());

            // when & then
            mockMvc.perform(post("/auth/logout")
                            .header("Authorization", authorization))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.code").value(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION.getCode()))
                    .andExpect(jsonPath("$.message").value(AuthExceptionCode.UNAUTHORIZED_FAILED_VALIDATION.getMessage()));
        }
    }
}
