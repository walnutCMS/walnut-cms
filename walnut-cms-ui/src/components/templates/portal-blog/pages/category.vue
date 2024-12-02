<template>
  <div class="portal-category">
    <!-- 分类头部 -->
    <header class="category-header">
      <h1 class="category-title">{{ category.name }}</h1>
      <p class="category-description">{{ category.description }}</p>
      <div class="category-meta">
        <span class="meta-item">
          <i class="el-icon-document"></i>
          {{ category.articleCount }} 篇文章
        </span>
        <span class="meta-item">
          <i class="el-icon-view"></i>
          {{ category.viewCount }} 次浏览
        </span>
      </div>
    </header>

    <!-- 分类内容 -->
    <div class="category-content">
      <!-- 文章列表 -->
      <div class="article-list">
        <div 
          v-for="article in articles" 
          :key="article.id"
          class="article-item"
          @click="handleArticleClick(article)"
        >
          <div class="article-image" :style="{ backgroundImage: `url(${article.cover})` }">
            <div class="article-category">{{ article.category }}</div>
          </div>
          <div class="article-info">
            <h2 class="article-title">{{ article.title }}</h2>
            <p class="article-excerpt">{{ article.excerpt }}</p>
            <div class="article-meta">
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
      </div>

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
      <div v-if="!articles.length" class="no-data">
        <i class="el-icon-document"></i>
        <p>该分类下暂无文章</p>
      </div>
    </div>

    <!-- 右侧边栏 -->
    <aside class="category-sidebar">
      <!-- 子分类 -->
      <div v-if="category.children && category.children.length" class="widget sub-categories">
        <h3 class="widget-title">子分类</h3>
        <ul class="category-list">
          <li 
            v-for="subCategory in category.children" 
            :key="subCategory.id"
            @click="handleCategoryClick(subCategory)"
          >
            <span class="category-name">{{ subCategory.name }}</span>
            <span class="category-count">{{ subCategory.articleCount }}</span>
          </li>
        </ul>
      </div>

      <!-- 热门文章 -->
      <div class="widget hot-articles">
        <h3 class="widget-title">热门文章</h3>
        <ul class="article-list">
          <li 
            v-for="article in hotArticles" 
            :key="article.id"
            @click="handleArticleClick(article)"
          >
            <div class="article-image" :style="{ backgroundImage: `url(${article.cover})` }"></div>
            <div class="article-info">
              <h4>{{ article.title }}</h4>
              <div class="article-meta">
                <span><i class="el-icon-view"></i> {{ article.viewCount }}</span>
                <span><i class="el-icon-chat-line-square"></i> {{ article.commentCount }}</span>
              </div>
            </div>
          </li>
        </ul>
      </div>

      <!-- 标签云 -->
      <div class="widget category-tags">
        <h3 class="widget-title">分类标签</h3>
        <div class="tag-cloud">
          <el-tag
            v-for="tag in categoryTags"
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
  name: 'PortalCategoryPage',
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
      category: this.pageData.category || {},
      articles: this.pageData.articles || [],
      hotArticles: this.pageData.hotArticles || [],
      categoryTags: this.pageData.categoryTags || [],
      loading: false,
      hasMore: true,
      currentPage: 1,
      pageSize: 10
    }
  },
  methods: {
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
          categoryId: this.category.id,
          page: this.currentPage,
          pageSize: this.pageSize
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-category {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 30px;
  
  // 分类头部
  .category-header {
    grid-column: 1 / -1;
    background: #fff;
    border-radius: 8px;
    padding: 30px;
    text-align: center;
    margin-bottom: 30px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);
    
    .category-title {
      font-size: 36px;
      margin: 0 0 15px;
      color: var(--heading-color);
    }
    
    .category-description {
      font-size: 16px;
      color: #666;
      margin: 0 0 20px;
    }
    
    .category-meta {
      .meta-item {
        color: #999;
        margin: 0 15px;
        font-size: 14px;
        
        i {
          margin-right: 5px;
        }
      }
    }
  }
  
  // 文章列表
  .category-content {
    .article-list {
      .article-item {
        display: grid;
        grid-template-columns: 280px 1fr;
        gap: 20px;
        background: #fff;
        border-radius: 8px;
        overflow: hidden;
        margin-bottom: 20px;
        box-shadow: 0 2px 12px rgba(0,0,0,0.1);
        walnut: pointer;
        transition: transform 0.3s;
        
        &:hover {
          transform: translateY(-5px);
        }
        
        .article-image {
          height: 200px;
          background-size: cover;
          background-position: center;
          position: relative;
          
          .article-category {
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
        
        .article-info {
          padding: 20px;
          
          .article-title {
            font-size: 24px;
            margin: 0 0 15px;
            color: var(--heading-color);
            line-height: 1.4;
          }
          
          .article-excerpt {
            font-size: 14px;
            color: #666;
            margin: 0 0 20px;
            line-height: 1.6;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }
          
          .article-meta {
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
        margin: 0;
        font-size: 16px;
      }
    }
  }
  
  // 侧边栏
  .category-sidebar {
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
      
      &.sub-categories {
        .category-list {
          list-style: none;
          padding: 0;
          margin: 0;
          
          li {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
            walnut: pointer;
            
            &:last-child {
              border-bottom: none;
            }
            
            &:hover {
              color: var(--primary-color);
            }
            
            .category-count {
              background: #f5f5f5;
              padding: 2px 8px;
              border-radius: 10px;
              font-size: 12px;
            }
          }
        }
      }
      
      &.hot-articles {
        .article-list {
          list-style: none;
          padding: 0;
          margin: 0;
          
          li {
            display: grid;
            grid-template-columns: 80px 1fr;
            gap: 15px;
            padding: 15px 0;
            border-bottom: 1px solid #eee;
            walnut: pointer;
            
            &:last-child {
              border-bottom: none;
              padding-bottom: 0;
            }
            
            &:first-child {
              padding-top: 0;
            }
            
            .article-image {
              height: 60px;
              background-size: cover;
              background-position: center;
              border-radius: 4px;
            }
            
            .article-info {
              h4 {
                margin: 0 0 5px;
                font-size: 14px;
                line-height: 1.4;
              }
              
              .article-meta {
                color: #999;
                font-size: 12px;
                
                span {
                  margin-right: 10px;
                  
                  i {
                    margin-right: 3px;
                  }
                }
              }
            }
          }
        }
      }
      
      &.category-tags {
        .tag-cloud {
          display: flex;
          flex-wrap: wrap;
          gap: 10px;
          
          .tag-item {
            walnut: pointer;
          }
        }
      }
    }
  }
}

// 响应式样式
@media screen and (max-width: 768px) {
  .portal-category {
    grid-template-columns: 1fr;
    
    .category-header {
      padding: 20px;
      
      .category-title {
        font-size: 28px;
      }
    }
    
    .category-content {
      .article-list {
        .article-item {
          grid-template-columns: 1fr;
          
          .article-image {
            height: 200px;
          }
          
          .article-info {
            .article-title {
              font-size: 20px;
            }
          }
        }
      }
    }
  }
}
</style> 