package kr.ac.ks.cs_web_back.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "회원가입")
public record MemberCreateRequest(
        @Schema(description = "이메일", example = "example@ks.ac.kr")
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        String email,

        @Schema(description = "비밀번호", example = "examplePassword123")
        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        @Size(max = 24, message = "비밀번호는 24자 내로 입력해 주세요.")
        String password,

        @Schema(description = "사용자명", example = "userNickName")
        @NotBlank(message = "사용자명이 입력되지 않았습니다.")
        @Size(max = 12, message = "유저명은 12자 내로 입력해 주세요.")
        String username,

        @Schema(description = "생년월일", example = "2002-09-05")
        @NotBlank(message = "생년월일이 입력되지 않았습니다.")
        LocalDate birthdate,

        @Schema(description = "전화번호", example = "055-663-5140")
        @NotBlank(message = "전화번호가 입력되지 않았습니다.")
        String number
) {
}
