package kr.ac.ks.cs_web_back.domain.auth.controller;

import jakarta.validation.Valid;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthSuccessCode;
import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;
import kr.ac.ks.cs_web_back.domain.auth.dto.response.AuthLoginResponse;
import kr.ac.ks.cs_web_back.domain.auth.service.AuthService;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.global.annotation.IdentifiedUser;
import kr.ac.ks.cs_web_back.global.response.CsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements SpringDocAuthController{

    private final AuthService authService;

    @PostMapping("/login")
    public CsResponse<AuthLoginResponse> login(
            @Valid @RequestBody AuthLoginRequest request
    ) {
        AuthLoginResponse token = authService.loginMember(request);
        return CsResponse.of(AuthSuccessCode.LOGIN_SUCCESS, token);
    }

    @PostMapping("/logout")
    public CsResponse<Void> logout (
            @Valid @RequestHeader("Authorization") String authorization,
            @IdentifiedUser Member member
    ) {
        authService.logout(authorization, member.getEmail());
        return CsResponse.of(AuthSuccessCode.LOGOUT_SUCCESS);
    }
}
