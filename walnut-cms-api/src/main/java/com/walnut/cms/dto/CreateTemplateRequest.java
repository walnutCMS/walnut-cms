package com.walnut.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建模板请求")
public class CreateTemplateRequest {
    @Schema(description = "模板名称")
    private String name;
    
    @Schema(description = "模板代码")
    private String code;
    
    @Schema(description = "模板描述")
    private String description;
    
    @Schema(description = "缩略图")
    private String thumbnail;
    
    @Schema(description = "模板类型", example = "page, component, layout")
    private String type;
    
    @Schema(description = "模板分类", example = "blog, news, portfolio")
    private String category;
    
    @Schema(description = "模板结构")
    private String structure;
    
    @Schema(description = "主文件")
    private String mainFile;
    
    @Schema(description = "数据结构")
    private String dataSchema;
    
    @Schema(description = "默认数据")
    private String defaultData;
    
    @Schema(description = "SEO配置")
    private String seoConfig;
    
    @Schema(description = "作者")
    private String author;
    
    @Schema(description = "状态", example = "draft, published")
    private String status = "draft";
} 