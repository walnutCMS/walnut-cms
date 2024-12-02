package com.walnut.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "模板查询请求")
public class TemplateQueryRequest {
    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页数量")
    private Integer pageSize = 10;

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "模板类型")
    private String type;

    @Schema(description = "模板分类")
    private String category;

    @Schema(description = "是否响应式")
    private Boolean isResponsive;

    @Schema(description = "状态")
    private String status;
} 