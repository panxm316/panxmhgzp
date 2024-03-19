<!--
 * @Author: wanghl
 * @Date: 2024-01-19 11:17:03
 * @LastEditTime: 2024-02-21 10:04:59
 * @LastEditors: wanghl
 * @Description: 
-->
<template>
  <div style="cursor: pointer" @click="goto">
    <el-badge :value="props.unreadNum">
      <svg-icon icon-class="message" :size="'18'" style="color: #fff" />
    </el-badge>
  </div>
</template>
<script setup lang="ts">
import { getMessageCountByEmpIdApi } from '@/api/system/message'
import { useRouter } from 'vue-router'
import useUserStore from '@/store/modules/user'
import { IAxios } from 'axios'
import useMessageStore from '@/store/modules/message'
import { TMessageDataType, EMessageType } from '@/store/modules/type/index'

const props = withDefaults(
  defineProps<{
    unreadNum: number
  }>(),
  {
    unreadNum: 0
  }
)

const { user } = useUserStore()
const messageStore = useMessageStore()
const router = useRouter()

onMounted(() => {
  getMessageCountByEmpId()
})

const getMessageCountByEmpId = () => {
  if (user.userid) {
    getMessageCountByEmpIdApi(user.userid).then(({ success, obj }: IAxios<TMessageDataType>) => {
      if (
        obj &&
        (obj.type === EMessageType.SYSTEM_MSG ||
          obj.type === EMessageType.FLOW_ApprovePass_MSG ||
          obj.type === EMessageType.FLOW_ApproveReject_MSG ||
          obj.type === EMessageType.FLOW_TodoTask_MSG)
      ) {
        messageStore.setMessageData(obj)
      }
    })
  }
}

const goto = () => {
  router.push('/personal/usermessage')
}
</script>
<style lang="scss">
.el-badge__content {
  margin-top: 10px;
}
</style>
