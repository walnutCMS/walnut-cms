package com.walnut.cms.config.freemarker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Base64;
import java.util.List;

@Component
public class TemplateUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Component("formatFileSize")
    public static class FormatFileSizeMethod implements TemplateMethodModelEx {
        private static final String[] UNITS = {"B", "KB", "MB", "GB", "TB"};

        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Missing file size argument");
            }

            long size = Long.parseLong(arguments.get(0).toString());
            if (size <= 0) return "0 B";

            int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
            DecimalFormat format = new DecimalFormat("#,##0.##");
            return format.format(size / Math.pow(1024, digitGroups)) + " " + UNITS[digitGroups];
        }
    }

    @Component("formatNumber")
    public static class FormatNumberMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Missing number argument");
            }

            double number = Double.parseDouble(arguments.get(0).toString());
            String pattern = arguments.size() > 1 ? arguments.get(1).toString() : "#,##0.##";
            return new DecimalFormat(pattern).format(number);
        }
    }

    @Component("urlEncode")
    public static class UrlEncodeMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Missing string argument");
            }

            try {
                return java.net.URLEncoder.encode(arguments.get(0).toString(), "UTF-8");
            } catch (Exception e) {
                throw new TemplateModelException("URL encoding failed", e);
            }
        }
    }

    @Component("htmlEscape")
    public static class HtmlEscapeMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Missing string argument");
            }

            String text = arguments.get(0).toString();
            return text.replace("&", "&amp;")
                      .replace("<", "&lt;")
                      .replace(">", "&gt;")
                      .replace("\"", "&quot;")
                      .replace("'", "&#39;");
        }
    }

    @Component("toJson")
    public static class ToJsonMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Missing object argument");
            }

            try {
                return objectMapper.writeValueAsString(arguments.get(0));
            } catch (Exception e) {
                throw new TemplateModelException("JSON serialization failed", e);
            }
        }
    }

    @Component("fromJson")
    public static class FromJsonMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Missing JSON string argument");
            }

            try {
                return objectMapper.readValue(arguments.get(0).toString(), Object.class);
            } catch (Exception e) {
                throw new TemplateModelException("JSON deserialization failed", e);
            }
        }
    }

    @Component("base64Encode")
    public static class Base64EncodeMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Missing string argument");
            }

            return Base64.getEncoder().encodeToString(arguments.get(0).toString().getBytes());
        }
    }

    @Component("base64Decode")
    public static class Base64DecodeMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Missing base64 string argument");
            }

            try {
                return new String(Base64.getDecoder().decode(arguments.get(0).toString()));
            } catch (Exception e) {
                throw new TemplateModelException("Base64 decoding failed", e);
            }
        }
    }

    @Component("hash")
    public static class HashMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Missing string argument");
            }

            return passwordEncoder.encode(arguments.get(0).toString());
        }
    }
} 