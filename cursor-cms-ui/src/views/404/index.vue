<template>
  <div class="not-found">
    <div class="content">
      <div class="error-code">
        <span>4</span>
        <div class="circle pulse"></div>
        <span>4</span>
      </div>
      <h2>抱歉，页面不存在</h2>
      <p>您访问的页面可能已被删除、更名或暂时不可用</p>
      <div class="actions">
        <el-button type="primary" icon="el-icon-back" @click="goBack">返回上页</el-button>
        <el-button type="default" icon="el-icon-s-home" @click="goHome">返回首页</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NotFound',
  methods: {
    async goHome() {
      try {
        if (this.$store.state.user.accessToken) {
          await this.$router.push('/dashboard')
        } else {
          await this.$router.push('/login')
        }
      } catch (error) {
        // 忽略重复导航错误
        if (error.name !== 'NavigationDuplicated') {
          console.error('路由跳转错误:', error)
        }
      }
    },
    async goBack() {
      try {
        if (window.history.length > 1) {
          this.$router.go(-1)
        } else {
          await this.goHome()
        }
      } catch (error) {
        // 忽略重复导航错误
        if (error.name !== 'NavigationDuplicated') {
          console.error('路由跳转错误:', error)
        }
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.not-found {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--background-color-base);
  overflow: hidden;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(to right, var(--primary-color), var(--success-color), var(--warning-color), var(--error-color));
    animation: rainbow 3s linear infinite;
  }

  .content {
    text-align: center;
    padding: 40px;
    background: var(--background-color-light);
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-5%);
    
    .error-code {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 30px;
      
      span {
        font-size: 120px;
        font-weight: bold;
        color: var(--primary-color);
        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
      }
      
      .circle {
        width: 80px;
        height: 80px;
        margin: 0 10px;
        border-radius: 50%;
        background: var(--primary-color);
        position: relative;
        
        &::before {
          content: '';
          position: absolute;
          top: 50%;
          left: 50%;
          width: 40px;
          height: 40px;
          background: var(--background-color-light);
          border-radius: 50%;
          transform: translate(-50%, -50%);
        }
        
        &.pulse {
          animation: pulse 2s infinite;
        }
      }
    }

    h2 {
      font-size: 28px;
      color: var(--text-color);
      margin: 0 0 16px;
      font-weight: 500;
    }

    p {
      color: var(--text-color-secondary);
      margin-bottom: 30px;
      font-size: 16px;
    }

    .actions {
      display: flex;
      justify-content: center;
      gap: 16px;

      .el-button {
        padding: 12px 24px;
        font-size: 16px;
      }
    }
  }
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(24, 144, 255, 0.4);
  }
  70% {
    box-shadow: 0 0 0 20px rgba(24, 144, 255, 0);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(24, 144, 255, 0);
  }
}

@keyframes rainbow {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

// 响应式适配
@media screen and (max-width: 768px) {
  .not-found {
    .content {
      padding: 20px;
      width: 90%;
      
      .error-code {
        span {
          font-size: 80px;
        }
        
        .circle {
          width: 60px;
          height: 60px;
          
          &::before {
            width: 30px;
            height: 30px;
          }
        }
      }

      h2 {
        font-size: 24px;
      }

      p {
        font-size: 14px;
      }

      .actions {
        flex-direction: column;
        
        .el-button {
          width: 100%;
        }
      }
    }
  }
}
</style> 