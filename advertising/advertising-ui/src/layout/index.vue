<template>
  <div :class="classObj" class="app-wrapper" :style="{ '--current-color': theme }">
    <navbar style="position: fixed; top: 0px; z-index: 99; width: 100%" @setLayout="setLayout" />
    <div
      v-if="device === 'mobile' && sidebar.opened"
      class="drawer-bg"
      @click="handleClickOutside"
    />
    <sidebar v-if="!sidebar.hide" class="sidebar-container" />
    <div :class="{ hasTagsView: needTagsView, sidebarHide: sidebar.hide }" class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <tags-view v-if="needTagsView" />
      </div>
      <app-main style="position: fixed; top: 50px; z-index: 99" class="app_main" />
      <settings ref="settingRef" />
    </div>
    <!-- 常用菜单--------------------------- -->
    <!-- @mousedown="dragx($event)" -->
    <div
      class="chat-box"
      @mousedown="dragx($event)"
      style="position: absolute; z-index: 100"
      ref="chatbox"
      :class="isCollapse === true ? 'w50' : 'w160'"
      :style="{ left: '0px', top: '50px' }"
    >
      <el-button
        :class="isCollapse === true ? 'w50' : 'w160'"
        type="primary"
        style="box-shadow: 0 0 6px rgba(0, 0, 0, 0.12)"
        ><el-icon @click="handleOpen2" v-if="showMenu === false"><ArrowRightBold /></el-icon
        ><el-icon @click="handleOpen2" v-if="showMenu === true"><ArrowDownBold /></el-icon
        ><span @click="handleOpen2" v-if="appStore.sidebar.opened">我的</span>常用</el-button
      >
      <el-menu
        v-if="showMenu === true"
        :default-active="dingwei"
        :collapse="isCollapse"
        class="el-menu-vertical-demo"
        :class="isCollapse === true ? 'left10' : ''"
        style="min-height: 500px"
        :style="'max-height: ' + tableheight + 'px;overflow: auto;'"
      >
        <el-menu-item
          v-for="(item, index) in getMymenuList"
          :key="item.id"
          style="color: #ffffff"
          :class="[isActive == index ? 'addBgColor' : '', isCollapse === true ? 'left101' : '']"
          @click="handleOpen(item, index)"
          ><svg-icon
            :icon-class="item.simageurl"
            :style="!isCollapse ? 'margin-right:16px' : 'margin-left:10px;width:16px;height:16px'"
          />
          <template #title> {{ item.sname }}</template>
        </el-menu-item>
      </el-menu>
    </div>
    <!-- 常用菜单--------------------------- -->
  </div>
</template>

<script setup lang="ts">
import { computed, watchEffect, ref } from 'vue'
import { useWindowSize } from '@vueuse/core'
import Sidebar from './components/Sidebar/index.vue'
import { AppMain, Navbar, Settings, TagsView } from './components'
import { getMyCommonmenuApi } from '@/api/login'
import type { IAxios } from 'axios'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import { initWebSocket, webSocketReturn } from '@/utils/webSocket'
// 初始化websocket
initWebSocket()
const appStore = useAppStore()
const settingsStore = useSettingsStore()
const theme = computed(() => settingsStore.theme)
const sidebar = computed(() => useAppStore().sidebar)
const device = computed(() => useAppStore().device)
const needTagsView = computed(() => settingsStore.tagsView)
const fixedHeader = computed(() => settingsStore.fixedHeader)
const height = ref(window.innerHeight - 170 + 'px') // 页面高度
const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const { width } = useWindowSize()
const WIDTH = 992 // refer to Bootstrap's responsive design
const zuocedingwei = ref('') // 左侧减少的宽度值
watchEffect(() => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    useAppStore().closeSideBar({ withoutAnimation: false })
  }
  if (width.value - 1 < WIDTH) {
    useAppStore().toggleDevice('mobile')
    useAppStore().closeSideBar({ withoutAnimation: true })
  } else {
    useAppStore().toggleDevice('desktop')
  }
  if (sidebar.value.opened === true) {
    zuocedingwei.value = '160px'
  } else {
    zuocedingwei.value = '54px'
  }
})

function handleClickOutside() {
  useAppStore().closeSideBar({ withoutAnimation: false })
}

const settingRef = ref<InstanceType<typeof Settings>>()
function setLayout() {
  settingRef.value?.openSetting()
}

onUnmounted(() => {
  webSocketReturn.close()
})
/** 常用菜单-------------------------------------------- */
import { useRouter } from 'vue-router'
import { computTableHeight } from '@/utils/index'
const router = useRouter()
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() + 60
  getMymenu()
})
/** 表格高度 */
const tableheight = ref(0)
const chatbox = ref()
const dingwei = ref('')
/** 菜单显示隐藏 */
const showMenu = computed(() => appStore.showMenu)
/** 点击的时候转换true和false */

const isActive = ref(3000)
// const isCollapse = ref(false)
const isCollapse = computed(() => !appStore.sidebar.opened)
/**
 * 常用菜单列表
 */
const getMymenuList = ref<any[]>([])
/** 获取常用菜单 */
const getMymenu = () => {
  getMyCommonmenuApi().then(({ obj }: IAxios<any[]>) => {
    console.log(obj)
    getMymenuList.value = obj
  })
}
/** 拖拽属性 */
const dragx = (el: any) => {
  let oDiv = chatbox.value //当前元素
  let disX = el.clientX - oDiv.offsetLeft
  let disY = el.clientY - oDiv.offsetTop
  document.onmousemove = function (e) {
    //通过事件委托，计算移动的距离
    let l = e.clientX - disX
    let t = e.clientY - disY
    if (l < 0) {
      //如果左侧的距离小于0，就让距离等于0.不能超出屏幕左侧。如果需要磁性吸附，把0改为100或者想要的数字即可
      l = 0
    } else if (l > document.documentElement.clientWidth - oDiv.offsetWidth) {
      //如果左侧的距离>屏幕的宽度-元素的宽度。也就是说元素的右侧超出屏幕的右侧，就让元素的右侧在屏幕的右侧上
      l = document.documentElement.clientWidth - oDiv.offsetWidth
    }
    if (t < 0) {
      //和左右距离同理
      t = 0
    } else if (t > document.documentElement.clientHeight - oDiv.offsetHeight) {
      t = document.documentElement.clientHeight - oDiv.offsetHeight
    }
    //移动当前元素
    oDiv.style.left = l + 'px'
    oDiv.style.top = t + 'px'
  }
  document.onmouseup = function (e) {
    document.onmousemove = null
    document.onmouseup = null
  }
  // 解决有些时候,在鼠标松开的时候,元素仍然可以拖动;
  document.ondragstart = function (ev) {
    ev.preventDefault()
  }
  document.ondragend = function (ev) {
    ev.preventDefault()
  }
  return false
}
/** 打开菜单 */
const handleOpen = (data: any, index: number) => {
  dingwei.value = index + ''
  /** 点击跳转路由 */
  const to = '/' + data.srouterpath
  router.push(to)
  console.log(dingwei.value)
  isActive.value = index
}
/** 点击折行 */
const handleOpen2 = () => {
  appStore.toggleShowMenu(!showMenu.value)
  // isCollapse.value = !isCollapse.value
}
/** 常用菜单-------------------------------------------- */
</script>

<style lang="scss" scoped>
@import '@/assets/styles/mixin.scss';
@import '@/assets/styles/variables.module.scss';

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.drawer-bg {
  background: #000;
  opacity: 0.3;
  width: 100%;
  top: 0;
  height: 100%;
  position: absolute;
  z-index: 999;
}

.fixed-header {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 9;
  width: calc(100% - #{$base-sidebar-width});
  transition: width 0.28s;
}

.hideSidebar .fixed-header {
  width: calc(100% - 54px);
}

.sidebarHide .fixed-header {
  width: 100%;
}

.mobile .fixed-header {
  width: 100%;
}
.sidebar-container {
  margin-top: 50px;
}
.app_main {
  height: v-bind(height);
  width: calc(100% - v-bind(zuocedingwei));
  overflow-y: auto;
  background: #f8f8f9 !important;
}
.ne-table .ne-tr,
.ne-table .ne-td {
  border: 1px black solid !important;
  padding: 0px 30px;
}
h1 {
  text-align: center;
}
h2 {
  color: #000;
  cursor: pointer;
  margin-left: 10%;
  font-weight: bold;
}
h3 {
  color: #409eff;
  cursor: pointer;
  margin-left: 10%;
  font-weight: 600;
}
h4 {
  text-align: center;
}
.navbox {
  width: 200px;
  position: fixed;
  z-index: 10000;
  background: white;
  border-right: 1px solid var(--el-menu-border-color);
  top: 0px;
  left: 0px;
  height: 100%;
  overflow-y: auto;
  padding-bottom: 60px;
}
.img_box {
  margin: 0 auto;
  width: 80%;
  img {
    width: 80%;
    margin-left: 10%;
  }
  p {
    line-height: 40px;
  }
}
.el-menu-vertical-demo {
  background-color: rgb(84, 92, 100);
  border: none;
  padding: 0px 0 5px;
  text-align: center;
  // box-shadow: var(--el-box-shadow-light);
}
.addBgColor {
  color: #ffffff !important;
  background-image: linear-gradient(270deg, #409eff, #0072fe);
  border-top-left-radius: 30px;
  border-bottom-left-radius: 30px;
  font-size: 14px;
  margin: 8px 0px 8px 0px;
  height: 40px;
  line-height: 40px;
}
.left10 {
  left: -11px;
}
.left101 {
  padding-left: 10px;
}
.w50 {
  width: 54px;
}
.w160 {
  width: 160px;
}
</style>
