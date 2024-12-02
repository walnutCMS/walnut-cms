package com.cursor.cms.config;

import com.cursor.cms.config.freemarker.directives.*;
import com.cursor.cms.config.freemarker.utils.AdvancedTemplateUtils.*;
import com.cursor.cms.config.freemarker.utils.TemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FreemarkerConfig {

    private final CutDirective cutDirective;
    private final DateFormatDirective dateFormatDirective;
    private final HasPermissionDirective hasPermissionDirective;
    private final CacheDirective cacheDirective;
    private final AdvancedCacheDirective advancedCacheDirective;
    private final XssDefenseDirective xssDefenseDirective;
    
    // Template Utils
    private final TemplateUtils.FormatFileSizeMethod formatFileSizeMethod;
    private final TemplateUtils.FormatNumberMethod formatNumberMethod;
    private final TemplateUtils.UrlEncodeMethod urlEncodeMethod;
    private final TemplateUtils.HtmlEscapeMethod htmlEscapeMethod;
    private final TemplateUtils.ToJsonMethod toJsonMethod;
    private final TemplateUtils.FromJsonMethod fromJsonMethod;
    private final TemplateUtils.Base64EncodeMethod base64EncodeMethod;
    private final TemplateUtils.Base64DecodeMethod base64DecodeMethod;
    private final TemplateUtils.HashMethod hashMethod;
    
    // Advanced Template Utils
    private final TimeAgoMethod timeAgoMethod;
    private final TruncateMethod truncateMethod;
    private final FormatCurrencyMethod formatCurrencyMethod;
    private final SlugifyMethod slugifyMethod;
    private final HighlightMethod highlightMethod;
    private final WordCountMethod wordCountMethod;
    private final PhoneFormatMethod phoneFormatMethod;

    @Bean
    @Primary
    public Configuration freemarkerConfiguration() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        
        // 基本设置
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        
        // 数字格式设置
        cfg.setNumberFormat("0.##");
        
        // 日期时间格式设置
        cfg.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        cfg.setDateFormat("yyyy-MM-dd");
        cfg.setTimeFormat("HH:mm:ss");
        
        // 设置标签语法
        cfg.setTagSyntax(Configuration.AUTO_DETECT_TAG_SYNTAX);
        
        // 设置模板更新延迟，0表示立即更新
        cfg.setTemplateUpdateDelayMilliseconds(0);
        
        // 设置空值处理
        cfg.setClassicCompatible(true);

        // 注册基本指令
        cfg.setSharedVariable("cut", cutDirective);
        cfg.setSharedVariable("dateFormat", dateFormatDirective);
        cfg.setSharedVariable("hasPermission", hasPermissionDirective);
        cfg.setSharedVariable("cache", cacheDirective);
        
        // 注册高级指令
        cfg.setSharedVariable("advancedCache", advancedCacheDirective);
        cfg.setSharedVariable("xssDefense", xssDefenseDirective);

        // 注册基本工具方法
        cfg.setSharedVariable("formatFileSize", formatFileSizeMethod);
        cfg.setSharedVariable("formatNumber", formatNumberMethod);
        cfg.setSharedVariable("urlEncode", urlEncodeMethod);
        cfg.setSharedVariable("htmlEscape", htmlEscapeMethod);
        cfg.setSharedVariable("toJson", toJsonMethod);
        cfg.setSharedVariable("fromJson", fromJsonMethod);
        cfg.setSharedVariable("base64Encode", base64EncodeMethod);
        cfg.setSharedVariable("base64Decode", base64DecodeMethod);
        cfg.setSharedVariable("hash", hashMethod);
        
        // 注册高级工具方法
        cfg.setSharedVariable("timeAgo", timeAgoMethod);
        cfg.setSharedVariable("truncate", truncateMethod);
        cfg.setSharedVariable("formatCurrency", formatCurrencyMethod);
        cfg.setSharedVariable("slugify", slugifyMethod);
        cfg.setSharedVariable("highlight", highlightMethod);
        cfg.setSharedVariable("wordCount", wordCountMethod);
        cfg.setSharedVariable("phoneFormat", phoneFormatMethod);

        // 添加共享变量
        Map<String, Object> sharedVariables = new HashMap<>();
        sharedVariables.put("ctx", "/");  // 上下文路径
        sharedVariables.put("version", "1.0.0");  // 版本号
        try {
            cfg.setAllSharedVariables(new freemarker.template.SimpleHash(sharedVariables));
        } catch (TemplateModelException e) {
            throw new RuntimeException(e);
        }

        return cfg;
    }
} 