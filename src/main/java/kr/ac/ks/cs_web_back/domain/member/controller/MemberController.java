package kr.ac.ks.cs_web_back.domain.member.controller;

import jakarta.validation.Valid;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import org.springframework.http.HttpStatus;
import kr.ac.ks.cs_web_back.domain.member.controller.code.MemberSuccessCode;
import kr.ac.ks.cs_web_back.domain.member.dto.response.MemberResponse;
import kr.ac.ks.cs_web_back.domain.member.service.MemberService;
import kr.ac.ks.cs_web_back.global.response.CsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements SpringDocMemberInfoController,SpringDocMemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CsResponse<Long> register(
            @Valid @RequestBody MemberCreateRequest request
    ) {
        Long id = memberService.createMember(request);
        return CsResponse.of(MemberSuccessCode.GENERATED_REGISTERED, id);
    }

    @GetMapping("/profile/{userid}")
    public CsResponse<MemberResponse> getMemberInfo(
            @RequestHeader("Authorization") String token,
            @PathVariable("userid") Long userId
    ) {
        MemberResponse response = memberService.getMemberInfo(userId);
        return CsResponse.of(MemberSuccessCode.OK_FOUND_USER_PROFILE, response);
    }

}
