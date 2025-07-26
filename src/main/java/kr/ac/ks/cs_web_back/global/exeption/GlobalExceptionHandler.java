package kr.ac.ks.cs_web_back.global.exeption;

import kr.ac.ks.cs_web_back.global.exeption.domain.*;
import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionCode;
import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(final Exception e) {
        System.out.printf("%s : %s\n", e.getClass(),  e.getMessage());

        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(5000, "알 수 없는 서버 에러가 발생했습니다."));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleException(final BadRequestException e) {
        System.out.printf("%s : %s\n", e.getClass(),  e.getMessage());

        final ExceptionCode exception = e.getExceptionCode();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleException(final UnauthorizedException e) {
        System.out.printf("%s : %s\n", e.getClass(),  e.getMessage());

        final ExceptionCode exception = e.getExceptionCode();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleException(final ForbiddenException e) {
        System.out.printf("%s : %s\n", e.getClass(),  e.getMessage());

        final ExceptionCode exception = e.getExceptionCode();
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(final NotFoundException e) {
        System.out.printf("%s : %s\n", e.getClass(),  e.getMessage());

        final ExceptionCode exception = e.getExceptionCode();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ExceptionResponse> handleException(final ConflictException e) {
        System.out.printf("%s : %s\n", e.getClass(),  e.getMessage());

        final ExceptionCode exception = e.getExceptionCode();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage()));
    }

}
