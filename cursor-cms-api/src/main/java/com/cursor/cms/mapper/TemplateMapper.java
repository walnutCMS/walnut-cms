package com.cursor.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cursor.cms.entity.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TemplateMapper extends BaseMapper<Template> {
    /**
     * 检查模板代码是否存在
     * @param code 模板代码
     * @param excludeId 排除的模板ID
     * @return 存在的数量
     */
    @Select("SELECT COUNT(*) FROM cms_template WHERE code = #{code} AND id != #{excludeId} AND deleted = 0")
    int checkCodeExists(String code, Long excludeId);
} 