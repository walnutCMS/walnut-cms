<template>
  <div class="blog-template" :class="{ 'is-mobile': isMobile }">
    <component
      :is="currentLayout"
      v-bind="layoutProps"
      @navigate="handleNavigate"
    >
      <component
        :is="currentPage"
        v-if="currentPage"
        v-bind="pageProps"
        @navigate="handleNavigate"
        @load-more="handleLoadMore"
        @comment="handleComment"
        @reply="handleReply"
        @like="handleLike"
        @share="handleShare"
      />
    </component>
  </div>
</template>

<script>
import BlogLayout from './layout'
import HomePage from './pages/home'
import ArticlePage from './pages/article'

export default {
  name: 'BlogTemplate',
  components: {
    BlogLayout,
    HomePage,
    ArticlePage
  },
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
    darkTheme: {
      type: Boolean,
      default: false
    },
    // 内容配置
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
    },
    // 页面数据
    pageData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      currentPath: '/',
      isMobile: window.innerWidth < 768
    }
  },
  computed: {
    currentLayout() {
      return BlogLayout
    },
    currentPage() {
      const pageMap = {
        '/': HomePage,
        '/article': ArticlePage
      }
      return pageMap[this.currentPath] || HomePage
    },
    layoutProps() {
      return {
        layout: this.layout,
        containerWidth: this.containerWidth,
        navPosition: this.navPosition,
        primaryColor: this.primaryColor,
        fontFamily: this.fontFamily,
        darkTheme: this.darkTheme,
        logo: this.logo,
        siteName: this.siteName,
        navigation: this.navigation,
        footer: this.footer
      }
    },
    pageProps() {
      return {
        ...this.pageData,
        isMobile: this.isMobile
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
      this.currentPath = path
      this.$emit('navigate', path)
    },
    handleResize() {
      this.isMobile = window.innerWidth < 768
    },
    handleLoadMore() {
      this.$emit('load-more')
    },
    handleComment(data) {
      this.$emit('comment', data)
    },
    handleReply(data) {
      this.$emit('reply', data)
    },
    handleLike(data) {
      this.$emit('like', data)
    },
    handleShare(data) {
      this.$emit('share', data)
    }
  }
}
</script>

<style lang="scss" scoped>
.blog-template {
  min-height: 100vh;
  
  &.is-mobile {
    // 移动端样式
  }
}
</style> 