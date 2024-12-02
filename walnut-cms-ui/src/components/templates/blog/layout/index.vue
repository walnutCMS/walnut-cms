<template>
  <div class="blog-layout" :class="{ 'is-mobile': isMobile }" :style="layoutStyle">
    <!-- 顶部导航 -->
    <header v-if="navPosition === 'top'" class="header">
      <div class="header-inner" :style="containerStyle">
        <div class="logo">
          <img :src="logo" alt="Logo">
          <span class="site-name">{{ siteName }}</span>
        </div>
        <nav class="nav" v-if="!isMobile">
          <ul>
            <li v-for="item in navigation" :key="item.id">
              <a :href="item.url" @click.prevent="handleNavigate(item.path)">{{ item.name }}</a>
            </li>
          </ul>
        </nav>
        <div v-else class="menu-toggle" @click="showMenu = !showMenu">
          <i :class="showMenu ? 'el-icon-close' : 'el-icon-menu'"></i>
        </div>
      </div>
      <!-- 移动端菜单 -->
      <transition name="slide-fade">
        <nav v-if="isMobile && showMenu" class="mobile-nav">
          <ul>
            <li v-for="item in navigation" :key="item.id">
              <a :href="item.url" @click.prevent="handleNavigate(item.path)">{{ item.name }}</a>
            </li>
          </ul>
        </nav>
      </transition>
    </header>

    <div class="main-container" :style="containerStyle">
      <!-- 左侧导航 -->
      <aside v-if="navPosition === 'left' && !isMobile" class="sidebar">
        <div class="logo">
          <img :src="logo" alt="Logo">
          <span class="site-name">{{ siteName }}</span>
        </div>
        <nav class="nav">
          <ul>
            <li v-for="item in navigation" :key="item.id">
              <a :href="item.url" @click.prevent="handleNavigate(item.path)">{{ item.name }}</a>
            </li>
          </ul>
        </nav>
      </aside>

      <!-- 主要内容区 -->
      <main class="main-content">
        <slot></slot>
      </main>
    </div>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-inner" :style="containerStyle">
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
  name: 'BlogLayout',
  props: {
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
    primaryColor: {
      type: String,
      default: '#409EFF'
    },
    fontFamily: {
      type: String,
      default: 'default'
    },
    darkTheme: {
      type: Boolean,
      default: false
    },
    logo: {
      type: String,
      default: ''
    },
    siteName: {
      type: String,
      default: '我的博客'
    },
    navigation: {
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
      showMenu: false,
      isMobile: window.innerWidth < 768
    }
  },
  computed: {
    layoutStyle() {
      return {
        '--primary-color': this.primaryColor,
        '--font-family': this.fontFamily !== 'default' ? this.fontFamily : null
      }
    },
    containerStyle() {
      return {
        maxWidth: this.layout === 'fixed' ? `${this.containerWidth}px` : '100%'
      }
    }
  },
  mounted() {
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    handleNavigate(path) {
      this.showMenu = false
      this.$emit('navigate', path)
    },
    handleResize() {
      this.isMobile = window.innerWidth < 768
      if (!this.isMobile) {
        this.showMenu = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.blog-layout {
  --font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial,
    'Noto Sans', sans-serif;

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
      margin: 0 auto;
      padding: 0 20px;
      height: 64px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .menu-toggle {
      font-size: 24px;
      walnut: pointer;
      padding: 5px;
    }
  }

  .mobile-nav {
    background-color: #fff;
    border-top: 1px solid #eee;
    padding: 10px 0;

    ul {
      margin: 0;
      padding: 0;
      list-style: none;
    }

    li {
      a {
        display: block;
        padding: 12px 20px;
        color: #333;
        text-decoration: none;
        font-size: 16px;

        &:active {
          background-color: #f5f5f5;
        }
      }
    }
  }

  .main-container {
    flex: 1;
    margin: 0 auto;
    padding: 20px;
    display: flex;
    gap: 20px;
  }

  .sidebar {
    width: 240px;
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

  .footer {
    background-color: #f5f5f5;
    padding: 40px 0;
    margin-top: 40px;

    .footer-inner {
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

  &.is-mobile {
    .main-container {
      padding: 15px;
    }

    .footer {
      padding: 20px 0;

      .footer-content {
        grid-template-columns: 1fr;
        gap: 20px;
      }
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
  .blog-layout {
    background-color: var(--background-color-base);

    .header,
    .mobile-nav,
    .sidebar {
      background-color: var(--background-color-base);
      border-color: var(--border-color-base);
    }

    .nav li a {
      color: var(--text-color);
    }

    .mobile-nav li a:active {
      background-color: var(--background-color-light);
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