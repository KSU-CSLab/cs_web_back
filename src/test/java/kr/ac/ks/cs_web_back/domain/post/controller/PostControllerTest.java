package kr.ac.ks.cs_web_back.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import kr.ac.ks.cs_web_back.domain.post.dto.request.PostCreateRequest;
import kr.ac.ks.cs_web_back.domain.post.service.PostService;
import kr.ac.ks.cs_web_back.global.config.SecurityConfig;
import kr.ac.ks.cs_web_back.global.config.WebConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@Import({SecurityConfig.class, WebConfig.class})
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private MemberRepository memberRepository;

    @Test
    @WithMockUser
    @DisplayName("게시글 작성 성공: 게시글 작성에 성공하면 201 Created를 반환한다.")
    void createpostSuccessfullyReturns201Created() throws Exception {
        // given
        PostCreateRequest request = new PostCreateRequest(
                "testTitle",
                "Hello World"
        );
        given(postService.createPost(any(), any())).willReturn(1L);

        // when & then
        mockMvc.perform(post("/post/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("게시글 작성 실패: 제목이 없으면 400 Bad Request를 반환한다.")
    public void createpostNoTitleReturns400BadRequest() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest(
                "",
                "Hello World"
        );

        // when & then
        mockMvc.perform(post("/post/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 작성 실패: 내용이 없으면 400 Bad Request를 반환한다.")
    public void createpostNoContentReturns400BadRequest() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest(
                "testTitle",
                ""
        );

        // when & then
        mockMvc.perform(post("/post/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 작성 실패: 유효하지 않은 토큰일 경우 401 Unauthrized를 반환한다.")
    public void createpostInvalidTokenReturns401Unauthorized() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest(
                "testTitle",
                "Hello World"
        );

        // when & then
        mockMvc.perform(post("/post/")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("게시글 작성 실패: 인증에 실패하였을 경우 401 Unauthrized를 반환한다,")
    public void createpostFailedValidationReturns401Unauthorized() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest(
                "testTitle",
                "Hello World"
        );

        // when & then
        mockMvc.perform(post("/post/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("게시글 작성 실패: 게시글 작성이 제한된 사용자 일 경우 403 Forbidden을 반환한다.")
    public void createpostForbiddenUserReturns403Forbidden() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest(
                "testTitle",
                "Hello World"
        );

        // when & then
        mockMvc.perform(post("/post/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("게시글 작성 실패: 서버에서 예기치 못한 오류 발생했을 경우 500 Internal Server Error를 반환한다.")
    public void createpostServerErrorReturns500InternalServerError() throws Exception {
        //given
        PostCreateRequest request = new PostCreateRequest(
                "testTitle",
                "Hello World"
        );

        // when & then
        mockMvc.perform(post("/post/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}