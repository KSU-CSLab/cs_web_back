package kr.ac.ks.cs_web_back.domain.post.controller.code;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostExceptionCode implements ExceptionCode {

    BAD_REQUEST_NO_TITLE(9010, "제목이 입력되지 않았습니다."),
    BAD_REQUEST_NO_CONTENT(9010, "내용이 입력되지 않았습니다."),
    UNAUTHRIZED_INVALID_TOKKEN(8001, "유효하지 않은 토큰입니다."),
    UNAUTHRIZED_FAILED_VALIDATION(8002, "인증에 실패했습니다."),
    FORBIDDEN_CREATE_POST(7003, "게시글 작성이 제한된 사용자입니다."),
    INTERNAL_SERVER_ERROR_UNEXPECTED(5000, "서버에서 예기치 못한 오류가 발생했습니다."),

    ;

    private final int code;
    private final String message;
}