package com.cursor.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cursor.cms.dto.TemplatePermissionDTO;
import com.cursor.cms.entity.TemplatePermission;
import java.util.List;

public interface TemplatePermissionService extends IService<TemplatePermission> {
    /**
     * 获取模板权限列表
     * @param templateId 模板ID
     * @return 权限列表
     */
    List<TemplatePermissionDTO> getTemplatePermissions(String templateId);

    /**
     * 授予模板权限
     * @param templateId 模板ID
     * @param userId 用户ID
     * @param permissions 权限列表
     */
    void grantPermissions(String templateId, String userId, List<String> permissions);

    /**
     * 撤销模板权限
     * @param templateId 模板ID
     * @param userId 用户ID
     * @param permissions 权限列表
     */
    void revokePermissions(String templateId, String userId, List<String> permissions);

    /**
     * 检查用户是否有指定权限
     * @param templateId 模板ID
     * @param userId 用户ID
     * @param permission 权限类型
     * @return 是否有权限
     */
    boolean hasPermission(String templateId, String userId, String permission);

    /**
     * 获取用户对模板的权限列表
     * @param templateId 模板ID
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> getUserPermissions(String templateId, String userId);
} 