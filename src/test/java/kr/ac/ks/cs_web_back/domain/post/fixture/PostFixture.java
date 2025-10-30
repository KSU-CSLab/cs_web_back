package kr.ac.ks.cs_web_back.domain.post.fixture;

import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.post.model.Post;

public class PostFixture {
    public static Post createPost(
            final String title,
            final String content,
            final Member member
    ) {
        return Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }
}