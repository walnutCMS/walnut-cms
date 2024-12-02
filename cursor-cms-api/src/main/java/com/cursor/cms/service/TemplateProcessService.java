package com.cursor.cms.service;

import java.util.Map;

public interface TemplateProcessService {
    /**
     * 处理模板
     * @param templateId 模板ID
     * @param data 模板数据
     * @return 处理结果
     */
    String processTemplate(String templateId, Map<String, Object> data);

    /**
     * 验证模板语法
     * @param content 模板内容
     * @return 是否有效
     */
    boolean validateTemplate(String content);

    /**
     * 预览模板
     * @param content 模板内容
     * @param data 预览数据
     * @return 预览结果
     */
    String previewTemplate(String content, Map<String, Object> data);
} 