package kr.ac.ks.cs_web_back.domain.auth.service;

import kr.ac.ks.cs_web_back.domain.auth.dto.response.TokenResponse;
import kr.ac.ks.cs_web_back.domain.auth.model.Auth;
import kr.ac.ks.cs_web_back.domain.auth.repository.AuthRepository;
import kr.ac.ks.cs_web_back.global.security.JwtTokenProvider;
import lombok.Generated;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    AuthRepository authRepository;
    JwtTokenProvider jwtTokenProvider;

    public void createAuth(String username, String password) {
        Auth auth = Auth.of(username, password);
        this.authRepository.save(auth);
    }

    public TokenResponse reissue(String oldRefreshToken) {
        this.jwtTokenProvider.validateRefreshToken(oldRefreshToken);
        String subject = this.jwtTokenProvider.getSubject(oldRefreshToken);
        String newAccess = this.jwtTokenProvider.createAccessToken(subject);
        String newRefresh = this.jwtTokenProvider.createRefreshToken(subject);
        return new TokenResponse(newAccess, newRefresh);
    }

    @Generated
    public AuthService(final AuthRepository authRepository, final JwtTokenProvider jwtTokenProvider) {
        this.authRepository = authRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
}