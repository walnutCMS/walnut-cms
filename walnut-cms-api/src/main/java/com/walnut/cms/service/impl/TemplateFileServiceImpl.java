package com.walnut.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.walnut.cms.common.exception.BusinessException;
import com.walnut.cms.dto.CreateTemplateFileRequest;
import com.walnut.cms.dto.TemplateFileDTO;
import com.walnut.cms.entity.Template;
import com.walnut.cms.entity.TemplateFile;
import com.walnut.cms.entity.User;
import com.walnut.cms.mapper.TemplateFileMapper;
import com.walnut.cms.mapper.TemplateMapper;
import com.walnut.cms.mapper.UserMapper;
import com.walnut.cms.service.TemplateFileService;
import com.walnut.cms.service.TemplatePermissionService;
import com.walnut.cms.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateFileServiceImpl extends ServiceImpl<TemplateFileMapper, TemplateFile> implements TemplateFileService {
    private final TemplateMapper templateMapper;
    private final UserMapper userMapper;
    private final TemplatePermissionService templatePermissionService;
    private final JwtUtil jwtUtil;

    @Override
    public IPage<TemplateFileDTO> getFileList(String templateId, Integer pageNum, Integer pageSize) {
        // 检查模板是否存在
        Template template = templateMapper.selectById(templateId);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        // 检查查看权限
        String userId = jwtUtil.getCurrentUserId();
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(templateId, userId, "view")) {
            throw new BusinessException("没有查看权限");
        }

        // 查询文件列表
        Page<TemplateFile> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<TemplateFile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TemplateFile::getTemplateId, templateId)
                .orderByAsc(TemplateFile::getPath);

        IPage<TemplateFile> filePage = page(page, wrapper);

        // 获取创建者信息
        List<String> creatorIds = filePage.getRecords().stream()
                .map(TemplateFile::getCreatorId)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userMapper.selectBatchIds(creatorIds);
        Map<String, String> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, User::getUsername));

        // 转换为DTO
        return filePage.convert(file -> {
            TemplateFileDTO dto = new TemplateFileDTO();
            BeanUtils.copyProperties(file, dto);
            dto.setCreatorName(creatorMap.get(file.getCreatorId()));
            return dto;
        });
    }

    @Override
    public TemplateFileDTO getFileById(String id) {
        TemplateFile file = getById(id);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        // 检查查看权限
        String userId = jwtUtil.getCurrentUserId();
        Template template = templateMapper.selectById(file.getTemplateId());
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(file.getTemplateId(), userId, "view")) {
            throw new BusinessException("没有查看权限");
        }

        // 获取创建者信息
        User creator = userMapper.selectById(file.getCreatorId());

        TemplateFileDTO dto = new TemplateFileDTO();
        BeanUtils.copyProperties(file, dto);
        if (creator != null) {
            dto.setCreatorName(creator.getUsername());
        }

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createTextFile(String templateId, CreateTemplateFileRequest request) {
        // 检查模板是否存在
        Template template = templateMapper.selectById(templateId);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        // 检查编辑权限
        String userId = jwtUtil.getCurrentUserId();
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(templateId, userId, "edit")) {
            throw new BusinessException("没有编辑权限");
        }

        // 检查路径是否存在
        if (checkPathExists(templateId, request.getPath(), "0")) {
            throw new BusinessException("文件路径已存在");
        }

        // 创建文件记录
        TemplateFile file = new TemplateFile();
        file.setTemplateId(templateId);
        file.setPath(request.getPath());
        file.setType(request.getType());
        file.setContent(request.getContent());
        file.setHash(DigestUtils.sha256Hex(request.getContent()));
        file.setSize((long) request.getContent().length());
        file.setCreatorId(userId);

        save(file);
        return file.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadBinaryFile(String templateId, String path, MultipartFile file) {
        // 检查模板是否存在
        Template template = templateMapper.selectById(templateId);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        // 检查编辑权限
        String userId = jwtUtil.getCurrentUserId();
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(templateId, userId, "edit")) {
            throw new BusinessException("没有编辑权限");
        }

        // 检查路径是否存在
        if (checkPathExists(templateId, path, "0")) {
            throw new BusinessException("文件路径已存在");
        }

        try {
            // 创建文件记录
            TemplateFile templateFile = new TemplateFile();
            templateFile.setTemplateId(templateId);
            templateFile.setPath(path);
            templateFile.setType(getFileType(file.getOriginalFilename()));
            templateFile.setContent(Base64.getEncoder().encodeToString(file.getBytes()));
            templateFile.setHash(DigestUtils.sha256Hex(file.getBytes()));
            templateFile.setSize(file.getSize());
            templateFile.setCreatorId(userId);

            save(templateFile);
            return templateFile.getId();
        } catch (IOException e) {
            throw new BusinessException("文件上传失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTextFile(String id, String content) {
        TemplateFile file = getById(id);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        // 检查编辑权限
        String userId = jwtUtil.getCurrentUserId();
        Template template = templateMapper.selectById(file.getTemplateId());
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(file.getTemplateId(), userId, "edit")) {
            throw new BusinessException("没有编辑权限");
        }

        // 更新文件内容
        file.setContent(content);
        file.setHash(DigestUtils.sha256Hex(content));
        file.setSize((long) content.length());

        updateById(file);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBinaryFile(String id, MultipartFile file) {
        TemplateFile templateFile = getById(id);
        if (templateFile == null) {
            throw new BusinessException("文件不存在");
        }

        // 检查编辑权限
        String userId = jwtUtil.getCurrentUserId();
        Template template = templateMapper.selectById(templateFile.getTemplateId());
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(templateFile.getTemplateId(), userId, "edit")) {
            throw new BusinessException("没有编辑权限");
        }

        try {
            // 更新文件内容
            templateFile.setContent(Base64.getEncoder().encodeToString(file.getBytes()));
            templateFile.setHash(DigestUtils.sha256Hex(file.getBytes()));
            templateFile.setSize(file.getSize());

            updateById(templateFile);
        } catch (IOException e) {
            throw new BusinessException("文件更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(String id) {
        TemplateFile file = getById(id);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        // 检查删除权限
        String userId = jwtUtil.getCurrentUserId();
        Template template = templateMapper.selectById(file.getTemplateId());
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(file.getTemplateId(), userId, "delete")) {
            throw new BusinessException("没有删除权限");
        }

        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFiles(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }

        // 检查权限
        List<TemplateFile> files = listByIds(ids);
        String userId = jwtUtil.getCurrentUserId();

        for (TemplateFile file : files) {
            Template template = templateMapper.selectById(file.getTemplateId());
            if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(file.getTemplateId(), userId, "delete")) {
                throw new BusinessException("没有删除权限");
            }
        }

        removeBatchByIds(ids);
    }

    @Override
    public String getFileContent(String id) {
        TemplateFile file = getById(id);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        // 检查查看权限
        String userId = jwtUtil.getCurrentUserId();
        Template template = templateMapper.selectById(file.getTemplateId());
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(file.getTemplateId(), userId, "view")) {
            throw new BusinessException("没有查看权限");
        }

        return file.getContent();
    }

    @Override
    public byte[] getFileBinaryContent(String id) {
        TemplateFile file = getById(id);
        if (file == null) {
            throw new BusinessException("文件不存在");
        }

        // 检查查看权限
        String userId = jwtUtil.getCurrentUserId();
        Template template = templateMapper.selectById(file.getTemplateId());
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(file.getTemplateId(), userId, "view")) {
            throw new BusinessException("没有查看权限");
        }

        return Base64.getDecoder().decode(file.getContent());
    }

    @Override
    public boolean checkPathExists(String templateId, String path, String excludeId) {
        return baseMapper.checkPathExists(templateId, path, excludeId) > 0;
    }

    /**
     * 检查用户是否是模板创建者
     * @param template 模板
     * @param userId 用户ID
     * @return 是否是创建者
     */
    private boolean isTemplateCreator(Template template, String userId) {
        return template.getCreatorId().equals(userId);
    }

    /**
     * 获取文件类型
     * @param filename 文件名
     * @return 文件类型
     */
    private String getFileType(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "other";
        }

        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "html":
            case "htm":
                return "html";
            case "css":
                return "css";
            case "js":
                return "js";
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
            case "webp":
                return "image";
            default:
                return "other";
        }
    }
} 