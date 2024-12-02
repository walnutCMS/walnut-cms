package com.cursor.cms.config.freemarker.directives;

import freemarker.core.Environment;
import freemarker.template.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class XssDefenseDirective implements TemplateDirectiveModel {
    
    private static final Pattern SCRIPT_PATTERN = Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern EVENT_PATTERN = Pattern.compile("on\\w+\\s*=", Pattern.CASE_INSENSITIVE);
    private static final Pattern JAVASCRIPT_PATTERN = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
    
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        if (body == null) {
            throw new TemplateModelException("Missing body");
        }

        // 获取参数
        String mode = params.containsKey("mode") ? 
            ((TemplateScalarModel) params.get("mode")).getAsString() : "escape";
        boolean allowTags = params.containsKey("allowTags") && 
            ((TemplateBooleanModel) params.get("allowTags")).getAsBoolean();

        // 渲染内容
        StringWriter writer = new StringWriter();
        body.render(writer);
        String content = writer.toString();

        // 根据模式处理内容
        String safeContent;
        switch (mode) {
            case "strip":
                safeContent = stripXss(content, allowTags);
                break;
            case "escape":
            default:
                safeContent = StringEscapeUtils.escapeHtml4(content);
                break;
        }

        // 输出安全内容
        env.getOut().write(safeContent);
    }

    private String stripXss(String content, boolean allowTags) {
        if (content == null) {
            return null;
        }

        // 移除脚本标签
        content = SCRIPT_PATTERN.matcher(content).replaceAll("");
        
        // 移除事件处理器
        content = EVENT_PATTERN.matcher(content).replaceAll("");
        
        // 移除javascript:协议
        content = JAVASCRIPT_PATTERN.matcher(content).replaceAll("");

        if (!allowTags) {
            // 移除所有HTML标签，但保留文本
            content = content.replaceAll("<[^>]*>", "");
        }

        return content;
    }
} 