package com.walnut.cms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "模板信息")
public class TemplateDTO {
    @Schema(description = "模板ID")
    private String id;

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "模板描述")
    private String description;

    @Schema(description = "模板类型")
    private String type;

    @Schema(description = "模板状态")
    private String status;

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