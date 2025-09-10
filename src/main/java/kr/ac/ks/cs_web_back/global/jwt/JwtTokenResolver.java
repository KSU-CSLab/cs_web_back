package kr.ac.ks.cs_web_back.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenResolver {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String HEADER_PREFIX = "Bearer ";

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(HEADER_PREFIX)) {
            return token.substring(HEADER_PREFIX.length());
        }

        return null;
    }

}
