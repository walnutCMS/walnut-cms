package com.cursor.cms.config.freemarker.directives;

import freemarker.core.Environment;
import freemarker.template.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class AdvancedCacheDirective implements TemplateDirectiveModel {
    
    private final StringRedisTemplate redisTemplate;
    private static final ConcurrentHashMap<String, CacheEntry> localCache = new ConcurrentHashMap<>();
    
    private static class CacheEntry {
        String content;
        long expireTime;
        
        CacheEntry(String content, long expireTime) {
            this.content = content;
            this.expireTime = expireTime;
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        // 检查参数
        if (!params.containsKey("key")) {
            throw new TemplateModelException("Missing key parameter");
        }
        if (body == null) {
            throw new TemplateModelException("Missing body");
        }

        // 获取参数
        String key = ((TemplateScalarModel) params.get("key")).getAsString();
        long timeout = params.containsKey("timeout") ? 
            ((TemplateNumberModel) params.get("timeout")).getAsNumber().longValue() : 3600;
        String level = params.containsKey("level") ? 
            ((TemplateScalarModel) params.get("level")).getAsString() : "all";
        boolean useLocal = "local".equals(level) || "all".equals(level);
        boolean useRedis = "redis".equals(level) || "all".equals(level);
        String condition = params.containsKey("condition") ? 
            ((TemplateScalarModel) params.get("condition")).getAsString() : null;
        
        String cacheKey = "template:cache:" + key;
        String content = null;

        // 检查条件
        if (condition != null && !Boolean.parseBoolean(condition)) {
            body.render(env.getOut());
            return;
        }

        // 尝试从本地缓存获取
        if (useLocal) {
            CacheEntry entry = localCache.get(cacheKey);
            if (entry != null && !entry.isExpired()) {
                content = entry.content;
            } else if (entry != null) {
                localCache.remove(cacheKey);
            }
        }

        // 尝试从Redis缓存获取
        if (content == null && useRedis) {
            content = redisTemplate.opsForValue().get(cacheKey);
        }

        // 如果缓存中没有，则渲染内容
        if (content == null) {
            StringWriter writer = new StringWriter();
            body.render(writer);
            content = writer.toString();

            // 存入缓存
            if (useLocal) {
                localCache.put(cacheKey, new CacheEntry(content, 
                    System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(timeout)));
            }
            if (useRedis) {
                redisTemplate.opsForValue().set(cacheKey, content, timeout, TimeUnit.SECONDS);
            }
        }

        // 输出内容
        env.getOut().write(content);
    }

    public static void clearLocalCache() {
        localCache.clear();
    }

    public static void removeFromLocalCache(String key) {
        localCache.remove("template:cache:" + key);
    }
} 