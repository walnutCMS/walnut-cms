<template>
  <div class="register-container">
    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
      <div class="title-container">
        <h3 class="title">注册</h3>
      </div>

      <el-form-item prop="username">
        <el-input
          ref="username"
          v-model="registerForm.username"
          placeholder="用户名"
          name="username"
          type="text"
          tabindex="1"
          prefix-icon="el-icon-user"
          @keyup.enter.native="$refs.nickname.focus()"
        />
      </el-form-item>

      <el-form-item prop="nickname">
        <el-input
          ref="nickname"
          v-model="registerForm.nickname"
          placeholder="昵称"
          name="nickname"
          type="text"
          tabindex="2"
          prefix-icon="el-icon-user"
          @keyup.enter.native="$refs.password.focus()"
        />
      </el-form-item>

      <el-form-item prop="password">
        <el-input
          ref="password"
          v-model="registerForm.password"
          :type="passwordVisible ? 'text' : 'password'"
          placeholder="密码"
          name="password"
          tabindex="3"
          prefix-icon="el-icon-lock"
          @keyup.enter.native="$refs.confirmPassword.focus()"
        >
          <i
            slot="suffix"
            :class="['el-input__icon', passwordVisible ? 'el-icon-view' : 'el-icon-hide']"
            @click="passwordVisible = !passwordVisible"
          />
        </el-input>
      </el-form-item>

      <el-form-item prop="confirmPassword">
        <el-input
          ref="confirmPassword"
          v-model="registerForm.confirmPassword"
          :type="confirmPasswordVisible ? 'text' : 'password'"
          placeholder="确认密码"
          name="confirmPassword"
          tabindex="4"
          prefix-icon="el-icon-lock"
          @keyup.enter.native="$refs.captcha.focus()"
        >
          <i
            slot="suffix"
            :class="['el-input__icon', confirmPasswordVisible ? 'el-icon-view' : 'el-icon-hide']"
            @click="confirmPasswordVisible = !confirmPasswordVisible"
          />
        </el-input>
      </el-form-item>

      <el-form-item prop="captcha">
        <el-row :gutter="20">
          <el-col :span="14">
            <el-input
              ref="captcha"
              v-model="registerForm.captcha"
              placeholder="验证码"
              name="captcha"
              tabindex="5"
              prefix-icon="el-icon-key"
              @keyup.enter.native="handleRegister"
            />
          </el-col>
          <el-col :span="10">
            <div class="captcha-container" @click="refreshCaptcha">
              <img v-if="captchaImage" :src="captchaImage" alt="验证码">
            </div>
          </el-col>
        </el-row>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleRegister">
        {{ loading ? '注册中...' : '注册' }}
      </el-button>

      <div class="tips">
        <router-link to="/login">
          <span>已有账号？立即登录</span>
        </router-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import { getCaptcha } from '@/api/user'

export default {
  name: 'Register',
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能少于6个字符'))
      } else {
        if (this.registerForm.confirmPassword !== '') {
          this.$refs.registerForm.validateField('confirmPassword')
        }
        callback()
      }
    }
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      registerForm: {
        username: '',
        nickname: '',
        password: '',
        confirmPassword: '',
        captcha: '',
        captchaKey: ''
      },
      registerRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入用户名' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        nickname: [
          { required: true, trigger: 'blur', message: '请输入昵称' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入密码' },
          { validator: validatePassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, trigger: 'blur', message: '请再次输入密码' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ],
        captcha: [
          { required: true, trigger: 'blur', message: '请输入验证码' }
        ]
      },
      loading: false,
      passwordVisible: false,
      confirmPasswordVisible: false,
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
          this.registerForm.captchaKey = res.data.key
        }
      } catch (error) {
        console.error('获取验证码失败:', error)
      }
    },
    handleRegister() {
      this.$refs.registerForm.validate(async valid => {
        if (valid) {
          this.loading = true
          try {
            const { confirmPassword, ...registerData } = this.registerForm
            await this.$store.dispatch('user/register', registerData)
            this.$message.success('注册成功，请登录')
            this.$router.push('/login')
          } catch (error) {
            this.refreshCaptcha()
            this.registerForm.captcha = ''
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
.register-container {
  min-height: 100vh;
  width: 100%;
  background-color: var(--background-color-base);
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.register-form {
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