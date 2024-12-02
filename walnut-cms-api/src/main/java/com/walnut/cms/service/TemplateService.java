package com.walnut.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.walnut.cms.dto.TemplateDTO;
import com.walnut.cms.dto.TemplateQueryRequest;
import com.walnut.cms.entity.Template;

public interface TemplateService extends IService<Template> {
    /**
     * 获取模板列表
     * @param request 查询参数
     * @return 模板列表
     */
    IPage<TemplateDTO> getTemplateList(TemplateQueryRequest request);

    /**
     * 获取模板详情
     * @param id 模板ID
     * @return 模板详情
     */
    TemplateDTO getTemplateById(String id);

    /**
     * 创建模板
     * @param dto 模板数据
     * @return 模板ID
     */
    String createTemplate(TemplateDTO dto);

    /**
     * 更新模板
     * @param id 模板ID
     * @param dto 更新数据
     */
    void updateTemplate(String id, TemplateDTO dto);

    /**
     * 删除模板
     * @param id 模板ID
     */
    void deleteTemplate(String id);

    /**
     * 发布模板
     * @param id 模板ID
     */
    void publishTemplate(String id);

    /**
     * 下架模板
     * @param id 模板ID
     */
    void unpublishTemplate(String id);

    /**
     * 复制模板
     * @param id 模板ID
     * @return 新模板ID
     */
    String copyTemplate(String id);

    /**
     * 导出模板
     * @param id 模板ID
     * @return 模板内容
     */
    String exportTemplate(String id);

    /**
     * 导入模板
     * @param content 模板内容
     * @return 模板ID
     */
    String importTemplate(String content);

    /**
     * 预览模板
     * @param id 模板ID
     * @param data 预览数据
     * @return 预览结果
     */
    String previewTemplate(String id, Object data);
} 