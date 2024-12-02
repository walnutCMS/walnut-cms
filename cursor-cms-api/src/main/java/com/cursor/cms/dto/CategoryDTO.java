package com.cursor.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "栏目信息")
public class CategoryDTO {
    @Schema(description = "栏目ID")
    private String id;

    @Schema(description = "栏目名称")
    private String name;

    @Schema(description = "父级ID")
    private String parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "层级")
    private Integer level;

    @Schema(description = "是否显示")
    private Boolean isShow;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "模板")
    private String template;

    @Schema(description = "文章模板")
    private String articleTemplate;

    @Schema(description = "子栏目")
    private List<CategoryDTO> children;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}