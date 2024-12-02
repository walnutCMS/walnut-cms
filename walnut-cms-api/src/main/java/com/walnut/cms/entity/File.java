package com.walnut.cms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("cms_file")
public class File {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    private String originalName;

    private String path;

    private String storePath;

    private String type;

    private Long size;

    private String mimeType;

    private String creatorId;

    private String uploaderIp;

    private Integer downloadCount;

    private LocalDateTime lastDownloadTime;

    private String md5;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean deleted;
}