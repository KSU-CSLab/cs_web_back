package kr.ac.ks.cs_web_back.domain.auth.controller;

import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthSuccessCode;
import kr.ac.ks.cs_web_back.domain.auth.dto.response.TokenResponse;
import kr.ac.ks.cs_web_back.domain.auth.service.AuthService;
import kr.ac.ks.cs_web_back.global.response.CsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping({"/reissue"})
    public CsResponse reissue(@RequestHeader("Authorization-Refresh") String oldRefreshToken) {
        // "Bearer" 접두사 제거
        if (oldRefreshToken.startsWith("Bearer ")) {
            oldRefreshToken = oldRefreshToken.substring(7);
        }

        TokenResponse tokens = this.authService.reissue(oldRefreshToken);
        return CsResponse.of(AuthSuccessCode.OK_TOKEN_REISSUED, tokens);
    }
}
