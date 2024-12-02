package com.cursor.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cms_template")
public class Template {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    private String code;

    private String description;

    private String thumbnail;

    private String type;

    private String category;

    private String structure;

    private String mainFile;

    private String dataSchema;

    private String defaultData;

    private String seoConfig;

    private String version;

    private String author;

    private String status;

    private String creatorId;

    private Integer hits;

    private LocalDateTime lastUsedTime;

    private Boolean isSystem;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;
} 