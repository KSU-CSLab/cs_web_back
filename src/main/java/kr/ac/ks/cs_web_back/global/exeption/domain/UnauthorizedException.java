package kr.ac.ks.cs_web_back.global.exeption.domain;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

    ExceptionCode exceptionCode;

    public UnauthorizedException(ExceptionCode code) {
        super(code.getMessage());
        this.exceptionCode = code;
    }
}
