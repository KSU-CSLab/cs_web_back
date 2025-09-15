package kr.ac.ks.cs_web_back.domain.auth.controller.code;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {

    BAD_REQUEST_NO_EMAIL(9010, "이메일이 입력되지 않았습니다."),
    BAD_REQUEST_NO_PASSWORD(9010, "비밀번호가 입력되지 않았습니다."),
    UNAUTHORIZED_INVALID_TOKEN(8001, "유효하지 않은 토큰입니다."),
    UNAUTHORIZED_FAILED_VALIDATION(8002, "인증에 실패했습니다."),
    UNAUTHORIZED_PASSWORD(8011, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER(6001, "일치하는 사용자가 존재하지 않습니다."),
    BAD_REQUEST_NO_TOKEN(9001, "토큰이 없습니다."),
    UNAUTHORIZED_TOKEN_FORM(8002, "유효하지 않은 토큰 형식입니다."),
    UNAUTHORIZED_EXPIRED_TOKEN(8003, "만료된 토큰입니다."),
    UNAUTHORIZED_TOKEN_FAILURE(8004, "인증에 실패했습니다."),
    INTERNAL_SERVER_ERROR(5000, "서버에서 예기치 못한 오류가 발생했습니다.")
    ;

    private final int code;
    private final String message;
}
