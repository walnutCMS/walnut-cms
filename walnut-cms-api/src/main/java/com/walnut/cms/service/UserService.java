package com.walnut.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.walnut.cms.dto.*;
import com.walnut.cms.entity.User;

public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param request 注册请求
     */
    void register(RegisterRequest request);

    /**
     * 用户登录
     * @param request 登录请求
     * @return 登录响应信息
     */
    LoginResponse login(LoginRequest request);

    /**
     * 刷新令牌
     * @param refreshToken 刷新令牌
     * @return 令牌响应信息
     */
    TokenResponse refreshToken(String refreshToken);

    /**
     * 忘记密码
     * @param email 用户邮箱
     */
    void forgotPassword(String email);

    /**
     * 重置密码
     * @param request 重置密码请求
     */
    void resetPassword(ResetPasswordRequest request);

    /**
     * 检查登录尝试次数
     * @param username 用户名
     */
    void checkLoginAttempts(String username);

    /**
     * 重置登录尝试次数
     * @param username 用户名
     */
    void resetLoginAttempts(String username);

    /**
     * 增加登录尝试次数
     * @param username 用户名
     */
    void incrementLoginAttempts(String username);

    /**
     * 获取用户信息
     * @return 用户信息
     */
    UserInfoDTO getUserInfo();

    /**
     * 更新用户信息
     * @param request 更新请求
     */
    void updateUserInfo(UpdateUserInfoRequest request);

    /**
     * 更新密码
     * @param request 更新密码请求
     */
    void updatePassword(ChangePasswordRequest request);

    /**
     * 退出登录
     * @param token 访问令牌
     */
    void logout(String token);
}