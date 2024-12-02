package com.walnut.cms.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();

        // 图片边框
        properties.put("kaptcha.border", "no");

        // 图片尺寸
        properties.put("kaptcha.image.width", "130");
        properties.put("kaptcha.image.height", "48");

        // 字体配置
        properties.put("kaptcha.textproducer.font.color", "0,98,204");  // 深蓝色
        properties.put("kaptcha.textproducer.font.size", "38");
        properties.put("kaptcha.textproducer.font.names", "Arial");

        // 文字配置
        properties.put("kaptcha.textproducer.char.length", "4");
        properties.put("kaptcha.textproducer.char.space", "6");
        // 验证码字符范围（去掉容易混淆的字符）
        properties.put("kaptcha.textproducer.char.string", "23456789ABCDEFGHJKLMNPQRSTUVWXYZ");

        // 干扰线配置
        properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.DefaultNoise");
        properties.put("kaptcha.noise.color", "114,148,180");  // 浅蓝色

        // 图片样式
        properties.put("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple");

        // 背景渐变色（浅灰到白色）
        properties.put("kaptcha.background.clear.from", "white");
        properties.put("kaptcha.background.clear.to", "white");

        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}