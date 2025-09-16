package kr.ac.ks.cs_web_back.global.exeption;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GlobalExceptionCode implements ExceptionCode {
    UNAUTHORIZED_NO_TOKEN(8001, "토큰이 없습니다."),
    UNAUTHORIZED_INVALID_TOKEN(8002, "유효하지 않은 토큰 형식입니다."),
    UNAUTHORIZED_EXPIRED_TOKEN(8003, "만료된 토큰입니다."),
    UNAUTHORIZED_FAILED_VALIDATION(8004, "인증에 실패했습니다."),
    ;

    private final int code;
    private final String message;
}
