package kr.ac.ks.cs_web_back.global.jwt;

import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomAuthenticationEntryPointTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("인증되지 않은 사용자가 보호된 리소스에 접근 시 401 Unauthorized 응답을 받는다.")
    void shouldReturn401UnauthorizedWhenUnauthenticatedAccessToSecuredResource() throws Exception {
        mockMvc.perform(get("/protected/securedResource"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.code").value(AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN.getCode()))
                .andExpect(jsonPath("$.message").value(AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN.getMessage()));
    }
}
