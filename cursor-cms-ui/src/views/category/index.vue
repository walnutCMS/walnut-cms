<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-plus"
          @click="handleCreate(null)"
      >
        添加根栏目
      </el-button>
    </div>

    <el-table
        v-loading="loading"
        :data="list"
        row-key="id"
        border
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="name" label="栏目名称" min-width="200">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="文章数量" width="120" align="center">
        <template slot-scope="scope">
          {{ scope.row.articleCount || 0 }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180" align="center">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300" align="center">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleCreate(row)">
            添加子栏目
          </el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
              type="danger"
              size="mini"
              @click="handleDelete(row)"
              :disabled="row.children && row.children.length > 0"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
          ref="dataForm"
          :rules="rules"
          :model="temp"
          label-position="left"
          label-width="80px"
          style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="栏目名称" prop="name">
          <el-input v-model="temp.name"/>
        </el-form-item>
        <el-form-item label="父栏目" v-if="temp.parentId !== null">
          <el-input :value="parentName" disabled/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {createCategory, deleteCategory, getCategoryList, updateCategory} from '@/api/category'

export default {
  name: 'Category',
  data() {
    return {
      loading: false,
      list: [],
      temp: {
        id: undefined,
        name: '',
        parentId: null
      },
      parentName: '',
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑栏目',
        create: '新增栏目'
      },
      rules: {
        name: [{required: true, message: '栏目名称必填', trigger: 'blur'}]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const res = await getCategoryList()
        if (res.code === 200) {
          this.list = res.data || []
        } else {
          this.$message.error(res.message || '获取栏目列表失败')
        }
      } catch (error) {
        console.error('获取栏目列表失败:', error)
        this.$message.error('获取栏目列表失败')
      } finally {
        this.loading = false
      }
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        parentId: null
      }
      this.parentName = ''
    },
    handleCreate(row) {
      this.resetTemp()
      if (row) {
        this.temp.parentId = row.id
        this.parentName = row.name
      }
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createCategory(this.temp).then(() => {
            this.dialogFormVisible = false
            this.getList()
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      if (row.parentId) {
        // 找到父节点名称
        const findParent = (list, parentId) => {
          for (const item of list) {
            if (item.id === parentId) {
              return item.name
            }
            if (item.children) {
              const name = findParent(item.children, parentId)
              if (name) return name
            }
          }
          return null
        }
        this.parentName = findParent(this.list, row.parentId)
      }
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateCategory(tempData.id, tempData).then(() => {
            this.dialogFormVisible = false
            this.getList()
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row) {
      if (row.children && row.children.length > 0) {
        this.$message.warning('请先删除子栏目')
        return
      }
      this.$confirm('确认删除该栏目吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCategory(row.id).then(() => {
          this.$notify({
            title: '成功',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-container {
  padding-bottom: 10px;

  .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
  }
}
</style>
