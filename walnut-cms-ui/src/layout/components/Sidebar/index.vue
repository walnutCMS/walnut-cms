<template>
  <el-menu
    :default-active="activeMenu"
    :collapse="!sidebar.opened"
    :background-color="menuBg"
    :text-color="menuText"
    :active-text-color="menuActiveText"
    :unique-opened="true"
    :collapse-transition="false"
    router
  >
    <sidebar-item 
      v-for="route in sidebarRoutes"
      :key="route.path"
      :item="route"
      :base-path="route.path"
    />
  </el-menu>
</template>

<script>
import { mapState } from 'vuex'
import SidebarItem from './SidebarItem'

export default {
  name: 'AppSidebar',
  components: { SidebarItem },
  computed: {
    ...mapState({
      sidebar: state => state.app.sidebar,
      permission_routes: state => state.permission.routes
    }),
    activeMenu() {
      const { meta, path } = this.$route
      if (meta && meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    menuBg() {
      return 'var(--background-color-base)'
    },
    menuText() {
      return 'var(--text-color)'
    },
    menuActiveText() {
      return 'var(--primary-color)'
    },
    sidebarRoutes() {
      const routes = this.permission_routes.filter(route => {
        if (route.hidden) return false
        // 如果是根路由且有子路由，检查第一个子路由
        if (route.path === '/' && route.children) {
          return !route.children[0].hidden
        }
        // 如果有子路由，检查是否所有子路由都被隐藏
        if (route.children) {
          return route.children.some(child => !child.hidden)
        }
        return true
      })
      return routes
    }
  }
}
</script>

<style lang="scss" scoped>
.el-menu {
  border: none;
  height: 100%;
}

:deep(.el-menu--collapse) {
  width: 54px;
}

:deep(.el-menu) {
  width: 210px;
}
</style> 