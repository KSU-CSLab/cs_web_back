package kr.ac.ks.cs_web_back.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthSuccessCode;
import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;
import kr.ac.ks.cs_web_back.domain.auth.dto.response.AuthLoginResponse;
import kr.ac.ks.cs_web_back.domain.auth.fixture.AuthFixture;
import kr.ac.ks.cs_web_back.domain.auth.service.AuthService;
import kr.ac.ks.cs_web_back.global.exeption.domain.NotFoundException;
import kr.ac.ks.cs_web_back.global.exeption.domain.UnauthorizedException;
import kr.ac.ks.cs_web_back.global.jwt.JwtTokenResolver;
import kr.ac.ks.cs_web_back.global.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("로그인 실패: 요청 본문에 이메일이 없으면 400 Bad Request를 반홚나다.")
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
}
