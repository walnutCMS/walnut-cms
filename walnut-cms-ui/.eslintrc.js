module.exports = {
  // 指定代码运行环境
  env: {
    node: true  // Node.js 环境
  },
  // 继承的规则集
  extends: [
    'plugin:vue/essential',     // Vue.js 基础规则
    'eslint:recommended'        // ESLint 推荐规则
  ],
  // 解析器配置
  parserOptions: {
    parser: '@babel/eslint-parser'  // 使用 Babel 解析器
  },
  // 自定义规则
  rules: {
    'vue/multi-word-component-names': 'off'  // 禁用组件名必须多词的规则
  }
}