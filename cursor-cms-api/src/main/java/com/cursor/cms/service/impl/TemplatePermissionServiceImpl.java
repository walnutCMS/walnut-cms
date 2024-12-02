package com.cursor.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cursor.cms.common.exception.BusinessException;
import com.cursor.cms.dto.TemplatePermissionDTO;
import com.cursor.cms.entity.TemplatePermission;
import com.cursor.cms.mapper.TemplatePermissionMapper;
import com.cursor.cms.service.TemplatePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplatePermissionServiceImpl extends ServiceImpl<TemplatePermissionMapper, TemplatePermission> implements TemplatePermissionService {

    @Override
    public List<TemplatePermissionDTO> getTemplatePermissions(String templateId) {
        List<TemplatePermission> permissions = list(new LambdaQueryWrapper<TemplatePermission>()
                .eq(TemplatePermission::getTemplateId, templateId));
        
        return permissions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantPermissions(String templateId, String userId, List<String> permissions) {
        // 检查是否已存在相同的权限
        List<String> existingPermissions = getUserPermissions(templateId, userId);
        List<String> newPermissions = permissions.stream()
                .filter(permission -> !existingPermissions.contains(permission))
                .collect(Collectors.toList());

        // 批量添加新权限
        if (!newPermissions.isEmpty()) {
            List<TemplatePermission> permissionEntities = newPermissions.stream()
                    .map(permission -> {
                        TemplatePermission entity = new TemplatePermission();
                        entity.setTemplateId(templateId);
                        entity.setUserId(userId);
                        entity.setPermission(permission);
                        return entity;
                    })
                    .collect(Collectors.toList());
            saveBatch(permissionEntities);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokePermissions(String templateId, String userId, List<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return;
        }

        // 删除指定的权限
        remove(new LambdaQueryWrapper<TemplatePermission>()
                .eq(TemplatePermission::getTemplateId, templateId)
                .eq(TemplatePermission::getUserId, userId)
                .in(TemplatePermission::getPermission, permissions));
    }

    @Override
    public boolean hasPermission(String templateId, String userId, String permission) {
        return count(new LambdaQueryWrapper<TemplatePermission>()
                .eq(TemplatePermission::getTemplateId, templateId)
                .eq(TemplatePermission::getUserId, userId)
                .eq(TemplatePermission::getPermission, permission)) > 0;
    }

    @Override
    public List<String> getUserPermissions(String templateId, String userId) {
        List<TemplatePermission> permissions = list(new LambdaQueryWrapper<TemplatePermission>()
                .eq(TemplatePermission::getTemplateId, templateId)
                .eq(TemplatePermission::getUserId, userId));
        
        return permissions.stream()
                .map(TemplatePermission::getPermission)
                .collect(Collectors.toList());
    }

    private TemplatePermissionDTO convertToDTO(TemplatePermission permission) {
        TemplatePermissionDTO dto = new TemplatePermissionDTO();
        BeanUtils.copyProperties(permission, dto);
        return dto;
    }
} 