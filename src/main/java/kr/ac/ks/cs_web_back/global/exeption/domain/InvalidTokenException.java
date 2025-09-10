package kr.ac.ks.cs_web_back.global.exeption.domain;

import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;

public class InvalidTokenException extends UnauthorizedException {
  public InvalidTokenException(final ExceptionCode exception) {
    super(exception);
  }
}
