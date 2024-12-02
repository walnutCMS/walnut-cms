<template>
  <div class="corporate-template">
    <Layout :config="config">
      <router-view></router-view>
    </Layout>
  </div>
</template>

<script>
import Layout from './layout/index.vue'
import Home from './pages/home.vue'
import About from './pages/about.vue'
import Contact from './pages/contact.vue'
import Product from './pages/product.vue'

export default {
  name: 'CorporateTemplate',
  components: {
    Layout,
    Home,
    About,
    Contact,
    Product
  },
  props: {
    config: {
      type: Object,
      default: () => ({})
    }
  },
  created() {
    // 注册路由
    const routes = [
      {
        path: '/',
        name: 'corporate-home',
        component: Home
      },
      {
        path: '/product',
        name: 'corporate-product',
        component: Product
      },
      {
        path: '/about',
        name: 'corporate-about',
        component: About
      },
      {
        path: '/contact',
        name: 'corporate-contact',
        component: Contact
      }
    ]

    // 添加路由前缀
    const baseRoute = {
      path: '/corporate',
      component: Layout,
      children: routes
    }

    this.$router.addRoutes([baseRoute])
  }
}
</script>

<style lang="scss" scoped>
.corporate-template {
  min-height: 100vh;
  background: #f5f5f5;
}
</style> 