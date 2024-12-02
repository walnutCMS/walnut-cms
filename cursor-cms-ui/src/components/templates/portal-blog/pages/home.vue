<template>
  <div class="portal-home">
    <!-- 顶部轮播图 -->
    <div class="banner-section">
      <el-carousel height="400px">
        <el-carousel-item v-for="banner in banners" :key="banner.id">
          <div class="banner-item" :style="{ backgroundImage: `url(${banner.image})` }">
            <div class="banner-content">
              <h2>{{ banner.title }}</h2>
              <p>{{ banner.description }}</p>
              <el-button type="primary" @click="handleBannerClick(banner)">
                阅读更多
              </el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 主要内容区 -->
    <div class="content-section">
      <!-- 左侧文章列表 -->
      <div class="main-content">
        <!-- 热门文章 -->
        <section class="featured-posts">
          <h2 class="section-title">热门文章</h2>
          <div class="featured-grid">
            <div 
              v-for="post in featuredPosts" 
              :key="post.id"
              class="featured-item"
              @click="handlePostClick(post)"
            >
              <div class="featured-image" :style="{ backgroundImage: `url(${post.cover})` }">
                <div class="featured-category">{{ post.category }}</div>
              </div>
              <div class="featured-info">
                <h3>{{ post.title }}</h3>
                <p>{{ post.excerpt }}</p>
                <div class="post-meta">
                  <span><i class="el-icon-time"></i> {{ post.date }}</span>
                  <span><i class="el-icon-view"></i> {{ post.views }}</span>
                  <span><i class="el-icon-chat-line-square"></i> {{ post.comments }}</span>
                </div>
              </div>
            </div>
          </div>
        </section>

        <!-- 最新文章列表 -->
        <section class="latest-posts">
          <h2 class="section-title">最新文章</h2>
          <div class="post-list">
            <div 
              v-for="post in latestPosts" 
              :key="post.id"
              class="post-item"
              @click="handlePostClick(post)"
            >
              <div class="post-image" :style="{ backgroundImage: `url(${post.cover})` }"></div>
              <div class="post-info">
                <div class="post-category">{{ post.category }}</div>
                <h3>{{ post.title }}</h3>
                <p>{{ post.excerpt }}</p>
                <div class="post-meta">
                  <span><i class="el-icon-time"></i> {{ post.date }}</span>
                  <span><i class="el-icon-view"></i> {{ post.views }}</span>
                  <span><i class="el-icon-chat-line-square"></i> {{ post.comments }}</span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 加载更多 -->
          <div class="load-more">
            <el-button 
              type="primary" 
              plain 
              :loading="loading"
              @click="handleLoadMore"
            >
              加载更多
            </el-button>
          </div>
        </section>
      </div>

      <!-- 右侧边栏 -->
      <aside class="sidebar">
        <!-- 分类列表 -->
        <div class="widget categories">
          <h3 class="widget-title">分类</h3>
          <ul class="category-list">
            <li 
              v-for="category in categories" 
              :key="category.id"
              @click="handleCategoryClick(category)"
            >
              <span class="category-name">{{ category.name }}</span>
              <span class="category-count">{{ category.count }}</span>
            </li>
          </ul>
        </div>

        <!-- 热门标签 -->
        <div class="widget tags">
          <h3 class="widget-title">热门标签</h3>
          <div class="tag-cloud">
            <el-tag
              v-for="tag in tags"
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

        <!-- 推荐阅读 -->
        <div class="widget recommended">
          <h3 class="widget-title">推荐阅读</h3>
          <ul class="recommended-list">
            <li 
              v-for="post in recommendedPosts" 
              :key="post.id"
              @click="handlePostClick(post)"
            >
              <div class="recommended-image" :style="{ backgroundImage: `url(${post.cover})` }"></div>
              <div class="recommended-info">
                <h4>{{ post.title }}</h4>
                <div class="post-meta">
                  <span><i class="el-icon-time"></i> {{ post.date }}</span>
                  <span><i class="el-icon-view"></i> {{ post.views }}</span>
                </div>
              </div>
            </li>
          </ul>
        </div>
      </aside>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PortalHomePage',
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
      loading: false,
      banners: this.pageData.banners || [],
      featuredPosts: this.pageData.featuredPosts || [],
      latestPosts: this.pageData.latestPosts || [],
      categories: this.pageData.categories || [],
      tags: this.pageData.tags || [],
      recommendedPosts: this.pageData.recommendedPosts || []
    }
  },
  methods: {
    handleBannerClick(banner) {
      this.$emit('navigate', '/article', { id: banner.id })
    },
    handlePostClick(post) {
      this.$emit('navigate', '/article', { id: post.id })
    },
    handleCategoryClick(category) {
      this.$emit('navigate', '/category', { id: category.id })
    },
    handleTagClick(tag) {
      this.$emit('navigate', '/tag', { id: tag.id })
    },
    handleLoadMore() {
      this.loading = true
      this.$emit('load-more')
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-home {
  // 轮播图样式
  .banner-section {
    margin-bottom: 30px;
    
    .banner-item {
      height: 100%;
      background-size: cover;
      background-position: center;
      position: relative;
      
      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0,0,0,0.3);
      }
      
      .banner-content {
        position: absolute;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        text-align: center;
        color: #fff;
        width: 80%;
        
        h2 {
          font-size: 36px;
          margin: 0 0 20px;
        }
        
        p {
          font-size: 18px;
          margin: 0 0 30px;
        }
      }
    }
  }
  
  // 内容区样式
  .content-section {
    display: grid;
    grid-template-columns: 1fr 300px;
    gap: 30px;
    
    // 主要内容
    .main-content {
      .section-title {
        font-size: 24px;
        margin: 0 0 20px;
        padding-bottom: 10px;
        border-bottom: 2px solid var(--primary-color);
      }
      
      // 热门文章
      .featured-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20px;
        margin-bottom: 40px;
        
        .featured-item {
          background: #fff;
          border-radius: 8px;
          overflow: hidden;
          box-shadow: 0 2px 12px rgba(0,0,0,0.1);
          cursor: pointer;
          transition: transform 0.3s;
          
          &:hover {
            transform: translateY(-5px);
          }
          
          .featured-image {
            height: 200px;
            background-size: cover;
            background-position: center;
            position: relative;
            
            .featured-category {
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
          
          .featured-info {
            padding: 20px;
            
            h3 {
              margin: 0 0 10px;
              font-size: 18px;
              line-height: 1.4;
            }
            
            p {
              margin: 0 0 15px;
              color: #666;
              font-size: 14px;
              line-height: 1.6;
            }
          }
        }
      }
      
      // 最新文章列表
      .post-list {
        .post-item {
          display: grid;
          grid-template-columns: 200px 1fr;
          gap: 20px;
          background: #fff;
          padding: 20px;
          border-radius: 8px;
          margin-bottom: 20px;
          box-shadow: 0 2px 12px rgba(0,0,0,0.1);
          cursor: pointer;
          transition: transform 0.3s;
          
          &:hover {
            transform: translateX(5px);
          }
          
          .post-image {
            height: 150px;
            background-size: cover;
            background-position: center;
            border-radius: 4px;
          }
          
          .post-info {
            .post-category {
              display: inline-block;
              background: var(--primary-color);
              color: #fff;
              padding: 5px 10px;
              border-radius: 4px;
              font-size: 14px;
              margin-bottom: 10px;
            }
            
            h3 {
              margin: 0 0 10px;
              font-size: 20px;
              line-height: 1.4;
            }
            
            p {
              margin: 0 0 15px;
              color: #666;
              font-size: 14px;
              line-height: 1.6;
            }
          }
        }
      }
      
      .post-meta {
        color: #999;
        font-size: 14px;
        
        span {
          margin-right: 15px;
          
          i {
            margin-right: 5px;
          }
        }
      }
      
      .load-more {
        text-align: center;
        margin-top: 30px;
      }
    }
    
    // 侧边栏
    .sidebar {
      .widget {
        background: #fff;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 30px;
        box-shadow: 0 2px 12px rgba(0,0,0,0.1);
        
        .widget-title {
          font-size: 18px;
          margin: 0 0 20px;
          padding-bottom: 10px;
          border-bottom: 2px solid var(--primary-color);
        }
      }
      
      // 分类列表
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
          cursor: pointer;
          
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
      
      // 标签云
      .tag-cloud {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
        
        .tag-item {
          cursor: pointer;
        }
      }
      
      // 推荐阅读
      .recommended-list {
        list-style: none;
        padding: 0;
        margin: 0;
        
        li {
          display: grid;
          grid-template-columns: 80px 1fr;
          gap: 15px;
          padding: 15px 0;
          border-bottom: 1px solid #eee;
          cursor: pointer;
          
          &:last-child {
            border-bottom: none;
            padding-bottom: 0;
          }
          
          &:first-child {
            padding-top: 0;
          }
          
          .recommended-image {
            height: 60px;
            background-size: cover;
            background-position: center;
            border-radius: 4px;
          }
          
          .recommended-info {
            h4 {
              margin: 0 0 5px;
              font-size: 14px;
              line-height: 1.4;
            }
            
            .post-meta {
              font-size: 12px;
            }
          }
        }
      }
    }
  }
}

// 响应式样式
@media screen and (max-width: 768px) {
  .portal-home {
    .content-section {
      grid-template-columns: 1fr;
      
      .main-content {
        .featured-grid {
          grid-template-columns: 1fr;
        }
        
        .post-list {
          .post-item {
            grid-template-columns: 1fr;
            
            .post-image {
              height: 200px;
            }
          }
        }
      }
    }
  }
}
</style> 