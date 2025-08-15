package kr.ac.ks.cs_web_back.domain.auth.dto.request;

public record ReissueRequest(String refreshToken) {
    public ReissueRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String refreshToken() {
        return this.refreshToken;
    }
}

