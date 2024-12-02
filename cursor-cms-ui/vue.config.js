module.exports = {
  // 是否需要转译第三方依赖
  transpileDependencies: [],
  
  // 部署应用包时的基本URL
  publicPath: '/',
  
  // 生产环境构建文件的目录
  outputDir: 'dist',
  
  // 放置生成的静态资源的目录
  assetsDir: 'static',
  
  // 开发环境是否启用eslint检查
  lintOnSave: process.env.NODE_ENV === 'development',
  
  // 生产环境是否生成 sourceMap 文件
  productionSourceMap: false,
  
  // 开发服务器配置
  devServer: {
    host: 'localhost',
    port: 8222,
    open: true,
    // 错误和警告的浮层配置
    overlay: {
      warnings: false,
      errors: true
    },
    // 代理配置
    proxy: {
      '/api': {
        target: 'http://localhost:8111',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/api'
        }
      }
    }
  },
  
  // CSS相关配置
  css: {
    loaderOptions: {
      sass: {
        // SASS全局变量配置已移除
      }
    }
  }
}
