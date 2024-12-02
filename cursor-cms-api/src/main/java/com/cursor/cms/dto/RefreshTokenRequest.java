package com.cursor.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "刷新令牌请求")
public class RefreshTokenRequest {
    @Schema(description = "刷新令牌")
    private String refreshToken;
} 