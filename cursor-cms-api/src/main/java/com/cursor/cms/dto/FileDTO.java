package com.cursor.cms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "文件信息")
public class FileDTO {
    @Schema(description = "文件ID")
    private String id;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "原始文件名")
    private String originalName;

    @Schema(description = "文件路径")
    private String path;

    @Schema(description = "访问URL")
    private String viewUrl;

    @Schema(description = "下载URL")
    private String downloadUrl;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "文件类型")
    private String type;

    @Schema(description = "MIME类型")
    private String mimeType;

    @Schema(description = "上传者ID")
    private String uploaderId;

    @Schema(description = "上传者IP")
    private String uploaderIp;

    @Schema(description = "下载次数")
    private Integer downloadCount;

    @Schema(description = "最后下载时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastDownloadTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
} 