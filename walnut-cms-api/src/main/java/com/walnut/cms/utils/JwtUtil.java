package com.walnut.cms.utils;

import com.walnut.cms.entity.User;
import com.walnut.cms.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    private final StringRedisTemplate redisTemplate;
    private final UserMapper userMapper;
    @Value("${jwt.secret}")
    private String secret;
    @Getter
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    public String generateToken(String userId) {
        return generateToken(userId, expiration);
    }

    public String generateRefreshToken(String userId) {
        return generateToken(userId, refreshExpiration);
    }

    private String generateToken(String userId, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getUserIdFromRefreshToken(String token) {
        return getUserIdFromToken(token);
    }

    public boolean validateToken(String token) {
        try {
            if (isTokenBlacklisted(token)) {
                return false;
            }
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            if (isTokenBlacklisted(token)) {
                return false;
            }
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            // 检查是否是刷新令牌（通过过期时间判断）
            return claims.getExpiration().getTime() - claims.getIssuedAt().getTime() == refreshExpiration;
        } catch (Exception e) {
            return false;
        }
    }

    public void addToBlacklist(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            // 计算token的剩余有效期
            Date expiration = claims.getExpiration();
            long ttl = expiration.getTime() - System.currentTimeMillis();

            if (ttl > 0) {
                // 将token加入黑名单，有效期为token的剩余时间
                redisTemplate.opsForValue().set(
                        TOKEN_BLACKLIST_PREFIX + token,
                        "1",
                        ttl,
                        TimeUnit.MILLISECONDS
                );
            }
        } catch (Exception ignored) {
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token));
    }

    public String getCurrentUserId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getCurrentUsername() {
        String userId = getCurrentUserId();
        User user = userMapper.selectById(userId);
        return user != null ? user.getUsername() : null;
    }

    public Long getTokenExpiration() {
        return expiration;
    }

    public Long getRefreshTokenExpiration() {
        return refreshExpiration;
    }
}