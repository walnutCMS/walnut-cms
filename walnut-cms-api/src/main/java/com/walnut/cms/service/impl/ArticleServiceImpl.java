package com.walnut.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.walnut.cms.common.constant.CommonConstants;
import com.walnut.cms.common.exception.BusinessException;
import com.walnut.cms.dto.ArticleDTO;
import com.walnut.cms.dto.ArticleQueryRequest;
import com.walnut.cms.dto.CreateArticleRequest;
import com.walnut.cms.dto.UpdateArticleRequest;
import com.walnut.cms.entity.Article;
import com.walnut.cms.entity.Category;
import com.walnut.cms.mapper.ArticleMapper;
import com.walnut.cms.mapper.CategoryMapper;
import com.walnut.cms.service.ArticleService;
import com.walnut.cms.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    private final CategoryMapper categoryMapper;
    private final JwtUtil jwtUtil;

    @Override
    public IPage<ArticleDTO> getArticleList(ArticleQueryRequest request) {
        Page<Article> page = new Page<>(request.getPageNum(), request.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        
        // 标题搜索
        if (StringUtils.hasText(request.getTitle())) {
            wrapper.like(Article::getTitle, request.getTitle());
        }
        
        // 文章ID
        if (StringUtils.hasText(request.getArticleId())) {
            wrapper.eq(Article::getId, request.getArticleId());
        }
        
        // 栏目筛选（包含子栏目）
        if (StringUtils.hasText(request.getCategoryId())) {
            List<String> categoryIds = new ArrayList<>();
            categoryIds.add(request.getCategoryId());
            getAllChildCategoryIds(request.getCategoryId(), categoryIds);
            wrapper.in(Article::getCategoryId, categoryIds);
        }
        
        // 作者筛选
        if (StringUtils.hasText(request.getAuthor())) {
            wrapper.eq(Article::getAuthor, request.getAuthor());
        }
        
        // 创建者筛选
        if (StringUtils.hasText(request.getCreatorId())) {
            wrapper.eq(Article::getCreatorId, request.getCreatorId());
        }
        
        // 状态筛选
        if (StringUtils.hasText(request.getStatus())) {
            wrapper.eq(Article::getStatus, request.getStatus());
        }
        
        // 置顶筛选
        if (request.getIsTop() != null) {
            wrapper.eq(Article::getIsTop, request.getIsTop());
        }
        
        // 推荐筛选
        if (request.getIsRecommend() != null) {
            wrapper.eq(Article::getIsRecommend, request.getIsRecommend());
        }
        
        // 时间范围筛选
        if (request.getStartTime() != null) {
            wrapper.ge(Article::getCreateTime, request.getStartTime());
        }
        if (request.getEndTime() != null) {
            wrapper.le(Article::getCreateTime, request.getEndTime());
        }
        
        // 排序
        wrapper.orderByDesc(Article::getCreateTime);
        
        // 执行查询
        IPage<Article> articlePage = page(page, wrapper);
        
        // 转换为DTO并填充栏目名称
        Set<String> categoryIds = articlePage.getRecords().stream()
                .map(Article::getCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        Map<String, String> categoryMap = new HashMap<>();
        if (!categoryIds.isEmpty()) {
            List<Category> categories = categoryMapper.selectBatchIds(categoryIds);
            categoryMap = categories.stream()
                    .collect(Collectors.toMap(Category::getId, Category::getName));
        }
        
        Map<String, String> finalCategoryMap = categoryMap;
        IPage<ArticleDTO> dtoPage = articlePage.convert(article -> {
            ArticleDTO dto = new ArticleDTO();
            BeanUtils.copyProperties(article, dto);
            dto.setCategoryName(finalCategoryMap.get(article.getCategoryId()));
            return dto;
        });
        
        return dtoPage;
    }

    /**
     * 递归获取所有子栏目ID
     * @param parentId 父栏目ID
     * @param categoryIds 存储所有栏目ID的列表
     */
    private void getAllChildCategoryIds(String parentId, List<String> categoryIds) {
        List<Category> children = categoryMapper.selectList(
            new LambdaQueryWrapper<Category>()
                .eq(Category::getParentId, parentId)
                .eq(Category::getDeleted, false)
        );
        
        if (!children.isEmpty()) {
            List<String> childIds = children.stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            categoryIds.addAll(childIds);
            
            // 递归获取每个子栏目的子栏目
            for (String childId : childIds) {
                getAllChildCategoryIds(childId, categoryIds);
            }
        }
    }

    @Override
    public ArticleDTO getArticleById(String id) {
        Article article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        Category category = categoryMapper.selectById(article.getCategoryId());

        ArticleDTO dto = new ArticleDTO();
        BeanUtils.copyProperties(article, dto);
        if (category != null) {
            dto.setCategoryName(category.getName());
        }

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveArticle(CreateArticleRequest request) {
        Article article = new Article();
        BeanUtils.copyProperties(request, article);
        article.setAuthor(jwtUtil.getCurrentUsername());
        article.setCreatorId(jwtUtil.getCurrentUserId());
        article.setViewCount(0L);
        article.setLikeCount(0L);
        article.setStatus(CommonConstants.ARTICLE_STATUS_DRAFT);

        save(article);
        return article.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(String id, UpdateArticleRequest request) {
        Article article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        BeanUtils.copyProperties(request, article);
        updateById(article);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(String id) {
        Article article = getById(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishArticle(String id) {
        Article article = getById(id);
        if (article != null) {
            article.setStatus(CommonConstants.ARTICLE_STATUS_PUBLISHED);
            article.setPublishTime(LocalDateTime.now());
            updateById(article);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unpublishArticle(String id) {
        Article article = getById(id);
        if (article != null) {
            article.setStatus(CommonConstants.ARTICLE_STATUS_DRAFT);
            article.setPublishTime(null);
            updateById(article);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticleBatch(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            removeBatchByIds(ids);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishArticleBatch(List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            List<Article> articles = listByIds(ids);
            LocalDateTime now = LocalDateTime.now();
            articles.forEach(article -> {
                article.setStatus(CommonConstants.ARTICLE_STATUS_PUBLISHED);
                article.setPublishTime(now);
            });
            updateBatchById(articles);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long incrementViewCount(String id) {
        Article article = getById(id);
        if (article != null) {
            article.setViewCount(article.getViewCount() + 1);
            updateById(article);
            return article.getViewCount();
        }
        return 0L;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long incrementLikeCount(String id) {
        Article article = getById(id);
        if (article != null) {
            article.setLikeCount(article.getLikeCount() + 1);
            updateById(article);
            return article.getLikeCount();
        }
        return 0L;
    }

    @Override
    public Long getArticleCountByCategory(String categoryId) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getCategoryId, categoryId);
        return count(wrapper);
    }

    /**
     * 递归获取所有子栏目ID
     * @param categoryId 当前栏目ID
     * @return 所有子栏目ID集合
     */
    private Set<String> getAllChildCategoryIds(String categoryId) {
        Set<String> result = new HashSet<>();

        // 查询直接子栏目
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, categoryId);
        List<Category> children = categoryMapper.selectList(wrapper);

        // 递归处理每个子栏目
        for (Category child : children) {
            result.add(child.getId());
            result.addAll(getAllChildCategoryIds(child.getId()));
        }

        return result;
    }
}