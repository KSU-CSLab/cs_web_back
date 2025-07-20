package kr.ac.ks.cs_web_back.domain.Member.controller;

import jakarta.validation.Valid;
import kr.ac.ks.cs_web_back.domain.Member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements SpringDocMemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Void> createMember(
            @Valid @RequestBody MemberCreateRequest request
    ) {
        memberService.createMember(request.email(), request.password(), request.username(), request.birthdate(), request.number());
        return ResponseEntity.ok().build();
    }
}
