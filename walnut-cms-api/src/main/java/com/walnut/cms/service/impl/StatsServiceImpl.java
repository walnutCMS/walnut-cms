package com.walnut.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.walnut.cms.dto.LatestArticleDTO;
import com.walnut.cms.dto.StatsDTO;
import com.walnut.cms.entity.Article;
import com.walnut.cms.mapper.ArticleMapper;
import com.walnut.cms.mapper.CategoryMapper;
import com.walnut.cms.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public StatsDTO getStats() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime tomorrow = today.plusDays(1);
        LocalDateTime weekAgo = today.minusWeeks(1);

        StatsDTO stats = new StatsDTO();

        // 文章统计
        stats.setArticleCount(articleMapper.selectCount(null));

        // 栏目统计
        stats.setCategoryCount(categoryMapper.selectCount(null));

        // 已发布和草稿统计
        stats.setPublishedCount(articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, "published")));
        stats.setDraftCount(articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, "draft")));

        // 浏览统计
        stats.setViewCount(articleMapper.sumViewCount());
        stats.setTodayViewCount(articleMapper.sumViewCountBetween(today, tomorrow));

        // 周数据统计
        StatsDTO.WeekData weekData = new StatsDTO.WeekData();
        List<String> dates = new ArrayList<>();
        List<Long> views = new ArrayList<>();
        List<Long> articles = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (LocalDateTime date = weekAgo; !date.isAfter(today); date = date.plusDays(1)) {
            LocalDateTime nextDay = date.plusDays(1);

            dates.add(date.format(formatter));
            views.add(articleMapper.sumViewCountBetween(date, nextDay));
            articles.add(articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                    .between(Article::getPublishTime, date, nextDay)));
        }

        weekData.setDates(dates);
        weekData.setViews(views);
        weekData.setArticles(articles);
        stats.setWeekData(weekData);

        return stats;
    }

    @Override
    public List<LatestArticleDTO> getLatestArticles(Integer limit) {
        List<Article> articles = articleMapper.selectLatestArticles(limit);
        return articles.stream().map(article -> {
            LatestArticleDTO dto = new LatestArticleDTO();
            dto.setId(article.getId());
            dto.setTitle(article.getTitle());
            dto.setAuthor(article.getAuthor());
            dto.setPublishTime(article.getPublishTime());
            return dto;
        }).collect(Collectors.toList());
    }
} 