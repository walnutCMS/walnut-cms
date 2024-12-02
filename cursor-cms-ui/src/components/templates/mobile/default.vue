<template>
  <div class="template-mobile-default" :style="containerStyle">
    <!-- 顶部导航 -->
    <header class="header">
      <div class="header-inner">
        <div class="logo">
          <img :src="logo" alt="Logo">
          <span class="site-name">{{ siteName }}</span>
        </div>
        <div class="menu-toggle" @click="showMenu = !showMenu">
          <i :class="showMenu ? 'el-icon-close' : 'el-icon-menu'"></i>
        </div>
      </div>
      <!-- 移动端导航菜单 -->
      <transition name="slide-fade">
        <nav v-show="showMenu" class="nav">
          <ul>
            <li v-for="item in navigation" :key="item.id">
              <a :href="item.url" @click="showMenu = false">{{ item.name }}</a>
            </li>
          </ul>
        </nav>
      </transition>
    </header>

    <!-- 主要内容区 -->
    <main class="main-content">
      <div v-for="(section, index) in sections" :key="index" class="section">
        <!-- 横幅区域 -->
        <div v-if="section.type === 'banner'" class="banner">
          <el-carousel height="200px" indicator-position="none">
            <el-carousel-item v-for="item in section.items" :key="item.id">
              <img :src="item.image" :alt="item.title">
              <div class="banner-content">
                <h2>{{ item.title }}</h2>
                <p>{{ item.description }}</p>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>

        <!-- 文章列表 -->
        <div v-else-if="section.type === 'articles'" class="articles">
          <h2 class="section-title">{{ section.title }}</h2>
          <div class="article-list">
            <div v-for="item in section.items" :key="item.id" class="article-item">
              <div class="article-image">
                <img :src="item.image" :alt="item.title">
              </div>
              <div class="article-content">
                <h3>{{ item.title }}</h3>
                <p>{{ item.summary }}</p>
                <div class="article-meta">
                  <span>{{ item.author }}</span>
                  <span>{{ item.date }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 图片网格 -->
        <div v-else-if="section.type === 'grid'" class="grid">
          <h2 class="section-title">{{ section.title }}</h2>
          <div class="grid-list">
            <div v-for="item in section.items" :key="item.id" class="grid-item">
              <img :src="item.image" :alt="item.title">
              <h3>{{ item.title }}</h3>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <div v-for="(column, index) in footer.columns" :key="index" class="footer-section">
          <h3>{{ column.title }}</h3>
          <ul>
            <li v-for="item in column.items" :key="item.id">
              <a :href="item.url">{{ item.name }}</a>
            </li>
          </ul>
        </div>
      </div>
      <div class="footer-bottom">
        <p>{{ footer.copyright }}</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'TemplateMobileDefault',
  props: {
    // 主题配置
    primaryColor: {
      type: String,
      default: '#409EFF'
    },
    fontFamily: {
      type: String,
      default: 'default'
    },
    // 内容配置
    logo: {
      type: String,
      default: ''
    },
    siteName: {
      type: String,
      default: '网站名称'
    },
    navigation: {
      type: Array,
      default: () => []
    },
    sections: {
      type: Array,
      default: () => []
    },
    footer: {
      type: Object,
      default: () => ({
        columns: [],
        copyright: ''
      })
    }
  },
  data() {
    return {
      showMenu: false
    }
  },
  computed: {
    containerStyle() {
      const style = {
        '--primary-color': this.primaryColor
      }
      
      if (this.fontFamily !== 'default') {
        style['--font-family'] = this.fontFamily
      }

      return style
    }
  }
}
</script>

<style lang="scss" scoped>
.template-mobile-default {
  --font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial,
    'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol',
    'Noto Color Emoji';

  font-family: var(--font-family);
  min-height: 100vh;
  display: flex;
  flex-direction: column;

  .header {
    background-color: #fff;
    box-shadow: 0 2px 8px rgba(0,0,0,.08);
    position: sticky;
    top: 0;
    z-index: 100;

    .header-inner {
      padding: 0 15px;
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .logo {
      display: flex;
      align-items: center;

      img {
        height: 24px;
        margin-right: 8px;
      }

      .site-name {
        font-size: 16px;
        font-weight: bold;
      }
    }

    .menu-toggle {
      font-size: 24px;
      cursor: pointer;
      padding: 5px;
    }
  }

  .nav {
    background-color: #fff;
    border-top: 1px solid #eee;

    ul {
      list-style: none;
      margin: 0;
      padding: 0;
    }

    li {
      border-bottom: 1px solid #eee;

      a {
        display: block;
        padding: 12px 15px;
        color: #333;
        text-decoration: none;
        font-size: 16px;

        &:active {
          background-color: #f5f5f5;
        }
      }
    }
  }

  .main-content {
    flex: 1;
    padding: 15px;
  }

  .section {
    margin-bottom: 20px;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 8px rgba(0,0,0,.08);
    overflow: hidden;

    .section-title {
      margin: 0;
      padding: 15px;
      font-size: 18px;
      border-bottom: 1px solid #eee;
    }
  }

  .banner {
    .banner-content {
      position: absolute;
      left: 0;
      right: 0;
      bottom: 0;
      padding: 15px;
      background: linear-gradient(to top, rgba(0,0,0,.7), transparent);
      color: #fff;

      h2 {
        margin: 0 0 5px;
        font-size: 18px;
      }

      p {
        margin: 0;
        font-size: 14px;
        opacity: 0.8;
      }
    }
  }

  .articles {
    .article-list {
      padding: 15px;
    }

    .article-item {
      margin-bottom: 15px;
      padding-bottom: 15px;
      border-bottom: 1px solid #eee;

      &:last-child {
        margin-bottom: 0;
        padding-bottom: 0;
        border-bottom: none;
      }
    }

    .article-image {
      margin-bottom: 10px;

      img {
        width: 100%;
        height: 180px;
        object-fit: cover;
        border-radius: 4px;
      }
    }

    .article-content {
      h3 {
        margin: 0 0 8px;
        font-size: 16px;
      }

      p {
        margin: 0 0 8px;
        color: #666;
        font-size: 14px;
      }
    }

    .article-meta {
      color: #999;
      font-size: 12px;

      span {
        margin-right: 10px;
      }
    }
  }

  .grid {
    .grid-list {
      padding: 15px;
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 15px;
    }

    .grid-item {
      text-align: center;

      img {
        width: 100%;
        height: 120px;
        object-fit: cover;
        border-radius: 4px;
        margin-bottom: 8px;
      }

      h3 {
        margin: 0 0 5px;
        font-size: 14px;
      }

      p {
        margin: 0;
        color: #666;
        font-size: 12px;
      }
    }
  }

  .footer {
    background-color: #f5f5f5;
    padding: 20px 15px;
    margin-top: 20px;

    .footer-content {
      margin-bottom: 20px;
    }

    .footer-section {
      margin-bottom: 20px;

      &:last-child {
        margin-bottom: 0;
      }

      h3 {
        margin: 0 0 10px;
        font-size: 16px;
      }

      ul {
        list-style: none;
        margin: 0;
        padding: 0;
      }

      li {
        margin-bottom: 8px;

        a {
          color: #666;
          text-decoration: none;
          font-size: 14px;

          &:active {
            color: var(--primary-color);
          }
        }
      }
    }

    .footer-bottom {
      text-align: center;
      color: #999;
      font-size: 12px;
      border-top: 1px solid #eee;
      padding-top: 15px;
    }
  }
}

.slide-fade-enter-active {
  transition: all 0.3s ease;
}

.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter,
.slide-fade-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}

[data-theme='dark'] {
  .template-mobile-default {
    background-color: var(--background-color-base);

    .header {
      background-color: var(--background-color-base);
      box-shadow: 0 2px 8px rgba(0,0,0,.2);
    }

    .nav {
      background-color: var(--background-color-base);
      border-top-color: var(--border-color-base);

      li {
        border-bottom-color: var(--border-color-base);

        a {
          color: var(--text-color);

          &:active {
            background-color: var(--background-color-light);
          }
        }
      }
    }

    .section {
      background-color: var(--background-color-base);
      box-shadow: 0 2px 8px rgba(0,0,0,.2);
    }

    .article-content {
      p {
        color: var(--text-color-secondary);
      }
    }

    .grid-item p {
      color: var(--text-color-secondary);
    }

    .footer {
      background-color: var(--background-color-light);

      .footer-section li a {
        color: var(--text-color-secondary);
      }
    }
  }
}
</style> 