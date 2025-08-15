package kr.ac.ks.cs_web_back.domain.auth.dto.response;

import kr.ac.ks.cs_web_back.domain.member.model.Member;

public record AuthResponse(Long id, String username) {
    public AuthResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static AuthResponse from(Member member) {
        return new AuthResponse(member.getId(), member.getUsername());
    }

    public Long id() {
        return this.id;
    }

    public String username() {
        return this.username;
    }
}
