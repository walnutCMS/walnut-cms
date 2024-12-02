package com.walnut.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "统计数据")
public class StatsDTO {
    @Schema(description = "文章总数")
    private Long articleCount;

    @Schema(description = "栏目总数")
    private Long categoryCount;

    @Schema(description = "已发布文章数")
    private Long publishedCount;

    @Schema(description = "草稿文章数")
    private Long draftCount;

    @Schema(description = "总浏览量")
    private Long viewCount;

    @Schema(description = "今日浏览量")
    private Long todayViewCount;

    @Schema(description = "周数据")
    private WeekData weekData;

    @Data
    @Schema(description = "周数据")
    public static class WeekData {
        @Schema(description = "日期列表")
        private List<String> dates;

        @Schema(description = "浏览量列表")
        private List<Long> views;

        @Schema(description = "文章数列表")
        private List<Long> articles;
    }
} 