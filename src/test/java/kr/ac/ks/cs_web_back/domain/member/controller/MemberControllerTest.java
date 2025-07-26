package kr.ac.ks.cs_web_back.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.ks.cs_web_back.domain.Member.controller.MemberController;
import kr.ac.ks.cs_web_back.domain.Member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.Member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입에 성공하면 200 Ok를 반환한다.")
    public void RegisteredSuccessfullyReturns200Ok() throws Exception {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "validEmail123@ks.ac.kr",
                "validPassword1234",
                "exampleUsername",
                "2002.09.05",
                "010-1234-5678"
        );

        // when & then
        mockMvc.perform(post("/member/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

}
