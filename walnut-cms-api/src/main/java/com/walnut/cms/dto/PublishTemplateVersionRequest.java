package com.walnut.cms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "发布模板版本请求")
public class PublishTemplateVersionRequest {
    @Schema(description = "发布说明")
    private String releaseNotes;

    @Schema(description = "兼容性说明")
    private String compatibility;

    @Schema(description = "是否强制发布")
    private Boolean force = false;
}