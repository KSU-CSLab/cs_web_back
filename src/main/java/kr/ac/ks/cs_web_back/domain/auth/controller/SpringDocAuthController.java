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

}
