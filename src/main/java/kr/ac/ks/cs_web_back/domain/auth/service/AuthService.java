package kr.ac.ks.cs_web_back.domain.auth.service;

import kr.ac.ks.cs_web_back.domain.auth.dto.response.TokenResponse;
import kr.ac.ks.cs_web_back.domain.auth.model.Auth;
import kr.ac.ks.cs_web_back.domain.auth.repository.AuthRepository;
import kr.ac.ks.cs_web_back.global.security.JwtTokenProvider;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    AuthRepository authRepository;
    JwtTokenProvider jwtTokenProvider;

    public TokenResponse reissue(String oldRefreshToken) {
        jwtTokenProvider.validateRefreshToken(oldRefreshToken);
        String subject = jwtTokenProvider.getSubject(oldRefreshToken);
        String newAccess = jwtTokenProvider.createAccessToken(subject);
        String newRefresh = jwtTokenProvider.createRefreshToken(subject);
        return new TokenResponse(newAccess, newRefresh);
    }

    @Generated
    public AuthService(final AuthRepository authRepository, final JwtTokenProvider jwtTokenProvider) {
        this.authRepository = authRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
}