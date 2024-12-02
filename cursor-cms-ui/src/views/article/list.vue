<template>
  <div class="app-container">
    <el-container>
      <!-- 左侧栏目树 -->
      <el-aside width="260px">
        <el-card class="category-tree">
          <div slot="header" class="tree-header">
            <span>栏目列表</span>
          </div>
          <el-tree
              ref="tree"
              :data="categoryOptions"
              :props="{
              label: 'name',
              children: 'children'
            }"
              node-key="id"
              highlight-current
              :default-expand-all="true"
              :expand-on-click-node="false"
              :render-content="renderTreeNode"
              @node-click="handleNodeClick"
          />
        </el-card>
      </el-aside>

      <!-- 右侧文章列表 -->
      <el-main>
        <div class="page-header">
          <div class="header-title">
            <h2>文章列表</h2>
            <span v-if="currentCategory" class="category-path">
              当前栏目：{{ currentCategory.name }}
            </span>
          </div>
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新建文章
          </el-button>
        </div>

        <el-card>
          <div class="filter-container">
            <el-input
                v-model="listQuery.keyword"
                placeholder="标题/内容"
                style="width: 200px;"
                class="filter-item"
                @keyup.enter.native="handleFilter"
            />
            <el-button
                class="filter-item"
                type="primary"
                icon="el-icon-search"
                @click="handleFilter"
            >
              搜索
            </el-button>
            <el-button
              v-if="multipleSelection.length > 0"
              class="filter-item"
              type="danger"
              icon="el-icon-delete"
              @click="handleBatchDelete"
            >
              批量删除
            </el-button>
            <el-button
              v-if="multipleSelection.length > 0"
              class="filter-item"
              type="success"
              icon="el-icon-upload2"
              @click="handleBatchPublish"
            >
              批量发布
            </el-button>
          </div>

          <el-table
              v-loading="listLoading"
              :data="list"
              border
              fit
              highlight-current-row
              style="width: 100%;"
          >
            <el-table-column
              width="55"
            >
              <template slot="header">
                <el-checkbox
                  v-model="isAllSelected"
                  :indeterminate="isIndeterminate"
                  @change="handleSelectAll"
                />
              </template>
              <template slot-scope="{row}">
                <el-checkbox
                  v-model="row.selected"
                  :disabled="row.isSystem"
                  @change="(val) => handleRowSelect(row, val)"
                />
              </template>
            </el-table-column>
            <el-table-column
                prop="id"
                label="ID"
                align="center"
                width="80"
            />
            <el-table-column
                prop="title"
                label="标题"
                min-width="200"
            >
              <template slot-scope="{row}">
                <router-link
                    :to="'/article/edit/'+row.id"
                    class="link-type"
                >
                  <span class="article-title">{{ row.title }}</span>
                </router-link>
              </template>
            </el-table-column>
            <el-table-column
                prop="category"
                label="分类"
                width="120"
            >
              <template slot-scope="scope">
                {{ getCategoryName(scope.row.categoryId) }}
              </template>
            </el-table-column>
            <el-table-column
                prop="summary"
                label="摘要"
                show-overflow-tooltip
            />
            <el-table-column
                prop="viewCount"
                label="浏览量"
                align="center"
                width="100"
            />
            <el-table-column
                prop="createTime"
                label="创建时间"
                align="center"
                width="180"
            >
              <template slot-scope="{row}">
                <span>{{ row.createTime }}</span>
              </template>
            </el-table-column>
            <el-table-column
                prop="status"
                label="状态"
                align="center"
                width="100"
            >
              <template slot-scope="{row}">
                <el-select 
                  v-model="row.status" 
                  size="mini" 
                  style="width: 90px"
                  @change="(value) => handleStatusChange(row, value)"
                >
                  <el-option label="草稿" value="draft">
                    <span style="color: #909399">草稿</span>
                  </el-option>
                  <el-option label="已发布" value="published">
                    <span style="color: #67C23A">已发布</span>
                  </el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column
                label="操作"
                align="center"
                width="220"
            >
              <template slot-scope="{row}">
                <el-button
                    size="mini"
                    type="primary"
                    icon="el-icon-edit"
                    @click="handleUpdate(row)"
                >
                  编辑
                </el-button>
                <el-dropdown trigger="click" @command="(command) => handleCommand(command, row)">
                  <el-button type="primary" size="mini">
                    更多<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="preview">
                      <i class="el-icon-view"/> 预览
                    </el-dropdown-item>
                    <el-dropdown-item command="delete" divided>
                      <i class="el-icon-delete" style="color: #F56C6C"/>
                      <span style="color: #F56C6C">删除</span>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>

          <pagination
              v-show="total>0"
              :total="total"
              :page.sync="listQuery.pageNum"
              :limit.sync="listQuery.pageSize"
              @pagination="getList"
          />
        </el-card>
      </el-main>
    </el-container>

    <!-- 预览对话框 -->
    <el-dialog
        title="文章预览"
        :visible.sync="previewVisible"
        width="800px"
        class="preview-dialog"
        append-to-body
    >
      <div class="preview-content">
        <h2 class="preview-title">{{ previewData.title }}</h2>
        <div class="preview-info">
          <span>分类：{{ previewData.categoryName }}</span>
          <span>创建时间：{{ previewData.createTime }}</span>
          <span>浏览量：{{ previewData.viewCount }}</span>
        </div>
        <div class="preview-body" v-html="previewData.content"/>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  deleteArticle,
  getArticle,
  getArticleList,
  publishArticle,
  unpublishArticle,
  batchDeleteArticles,
  batchPublishArticles
} from '@/api/article'
import {getCategoryList} from '@/api/category'
import {getTemplateList} from '@/api/template'
import Pagination from '@/components/Pagination'

export default {
  name: 'ArticleList',
  components: {Pagination},
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        pageNum: 1,
        pageSize: 10,
        keyword: '',
        categoryId: undefined
      },
      categoryOptions: [],
      currentCategory: null,
      previewVisible: false,
      previewData: {},
      multipleSelection: [],
      categories: [],
      templateList: [],
      categoryMap: {},
      templateMap: {}
    }
  },
  created() {
    this.getList()
    this.getCategoryOptions()
    this.fetchTemplates()
  },
  computed: {
    isAllSelected() {
      return this.list && this.list.length > 0 && this.list.every(item => item.isSystem || item.selected)
    },
    isIndeterminate() {
      return this.list && this.list.some(item => item.selected) && !this.isAllSelected
    }
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const response = await getArticleList({
          pageNum: this.listQuery.pageNum,
          pageSize: this.listQuery.pageSize,
          keyword: this.listQuery.keyword,
          categoryId: this.listQuery.categoryId
        })
        const {code, data} = response
        if (code === 200) {
          this.list = data.list.map(item => ({
            ...item,
            selected: false
          }))
          this.total = data.total
          this.multipleSelection = []
        }
      } catch (error) {
        console.error('获取文章列表失败:', error)
        this.$message.error('获取文章列表失败')
      }
      this.listLoading = false
    },
    async getCategoryOptions() {
      try {
        const {data} = await getCategoryList()
        this.categoryOptions = data
        // 创建分类映射
        this.createCategoryMap(data)
      } catch (error) {
        console.error('获取分类列表失败:', error)
      }
    },
    createCategoryMap(categories, parentPath = '') {
      categories.forEach(category => {
        const fullPath = parentPath ? `${parentPath}/${category.name}` : category.name
        this.categoryMap[category.id] = fullPath
        if (category.children && category.children.length > 0) {
          this.createCategoryMap(category.children, fullPath)
        }
      })
    },
    getCategoryName(categoryId) {
      return this.categoryMap[categoryId] || '未分类'
    },
    handleNodeClick(data) {
      this.currentCategory = data
      this.listQuery.categoryId = data.id
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.getList()
    },
    handleCreate() {
      this.$router.push('/article/create')
    },
    handleUpdate(row) {
      this.$router.push(`/article/edit/${row.id}`)
    },
    async handleCommand(command, row) {
      switch (command) {
        case 'preview':
          await this.handlePreview(row)
          break
        case 'publish':
          await this.handlePublish(row)
          break
        case 'unpublish':
          await this.handleUnpublish(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },
    async handlePreview(row) {
      this.listLoading = true
      try {
        const response = await getArticle(row.id)
        if (response.code === 200) {
          this.previewData = response.data
          this.previewVisible = true
        } else {
          this.$message.error(response.message || '获取文章详情失败')
        }
      } catch (error) {
        console.error('获取文章详情失败:', error)
        this.$message.error('获取文章详情失败')
      }
      this.listLoading = false
    },
    async handlePublish(row) {
      try {
        const response = await publishArticle(row.id)
        if (response.code === 200) {
          this.$message.success('发布成功')
          this.getList()
        } else {
          this.$message.error(response.message || '发布失败')
        }
      } catch (error) {
        console.error('发布文章失败:', error)
        this.$message.error('发布失败')
      }
    },
    async handleUnpublish(row) {
      try {
        const response = await unpublishArticle(row.id)
        if (response.code === 200) {
          this.$message.success('下线成功')
          this.getList()
        } else {
          this.$message.error(response.message || '下线失败')
        }
      } catch (error) {
        console.error('下线文章失败:', error)
        this.$message.error('下线失败')
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除该文章吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteArticle(row.id)
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除文章失败:', error)
          this.$message.error('删除失败')
        }
      }).catch(() => {
      })
    },
    handleRowSelect(row, selected) {
      if (selected) {
        this.multipleSelection.push(row)
      } else {
        const index = this.multipleSelection.findIndex(item => item.id === row.id)
        if (index > -1) {
          this.multipleSelection.splice(index, 1)
        }
      }
    },
    async fetchTemplates() {
      try {
        const {data} = await getTemplateList()
        this.templateList = data.list
        console.log(data.list)
        // 创建模板映射
        this.templateMap = new Map(this.templateList.map(template => [template.id, template]))
      } catch (error) {
        console.error('获取模板列表失败:', error)
      }
    },
    async handleBatchDelete() {
      try {
        await this.$confirm('确认批量删除选中的文章吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const ids = this.multipleSelection.map(item => item.id)
        const response = await batchDeleteArticles(ids)
        
        if (response.code === 200) {
          this.$message.success('批量删除成功')
          this.getList()
        } else {
          this.$message.error(response.message || '���量删除失败')
        }
      } catch (error) {
        console.error('批量删除失败:', error)
      }
    },
    async handleBatchPublish() {
      try {
        await this.$confirm('确认批量发布选中的文章吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const ids = this.multipleSelection.map(item => item.id)
        const response = await batchPublishArticles(ids)
        
        if (response.code === 200) {
          this.$message.success('批量发布成功')
          this.getList()
        } else {
          this.$message.error(response.message || '批量发布失败')
        }
      } catch (error) {
        console.error('批量发布失败:', error)
      }
    },
    async handleStatusChange(row, value) {
      try {
        const action = value === 'published' ? publishArticle : unpublishArticle
        const response = await action(row.id)
        
        if (response.code === 200) {
          this.$message.success(value === 'published' ? '发布成功' : '下线成功')
          // 更新本地数据状态
          row.status = value
        } else {
          this.$message.error(response.message || '操作失败')
          // 恢复原状态
          row.status = value === 'published' ? 'draft' : 'published'
        }
      } catch (error) {
        console.error('状态更新失败:', error)
        this.$message.error('操作失败')
        // 恢复原状态
        row.status = value === 'published' ? 'draft' : 'published'
      }
    },
    renderTreeNode(h, { node, data }) {
      return h('span', {
        class: {
          'custom-tree-node': true,
          'is-current': this.currentCategory && this.currentCategory.id === data.id
        },
        style: {
          display: 'flex',
          alignItems: 'center',
          fontSize: '14px',
          padding: '8px',
          borderRadius: '4px',
          cursor: 'pointer',
          transition: 'all 0.3s'
        }
      }, [
        h('span', {
          style: {
            color: this.currentCategory && this.currentCategory.id === data.id ? '#fff' : 'var(--text-color)'
          }
        }, node.label)
      ])
    },
    handleSelectAll(val) {
      this.list.forEach(item => {
        if (!item.isSystem) {
          item.selected = val
        }
      })
      if (val) {
        this.multipleSelection = this.list.filter(item => !item.isSystem)
      } else {
        this.multipleSelection = []
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  height: 100%;
  padding: 20px;

  .el-container {
    height: 100%;
    background-color: var(--background-color-base);
  }
}

.el-aside {
  background-color: var(--background-color-base);
  border-right: 1px solid var(--border-color-base);
  margin-right: 20px;

  .category-tree {
    height: 100%;

    .tree-header {
      font-weight: bold;
    }

    :deep(.custom-tree-node) {
      &:hover {
        background-color: var(--background-color-light);
      }
      
      &.is-current {
        background-color: var(--primary-color);
        
        &:hover {
          background-color: var(--primary-color);
        }
      }
    }

    :deep(.el-tree-node__content) {
      height: auto;
      padding: 4px 8px;
    }
  }
}

.el-main {
  padding: 0;
  background-color: var(--background-color-base);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  .header-title {
    display: flex;
    align-items: center;

    h2 {
      margin: 0;
      color: var(--text-color);
    }

    .category-path {
      margin-left: 10px;
      color: var(--text-color-secondary);
      font-size: 14px;
    }
  }
}

.filter-container {
  padding-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.article-title {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;

  &:hover {
    text-decoration: underline;
  }
}

.link-type {
  text-decoration: none;
}

// 暗色主题适配
[data-theme='dark'] {
  .el-aside {
    border-right-color: var(--border-color-base);
  }

  .el-card {
    background-color: var(--background-color-base);
    border-color: var(--border-color-base);

    .el-card__header {
      border-bottom-color: var(--border-color-base);
    }
  }

  .article-title {
    color: var(--primary-color);
  }

  .el-table {
    background-color: var(--background-color-base);
    color: var(--text-color);

    th,
    td {
      background-color: var(--background-color-base);
      border-color: var(--border-color-base);
    }

    th {
      background-color: var(--background-color-light);
    }

    tr:hover > td {
      background-color: var(--background-color-light);
    }
  }
}

.preview-dialog {
  :deep(.el-dialog__body) {
    padding: 20px;
  }
}

.preview-content {
  .preview-title {
    margin: 0 0 20px;
    text-align: center;
    color: var(--text-color);
    font-size: 24px;
  }

  .preview-info {
    margin-bottom: 20px;
    padding: 10px;
    background-color: var(--background-color-light);
    border-radius: 4px;
    color: var(--text-color-secondary);
    font-size: 14px;

    span {
      margin-right: 20px;

      &:last-child {
        margin-right: 0;
      }
    }
  }

  .preview-body {
    line-height: 1.8;
    color: var(--text-color);

    :deep(img) {
      max-width: 100%;
      height: auto;
    }

    :deep(p) {
      margin: 1em 0;
    }

    :deep(h1),
    :deep(h2),
    :deep(h3),
    :deep(h4),
    :deep(h5),
    :deep(h6) {
      margin: 1.5em 0 1em;
      color: var(--text-color);
    }
  }
}

// 暗色主题适配
[data-theme='dark'] {
  .preview-dialog {
    :deep(.el-dialog) {
      background-color: var(--background-color-base);

      .el-dialog__header {
        border-bottom-color: var(--border-color-base);
      }
    }
  }
}
</style>
