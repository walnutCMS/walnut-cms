package com.walnut.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "忘记密码请求")
public class ForgotPasswordRequest {
    @Schema(description = "邮箱")
    private String email;
} 