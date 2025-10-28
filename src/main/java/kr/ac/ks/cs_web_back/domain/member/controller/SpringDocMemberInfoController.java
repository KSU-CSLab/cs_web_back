package kr.ac.ks.cs_web_back.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.ks.cs_web_back.domain.member.dto.response.MemberResponse;
import kr.ac.ks.cs_web_back.global.response.CsResponse;
import kr.ac.ks.cs_web_back.global.swagger.error.ApiErrorResponse;
import kr.ac.ks.cs_web_back.global.swagger.error.ErrorCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "유저 정보 조회 API", description = "사용자 정보 조회 관련 API")
public interface SpringDocMemberInfoController {

    @Operation(
            summary = "유저 정보 조회", description = "유저 정보 조회"
    )
    @Parameter(name = "Authorization", description = "accessToken", in = ParameterIn.HEADER, required = true, example = "accessToken")
    @Parameter(name = "userid", description = "조회할 사용자 ID (본인)", in = ParameterIn.PATH, required = true, example = "1")
    @ApiResponse(responseCode = "200", description = "유저 정보 조회에 성공했습니다.")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/member/profile/{userid}", errorCases = {
            @ErrorCase(description = "토큰 없음", code = 8001, exampleMessage = "토큰이 없습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/member/profile/{userid}", errorCases = {
            @ErrorCase(description = "유효하지 않은 토큰", code = 8001, exampleMessage = "유효하지 않은 토큰입니다."),
            @ErrorCase(description = "만료된 토큰", code = 8002, exampleMessage = "만료된 토큰입니다."),
            @ErrorCase(description = "인증 실패", code = 8002, exampleMessage = "인증에 실패했습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.FORBIDDEN, instance = "/member/profile/{userid}", errorCases = {
            @ErrorCase(description = "본인만 조회 가능", code = 7001, exampleMessage = "본인의 정보만 조회할 수 있습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.NOT_FOUND, instance = "/member/profile/{userid}", errorCases = {
            @ErrorCase(description = "사용자 없음", code = 6001, exampleMessage = "일치하는 사용자가 존재하지 않습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.INTERNAL_SERVER_ERROR, instance = "/member/profile/{userid}", errorCases = {
            @ErrorCase(description = "서버 에러", code = 5000, exampleMessage = "서버에서 예기치 못한 오류가 발생했습니다.")
    })
    CsResponse<MemberResponse> getMemberInfo(
            @RequestHeader("Authorization") String token,
            @PathVariable("userid") Long userId
    );
}
