package kr.ac.ks.cs_web_back.domain.post.controller.code;

import kr.ac.ks.cs_web_back.global.response.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostSuccessCode implements SuccessCode {
    CREATE_POST_SUCCESS(2203, "게시글 작성에 성공했습니다.");

    private final int code;
    private final String message;
}