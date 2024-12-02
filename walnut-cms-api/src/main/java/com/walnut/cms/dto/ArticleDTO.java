package com.walnut.cms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "文章信息")
public class ArticleDTO {
    @Schema(description = "文章ID")
    private String id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

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

    @Schema(description = "栏目ID")
    private String categoryId;

    @Schema(description = "栏目名称")
    private String categoryName;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "浏览次数")
    private Long viewCount;

    @Schema(description = "点赞数")
    private Long likeCount;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "创建者ID")
    private String creatorId;

    @Schema(description = "创建者名称")
    private String creatorName;

    @Schema(description = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}