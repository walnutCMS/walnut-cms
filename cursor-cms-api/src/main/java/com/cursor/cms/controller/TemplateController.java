package com.cursor.cms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cursor.cms.common.Result;
import com.cursor.cms.dto.TemplateDTO;
import com.cursor.cms.dto.TemplateQueryRequest;
import com.cursor.cms.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/template")
@RequiredArgsConstructor
@Tag(name = "模板管理")
public class TemplateController {
    private final TemplateService templateService;

    @GetMapping("/list")
    @Operation(summary = "获取模板列表")
    public Result<Map<String, Object>> getTemplateList(TemplateQueryRequest request) {
        IPage<TemplateDTO> page = templateService.getTemplateList(request);
        
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());
        
        return Result.success(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取模板详情")
    public Result<TemplateDTO> getTemplate(@PathVariable String id) {
        return Result.success(templateService.getTemplateById(id));
    }

    @PostMapping
    @Operation(summary = "创建模板")
    public Result<Map<String, String>> createTemplate(@RequestBody TemplateDTO dto) {
        String id = templateService.createTemplate(dto);
        
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        
        return Result.success(data);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新模板")
    public Result<Void> updateTemplate(@PathVariable String id, @RequestBody TemplateDTO dto) {
        templateService.updateTemplate(id, dto);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除模板")
    public Result<Void> deleteTemplate(@PathVariable String id) {
        templateService.deleteTemplate(id);
        return Result.success(null);
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "发布模板")
    public Result<Void> publishTemplate(@PathVariable String id) {
        templateService.publishTemplate(id);
        return Result.success(null);
    }

    @PutMapping("/{id}/unpublish")
    @Operation(summary = "下架模板")
    public Result<Void> unpublishTemplate(@PathVariable String id) {
        templateService.unpublishTemplate(id);
        return Result.success(null);
    }

    @PostMapping("/{id}/copy")
    @Operation(summary = "复制模板")
    public Result<Map<String, String>> copyTemplate(@PathVariable String id) {
        String newId = templateService.copyTemplate(id);
        
        Map<String, String> data = new HashMap<>();
        data.put("id", newId);
        
        return Result.success(data);
    }

    @GetMapping("/{id}/export")
    @Operation(summary = "导出模板")
    public Result<String> exportTemplate(@PathVariable String id) {
        return Result.success(templateService.exportTemplate(id));
    }

    @PostMapping("/import")
    @Operation(summary = "导入模板")
    public Result<Map<String, String>> importTemplate(@RequestBody String content) {
        String id = templateService.importTemplate(content);
        
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        
        return Result.success(data);
    }

    @PostMapping("/{id}/preview")
    @Operation(summary = "预览模板")
    public Result<String> previewTemplate(@PathVariable String id, @RequestBody Map<String, Object> data) {
        return Result.success(templateService.previewTemplate(id, data));
    }
} 