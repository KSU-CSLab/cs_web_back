package kr.ac.ks.cs_web_back.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ac.ks.cs_web_back.domain.post.dto.request.PostCreateRequest;
import kr.ac.ks.cs_web_back.global.response.CsResponse;
import kr.ac.ks.cs_web_back.global.swagger.error.ApiErrorResponse;
import kr.ac.ks.cs_web_back.global.swagger.error.ErrorCase;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "게시글 API", description = "게시글 관련 API")
public interface SpringDocPostController {

    @Operation(summary = "게시글 작성", description = "새로운 게시글을 생성합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201", description = "게시글 작성에 성공했습니다.")
    @ApiErrorResponse(status = HttpStatus.BAD_REQUEST, instance = "/post", errorCases = {
            @ErrorCase(description = "제목 없음", code = 9010, exampleMessage = "제목이 입력되지 않았습니다."),
            @ErrorCase(description = "내용 없음", code = 9011, exampleMessage = "내용이 입력되지 않았습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.UNAUTHORIZED, instance = "/post", errorCases = {
            @ErrorCase(description = "유효하지 않은 토큰", code = 8001, exampleMessage = "유효하지 않은 토큰입니다."),
            @ErrorCase(description = "인증 실패", code = 8002, exampleMessage = "인증에 실패했습니다.")
    })
    @ApiErrorResponse(status = HttpStatus.FORBIDDEN, instance = "/post", errorCases = {
            @ErrorCase(description = "권한 없음", code = 7003, exampleMessage = "게시글 작성이 제한된 사용자입니다.")
    })
    CsResponse<Long> createPost(
            @RequestBody PostCreateRequest request,
            UserDetails userDetails
    );
}