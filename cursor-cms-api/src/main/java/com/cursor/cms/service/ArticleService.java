package com.cursor.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cursor.cms.dto.ArticleDTO;
import com.cursor.cms.dto.ArticleQueryRequest;
import com.cursor.cms.dto.CreateArticleRequest;
import com.cursor.cms.dto.UpdateArticleRequest;
import com.cursor.cms.entity.Article;

import java.util.List;

public interface ArticleService extends IService<Article> {
    /**
     * 获取文章列表
     * @param request 查询参数
     * @return 文章列表
     */
    IPage<ArticleDTO> getArticleList(ArticleQueryRequest request);

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    ArticleDTO getArticleById(String id);

    /**
     * 创建文章
     * @param request 文章数据
     * @return 文章ID
     */
    String saveArticle(CreateArticleRequest request);

    /**
     * 更新文章
     * @param id 文章ID
     * @param request 更新数据
     */
    void updateArticle(String id, UpdateArticleRequest request);

    /**
     * 删除文章
     * @param id 文章ID
     */
    void deleteArticle(String id);

    /**
     * 发布文章
     * @param id 文章ID
     */
    void publishArticle(String id);

    /**
     * 下架文章
     * @param id 文章ID
     */
    void unpublishArticle(String id);

    /**
     * 批量删除文章
     * @param ids 文章ID列表
     */
    void deleteArticleBatch(List<String> ids);

    /**
     * 批量发布文章
     * @param ids 文章ID列表
     */
    void publishArticleBatch(List<String> ids);

    /**
     * 更新文章浏览次数
     * @param id 文章ID
     * @return 更新后的浏览次数
     */
    Long incrementViewCount(String id);

    /**
     * 更新文章点赞数
     * @param id 文章ID
     * @return 更新后的点赞数
     */
    Long incrementLikeCount(String id);

    /**
     * 获取分类下的文章数量
     * @param categoryId 分类ID
     * @return 文章数量
     */
    Long getArticleCountByCategory(String categoryId);
}