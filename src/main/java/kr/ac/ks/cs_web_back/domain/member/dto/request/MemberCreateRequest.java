package kr.ac.ks.cs_web_back.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "회원가입")
public record MemberCreateRequest(
        @Schema(description = "이메일", example = "example@ks.ac.kr")
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @Schema(description = "비밀번호", example = "examplePassword123")
        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        @Size(max = 48, message = "비밀번호는 48자 이상일 수 없습니다.")
        String password,

        @Schema(description = "사용자명", example = "userNickName")
        @NotBlank(message = "사용자명이 입력되지 않았습니다.")
        @Size(max = 12, message = "사용자명은 12자 이상일 수 없습니다.")
        String username
) {
}
