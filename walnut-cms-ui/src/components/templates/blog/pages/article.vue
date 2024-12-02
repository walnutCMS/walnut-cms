<template>
  <div class="blog-article">
    <!-- 文章头部 -->
    <header class="article-header">
      <h1 class="article-title">{{ article.title }}</h1>
      <div class="article-meta">
        <div class="meta-left">
          <span class="author">
            <img :src="article.author.avatar" :alt="article.author.name" class="author-avatar">
            {{ article.author.name }}
          </span>
          <span class="date">{{ article.date }}</span>
          <span class="category">{{ article.category }}</span>
        </div>
        <div class="meta-right">
          <span class="views">
            <i class="el-icon-view"></i>
            {{ article.views }}
          </span>
          <span class="likes">
            <i class="el-icon-star-off"></i>
            {{ article.likes }}
          </span>
          <span class="comments">
            <i class="el-icon-chat-dot-round"></i>
            {{ article.comments }}
          </span>
        </div>
      </div>
    </header>

    <!-- 文章封面 -->
    <div v-if="article.cover" class="article-cover">
      <img :src="article.cover" :alt="article.title">
    </div>

    <!-- 文章内容 -->
    <div class="article-content" v-html="article.content"></div>

    <!-- 文章标签 -->
    <div v-if="article.tags && article.tags.length" class="article-tags">
      <i class="el-icon-collection-tag"></i>
      <el-tag
        v-for="tag in article.tags"
        :key="tag"
        size="small"
        effect="plain"
        class="tag"
      >
        {{ tag }}
      </el-tag>
    </div>

    <!-- 分享按钮 -->
    <div class="article-share">
      <span class="share-title">分享到：</span>
      <div class="share-buttons">
        <button class="share-button wechat" @click="handleShare('wechat')">
          <i class="el-icon-chat-dot-square"></i>
        </button>
        <button class="share-button weibo" @click="handleShare('weibo')">
          <i class="el-icon-share"></i>
        </button>
        <button class="share-button link" @click="handleShare('link')">
          <i class="el-icon-link"></i>
        </button>
      </div>
    </div>

    <!-- 上一篇/下一篇 -->
    <div class="article-navigation">
      <div v-if="article.prev" class="prev" @click="handleNavigate(article.prev)">
        <span class="nav-label">上一篇</span>
        <h3>{{ article.prev.title }}</h3>
      </div>
      <div v-if="article.next" class="next" @click="handleNavigate(article.next)">
        <span class="nav-label">下一篇</span>
        <h3>{{ article.next.title }}</h3>
      </div>
    </div>

    <!-- 相关文章 -->
    <div v-if="article.related && article.related.length" class="related-articles">
      <h2 class="section-title">相关文章</h2>
      <div class="related-grid">
        <div
          v-for="item in article.related"
          :key="item.id"
          class="related-item"
          @click="handleNavigate(item)"
        >
          <div class="related-image">
            <img :src="item.image" :alt="item.title">
          </div>
          <div class="related-content">
            <h3>{{ item.title }}</h3>
            <p>{{ item.summary }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 评论区 -->
    <div class="article-comments">
      <h2 class="section-title">评论 ({{ article.comments }})</h2>
      <div class="comment-form">
        <el-input
          type="textarea"
          v-model="commentContent"
          :rows="4"
          placeholder="写下你的评论..."
        />
        <div class="form-footer">
          <el-button type="primary" @click="handleComment">发表评论</el-button>
        </div>
      </div>
      <div class="comment-list">
        <div v-for="comment in article.commentList" :key="comment.id" class="comment-item">
          <div class="comment-avatar">
            <img :src="comment.avatar" :alt="comment.author">
          </div>
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-author">{{ comment.author }}</span>
              <span class="comment-date">{{ comment.date }}</span>
            </div>
            <div class="comment-text">{{ comment.content }}</div>
            <div class="comment-actions">
              <button @click="handleReply(comment)">回复</button>
              <button @click="handleLike(comment)">
                <i class="el-icon-star-off"></i>
                {{ comment.likes }}
              </button>
            </div>
            <!-- 回复列表 -->
            <div v-if="comment.replies && comment.replies.length" class="reply-list">
              <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                <div class="reply-avatar">
                  <img :src="reply.avatar" :alt="reply.author">
                </div>
                <div class="reply-content">
                  <div class="reply-header">
                    <span class="reply-author">{{ reply.author }}</span>
                    <span class="reply-date">{{ reply.date }}</span>
                  </div>
                  <div class="reply-text">{{ reply.content }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BlogArticle',
  props: {
    article: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      commentContent: ''
    }
  },
  methods: {
    handleShare(platform) {
      // 实现分享功能
      this.$emit('share', { platform, article: this.article })
    },
    handleNavigate(article) {
      this.$emit('navigate', `/article/${article.id}`)
    },
    handleComment() {
      if (!this.commentContent.trim()) {
        this.$message.warning('请输入评论内容')
        return
      }
      this.$emit('comment', {
        articleId: this.article.id,
        content: this.commentContent
      })
      this.commentContent = ''
    },
    handleReply(comment) {
      this.$emit('reply', {
        articleId: this.article.id,
        commentId: comment.id
      })
    },
    handleLike(comment) {
      this.$emit('like', {
        articleId: this.article.id,
        commentId: comment.id
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.blog-article {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;

  .article-header {
    margin-bottom: 30px;
    text-align: center;

    .article-title {
      margin: 0 0 20px;
      font-size: 36px;
      line-height: 1.4;
    }

    .article-meta {
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: #999;
      font-size: 14px;

      .meta-left,
      .meta-right {
        display: flex;
        align-items: center;
        gap: 20px;
      }

      .author {
        display: flex;
        align-items: center;

        .author-avatar {
          width: 24px;
          height: 24px;
          border-radius: 50%;
          margin-right: 8px;
        }
      }

      i {
        margin-right: 5px;
      }
    }
  }

  .article-cover {
    margin: -20px -20px 30px;
    height: 400px;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .article-content {
    line-height: 1.8;
    color: #333;
    margin-bottom: 30px;

    ::v-deep {
      img {
        max-width: 100%;
        height: auto;
        margin: 20px 0;
      }

      h2 {
        margin: 40px 0 20px;
        font-size: 24px;
      }

      h3 {
        margin: 30px 0 15px;
        font-size: 20px;
      }

      p {
        margin: 20px 0;
      }

      blockquote {
        margin: 20px 0;
        padding: 15px 20px;
        background: #f8f9fa;
        border-left: 4px solid var(--primary-color);
        color: #666;
      }

      code {
        background: #f8f9fa;
        padding: 2px 5px;
        border-radius: 3px;
      }

      pre {
        background: #f8f9fa;
        padding: 15px;
        border-radius: 4px;
        overflow-x: auto;

        code {
          background: none;
          padding: 0;
        }
      }
    }
  }

  .article-tags {
    display: flex;
    align-items: center;
    margin-bottom: 30px;

    i {
      margin-right: 10px;
      color: #999;
    }

    .tag {
      margin-right: 10px;
    }
  }

  .article-share {
    display: flex;
    align-items: center;
    margin-bottom: 30px;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 4px;

    .share-title {
      margin-right: 15px;
      color: #666;
    }

    .share-buttons {
      display: flex;
      gap: 10px;
    }

    .share-button {
      width: 36px;
      height: 36px;
      border: none;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      walnut: pointer;
      transition: all 0.3s;

      i {
        font-size: 20px;
        color: #fff;
      }

      &.wechat {
        background-color: #07c160;
        &:hover { background-color: darken(#07c160, 10%); }
      }

      &.weibo {
        background-color: #e6162d;
        &:hover { background-color: darken(#e6162d, 10%); }
      }

      &.link {
        background-color: #666;
        &:hover { background-color: darken(#666, 10%); }
      }
    }
  }

  .article-navigation {
    display: flex;
    justify-content: space-between;
    margin-bottom: 40px;
    padding: 20px 0;
    border-top: 1px solid #eee;
    border-bottom: 1px solid #eee;

    .prev,
    .next {
      flex: 1;
      walnut: pointer;
      transition: all 0.3s;

      &:hover {
        color: var(--primary-color);
      }
    }

    .next {
      text-align: right;
    }

    .nav-label {
      display: block;
      color: #999;
      font-size: 14px;
      margin-bottom: 5px;
    }

    h3 {
      margin: 0;
      font-size: 16px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .related-articles {
    margin-bottom: 40px;

    .related-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 20px;
    }

    .related-item {
      walnut: pointer;
      transition: transform 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .related-image {
        height: 150px;
        margin-bottom: 10px;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
          border-radius: 4px;
        }
      }

      h3 {
        margin: 0 0 5px;
        font-size: 16px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      p {
        margin: 0;
        color: #666;
        font-size: 14px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }

  .article-comments {
    .comment-form {
      margin-bottom: 30px;

      .form-footer {
        margin-top: 15px;
        text-align: right;
      }
    }

    .comment-item {
      display: flex;
      margin-bottom: 20px;
      padding-bottom: 20px;
      border-bottom: 1px solid #eee;

      &:last-child {
        margin-bottom: 0;
        padding-bottom: 0;
        border-bottom: none;
      }
    }

    .comment-avatar {
      width: 40px;
      height: 40px;
      margin-right: 15px;

      img {
        width: 100%;
        height: 100%;
        border-radius: 50%;
      }
    }

    .comment-content {
      flex: 1;
      min-width: 0;
    }

    .comment-header {
      margin-bottom: 5px;

      .comment-author {
        font-weight: bold;
        margin-right: 10px;
      }

      .comment-date {
        color: #999;
        font-size: 14px;
      }
    }

    .comment-text {
      margin-bottom: 10px;
      line-height: 1.6;
    }

    .comment-actions {
      display: flex;
      gap: 15px;

      button {
        background: none;
        border: none;
        color: #999;
        walnut: pointer;
        font-size: 14px;
        padding: 0;
        display: flex;
        align-items: center;

        i {
          margin-right: 5px;
        }

        &:hover {
          color: var(--primary-color);
        }
      }
    }

    .reply-list {
      margin-top: 15px;
      padding-left: 55px;
    }

    .reply-item {
      display: flex;
      margin-bottom: 15px;

      &:last-child {
        margin-bottom: 0;
      }
    }

    .reply-avatar {
      width: 32px;
      height: 32px;
      margin-right: 10px;

      img {
        width: 100%;
        height: 100%;
        border-radius: 50%;
      }
    }

    .reply-content {
      flex: 1;
      min-width: 0;
    }

    .reply-header {
      margin-bottom: 5px;

      .reply-author {
        font-weight: bold;
        margin-right: 10px;
      }

      .reply-date {
        color: #999;
        font-size: 12px;
      }
    }

    .reply-text {
      font-size: 14px;
      line-height: 1.6;
    }
  }
}

[data-theme='dark'] {
  .blog-article {
    .article-content {
      color: var(--text-color);

      ::v-deep {
        blockquote {
          background-color: var(--background-color-light);
          color: var(--text-color-secondary);
        }

        code,
        pre {
          background-color: var(--background-color-light);
        }
      }
    }

    .article-share {
      background-color: var(--background-color-light);

      .share-title {
        color: var(--text-color-secondary);
      }
    }

    .article-navigation {
      border-color: var(--border-color-base);
    }

    .related-articles {
      .related-item {
        p {
          color: var(--text-color-secondary);
        }
      }
    }

    .article-comments {
      .comment-item {
        border-color: var(--border-color-base);
      }

      .comment-actions button {
        color: var(--text-color-secondary);

        &:hover {
          color: var(--primary-color);
        }
      }
    }
  }
}
</style> 