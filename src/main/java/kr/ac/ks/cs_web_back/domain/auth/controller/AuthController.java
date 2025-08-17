package kr.ac.ks.cs_web_back.domain.auth.controller;

import jakarta.validation.Valid;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthSuccessCode;
import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;
import kr.ac.ks.cs_web_back.domain.auth.service.AuthService;
import kr.ac.ks.cs_web_back.global.response.CsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements SpringDocAuthController{

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public CsResponse<String> login(
            @Valid @RequestBody AuthLoginRequest request
    ) {
        String token = authService.loginMember(request);
        return CsResponse.of(AuthSuccessCode.LOGIN_SUCCESS, token);
    }
}
