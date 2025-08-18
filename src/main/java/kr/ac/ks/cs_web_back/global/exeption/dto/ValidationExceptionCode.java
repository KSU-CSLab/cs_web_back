package kr.ac.ks.cs_web_back.global.exeption.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ValidationExceptionCode implements ExceptionCode {

    NOT_BLANK("NotBlank", 9010, "공백일 수 없습니다."),
    NOT_NULL("NotNull", 9011, "Null일 수 없습니다."),
    NOT_EMPTY("NotEmpty", 9012, "비어있을 수 없습니다."),
    EMAIL("Email", 9020, "유효한 이메일 형식이 아닙니다."),
    SIZE("Size", 9030, "크기가 지정된 범위를 벗어났습니다."),
    MAX("Max", 9031, "지정된 최댓값보다 큽니다."),
    MIN("Min", 9032, "지정된 최솟값보다 작습니다."),

    // 기본값
    DEFAULT("Default", 9000, "유효성 검사에 실패했습니다.");

    private final String annotationName;
    private final int code;
    private final String message;

    public static ValidationExceptionCode findByAnnotationName(String name) {
        return Arrays.stream(values())
                .filter(code -> code.annotationName.equals(name))
                .findFirst()
                .orElse(DEFAULT);
    }
}
