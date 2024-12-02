package com.walnut.cms.config.freemarker.directives;

import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

@Component
public class CutDirective implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        // 检查参数
        if (body == null) {
            throw new TemplateModelException("missing body");
        }

        // 获取参数
        int length = params.containsKey("length") ? 
            ((TemplateNumberModel) params.get("length")).getAsNumber().intValue() : 100;
        String suffix = params.containsKey("suffix") ? 
            ((TemplateScalarModel) params.get("suffix")).getAsString() : "...";

        // 捕获body内容
        Writer writer = env.getOut();
        StringWriter bodyWriter = new StringWriter();
        body.render(bodyWriter);
        String text = bodyWriter.toString();

        // 截断文本
        if (text.length() > length) {
            writer.write(text.substring(0, length) + suffix);
        } else {
            writer.write(text);
        }
    }
} 