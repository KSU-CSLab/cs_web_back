package kr.ac.ks.cs_web_back.domain.post.service;

import kr.ac.ks.cs_web_back.domain.member.fixture.MemberFixture;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.post.dto.request.PostCreateRequest;
import kr.ac.ks.cs_web_back.domain.post.model.Post;
import kr.ac.ks.cs_web_back.domain.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kr.ac.ks.cs_web_back.domain.member.fixture.MemberFixture.memberFixture;
import static kr.ac.ks.cs_web_back.domain.post.fixture.PostFixture.createPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("게시글 작성 성공")
    void createPostSuccessfully() {
        // given
        Member member = memberFixture();
        PostCreateRequest request = new PostCreateRequest("testTitle", "Hello World");
        Post post = createPost(request.title(), request.content(), member);

        // postRepository.save()가 호출되면, 준비된 post 객체를 반환하도록 설정
        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        Long createdPostId = postService.createPost(request, String.valueOf(member));

        // then
        assertThat(createdPostId).isEqualTo(post.getId());
        verify(postRepository).save(any(Post.class)); // save 메소드가 호출되었는지 검증
    }
}