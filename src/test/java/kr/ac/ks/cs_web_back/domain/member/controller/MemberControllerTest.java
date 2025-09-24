package kr.ac.ks.cs_web_back.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.member.fixture.MemberFixture;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.service.MemberService;
import kr.ac.ks.cs_web_back.global.jwt.JwtTokenResolver;
import kr.ac.ks.cs_web_back.global.jwt.JwtUtil;
import kr.ac.ks.cs_web_back.global.resolver.IdentifiedUserArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@WebMvcTest(controllers = MemberController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class MemberControllerTest {

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtTokenResolver jwtTokenResolver;

    @MockitoBean
    private IdentifiedUserArgumentResolver identifiedUserArgumentResolver;

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
    @DisplayName("회원탈퇴 성공: 회원 탈퇴에 성공하면 200 Ok를 반환한다.")
    public void withdrawalSuccessfullyReturns200Ok() throws Exception {
        // given
        String token = "Bearer valid-access-token";
        Member member = MemberFixture.memberFixture();

        given(identifiedUserArgumentResolver.resolveArgument(any(), any(), any(), any())).willReturn(member);
        doNothing().when(memberService).deleteMember(member);

        // when & then
        mockMvc.perform(post("/member/withdrawal")
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }
}
