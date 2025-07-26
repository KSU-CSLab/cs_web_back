package kr.ac.ks.cs_web_back.domain.Member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.ks.cs_web_back.domain.Member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.global.swagger.error.ApiErrorResponse;
import kr.ac.ks.cs_web_back.global.swagger.error.ErrorCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "멤버 API", description = "회원 관리 API")
public interface SpringDocMemberController {

    @Operation(summary = "회원가입", description = "회원 가입")
    @ApiResponse(responseCode = "200", description = "회원가입에 성공했습니다.")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/member/register", errorCases = {
            @ErrorCase(description = "이메일 없음", exampleMessage = "이메일이 입력되지 않았습니다."),
            @ErrorCase(description = "비밀번호 없음", exampleMessage = "비밀번호가 입력되지 않았습니다."),
            @ErrorCase(description = "사용자명 없음", exampleMessage = "사용자명이 입력되지 않았습니다."),
            @ErrorCase(description = "전화번호 없음", exampleMessage = "전화번호가 입력되지 않았습니다."),
            @ErrorCase(description = "생년월일 없음", exampleMessage = "생년월일 입력되지 않았습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.CONFLICT, instance = "/member/register", errorCases = {
            @ErrorCase(description = "중복 이메일", exampleMessage = "이미 존재하는 이메일입니다."),
            @ErrorCase(description = "중복 사용자명", exampleMessage = "이미 존재하는 사용자명입니다.")
    })
    ResponseEntity<Void> createMember(
            @RequestBody MemberCreateRequest request
    );
}
