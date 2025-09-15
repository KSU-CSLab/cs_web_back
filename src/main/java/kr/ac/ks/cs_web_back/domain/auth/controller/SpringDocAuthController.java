package kr.ac.ks.cs_web_back.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;
import kr.ac.ks.cs_web_back.domain.auth.dto.response.AuthLoginResponse;
import kr.ac.ks.cs_web_back.global.response.CsResponse;
import kr.ac.ks.cs_web_back.global.swagger.error.ApiErrorResponse;
import kr.ac.ks.cs_web_back.global.swagger.error.ErrorCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "인증 API", description = "인증 관련 API")
public interface SpringDocAuthController {

    @Operation(summary = "로그인", description = "사용자의 이메일과 비밀번호로 로그인하고, 성공시 JWT 토큰을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "로그인에 성공했습니다.")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/auth/login", errorCases = {
            @ErrorCase(description = "이메일 없음", code = 9010, exampleMessage = "이메일이 입력되지 않았습니다."),
            @ErrorCase(description = "비밀번호 없음", code = 9011, exampleMessage = "비밀번호가 입력되지 않았습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/auth/login", errorCases = {
            @ErrorCase(description = "비밀번호 불일치", code = 8011, exampleMessage = "비밀번호가 일치하지 않습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.NOT_FOUND, instance = "/auth/login", errorCases = {
            @ErrorCase(description = "사용자 없음", code = 6001, exampleMessage = "일치하는 사용자가 존재하지 않습니다.")
    })
    CsResponse<AuthLoginResponse> login(
            @RequestBody AuthLoginRequest request
    );
    @Operation(summary = "로그아웃", description = "사용자의 JWT토큰을 받아 형식을 검사하고 성공시 토큰을 만료시킵니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃에 성공했습니다.")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/auth/logout", errorCases = {
            @ErrorCase(description = "토큰 없음", code = 9001, exampleMessage = "토큰이 없습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/auth/logout", errorCases = {
            @ErrorCase(description = "유효하지 않은 토큰", code = 8002, exampleMessage = "유효하지 않은 토큰 형식입니다."),
            @ErrorCase(description = "만료된 토큰", code = 8003, exampleMessage = "만료된 토큰입니다."),
            @ErrorCase(description = "인증 실패", code = 8004, exampleMessage = "인증에 실패했습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.INTERNAL_SERVER_ERROR, instance = "/auth/logout", errorCases = {
            @ErrorCase(description = "서버 오류", code = 5000, exampleMessage = "서버에서 예기치 못한 오류가 발생했습니다.")
    })
    CsResponse<Void> logout(
            @RequestHeader("Authorization") String authorizationHeader
    );
}
