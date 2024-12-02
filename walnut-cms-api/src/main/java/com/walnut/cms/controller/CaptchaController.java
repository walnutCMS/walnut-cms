package com.walnut.cms.controller;

import com.walnut.cms.common.Result;
import com.google.code.kaptcha.Producer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
@Tag(name = "验证码")
public class CaptchaController {
    private final Producer captchaProducer;
    private final StringRedisTemplate redisTemplate;

    @GetMapping
    @Operation(summary = "获取验证码")
    public Result<Map<String, String>> getCaptcha() {
        try {
            // 生成验证码文本
            String captchaText = captchaProducer.createText();

            // 生成验证码图片
            BufferedImage image = captchaProducer.createImage(captchaText);

            // 将图片转换为Base64
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            String base64Image = Base64.getEncoder().encodeToString(outputStream.toByteArray());

            // 生成验证码key
            String captchaKey = UUID.randomUUID().toString();

            // 将验证码存入Redis，设置5分钟过期
            redisTemplate.opsForValue().set("captcha:" + captchaKey, captchaText, 5, TimeUnit.MINUTES);

            // 返回验证码key和图片
            Map<String, String> result = new HashMap<>();
            result.put("key", captchaKey);
            result.put("image", "data:image/jpeg;base64," + base64Image);

            return Result.success(result);
        } catch (Exception e) {
            return Result.error("生成验证码失败");
        }
    }
}