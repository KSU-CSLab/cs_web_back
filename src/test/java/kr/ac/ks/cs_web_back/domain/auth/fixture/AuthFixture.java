package kr.ac.ks.cs_web_back.domain.auth.fixture;

import kr.ac.ks.cs_web_back.domain.auth.dto.request.AuthLoginRequest;

public class AuthFixture {
    public static AuthLoginRequest successLoginRequest() {
        return new AuthLoginRequest(
                "example@ks.ac.kr",
                "examplePassword1234!"
        );
    }
    public static AuthLoginRequest wrongPasswordLoginRequest() {
        return new AuthLoginRequest(
                "example@ks.ac.kr",
                "wrongPassword1234!"
        );
    }
    public static AuthLoginRequest NonExistentEmailLoginRequest() {
        return new AuthLoginRequest(
                "nonexistent@ks.ac.kr",
                "wrongPassword1234!"
        );
    }
}
