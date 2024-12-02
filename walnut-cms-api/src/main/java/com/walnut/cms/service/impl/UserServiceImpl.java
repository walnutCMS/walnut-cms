package com.walnut.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.walnut.cms.common.constant.SecurityConstants;
import com.walnut.cms.common.exception.BusinessException;
import com.walnut.cms.dto.*;
import com.walnut.cms.entity.User;
import com.walnut.cms.mapper.UserMapper;
import com.walnut.cms.service.EmailService;
import com.walnut.cms.service.UserService;
import com.walnut.cms.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        // 验证验证码
        String captcha = redisTemplate.opsForValue().get("captcha:" + request.getCaptchaKey());
        if (!StringUtils.hasText(captcha) || !captcha.equalsIgnoreCase(request.getCaptcha())) {
            throw new BusinessException("验证码错误或已过期");
        }

        // 验证密码是否一致
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 验证密码格式
        if (!request.getPassword().matches(SecurityConstants.PASSWORD_PATTERN)) {
            throw new BusinessException(SecurityConstants.PASSWORD_MESSAGE);
        }

        // 检查用户名是否已存在
        if (count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername())) > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (count(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, request.getEmail())) > 0) {
            throw new BusinessException("邮箱已被注册");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setNickname(request.getNickname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setLoginAttempts(0);

        save(user);

        // 删除验证码
        redisTemplate.delete("captcha:" + request.getCaptchaKey());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 验证验证码
        String captcha = redisTemplate.opsForValue().get("captcha:" + request.getCaptchaKey());
        if (!StringUtils.hasText(captcha) || !captcha.equalsIgnoreCase(request.getCaptcha())) {
            throw new BusinessException("验证码错误或已过期");
        }

        // 检查登录尝试次数
        checkLoginAttempts(request.getUsername());

        // 验证用户名和密码
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername()));

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            incrementLoginAttempts(request.getUsername());
            throw new BusinessException("用户名或密码错误");
        }

        // 重置登录尝试次数
        resetLoginAttempts(request.getUsername());

        // 生成token
        String accessToken = jwtUtil.generateToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 删除验证码
        redisTemplate.delete("captcha:" + request.getCaptchaKey());

        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getTokenExpiration() / 1000)
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new BusinessException("刷新令牌无效或已过期");
        }

        String userId = jwtUtil.getUserIdFromRefreshToken(refreshToken);
        String newAccessToken = jwtUtil.generateToken(userId);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getTokenExpiration() / 1000)
                .build();
    }

    @Override
    public UserInfoDTO getUserInfo() {
        // 从JWT中获取当前用户ID
        String userId = jwtUtil.getCurrentUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserInfoDTO userInfo = new UserInfoDTO();
        BeanUtils.copyProperties(user, userInfo);
        return userInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UpdateUserInfoRequest request) {
        String userId = jwtUtil.getCurrentUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 更新用户信息
        if (StringUtils.hasText(request.getNickname())) {
            user.setNickname(request.getNickname());
        }
        if (StringUtils.hasText(request.getAvatar())) {
            user.setAvatar(request.getAvatar());
        }
        if (StringUtils.hasText(request.getEmail())) {
            user.setEmail(request.getEmail());
        }
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(ChangePasswordRequest request) {
        String userId = jwtUtil.getCurrentUserId();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 验证旧密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        // 验证新密码是否一致
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }
        // 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void forgotPassword(String email) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email));

        if (user == null) {
            throw new BusinessException("邮箱未注册");
        }

        // 生成重置码
        String resetCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        user.setResetCode(resetCode);
        user.setResetCodeExpireTime(LocalDateTime.now().plusHours(24));
        updateById(user);

        // 发送重置密码邮件
        emailService.sendResetPasswordEmail(email, resetCode, user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(ResetPasswordRequest request) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getResetCode, request.getResetCode()));

        if (user == null) {
            throw new BusinessException("重置码无效");
        }

        if (user.getResetCodeExpireTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("重置码已过期");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetCode(null);
        user.setResetCodeExpireTime(null);
        updateById(user);
    }

    @Override
    public void checkLoginAttempts(String username) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        if (user != null && user.getLoginAttempts() >= SecurityConstants.MAX_LOGIN_ATTEMPTS) {
            LocalDateTime lastAttemptTime = user.getLastLoginAttemptTime();
            if (lastAttemptTime != null &&
                    lastAttemptTime.plusSeconds(SecurityConstants.LOGIN_ATTEMPT_TIMEOUT)
                            .isAfter(LocalDateTime.now())) {
                throw new BusinessException("登录尝试次数过多，请稍后再试");
            } else {
                // 重置登录尝试次数
                resetLoginAttempts(username);
            }
        }
    }

    @Override
    public void resetLoginAttempts(String username) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        if (user != null) {
            user.setLoginAttempts(0);
            user.setLastLoginAttemptTime(null);
            updateById(user);
        }
    }

    @Override
    public void incrementLoginAttempts(String username) {
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));

        if (user != null) {
            user.setLoginAttempts(user.getLoginAttempts() + 1);
            user.setLastLoginAttemptTime(LocalDateTime.now());
            updateById(user);
        }
    }

    @Override
    public void logout(String token) {
        jwtUtil.addToBlacklist(token);
    }
}