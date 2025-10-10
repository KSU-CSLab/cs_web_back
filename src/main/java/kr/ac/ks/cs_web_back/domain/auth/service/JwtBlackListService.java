package kr.ac.ks.cs_web_back.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtBlackListService {

    private final RedisTemplate<String, String> redisTemplate;

    public void blacklist(String accessToken, long remainingTime) {
        redisTemplate.opsForValue().set(
                accessToken,
                "logout",
                remainingTime,
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isBlackListed(String accessToken) {
        return redisTemplate.opsForValue().get(accessToken) != null;
    }
}
