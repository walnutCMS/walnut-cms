package com.cursor.cms.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cms_article")
public class Article {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String title;

    private String content;

    private String summary;

    private String thumbnail;

    private String source;

    private String sourceUrl;

    private String keywords;

    private String categoryId;

    private String author;

    private Long viewCount;

    private Long likeCount;

    private Boolean isTop;

    private Boolean isRecommend;

    private String status;

    private String creatorId;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    @TableLogic
    private Boolean deleted;
}