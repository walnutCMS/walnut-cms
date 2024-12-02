<template>
  <div class="article-preview">
    <template v-if="article">
      <div class="default-preview">
        <h1 class="title">{{ article.title }}</h1>
        <div class="meta">
          <span class="category" v-if="article.category">
            <i class="el-icon-folder"></i>
            {{ article.category }}
          </span>
          <span class="tags" v-if="article.tags && article.tags.length">
            <i class="el-icon-collection-tag"></i>
            <el-tag
              v-for="tag in article.tags"
              :key="tag"
              size="small"
              type="info"
              effect="plain"
            >{{ tag }}</el-tag>
          </span>
          <span class="time">
            <i class="el-icon-time"></i>
            {{ article.publishTime | formatDate }}
          </span>
        </div>
        <div class="content" v-html="article.content"></div>
      </div>
    </template>
    
    <div v-else class="loading">
      <el-skeleton :rows="10" animated />
    </div>
  </div>
</template>

<script>
import { formatDate } from '@/utils'

export default {
  name: 'ArticlePreview',
  data() {
    return {
      article: null
    }
  },
  filters: {
    formatDate(time) {
      return formatDate(time)
    }
  },
  created() {
    // 从 localStorage 获取预览数据
    const previewData = localStorage.getItem('articlePreview')
    if (previewData) {
      this.article = JSON.parse(previewData)
    }
  }
}
</script>

<style lang="scss" scoped>
.article-preview {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  
  .loading {
    padding: 40px;
  }
  
  .default-preview {
    background: #fff;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
    
    .title {
      font-size: 28px;
      font-weight: 600;
      color: #2c3e50;
      margin-bottom: 20px;
      text-align: center;
    }
    
    .meta {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 20px;
      margin-bottom: 30px;
      color: #606266;
      font-size: 14px;
      
      .category,
      .time,
      .tags {
        display: flex;
        align-items: center;
        gap: 5px;
        
        i {
          font-size: 16px;
        }
      }
      
      .tags {
        .el-tag {
          margin-right: 5px;
          
          &:last-child {
            margin-right: 0;
          }
        }
      }
    }
    
    .content {
      line-height: 1.8;
      color: #2c3e50;
      
      ::v-deep {
        img {
          max-width: 100%;
          height: auto;
          margin: 10px 0;
        }
        
        p {
          margin: 16px 0;
        }
        
        h1, h2, h3, h4, h5, h6 {
          margin: 24px 0 16px;
          font-weight: 600;
          line-height: 1.25;
        }
        
        blockquote {
          margin: 16px 0;
          padding: 0 16px;
          color: #6a737d;
          border-left: 4px solid #dfe2e5;
        }
        
        pre {
          padding: 16px;
          overflow: auto;
          font-size: 85%;
          line-height: 1.45;
          background-color: #f6f8fa;
          border-radius: 6px;
        }
        
        code {
          padding: 0.2em 0.4em;
          margin: 0;
          font-size: 85%;
          background-color: rgba(27,31,35,0.05);
          border-radius: 6px;
        }
        
        table {
          display: block;
          width: 100%;
          overflow: auto;
          margin: 16px 0;
          border-spacing: 0;
          border-collapse: collapse;
          
          th, td {
            padding: 6px 13px;
            border: 1px solid #dfe2e5;
          }
          
          tr {
            background-color: #fff;
            border-top: 1px solid #c6cbd1;
            
            &:nth-child(2n) {
              background-color: #f6f8fa;
            }
          }
        }
      }
    }
  }
}

// 暗色主题适配
[data-theme='dark'] {
  .article-preview {
    .default-preview {
      background-color: var(--background-color-base);
      box-shadow: 0 2px 12px 0 rgba(0,0,0,0.3);
      
      .title {
        color: var(--color-text-primary);
      }
      
      .meta {
        color: var(--color-text-secondary);
      }
      
      .content {
        color: var(--color-text-primary);
        
        ::v-deep {
          blockquote {
            color: var(--color-text-secondary);
            border-left-color: var(--border-color-base);
          }
          
          pre {
            background-color: var(--background-color-base);
          }
          
          code {
            background-color: rgba(255,255,255,0.1);
          }
          
          table {
            th, td {
              border-color: var(--border-color-base);
            }
            
            tr {
              background-color: var(--background-color-base);
              border-top-color: var(--border-color-base);
              
              &:nth-child(2n) {
                background-color: var(--background-color-overlay);
              }
            }
          }
        }
      }
    }
  }
}
</style> 