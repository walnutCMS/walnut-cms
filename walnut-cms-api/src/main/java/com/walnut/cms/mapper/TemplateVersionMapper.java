package com.walnut.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.walnut.cms.entity.TemplateVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TemplateVersionMapper extends BaseMapper<TemplateVersion> {
    /**
     * 检查版本号是否存在
     * @param templateId 模板ID
     * @param version 版本号
     * @param excludeId 排除的版本ID
     * @return 存在的数量
     */
    @Select("SELECT COUNT(*) FROM cms_template_version WHERE template_id = #{templateId} AND version = #{version} AND id != #{excludeId} AND deleted = 0")
    int checkVersionExists(Long templateId, String version, Long excludeId);

    /**
     * 获取最新版本号
     * @param templateId 模板ID
     * @return 最新版本号
     */
    @Select("SELECT version FROM cms_template_version WHERE template_id = #{templateId} AND deleted = 0 ORDER BY create_time DESC LIMIT 1")
    String getLatestVersion(Long templateId);
} 