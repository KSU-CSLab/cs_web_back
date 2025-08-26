package kr.ac.ks.cs_web_back.domain.auth.dto.response;

public record TokenResponse(String accessToken, String refreshToken) {
    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}