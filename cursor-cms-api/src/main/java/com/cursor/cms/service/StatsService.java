package com.cursor.cms.service;

import com.cursor.cms.dto.LatestArticleDTO;
import com.cursor.cms.dto.StatsDTO;

import java.util.List;

public interface StatsService {
    /**
     * 获取统计数据
     * @return 统计数据
     */
    StatsDTO getStats();

    /**
     * 获取最新文章
     * @param limit 获取数量
     * @return 文章列表
     */
    List<LatestArticleDTO> getLatestArticles(Integer limit);
} 