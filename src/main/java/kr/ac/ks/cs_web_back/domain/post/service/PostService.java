package kr.ac.ks.cs_web_back.domain.post.service;

import kr.ac.ks.cs_web_back.domain.member.controller.code.MemberExceptionCode;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import kr.ac.ks.cs_web_back.domain.post.dto.request.PostCreateRequest;
import kr.ac.ks.cs_web_back.domain.post.model.Post;
import kr.ac.ks.cs_web_back.domain.post.repository.PostRepository;
import kr.ac.ks.cs_web_back.global.exeption.domain.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Long createPost(final PostCreateRequest request, final String userEmail) {
        final Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(MemberExceptionCode.NOT_FOUND_USER));

        final Post post = Post.of(request, member);

        return postRepository.save(post).getId();
    }
}