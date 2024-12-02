<template>
  <div class="portal-blog-layout" :class="[`layout-${layout}`, { 'dark-theme': darkTheme }]">
    <!-- 顶部导航 -->
    <header class="portal-header" :class="{ 'fixed': layout === 'fixed' }">
      <div class="header-container" :style="{ maxWidth: `${containerWidth}px` }">
        <div class="logo-area">
          <img v-if="logo" :src="logo" :alt="siteName" class="logo-image">
          <h1 v-else class="logo-text">{{ siteName }}</h1>
        </div>
        
        <nav class="main-nav">
          <ul class="nav-list">
            <li v-for="item in navigation" :key="item.path" class="nav-item">
              <a 
                :class="{ active: currentPath === item.path }"
                @click="handleNavigate(item.path)"
              >
                {{ item.title }}
              </a>
            </li>
          </ul>
        </nav>

        <div class="header-tools">
          <div class="search-box">
            <input 
              type="text" 
              v-model="searchText"
              @keyup.enter="handleSearch"
              placeholder="搜索文章..."
            >
            <button @click="handleSearch">
              <i class="el-icon-search"></i>
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- 主要内容区 -->
    <main class="portal-main" :class="{ 'has-fixed-header': layout === 'fixed' }">
      <div class="main-container" :style="{ maxWidth: `${containerWidth}px` }">
        <slot></slot>
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="portal-footer">
      <div class="footer-container" :style="{ maxWidth: `${containerWidth}px` }">
        <div class="footer-columns">
          <div 
            v-for="(column, index) in footer.columns" 
            :key="index"
            class="footer-column"
          >
            <h3>{{ column.title }}</h3>
            <ul>
              <li v-for="(link, idx) in column.links" :key="idx">
                <a 
                  :href="link.url"
                  :target="link.external ? '_blank' : '_self'"
                >
                  {{ link.text }}
                </a>
              </li>
            </ul>
          </div>
        </div>
        
        <div class="footer-copyright">
          {{ footer.copyright }}
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'PortalBlogLayout',
  props: {
    layout: String,
    containerWidth: Number,
    navPosition: String,
    primaryColor: String,
    fontFamily: String,
    darkTheme: Boolean,
    logo: String,
    siteName: String,
    navigation: Array,
    footer: Object,
    searchKeyword: String,
    onSearch: Function
  },
  data() {
    return {
      searchText: this.searchKeyword || '',
      currentPath: '/'
    }
  },
  methods: {
    handleNavigate(path) {
      this.currentPath = path
      this.$emit('navigate', path)
    },
    handleSearch() {
      if (this.searchText.trim()) {
        this.onSearch?.(this.searchText.trim())
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-blog-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  
  // 头部样式
  .portal-header {
    background: #fff;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    
    &.fixed {
      position: fixed;
      top: 0;
      left: 0;
      right: 0;
      z-index: 100;
    }
    
    .header-container {
      margin: 0 auto;
      padding: 0 20px;
      height: 64px;
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    
    .logo-area {
      display: flex;
      align-items: center;
      
      .logo-image {
        height: 40px;
        width: auto;
      }
      
      .logo-text {
        font-size: 24px;
        margin: 0;
      }
    }
    
    .main-nav {
      flex: 1;
      margin: 0 40px;
      
      .nav-list {
        display: flex;
        list-style: none;
        margin: 0;
        padding: 0;
        
        .nav-item {
          margin: 0 15px;
          
          a {
            color: #333;
            text-decoration: none;
            font-size: 16px;
            padding: 8px 0;
            walnut: pointer;
            
            &.active {
              color: var(--primary-color);
              border-bottom: 2px solid var(--primary-color);
            }
          }
        }
      }
    }
    
    .header-tools {
      .search-box {
        display: flex;
        align-items: center;
        
        input {
          width: 200px;
          height: 36px;
          padding: 0 15px;
          border: 1px solid #ddd;
          border-radius: 18px;
          outline: none;
          
          &:focus {
            border-color: var(--primary-color);
          }
        }
        
        button {
          margin-left: -40px;
          background: none;
          border: none;
          color: #666;
          walnut: pointer;
          
          &:hover {
            color: var(--primary-color);
          }
        }
      }
    }
  }
  
  // 主要内容区样式
  .portal-main {
    flex: 1;
    padding: 20px;
    
    &.has-fixed-header {
      margin-top: 64px;
    }
    
    .main-container {
      margin: 0 auto;
    }
  }
  
  // 页脚样式
  .portal-footer {
    background: #f5f5f5;
    padding: 40px 0;
    
    .footer-container {
      margin: 0 auto;
      padding: 0 20px;
    }
    
    .footer-columns {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 40px;
      margin-bottom: 40px;
      
      .footer-column {
        h3 {
          font-size: 18px;
          margin: 0 0 20px;
        }
        
        ul {
          list-style: none;
          padding: 0;
          margin: 0;
          
          li {
            margin: 10px 0;
            
            a {
              color: #666;
              text-decoration: none;
              
              &:hover {
                color: var(--primary-color);
              }
            }
          }
        }
      }
    }
    
    .footer-copyright {
      text-align: center;
      color: #999;
      border-top: 1px solid #ddd;
      padding-top: 20px;
    }
  }
  
  // 深色主题
  &.dark-theme {
    background: #1a1a1a;
    color: #fff;
    
    .portal-header {
      background: #2a2a2a;
      
      .main-nav .nav-list .nav-item a {
        color: #fff;
      }
    }
    
    .portal-footer {
      background: #2a2a2a;
      
      .footer-column ul li a {
        color: #ccc;
      }
    }
  }
}

// 响应式样式
@media screen and (max-width: 768px) {
  .portal-blog-layout {
    .portal-header {
      .header-container {
        height: auto;
        padding: 10px;
        flex-direction: column;
      }
      
      .main-nav {
        margin: 10px 0;
        width: 100%;
        
        .nav-list {
          flex-wrap: wrap;
          justify-content: center;
        }
      }
      
      .header-tools {
        width: 100%;
        margin-top: 10px;
        
        .search-box {
          width: 100%;
          
          input {
            width: 100%;
          }
        }
      }
    }
    
    .portal-main.has-fixed-header {
      margin-top: 120px;
    }
  }
}
</style> 