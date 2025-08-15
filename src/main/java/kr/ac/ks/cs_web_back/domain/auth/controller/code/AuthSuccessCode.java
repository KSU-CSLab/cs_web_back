package kr.ac.ks.cs_web_back.domain.auth.controller.code;

import kr.ac.ks.cs_web_back.global.response.SuccessCode;
import lombok.Generated;

public enum AuthSuccessCode implements SuccessCode {
    OK_TOKEN_REISSUED(2005, "토큰 재발급에 성공했습니다."),
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
    private AuthSuccessCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

}
