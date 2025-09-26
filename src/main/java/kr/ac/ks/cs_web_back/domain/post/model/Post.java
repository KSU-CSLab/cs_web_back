package kr.ac.ks.cs_web_back.domain.post.model;

import jakarta.persistence.*;
import kr.ac.ks.cs_web_back.domain.common.BaseEntity;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.post.dto.request.PostCreateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Builder
    private Post(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public static Post of(PostCreateRequest request, Member member) {
        return Post.builder()
                .member(member)
                .title(request.title())
                .content(request.content())
                .build();
    }
}