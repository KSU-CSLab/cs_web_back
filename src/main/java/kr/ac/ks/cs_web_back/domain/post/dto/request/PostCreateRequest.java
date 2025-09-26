package kr.ac.ks.cs_web_back.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "게시글 생성")
public record PostCreateRequest(

        @Schema(description = "게시글 내용", example = "감자칩에 밥 비벼먹어 본사람")
        @NotBlank(message = "제목이 입력되지 않았습니다.")
        String title,

        @Schema(description = "게시글 내용", example = "일단 나부터")
        @NotBlank(message = "내용이 입력되지 않았습니다.")
        String content
) {
}