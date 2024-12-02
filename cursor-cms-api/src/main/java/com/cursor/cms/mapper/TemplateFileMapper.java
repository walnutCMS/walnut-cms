package com.cursor.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cursor.cms.entity.TemplateFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TemplateFileMapper extends BaseMapper<TemplateFile> {
    /**
     * 检查文件路径是否存在
     * @param templateId 模板ID
     * @param path 文件路径
     * @param excludeId 排除的文件ID
     * @return 存在的数量
     */
    @Select("SELECT COUNT(*) FROM cms_template_file WHERE template_id = #{templateId} AND path = #{path} AND id != #{excludeId} AND deleted = 0")
    int checkPathExists(@Param("templateId") String templateId, @Param("path") String path, @Param("excludeId") String excludeId);

    /**
     * 根据模板ID和文件类型查询文件列表
     * @param templateId 模板ID
     * @param type 文件类型
     * @return 文件列表
     */
    @Select("SELECT * FROM cms_template_file WHERE template_id = #{templateId} AND type = #{type} AND deleted = 0 ORDER BY path")
    List<TemplateFile> selectByTemplateIdAndType(@Param("templateId") String templateId, @Param("type") String type);

    /**
     * 根据模板ID和路径查询文件
     * @param templateId 模板ID
     * @param path 文件路径
     * @return 文件信息
     */
    @Select("SELECT * FROM cms_template_file WHERE template_id = #{templateId} AND path = #{path} AND deleted = 0 LIMIT 1")
    TemplateFile selectByTemplateIdAndPath(@Param("templateId") String templateId, @Param("path") String path);

    /**
     * 更新文件内容和哈希值
     * @param id 文件ID
     * @param content 文件内容
     * @param hash 文件哈希值
     * @param size 文件大小
     * @return 更新数量
     */
    @Update("UPDATE cms_template_file SET content = #{content}, hash = #{hash}, size = #{size} WHERE id = #{id} AND deleted = 0")
    int updateContent(@Param("id") String id, @Param("content") String content, @Param("hash") String hash, @Param("size") Long size);

    /**
     * 批量删除文件
     * @param templateId 模板ID
     * @return 删除数量
     */
    @Update("UPDATE cms_template_file SET deleted = 1 WHERE template_id = #{templateId} AND deleted = 0")
    int deleteByTemplateId(@Param("templateId") String templateId);

    /**
     * 获取模板文件总大小
     * @param templateId 模板ID
     * @return 总大小(字节)
     */
    @Select("SELECT COALESCE(SUM(size), 0) FROM cms_template_file WHERE template_id = #{templateId} AND deleted = 0")
    Long getTemplateTotalSize(@Param("templateId") String templateId);
} 