package com.walnut.cms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "模板文件信息")
public class TemplateFileDTO {
    @Schema(description = "文件ID")
    private String id;

    @Schema(description = "模板ID")
    private String templateId;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "文件路径")
    private String path;

    @Schema(description = "文件类型")
    private String type;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "是否二进制文件")
    private boolean binary;

    @Schema(description = "创建者ID")
    private String creatorId;

    @Schema(description = "创建者名称")
    private String creatorName;

    @Schema(description = "最后修改者ID")
    private String lastModifierId;

    @Schema(description = "最后修改者名称")
    private String lastModifierName;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
} 