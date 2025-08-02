package kr.ac.ks.cs_web_back.domain.member.controller.code;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberExceptionCode implements ExceptionCode {

    BAD_REQUEST_NO_EMAIL(9010, "이메일이 입력되지 않았습니다."),
    BAD_REQUEST_NO_PASSWORD(9011, "비밀번호가 입력되지 않았습니다."),
    BAD_REQUEST_NO_USERNAME(9012, "사용자명이 입력되지 않았습니다."),
    CONFLICT_EMAIL(5010, "이미 존재하는 이메일입니다."),
    CONFLICT_USERNAME(5012, "이미 존재하는 사용자명입니다."),
    ;

    private final int code;
    private final String message;
}
