package com.cursor.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建模板版本请求")
public class CreateTemplateVersionRequest {
    @Schema(description = "版本号", example = "1.0.0")
    private String version;

    @Schema(description = "模板内容")
    private String content;

    @Schema(description = "版本描述")
    private String description;

    @Schema(description = "变更日志")
    private String changelog;

    @Schema(description = "兼容性说明")
    private String compatibility;

    @Schema(description = "发布说明")
    private String releaseNotes;

    @Schema(description = "状态", example = "draft, published")
    private String status = "draft";
}