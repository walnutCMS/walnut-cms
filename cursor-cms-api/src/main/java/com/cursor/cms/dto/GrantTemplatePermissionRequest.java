package com.cursor.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "授予模板权限请求")
public class GrantTemplatePermissionRequest {
    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "权限列表")
    private List<String> permissions;
} 