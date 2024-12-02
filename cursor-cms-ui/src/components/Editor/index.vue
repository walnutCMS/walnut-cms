<template>
  <div class="editor-container">
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editor"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    />
    <Editor
      :style="{height: `${height}px`, 'overflow-y': 'hidden'}"
      v-model="content"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="handleCreated"
      @onChange="handleChange"
    />
  </div>
</template>

<script>
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { uploadFile } from '@/api/upload'

export default {
  name: 'WangEditor',
  components: { Editor, Toolbar },
  props: {
    value: {
      type: String,
      default: ''
    },
    height: {
      type: [String, Number],
      default: 400
    }
  },
  data() {
    return {
      editor: null,
      content: '',
      toolbarConfig: {
        excludeKeys: []
      },
      editorConfig: {
        placeholder: '请输入内容...',
        MENU_CONF: {
          uploadImage: {
            server: '/api/file/upload',
            fieldName: 'file',
            headers: {
              Authorization: 'Bearer ' + localStorage.getItem('accessToken')
            },
            maxFileSize: 2 * 1024 * 1024, // 2MB
            maxNumberOfFiles: 10,
            allowedFileTypes: ['image/*'],
            // 自定义上传
            customUpload: async (file, insertFn) => {
              try {
                const formData = new FormData()
                formData.append('file', file)
                const res = await uploadFile(formData)
                insertFn(res.data.viewUrl)
              } catch (error) {
                console.error('图片上传失败:', error)
              }
            }
          },
          uploadVideo: {
            server: '/api/file/upload',
            fieldName: 'file',
            headers: {
              Authorization: 'Bearer ' + localStorage.getItem('accessToken')
            },
            maxFileSize: 100 * 1024 * 1024, // 100MB
            maxNumberOfFiles: 5,
            allowedFileTypes: ['video/*'],
            // 自定义上传
            customUpload: async (file, insertFn) => {
              try {
                const formData = new FormData()
                formData.append('file', file)
                const res = await uploadFile(formData)
                insertFn(res.data.viewUrl)
              } catch (error) {
                console.error('视频上传失败:', error)
              }
            }
          }
        }
      },
      mode: 'default'
    }
  },
  watch: {
    value: {
      handler(val) {
        if (val !== this.content) {
          this.content = val
        }
      },
      immediate: true
    }
  },
  mounted() {
    // 模拟 ajax 异步获取内容
    setTimeout(() => {
      this.content = this.value
    }, 100)
  },
  beforeDestroy() {
    const editor = this.editor
    if (editor == null) return
    editor.destroy()
  },
  methods: {
    handleCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
    },
    handleChange(editor) {
      this.$emit('input', this.content)
    }
  }
}
</script>

<style lang="scss" scoped>
.editor-container {
  border: 1px solid var(--border-color-base);
  z-index: 100;
}

// 暗色主题适配
[data-theme='dark'] {
  :deep(.w-e-toolbar) {
    background-color: var(--background-color-base) !important;
    border-bottom-color: var(--border-color-base) !important;

    .w-e-bar-item button {
      color: var(--text-color) !important;
      &:hover {
        background-color: var(--background-color-light) !important;
      }
    }

    .w-e-bar-divider {
      background-color: var(--border-color-base) !important;
    }
  }

  :deep(.w-e-text-container) {
    background-color: var(--background-color-base) !important;

    .w-e-text {
      background-color: var(--background-color-base) !important;
      color: var(--text-color) !important;

      &[data-placeholder]::before {
        color: var(--text-color-secondary) !important;
      }
    }
  }

  :deep(.w-e-panel) {
    background-color: var(--background-color-base) !important;
    border-color: var(--border-color-base) !important;

    .w-e-panel-tab-title {
      color: var(--text-color) !important;
    }

    input {
      background-color: var(--background-color-light) !important;
      border-color: var(--border-color-base) !important;
      color: var(--text-color) !important;
    }

    button {
      background-color: var(--primary-color) !important;
      border-color: var(--primary-color) !important;
      color: #fff !important;
    }
  }
}
</style>
