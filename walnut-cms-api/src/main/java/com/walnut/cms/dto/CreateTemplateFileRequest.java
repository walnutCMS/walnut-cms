package com.walnut.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Schema(description = "创建模板文件请求")
public class CreateTemplateFileRequest {
    @NotBlank(message = "文件路径不能为空")
    @Size(max = 255, message = "文件路径长度不能超过255个字符")
    @Pattern(regexp = "^[a-zA-Z0-9/._-]+$", message = "文件路径只能包含字母、数字、下划线、横线、点和斜杠")
    @Schema(description = "文件路径", example = "templates/layout.html")
    private String path;

    @NotBlank(message = "文件类型不能为空")
    @Pattern(regexp = "^(html|css|js|image|other)$", message = "文件类型只能是html、css、js、image或other")
    @Schema(description = "文件类型", example = "html")
    private String type;

    @Size(max = 1048576, message = "文件内容长度不能超过1MB")
    @Schema(description = "文件内容", example = "<html>...</html>")
    private String content;

    @Schema(description = "MIME类型")
    private String mimeType;

    @Schema(description = "编码")
    private String encoding = "UTF-8";

    @Schema(description = "版本")
    private String version = "1.0.0";

    @Schema(description = "文件大小(字节)")
    private Long size;

    @Schema(description = "文件内容hash")
    private String hash;

    public String getName() {
        if (path != null) {
            int lastIndex = path.lastIndexOf('/');
            return lastIndex >= 0 ? path.substring(lastIndex + 1) : path;
        }
        return null;
    }

    public String getExtension() {
        String name = getName();
        if (name != null) {
            int lastIndex = name.lastIndexOf('.');
            return lastIndex >= 0 ? name.substring(lastIndex + 1).toLowerCase() : null;
        }
        return null;
    }

    public boolean isBinary() {
        return type != null && (type.equals("image") || type.equals("other"));
    }
} 