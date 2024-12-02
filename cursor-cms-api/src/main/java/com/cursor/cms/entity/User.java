package com.cursor.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cms_user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String nickname;

    private String role;

    private String avatar;

    private String status;

    private Integer loginAttempts;

    private LocalDateTime lastLoginAttemptTime;

    private String resetCode;

    private LocalDateTime resetCodeExpireTime;

    private LocalDateTime lastLoginTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}