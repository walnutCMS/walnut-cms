<template>
  <div class="home-page">
    <!-- Banner Section -->
    <section class="banner" :style="{ backgroundImage: `url(${config.bannerImage || ''})` }">
      <div class="container">
        <h1>{{ config.bannerTitle || '欢迎访问我们的网站' }}</h1>
        <p>{{ config.bannerSubtitle || '为您提供优质的产品和服务' }}</p>
        <el-button type="primary" size="large" @click="handleContact">联系我们</el-button>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features">
      <div class="container">
        <h2 class="section-title">我们的优势</h2>
        <div class="feature-grid">
          <div v-for="(feature, index) in features" :key="index" class="feature-item">
            <i :class="feature.icon"></i>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Products Section -->
    <section class="products">
      <div class="container">
        <h2 class="section-title">产品展示</h2>
        <div class="product-grid">
          <div v-for="(product, index) in products" :key="index" class="product-item">
            <div class="product-image">
              <img :src="product.image" :alt="product.name">
            </div>
            <h3>{{ product.name }}</h3>
            <p>{{ product.description }}</p>
            <el-button type="text" @click="handleProductDetail(product)">了解更多</el-button>
          </div>
        </div>
      </div>
    </section>

    <!-- About Section -->
    <section class="about">
      <div class="container">
        <div class="about-content">
          <div class="about-text">
            <h2 class="section-title">关于我们</h2>
            <p>{{ config.aboutText || '这里是关于我们的介绍文字...' }}</p>
            <el-button type="primary" plain @click="handleAbout">了解更多</el-button>
          </div>
          <div class="about-image">
            <img :src="config.aboutImage" alt="About Us" v-if="config.aboutImage">
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'CorporateHome',
  props: {
    config: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      features: [
        {
          icon: 'el-icon-medal',
          title: '品质保证',
          description: '严格的质量控制体系'
        },
        {
          icon: 'el-icon-service',
          title: '专业服务',
          description: '24小时客户服务支持'
        },
        {
          icon: 'el-icon-success',
          title: '技术创新',
          description: '持续的技术研发投入'
        },
        {
          icon: 'el-icon-connection',
          title: '全球网络',
          description: '完善的销售服务网络'
        }
      ],
      products: [
        {
          name: '产品一',
          description: '产品描述信息...',
          image: 'https://via.placeholder.com/300x200'
        },
        {
          name: '产品二',
          description: '产品描述信息...',
          image: 'https://via.placeholder.com/300x200'
        },
        {
          name: '产品三',
          description: '产品描述信息...',
          image: 'https://via.placeholder.com/300x200'
        }
      ]
    }
  },
  methods: {
    handleContact() {
      this.$router.push('/contact')
    },
    handleAbout() {
      this.$router.push('/about')
    },
    handleProductDetail(product) {
      this.$router.push('/product')
    }
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  .banner {
    height: 600px;
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    display: flex;
    align-items: center;
    color: #fff;
    text-align: center;
    position: relative;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0,0,0,0.5);
    }

    .container {
      position: relative;
      z-index: 1;
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
    }

    h1 {
      font-size: 3rem;
      margin-bottom: 1rem;
    }

    p {
      font-size: 1.5rem;
      margin-bottom: 2rem;
    }
  }

  .section-title {
    text-align: center;
    margin-bottom: 3rem;
    font-size: 2rem;
    color: #333;
  }

  .features {
    padding: 5rem 0;
    background: #f9f9f9;

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
    }

    .feature-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 2rem;
    }

    .feature-item {
      text-align: center;
      padding: 2rem;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 12px rgba(0,0,0,0.1);
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-5px);
      }

      i {
        font-size: 3rem;
        color: #1890ff;
        margin-bottom: 1rem;
      }

      h3 {
        margin-bottom: 1rem;
        color: #333;
      }

      p {
        color: #666;
        line-height: 1.6;
      }
    }
  }

  .products {
    padding: 5rem 0;

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
    }

    .product-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 2rem;
    }

    .product-item {
      text-align: center;
      
      .product-image {
        margin-bottom: 1rem;
        overflow: hidden;
        border-radius: 8px;

        img {
          width: 100%;
          height: 200px;
          object-fit: cover;
          transition: transform 0.3s ease;

          &:hover {
            transform: scale(1.1);
          }
        }
      }

      h3 {
        margin-bottom: 0.5rem;
        color: #333;
      }

      p {
        color: #666;
        margin-bottom: 1rem;
      }
    }
  }

  .about {
    padding: 5rem 0;
    background: #f9f9f9;

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
    }

    .about-content {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 4rem;
      align-items: center;
    }

    .about-text {
      .section-title {
        text-align: left;
        margin-bottom: 2rem;
      }

      p {
        color: #666;
        line-height: 1.8;
        margin-bottom: 2rem;
      }
    }

    .about-image {
      img {
        width: 100%;
        border-radius: 8px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.1);
      }
    }
  }
}

@media (max-width: 768px) {
  .home-page {
    .banner {
      height: 400px;

      h1 {
        font-size: 2rem;
      }

      p {
        font-size: 1.2rem;
      }
    }

    .about {
      .about-content {
        grid-template-columns: 1fr;
        gap: 2rem;
      }
    }
  }
}
</style> 