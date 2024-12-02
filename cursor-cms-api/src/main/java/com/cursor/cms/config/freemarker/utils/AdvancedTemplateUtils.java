package com.cursor.cms.config.freemarker.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class AdvancedTemplateUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Component
    public static class TimeAgoMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.size() != 1) {
                throw new TemplateModelException("Wrong arguments");
            }
            
            LocalDateTime date = LocalDateTime.parse(arguments.get(0).toString(), 
                DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime now = LocalDateTime.now();
            
            long seconds = java.time.Duration.between(date, now).getSeconds();
            
            if (seconds < 60) {
                return seconds + " seconds ago";
            } else if (seconds < 3600) {
                return (seconds / 60) + " minutes ago";
            } else if (seconds < 86400) {
                return (seconds / 3600) + " hours ago";
            } else {
                return (seconds / 86400) + " days ago";
            }
        }
    }

    @Component
    public static class TruncateMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.size() < 2) {
                throw new TemplateModelException("Wrong arguments");
            }
            
            String text = arguments.get(0).toString();
            int length = Integer.parseInt(arguments.get(1).toString());
            String suffix = arguments.size() > 2 ? arguments.get(2).toString() : "...";
            
            if (text.length() <= length) {
                return text;
            }
            
            return text.substring(0, length) + suffix;
        }
    }

    @Component
    public static class FormatCurrencyMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.size() < 1) {
                throw new TemplateModelException("Wrong arguments");
            }
            
            double amount = Double.parseDouble(arguments.get(0).toString());
            String currency = arguments.size() > 1 ? arguments.get(1).toString() : "USD";
            
            DecimalFormat df = new DecimalFormat("#,##0.00");
            return currency + " " + df.format(amount);
        }
    }

    @Component
    public static class SlugifyMethod implements TemplateMethodModelEx {
        private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
        private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
        
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Wrong arguments");
            }
            
            String input = arguments.get(0).toString();
            
            String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
            String normalized = NONLATIN.matcher(nowhitespace).replaceAll("");
            
            return normalized.toLowerCase();
        }
    }

    @Component
    public static class HighlightMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.size() < 2) {
                throw new TemplateModelException("Wrong arguments");
            }
            
            String text = arguments.get(0).toString();
            String query = arguments.get(1).toString();
            String highlightClass = arguments.size() > 2 ? 
                arguments.get(2).toString() : "highlight";
            
            if (StringUtils.isBlank(query)) {
                return text;
            }
            
            return text.replaceAll("(?i)(" + Pattern.quote(query) + ")", 
                "<span class=\"" + highlightClass + "\">$1</span>");
        }
    }

    @Component
    public static class WordCountMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Wrong arguments");
            }
            
            String text = arguments.get(0).toString();
            return text.split("\\s+").length;
        }
    }

    @Component
    public static class PhoneFormatMethod implements TemplateMethodModelEx {
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments.isEmpty()) {
                throw new TemplateModelException("Wrong arguments");
            }
            
            String phone = arguments.get(0).toString().replaceAll("[^0-9]", "");
            
            if (phone.length() == 10) {
                return String.format("(%s) %s-%s", 
                    phone.substring(0, 3),
                    phone.substring(3, 6),
                    phone.substring(6));
            }
            
            return phone;
        }
    }
}