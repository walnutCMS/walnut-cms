<template>
  <div class="template-pc-default" :style="containerStyle">
    <!-- 顶部导航 -->
    <header v-if="navPosition === 'top'" class="header">
      <div class="header-inner">
        <div class="logo">
          <img :src="logo" alt="Logo">
          <span class="site-name">{{ siteName }}</span>
        </div>
        <nav class="nav">
          <ul>
            <li v-for="item in navigation" :key="item.id">
              <a :href="item.url">{{ item.name }}</a>
            </li>
          </ul>
        </nav>
      </div>
    </header>

    <div class="main-container">
      <!-- 左侧导航 -->
      <aside v-if="navPosition === 'left'" class="sidebar">
        <div class="logo">
          <img :src="logo" alt="Logo">
          <span class="site-name">{{ siteName }}</span>
        </div>
        <nav class="nav">
          <ul>
            <li v-for="item in navigation" :key="item.id">
              <a :href="item.url">{{ item.name }}</a>
            </li>
          </ul>
        </nav>
      </aside>

      <!-- 主要内容区 -->
      <main class="main-content">
        <div v-for="(section, index) in sections" :key="index" class="section">
          <!-- 横幅区域 -->
          <div v-if="section.type === 'banner'" class="banner">
            <el-carousel height="400px">
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
    </div>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-inner">
        <div class="footer-content">
          <div v-for="(column, index) in footer.columns" :key="index" class="footer-column">
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
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'TemplatePcDefault',
  props: {
    // 布局配置
    layout: {
      type: String,
      default: 'fixed'
    },
    containerWidth: {
      type: Number,
      default: 1200
    },
    navPosition: {
      type: String,
      default: 'top'
    },
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
.template-pc-default {
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
      max-width: v-bind('layout === "fixed" ? containerWidth + "px" : "100%"');
      margin: 0 auto;
      padding: 0 20px;
      height: 64px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
  }

  .main-container {
    flex: 1;
    display: flex;
    max-width: v-bind('layout === "fixed" ? containerWidth + "px" : "100%"');
    margin: 0 auto;
    padding: 20px;
  }

  .sidebar {
    width: 240px;
    margin-right: 20px;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 8px rgba(0,0,0,.08);
    padding: 20px;

    .logo {
      margin-bottom: 20px;
      padding-bottom: 20px;
      border-bottom: 1px solid #eee;
    }
  }

  .main-content {
    flex: 1;
    min-width: 0;
  }

  .logo {
    display: flex;
    align-items: center;

    img {
      height: 32px;
      margin-right: 10px;
    }

    .site-name {
      font-size: 18px;
      font-weight: bold;
    }
  }

  .nav {
    ul {
      display: flex;
      list-style: none;
      margin: 0;
      padding: 0;
    }

    li {
      margin: 0 15px;

      a {
        color: #333;
        text-decoration: none;
        font-size: 16px;
        transition: color 0.3s;

        &:hover {
          color: var(--primary-color);
        }
      }
    }
  }

  .section {
    margin-bottom: 40px;
    background-color: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 8px rgba(0,0,0,.08);
    overflow: hidden;

    .section-title {
      margin: 0;
      padding: 20px;
      font-size: 24px;
      border-bottom: 1px solid #eee;
    }
  }

  .banner {
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

  .articles {
    .article-list {
      padding: 20px;
    }

    .article-item {
      display: flex;
      margin-bottom: 20px;
      padding-bottom: 20px;
      border-bottom: 1px solid #eee;

      &:last-child {
        margin-bottom: 0;
        padding-bottom: 0;
        border-bottom: none;
      }
    }

    .article-image {
      width: 200px;
      margin-right: 20px;

      img {
        width: 100%;
        height: 120px;
        object-fit: cover;
        border-radius: 4px;
      }
    }

    .article-content {
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

    .article-meta {
      color: #999;
      font-size: 14px;

      span {
        margin-right: 15px;
      }
    }
  }

  .grid {
    .grid-list {
      padding: 20px;
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 20px;
    }

    .grid-item {
      text-align: center;

      img {
        width: 100%;
        height: 160px;
        object-fit: cover;
        border-radius: 4px;
        margin-bottom: 10px;
      }

      h3 {
        margin: 0 0 10px;
        font-size: 18px;
      }

      p {
        margin: 0;
        color: #666;
      }
    }
  }

  .footer {
    background-color: #f5f5f5;
    padding: 40px 0;
    margin-top: 40px;

    .footer-inner {
      max-width: v-bind('layout === "fixed" ? containerWidth + "px" : "100%"');
      margin: 0 auto;
      padding: 0 20px;
    }

    .footer-content {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 40px;
      margin-bottom: 40px;
    }

    .footer-column {
      h3 {
        margin: 0 0 20px;
        font-size: 18px;
      }

      ul {
        list-style: none;
        margin: 0;
        padding: 0;
      }

      li {
        margin-bottom: 10px;

        a {
          color: #666;
          text-decoration: none;
          transition: color 0.3s;

          &:hover {
            color: var(--primary-color);
          }
        }
      }
    }

    .footer-bottom {
      text-align: center;
      color: #999;
      border-top: 1px solid #eee;
      padding-top: 20px;
    }
  }
}

[data-theme='dark'] {
  .template-pc-default {
    background-color: var(--background-color-base);

    .header {
      background-color: var(--background-color-base);
      box-shadow: 0 2px 8px rgba(0,0,0,.2);
    }

    .sidebar,
    .section {
      background-color: var(--background-color-base);
      box-shadow: 0 2px 8px rgba(0,0,0,.2);
    }

    .nav li a {
      color: var(--text-color);
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

      .footer-column li a {
        color: var(--text-color-secondary);
      }
    }
  }
}
</style> 