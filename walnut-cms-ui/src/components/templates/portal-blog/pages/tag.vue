<template>
  <div class="portal-tag">
    <!-- 标签头部 -->
    <header class="tag-header">
      <div class="tag-info">
        <el-tag
          :type="tag.type"
          effect="dark"
          size="large"
          class="tag-name"
        >
          {{ tag.name }}
        </el-tag>
        <p class="tag-description">{{ tag.description }}</p>
      </div>
      <div class="tag-meta">
        <span class="meta-item">
          <i class="el-icon-document"></i>
          {{ tag.articleCount }} 篇文章
        </span>
        <span class="meta-item">
          <i class="el-icon-view"></i>
          {{ tag.viewCount }} 次浏览
        </span>
        <span class="meta-item">
          <i class="el-icon-user"></i>
          {{ tag.followCount }} 人关注
        </span>
      </div>
      <div class="tag-actions">
        <el-button 
          type="primary" 
          :icon="hasFollowed ? 'el-icon-check' : 'el-icon-plus'"
          @click="handleFollow"
          :disabled="hasFollowed"
        >
          {{ hasFollowed ? '已关注' : '关注标签' }}
        </el-button>
      </div>
    </header>

    <!-- 标签内容 -->
    <div class="tag-content">
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
            <div class="article-tags">
              <el-tag
                v-for="tag in article.tags"
                :key="tag.id"
                :type="tag.type"
                size="small"
                effect="plain"
                @click.stop="handleTagClick(tag)"
              >
                {{ tag.name }}
              </el-tag>
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
        <p>该标签下暂无文章</p>
      </div>
    </div>

    <!-- 右侧边栏 -->
    <aside class="tag-sidebar">
      <!-- 相关标签 -->
      <div class="widget related-tags">
        <h3 class="widget-title">相关标签</h3>
        <div class="tag-cloud">
          <el-tag
            v-for="relatedTag in relatedTags"
            :key="relatedTag.id"
            :type="relatedTag.type"
            effect="plain"
            class="tag-item"
            @click="handleTagClick(relatedTag)"
          >
            {{ relatedTag.name }}
            <span class="tag-count">({{ relatedTag.articleCount }})</span>
          </el-tag>
        </div>
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

      <!-- 活跃作者 -->
      <div class="widget active-authors">
        <h3 class="widget-title">活跃作者</h3>
        <ul class="author-list">
          <li 
            v-for="author in activeAuthors" 
            :key="author.id"
            @click="handleAuthorClick(author)"
          >
            <img :src="author.avatar" :alt="author.name" class="author-avatar">
            <div class="author-info">
              <h4>{{ author.name }}</h4>
              <p>{{ author.articleCount }} 篇文章</p>
            </div>
          </li>
        </ul>
      </div>
    </aside>
  </div>
</template>

<script>
export default {
  name: 'PortalTagPage',
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
      tag: this.pageData.tag || {},
      articles: this.pageData.articles || [],
      relatedTags: this.pageData.relatedTags || [],
      hotArticles: this.pageData.hotArticles || [],
      activeAuthors: this.pageData.activeAuthors || [],
      hasFollowed: false,
      loading: false,
      hasMore: true,
      currentPage: 1,
      pageSize: 10
    }
  },
  methods: {
    handleFollow() {
      if (!this.hasFollowed) {
        this.hasFollowed = true
        this.$emit('follow', { id: this.tag.id })
      }
    },
    handleArticleClick(article) {
      this.$emit('navigate', '/article', { id: article.id })
    },
    handleTagClick(tag) {
      if (tag.id !== this.tag.id) {
        this.$emit('navigate', '/tag', { id: tag.id })
      }
    },
    handleAuthorClick(author) {
      this.$emit('navigate', '/author', { id: author.id })
    },
    handleLoadMore() {
      if (!this.loading && this.hasMore) {
        this.loading = true
        this.currentPage += 1
        this.$emit('load-more', {
          tagId: this.tag.id,
          page: this.currentPage,
          pageSize: this.pageSize
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-tag {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 30px;
  
  // 标签头部
  .tag-header {
    grid-column: 1 / -1;
    background: #fff;
    border-radius: 8px;
    padding: 30px;
    margin-bottom: 30px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);
    text-align: center;
    
    .tag-info {
      margin-bottom: 20px;
      
      .tag-name {
        font-size: 24px;
        padding: 10px 20px;
        margin-bottom: 15px;
      }
      
      .tag-description {
        color: #666;
        font-size: 16px;
        margin: 0;
      }
    }
    
    .tag-meta {
      margin-bottom: 20px;
      
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
  .tag-content {
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
            margin: 0 0 15px;
            line-height: 1.6;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }
          
          .article-meta {
            color: #999;
            font-size: 14px;
            margin-bottom: 15px;
            
            .meta-item {
              margin-right: 15px;
              
              i {
                margin-right: 5px;
              }
            }
          }
          
          .article-tags {
            .el-tag {
              margin-right: 10px;
              margin-bottom: 5px;
              walnut: pointer;
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
  .tag-sidebar {
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
      
      &.related-tags {
        .tag-cloud {
          display: flex;
          flex-wrap: wrap;
          gap: 10px;
          
          .tag-item {
            walnut: pointer;
            
            .tag-count {
              margin-left: 5px;
              font-size: 12px;
              opacity: 0.8;
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
      
      &.active-authors {
        .author-list {
          list-style: none;
          padding: 0;
          margin: 0;
          
          li {
            display: flex;
            align-items: center;
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
            
            &:hover {
              .author-info {
                h4 {
                  color: var(--primary-color);
                }
              }
            }
            
            .author-avatar {
              width: 50px;
              height: 50px;
              border-radius: 50%;
              margin-right: 15px;
            }
            
            .author-info {
              h4 {
                margin: 0 0 5px;
                font-size: 16px;
                transition: color 0.3s;
              }
              
              p {
                margin: 0;
                color: #999;
                font-size: 14px;
              }
            }
          }
        }
      }
    }
  }
}

// 响应式样式
@media screen and (max-width: 768px) {
  .portal-tag {
    grid-template-columns: 1fr;
    
    .tag-header {
      padding: 20px;
      
      .tag-info {
        .tag-name {
          font-size: 20px;
        }
      }
    }
    
    .tag-content {
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