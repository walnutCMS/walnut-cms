package com.cursor.cms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "模板版本信息")
public class TemplateVersionDTO {
    @Schema(description = "版本ID")
    private String id;

    @Schema(description = "模板ID")
    private String templateId;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "版本描述")
    private String description;

    @Schema(description = "创建者ID")
    private String creatorId;

    @Schema(description = "创建者名称")
    private String creatorName;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
} 