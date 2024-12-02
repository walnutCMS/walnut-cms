package com.walnut.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "注册请求")
public class RegisterRequest {
    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "确认密码")
    private String confirmPassword;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "验证码")
    private String captcha;

    @Schema(description = "验证码key")
    private String captchaKey;
}