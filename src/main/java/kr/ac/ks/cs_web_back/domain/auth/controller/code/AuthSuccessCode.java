package kr.ac.ks.cs_web_back.domain.auth.controller.code;

import kr.ac.ks.cs_web_back.global.response.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthSuccessCode implements SuccessCode {

    LOGIN_SUCCESS(2001, "로그인에 성공했습니다."),
    LOGOUT_SUCCESS(2002, "로그아웃에 성공했습니다."),
    ;

    private final int code;
    private final String message;
}
