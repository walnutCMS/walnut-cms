package com.walnut.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.walnut.cms.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 获取直接子栏目
     * @param parentId 父栏目ID
     * @return 子栏目列表
     */
    @Select("SELECT * FROM cms_category WHERE parent_id = #{parentId} AND deleted = 0")
    List<Category> selectChildren(String parentId);

    /**
     * 获取直接父栏目
     * @param categoryId 栏目ID
     * @return 父栏目
     */
    @Select("SELECT * FROM cms_category WHERE id = (SELECT parent_id FROM cms_category WHERE id = #{categoryId} AND deleted = 0) AND deleted = 0")
    Category selectParent(String categoryId);
}