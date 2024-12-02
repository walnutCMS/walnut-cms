package com.cursor.cms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cms_template_permission")
public class TemplatePermission {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String templateId;
    
    private String userId;
    
    private String permission;  // view, edit, delete, publish
    
    private String grantedBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Boolean deleted;
} 