<template>
  <div class="preview-page">
    <div class="preview-header">
      <div class="left">
        <el-button icon="el-icon-back" @click="goBack">返回</el-button>
        <span class="template-name">{{ templateInfo.name }}</span>
      </div>
      <div class="right">
        <el-radio-group v-model="device" size="small">
          <el-radio-button label="pc">
            <i class="el-icon-monitor"></i> PC端
          </el-radio-button>
          <el-radio-button label="mobile">
            <i class="el-icon-mobile"></i> 移动端
          </el-radio-button>
        </el-radio-group>
        <el-button
          type="primary"
          icon="el-icon-check"
          @click="handleApply"
        >
          应用模板
        </el-button>
      </div>
    </div>

    <div class="preview-container" :class="{ 'is-mobile': device === 'mobile' }">
      <div v-loading="loading" class="preview-frame">
        <component
          :is="currentComponent"
          v-if="!loading && currentComponent"
          v-bind="templateData"
        />
      </div>
    </div>

    <!-- 应用模板对话框 -->
    <el-dialog
      title="应用模板"
      :visible.sync="applyDialogVisible"
      width="500px"
    >
      <el-form
        ref="applyForm"
        :model="applyForm"
        :rules="applyRules"
        label-width="100px"
      >
        <el-form-item label="应用页面" prop="pageId">
          <el-select
            v-model="applyForm.pageId"
            placeholder="请选择要应用的页面"
            style="width: 100%"
          >
            <el-option
              v-for="item in pageOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否覆盖" prop="override">
          <el-switch
            v-model="applyForm.override"
            active-text="覆盖现有内容"
            inactive-text="保留现有内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="applyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmApply">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getTemplate, previewTemplate, applyTemplate } from '@/api/template'
import { getPageList } from '@/api/page'
import PcDefault from '@/components/templates/pc/default'
import MobileDefault from '@/components/templates/mobile/default'

const templateComponents = {
  pc: {
    default: PcDefault
  },
  mobile: {
    default: MobileDefault
  }
}

export default {
  name: 'TemplatePreview',
  components: {
    PcDefault,
    MobileDefault
  },
  data() {
    return {
      loading: true,
      device: 'pc',
      templateInfo: {},
      templateData: {},
      currentComponent: null,
      applyDialogVisible: false,
      applyForm: {
        pageId: '',
        override: false
      },
      applyRules: {
        pageId: [
          { required: true, message: '请选择要应用的页面', trigger: 'change' }
        ]
      },
      pageOptions: []
    }
  },
  created() {
    this.fetchData()
    this.fetchPages()
  },
  methods: {
    async fetchData() {
      try {
        // 获取模板基本信息
        const templateRes = await getTemplate(this.$route.params.id)
        if (templateRes.code === 200) {
          this.templateInfo = templateRes.data
        }

        // 获取模板预览数据
        const previewRes = await previewTemplate(this.$route.params.id)
        if (previewRes.code === 200) {
          this.templateData = previewRes.data
          // 设置当前模板组件
          this.currentComponent = templateComponents[this.templateInfo.type]?.[this.templateInfo.code] || null
        }
      } catch (error) {
        console.error('获取模板数据失败:', error)
        this.$message.error('获取模板数据失败')
      } finally {
        this.loading = false
      }
    },
    async fetchPages() {
      try {
        const response = await getPageList()
        if (response.code === 200) {
          this.pageOptions = response.data
        }
      } catch (error) {
        console.error('获取页面列表失败:', error)
      }
    },
    goBack() {
      this.$router.push('/template/list')
    },
    handleApply() {
      this.applyDialogVisible = true
    },
    async confirmApply() {
      this.$refs.applyForm.validate(async valid => {
        if (valid) {
          try {
            const response = await applyTemplate({
              templateId: this.$route.params.id,
              ...this.applyForm
            })
            
            if (response.code === 200) {
              this.$message.success('应用模板成功')
              this.applyDialogVisible = false
            }
          } catch (error) {
            console.error('应用模板失败:', error)
            this.$message.error('应用模板失败')
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.preview-page {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--background-color-base);
  z-index: 2000;
}

.preview-header {
  height: 60px;
  padding: 0 20px;
  background-color: var(--background-color-base);
  border-bottom: 1px solid var(--border-color-base);
  display: flex;
  justify-content: space-between;
  align-items: center;

  .left {
    display: flex;
    align-items: center;

    .template-name {
      margin-left: 15px;
      font-size: 16px;
      font-weight: bold;
      color: var(--text-color);
    }
  }

  .right {
    display: flex;
    align-items: center;

    .el-radio-group {
      margin-right: 15px;
    }
  }
}

.preview-container {
  height: calc(100vh - 60px);
  padding: 20px;
  overflow: auto;
  background-color: var(--background-color-light);

  &.is-mobile {
    display: flex;
    justify-content: center;
    
    .preview-frame {
      width: 375px;
      min-height: 667px;
      background-color: #fff;
      box-shadow: 0 0 20px rgba(0,0,0,.1);
      overflow: hidden;
    }
  }

  .preview-frame {
    width: 100%;
    min-height: 100%;
    background-color: #fff;
    border-radius: 4px;
    overflow: hidden;
  }
}

[data-theme='dark'] {
  .preview-frame {
    background-color: var(--background-color-base);
  }
}
</style> 