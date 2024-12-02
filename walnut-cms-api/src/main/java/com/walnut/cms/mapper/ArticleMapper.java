package com.walnut.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.walnut.cms.dto.ArticleDTO;
import com.walnut.cms.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    IPage<ArticleDTO> selectArticlePage(Page<?> page, @Param("categoryIds") List<String> categoryIds);

    @Select("SELECT COALESCE(SUM(view_count), 0) FROM cms_article")
    Long sumViewCount();

    @Select("SELECT COALESCE(SUM(view_count), 0) FROM cms_article WHERE create_time BETWEEN #{start} AND #{end}")
    Long sumViewCountBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Select("SELECT * FROM cms_article ORDER BY create_time DESC LIMIT #{limit}")
    List<Article> selectLatestArticles(@Param("limit") Integer limit);

    @Select("SELECT id FROM cms_category WHERE parent_id = #{parentId} AND deleted = 0")
    List<String> selectChildCategoryIds(@Param("parentId") String parentId);
}