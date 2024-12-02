package com.cursor.cms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cms_template_file")
public class TemplateFile {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String templateId;

    private String path;

    private String type;

    private String content;

    private String mimeType;

    private String encoding;

    private String version;

    private Long size;

    private String hash;

    private String creatorId;

    private String lastModifiedBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;
} 