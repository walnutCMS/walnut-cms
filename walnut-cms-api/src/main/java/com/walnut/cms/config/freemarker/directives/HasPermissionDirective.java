package com.walnut.cms.config.freemarker.directives;

import com.walnut.cms.service.TemplatePermissionService;
import com.walnut.cms.utils.JwtUtil;
import freemarker.core.Environment;
import freemarker.template.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HasPermissionDirective implements TemplateDirectiveModel {
    
    private final TemplatePermissionService templatePermissionService;
    private final JwtUtil jwtUtil;

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        // 检查参数
        if (!params.containsKey("templateId")) {
            throw new TemplateModelException("Missing templateId parameter");
        }
        if (!params.containsKey("permission")) {
            throw new TemplateModelException("Missing permission parameter");
        }
        if (body == null) {
            throw new TemplateModelException("Missing body");
        }

        // 获取参数
        String templateId = ((TemplateScalarModel) params.get("templateId")).getAsString();
        String permission = ((TemplateScalarModel) params.get("permission")).getAsString();

        // 检查权限
        try {
            String userId = jwtUtil.getCurrentUserId();
            if (templatePermissionService.hasPermission(templateId, userId, permission)) {
                body.render(env.getOut());
            }
        } catch (Exception e) {
            // 权限检查失败，不渲染内容
        }
    }
} 