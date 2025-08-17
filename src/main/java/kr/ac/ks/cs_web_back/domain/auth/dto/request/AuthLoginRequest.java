package kr.ac.ks.cs_web_back.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @Schema(description = "이메일", example = "example@ks.ac.kr")
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @Schema(description = "비밀번호", example = "examplePassword123")
        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        String password
) {}
