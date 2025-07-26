package kr.ac.ks.cs_web_back.global.exeption.domain;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    ExceptionCode exceptionCode;

    public NotFoundException(ExceptionCode code) {
        super(code.getMessage());
        this.exceptionCode = code;
    }
}
