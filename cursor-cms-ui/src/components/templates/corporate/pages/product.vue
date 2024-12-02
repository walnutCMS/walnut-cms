<template>
  <div class="product-page">
    <!-- Hero Section -->
    <section class="hero" :style="{ backgroundImage: `url(${config.productHeroImage || ''})` }">
      <div class="container">
        <h1>{{ config.productTitle || '产品中心' }}</h1>
        <p>{{ config.productSubtitle || '为您提供优质的产品解决方案' }}</p>
      </div>
    </section>

    <!-- Product Categories -->
    <section class="categories">
      <div class="container">
        <el-tabs v-model="activeCategory" @tab-click="handleCategoryChange">
          <el-tab-pane
            v-for="category in categories"
            :key="category.id"
            :label="category.name"
            :name="category.id"
          >
            <div class="product-grid">
              <div 
                v-for="product in getProductsByCategory(category.id)"
                :key="product.id"
                class="product-card"
                @click="showProductDetail(product)"
              >
                <div class="product-image">
                  <img :src="product.image" :alt="product.name">
                </div>
                <div class="product-info">
                  <h3>{{ product.name }}</h3>
                  <p>{{ product.description }}</p>
                  <el-button type="text">了解更多</el-button>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </section>

    <!-- Product Detail Dialog -->
    <el-dialog
      :title="selectedProduct ? selectedProduct.name : ''"
      :visible.sync="dialogVisible"
      width="70%"
      :fullscreen="isMobile"
    >
      <div v-if="selectedProduct" class="product-detail">
        <div class="product-gallery">
          <el-carousel height="400px">
            <el-carousel-item v-for="(image, index) in selectedProduct.gallery" :key="index">
              <img :src="image" :alt="selectedProduct.name">
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="product-content">
          <div class="product-description">
            <h2>产品描述</h2>
            <div v-html="selectedProduct.content"></div>
          </div>
          <div class="product-specs" v-if="selectedProduct.specifications">
            <h2>产品规格</h2>
            <el-table :data="selectedProduct.specifications" stripe>
              <el-table-column prop="name" label="参数名称"></el-table-column>
              <el-table-column prop="value" label="参数值"></el-table-column>
            </el-table>
          </div>
          <div class="product-features" v-if="selectedProduct.features">
            <h2>产品特点</h2>
            <ul>
              <li v-for="(feature, index) in selectedProduct.features" :key="index">
                {{ feature }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'CorporateProduct',
  props: {
    config: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeCategory: '1',
      dialogVisible: false,
      selectedProduct: null,
      categories: [
        {
          id: '1',
          name: '全部产品'
        },
        {
          id: '2',
          name: '解决方案'
        },
        {
          id: '3',
          name: '服务支持'
        }
      ],
      products: [
        {
          id: '1',
          categoryId: '2',
          name: '产品名称1',
          description: '产品简短描述...',
          image: 'https://via.placeholder.com/300x200',
          gallery: [
            'https://via.placeholder.com/800x400',
            'https://via.placeholder.com/800x400',
            'https://via.placeholder.com/800x400'
          ],
          content: '<p>详细的产品描述内容...</p>',
          specifications: [
            { name: '规格1', value: '值1' },
            { name: '规格2', value: '值2' }
          ],
          features: [
            '特点1',
            '特点2',
            '特点3'
          ]
        },
        {
          id: '2',
          categoryId: '2',
          name: '产品名称2',
          description: '产品简短描述...',
          image: 'https://via.placeholder.com/300x200',
          gallery: [
            'https://via.placeholder.com/800x400',
            'https://via.placeholder.com/800x400'
          ],
          content: '<p>详细的产品描述内容...</p>',
          specifications: [
            { name: '规格1', value: '值1' },
            { name: '规格2', value: '值2' }
          ],
          features: [
            '特点1',
            '特点2'
          ]
        }
      ]
    }
  },
  computed: {
    isMobile() {
      return window.innerWidth <= 768
    }
  },
  methods: {
    handleCategoryChange(tab) {
      this.activeCategory = tab.name
    },
    getProductsByCategory(categoryId) {
      if (categoryId === '1') {
        return this.products
      }
      return this.products.filter(product => product.categoryId === categoryId)
    },
    showProductDetail(product) {
      this.selectedProduct = product
      this.dialogVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>
.product-page {
  .hero {
    height: 300px;
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

      h1 {
        font-size: 2.5rem;
        margin-bottom: 1rem;
      }

      p {
        font-size: 1.2rem;
      }
    }
  }

  .categories {
    padding: 4rem 0;

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
    }

    .product-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
      gap: 2rem;
      margin-top: 2rem;
    }

    .product-card {
      background: #fff;
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 2px 12px rgba(0,0,0,0.1);
      transition: transform 0.3s ease;
      cursor: pointer;

      &:hover {
        transform: translateY(-5px);
      }

      .product-image {
        height: 200px;
        overflow: hidden;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
          transition: transform 0.3s ease;

          &:hover {
            transform: scale(1.1);
          }
        }
      }

      .product-info {
        padding: 1.5rem;

        h3 {
          margin-bottom: 0.5rem;
          color: #333;
        }

        p {
          color: #666;
          margin-bottom: 1rem;
          line-height: 1.6;
        }
      }
    }
  }
}

.product-detail {
  .product-gallery {
    margin-bottom: 2rem;

    img {
      width: 100%;
      height: 100%;
      object-fit: contain;
    }
  }

  .product-content {
    h2 {
      font-size: 1.5rem;
      color: #333;
      margin: 2rem 0 1rem;
    }

    .product-description {
      color: #666;
      line-height: 1.8;
      margin-bottom: 2rem;
    }

    .product-specs {
      margin-bottom: 2rem;
    }

    .product-features {
      ul {
        list-style: none;
        padding: 0;

        li {
          padding: 0.5rem 0;
          color: #666;
          position: relative;
          padding-left: 1.5rem;

          &::before {
            content: '✓';
            color: #1890ff;
            position: absolute;
            left: 0;
          }
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .product-page {
    .hero {
      height: 200px;

      h1 {
        font-size: 2rem;
      }

      p {
        font-size: 1rem;
      }
    }

    .categories {
      .product-grid {
        grid-template-columns: 1fr;
      }
    }
  }
}
</style> 