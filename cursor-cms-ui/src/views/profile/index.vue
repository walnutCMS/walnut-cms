<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div class="user-profile">
            <div class="box-center">
              <div class="avatar-wrapper">
                <img :src="user.avatar" class="user-avatar">
                <el-upload
                  class="avatar-uploader"
                  :action="uploadUrl"
                  :headers="headers"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :before-upload="beforeAvatarUpload"
                >
                  <i class="el-icon-camera avatar-uploader-icon"></i>
                </el-upload>
              </div>
              <div class="user-name">{{ user.nickname }}</div>
            </div>
            <div class="box-center">
              <div class="user-role">{{ user.role }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="18">
        <el-card>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="个人资料" name="profile">
              <el-form ref="form" :model="user" :rules="rules" label-width="80px">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="user.username" disabled />
                </el-form-item>
                <el-form-item label="昵称" prop="nickname">
                  <el-input v-model="user.nickname" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="user.email" />
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="user.phone" />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="updateProfile">保存修改</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <el-tab-pane label="修改密码" name="password">
              <el-form ref="passwordForm" :model="passwordForm" :rules="passwordRules" label-width="100px">
                <el-form-item label="当前密码" prop="oldPassword">
                  <el-input
                    v-model="passwordForm.oldPassword"
                    type="password"
                    show-password
                    placeholder="请输入当前密码"
                  />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input
                    v-model="passwordForm.newPassword"
                    type="password"
                    show-password
                    placeholder="请输入新密码"
                  />
                </el-form-item>
                <el-form-item label="确认新密码" prop="confirmPassword">
                  <el-input
                    v-model="passwordForm.confirmPassword"
                    type="password"
                    show-password
                    placeholder="请再次输入新密码"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="updatePassword">修改密码</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { updateUserInfo, updateUserPassword, updateUserAvatar } from '@/api/user'

export default {
  name: 'Profile',
  data() {
    const validatePass = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能少于6个字符'))
      } else {
        callback()
      }
    }
    const validatePass2 = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    return {
      activeTab: 'profile',
      user: {
        username: '',
        nickname: '',
        avatar: '',
        email: '',
        phone: '',
        role: ''
      },
      rules: {
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        phone: [
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ]
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入当前密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入新密码', trigger: 'blur' },
          { validator: validatePass2, trigger: 'blur' }
        ]
      },
      uploadUrl: '/api/file/upload',
      headers: {
        Authorization: 'Bearer ' + this.$store.getters.token
      }
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'roles'
    ])
  },
  created() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      this.user = {
        ...this.$store.getters.userInfo,
        role: this.roles[0]
      }
    },
    async updateProfile() {
      try {
        await this.$refs.form.validate()
        await updateUserInfo(this.user)
        await this.$store.dispatch('user/getUserInfo')
        this.$message.success('个人资料更新成功')
      } catch (error) {
        console.error('更新个人资料失败:', error)
      }
    },
    async updatePassword() {
      try {
        await this.$refs.passwordForm.validate()
        await updateUserPassword(this.passwordForm)
        this.$message.success('密码修改成功，请重新登录')
        await this.$store.dispatch('user/logout')
        this.$router.push('/login')
      } catch (error) {
        console.error('修改密码失败:', error)
      }
    },
    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('上传头像图片只能是图片格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
        return false
      }
      return true
    },
    async handleAvatarSuccess(response) {
      if (response.code === 200) {
        try {
          this.user.avatar = response.data.viewUrl;
          await updateUserInfo(this.user)
          await this.$store.dispatch('user/getUserInfo')
          this.$message.success('头像更新成功')
        } catch (error) {
          console.error('更新头像失败:', error)
          this.$message.error(error.message || '头像更新失败')
        }
      } else {
        this.$message.error(response.message || '头像上传失败')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.box-card {
  .user-profile {
    .box-center {
      padding-top: 10px;
      text-align: center;
    }

    .user-name {
      font-size: 20px;
      font-weight: 500;
      margin: 10px 0;
    }

    .user-role {
      font-size: 14px;
      color: #999;
    }
  }
}

.avatar-wrapper {
  position: relative;
  margin: 0 auto;
  width: 128px;
  height: 128px;
  border-radius: 50%;
  overflow: hidden;

  .user-avatar {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .avatar-uploader {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 40px;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    opacity: 0;
    transition: opacity 0.3s;

    &:hover {
      opacity: 1;
    }
  }

  .avatar-uploader-icon {
    font-size: 20px;
    color: #fff;
  }
}

.el-tabs {
  margin: 20px 0;
}

.el-form {
  max-width: 460px;
}
</style>
