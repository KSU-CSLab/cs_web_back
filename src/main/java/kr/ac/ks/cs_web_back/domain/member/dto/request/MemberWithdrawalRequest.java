package kr.ac.ks.cs_web_back.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "회원탈퇴 요청 DTO")
public record MemberWithdrawalRequest(

        @Schema(description = "사용자명", example = "userNickName")
        @NotNull(message = "사용자명이 입력되지 않았습니다.")
        String Username,

        @Schema(description = "비밀번호", example = "examplePassword123")
        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        String password
){
        public Long username() {
        }
}