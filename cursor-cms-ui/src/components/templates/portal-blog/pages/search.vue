<template>
  <div class="portal-search">
    <!-- 搜索头部 -->
    <header class="search-header">
      <h1 class="search-title">搜索结果</h1>
      <div class="search-info">
        关键词 "{{ keyword }}" 共找到 {{ total }} 个结果
      </div>
      
      <!-- 搜索过滤器 -->
      <div class="search-filters">
        <el-radio-group v-model="currentType" @change="handleTypeChange">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="article">文章</el-radio-button>
          <el-radio-button label="category">分类</el-radio-button>
          <el-radio-button label="tag">标签</el-radio-button>
        </el-radio-group>
        
        <el-select v-model="sortBy" placeholder="排序方式" @change="handleSortChange">
          <el-option label="相关度" value="relevance"></el-option>
          <el-option label="最新发布" value="time"></el-option>
          <el-option label="最多浏览" value="views"></el-option>
          <el-option label="最多评论" value="comments"></el-option>
        </el-select>
      </div>
    </header>

    <!-- 搜索结果 -->
    <div class="search-results">
      <!-- 文章结果 -->
      <template v-if="currentType === 'all' || currentType === 'article'">
        <div 
          v-for="article in articles" 
          :key="article.id"
          class="result-item article-item"
          @click="handleArticleClick(article)"
        >
          <div class="item-image" :style="{ backgroundImage: `url(${article.cover})` }">
            <div class="item-category">{{ article.category }}</div>
          </div>
          <div class="item-info">
            <h2 class="item-title">
              <span v-html="highlightKeyword(article.title)"></span>
            </h2>
            <p class="item-excerpt" v-html="highlightKeyword(article.excerpt)"></p>
            <div class="item-meta">
              <span class="meta-item">
                <i class="el-icon-user"></i>
                {{ article.author }}
              </span>
              <span class="meta-item">
                <i class="el-icon-time"></i>
                {{ article.createTime }}
              </span>
              <span class="meta-item">
                <i class="el-icon-view"></i>
                {{ article.viewCount }}
              </span>
              <span class="meta-item">
                <i class="el-icon-chat-line-square"></i>
                {{ article.commentCount }}
              </span>
            </div>
          </div>
        </div>
      </template>

      <!-- 分类结果 -->
      <template v-if="currentType === 'all' || currentType === 'category'">
        <div 
          v-for="category in categories" 
          :key="category.id"
          class="result-item category-item"
          @click="handleCategoryClick(category)"
        >
          <div class="item-icon">
            <i class="el-icon-folder"></i>
          </div>
          <div class="item-info">
            <h3 class="item-title">
              <span v-html="highlightKeyword(category.name)"></span>
            </h3>
            <p class="item-description" v-html="highlightKeyword(category.description)"></p>
            <div class="item-meta">
              <span class="meta-item">
                <i class="el-icon-document"></i>
                {{ category.articleCount }} 篇文章
              </span>
              <span class="meta-item">
                <i class="el-icon-view"></i>
                {{ category.viewCount }} 次浏览
              </span>
            </div>
          </div>
        </div>
      </template>

      <!-- 标签结果 -->
      <template v-if="currentType === 'all' || currentType === 'tag'">
        <div 
          v-for="tag in tags" 
          :key="tag.id"
          class="result-item tag-item"
          @click="handleTagClick(tag)"
        >
          <div class="item-icon">
            <i class="el-icon-collection-tag"></i>
          </div>
          <div class="item-info">
            <h3 class="item-title">
              <span v-html="highlightKeyword(tag.name)"></span>
            </h3>
            <p class="item-description" v-html="highlightKeyword(tag.description)"></p>
            <div class="item-meta">
              <span class="meta-item">
                <i class="el-icon-document"></i>
                {{ tag.articleCount }} 篇文章
              </span>
            </div>
          </div>
        </div>
      </template>

      <!-- 加载更多 -->
      <div v-if="hasMore" class="load-more">
        <el-button 
          type="primary" 
          plain 
          :loading="loading"
          @click="handleLoadMore"
        >
          加载更多
        </el-button>
      </div>

      <!-- 暂无数据 -->
      <div v-if="!hasResults" class="no-data">
        <i class="el-icon-search"></i>
        <p>未找到相关结果</p>
        <el-button type="text" @click="handleReset">重新搜索</el-button>
      </div>
    </div>

    <!-- 右侧边栏 -->
    <aside class="search-sidebar">
      <!-- 搜索历史 -->
      <div class="widget search-history">
        <h3 class="widget-title">搜索历史</h3>
        <ul class="history-list">
          <li 
            v-for="(item, index) in searchHistory" 
            :key="index"
            @click="handleHistoryClick(item)"
          >
            <span class="history-keyword">{{ item }}</span>
            <i class="el-icon-close" @click.stop="handleRemoveHistory(index)"></i>
          </li>
        </ul>
        <div v-if="searchHistory.length" class="clear-history">
          <el-button type="text" @click="handleClearHistory">
            清空历史记录
          </el-button>
        </div>
      </div>

      <!-- 热门搜索 -->
      <div class="widget hot-searches">
        <h3 class="widget-title">热门搜索</h3>
        <ul class="hot-list">
          <li 
            v-for="(item, index) in hotSearches" 
            :key="index"
            @click="handleHotSearch(item)"
          >
            <span class="hot-rank" :class="{ 'top-rank': index < 3 }">{{ index + 1 }}</span>
            <span class="hot-keyword">{{ item }}</span>
            <span class="hot-count">{{ hotCounts[index] }}</span>
          </li>
        </ul>
      </div>

      <!-- 相关标签 -->
      <div class="widget related-tags">
        <h3 class="widget-title">相关标签</h3>
        <div class="tag-cloud">
          <el-tag
            v-for="tag in relatedTags"
            :key="tag.id"
            :type="tag.type"
            effect="plain"
            class="tag-item"
            @click="handleTagClick(tag)"
          >
            {{ tag.name }}
          </el-tag>
        </div>
      </div>
    </aside>
  </div>
</template>

<script>
export default {
  name: 'PortalSearchPage',
  props: {
    pageData: {
      type: Object,
      default: () => ({})
    },
    isMobile: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      keyword: this.pageData.keyword || '',
      total: this.pageData.total || 0,
      articles: this.pageData.articles || [],
      categories: this.pageData.categories || [],
      tags: this.pageData.tags || [],
      relatedTags: this.pageData.relatedTags || [],
      hotSearches: this.pageData.hotSearches || [],
      hotCounts: this.pageData.hotCounts || [],
      searchHistory: this.getSearchHistory(),
      currentType: 'all',
      sortBy: 'relevance',
      loading: false,
      hasMore: true,
      currentPage: 1,
      pageSize: 10
    }
  },
  computed: {
    hasResults() {
      return this.articles.length > 0 || this.categories.length > 0 || this.tags.length > 0
    }
  },
  methods: {
    handleTypeChange(type) {
      this.currentType = type
      this.currentPage = 1
      this.$emit('filter', { type, sort: this.sortBy })
    },
    handleSortChange(sort) {
      this.sortBy = sort
      this.currentPage = 1
      this.$emit('filter', { type: this.currentType, sort })
    },
    handleArticleClick(article) {
      this.$emit('navigate', '/article', { id: article.id })
    },
    handleCategoryClick(category) {
      this.$emit('navigate', '/category', { id: category.id })
    },
    handleTagClick(tag) {
      this.$emit('navigate', '/tag', { id: tag.id })
    },
    handleLoadMore() {
      if (!this.loading && this.hasMore) {
        this.loading = true
        this.currentPage += 1
        this.$emit('load-more', {
          keyword: this.keyword,
          type: this.currentType,
          sort: this.sortBy,
          page: this.currentPage,
          pageSize: this.pageSize
        })
      }
    },
    handleReset() {
      this.$emit('reset')
    },
    handleHistoryClick(keyword) {
      this.$emit('search', keyword)
    },
    handleHotSearch(keyword) {
      this.$emit('search', keyword)
    },
    handleRemoveHistory(index) {
      this.searchHistory.splice(index, 1)
      this.saveSearchHistory()
    },
    handleClearHistory() {
      this.searchHistory = []
      this.saveSearchHistory()
    },
    highlightKeyword(text) {
      if (!text || !this.keyword) return text
      const reg = new RegExp(this.keyword, 'gi')
      return text.replace(reg, match => `<span class="highlight">${match}</span>`)
    },
    getSearchHistory() {
      try {
        return JSON.parse(localStorage.getItem('searchHistory')) || []
      } catch (e) {
        return []
      }
    },
    saveSearchHistory() {
      localStorage.setItem('searchHistory', JSON.stringify(this.searchHistory))
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-search {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 30px;
  
  // 搜索头部
  .search-header {
    grid-column: 1 / -1;
    background: #fff;
    border-radius: 8px;
    padding: 30px;
    margin-bottom: 30px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);
    
    .search-title {
      font-size: 28px;
      margin: 0 0 15px;
      color: var(--heading-color);
    }
    
    .search-info {
      color: #666;
      margin-bottom: 20px;
    }
    
    .search-filters {
      display: flex;
      gap: 20px;
      align-items: center;
    }
  }
  
  // 搜索结果
  .search-results {
    .result-item {
      background: #fff;
      border-radius: 8px;
      margin-bottom: 20px;
      box-shadow: 0 2px 12px rgba(0,0,0,0.1);
      cursor: pointer;
      transition: transform 0.3s;
      
      &:hover {
        transform: translateY(-5px);
      }
      
      // 文章结果样式
      &.article-item {
        display: grid;
        grid-template-columns: 200px 1fr;
        gap: 20px;
        overflow: hidden;
        
        .item-image {
          height: 150px;
          background-size: cover;
          background-position: center;
          position: relative;
          
          .item-category {
            position: absolute;
            top: 15px;
            left: 15px;
            background: var(--primary-color);
            color: #fff;
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 14px;
          }
        }
      }
      
      // 分类和标签结果样式
      &.category-item,
      &.tag-item {
        display: grid;
        grid-template-columns: 60px 1fr;
        gap: 20px;
        padding: 20px;
        
        .item-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          background: var(--primary-color);
          color: #fff;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 24px;
        }
      }
      
      .item-info {
        padding: 20px;
        
        .item-title {
          font-size: 20px;
          margin: 0 0 10px;
          color: var(--heading-color);
          line-height: 1.4;
        }
        
        .item-excerpt,
        .item-description {
          font-size: 14px;
          color: #666;
          margin: 0 0 15px;
          line-height: 1.6;
        }
        
        .item-meta {
          color: #999;
          font-size: 14px;
          
          .meta-item {
            margin-right: 15px;
            
            i {
              margin-right: 5px;
            }
          }
        }
      }
    }
    
    .load-more {
      text-align: center;
      margin-top: 30px;
    }
    
    .no-data {
      text-align: center;
      padding: 50px 0;
      color: #999;
      
      i {
        font-size: 48px;
        margin-bottom: 10px;
      }
      
      p {
        margin: 0 0 15px;
        font-size: 16px;
      }
    }
  }
  
  // 侧边栏
  .search-sidebar {
    .widget {
      background: #fff;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 30px;
      box-shadow: 0 2px 12px rgba(0,0,0,0.1);
      
      .widget-title {
        font-size: 18px;
        margin: 0 0 20px;
        padding-bottom: 10px;
        border-bottom: 2px solid var(--primary-color);
      }
      
      &.search-history {
        .history-list {
          list-style: none;
          padding: 0;
          margin: 0;
          
          li {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
            cursor: pointer;
            
            &:last-child {
              border-bottom: none;
            }
            
            &:hover {
              color: var(--primary-color);
              
              .el-icon-close {
                opacity: 1;
              }
            }
            
            .el-icon-close {
              opacity: 0;
              transition: opacity 0.3s;
              
              &:hover {
                color: var(--danger-color);
              }
            }
          }
        }
        
        .clear-history {
          margin-top: 15px;
          text-align: right;
        }
      }
      
      &.hot-searches {
        .hot-list {
          list-style: none;
          padding: 0;
          margin: 0;
          
          li {
            display: flex;
            align-items: center;
            padding: 10px 0;
            cursor: pointer;
            
            &:hover {
              color: var(--primary-color);
            }
            
            .hot-rank {
              width: 24px;
              height: 24px;
              border-radius: 4px;
              background: #f5f5f5;
              color: #999;
              display: flex;
              align-items: center;
              justify-content: center;
              margin-right: 10px;
              font-size: 14px;
              
              &.top-rank {
                background: var(--primary-color);
                color: #fff;
              }
            }
            
            .hot-keyword {
              flex: 1;
            }
            
            .hot-count {
              color: #999;
              font-size: 12px;
            }
          }
        }
      }
      
      &.related-tags {
        .tag-cloud {
          display: flex;
          flex-wrap: wrap;
          gap: 10px;
          
          .tag-item {
            cursor: pointer;
          }
        }
      }
    }
  }
}

// 高亮样式
:deep(.highlight) {
  color: var(--primary-color);
  font-weight: bold;
}

// 响应式样式
@media screen and (max-width: 768px) {
  .portal-search {
    grid-template-columns: 1fr;
    
    .search-header {
      padding: 20px;
      
      .search-filters {
        flex-direction: column;
        align-items: stretch;
      }
    }
    
    .search-results {
      .result-item {
        &.article-item {
          grid-template-columns: 1fr;
          
          .item-image {
            height: 200px;
          }
        }
      }
    }
  }
}
</style> 