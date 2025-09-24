package kr.ac.ks.cs_web_back.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.global.annotation.IdentifiedUser;
import kr.ac.ks.cs_web_back.global.response.CsResponse;
import kr.ac.ks.cs_web_back.global.swagger.error.ApiErrorResponse;
import kr.ac.ks.cs_web_back.global.swagger.error.ErrorCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "멤버 API", description = "회원 관리 API")
public interface SpringDocMemberController {

    @Operation(summary = "회원가입", description = "회원 가입")
    @ApiResponse(responseCode = "201", description = "회원가입에 성공했습니다.")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/member/register", errorCases = {
            @ErrorCase(description = "이메일 없음", code = 9010, exampleMessage = "이메일이 입력되지 않았습니다."),
            @ErrorCase(description = "비밀번호 없음", code = 9011, exampleMessage = "비밀번호가 입력되지 않았습니다."),
            @ErrorCase(description = "사용자명 없음", code = 9012, exampleMessage = "사용자명이 입력되지 않았습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.CONFLICT, instance = "/member/register", errorCases = {
            @ErrorCase(description = "중복 이메일", code = 5010, exampleMessage = "이미 존재하는 이메일입니다."),
            @ErrorCase(description = "중복 사용자명", code = 5011, exampleMessage = "이미 존재하는 사용자명입니다.")
    })
    CsResponse<Long> register(
            @RequestBody MemberCreateRequest request
    );

    @Operation(summary = "회원탈퇴", description = "회원 탈퇴")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴에 성공했습니다.")
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/member/withdrawal", errorCases = {
            @ErrorCase(description = "토큰 형식 이상", code = 8001, exampleMessage = "유효하지 않은 토큰입니다."),
            @ErrorCase(description = "인증 실패 (토큰 만료 혹은 틀린 이메일 / 비밀번호)", code = 8002, exampleMessage = "인증에 실패했습니다.")
    })
    CsResponse<Void> withdraw(
            @Parameter(hidden = true) @IdentifiedUser Member member
    );
}
