<!--
 * @Author: wanghl
 * @Date: 2023-08-10 14:57:39
 * @LastEditTime: 2023-08-21 13:46:31
 * @LastEditors: peij
 * @Description:
-->
<template>
  <section class="app-main">
    <router-view v-slot="{ Component, route }">
      <!-- <transition name="fade-transform" mode="out-in"> -->
      <keep-alive :include="tagsViewStore.cachedViews">
        <component :is="Component" v-if="!route.meta.link" :key="route.path" />
      </keep-alive>
      <!-- </transition> -->
    </router-view>
    <!-- <iframe-toggle /> -->
  </section>
</template>

<script setup lang="ts">
import iframeToggle from './IframeToggle/index.vue'
import useTagsViewStore from '@/store/modules/tagsView'

const tagsViewStore = useTagsViewStore()
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header + .app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
    background-color: #fff;
  }

  .fixed-header + .app-main {
    padding-top: 84px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 17px;
  }
}
</style>
