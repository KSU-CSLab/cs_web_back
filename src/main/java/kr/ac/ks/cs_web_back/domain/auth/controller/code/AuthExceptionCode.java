package kr.ac.ks.cs_web_back.domain.auth.controller.code;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import lombok.Generated;

public enum AuthExceptionCode implements ExceptionCode {
    BAD_REQUEST_NO_REFRESH(9001, "토큰이 없습니다."),
    UNAUTHORIZED_INVALID_FORMAT(8002, "유효하지 않은 토큰 형식입니다."),
    UNAUTHORIZED_EXPIRED(8003, "만료된 토큰입니다."),
    INTERNAL_SERVER_ERROR(5000, "서버에서 예기치 못한 오류가 발생했습니다.")
    ;

    private int code;
    private String message;

    @Generated
    public int getCode() {
        return this.code;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    private AuthExceptionCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
