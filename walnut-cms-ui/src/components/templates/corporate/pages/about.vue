<template>
  <div class="about-page">
    <!-- Hero Section -->
    <section class="hero" :style="{ backgroundImage: `url(${config.aboutHeroImage || ''})` }">
      <div class="container">
        <h1>{{ config.aboutTitle || '关于我们' }}</h1>
        <p>{{ config.aboutSubtitle || '了解我们的故事和使命' }}</p>
      </div>
    </section>

    <!-- Company Info -->
    <section class="company-info">
      <div class="container">
        <div class="info-grid">
          <div class="info-text">
            <h2>公司简介</h2>
            <div class="content" v-html="config.companyIntro || '这里是公司简介内容...'"></div>
          </div>
          <div class="info-image">
            <img :src="config.companyImage" alt="Company" v-if="config.companyImage">
          </div>
        </div>
      </div>
    </section>

    <!-- Values -->
    <section class="values">
      <div class="container">
        <h2>企业文化</h2>
        <div class="values-grid">
          <div v-for="(value, index) in values" :key="index" class="value-item">
            <div class="icon">
              <i :class="value.icon"></i>
            </div>
            <h3>{{ value.title }}</h3>
            <p>{{ value.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Team -->
    <section class="team">
      <div class="container">
        <h2>管理团队</h2>
        <div class="team-grid">
          <div v-for="(member, index) in team" :key="index" class="team-member">
            <div class="member-image">
              <img :src="member.avatar" :alt="member.name">
            </div>
            <h3>{{ member.name }}</h3>
            <p class="position">{{ member.position }}</p>
            <p class="description">{{ member.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- History -->
    <section class="history">
      <div class="container">
        <h2>发展历程</h2>
        <div class="timeline">
          <div v-for="(item, index) in history" :key="index" class="timeline-item">
            <div class="time">{{ item.year }}</div>
            <div class="content">
              <h3>{{ item.title }}</h3>
              <p>{{ item.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'CorporateAbout',
  props: {
    config: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      values: [
        {
          icon: 'el-icon-aim',
          title: '愿景',
          description: '成为行业领导者'
        },
        {
          icon: 'el-icon-trophy',
          title: '使命',
          description: '为客户创造价值'
        },
        {
          icon: 'el-icon-star-off',
          title: '价值观',
          description: '诚信、创新、共赢'
        },
        {
          icon: 'el-icon-coordinate',
          title: '目标',
          description: '持续创新发展'
        }
      ],
      team: [
        {
          name: '张三',
          position: '首席执行官',
          description: '拥有20年行业经验',
          avatar: 'https://via.placeholder.com/150'
        },
        {
          name: '李四',
          position: '技术总监',
          description: '技术创新领军人',
          avatar: 'https://via.placeholder.com/150'
        },
        {
          name: '王五',
          position: '市场总监',
          description: '营销策略专家',
          avatar: 'https://via.placeholder.com/150'
        }
      ],
      history: [
        {
          year: '2023',
          title: '全球扩张',
          description: '开拓国际市场'
        },
        {
          year: '2020',
          title: '技术突破',
          description: '获得多项发明专利'
        },
        {
          year: '2015',
          title: '品牌升级',
          description: '完成品牌战略升级'
        },
        {
          year: '2010',
          title: '公司成立',
          description: '开启创业征程'
        }
      ]
    }
  }
}
</script>

<style lang="scss" scoped>
.about-page {
  .hero {
    height: 400px;
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
        font-size: 3rem;
        margin-bottom: 1rem;
      }

      p {
        font-size: 1.5rem;
      }
    }
  }

  section {
    padding: 5rem 0;

    .container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 1rem;
    }

    h2 {
      text-align: center;
      font-size: 2rem;
      color: #333;
      margin-bottom: 3rem;
    }
  }

  .company-info {
    background: #fff;

    .info-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 4rem;
      align-items: center;

      .info-text {
        h2 {
          text-align: left;
          margin-bottom: 2rem;
        }

        .content {
          color: #666;
          line-height: 1.8;
        }
      }

      .info-image {
        img {
          width: 100%;
          border-radius: 8px;
          box-shadow: 0 4px 20px rgba(0,0,0,0.1);
        }
      }
    }
  }

  .values {
    background: #f9f9f9;

    .values-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 2rem;
    }

    .value-item {
      text-align: center;
      padding: 2rem;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 2px 12px rgba(0,0,0,0.1);
      transition: transform 0.3s ease;

      &:hover {
        transform: translateY(-5px);
      }

      .icon {
        i {
          font-size: 3rem;
          color: #1890ff;
          margin-bottom: 1rem;
        }
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

  .team {
    .team-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 2rem;
    }

    .team-member {
      text-align: center;

      .member-image {
        width: 150px;
        height: 150px;
        margin: 0 auto 1rem;
        overflow: hidden;
        border-radius: 50%;
        box-shadow: 0 4px 20px rgba(0,0,0,0.1);

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }

      h3 {
        margin-bottom: 0.5rem;
        color: #333;
      }

      .position {
        color: #1890ff;
        margin-bottom: 1rem;
      }

      .description {
        color: #666;
      }
    }
  }

  .history {
    background: #f9f9f9;

    .timeline {
      position: relative;
      max-width: 800px;
      margin: 0 auto;

      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 50%;
        transform: translateX(-50%);
        width: 2px;
        height: 100%;
        background: #1890ff;
      }
    }

    .timeline-item {
      position: relative;
      padding: 2rem 0;
      width: 50%;
      
      &:nth-child(odd) {
        padding-right: 3rem;
        text-align: right;

        .time {
          right: -4.5rem;
        }
      }

      &:nth-child(even) {
        margin-left: 50%;
        padding-left: 3rem;

        .time {
          left: -4.5rem;
        }
      }

      .time {
        position: absolute;
        top: 2rem;
        width: 3rem;
        text-align: center;
        background: #1890ff;
        color: #fff;
        padding: 0.5rem;
        border-radius: 4px;
      }

      .content {
        background: #fff;
        padding: 1.5rem;
        border-radius: 8px;
        box-shadow: 0 2px 12px rgba(0,0,0,0.1);

        h3 {
          margin-bottom: 0.5rem;
          color: #333;
        }

        p {
          color: #666;
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .about-page {
    .hero {
      height: 300px;

      h1 {
        font-size: 2rem;
      }

      p {
        font-size: 1.2rem;
      }
    }

    .company-info {
      .info-grid {
        grid-template-columns: 1fr;
        gap: 2rem;
      }
    }

    .history {
      .timeline {
        &::before {
          left: 0;
        }
      }

      .timeline-item {
        width: 100%;
        padding: 0 0 2rem 2rem !important;
        margin-left: 0 !important;
        text-align: left !important;

        .time {
          left: -1.5rem !important;
          right: auto !important;
        }
      }
    }
  }
}
</style> 