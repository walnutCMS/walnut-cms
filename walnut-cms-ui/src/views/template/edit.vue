<template>
  <div class="app-container">
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="template-form"
    >
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>基本信息</span>
        </div>
        <el-form-item label="模板名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="pc">PC端</el-radio>
            <el-radio label="mobile">移动端</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="缩略图" prop="thumbnail">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :headers="headers"
            :show-file-list="false"
            :on-success="handleThumbnailSuccess"
            :before-upload="beforeThumbnailUpload"
          >
            <img v-if="form.thumbnail" :src="form.thumbnail" class="thumbnail">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="模板描述" prop="description">
          <el-input
            type="textarea"
            v-model="form.description"
            :rows="4"
            placeholder="请输入模板描述"
          />
        </el-form-item>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>布局设置</span>
        </div>
        <el-form-item label="布局方式" prop="layout">
          <el-radio-group v-model="form.layout">
            <el-radio label="fixed">固定宽度</el-radio>
            <el-radio label="fluid">流式布局</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          v-if="form.layout === 'fixed'"
          label="容器宽度"
          prop="containerWidth"
        >
          <el-input-number
            v-model="form.containerWidth"
            :min="800"
            :max="1920"
            :step="10"
          />
        </el-form-item>
        <el-form-item label="导航位置" prop="navPosition">
          <el-radio-group v-model="form.navPosition">
            <el-radio label="top">顶部导航</el-radio>
            <el-radio label="left">左侧导航</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>主题设置</span>
        </div>
        <el-form-item label="主色调" prop="primaryColor">
          <el-color-picker v-model="form.primaryColor" />
        </el-form-item>
        <el-form-item label="字体" prop="fontFamily">
          <el-select v-model="form.fontFamily" placeholder="请选择字体">
            <el-option
              v-for="item in fontOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="暗色主题" prop="darkTheme">
          <el-switch v-model="form.darkTheme" />
        </el-form-item>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>SEO设置</span>
        </div>
        <el-form-item label="页面标题" prop="seo.title">
          <el-input v-model="form.seo.title" placeholder="请输入页面标题" />
        </el-form-item>
        <el-form-item label="关键词" prop="seo.keywords">
          <el-input v-model="form.seo.keywords" placeholder="请输入关键词，多个关键词用英文逗号分隔" />
        </el-form-item>
        <el-form-item label="页面描述" prop="seo.description">
          <el-input
            type="textarea"
            v-model="form.seo.description"
            :rows="4"
            placeholder="请输入页面描述"
          />
        </el-form-item>
      </el-card>

      <div class="template-form-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script>
import { getTemplate, createTemplate, updateTemplate } from '@/api/template'
import { getTemplate as getLocalTemplate, validateTemplateConfig, mergeTemplateConfig } from '@/components/templates'

export default {
  name: 'TemplateEdit',
  data() {
    return {
      isEdit: false,
      uploadUrl: process.env.VUE_APP_BASE_API + '/api/file/upload',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('accessToken')
      },
      form: {
        name: '',
        code: '',
        type: 'blog',
        thumbnail: '',
        description: '',
        layout: 'fixed',
        containerWidth: 1200,
        navPosition: 'top',
        primaryColor: '#409EFF',
        fontFamily: 'default',
        darkTheme: false,
        seo: {
          title: '',
          keywords: '',
          description: ''
        }
      },
      rules: {
        name: [
          { required: true, message: '请输入模板名称', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请选择模板类型', trigger: 'change' }
        ],
        thumbnail: [
          { required: true, message: '请上传缩略图', trigger: 'change' }
        ],
        'seo.title': [
          { required: true, message: '请输入页面标题', trigger: 'blur' }
        ]
      },
      typeOptions: [
        { label: '博客', value: 'blog' },
        { label: '企业官网', value: 'corporate' },
        { label: '电商', value: 'ecommerce' },
        { label: '门户', value: 'portal' }
      ],
      fontOptions: [
        { label: '默认字体', value: 'default' },
        { label: '微软雅黑', value: 'Microsoft YaHei' },
        { label: '宋体', value: 'SimSun' },
        { label: '黑体', value: 'SimHei' },
        { label: 'Arial', value: 'Arial' },
        { label: 'Helvetica', value: 'Helvetica' }
      ]
    }
  },
  created() {
    this.isEdit = this.$route.params.id !== '0'
    if (this.isEdit) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      try {
        // 获取远程模板数据
        const response = await getTemplate(this.$route.params.id)
        if (response.code === 200) {
          // 获取本地模板配置
          const localTemplate = getLocalTemplate(response.data.code)
          if (!localTemplate) {
            this.$message.error('模板类型不存在')
            return
          }

          // 合并配置
          this.form = mergeTemplateConfig(
            response.data.code,
            response.data
          )
        }
      } catch (error) {
        console.error('获取模板详情失败:', error)
      }
    },
    handleThumbnailSuccess(response, file) {
      if (response.code === 200) {
        this.form.thumbnail = response.data.viewUrl
      } else {
        this.$message.error('上传失败')
      }
    },
    beforeThumbnailUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('只能上传图片文件!')
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过 2MB!')
      }
      return isImage && isLt2M
    },
    cancel() {
      this.$router.push('/template/list')
    },
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            // 验证模板配置
            const validation = validateTemplateConfig(this.form.code, this.form)
            if (!validation.valid) {
              this.$message.error(validation.errors.join('\n'))
              return
            }

            const submitData = { ...this.form }
            const response = this.isEdit
              ? await updateTemplate(submitData)
              : await createTemplate(submitData)
            
            if (response.code === 200) {
              this.$message.success(this.isEdit ? '更新成功' : '创建成功')
              this.$router.push('/template/list')
            }
          } catch (error) {
            console.error(this.isEdit ? '更新模板失败:' : '创建模板失败:', error)
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.template-form {
  max-width: 980px;
  margin: 0 auto;

  .box-card {
    margin-bottom: 20px;
  }
}

.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed var(--border-color-base);
    border-radius: 6px;
    walnut: pointer;
    position: relative;
    overflow: hidden;
    transition: all 0.3s;

    &:hover {
      border-color: var(--primary-color);
    }
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 240px;
  height: 135px;
  line-height: 135px;
  text-align: center;
}

.thumbnail {
  width: 240px;
  height: 135px;
  display: block;
  object-fit: cover;
}

.template-form-footer {
  text-align: center;
  margin-top: 40px;
  padding-bottom: 40px;
}

[data-theme='dark'] {
  .avatar-uploader {
    :deep(.el-upload) {
      background-color: var(--background-color-base);
    }
  }

  .avatar-uploader-icon {
    color: var(--text-color-secondary);
  }
}
</style> 