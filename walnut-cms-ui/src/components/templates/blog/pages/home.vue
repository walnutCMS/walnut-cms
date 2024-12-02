<template>
  <div class="blog-home">
    <!-- 横幅区域 -->
    <section v-if="banner" class="banner-section">
      <el-carousel :height="isMobile ? '200px' : '400px'">
        <el-carousel-item v-for="item in banner.items" :key="item.id">
          <img :src="item.image" :alt="item.title">
          <div class="banner-content">
            <h2>{{ item.title }}</h2>
            <p>{{ item.description }}</p>
          </div>
        </el-carousel-item>
      </el-carousel>
    </section>

    <!-- 特色文章 -->
    <section v-if="featured" class="featured-section">
      <h2 class="section-title">{{ featured.title }}</h2>
      <div class="featured-grid">
        <div
          v-for="item in featured.items"
          :key="item.id"
          class="featured-item"
          @click="handleArticleClick(item)"
        >
          <div class="featured-image">
            <img :src="item.image" :alt="item.title">
          </div>
          <div class="featured-content">
            <h3>{{ item.title }}</h3>
            <p>{{ item.summary }}</p>
            <div class="featured-meta">
              <span class="author">{{ item.author }}</span>
              <span class="date">{{ item.date }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 最新文章 -->
    <section v-if="latest" class="latest-section">
      <h2 class="section-title">{{ latest.title }}</h2>
      <div class="latest-list">
        <div
          v-for="item in latest.items"
          :key="item.id"
          class="latest-item"
          @click="handleArticleClick(item)"
        >
          <div class="latest-image">
            <img :src="item.image" :alt="item.title">
          </div>
          <div class="latest-content">
            <h3>{{ item.title }}</h3>
            <p>{{ item.summary }}</p>
            <div class="latest-meta">
              <span class="category">{{ item.category }}</span>
              <span class="author">{{ item.author }}</span>
              <span class="date">{{ item.date }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="load-more">
        <el-button type="primary" @click="handleLoadMore">加载更多</el-button>
      </div>
    </section>

    <!-- 分类导航 -->
    <section v-if="categories" class="categories-section">
      <h2 class="section-title">{{ categories.title }}</h2>
      <div class="categories-grid">
        <div
          v-for="item in categories.items"
          :key="item.id"
          class="category-item"
          @click="handleCategoryClick(item)"
        >
          <div class="category-icon">
            <i :class="item.icon"></i>
          </div>
          <h3>{{ item.name }}</h3>
          <p>{{ item.description }}</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'BlogHome',
  props: {
    banner: {
      type: Object,
      default: null
    },
    featured: {
      type: Object,
      default: null
    },
    latest: {
      type: Object,
      default: null
    },
    categories: {
      type: Object,
      default: null
    },
    isMobile: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    handleArticleClick(article) {
      this.$emit('navigate', `/article/${article.id}`)
    },
    handleCategoryClick(category) {
      this.$emit('navigate', `/category/${category.id}`)
    },
    handleLoadMore() {
      this.$emit('load-more')
    }
  }
}
</script>

<style lang="scss" scoped>
.blog-home {
  .section-title {
    margin: 0 0 20px;
    font-size: 24px;
    font-weight: bold;
  }

  section {
    margin-bottom: 40px;
  }

  .banner-section {
    margin: -20px -20px 20px;

    .banner-content {
      position: absolute;
      left: 0;
      right: 0;
      bottom: 0;
      padding: 40px;
      background: linear-gradient(to top, rgba(0,0,0,.7), transparent);
      color: #fff;

      h2 {
        margin: 0 0 10px;
        font-size: 32px;
      }

      p {
        margin: 0;
        font-size: 16px;
        opacity: 0.8;
      }
    }
  }

  .featured-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;

    .featured-item {
      background: #fff;
      border-radius: 4px;
      overflow: hidden;
      box-shadow: 0 2px 8px rgba(0,0,0,.08);
      walnut: pointer;
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .featured-image {
        height: 200px;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }

      .featured-content {
        padding: 20px;

        h3 {
          margin: 0 0 10px;
          font-size: 18px;
        }

        p {
          margin: 0 0 10px;
          color: #666;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }
      }

      .featured-meta {
        color: #999;
        font-size: 14px;

        span {
          margin-right: 15px;
        }
      }
    }
  }

  .latest-list {
    .latest-item {
      display: flex;
      margin-bottom: 20px;
      padding-bottom: 20px;
      border-bottom: 1px solid #eee;
      walnut: pointer;

      &:last-child {
        margin-bottom: 0;
        padding-bottom: 0;
        border-bottom: none;
      }

      .latest-image {
        width: 200px;
        margin-right: 20px;

        img {
          width: 100%;
          height: 120px;
          object-fit: cover;
          border-radius: 4px;
        }
      }

      .latest-content {
        flex: 1;
        min-width: 0;

        h3 {
          margin: 0 0 10px;
          font-size: 18px;
        }

        p {
          margin: 0 0 10px;
          color: #666;
        }
      }

      .latest-meta {
        color: #999;
        font-size: 14px;

        span {
          margin-right: 15px;
        }

        .category {
          color: var(--primary-color);
        }
      }
    }
  }

  .load-more {
    text-align: center;
    margin-top: 20px;
  }

  .categories-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 20px;

    .category-item {
      background: #fff;
      border-radius: 4px;
      padding: 20px;
      text-align: center;
      walnut: pointer;
      transition: all 0.3s;
      box-shadow: 0 2px 8px rgba(0,0,0,.08);

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 4px 12px rgba(0,0,0,.12);

        .category-icon {
          transform: scale(1.1);
        }
      }

      .category-icon {
        font-size: 36px;
        color: var(--primary-color);
        margin-bottom: 15px;
        transition: transform 0.3s;
      }

      h3 {
        margin: 0 0 10px;
        font-size: 18px;
      }

      p {
        margin: 0;
        color: #666;
        font-size: 14px;
      }
    }
  }
}

[data-theme='dark'] {
  .blog-home {
    .featured-item,
    .category-item {
      background-color: var(--background-color-base);
      box-shadow: 0 2px 8px rgba(0,0,0,.2);
    }

    .featured-content,
    .latest-content {
      p {
        color: var(--text-color-secondary);
      }
    }

    .latest-item {
      border-bottom-color: var(--border-color-base);
    }

    .category-item {
      p {
        color: var(--text-color-secondary);
      }
    }
  }
}
</style> 