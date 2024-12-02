package com.walnut.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.walnut.cms.common.exception.BusinessException;
import com.walnut.cms.dto.FileDTO;
import com.walnut.cms.entity.File;
import com.walnut.cms.mapper.FileMapper;
import com.walnut.cms.service.FileService;
import com.walnut.cms.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    private final JwtUtil jwtUtil;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.max-size}")
    private long maxFileSize;

    // 常用MIME类型映射
    private static final Map<String, String> MIME_TYPE_MAP = new HashMap<>();

    static {
        // 图片类型
        MIME_TYPE_MAP.put("jpg", "image/jpeg");
        MIME_TYPE_MAP.put("jpeg", "image/jpeg");
        MIME_TYPE_MAP.put("png", "image/png");
        MIME_TYPE_MAP.put("gif", "image/gif");
        MIME_TYPE_MAP.put("bmp", "image/bmp");
        MIME_TYPE_MAP.put("webp", "image/webp");
        MIME_TYPE_MAP.put("svg", "image/svg+xml");
        MIME_TYPE_MAP.put("ico", "image/x-icon");

        // 视频类型
        MIME_TYPE_MAP.put("mp4", "video/mp4");
        MIME_TYPE_MAP.put("webm", "video/webm");
        MIME_TYPE_MAP.put("avi", "video/x-msvideo");
        MIME_TYPE_MAP.put("wmv", "video/x-ms-wmv");
        MIME_TYPE_MAP.put("flv", "video/x-flv");
        MIME_TYPE_MAP.put("mov", "video/quicktime");

        // 音频类型
        MIME_TYPE_MAP.put("mp3", "audio/mpeg");
        MIME_TYPE_MAP.put("wav", "audio/wav");
        MIME_TYPE_MAP.put("ogg", "audio/ogg");
        MIME_TYPE_MAP.put("m4a", "audio/mp4");
        MIME_TYPE_MAP.put("wma", "audio/x-ms-wma");

        // 文档类型
        MIME_TYPE_MAP.put("pdf", "application/pdf");
        MIME_TYPE_MAP.put("doc", "application/msword");
        MIME_TYPE_MAP.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        MIME_TYPE_MAP.put("xls", "application/vnd.ms-excel");
        MIME_TYPE_MAP.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        MIME_TYPE_MAP.put("ppt", "application/vnd.ms-powerpoint");
        MIME_TYPE_MAP.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        MIME_TYPE_MAP.put("txt", "text/plain");
        MIME_TYPE_MAP.put("csv", "text/csv");
        MIME_TYPE_MAP.put("html", "text/html");
        MIME_TYPE_MAP.put("htm", "text/html");
        MIME_TYPE_MAP.put("xml", "application/xml");
        MIME_TYPE_MAP.put("json", "application/json");
        MIME_TYPE_MAP.put("md", "text/markdown");

        // 压缩文件类型
        MIME_TYPE_MAP.put("zip", "application/zip");
        MIME_TYPE_MAP.put("rar", "application/x-rar-compressed");
        MIME_TYPE_MAP.put("7z", "application/x-7z-compressed");
        MIME_TYPE_MAP.put("tar", "application/x-tar");
        MIME_TYPE_MAP.put("gz", "application/gzip");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileDTO uploadFile(MultipartFile file, HttpServletRequest request) {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new BusinessException("上传的文件不能为空");
        }

        // 检查文件大小
        if (file.getSize() > maxFileSize) {
            throw new BusinessException("文件大小超过限制");
        }

        try {
            // 获取文件扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFilename);
            if (extension == null) {
                extension = "bin"; // 如果没有扩展名，使用bin作为默认扩展名
            }

            // 生成唯一文件名
            String uniqueFileName = UUID.randomUUID().toString() + "." + extension;
            
            // 创建上传目录（如果不存在）
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 保存文件
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 保存文件信息到数据库
            File fileEntity = new File();
            fileEntity.setName(uniqueFileName);
            fileEntity.setOriginalName(originalFilename);
            fileEntity.setPath(filePath.toString());
            fileEntity.setStorePath(uniqueFileName);
            fileEntity.setType(extension.toLowerCase());
            fileEntity.setSize(file.getSize());
            fileEntity.setMimeType(getMimeType(extension));
            fileEntity.setCreatorId(jwtUtil.getCurrentUserId());
            fileEntity.setUploaderIp(getClientIp(request));
            fileEntity.setDownloadCount(0);

            save(fileEntity);

            // 转换为DTO返回
            return convertToDTO(fileEntity);

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public IPage<FileDTO> getFileList(Integer pageNum, Integer pageSize) {
        Page<File> page = new Page<>(pageNum, pageSize);
        IPage<File> filePage = page(page, new LambdaQueryWrapper<File>()
                .orderByDesc(File::getCreateTime));

        return filePage.convert(this::convertToDTO);
    }

    @Override
    public FileDTO getFileById(Long id) {
        File file = getById(id);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        return convertToDTO(file);
    }

    private FileDTO convertToDTO(File file) {
        FileDTO dto = new FileDTO();
        BeanUtils.copyProperties(file, dto);
        
        // 设置访问和下载URL
        String fileId = file.getId();
        dto.setViewUrl("/api/file/view/" + fileId);
        dto.setDownloadUrl("/api/file/download/" + fileId);
        
        return dto;
    }

    @Override
    public Resource downloadFile(Long id) {
        File file = getById(id);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        try {
            Path filePath = Paths.get(uploadDir, file.getStorePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // 更新下载次数
                file.setDownloadCount(file.getDownloadCount() + 1);
                file.setLastDownloadTime(java.time.LocalDateTime.now());
                updateById(file);

                return resource;
            } else {
                throw new BusinessException("文件不可读或不存在");
            }
        } catch (MalformedURLException e) {
            throw new BusinessException("文件路径错误: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(Long id) {
        File file = getById(id);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        try {
            // 删除物理文件
            Path filePath = Paths.get(uploadDir, file.getStorePath());
            Files.deleteIfExists(filePath);

            // 删除数据库记录
            removeById(id);
        } catch (IOException e) {
            log.error("文件删除失败", e);
            throw new BusinessException("文件删除失败: " + e.getMessage());
        }
    }

    @Override
    public MediaType getMediaType(String fileType) {
        if (fileType == null) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }

        String mimeType = getMimeType(fileType);
        try {
            return MediaType.parseMediaType(mimeType);
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    /**
     * 获取MIME类型
     * @param extension 文件扩展名
     * @return MIME类型
     */
    private String getMimeType(String extension) {
        if (extension == null) {
            return "application/octet-stream";
        }

        String ext = extension.toLowerCase();
        String mimeType = MIME_TYPE_MAP.get(ext);

        if (mimeType != null) {
            return mimeType;
        }

        try {
            // 尝试使用系统的MIME类型检测
            Path tempFile = Files.createTempFile("temp", "." + ext);
            String systemMimeType = Files.probeContentType(tempFile);
            Files.delete(tempFile);

            return systemMimeType != null ? systemMimeType : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 