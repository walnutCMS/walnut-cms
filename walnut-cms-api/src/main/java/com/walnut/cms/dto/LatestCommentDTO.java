package com.walnut.cms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "最新评论")
public class LatestCommentDTO {
    @Schema(description = "评论ID")
    private String id;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "评论者")
    private String author;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
} 