package com.walnut.cms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.walnut.cms.common.Result;
import com.walnut.cms.dto.CreateTemplateFileRequest;
import com.walnut.cms.dto.TemplateFileDTO;
import com.walnut.cms.service.TemplateFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/template-files")
@RequiredArgsConstructor
@Tag(name = "模板文件管理")
public class TemplateFileController {

    private final TemplateFileService templateFileService;

    @GetMapping("/list")
    @Operation(summary = "获取模板文件列表")
    public Result<Map<String, Object>> getFileList(
            @Parameter(description = "模板ID") @RequestParam String templateId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<TemplateFileDTO> page = templateFileService.getFileList(templateId, pageNum, pageSize);
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取模板文件详情")
    public Result<TemplateFileDTO> getFileById(
            @Parameter(description = "文件ID") @PathVariable String id) {
        return Result.success(templateFileService.getFileById(id));
    }

    @PostMapping("/text")
    @Operation(summary = "创建文本文件")
    public Result<Map<String, String>> createTextFile(
            @Parameter(description = "模板ID") @RequestParam String templateId,
            @Parameter(description = "文件信息") @RequestBody @Valid CreateTemplateFileRequest request) {
        String id = templateFileService.createTextFile(templateId, request);
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        return Result.success(data);
    }

    @PostMapping("/binary")
    @Operation(summary = "上传二进制文件")
    public Result<Map<String, String>> uploadBinaryFile(
            @Parameter(description = "模板ID") @RequestParam String templateId,
            @Parameter(description = "文件路径") @RequestParam String path,
            @Parameter(description = "文件") @RequestParam MultipartFile file) {
        String id = templateFileService.uploadBinaryFile(templateId, path, file);
        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        return Result.success(data);
    }

    @PutMapping("/{id}/text")
    @Operation(summary = "更新文本文件")
    public Result<Void> updateTextFile(
            @Parameter(description = "文件ID") @PathVariable String id,
            @Parameter(description = "文件内容") @RequestBody String content) {
        templateFileService.updateTextFile(id, content);
        return Result.success(null);
    }

    @PutMapping("/{id}/binary")
    @Operation(summary = "更新二进制文件")
    public Result<Void> updateBinaryFile(
            @Parameter(description = "文件ID") @PathVariable String id,
            @Parameter(description = "文件") @RequestParam MultipartFile file) {
        templateFileService.updateBinaryFile(id, file);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件")
    public Result<Void> deleteFile(
            @Parameter(description = "文件ID") @PathVariable String id) {
        templateFileService.deleteFile(id);
        return Result.success(null);
    }

    @DeleteMapping("/batch")
    @Operation(summary = "批量删除文件")
    public Result<Void> deleteFiles(
            @Parameter(description = "文件ID列表") @RequestBody List<String> ids) {
        templateFileService.deleteFiles(ids);
        return Result.success(null);
    }

    @GetMapping("/{id}/content")
    @Operation(summary = "获取文件内容")
    public Result<Map<String, Object>> getFileContent(
            @Parameter(description = "文件ID") @PathVariable String id) {
        TemplateFileDTO file = templateFileService.getFileById(id);
        Map<String, Object> data = new HashMap<>();
        
        if (file.isBinary()) {
            byte[] content = templateFileService.getFileBinaryContent(id);
            data.put("content", content);
            data.put("contentType", MediaType.APPLICATION_OCTET_STREAM_VALUE);
        } else {
            String content = templateFileService.getFileContent(id);
            data.put("content", content);
            data.put("contentType", MediaType.TEXT_PLAIN_VALUE);
        }
        
        data.put("filename", file.getName());
        return Result.success(data);
    }

    @GetMapping("/check-path")
    @Operation(summary = "检查文件路径是否存在")
    public Result<Boolean> checkPathExists(
            @Parameter(description = "模板ID") @RequestParam String templateId,
            @Parameter(description = "文件路径") @RequestParam String path,
            @Parameter(description = "排除的文件ID") @RequestParam(required = false) String excludeId) {
        return Result.success(templateFileService.checkPathExists(templateId, path, excludeId));
    }
} 