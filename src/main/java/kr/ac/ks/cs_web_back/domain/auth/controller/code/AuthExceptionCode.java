package kr.ac.ks.cs_web_back.domain.auth.controller.code;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {

    BAD_REQUEST_NO_EMAIL(9010, "이메일이 입력되지 않았습니다."),
    BAD_REQUEST_NO_PASSWORD(9010, "비밀번호가 입력되지 않았습니다."),

    UNAUTHORIZED_PASSWORD(8011, "이메일 또는 비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED_INVALID_TOKEN(8001, "유효하지 않은 토큰입니다."),
    UNAUTHORIZED_FAILED_VALIDATION(8002, "인증에 실패했습니다."),
    ;

    private final int code;
    private final String message;
}
