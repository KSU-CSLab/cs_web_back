package kr.ac.ks.cs_web_back.domain.member.controller;

import jakarta.validation.Valid;
import kr.ac.ks.cs_web_back.domain.member.controller.code.MemberSuccessCode;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberWithdrawalRequest;
import kr.ac.ks.cs_web_back.domain.member.service.MemberService;
import kr.ac.ks.cs_web_back.global.response.CsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements SpringDocMemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CsResponse<Long> register(
            @Valid @RequestBody MemberCreateRequest request
    ) {
        Long id = memberService.createMember(request);
        return CsResponse.of(MemberSuccessCode.GENERATED_REGISTERED, id);
    }
    @PostMapping("/withdrawal")
    @ResponseStatus(HttpStatus.OK)
    public CsResponse<Long> withdraw(
            @Valid @RequestBody MemberWithdrawalRequest request
    ) {
        Long id = memberService.withdraw(request);
        return CsResponse.of(MemberSuccessCode.OK_WITHDRAWN, id);
    }
}