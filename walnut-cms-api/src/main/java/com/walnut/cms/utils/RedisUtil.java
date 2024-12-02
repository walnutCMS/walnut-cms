package com.walnut.cms.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private static final String CAPTCHA_PREFIX = "captcha:";
    private static final long CAPTCHA_EXPIRE = 5; // 验证码5分钟过期
    private final StringRedisTemplate redisTemplate;

    public void setCaptcha(String key, String code) {
        redisTemplate.opsForValue().set(CAPTCHA_PREFIX + key, code, CAPTCHA_EXPIRE, TimeUnit.MINUTES);
    }

    public String getCaptcha(String key) {
        return redisTemplate.opsForValue().get(CAPTCHA_PREFIX + key);
    }

    public void deleteCaptcha(String key) {
        redisTemplate.delete(CAPTCHA_PREFIX + key);
    }
}