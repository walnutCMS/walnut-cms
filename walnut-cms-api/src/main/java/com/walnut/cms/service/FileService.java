package com.walnut.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.walnut.cms.dto.FileDTO;
import com.walnut.cms.entity.File;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface FileService extends IService<File> {
    /**
     * 上传文件
     * @param file 文件
     * @param request HTTP请求
     * @return 文件信息
     */
    FileDTO uploadFile(MultipartFile file, HttpServletRequest request);

    /**
     * 获取文件列表
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 文件列表
     */
    IPage<FileDTO> getFileList(Integer pageNum, Integer pageSize);

    /**
     * 获取文件信息
     * @param id 文件ID
     * @return 文件信息
     */
    FileDTO getFileById(Long id);

    /**
     * 下载文件
     * @param id 文件ID
     * @return 文件资源
     */
    Resource downloadFile(Long id);

    /**
     * 删除文件
     * @param id 文件ID
     */
    void deleteFile(Long id);

    /**
     * 根据文件扩展名获取对应的MediaType
     * @param fileType 文件扩展名
     * @return MediaType
     */
    MediaType getMediaType(String fileType);
} 