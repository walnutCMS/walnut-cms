<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="请输入模板名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.type"
        placeholder="模板类型"
        clearable
        class="filter-item"
        style="width: 130px"
      >
        <el-option
          v-for="item in typeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        搜索
      </el-button>
      <el-button
        class="filter-item"
        type="primary"
        icon="el-icon-plus"
        @click="handleCreate"
      >
        新增模板
      </el-button>
    </div>

    <!-- 模板列表 -->
    <el-row :gutter="20">
      <el-col
        v-for="item in list"
        :key="item.id"
        :xs="24"
        :sm="12"
        :md="8"
        :lg="6"
        :xl="4"
      >
        <el-card class="template-card" :body-style="{ padding: '0px' }">
          <div class="template-preview">
            <img :src="item.thumbnail" class="image">
            <div class="mask">
              <div class="actions">
                <el-button
                  type="primary"
                  size="small"
                  icon="el-icon-view"
                  circle
                  @click="handlePreview(item)"
                />
                <el-button
                  type="warning"
                  size="small"
                  icon="el-icon-edit"
                  circle
                  @click="handleEdit(item)"
                />
                <el-button
                  type="danger"
                  size="small"
                  icon="el-icon-delete"
                  circle
                  @click="handleDelete(item)"
                />
              </div>
            </div>
          </div>
          <div class="template-info">
            <span class="template-name">{{ item.name }}</span>
            <div class="template-meta">
              <el-tag size="small" :type="item.type === 'pc' ? '' : 'success'">
                {{ item.type === 'pc' ? 'PC端' : '移动端' }}
              </el-tag>
              <span class="template-time">{{ item.updateTime }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="listQuery.pageNum"
        :page-sizes="[8, 16, 24, 32]"
        :page-size="listQuery.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>

    <!-- 预览对话框 -->
    <el-dialog
      title="模板预览"
      :visible.sync="previewVisible"
      width="90%"
      top="5vh"
      :fullscreen="isFullscreen"
      :before-close="handlePreviewClose"
    >
      <div class="preview-toolbar">
        <el-radio-group v-model="previewDevice" size="small">
          <el-radio-button label="pc">
            <i class="el-icon-monitor"></i> PC
          </el-radio-button>
          <el-radio-button label="mobile">
            <i class="el-icon-mobile"></i> 移动端
          </el-radio-button>
        </el-radio-group>
        <el-button
          type="text"
          @click="isFullscreen = !isFullscreen"
        >
          <i :class="isFullscreen ? 'el-icon-close' : 'el-icon-full-screen'"></i>
          {{ isFullscreen ? '退出全屏' : '全屏预览' }}
        </el-button>
      </div>
      <div
        class="preview-container"
        :class="{ 'is-mobile': previewDevice === 'mobile' }"
      >
        <iframe
          :src="previewUrl"
          frameborder="0"
          class="preview-frame"
        ></iframe>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getTemplateList, deleteTemplate } from '@/api/template'
import { getTemplateList as getLocalTemplateList } from '@/components/templates'

export default {
  name: 'TemplateList',
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        pageNum: 1,
        pageSize: 8,
        keyword: '',
        type: ''
      },
      typeOptions: [
        { label: '博客', value: 'blog' },
        { label: '企业官网', value: 'corporate' },
        { label: '电商', value: 'ecommerce' },
        { label: '门户', value: 'portal' }
      ],
      previewVisible: false,
      previewDevice: 'pc',
      isFullscreen: false,
      previewUrl: ''
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        // 获取远程模板列表
        const response = await getTemplateList(this.listQuery)
        if (response.code === 200) {
          // 获取本地模板列表
          const localTemplates = getLocalTemplateList()
          
          // 合并远程和本地模板数据
          this.list = response.data.list.map(item => {
            const localTemplate = localTemplates.find(t => t.code === item.code)
            return {
              ...item,
              name: localTemplate?.name || item.name,
              description: localTemplate?.description || item.description,
              thumbnail: localTemplate?.thumbnail || item.thumbnail
            }
          })
          this.total = response.data.total
        }
      } catch (error) {
        console.error('获取模板列表失败:', error)
      }
      this.listLoading = false
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.getList()
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
      this.getList()
    },
    handleCreate() {
      this.$router.push('/template/edit/0')
    },
    handleEdit(row) {
      this.$router.push(`/template/edit/${row.id}`)
    },
    handlePreview(row) {
      this.$router.push(`/template/preview/${row.id}`)
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该模板吗？', '提示', {
          type: 'warning'
        })
        const response = await deleteTemplate(row.id)
        if (response.code === 200) {
          this.$message.success('删除成功')
          this.getList()
        }
      } catch (error) {
        console.error('删除模板失败:', error)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  padding-bottom: 20px;
  .filter-item {
    margin-right: 10px;
  }
}

.template-card {
  margin-bottom: 20px;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
  }

  .template-preview {
    position: relative;
    height: 200px;
    overflow: hidden;

    .image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .mask {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0,0,0,.5);
      display: flex;
      align-items: center;
      justify-content: center;
      opacity: 0;
      transition: all 0.3s;

      .actions {
        .el-button {
          margin: 0 5px;
        }
      }
    }

    &:hover .mask {
      opacity: 1;
    }
  }

  .template-info {
    padding: 14px;

    .template-name {
      font-size: 16px;
      font-weight: bold;
      color: var(--text-color);
    }

    .template-meta {
      margin-top: 8px;
      display: flex;
      justify-content: space-between;
      align-items: center;

      .template-time {
        font-size: 12px;
        color: var(--text-color-secondary);
      }
    }
  }
}

[data-theme='dark'] {
  .template-card {
    background-color: var(--background-color-base);
    border-color: var(--border-color-base);

    .template-info {
      background-color: var(--background-color-base);
    }
  }
}
</style> 