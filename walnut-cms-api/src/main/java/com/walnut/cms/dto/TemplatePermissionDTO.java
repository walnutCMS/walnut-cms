package com.walnut.cms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "模板权限信息")
public class TemplatePermissionDTO {
    @Schema(description = "权限ID")
    private String id;

    @Schema(description = "模板ID")
    private String templateId;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "权限类型")
    private String permission;

    @Schema(description = "授权人ID")
    private String grantedBy;

    @Schema(description = "授权人用户名")
    private String grantedByUsername;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
} 