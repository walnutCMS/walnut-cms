package com.cursor.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cursor.cms.common.exception.BusinessException;
import com.cursor.cms.dto.TemplateDTO;
import com.cursor.cms.dto.TemplateQueryRequest;
import com.cursor.cms.entity.Template;
import com.cursor.cms.mapper.TemplateMapper;
import com.cursor.cms.service.TemplateService;
import com.cursor.cms.service.TemplatePermissionService;
import com.cursor.cms.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.StringWriter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {
    private final TemplatePermissionService templatePermissionService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public IPage<TemplateDTO> getTemplateList(TemplateQueryRequest request) {
        LambdaQueryWrapper<Template> wrapper = new LambdaQueryWrapper<Template>()
                .like(StringUtils.hasText(request.getName()), Template::getName, request.getName())
                .eq(StringUtils.hasText(request.getType()), Template::getType, request.getType())
                .eq(StringUtils.hasText(request.getCategory()), Template::getCategory, request.getCategory())
                .eq(request.getStatus() != null, Template::getStatus, request.getStatus())
                .orderByDesc(Template::getCreateTime);

        Page<Template> page = new Page<>(request.getPageNum(), request.getPageSize());
        return page(page, wrapper).convert(template -> {
            TemplateDTO dto = new TemplateDTO();
            BeanUtils.copyProperties(template, dto);
            return dto;
        });
    }

    @Override
    public TemplateDTO getTemplateById(String id) {
        Template template = getById(id);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        // 检查查看权限
        String userId = jwtUtil.getCurrentUserId();
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(id, userId, "view")) {
            throw new BusinessException("没有查看权限");
        }

        return convertToDTO(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createTemplate(TemplateDTO dto) {
        Template template = new Template();
        BeanUtils.copyProperties(dto, template);
        
        // 设置创建者
        template.setCreatorId(jwtUtil.getCurrentUserId());
        template.setStatus("draft");
        
        save(template);
        return template.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplate(String id, TemplateDTO dto) {
        Template template = getById(id);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        // 检查编辑权限
        String userId = jwtUtil.getCurrentUserId();
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(id, userId, "edit")) {
            throw new BusinessException("没有编辑权限");
        }

        BeanUtils.copyProperties(dto, template);
        updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(String id) {
        Template template = getById(id);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        // 检查删除权限
        String userId = jwtUtil.getCurrentUserId();
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(id, userId, "delete")) {
            throw new BusinessException("没有删除权限");
        }

        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishTemplate(String id) {
        Template template = getById(id);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        // 检查发布权限
        String userId = jwtUtil.getCurrentUserId();
        if (!isTemplateCreator(template, userId) && !templatePermissionService.hasPermission(id, userId, "publish")) {
            throw new BusinessException("没有发布权限");
        }

        template.setStatus("published");
        updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unpublishTemplate(String id) {
        Template template = getById(id);
        if (template == null) {
            throw new BusinessException("Template not found");
        }

        template.setStatus("draft");
        updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyTemplate(String id) {
        Template template = getById(id);
        if (template == null) {
            throw new BusinessException("Template not found");
        }

        // 创建新模板
        Template newTemplate = new Template();
        BeanUtils.copyProperties(template, newTemplate);
        newTemplate.setId(null);
        newTemplate.setName(template.getName() + " (Copy)");
        newTemplate.setCode(template.getCode() + "-" + UUID.randomUUID().toString().substring(0, 8));
        newTemplate.setStatus("draft");
        newTemplate.setCreatorId(jwtUtil.getCurrentUserId());

        save(newTemplate);
        return newTemplate.getId();
    }

    @Override
    public String exportTemplate(String id) {
        Template template = getById(id);
        if (template == null) {
            throw new BusinessException("Template not found");
        }

        try {
            return objectMapper.writeValueAsString(template);
        } catch (Exception e) {
            log.error("Failed to export template", e);
            throw new BusinessException("Failed to export template");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importTemplate(String content) {
        try {
            Template template = objectMapper.readValue(content, Template.class);
            template.setId(null);
            template.setStatus("draft");
            template.setCreatorId(jwtUtil.getCurrentUserId());

            // 检查模板代码是否存在
            if (baseMapper.checkCodeExists(template.getCode(), null) > 0) {
                template.setCode(template.getCode() + "-" + UUID.randomUUID().toString().substring(0, 8));
            }

            save(template);
            return template.getId();
        } catch (Exception e) {
            log.error("Failed to import template", e);
            throw new BusinessException("Failed to import template");
        }
    }

    @Override
    public String previewTemplate(String id, Object data) {
        Template template = getById(id);
        if (template == null) {
            throw new BusinessException("模板不存在");
        }

        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setDefaultEncoding("UTF-8");
            
            // Create a string template loader
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            stringLoader.putTemplate("template", template.getCode());
            cfg.setTemplateLoader(stringLoader);
            
            // Get the template and process it
            freemarker.template.Template ftl = cfg.getTemplate("template");
            StringWriter writer = new StringWriter();
            ftl.process(data, writer);
            
            return writer.toString();
        } catch (Exception e) {
            log.error("Template preview failed", e);
            throw new BusinessException("模板预览失败: " + e.getMessage());
        }
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
     * 转换为DTO
     * @param template 模板实体
     * @return 模板DTO
     */
    private TemplateDTO convertToDTO(Template template) {
        TemplateDTO dto = new TemplateDTO();
        BeanUtils.copyProperties(template, dto);
        return dto;
    }
} 