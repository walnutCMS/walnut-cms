<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <div class="title-container">
        <h3 class="title">登录</h3>
      </div>

      <el-form-item prop="username">
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="用户名"
          name="username"
          type="text"
          tabindex="1"
          prefix-icon="el-icon-user"
          @keyup.enter.native="$refs.password.focus()"
        />
      </el-form-item>

      <el-form-item prop="password">
        <el-input
          ref="password"
          v-model="loginForm.password"
          :type="passwordVisible ? 'text' : 'password'"
          placeholder="密码"
          name="password"
          tabindex="2"
          prefix-icon="el-icon-lock"
          @keyup.enter.native="handleLogin"
        >
          <i
            slot="suffix"
            :class="['el-input__icon', passwordVisible ? 'el-icon-view' : 'el-icon-hide']"
            @click="passwordVisible = !passwordVisible"
          />
        </el-input>
      </el-form-item>

      <el-form-item prop="captcha">
        <el-row :gutter="20">
          <el-col :span="14">
            <el-input
              v-model="loginForm.captcha"
              placeholder="验证码"
              name="captcha"
              tabindex="3"
              prefix-icon="el-icon-key"
              @keyup.enter.native="handleLogin"
            />
          </el-col>
          <el-col :span="10">
            <div class="captcha-container" @click="refreshCaptcha">
              <img v-if="captchaImage" :src="captchaImage" alt="验证码">
            </div>
          </el-col>
        </el-row>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">
        {{ loading ? '登录中...' : '登录' }}
      </el-button>

      <div class="tips">
        <router-link to="/register">
          <span>还没有账号？立即注册</span>
        </router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import { getCaptcha } from '@/api/user'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        captcha: '',
        captchaKey: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
        password: [{ required: true, trigger: 'blur', message: '请输入密码' }],
        captcha: [{ required: true, trigger: 'blur', message: '请输入验证码' }]
      },
      loading: false,
      passwordVisible: false,
      captchaImage: ''
    }
  },
  created() {
    this.refreshCaptcha()
  },
  methods: {
    async refreshCaptcha() {
      try {
        const res = await getCaptcha()
        if (res.code === 200) {
          this.captchaImage = res.data.image
          this.loginForm.captchaKey = res.data.key
        }
      } catch (error) {
        console.error('获取验证码失败:', error)
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (valid) {
          this.loading = true
          try {
            await this.$store.dispatch('user/login', this.loginForm)
            this.$router.push({ path: this.$route.query.redirect || '/' })
          } catch (error) {
            this.refreshCaptcha()
            this.loginForm.captcha = ''
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  width: 100%;
  background-color: var(--background-color-base);
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-form {
  position: relative;
  width: 520px;
  max-width: 100%;
  padding: 160px 35px 0;
  margin: 0 auto;
  overflow: hidden;
}

.el-form-item {
  border: 1px solid var(--border-color-base);
  background: var(--background-color-light);
  border-radius: 5px;
  margin-bottom: 20px;
}

.el-form-item__content {
  min-height: 40px;
  line-height: 40px;
}

.el-input {
  display: inline-block;
  height: 47px;
  width: 100%;
}

.el-input input {
  background: transparent;
  border: 0;
  -webkit-appearance: none;
  border-radius: 0;
  padding: 12px 5px 12px 15px;
  height: 47px;
  color: var(--text-color);
  caret-color: var(--primary-color);
}

.el-input input:-webkit-autofill {
  box-shadow: 0 0 0 1000px var(--background-color-light) inset !important;
  -webkit-text-fill-color: var(--text-color) !important;
}

.tips {
  font-size: 14px;
  color: var(--text-color-secondary);
  text-align: center;
}

.tips span:first-of-type {
  margin-right: 16px;
}

.title-container {
  position: relative;
}

.title {
  font-size: 26px;
  color: var(--text-color);
  margin: 0 auto 40px auto;
  text-align: center;
  font-weight: bold;
}

.captcha-container {
  height: 47px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--background-color-light);
  border: 1px solid var(--border-color-base);
  border-radius: 5px;
}

.captcha-container img {
  height: 40px;
}
</style> 