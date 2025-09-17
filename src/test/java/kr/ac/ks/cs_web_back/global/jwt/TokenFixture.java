package kr.ac.ks.cs_web_back.global.jwt;

public class TokenFixture {
    private static final String TEST_EMAIL = "example@ks.ac.kr";

    /**
     * 테스트용 Access Token 생성
     * @param jwtUtil JwtUtil 인스턴스
     * @return 생성된 Access Token
     */
    public static String createAccessToken(JwtUtil jwtUtil) {
        return jwtUtil.generateAccessToken(TEST_EMAIL);
    }

    public static String createAccessToken(JwtUtil jwtUtil, String email) {
        return jwtUtil.generateAccessToken(email);
    }

    /**
     * 테스트용 Refresh Token 생성
     * @param jwtUtil JwtUtil 인스턴스
     * @return 생성된 Refresh Token
     */
    public static String createRefreshToken(JwtUtil jwtUtil) {
        return jwtUtil.generateRefreshToken(TEST_EMAIL);
    }

    public static String createRefreshToken(JwtUtil jwtUtil, String email) {
        return jwtUtil.generateRefreshToken(email);
    }
}
