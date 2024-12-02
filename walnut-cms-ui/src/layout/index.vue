<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <sidebar class="sidebar-container" />
    <div class="main-container">
      <navbar />
      <app-main />
    </div>
  </div>
</template>

<script>
import { Navbar, Sidebar, AppMain } from './components'
import ResizeMixin from './mixin/resize'
import { mapState } from 'vuex'

export default {
  name: 'Layout',
  components: {
    Navbar,
    Sidebar,
    AppMain
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapState({
      sidebar: state => state.app.sidebar,
      device: state => state.app.device
    }),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/variables.scss";

.app-wrapper {
  position: relative;
  height: 100vh;
  width: 100%;

  .sidebar-container {
    position: fixed;
    top: 0;
    left: 0;
    width: $sideBarWidth;
    height: 100vh;
    background-color: var(--background-color-base);
    box-shadow: var(--box-shadow-base);
    z-index: 1001;
    transition: width 0.28s;
    overflow: hidden;
  }

  .main-container {
    position: relative;
    margin-left: $sideBarWidth;
    min-height: 100vh;
    background-color: var(--background-color-light);
    transition: margin-left 0.28s;
    overflow-x: hidden;
  }

  &.hideSidebar {
    .sidebar-container {
      width: 54px;
    }
    .main-container {
      margin-left: 54px;
    }
  }

  &.mobile {
    .sidebar-container {
      position: fixed;
      top: 0;
      left: 0;
      width: $sideBarWidth !important;
      height: 100vh;
      transition: transform 0.3s;
      z-index: 1001;
    }

    .main-container {
      margin-left: 0;
    }

    &.openSidebar {
      .sidebar-container {
        transform: translate3d(0, 0, 0);
      }
      .main-container {
        position: fixed;
        left: $sideBarWidth;
      }
    }

    &.hideSidebar {
      .sidebar-container {
        transform: translate3d(-$sideBarWidth, 0, 0);
      }
      .main-container {
        position: relative;
        left: 0;
      }
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0;
    left: 0;
    z-index: 999;
  }
}
</style> 