package com.cursor.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "文章查询请求")
public class ArticleQueryRequest {
    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页数量")
    private Integer pageSize = 10;

    @Schema(description = "标题关键词")
    private String title;

    @Schema(description = "文章ID")
    private String articleId;

    @Schema(description = "栏目ID")
    private String categoryId;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "创建者ID")
    private String creatorId;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;
} 