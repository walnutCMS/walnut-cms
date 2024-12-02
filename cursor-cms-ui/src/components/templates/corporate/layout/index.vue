<template>
  <div class="corporate-layout">
    <header class="header">
      <div class="container">
        <div class="logo">
          <img :src="config.logo" alt="Logo" v-if="config.logo">
          <h1 v-else>{{ config.title || 'Company Name' }}</h1>
        </div>
        <nav class="nav">
          <router-link 
            v-for="item in menuItems" 
            :key="item.path"
            :to="item.path"
            class="nav-item"
          >
            {{ item.name }}
          </router-link>
        </nav>
      </div>
    </header>

    <main class="main">
      <router-view></router-view>
    </main>

    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-info">
            <h3>{{ config.title || 'Company Name' }}</h3>
            <p>{{ config.description || 'Company Description' }}</p>
          </div>
          <div class="footer-contact">
            <h4>联系我们</h4>
            <p>电话：{{ config.phone || 'N/A' }}</p>
            <p>邮箱：{{ config.email || 'N/A' }}</p>
            <p>地址：{{ config.address || 'N/A' }}</p>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; {{ new Date().getFullYear() }} {{ config.title || 'Company Name' }}. All rights reserved.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'CorporateLayout',
  props: {
    config: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      menuItems: [
        { name: '首页', path: '/' },
        { name: '产品', path: '/product' },
        { name: '关于', path: '/about' },
        { name: '联系', path: '/contact' }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.corporate-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;

  .header {
    background: #fff;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    position: fixed;
    width: 100%;
    top: 0;
    z-index: 100;

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 1rem;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .logo {
      img {
        height: 40px;
      }
      h1 {
        margin: 0;
        font-size: 1.5rem;
        color: #333;
      }
    }

    .nav {
      display: flex;
      gap: 2rem;

      .nav-item {
        color: #333;
        text-decoration: none;
        font-weight: 500;
        padding: 0.5rem 0;
        position: relative;

        &::after {
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          width: 0;
          height: 2px;
          background: #1890ff;
          transition: width 0.3s ease;
        }

        &:hover,
        &.router-link-active {
          color: #1890ff;
          &::after {
            width: 100%;
          }
        }
      }
    }
  }

  .main {
    flex: 1;
    margin-top: 73px;
  }

  .footer {
    background: #f5f5f5;
    padding: 3rem 0 1rem;

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
    }

    .footer-content {
      display: grid;
      grid-template-columns: 2fr 1fr;
      gap: 2rem;
      margin-bottom: 2rem;
    }

    .footer-info {
      h3 {
        margin: 0 0 1rem;
        color: #333;
      }
      p {
        color: #666;
        line-height: 1.6;
      }
    }

    .footer-contact {
      h4 {
        margin: 0 0 1rem;
        color: #333;
      }
      p {
        color: #666;
        margin: 0.5rem 0;
      }
    }

    .footer-bottom {
      border-top: 1px solid #ddd;
      padding-top: 1rem;
      text-align: center;
      color: #999;
    }
  }
}

@media (max-width: 768px) {
  .corporate-layout {
    .header {
      .container {
        flex-direction: column;
        gap: 1rem;
      }

      .nav {
        flex-wrap: wrap;
        justify-content: center;
        gap: 1rem;
      }
    }

    .main {
      margin-top: 116px;
    }

    .footer {
      .footer-content {
        grid-template-columns: 1fr;
      }
    }
  }
}
</style> 