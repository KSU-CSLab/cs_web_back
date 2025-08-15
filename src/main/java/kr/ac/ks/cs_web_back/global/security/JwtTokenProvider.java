package kr.ac.ks.cs_web_back.global.security;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    public JwtTokenProvider() {
    }

    public void validateRefreshToken(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidFormat();
        }
    }

    public String getSubject(String token) {
        return "memberId-or-username";
    }

    public String createAccessToken(String subject) {
        return "new_accessToken";
    }

    public String createRefreshToken(String subject) {
        return "new_refreshToken";
    }

    public static class InvalidFormat extends RuntimeException {
        public InvalidFormat() {
        }
    }

    public static class Expired extends RuntimeException {
        public Expired() {
        }
    }
}
