package com.walnut.cms.service.impl;

import com.walnut.cms.common.exception.BusinessException;
import com.walnut.cms.service.TemplateFileService;
import com.walnut.cms.service.TemplateProcessService;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateProcessServiceImpl implements TemplateProcessService {

    private final Configuration freemarkerConfiguration;
    private final TemplateLoader databaseTemplateLoader;
    private final TemplateFileService templateFileService;

    @Override
    public String processTemplate(String templateId, Map<String, Object> data) {
        try {
            // 获取模板内容
            String content = templateFileService.getFileContent(templateId);
            if (content == null) {
                throw new BusinessException("模板不存在");
            }

            // 创建模板
            Template template = new Template(templateId, content, freemarkerConfiguration);

            // 处理模板
            StringWriter writer = new StringWriter();
            template.process(data, writer);

            return writer.toString();
        } catch (Exception e) {
            log.error("处理模板失败: {}", e.getMessage(), e);
            throw new BusinessException("处理模板失败: " + e.getMessage());
        }
    }

    @Override
    public boolean validateTemplate(String content) {
        try {
            // 创建临时模板并解析
            new Template("temp", new StringReader(content), freemarkerConfiguration);
            return true;
        } catch (Exception e) {
            log.warn("模板验证失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String previewTemplate(String content, Map<String, Object> data) {
        try {
            // 创建临时模板
            Template template = new Template("preview", new StringReader(content), freemarkerConfiguration);

            // 处理模板
            StringWriter writer = new StringWriter();
            template.process(data, writer);

            return writer.toString();
        } catch (Exception e) {
            log.error("预览模板失败: {}", e.getMessage(), e);
            throw new BusinessException("预览模板失败: " + e.getMessage());
        }
    }
} 