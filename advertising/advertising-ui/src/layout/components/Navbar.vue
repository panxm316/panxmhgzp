<template>
  <div class="navbar">
    <div style="width: 200px; float: left">
      <logo :collapse="true" />
    </div>
    <hamburger
      id="hamburger-container"
      :is-active="appStore.sidebar.opened"
      class="hamburger-container"
      style="cursor: pointer; text-align: center; line-height: 45px"
      @toggleClick="toggleSideBar"
    />
    <!-- 面包屑导航 -->
    <!-- <breadcrumb
      v-if="!settingsStore.topNav"
      id="breadcrumb-container"
      class="breadcrumb-container"
    /> -->
    <top-nav v-if="settingsStore.topNav" id="topmenu-container" class="topmenu-container" />

    <div class="right-menu">
      <template v-if="appStore.device !== 'mobile'">
        <header-search id="header-search" class="right-menu-item" />

        <!-- <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />
        </el-tooltip>

        <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
        </el-tooltip>

        <screenfull id="screenfull" class="right-menu-item hover-effect" /> -->
        <el-tooltip content="消息通知" effect="dark" placement="bottom">
          <msg-alert style="margin-right: 30px" :unread-num="messageData.messageCount" />
        </el-tooltip>
        <el-tooltip
          :content="settopnav ? '关闭 TopNav' : '开启 TopNav'"
          effect="dark"
          placement="bottom"
          @click="topNavsz"
        >
          <span v-if="settopnav" style="cursor: pointer; padding-top: 3px; margin-right: 10px"
            ><svg-icon icon-class="topleft" size="24" @click="topNavsz"></svg-icon
          ></span>
          <span v-else style="cursor: pointer; padding-top: 3px; margin-right: 10px"
            ><svg-icon icon-class="leftright" size="24" @click="topNavsz"></svg-icon
          ></span>
        </el-tooltip>
        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>
      </template>
      <div class="avatar-container">
        <el-dropdown class="right-menu-item hover-effect" trigger="click" @command="handleCommand">
          <div class="avatar-wrapper">
            <img :src="userStore.avatar" class="user-avatar" />
            <div class="deptname">
              <p :class="userStore.user?.deptname == '' ? 'oneclass' : 'twoclass'">
                {{ userStore.user?.username }}
              </p>
              <p v-if="userStore.user?.deptname != ''">
                {{ userStore.user?.deptname }}
              </p>
            </div>
            <!-- <h5
              style="
                float: right;
                margin: 5px 0px 0 5px;
                color: #fff;
                font-size: 13px;
                line-height: 16px;
              "
            ></h5> -->
            <el-icon :size="18" style="color: #fff"><caret-bottom /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <router-link to="/user/profile">
                <el-dropdown-item>个人中心</el-dropdown-item>
              </router-link>
              <router-link to="/user/mymenu">
                <el-dropdown-item>常用设置</el-dropdown-item>
              </router-link>
              <router-link v-if="userStore.user.adminflag === 3" to="/personal/operatelog">
                <el-dropdown-item>操作日志</el-dropdown-item>
              </router-link>
              <!-- <el-dropdown-item command="setLayout">
                <span>布局设置</span>
              </el-dropdown-item> -->
              <!-- <el-dropdown-item divided command="topNavsz">
                <span v-if="settopnav">关闭 TopNav</span>
                <span v-else>开启 TopNav</span>
              </el-dropdown-item> -->
              <el-dropdown-item divided command="logout">
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessageBox, ElNotification, NotificationHandle } from 'element-plus'
import Breadcrumb from '@/components/Breadcrumb/index.vue'
import TopNav from '@/components/TopNav/index.vue'
import Hamburger from '@/components/Hamburger/index.vue'
import Screenfull from '@/components/Screenfull/index.vue'
import SizeSelect from '@/components/SizeSelect/index.vue'
import HeaderSearch from '@/components/HeaderSearch/index.vue'
import RuoYiGit from '@/components/RuoYi/Git/index.vue'
import RuoYiDoc from '@/components/RuoYi/Doc/index.vue'
import { storeToRefs } from 'pinia'
import useAppStore from '@/store/modules/app'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import useMessageStore from '@/store/modules/message'
import { EMessageType, type TMessageDataType } from '@/store/modules/type/index'
import Logo from './Sidebar/Logo.vue'
import Cookies from 'js-cookie'
import { useRouter } from 'vue-router'
import { hgFormatDate } from '@/utils'
const router = useRouter()
const { proxy } = getCurrentInstance() as any
const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()
const messageStore = useMessageStore()
const { messageData, noticeCount, taskCount } = storeToRefs(messageStore)
const settopnav = ref(Cookies.get('topNavsys') === 'true')
let notificationhandle: NotificationHandle
function toggleSideBar() {
  appStore.toggleSideBar()
}

function handleCommand(command: any) {
  switch (command) {
    case 'setLayout':
      setLayout()
      break
    case 'logout':
      logout()
      break
    case 'topNavsz':
      topNavsz()
      break
    default:
      break
  }
}

function logout() {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      userStore.logOut().then(() => {
        location.href = import.meta.env.BASE_URL + 'index'
      })
    })
    .catch(() => {})
}

const emits = defineEmits(['setLayout'])
function setLayout() {
  emits('setLayout')
}
const topNavsz = () => {
  console.log(settopnav.value)
  ElMessageBox.confirm('确定修改布局吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      settopnav.value = !settopnav.value
      proxy.$modal.loading('正在切换请稍后...')
      Cookies.set('topNavsys', settopnav.value.toString())
      setTimeout(() => window.location.reload(), 1000)
    })
    .catch(() => {})
}
/** 监听系统消息 */
watch(
  () => messageData.value.messageCount,
  (newValue, oldValue) => {
    if (newValue > oldValue && messageData.value.messageContent) {
      notificationhandle = ElNotification({
        title: messageData.value.messageTitle,
        // type: 'success',
        duration: 0,
        offset: 34,
        customClass: 'notificationmsg',
        dangerouslyUseHTMLString: true,
        message: `<a href='javascript:;'>${messageData.value.messageContent}</a>`,
        onClick: () => handleMsgClick(messageData.value)
      })
    }
  }
)
const handleMsgClick = (messageDataType: TMessageDataType) => {
  // 通过否决消息
  if (
    messageDataType.type === EMessageType.FLOW_ApprovePass_MSG ||
    messageDataType.type === EMessageType.FLOW_ApproveReject_MSG
  ) {
    messageStore.updateMessageStatus(
      {
        messageId: messageDataType.messageId,
        readed: true
      },
      {
        processInstanceId: messageDataType.processInstanceId,
        processInstanceCreate: hgFormatDate(
          new Date(messageDataType.processInstanceCreate),
          'YYYY-MM-DD'
        ),
        processInstanceType: messageDataType.type
      }
    )
    router.push({
      path: '/task/started'
    })
  }
  // 待办消息
  if (messageDataType.type === EMessageType.FLOW_TodoTask_MSG) {
    messageStore.updateMessageStatus(
      {
        messageId: messageDataType.messageId,
        readed: true
      },
      {
        processInstanceId: messageDataType.processInstanceId,
        processInstanceCreate: '',
        processInstanceType: messageDataType.type
      }
    )
    router.push({
      path: '/task/pending'
    })
  }
  notificationhandle.close()
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background-image: linear-gradient(90deg, #0072fe, #00cec6);
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;
    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 250px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;
    display: flex;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 40px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 20px;
        }

        i {
          cursor: pointer;
          position: absolute;
          right: -25px;
          top: 10px;
        }
      }
    }
  }
}
.deptname {
  float: right;
  color: #fff;
  font-size: 13px;
  height: 60px;
  display: table-cell;
  vertical-align: middle;
  margin-left: 5px;
  text-align: center;
  p {
    margin: 0;
    padding: 0;
    line-height: 16px;
  }
}
.oneclass {
  margin-top: 12px !important;
  font-size: 14px;
}
.twoclass {
  margin-top: 3px !important;
}
:global(.notificationmsg) {
  width: 500px;
}
</style>
