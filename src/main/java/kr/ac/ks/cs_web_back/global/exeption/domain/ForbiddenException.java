package kr.ac.ks.cs_web_back.global.exeption.domain;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

    ExceptionCode exceptionCode;

    public ForbiddenException(ExceptionCode code) {
        super(code.getMessage());
        this.exceptionCode = code;
    }
}
