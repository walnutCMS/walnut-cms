package com.walnut.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.walnut.cms.dto.CreateTemplateVersionRequest;
import com.walnut.cms.dto.TemplateVersionDTO;
import com.walnut.cms.entity.TemplateVersion;

public interface TemplateVersionService extends IService<TemplateVersion> {
    /**
     * 获取模板版本列表
     * @param templateId 模板ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 版本列表
     */
    IPage<TemplateVersionDTO> getVersionList(Long templateId, Integer pageNum, Integer pageSize);

    /**
     * 获取版本详情
     * @param id 版本ID
     * @return 版本详情
     */
    TemplateVersionDTO getVersionById(Long id);

    /**
     * 创建新版本
     * @param templateId 模板ID
     * @param request 版本信息
     * @return 版本ID
     */
    Long createVersion(Long templateId, CreateTemplateVersionRequest request);

    /**
     * 发布版本
     * @param id 版本ID
     */
    void publishVersion(Long id);

    /**
     * 回滚到指定版本
     * @param id 版本ID
     * @return 新版本ID
     */
    Long rollbackVersion(Long id);

    /**
     * 删除版本
     * @param id 版本ID
     */
    void deleteVersion(Long id);

    /**
     * 获取最新版本
     * @param templateId 模板ID
     * @return 版本详情
     */
    TemplateVersionDTO getLatestVersion(Long templateId);

    /**
     * 生成下一个版本号
     * @param templateId 模板ID
     * @return 版本号
     */
    String generateNextVersion(Long templateId);
} 