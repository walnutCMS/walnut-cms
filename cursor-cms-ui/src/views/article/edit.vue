<template>
  <div class="article-edit">
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入文章标题"/>
      </el-form-item>

      <el-form-item label="分类" prop="categoryId">
        <el-cascader
          v-model="form.categoryId"
          :options="categoryOptions"
          :props="{
            value: 'id',
            label: 'name',
            children: 'children',
            checkStrictly: true,
            emitPath: false
          }"
          placeholder="请选择分类"
          clearable
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="摘要" prop="summary">
        <el-input
            v-model="form.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入文章摘要"
        />
      </el-form-item>

      <el-form-item label="内容" prop="content">
        <editor v-model="form.content" :height="500"/>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择文章状态">
          <el-option value="draft" label="草稿" />
          <el-option value="published" label="已发布" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import Editor from '@/components/Editor'
import {createArticle, getArticle, updateArticle} from '@/api/article'
import {getCategoryList} from '@/api/category'
import {getTemplateList} from '@/api/template'

export default {
  name: 'ArticleEdit',
  components: {
    Editor
  },
  data() {
    return {
      form: {
        title: '',
        categoryId: undefined,
        summary: '',
        content: '',
        status: 'draft'
      },
      rules: {
        title: [{required: true, message: '请输入标题', trigger: 'blur'}],
        categoryId: [{required: true, message: '请选择分类', trigger: 'change'}],
        content: [{required: true, message: '请输入内容', trigger: 'blur'}]
      },
      categoryGroups: [],
      categoryOptions: [],
      templates: [],
      loading: false
    }
  },
  created() {
    this.fetchCategories()
    this.fetchTemplates()
    const id = this.$route.params.id
    if (id && id !== '0') {
      this.fetchArticle(id)
    }
  },
  methods: {
    async fetchCategories() {
      try {
        const response = await getCategoryList()
        if (response.code === 200) {
          this.categoryOptions = response.data
        }
      } catch (error) {
        console.error('获取分类列表失败:', error)
        this.$message.error('获取分类列表失败')
      }
    },
    async fetchTemplates() {
      try {
        const response = await getTemplateList()
        if (response.code === 200) {
          this.templates = response.data.list
        }
      } catch (error) {
        console.error('获取模板列表失败:', error)
      }
    },
    async fetchArticle(id) {
      try {
        const response = await getArticle(id)
        if (response.code === 200) {
          const {title, categoryId, summary, content, status} = response.data
          this.form = {
            title,
            categoryId,
            summary,
            content,
            status
          }
        }
      } catch (error) {
        console.error('获取文章详情失败:', error)
      }
    },
    async submitForm() {
      const id = this.$route.params.id
      const isEdit = id && id !== '0'
      try {
        await this.$refs.form.validate()
        const apiCall = isEdit ? updateArticle : createArticle
        const data = {...this.form}

        const response = await apiCall(isEdit ? id : data, isEdit ? data : undefined)
        if (response.code === 200) {
          this.$message.success(isEdit ? '更新成功' : '创建成功')
          this.$router.push('/article/list')
        }
      } catch (error) {
        console.error(isEdit ? '更新文章失败:' : '创建文章失败:', error)
      }
    },
    cancel() {
      this.$router.push('/article/list')
    }
  }
}
</script>

<style lang="scss" scoped>
.article-edit {
  padding: 20px;
  background-color: var(--background-color-base);

  .el-form {
    max-width: 1200px;
    margin: 0 auto;
  }
}
</style>
