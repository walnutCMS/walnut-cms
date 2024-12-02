package com.cursor.cms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cursor.cms.common.Result;
import com.cursor.cms.dto.*;
import com.cursor.cms.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
@Tag(name = "文章管理")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/list")
    @Operation(summary = "获取文章列表")
    public Result<Map<String, Object>> getArticleList(ArticleQueryRequest request) {
        IPage<ArticleDTO> page = articleService.getArticleList(request);
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取文章详情")
    public Result<ArticleDTO> getArticle(@PathVariable String id) {
        return Result.success(articleService.getArticleById(id));
    }

    @PostMapping
    @Operation(summary = "创建文章")
    public Result<Map<String, String>> createArticle(@RequestBody CreateArticleRequest request) {
        String articleId = articleService.saveArticle(request);
        Map<String, String> data = new HashMap<>();
        data.put("id", articleId);
        return Result.success(data);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新文章")
    public Result<Void> updateArticle(@PathVariable String id, @RequestBody UpdateArticleRequest request) {
        articleService.updateArticle(id, request);
        return Result.success(null);
    }

    @PutMapping("/publish/{id}")
    @Operation(summary = "发布文章")
    public Result<Void> publishArticle(@PathVariable String id) {
        articleService.publishArticle(id);
        return Result.success(null);
    }

    @PutMapping("/unpublish/{id}")
    @Operation(summary = "下架文章")
    public Result<Void> unpublishArticle(@PathVariable String id) {
        articleService.unpublishArticle(id);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章")
    public Result<Void> deleteArticle(@PathVariable String id) {
        articleService.deleteArticle(id);
        return Result.success(null);
    }

    @PostMapping("/batch/publish")
    @Operation(summary = "批量发布文章")
    public Result<Void> batchPublishArticles(@RequestBody Map<String, List<String>> request) {
        articleService.publishArticleBatch(request.get("ids"));
        return Result.success(null);
    }

    @DeleteMapping("/batch/delete")
    @Operation(summary = "批量删除文章")
    public Result<Void> batchDeleteArticles(@RequestBody Map<String, List<String>> request) {
        articleService.deleteArticleBatch(request.get("ids"));
        return Result.success(null);
    }

    @PostMapping("/{id}/view")
    @Operation(summary = "增加浏览次数")
    public Result<Map<String, Long>> incrementViewCount(@PathVariable String id) {
        Long viewCount = articleService.incrementViewCount(id);
        Map<String, Long> data = new HashMap<>();
        data.put("viewCount", viewCount);
        return Result.success(data);
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "增加点赞数")
    public Result<Map<String, Long>> incrementLikeCount(@PathVariable String id) {
        Long likeCount = articleService.incrementLikeCount(id);
        Map<String, Long> data = new HashMap<>();
        data.put("likeCount", likeCount);
        return Result.success(data);
    }
}