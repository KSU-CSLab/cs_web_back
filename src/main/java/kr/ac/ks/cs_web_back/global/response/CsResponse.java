package kr.ac.ks.cs_web_back.global.response;

import jakarta.annotation.Nullable;

public record CsResponse<T>(
        int code,
        String message,
        @Nullable T data
) {
    public static <T> CsResponse<T> of(final SuccessCode code) {
        return new CsResponse<>(code.getCode(), code.getMessage(), null);
    }
    public static <T> CsResponse<T> of(final SuccessCode code, T data) {
        return new CsResponse<>(code.getCode(), code.getMessage(), data);
    }
}
