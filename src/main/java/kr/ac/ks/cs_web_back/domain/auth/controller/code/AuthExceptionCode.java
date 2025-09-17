package kr.ac.ks.cs_web_back.domain.auth.controller.code;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import lombok.Generated;

public enum AuthExceptionCode implements ExceptionCode {
    UNAUTHORIZED_INVALID_TYPE(8001, "유효하지 않은 토큰 형식입니다."),
    UNAUTHORIZED_CERTIFIED_FAILED(8002, "인증에 실패했습니다."),
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
