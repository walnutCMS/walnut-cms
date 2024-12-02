package com.walnut.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.walnut.cms.entity.TemplatePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface TemplatePermissionMapper extends BaseMapper<TemplatePermission> {
    /**
     * 获取用户对模板的权限列表
     * @param templateId 模板ID
     * @param userId 用户ID
     * @return 权限列表
     */
    @Select("SELECT permission FROM cms_template_permission WHERE template_id = #{templateId} AND user_id = #{userId} AND deleted = 0")
    List<String> getUserPermissions(String templateId, String userId);

    /**
     * 检查用户是否有指定权限
     * @param templateId 模板ID
     * @param userId 用户ID
     * @param permission 权限类型
     * @return 存在的数量
     */
    @Select("SELECT COUNT(*) FROM cms_template_permission WHERE template_id = #{templateId} AND user_id = #{userId} AND permission = #{permission} AND deleted = 0")
    int checkPermission(String templateId, String userId, String permission);
} 