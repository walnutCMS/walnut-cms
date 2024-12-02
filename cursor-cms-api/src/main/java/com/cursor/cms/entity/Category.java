package com.cursor.cms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cms_category")
public class Category {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    private String description;

    private String parentId;

    private Integer sort;

    private String path;

    private Integer level;

    private Boolean isShow;

    private String icon;

    private String template;

    private String articleTemplate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;
}