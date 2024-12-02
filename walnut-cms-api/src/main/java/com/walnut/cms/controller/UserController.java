package com.walnut.cms.controller;

import com.walnut.cms.common.Result;
import com.walnut.cms.common.constant.SecurityConstants;
import com.walnut.cms.dto.*;
import com.walnut.cms.service.UserService;
import com.walnut.cms.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户管理")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success(null);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Result.success(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出")
    public Result<Void> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            userService.logout(token);
        }
        return Result.success(null);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "刷新Token")
    public Result<TokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        TokenResponse response = userService.refreshToken(request.getRefreshToken());
        return Result.success(response);
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public Result<UserInfoDTO> getUserInfo() {
        UserInfoDTO userInfo = userService.getUserInfo();
        return Result.success(userInfo);
    }

    @PutMapping("/info")
    @Operation(summary = "更新用户信息")
    public Result<Void> updateUserInfo(@RequestBody UpdateUserInfoRequest request) {
        userService.updateUserInfo(request);
        return Result.success(null);
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Result<Void> updatePassword(@RequestBody ChangePasswordRequest request) {
        userService.updatePassword(request);
        return Result.success(null);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "忘记密码")
    public Result<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request.getEmail());
        return Result.success(null);
    }

    @PostMapping("/password/reset")
    @Operation(summary = "重置密码")
    public Result<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return Result.success(null);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConstants.HEADER_STRING);
        if (bearerToken != null && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return bearerToken.substring(SecurityConstants.TOKEN_PREFIX.length());
        }
        return null;
    }
}