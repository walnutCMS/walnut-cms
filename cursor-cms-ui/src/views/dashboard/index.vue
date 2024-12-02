<template>
  <div class="dashboard">
    <!-- 数据统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="data-card">
          <div class="data-header">
            <span class="title">文章总数</span>
            <i class="el-icon-document"></i>
          </div>
          <div class="data-content">
            <span class="number">{{ stats.articleCount }}</span>
            <span class="label">篇</span>
          </div>
          <div class="data-footer">
            <div class="status-info">
              <div class="status-item">
                <span class="label">已发布</span>
                <span class="value">{{ stats.publishedCount }}</span>
              </div>
              <div class="status-item">
                <span class="label">草稿</span>
                <span class="value">{{ stats.draftCount }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="data-card">
          <div class="data-header">
            <span class="title">栏目总数</span>
            <i class="el-icon-folder"></i>
          </div>
          <div class="data-content">
            <span class="number">{{ stats.categoryCount }}</span>
            <span class="label">个</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="data-card">
          <div class="data-header">
            <span class="title">浏览总数</span>
            <i class="el-icon-view"></i>
          </div>
          <div class="data-content">
            <span class="number">{{ stats.viewCount }}</span>
            <span class="label">次</span>
          </div>
          <div class="data-footer">
            <div class="status-info">
              <div class="status-item">
                <span class="label">今日</span>
                <span class="value">{{ stats.todayViewCount }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card class="chart-card">
          <div slot="header">
            <span>数据趋势（最近一周）</span>
          </div>
          <div ref="trendChart" style="height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最新文章 -->
    <el-row class="latest-row">
      <el-col :span="24">
        <el-card class="latest-articles">
          <div slot="header">
            <span>最新文章</span>
            <el-button type="text" @click="$router.push('/article/list')">
              查看更多
            </el-button>
          </div>
          <el-table :data="latestArticles" style="width: 100%">
            <el-table-column prop="title" label="标题" min-width="200">
              <template slot-scope="{row}">
                <router-link
                  :to="`/article/edit/${row.id}`"
                  class="link-type"
                >
                  {{ row.title }}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column prop="categoryName" label="栏目" width="120" />
            <el-table-column prop="author" label="作者" width="120" />
            <el-table-column prop="viewCount" label="浏览" width="100" align="center" />
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.status === 'published' ? 'success' : 'warning'">
                  {{ row.status === 'published' ? '已发布' : '草稿' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStats } from '@/api/stats'
import {getArticleList} from '@/api/article'
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        articleCount: 0,
        categoryCount: 0,
        publishedCount: 0,
        draftCount: 0,
        viewCount: 0,
        todayViewCount: 0,
        weekData: {
          dates: [],
          views: [],
          articles: []
        }
      },
      latestArticles: [],
      trendChart: null
    }
  },
  created() {
    this.getStats()
    this.getLatestArticles()
  },
  mounted() {
    this.initChart()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.trendChart?.dispose()
  },
  methods: {
    async getStats() {
      try {
        const res = await getStats()
        if (res.code === 200) {
          this.stats = res.data
          this.updateChart()
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    async getLatestArticles() {
      try {
        const res = await getArticleList({
          pageNum: 1,
          pageSize: 10,
          orderBy: 'createTime',
          order: 'desc'
        })
        if (res.code === 200) {
          this.latestArticles = res.data.list || []
        }
      } catch (error) {
        console.error('获取最新文章失败:', error)
      }
    },
    initChart() {
      this.trendChart = echarts.init(this.$refs.trendChart)
      this.updateChart()
    },
    updateChart() {
      if (!this.trendChart) return

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['浏览量', '文章数']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.stats.weekData.dates,
          axisTick: {
            alignWithLabel: true
          }
        },
        yAxis: [
          {
            type: 'value',
            name: '浏览量',
            position: 'left'
          },
          {
            type: 'value',
            name: '文章数',
            position: 'right'
          }
        ],
        series: [
          {
            name: '浏览量',
            type: 'line',
            smooth: true,
            data: this.stats.weekData.views,
            areaStyle: {
              opacity: 0.3
            }
          },
          {
            name: '文章数',
            type: 'bar',
            yAxisIndex: 1,
            data: this.stats.weekData.articles
          }
        ]
      }

      this.trendChart.setOption(option)
    },
    handleResize() {
      this.trendChart?.resize()
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  padding: 20px;

  .el-row {
    margin-bottom: 20px;
  }

  .data-card {
    height: 180px;

    .data-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .title {
        font-size: 16px;
        color: var(--text-color);
      }

      i {
        font-size: 24px;
        color: var(--primary-color);
      }
    }

    .data-content {
      text-align: center;
      margin-bottom: 20px;

      .number {
        font-size: 36px;
        font-weight: bold;
        color: var(--text-color);
      }

      .label {
        margin-left: 5px;
        font-size: 14px;
        color: var(--text-color-secondary);
      }
    }

    .data-footer {
      .status-info {
        display: flex;
        justify-content: space-around;

        .status-item {
          text-align: center;

          .label {
            display: block;
            font-size: 12px;
            color: var(--text-color-secondary);
            margin-bottom: 5px;
          }

          .value {
            font-size: 16px;
            color: var(--text-color);
          }
        }
      }
    }
  }

  .chart-card {
    .el-card__header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .latest-articles {
    .el-card__header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .link-type {
      color: var(--primary-color);
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}

// 暗色主题适配
[data-theme='dark'] {
  .dashboard {
    .data-card {
      background-color: var(--background-color-base);
      border-color: var(--border-color-base);
    }

    .chart-card, .latest-articles {
      background-color: var(--background-color-base);
      border-color: var(--border-color-base);
    }
  }
}
</style>
