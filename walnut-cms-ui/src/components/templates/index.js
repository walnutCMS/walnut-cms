// PC端默认模板
import PcDefault from './pc/default'
// 移动端默认模板
import MobileDefault from './mobile/default'
// 博客模板
import BlogTemplate from './blog'
// 企业官网模板
import CorporateTemplate from './corporate'

// 模板配置
export const templates = {
  pc: {
    name: 'PC端默认模板',
    code: 'pc',
    description: '适用于一般网站的PC端模板',
    thumbnail: '/static/templates/pc.png',
    component: PcDefault,
    defaultConfig: {
      layout: 'fixed',
      containerWidth: 1200,
      navPosition: 'top',
      primaryColor: '#409EFF',
      fontFamily: 'default',
      darkTheme: false,
      logo: '/static/logo.png',
      siteName: '默认网站',
      navigation: [
        { id: 1, name: '首页', path: '/' },
        { id: 2, name: '关于', path: '/about' }
      ],
      footer: {
        columns: [
          {
            title: '关于我们',
            items: [
              { id: 1, name: '关于我们', url: '/about' },
              { id: 2, name: '联系我们', url: '/contact' }
            ]
          }
        ],
        copyright: '© 2024 默认网站. All rights reserved.'
      }
    }
  },
  mobile: {
    name: '移动端默认模板',
    code: 'mobile',
    description: '适用于一般网站的移动端模板',
    thumbnail: '/static/templates/mobile.png',
    component: MobileDefault,
    defaultConfig: {
      primaryColor: '#409EFF',
      fontFamily: 'default',
      darkTheme: false,
      logo: '/static/logo.png',
      siteName: '默认网站',
      navigation: [
        { id: 1, name: '首页', path: '/' },
        { id: 2, name: '关于', path: '/about' }
      ],
      footer: {
        columns: [
          {
            title: '关于我们',
            items: [
              { id: 1, name: '关于我们', url: '/about' },
              { id: 2, name: '联系我们', url: '/contact' }
            ]
          }
        ],
        copyright: '© 2024 默认网站. All rights reserved.'
      }
    }
  },
  blog: {
    name: '博客模板',
    code: 'blog',
    description: '适用于个人博客、技术博客等内容展示网站',
    thumbnail: '/static/templates/blog.png',
    component: BlogTemplate,
    defaultConfig: {
      layout: 'fixed',
      containerWidth: 1200,
      navPosition: 'top',
      primaryColor: '#409EFF',
      fontFamily: 'default',
      darkTheme: false,
      logo: '/static/logo.png',
      siteName: '我的博客',
      navigation: [
        { id: 1, name: '首页', path: '/' },
        { id: 2, name: '文章', path: '/article' },
        { id: 3, name: '分类', path: '/category' },
        { id: 4, name: '关于', path: '/about' }
      ],
      footer: {
        columns: [
          {
            title: '关于我们',
            items: [
              { id: 1, name: '关于我们', url: '/about' },
              { id: 2, name: '联系我们', url: '/contact' }
            ]
          },
          {
            title: '友情链接',
            items: [
              { id: 1, name: 'GitHub', url: 'https://github.com' },
              { id: 2, name: 'Vue.js', url: 'https://vuejs.org' }
            ]
          }
        ],
        copyright: '© 2024 我的博客. All rights reserved.'
      }
    }
  },
  corporate: {
    name: '企业官网模板',
    code: 'corporate',
    description: '适用于企业官网、品牌展示等商业网站',
    thumbnail: '/static/templates/corporate.png',
    component: CorporateTemplate,
    defaultConfig: {
      layout: 'fixed',
      containerWidth: 1200,
      navPosition: 'top',
      primaryColor: '#1890ff',
      fontFamily: 'default',
      darkTheme: false,
      logo: '/static/logo.png',
      siteName: '企业官网',
      navigation: [
        { id: 1, name: '首页', path: '/' },
        { id: 2, name: '关于我们', path: '/about' },
        { id: 3, name: '产品服务', path: '/product' },
        { id: 4, name: '联系我们', path: '/contact' }
      ],
      footer: {
        columns: [
          {
            title: '关于我们',
            items: [
              { id: 1, name: '公司简介', url: '/about' },
              { id: 2, name: '发展历程', url: '/history' },
              { id: 3, name: '企业文化', url: '/culture' }
            ]
          },
          {
            title: '产品服务',
            items: [
              { id: 1, name: '产品中心', url: '/product' },
              { id: 2, name: '解决方案', url: '/solution' },
              { id: 3, name: '成功案例', url: '/case' }
            ]
          },
          {
            title: '联系我们',
            items: [
              { id: 1, name: '联系方式', url: '/contact' },
              { id: 2, name: '加入我们', url: '/join' },
              { id: 3, name: '服务支持', url: '/support' }
            ]
          }
        ],
        copyright: '© 2024 企业官网. All rights reserved.'
      }
    }
  }
}

// 获取所有模板列表
export function getTemplateList() {
  return Object.keys(templates).map(key => ({
    code: key,
    ...templates[key],
    component: undefined // 不返回组件，避免数据太大
  }))
}

// 获取指定模板
export function getTemplate(code) {
  return templates[code]
}

// 获取模板组件
export function getTemplateComponent(code) {
  const template = templates[code]
  return template ? template.component : null
}

// 获取模板默认配置
export function getTemplateDefaultConfig(code) {
  const template = templates[code]
  return template ? template.defaultConfig : null
}

// 验证模板配置
export function validateTemplateConfig(code, config) {
  const template = templates[code]
  if (!template) {
    return {
      valid: false,
      errors: ['模板不存在']
    }
  }

  const errors = []
  const defaultConfig = template.defaultConfig

  // 检查必需字段
  const requiredFields = ['layout', 'navPosition', 'primaryColor']
  requiredFields.forEach(field => {
    if (!config[field]) {
      errors.push(`缺少必需字段: ${field}`)
    }
  })

  // 检查字段类型
  if (config.containerWidth && typeof config.containerWidth !== 'number') {
    errors.push('containerWidth 必须是数字')
  }
  if (config.darkTheme && typeof config.darkTheme !== 'boolean') {
    errors.push('darkTheme 必须是布尔值')
  }

  // 检查导航配置
  if (config.navigation && !Array.isArray(config.navigation)) {
    errors.push('navigation 必须是数组')
  }

  // 检查页脚配置
  if (config.footer) {
    if (!Array.isArray(config.footer.columns)) {
      errors.push('footer.columns 必须是数组')
    }
    if (typeof config.footer.copyright !== 'string') {
      errors.push('footer.copyright 必须是字符串')
    }
  }

  return {
    valid: errors.length === 0,
    errors
  }
}

// 合并模板配置
export function mergeTemplateConfig(code, userConfig) {
  const defaultConfig = getTemplateDefaultConfig(code)
  if (!defaultConfig) {
    return userConfig
  }

  return {
    ...defaultConfig,
    ...userConfig,
    // 深度合并特定字段
    navigation: userConfig.navigation || defaultConfig.navigation,
    footer: {
      ...defaultConfig.footer,
      ...userConfig.footer,
      columns: userConfig.footer?.columns || defaultConfig.footer.columns
    }
  }
} 