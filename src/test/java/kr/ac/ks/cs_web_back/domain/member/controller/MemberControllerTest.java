package kr.ac.ks.cs_web_back.domain.member.controller;

import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.member.service.MemberService;
import kr.ac.ks.cs_web_back.global.jwt.JwtTokenResolver;
import kr.ac.ks.cs_web_back.global.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(MemberControllerTest.TestErrorAdvice.class)
public class MemberControllerTest {

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtTokenResolver jwtTokenResolver;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공: 회원가입에 성공하면 201 Created를 반환한다.")
    public void registeredSuccessfullyReturns201Created() throws Exception {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "validEmail123@ks.ac.kr",
                "validPassword1234!",
                "exampleUser"
        );

        // when & then
        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 실패: 이메일 필드가 없으면 400 BadRequest를 반환한다.")
    public void registeredWithoutEmailReturns400BadRequest() throws Exception {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "",
                "validPassword1234!",
                "exampleUser"
        );

        // when & then
        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패: 비밀번호 필드가 없으면 400 BadRequest를 반환한다.")
    public void registeredWithoutPasswordReturns400BadRequest() throws Exception {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "example@ks.ac.kr",
                "",
                "exampleUser"
        );

        // when & then
        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패: 유저네임 필드가 없으면 400 BadRequest를 반환한다.")
    public void registeredWithoutUsernameReturns400BadRequest() throws Exception {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "example@ks.ac.kr",
                "validPassword1234!",
                ""
        );

        // when & then
        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패: 입력된 이메일 형식이 유효하지 않으면 400 BadRequest를 반환한다.")
    public void registeredWithInvalidEmailReturns400BadRequest() throws Exception {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "invalidEmail",
                "validPassword1234!",
                "exampleUser"
        );

        // when & then
        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패: 비밀번호가 48글자 이상이면 400 BadRequest를 반환한다.")
    public void tooLongPasswordReturns400BadRequest() throws Exception {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "invalidEmail",
                "a".repeat(48),
                "exampleUser"
        );

        // when & then
        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("회원가입 실패: 유저네임이 12글자를 초과하면 400 BadRequest를 반환한다.")
    public void tooLongUsernameReturns400BadRequest() throws Exception {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "invalidEmail",
                "validPassword1234!",
                "username".repeat(10)
        );

        // when & then
        mockMvc.perform(post("/member/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유저조회 성공: 유저 정보 조회에 성공하면 200 OK를 반환한다.")
    public void getMemberInfoReturns200Ok() throws Exception {
        // given
        var resp = new kr.ac.ks.cs_web_back.domain.member.dto.response.MemberResponse(
                "example@ks.ac.kr",
                "쿨쿨푸데데드르렁퓨우",
                java.time.LocalDate.of(2006, 2, 3),
                "010 6648 7274"
        );
        given(memberService.getMemberInfo(anyLong())).willReturn(resp);

        // when & then
        mockMvc.perform(get("/member/profile/{userid}", 1L)
                        .header("Authorization", "accessToken")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저조회 실패: Authorization 헤더가 없으면 400 BadRequest를 반환한다.")
    public void getMemberInfoWithoutAuthHeaderReturns400BadRequest() throws Exception {
        // when & then
        mockMvc.perform(get("/member/profile/{userid}", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("유저조회 실패: 유효하지 않은 토큰 형식이면 401 Unauthorized를 반환한다.")
    void getMemberInfo_401_invalidTokenFormat() throws Exception {
        // given
        given(memberService.getMemberInfo(anyLong()))
                .willThrow(new InvalidTokenFormatException("유효하지 않은 토큰 형식입니다."));

        // when & then
        mockMvc.perform(get("/member/profile/{userid}", 1L)
                        .header("Authorization", "bad-format-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("유저조회 실패: 만료된 토큰이면 401 Unauthorized를 반환한다.")
    void getMemberInfo_401_expiredToken() throws Exception {
        // given
        given(memberService.getMemberInfo(anyLong()))
                .willThrow(new TokenExpiredException("만료된 토큰입니다."));

        // when & then
        mockMvc.perform(get("/member/profile/{userid}", 1L)
                        .header("Authorization", "expired-token")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("유저조회 실패: 본인의 정보가 아니면 403 Forbidden를 반환한다.")
    void getMemberInfo_403_forbidden() throws Exception {
        // given
        given(memberService.getMemberInfo(anyLong()))
                .willThrow(new ForbiddenAccessException("본인의 정보만 조회할 수 있습니다."));

        // when & then
        mockMvc.perform(get("/member/profile/{userid}", 2L)
                        .header("Authorization", "accessToken-of-user-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }




    @Test
    @DisplayName("유저조회 실패: 존재하지 않는 사용자면 404 NotFound를 반환한다.")
    public void getMemberInfoReturns404NotFound() throws Exception {
        // given
        given(memberService.getMemberInfo(anyLong()))
                .willThrow(new IllegalArgumentException("일치하는 사용자가 존재하지 않습니다."));

        // when & then
        mockMvc.perform(get("/member/profile/{userid}", 99L)
                        .header("Authorization", "accessToken")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // 테스트 전용 예외
    static class InvalidTokenFormatException extends RuntimeException {
        public InvalidTokenFormatException(String m) {
            super(m);
        }
    }

    static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException(String m) {
            super(m);
        }
    }

    static class ForbiddenAccessException extends RuntimeException {
        public ForbiddenAccessException(String m) {
            super(m);
        }
    }

    // 테스트 전용 핸들러
    @org.springframework.web.bind.annotation.RestControllerAdvice(assignableTypes = MemberController.class)
    static class TestErrorAdvice {
        @org.springframework.web.bind.annotation.ExceptionHandler(org.springframework.web.bind.MissingRequestHeaderException.class)
        public org.springframework.http.ResponseEntity<java.util.Map<String, Object>> h0(org.springframework.web.bind.MissingRequestHeaderException e) {
            return org.springframework.http.ResponseEntity
                    .status(org.springframework.http.HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("code", 9001, "message", "토큰이 없습니다."));
        }

        @org.springframework.web.bind.annotation.ExceptionHandler(InvalidTokenFormatException.class)
        org.springframework.http.ResponseEntity<java.util.Map<String, Object>> h1(InvalidTokenFormatException e) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("code", 8002, "message", e.getMessage()));
        }

        @org.springframework.web.bind.annotation.ExceptionHandler(TokenExpiredException.class)
        org.springframework.http.ResponseEntity<java.util.Map<String, Object>> h2(TokenExpiredException e) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED)
                    .body(java.util.Map.of("code", 8003, "message", e.getMessage()));
        }

        @org.springframework.web.bind.annotation.ExceptionHandler(ForbiddenAccessException.class)
        org.springframework.http.ResponseEntity<java.util.Map<String, Object>> h3(ForbiddenAccessException e) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN)
                    .body(java.util.Map.of("code", 7001, "message", e.getMessage()));
        }

        @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
        org.springframework.http.ResponseEntity<java.util.Map<String, Object>> h4(IllegalArgumentException e) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("code", 6001, "message", e.getMessage()));
        }
    }
}
