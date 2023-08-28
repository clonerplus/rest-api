package ir.sobhan.restapi.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToken(String tokenKey, String tokenValue, long expiration) {
        redisTemplate.opsForValue().set(tokenKey, tokenValue, expiration, TimeUnit.MILLISECONDS);
    }

    public Optional<String> getToken(String tokenKey) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(tokenKey));
    }

    public void deleteToken(String tokenKey) {
        redisTemplate.delete(tokenKey);
    }
}

