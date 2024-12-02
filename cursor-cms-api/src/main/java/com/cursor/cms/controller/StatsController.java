package com.cursor.cms.controller;

import com.cursor.cms.common.Result;
import com.cursor.cms.dto.LatestArticleDTO;
import com.cursor.cms.dto.StatsDTO;
import com.cursor.cms.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "统计管理")
public class StatsController {
    private final StatsService statsService;

    @GetMapping
    @Operation(summary = "获取统计数据")
    public Result<StatsDTO> getStats() {
        return Result.success(statsService.getStats());
    }

    @GetMapping("/article/latest")
    @Operation(summary = "获取最新文章")
    public Result<List<LatestArticleDTO>> getLatestArticles(
            @RequestParam(defaultValue = "5") Integer limit) {
        return Result.success(statsService.getLatestArticles(limit));
    }
}