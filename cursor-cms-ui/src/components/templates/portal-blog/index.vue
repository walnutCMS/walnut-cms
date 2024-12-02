<template>
  <div class="portal-blog-template" :class="{ 'is-mobile': isMobile }">
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
        @search="handleSearch"
        @filter="handleFilter"
      />
    </component>
  </div>
</template>

<script>
import PortalLayout from './layout'
import HomePage from './pages/home'
import CategoryPage from './pages/category'
import ArticlePage from './pages/article'
import TopicPage from './pages/topic'
import SearchPage from './pages/search'

export default {
  name: 'PortalBlogTemplate',
  components: {
    PortalLayout,
    HomePage,
    CategoryPage,
    ArticlePage,
    TopicPage,
    SearchPage
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
      default: '门户博客'
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
      isMobile: window.innerWidth < 768,
      searchKeyword: '',
      currentCategory: null,
      currentTopic: null
    }
  },
  computed: {
    currentLayout() {
      return PortalLayout
    },
    currentPage() {
      const pageMap = {
        '/': HomePage,
        '/category': CategoryPage,
        '/article': ArticlePage,
        '/topic': TopicPage,
        '/search': SearchPage
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
        footer: this.footer,
        searchKeyword: this.searchKeyword,
        onSearch: this.handleSearch
      }
    },
    pageProps() {
      return {
        ...this.pageData,
        isMobile: this.isMobile,
        currentCategory: this.currentCategory,
        currentTopic: this.currentTopic
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
    handleNavigate(path, params = {}) {
      this.currentPath = path
      if (params.category) {
        this.currentCategory = params.category
      }
      if (params.topic) {
        this.currentTopic = params.topic
      }
      this.$emit('navigate', { path, params })
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
    },
    handleSearch(keyword) {
      this.searchKeyword = keyword
      this.currentPath = '/search'
      this.$emit('search', keyword)
    },
    handleFilter(filters) {
      this.$emit('filter', filters)
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-blog-template {
  min-height: 100vh;
  
  &.is-mobile {
    // 移动端样式
  }
}
</style> 