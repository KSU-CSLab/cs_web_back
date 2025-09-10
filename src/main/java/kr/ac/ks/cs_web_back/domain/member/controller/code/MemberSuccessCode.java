package kr.ac.ks.cs_web_back.domain.member.controller.code;

import kr.ac.ks.cs_web_back.global.response.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberSuccessCode implements SuccessCode {

    GENERATED_REGISTERED(2101, "회원가입에 성공했습니다."),
    OK_WITHDRAWN(2102,"회원 탈퇴에 성공했습니다."),
    OK_FOUND_USER_PROFILE(2103, "유저 정보 조회에 성공했습니다."),
    OK_MODIFIED_USER_PROFILE(2104, "사용자 정보 변경에 성공했습니다."),
    OK_FOUND_USER_WRITTEN_POSTS(2105, "해당 사용자가 작성한 게시글을 조회했습니다."),
    OK_FOUND_USER_WRITTEN_COMMENTS(2106, "해당 사용자가 작성한 댓글을 조회했습니다."),
    ;


    private final int code;
    private final String message;
}
