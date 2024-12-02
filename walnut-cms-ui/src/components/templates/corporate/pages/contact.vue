<template>
  <div class="contact-page">
    <!-- Contact Info -->
    <section class="contact-info">
      <div class="container">
        <h1>联系我们</h1>
        <p class="subtitle">{{ config.contactSubtitle || '期待与您沟通交流' }}</p>
        
        <div class="info-grid">
          <div class="info-item">
            <i class="el-icon-location"></i>
            <h3>公司地址</h3>
            <p>{{ config.address || '地址信息' }}</p>
          </div>
          <div class="info-item">
            <i class="el-icon-phone"></i>
            <h3>联系电话</h3>
            <p>{{ config.phone || '电话信息' }}</p>
          </div>
          <div class="info-item">
            <i class="el-icon-message"></i>
            <h3>电子邮箱</h3>
            <p>{{ config.email || '邮箱信息' }}</p>
          </div>
          <div class="info-item">
            <i class="el-icon-time"></i>
            <h3>工作时间</h3>
            <p>{{ config.workingHours || '周一至周五 9:00-18:00' }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Contact Form -->
    <section class="contact-form">
      <div class="container">
        <div class="form-wrapper">
          <div class="form-content">
            <h2>在线留言</h2>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" placeholder="请输入您的姓名"></el-input>
              </el-form-item>
              <el-form-item label="电话" prop="phone">
                <el-input v-model="form.phone" placeholder="请输入您的联系电话"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="form.email" placeholder="请输入您的电子邮箱"></el-input>
              </el-form-item>
              <el-form-item label="主题" prop="subject">
                <el-input v-model="form.subject" placeholder="请输入留言主题"></el-input>
              </el-form-item>
              <el-form-item label="留言内容" prop="message">
                <el-input
                  type="textarea"
                  v-model="form.message"
                  :rows="4"
                  placeholder="请输入您的留言内容"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm">提交留言</el-button>
                <el-button @click="resetForm">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
          
          <div class="map-wrapper" v-if="config.mapUrl">
            <iframe
              :src="config.mapUrl"
              width="100%"
              height="100%"
              frameborder="0"
              style="border:0"
              allowfullscreen
            ></iframe>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'CorporateContact',
  props: {
    config: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      form: {
        name: '',
        phone: '',
        email: '',
        subject: '',
        message: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        subject: [
          { required: true, message: '请输入留言主题', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        message: [
          { required: true, message: '请输入留言内容', trigger: 'blur' },
          { min: 10, max: 500, message: '长度在 10 到 500 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message({
            message: '留言提交成功！我们会尽快与您联系。',
            type: 'success'
          })
          this.resetForm()
        }
      })
    },
    resetForm() {
      this.$refs.form.resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
.contact-page {
  padding: 4rem 0;

  .container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 1rem;
  }

  .contact-info {
    text-align: center;
    margin-bottom: 4rem;

    h1 {
      font-size: 2.5rem;
      color: #333;
      margin-bottom: 1rem;
    }

    .subtitle {
      color: #666;
      font-size: 1.2rem;
      margin-bottom: 3rem;
    }

    .info-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 2rem;
    }

    .info-item {
      padding: 2rem;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 12px rgba(0,0,0,0.1);
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-5px);
      }

      i {
        font-size: 3rem;
        color: #1890ff;
        margin-bottom: 1rem;
      }

      h3 {
        margin-bottom: 1rem;
        color: #333;
      }

      p {
        color: #666;
        line-height: 1.6;
      }
    }
  }

  .contact-form {
    .form-wrapper {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 2rem;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 12px rgba(0,0,0,0.1);
      overflow: hidden;
    }

    .form-content {
      padding: 2rem;

      h2 {
        font-size: 1.8rem;
        color: #333;
        margin-bottom: 2rem;
      }
    }

    .map-wrapper {
      height: 100%;
      min-height: 400px;
    }
  }
}

@media (max-width: 768px) {
  .contact-page {
    .contact-form {
      .form-wrapper {
        grid-template-columns: 1fr;
      }

      .map-wrapper {
        height: 300px;
      }
    }
  }
}
</style> 