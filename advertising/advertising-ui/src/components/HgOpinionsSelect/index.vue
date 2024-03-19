<!--
 * @Author: wanghl
 * @Date: 2024-03-08 13:57:30
 * @LastEditTime: 2024-03-11 08:33:20
 * @LastEditors: wanghl
 * @Description: 意见选择
-->

<template>
  <el-popover placement="bottom" :width="300" trigger="hover">
    <template #reference>
      <el-button type="primary" icon="ChatLineRound" size="small" @click="showsopinion"
        >选择审批意见</el-button
      >
    </template>
    <ul class="opinions-list">
      <li v-for="item in opinionsList" :key="item.id" @click="onOpinionClick(item)">
        <span v-if="item.sopinion === '' || item.sopinion === undefined">{{ item.sopinion }}</span>
        <span v-else>{{ item.sopinion }}</span>
        {{ item.sopinion }}
      </li>
    </ul>
  </el-popover>
</template>
<script setup lang="ts">
defineOptions({
  name: 'HgOpinionsSelect'
})
import { defineProps, defineEmits, ref, watch, nextTick } from 'vue'
import { CSSProperties } from 'vue'
import { getOpinionsListApi, getopinionsByIdApi } from '@/api/task/opinions'
import {
  TwOpinionsPageList,
  TwOpinionsList,
  TwsaveOpinions,
  TwopinionsList
} from '@/types/views/task/opinions'
import useUserStore from '@/store/modules/user'
import { IAxios } from 'axios'
const user = useUserStore().user

const props = defineProps<{ data?: TwOpinionsList }>()

/** 导出返回数据 */
const emit = defineEmits<{
  selectionztree: [data: string]
}>()
// const ztreeSelectIds = ref(props.selectids)
/** 审批意见列表 */
const opinionsList = ref<TwopinionsList[]>([])
/** 审批意见 */
const queryInfo = ref<TwOpinionsList>({
  cacheDTOKey: '',
  endTime: '',
  ipasstype: '',
  loginUserId: user.userid,
  queryKey: '',
  sflowtype: '',
  startTime: ''
})
watch(
  () => props.data,
  (newValue, oldValue) => {
    queryInfo.value = {
      cacheDTOKey: '',
      endTime: '',
      ipasstype: newValue!.ipasstype,
      loginUserId: user.userid,
      queryKey: '',
      sflowtype: newValue!.sflowtype,
      startTime: ''
    }
    nextTick(() => {
      getOpinionsList()
    })
  },
  { deep: true }
)
/** 下拉显示隐藏 */
const visible = ref(false)
onMounted(() => {
  queryInfo.value = {
    cacheDTOKey: '',
    endTime: '',
    ipasstype: props.data!.ipasstype,
    loginUserId: user.userid,
    queryKey: '',
    sflowtype: props.data!.sflowtype,
    startTime: ''
  }
  nextTick(() => {
    getOpinionsList()
  })
})

/** 获取意见列表数据 */
const getOpinionsList = () => {
  getOpinionsListApi(queryInfo.value).then((res: IAxios<TwopinionsList[]>) => {
    console.log(res)
    opinionsList.value = res.obj
  })
}
/** 显示意见 */
const showsopinion = () => {
  visible.value = !visible.value
}
/** 点击意见 */
const onOpinionClick = (item: TwopinionsList) => {
  visible.value = false
  emit('selectionztree', item.sopinion as string)
}
</script>
<style scoped lang="scss">
.opinions-list {
  padding: 0;
  margin: 0;
  list-style: none;
  li {
    padding: 0;
    line-height: 30px;
    cursor: pointer;
    &:hover {
      color: #1562d5;
    }
  }
}
</style>
