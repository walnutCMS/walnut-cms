<template>
  <div class="portal-article">
    <div class="article-container">
      <!-- 文章主体 -->
      <article class="article-main">
        <header class="article-header">
          <h1 class="article-title">{{ article.title }}</h1>
          <div class="article-meta">
            <span class="meta-item">
              <i class="el-icon-user"></i>
              {{ article.author }}
            </span>
            <span class="meta-item">
              <i class="el-icon-time"></i>
              {{ article.createTime }}
            </span>
            <span class="meta-item">
              <i class="el-icon-view"></i>
              {{ article.viewCount }} 阅读
            </span>
            <span class="meta-item">
              <i class="el-icon-chat-line-square"></i>
              {{ article.commentCount }} 评论
            </span>
          </div>
          <div class="article-tags">
            <el-tag
              v-for="tag in article.tags"
              :key="tag.id"
              size="small"
              effect="plain"
              @click="handleTagClick(tag)"
            >
              {{ tag.name }}
            </el-tag>
          </div>
        </header>

        <!-- 文章内容 -->
        <div class="article-content" v-html="article.content"></div>

        <!-- 文章底部 -->
        <footer class="article-footer">
          <div class="article-copyright">
            <p>本文由 {{ article.author }} 创作，采用 
              <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/" target="_blank">
                知识共享署名-非商业性使用-相同方式共享 4.0 国际许可协议
              </a>
              进行许可。
            </p>
          </div>

          <div class="article-actions">
            <el-button type="primary" @click="handleLike" :disabled="hasLiked">
              <i class="el-icon-star-off"></i>
              {{ hasLiked ? '已点赞' : '点赞' }}
              <span v-if="article.likeCount">({{ article.likeCount }})</span>
            </el-button>
            <el-button type="success" @click="handleShare">
              <i class="el-icon-share"></i>
              分享
            </el-button>
          </div>

          <!-- 上一篇/下一篇 -->
          <div class="article-navigation">
            <div v-if="prevArticle" class="prev-article" @click="handleArticleClick(prevArticle)">
              <span class="nav-label">上一篇</span>
              <h4>{{ prevArticle.title }}</h4>
            </div>
            <div v-if="nextArticle" class="next-article" @click="handleArticleClick(nextArticle)">
              <span class="nav-label">下一篇</span>
              <h4>{{ nextArticle.title }}</h4>
            </div>
          </div>
        </footer>

        <!-- 评论区 -->
        <div class="article-comments">
          <h3 class="comments-title">
            评论 
            <span v-if="article.commentCount">({{ article.commentCount }})</span>
          </h3>
          
          <!-- 评论输入框 -->
          <div class="comment-form">
            <el-input
              type="textarea"
              v-model="commentContent"
              :rows="4"
              placeholder="写下你的评论..."
            ></el-input>
            <div class="form-actions">
              <el-button type="primary" @click="handleComment">
                发表评论
              </el-button>
            </div>
          </div>

          <!-- 评论列表 -->
          <div class="comments-list">
            <div 
              v-for="comment in comments" 
              :key="comment.id"
              class="comment-item"
            >
              <div class="comment-avatar">
                <img :src="comment.avatar" :alt="comment.username">
              </div>
              <div class="comment-content">
                <div class="comment-header">
                  <span class="comment-author">{{ comment.username }}</span>
                  <span class="comment-time">{{ comment.createTime }}</span>
                </div>
                <div class="comment-text">{{ comment.content }}</div>
                <div class="comment-actions">
                  <span @click="handleReply(comment)">回复</span>
                  <span @click="handleLikeComment(comment)">
                    <i class="el-icon-star-off"></i>
                    {{ comment.likeCount || '点赞' }}
                  </span>
                </div>

                <!-- 回复列表 -->
                <div v-if="comment.replies && comment.replies.length" class="replies-list">
                  <div 
                    v-for="reply in comment.replies" 
                    :key="reply.id"
                    class="reply-item"
                  >
                    <div class="reply-avatar">
                      <img :src="reply.avatar" :alt="reply.username">
                    </div>
                    <div class="reply-content">
                      <div class="reply-header">
                        <span class="reply-author">{{ reply.username }}</span>
                        <span class="reply-time">{{ reply.createTime }}</span>
                      </div>
                      <div class="reply-text">
                        <template v-if="reply.replyTo">
                          回复 <span class="reply-to">@{{ reply.replyTo }}</span>：
                        </template>
                        {{ reply.content }}
                      </div>
                      <div class="reply-actions">
                        <span @click="handleReply(comment, reply)">回复</span>
                        <span @click="handleLikeReply(reply)">
                          <i class="el-icon-star-off"></i>
                          {{ reply.likeCount || '点赞' }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 加载更多评论 -->
          <div v-if="hasMoreComments" class="load-more">
            <el-button 
              type="text" 
              :loading="loadingComments"
              @click="loadMoreComments"
            >
              加载更多评论
            </el-button>
          </div>
        </div>
      </article>

      <!-- 右侧边栏 -->
      <aside class="article-sidebar">
        <!-- 作者信息 -->
        <div class="widget author-card">
          <div class="author-header">
            <img :src="article.authorAvatar" :alt="article.author" class="author-avatar">
            <h3 class="author-name">{{ article.author }}</h3>
            <p class="author-description">{{ article.authorDescription }}</p>
          </div>
          <div class="author-stats">
            <div class="stat-item">
              <div class="stat-value">{{ article.authorArticleCount }}</div>
              <div class="stat-label">文章</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ article.authorFollowers }}</div>
              <div class="stat-label">粉丝</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ article.authorLikes }}</div>
              <div class="stat-label">获赞</div>
            </div>
          </div>
          <div class="author-actions">
            <el-button type="primary" @click="handleFollow" :disabled="hasFollowed">
              {{ hasFollowed ? '已��注' : '关注作者' }}
            </el-button>
          </div>
        </div>

        <!-- 相关文章 -->
        <div class="widget related-articles">
          <h3 class="widget-title">相关文章</h3>
          <ul class="article-list">
            <li 
              v-for="item in relatedArticles" 
              :key="item.id"
              @click="handleArticleClick(item)"
            >
              <div class="article-image" :style="{ backgroundImage: `url(${item.cover})` }"></div>
              <div class="article-info">
                <h4>{{ item.title }}</h4>
                <div class="article-meta">
                  <span><i class="el-icon-view"></i> {{ item.viewCount }}</span>
                  <span><i class="el-icon-chat-line-square"></i> {{ item.commentCount }}</span>
                </div>
              </div>
            </li>
          </ul>
        </div>

        <!-- 热门标签 -->
        <div class="widget hot-tags">
          <h3 class="widget-title">热门标签</h3>
          <div class="tag-cloud">
            <el-tag
              v-for="tag in hotTags"
              :key="tag.id"
              :type="tag.type"
              effect="plain"
              class="tag-item"
              @click="handleTagClick(tag)"
            >
              {{ tag.name }}
            </el-tag>
          </div>
        </div>
      </aside>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PortalArticlePage',
  props: {
    pageData: {
      type: Object,
      default: () => ({})
    },
    isMobile: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      article: this.pageData.article || {},
      comments: this.pageData.comments || [],
      relatedArticles: this.pageData.relatedArticles || [],
      hotTags: this.pageData.hotTags || [],
      prevArticle: this.pageData.prevArticle || null,
      nextArticle: this.pageData.nextArticle || null,
      commentContent: '',
      hasLiked: false,
      hasFollowed: false,
      hasMoreComments: true,
      loadingComments: false,
      currentPage: 1,
      pageSize: 10
    }
  },
  methods: {
    handleTagClick(tag) {
      this.$emit('navigate', '/tag', { id: tag.id })
    },
    handleArticleClick(article) {
      this.$emit('navigate', '/article', { id: article.id })
    },
    handleLike() {
      if (!this.hasLiked) {
        this.hasLiked = true
        this.$emit('like', { id: this.article.id })
      }
    },
    handleShare() {
      this.$emit('share', { 
        id: this.article.id,
        title: this.article.title,
        summary: this.article.summary
      })
    },
    handleFollow() {
      if (!this.hasFollowed) {
        this.hasFollowed = true
        this.$emit('follow', { id: this.article.authorId })
      }
    },
    handleComment() {
      if (this.commentContent.trim()) {
        this.$emit('comment', {
          articleId: this.article.id,
          content: this.commentContent.trim()
        })
        this.commentContent = ''
      }
    },
    handleReply(comment, reply = null) {
      this.$emit('reply', {
        articleId: this.article.id,
        commentId: comment.id,
        replyId: reply ? reply.id : null,
        replyTo: reply ? reply.username : comment.username
      })
    },
    handleLikeComment(comment) {
      this.$emit('like-comment', { id: comment.id })
    },
    handleLikeReply(reply) {
      this.$emit('like-reply', { id: reply.id })
    },
    loadMoreComments() {
      if (!this.loadingComments && this.hasMoreComments) {
        this.loadingComments = true
        this.currentPage += 1
        this.$emit('load-more-comments', {
          articleId: this.article.id,
          page: this.currentPage,
          pageSize: this.pageSize
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-article {
  .article-container {
    display: grid;
    grid-template-columns: 1fr 300px;
    gap: 30px;
    
    // 文章主体
    .article-main {
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 12px rgba(0,0,0,0.1);
      padding: 30px;
      
      // 文章头部
      .article-header {
        margin-bottom: 30px;
        padding-bottom: 20px;
        border-bottom: 1px solid #eee;
        
        .article-title {
          font-size: 32px;
          line-height: 1.4;
          margin: 0 0 20px;
          color: var(--heading-color);
        }
        
        .article-meta {
          margin-bottom: 15px;
          
          .meta-item {
            color: #999;
            margin-right: 20px;
            font-size: 14px;
            
            i {
              margin-right: 5px;
            }
          }
        }
        
        .article-tags {
          .el-tag {
            margin-right: 10px;
            walnut: pointer;
          }
        }
      }
      
      // 文章内容
      .article-content {
        font-size: 16px;
        line-height: 1.8;
        color: var(--text-color);
        
        :deep(img) {
          max-width: 100%;
          height: auto;
          margin: 20px 0;
        }
        
        :deep(h1),
        :deep(h2),
        :deep(h3),
        :deep(h4),
        :deep(h5),
        :deep(h6) {
          margin: 1.5em 0 0.5em;
          color: var(--heading-color);
        }
        
        :deep(p) {
          margin: 1em 0;
        }
        
        :deep(blockquote) {
          margin: 1em 0;
          padding: 15px 20px;
          background: #f8f9fa;
          border-left: 4px solid var(--primary-color);
          color: #666;
        }
        
        :deep(code) {
          background: #f8f9fa;
          padding: 2px 5px;
          border-radius: 3px;
          color: #e83e8c;
        }
        
        :deep(pre) {
          background: #282c34;
          color: #abb2bf;
          padding: 15px;
          border-radius: 5px;
          overflow-x: auto;
          
          code {
            background: none;
            color: inherit;
            padding: 0;
          }
        }
      }
      
      // 文章底部
      .article-footer {
        margin-top: 40px;
        padding-top: 20px;
        border-top: 1px solid #eee;
        
        .article-copyright {
          background: #f8f9fa;
          padding: 15px;
          border-radius: 4px;
          margin-bottom: 20px;
          font-size: 14px;
          color: #666;
          
          a {
            color: var(--primary-color);
            text-decoration: none;
            
            &:hover {
              text-decoration: underline;
            }
          }
        }
        
        .article-actions {
          display: flex;
          justify-content: center;
          gap: 20px;
          margin-bottom: 30px;
        }
        
        .article-navigation {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 20px;
          margin-top: 30px;
          
          .prev-article,
          .next-article {
            padding: 15px;
            background: #f8f9fa;
            border-radius: 4px;
            walnut: pointer;
            transition: transform 0.3s;
            
            &:hover {
              transform: translateY(-2px);
            }
            
            .nav-label {
              color: #999;
              font-size: 14px;
            }
            
            h4 {
              margin: 5px 0 0;
              font-size: 16px;
              color: var(--heading-color);
            }
          }
          
          .next-article {
            text-align: right;
          }
        }
      }
      
      // 评论区
      .article-comments {
        margin-top: 40px;
        
        .comments-title {
          font-size: 24px;
          margin: 0 0 20px;
          
          span {
            color: #999;
            font-size: 16px;
          }
        }
        
        .comment-form {
          margin-bottom: 30px;
          
          .form-actions {
            margin-top: 15px;
            text-align: right;
          }
        }
        
        .comments-list {
          .comment-item {
            display: grid;
            grid-template-columns: 50px 1fr;
            gap: 15px;
            margin-bottom: 20px;
            padding-bottom: 20px;
            border-bottom: 1px solid #eee;
            
            &:last-child {
              border-bottom: none;
            }
            
            .comment-avatar {
              img {
                width: 50px;
                height: 50px;
                border-radius: 50%;
                object-fit: cover;
              }
            }
            
            .comment-content {
              .comment-header {
                margin-bottom: 10px;
                
                .comment-author {
                  font-weight: bold;
                  margin-right: 10px;
                }
                
                .comment-time {
                  color: #999;
                  font-size: 14px;
                }
              }
              
              .comment-text {
                font-size: 14px;
                line-height: 1.6;
                margin-bottom: 10px;
              }
              
              .comment-actions {
                span {
                  color: #999;
                  font-size: 14px;
                  margin-right: 15px;
                  walnut: pointer;
                  
                  &:hover {
                    color: var(--primary-color);
                  }
                  
                  i {
                    margin-right: 5px;
                  }
                }
              }
              
              .replies-list {
                margin-top: 15px;
                padding-left: 20px;
                border-left: 2px solid #eee;
                
                .reply-item {
                  display: grid;
                  grid-template-columns: 40px 1fr;
                  gap: 10px;
                  margin-bottom: 15px;
                  
                  &:last-child {
                    margin-bottom: 0;
                  }
                  
                  .reply-avatar {
                    img {
                      width: 40px;
                      height: 40px;
                      border-radius: 50%;
                      object-fit: cover;
                    }
                  }
                  
                  .reply-content {
                    .reply-header {
                      margin-bottom: 5px;
                      
                      .reply-author {
                        font-weight: bold;
                        margin-right: 10px;
                      }
                      
                      .reply-time {
                        color: #999;
                        font-size: 12px;
                      }
                    }
                    
                    .reply-text {
                      font-size: 14px;
                      line-height: 1.6;
                      margin-bottom: 5px;
                      
                      .reply-to {
                        color: var(--primary-color);
                      }
                    }
                    
                    .reply-actions {
                      span {
                        color: #999;
                        font-size: 12px;
                        margin-right: 15px;
                        walnut: pointer;
                        
                        &:hover {
                          color: var(--primary-color);
                        }
                        
                        i {
                          margin-right: 3px;
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        
        .load-more {
          text-align: center;
          margin-top: 20px;
        }
      }
    }
    
    // 侧边栏
    .article-sidebar {
      .widget {
        background: #fff;
        border-radius: 8px;
        box-shadow: 0 2px 12px rgba(0,0,0,0.1);
        padding: 20px;
        margin-bottom: 30px;
        
        .widget-title {
          font-size: 18px;
          margin: 0 0 20px;
          padding-bottom: 10px;
          border-bottom: 2px solid var(--primary-color);
        }
        
        &.author-card {
          text-align: center;
          
          .author-header {
            margin-bottom: 20px;
            
            .author-avatar {
              width: 80px;
              height: 80px;
              border-radius: 50%;
              margin-bottom: 15px;
            }
            
            .author-name {
              font-size: 18px;
              margin: 0 0 10px;
            }
            
            .author-description {
              color: #666;
              font-size: 14px;
              margin: 0;
            }
          }
          
          .author-stats {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 10px;
            margin-bottom: 20px;
            
            .stat-item {
              .stat-value {
                font-size: 20px;
                font-weight: bold;
                color: var(--primary-color);
              }
              
              .stat-label {
                font-size: 14px;
                color: #666;
              }
            }
          }
        }
        
        &.related-articles {
          .article-list {
            list-style: none;
            padding: 0;
            margin: 0;
            
            li {
              display: grid;
              grid-template-columns: 80px 1fr;
              gap: 15px;
              padding: 15px 0;
              border-bottom: 1px solid #eee;
              walnut: pointer;
              
              &:last-child {
                border-bottom: none;
                padding-bottom: 0;
              }
              
              &:first-child {
                padding-top: 0;
              }
              
              .article-image {
                height: 60px;
                background-size: cover;
                background-position: center;
                border-radius: 4px;
              }
              
              .article-info {
                h4 {
                  margin: 0 0 5px;
                  font-size: 14px;
                  line-height: 1.4;
                }
                
                .article-meta {
                  color: #999;
                  font-size: 12px;
                  
                  span {
                    margin-right: 10px;
                    
                    i {
                      margin-right: 3px;
                    }
                  }
                }
              }
            }
          }
        }
        
        &.hot-tags {
          .tag-cloud {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            
            .tag-item {
              walnut: pointer;
            }
          }
        }
      }
    }
  }
}

// 响应式样式
@media screen and (max-width: 768px) {
  .portal-article {
    .article-container {
      grid-template-columns: 1fr;
      
      .article-main {
        padding: 20px;
        
        .article-header {
          .article-title {
            font-size: 24px;
          }
        }
        
        .article-footer {
          .article-navigation {
            grid-template-columns: 1fr;
            
            .next-article {
              text-align: left;
            }
          }
        }
      }
    }
  }
}
</style> 