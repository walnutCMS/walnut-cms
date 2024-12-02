package com.cursor.cms.config;

import com.cursor.cms.service.TemplateFileService;
import freemarker.cache.TemplateLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@Component
@RequiredArgsConstructor
public class TemplateLoaderConfig {

    private final TemplateFileService templateFileService;

    @Bean
    public TemplateLoader databaseTemplateLoader() {
        return new TemplateLoader() {
            @Override
            public Object findTemplateSource(String name) {
                try {
                    // 从数据库加载模板内容
                    return templateFileService.getFileContent(name);
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public long getLastModified(Object templateSource) {
                // 每次都重新加载模板
                return -1L;
            }

            @Override
            public Reader getReader(Object templateSource, String encoding) {
                return new StringReader((String) templateSource);
            }

            @Override
            public void closeTemplateSource(Object templateSource) {
                // 不需要关闭任何资源
            }
        };
    }
} 