<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in breadcrumbs" :key="item.path">
        <span 
          v-if="index === breadcrumbs.length - 1" 
          class="no-redirect"
        >{{ item.meta.title }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
export default {
  name: 'AppBreadcrumb',
  data() {
    return {
      breadcrumbs: []  // 面包屑导航项列表
    }
  },
  watch: {
    // 监听路由变化，更新面包屑
    $route: {
      immediate: true,
      handler() {
        this.getBreadcrumb()
      }
    }
  },
  methods: {
    // 获取面包屑导航数据
    getBreadcrumb() {
      // 过滤有标题的路由
      let matched = this.$route.matched.filter(item => item.meta && item.meta.title)
      const first = matched[0]
      // 如果不是首页，添加首页到面包屑开头
      if (!this.isHome(first)) {
        matched = [{ path: '/dashboard', meta: { title: '首页' }}].concat(matched)
      }
      this.breadcrumbs = matched
    },
    // 判断是否是首页
    isHome(route) {
      return route.path === '/dashboard'
    },
    // 处理面包屑点击
    handleLink(item) {
      const { redirect, path } = item
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      this.$router.push(path)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px;
  margin-left: 8px;

  .no-redirect {
    color: var(--text-color);
    cursor: text;
  }

  a {
    color: var(--text-color-secondary);
    cursor: pointer;
    
    &:hover {
      color: var(--primary-color);
    }
  }
}

.breadcrumb-enter-active,
.breadcrumb-leave-active {
  transition: all .5s;
}

.breadcrumb-enter,
.breadcrumb-leave-active {
  opacity: 0;
  transform: translateX(20px);
}

.breadcrumb-leave-active {
  position: absolute;
}
</style> 