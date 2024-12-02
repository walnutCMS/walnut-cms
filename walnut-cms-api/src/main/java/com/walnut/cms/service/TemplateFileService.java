package com.walnut.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.walnut.cms.dto.CreateTemplateFileRequest;
import com.walnut.cms.dto.TemplateFileDTO;
import com.walnut.cms.entity.TemplateFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TemplateFileService extends IService<TemplateFile> {
    /**
     * 获取模板文件列表
     * @param templateId 模板ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 文件列表
     */
    IPage<TemplateFileDTO> getFileList(String templateId, Integer pageNum, Integer pageSize);

    /**
     * 获取文件详情
     * @param id 文件ID
     * @return 文件详情
     */
    TemplateFileDTO getFileById(String id);

    /**
     * 创建文本文件
     * @param templateId 模板ID
     * @param request 文件信息
     * @return 文件ID
     */
    String createTextFile(String templateId, CreateTemplateFileRequest request);

    /**
     * 上传二进制文件
     * @param templateId 模板ID
     * @param path 文件路径
     * @param file 文件
     * @return 文件ID
     */
    String uploadBinaryFile(String templateId, String path, MultipartFile file);

    /**
     * 更新文本文件
     * @param id 文件ID
     * @param content 文件内容
     */
    void updateTextFile(String id, String content);

    /**
     * 更新二进制文件
     * @param id 文件ID
     * @param file 文件
     */
    void updateBinaryFile(String id, MultipartFile file);

    /**
     * 删除文件
     * @param id 文件ID
     */
    void deleteFile(String id);

    /**
     * 批量删除文件
     * @param ids 文件ID列表
     */
    void deleteFiles(List<String> ids);

    /**
     * 获取文件内容
     * @param id 文件ID
     * @return 文件内容
     */
    String getFileContent(String id);

    /**
     * 获取文件字节内容
     * @param id 文件ID
     * @return 文件字节内容
     */
    byte[] getFileBinaryContent(String id);

    /**
     * 检查文件路径是否存在
     * @param templateId 模板ID
     * @param path 文件路径
     * @param excludeId 排除的文件ID
     * @return 是否存在
     */
    boolean checkPathExists(String templateId, String path, String excludeId);
} 