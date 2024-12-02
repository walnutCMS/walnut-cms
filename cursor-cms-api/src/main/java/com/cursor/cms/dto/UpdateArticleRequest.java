package com.cursor.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新文章请求")
public class UpdateArticleRequest {
    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "栏目ID")
    private String categoryId;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "缩略图")
    private String thumbnail;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "来源URL")
    private String sourceUrl;

    @Schema(description = "关键词")
    private String keywords;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @Schema(description = "状态")
    private String status;
} 