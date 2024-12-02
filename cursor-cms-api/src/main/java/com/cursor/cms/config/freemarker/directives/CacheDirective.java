package com.cursor.cms.config.freemarker.directives;

import freemarker.core.Environment;
import freemarker.template.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CacheDirective implements TemplateDirectiveModel {
    
    private final StringRedisTemplate redisTemplate;

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
        String cacheKey = "template:cache:" + key;

        // 尝试从缓存获取
        String content = redisTemplate.opsForValue().get(cacheKey);
        if (content != null) {
            env.getOut().write(content);
            return;
        }

        // 渲染内容
        StringWriter writer = new StringWriter();
        body.render(writer);
        content = writer.toString();

        // 存入缓存
        redisTemplate.opsForValue().set(cacheKey, content, timeout, TimeUnit.SECONDS);

        // 输出内容
        env.getOut().write(content);
    }
} 