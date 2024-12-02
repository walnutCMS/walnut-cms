package com.walnut.cms.config.freemarker.directives;

import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

@Component
public class DateFormatDirective implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        // 获取参数
        TemplateModel value = (TemplateModel) params.get("value");
        String pattern = params.containsKey("pattern") ? 
            ((TemplateScalarModel) params.get("pattern")).getAsString() : "yyyy-MM-dd HH:mm:ss";

        // 格式化日期
        String result;
        if (value instanceof TemplateDateModel) {
            Date date = ((TemplateDateModel) value).getAsDate();
            result = new SimpleDateFormat(pattern).format(date);
        } else if (value instanceof TemplateScalarModel) {
            // 处理LocalDateTime
            LocalDateTime dateTime = LocalDateTime.parse(((TemplateScalarModel) value).getAsString());
            result = dateTime.format(DateTimeFormatter.ofPattern(pattern));
        } else {
            throw new TemplateModelException("Invalid date value");
        }

        // 输出结果
        env.getOut().write(result);
    }
} 