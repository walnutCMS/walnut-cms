package com.cursor.cms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cursor.cms.common.Result;
import com.cursor.cms.dto.FileDTO;
import com.cursor.cms.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@Tag(name = "文件管理")
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<FileDTO> uploadFile(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        return Result.success(fileService.uploadFile(file, request));
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "下载文件")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        FileDTO file = fileService.getFileById(id);
        Resource resource = fileService.downloadFile(id);

        String encodedFilename = UriUtils.encode(file.getName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename*=UTF-8''" + encodedFilename;

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    @GetMapping("/view/{id}")
    @Operation(summary = "查看文件")
    public ResponseEntity<Resource> viewFile(@PathVariable Long id) {
        FileDTO file = fileService.getFileById(id);
        Resource resource = fileService.downloadFile(id);

        // 根据文件类型设置正确的Content-Type
        MediaType mediaType = fileService.getMediaType(file.getType());

        return ResponseEntity.ok()
                .contentType(mediaType)
                // 允许跨域访问
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                // 设置缓存策略
                .header(HttpHeaders.CACHE_CONTROL, "max-age=31536000")
                .body(resource);
    }

    @GetMapping("/list")
    @Operation(summary = "获取文件列表")
    public Result<Map<String, Object>> getFileList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<FileDTO> page = fileService.getFileList(pageNum, pageSize);

        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());

        return Result.success(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文件信息")
    public Result<FileDTO> getFile(@PathVariable Long id) {
        return Result.success(fileService.getFileById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件")
    public Result<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return Result.success(null);
    }
} 