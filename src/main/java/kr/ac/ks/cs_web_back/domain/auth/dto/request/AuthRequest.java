package kr.ac.ks.cs_web_back.domain.auth.dto.request;

public record AuthRequest(String username, String password) {
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String username() {
        return this.username;
    }

    public String password() {
        return this.password;
    }
}