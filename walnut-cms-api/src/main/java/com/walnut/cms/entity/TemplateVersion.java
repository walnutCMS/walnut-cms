package com.walnut.cms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cms_template_version")
public class TemplateVersion {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String templateId;

    private String version;

    private String content;

    private String description;

    private String changelog;

    private String compatibility;

    private String releaseNotes;

    private String status;

    private String creatorId;

    private String publisherId;

    private LocalDateTime publishTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;
} 