package com.walnut.cms.service;

public interface EmailService {
    /**
     * 发送重置密码邮件
     * @param to 收件人邮箱
     * @param resetCode 重置码
     * @param username 用户名
     */
    void sendResetPasswordEmail(String to, String resetCode, String username);
} 