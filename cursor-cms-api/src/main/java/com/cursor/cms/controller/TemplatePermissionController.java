package com.cursor.cms.controller;

import com.cursor.cms.common.Result;
import com.cursor.cms.dto.GrantTemplatePermissionRequest;
import com.cursor.cms.dto.TemplatePermissionDTO;
import com.cursor.cms.service.TemplatePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/template/{templateId}/permission")
@RequiredArgsConstructor
@Tag(name = "模板权限管理")
public class TemplatePermissionController {
    private final TemplatePermissionService templatePermissionService;

    @GetMapping
    @Operation(summary = "获取模板权限列表")
    public Result<List<TemplatePermissionDTO>> getTemplatePermissions(@PathVariable String templateId) {
        return Result.success(templatePermissionService.getTemplatePermissions(templateId));
    }

    @PostMapping("/grant")
    @Operation(summary = "授予模板权限")
    public Result<Void> grantPermissions(
            @PathVariable String templateId,
            @RequestBody GrantTemplatePermissionRequest request) {
        templatePermissionService.grantPermissions(templateId, request.getUserId(), request.getPermissions());
        return Result.success(null);
    }

    @PostMapping("/revoke")
    @Operation(summary = "撤销模板权限")
    public Result<Void> revokePermissions(
            @PathVariable String templateId,
            @RequestBody GrantTemplatePermissionRequest request) {
        templatePermissionService.revokePermissions(templateId, request.getUserId(), request.getPermissions());
        return Result.success(null);
    }

    @GetMapping("/check")
    @Operation(summary = "检查用户权限")
    public Result<Boolean> checkPermission(
            @PathVariable String templateId,
            @RequestParam String userId,
            @RequestParam String permission) {
        return Result.success(templatePermissionService.hasPermission(templateId, userId, permission));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户权限列表")
    public Result<List<String>> getUserPermissions(
            @PathVariable String templateId,
            @PathVariable String userId) {
        return Result.success(templatePermissionService.getUserPermissions(templateId, userId));
    }
} 