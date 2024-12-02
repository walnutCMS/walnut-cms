<template>
  <div class="tinymce-container">
    <editor
      v-model="myValue"
      :init="init"
      :disabled="disabled"
      @onChange="onChange"
    />
  </div>
</template>

<script>
import Editor from '@tinymce/tinymce-vue'
import { uploadFile } from '@/api/upload'

export default {
  name: 'Tinymce',
  components: { Editor },
  props: {
    value: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    height: {
      type: [Number, String],
      default: 360
    }
  },
  data() {
    return {
      init: {
        language: 'zh_CN',
        height: this.height,
        menubar: 'file edit view insert format tools table help',
        plugins: [
          'advlist autolink lists link image charmap print preview anchor',
          'searchreplace visualblocks code fullscreen',
          'insertdatetime media table paste code help wordcount'
        ],
        toolbar:
          'undo redo | formatselect | bold italic backcolor | \
          alignleft aligncenter alignright alignjustify | \
          bullist numlist outdent indent | removeformat | image | help',
        images_upload_handler: async (blobInfo, success, failure) => {
          try {
            const formData = new FormData()
            formData.append('file', blobInfo.blob())
            const response = await uploadFile(formData)
            if (response.code === 200) {
              success(response.data.viewUrl)
            } else {
              failure(response.message || '图片上传失败')
            }
          } catch (error) {
            console.error('图片上传失败:', error)
            failure('图片上传失败')
          }
        },
        images_upload_url: process.env.VUE_APP_BASE_API + '/api/file/upload',
        automatic_uploads: true,
        file_picker_types: 'image',
        images_reuse_filename: true,
        images_upload_credentials: true,
        images_upload_base_path: process.env.VUE_APP_BASE_API,
        images_upload_handler_fn: null,
        images_upload_max_size: 2 * 1024 * 1024, // 2MB
        images_upload_mime_types: 'image/jpeg,image/png,image/gif'
      },
      myValue: this.value
    }
  },
  watch: {
    value(val) {
      this.myValue = val
    }
  },
  methods: {
    onChange(e) {
      this.$emit('input', e.target.getContent())
    }
  }
}
</script>

<style lang="scss" scoped>
.tinymce-container {
  position: relative;
  line-height: normal;

  .mce-fullscreen {
    z-index: 10000;
  }
}

// 暗色主题适配
[data-theme='dark'] {
  :deep(.tox) {
    .tox-edit-area__iframe {
      background: var(--background-color-base) !important;
    }

    .tox-toolbar,
    .tox-toolbar__primary,
    .tox-toolbar__overflow {
      background: var(--background-color-base) !important;
    }

    .tox-tbtn {
      color: var(--text-color) !important;

      &:hover {
        background: var(--background-color-light) !important;
      }
    }
  }
}
</style>
