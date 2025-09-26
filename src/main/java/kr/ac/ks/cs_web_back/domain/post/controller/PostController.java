package kr.ac.ks.cs_web_back.domain.post.controller;

import jakarta.validation.Valid;
import kr.ac.ks.cs_web_back.domain.post.controller.code.PostSuccessCode;
import kr.ac.ks.cs_web_back.domain.post.dto.request.PostCreateRequest;
import kr.ac.ks.cs_web_back.domain.post.service.PostService;
import kr.ac.ks.cs_web_back.global.response.CsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController implements SpringDocPostController {

    private final PostService postService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CsResponse<Long> createPost(
            @Valid @RequestBody PostCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userEmail = userDetails.getUsername();
        Long postId = postService.createPost(request, userEmail);
        return CsResponse.of(PostSuccessCode.CREATE_POST_SUCCESS, postId);
    }
}