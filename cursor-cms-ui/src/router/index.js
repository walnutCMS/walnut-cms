import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/layout'

Vue.use(VueRouter)

// 基础路由 - 不需要权限
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true,
    meta: { title: '登录' }
  },
  {
    path: '/register',
    component: () => import('@/views/register/index'),
    hidden: true,
    meta: { title: '注册' }
  },
  {
    path: '/404',
    component: () => import('@/views/404/index'),
    hidden: true,
    meta: { title: '404' }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index'),
        meta: { title: '首页', icon: 'el-icon-s-home', affix: true }
      }
    ]
  }
]

// 动态路由 - 需要根据用户角色动态加载
export const asyncRoutes = [
  {
    path: '/category',
    component: Layout,
    redirect: '/category/list',
    name: 'Category',
    meta: { title: '栏目管理', icon: 'el-icon-menu' },
    children: [
      {
        path: 'list',
        name: 'CategoryList',
        component: () => import('@/views/category/index'),
        meta: { title: '栏目列表', icon: 'el-icon-folder' }
      }
    ]
  },
  {
    path: '/article',
    component: Layout,
    redirect: '/article/list',
    name: 'Article',
    meta: { title: '文章管理', icon: 'el-icon-document' },
    children: [
      {
        path: 'list',
        name: 'ArticleList',
        component: () => import('@/views/article/list'),
        meta: { title: '文章列表', icon: 'el-icon-document-copy' }
      },
      {
        path: 'create',
        name: 'ArticleCreate',
        component: () => import('@/views/article/edit'),
        meta: { title: '创建文章', icon: 'el-icon-edit', hidden: true }
      },
      {
        path: 'edit/:id',
        name: 'ArticleEdit',
        component: () => import('@/views/article/edit'),
        meta: { title: '编辑文章', icon: 'el-icon-edit', hidden: true }
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    name: 'Profile',
    meta: { title: '个人中心', icon: 'el-icon-user' },
    children: [
      {
        path: 'index',
        name: 'ProfileIndex',
        component: () => import('@/views/profile/index'),
        meta: { title: '个人信息', icon: 'el-icon-user' }
      }
    ]
  }
]

// 404 路由 - 放在最后加载
export const notFoundRoute = {
  path: '*',
  redirect: '/404',
  hidden: true,
  meta: { title: '404' }
}

const createRouter = () => new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// 重置路由方法
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

// 处理路由重复点击的报错问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default router
