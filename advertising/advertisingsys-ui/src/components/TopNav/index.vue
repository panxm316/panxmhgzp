<template>
  <el-menu :default-active="activeMenu" mode="horizontal" :ellipsis="false" @select="handleSelect">
    <div>
      <logo :collapse="true" />
    </div>
    <hamburger
      id="hamburger-container"
      :is-active="appStore.sidebar.opened"
      class="hamburger-container"
      style="cursor: pointer; text-align: center; line-height: 45px"
      @toggleClick="toggleSideBar"
    />
    <template v-for="(item, index) in topMenus">
      <el-menu-item
        v-if="index < visibleNumber"
        :key="index"
        :style="{ '--theme': theme }"
        style="padding: 0 15px !important"
        :index="item.path"
      >
        <svg-icon :icon-class="item.meta.icon" style="margin-right: 5px" />
        <!-- <svg-icon icon-class="system" class="el-input__icon input-icon" /> -->
        {{ item.meta.title }}</el-menu-item
      >
    </template>

    <!-- 顶部菜单超出数量折叠 -->
    <el-sub-menu v-if="topMenus.length > visibleNumber" :style="{ '--theme': theme }" index="more">
      <template #title>更多菜单</template>
      <template v-for="(item, index) in topMenus">
        <el-menu-item v-if="index >= visibleNumber" :key="index" :index="item.path"
          ><svg-icon :icon-class="item.meta.icon" /> {{ item.meta.title }}</el-menu-item
        >
      </template>
    </el-sub-menu>
  </el-menu>
</template>

<script setup lang="ts">
import { computed, ref, onBeforeUnmount, onMounted } from 'vue'
import { useRoute, useRouter, RouteRecordRaw } from 'vue-router'
import { constantRoutes } from '@/router'
import { isHttp } from '@/utils/validate'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import Logo from '@/layout/components/Sidebar/Logo.vue'
// 顶部栏初始数
const visibleNumber = ref<number>(0)
// 当前激活菜单的 index
const currentIndex = ref(null)
// 隐藏侧边栏路由
const hideList = ['/index', '/user/profile']

const appStore = useAppStore()
const settingsStore = useSettingsStore()
const permissionStore = usePermissionStore()
const route = useRoute() as any
const router = useRouter() as any
const isCollapse = computed(() => !appStore.sidebar.opened)
// 主题颜色
const theme = computed(() => settingsStore.theme)
// 所有的路由信息
const routers = computed(() => permissionStore.topbarRouters)

// 顶部显示菜单
const topMenus = computed(() => {
  const topMenus: any[] = []
  routers.value.map((menu) => {
    if (menu.hidden !== true) {
      // 兼容顶部栏一级菜单内部跳转
      if (menu.path === '/') {
        if (menu.children) {
          topMenus.push(menu.children[0])
        }
      } else {
        topMenus.push(menu)
      }
    }
  })
  return topMenus
})

// 设置子路由
const childrenMenus = computed(() => {
  const childrenMenus: any[] = []
  routers.value.map((router) => {
    const children = router.children as {
      [Key in keyof (RouteRecordRaw & { parentPath: string })]: (RouteRecordRaw & {
        parentPath: string
      })[Key]
    }[]
    for (const item in children) {
      if (children[item].parentPath === undefined) {
        if (router.path === '/') {
          children[item].path = '/' + children[item].path
        } else {
          if (!isHttp(children[item].path)) {
            children[item].path = router.path + '/' + children[item].path
          }
        }
        children[item].parentPath = router.path
      }
      childrenMenus.push(children[item])
    }
  })
  return constantRoutes.concat(childrenMenus)
})

// 默认激活的菜单
const activeMenu = computed(() => {
  let path = route.path
  // peij 20230911   默认选中一级菜单
  if (routers.value.length > 0 && path === '/index') {
    if (routers.value[0].children && routers.value[0].children?.length > 0) {
      path = routers.value[0].children[0].path
    }
  }

  let activePath = path
  if (path !== undefined && path.lastIndexOf('/') > 0 && hideList.indexOf(path) === -1) {
    const tmpPath = path.substring(1, path.length)
    activePath = '/' + tmpPath.substring(0, tmpPath.indexOf('/'))
    if (!route.meta.link) {
      appStore.toggleSideBarHide(false)
    }
  } else if (!route.children) {
    activePath = path
    appStore.toggleSideBarHide(true)
  }
  activeRoutes(activePath)
  return activePath
})

function setVisibleNumber() {
  const width = document.body.getBoundingClientRect().width / 3
  visibleNumber.value = width / 85
}

function handleSelect(key: any, keyPath: any) {
  currentIndex.value = key
  const route = routers.value.find((item) => item.path === key)
  if (isHttp(key)) {
    // http(s):// 路径新窗口打开
    window.open(key, '_blank')
  } else if (!route || !route.children) {
    // 没有子路由路径内部打开
    router.push({ path: key })
    appStore.toggleSideBarHide(true)
  } else {
    // 显示左侧联动菜单
    activeRoutes(key)
    appStore.toggleSideBarHide(false)
    // caogd 20230809 点击上测菜单默认选中子菜单中第一个
    if (permissionStore.sidebarRouters?.length > 0) {
      const routes = permissionStore.sidebarRouters
      if (routes.length > 0 && routes[0]?.children && routes[0]?.children?.length > 0) {
        router.push({ path: routes[0]?.path + '/' + routes[0]?.children[0]?.path })
      } else {
        router.push({ path: routes[0]?.path })
      }
    }
  }
}

function activeRoutes(key: any) {
  const routes: any[] = []
  if (childrenMenus.value && childrenMenus.value.length > 0) {
    childrenMenus.value.map((item: any) => {
      if (key === item.parentPath || (key === 'index' && item.path === '')) {
        routes.push(item)
      }
    })
  }
  if (routes.length > 0) {
    permissionStore.setSidebarRouters(routes)
  } else {
    appStore.toggleSideBarHide(true)
  }
  return routes
}

onMounted(() => {
  window.addEventListener('resize', setVisibleNumber)
})
onBeforeUnmount(() => {
  window.removeEventListener('resize', setVisibleNumber)
})

onMounted(() => {
  setVisibleNumber()
})
/**
 * 显示隐藏切换
 */
const toggleSideBar = () => {
  appStore.toggleSideBar(false)
}
</script>

<style lang="scss">
.topmenu-container.el-menu--horizontal > .el-menu-item {
  float: left;
  height: 50px !important;
  line-height: 50px !important;
  color: #fff !important;
  padding: 0 5px !important;
  margin: 0 10px !important;
  &:hover {
    background: rgba(0, 0, 0, 0);
  }
}
.el-menu {
  background-color: rgba(0, 0, 0, 0);
}
.topmenu-container.el-menu--horizontal > .el-menu-item.is-active,
.el-menu--horizontal > .el-sub-menu.is-active .el-submenu__title {
  border-bottom: 2px solid #0072fe !important;
  color: #409eff;
  background-color: rgba(48, 65, 86, 0.4);
  background-image: none;
  margin: 0 10px;
  font-size: 16px;
  border-radius: 0px;
  span {
    font-size: 16px;
    //color: #409EFF;
  }
  svg {
    font-size: 16px;
    //color: #409EFF;
  }
}
.is-opened {
  background-color: #fff;
}
/* sub-menu item */
.topmenu-container.el-menu--horizontal > .el-sub-menu .el-sub-menu__title {
  float: left;
  height: 50px !important;
  line-height: 50px !important;
  color: #fff !important;
  padding: 0 5px !important;
  margin: 0 10px !important;
}
.el-menu--horizontal > .el-sub-menu .el-sub-menu__title:hover {
  background-color: rgba(48, 65, 86, 0);
}
</style>
