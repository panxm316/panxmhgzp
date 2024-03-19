<!--
 * @Author: caogd
 * @Date: 2023-10-17 10:56:15
 * @LastEditTime: 2023-11-14 15:31:27
 * @LastEditors: wanghl
 * @Description: 流程树组件
-->
<script setup lang="ts">
import FlowNodeFormat from '@/components/Flow/FlowNodeFormat.vue'
import { formatStartNodeShow } from '@/api/task'
import { onMounted, ref, watch } from 'vue'
import { getAssetsImage } from '@/utils/index'

const props = defineProps({
  flowId: {
    type: String,
    default: ''
  },
  disableSelect: {
    type: Boolean,
    default: false
  },
  taskId: {
    type: String,
    default: ''
  },
  processInstanceId: {
    type: String,
    default: ''
  },
  formData: {
    type: Object,
    dafault: () => {}
  },
  selectUserNodeId: {
    type: Array,
    dafault: () => []
  },
  customerProcessInstance: {
    type: Array,
    dafault: () => []
  }
})
const row = ref([])
const allProcess = ref([])

const queryData = (isAllProcess: boolean, p: any = {}) => {
  var data = {
    flowId: props.flowId,
    processInstanceId: props.processInstanceId,
    paramMap: p,
    taskId: props.taskId,
    isAllProcess
  }
  formatStartNodeShow(data).then(({ obj }) => {
    if (isAllProcess) {
      allProcess.value = obj
    } else {
      row.value = obj
    }
  })
}
watch(
  () => props.processInstanceId,
  (val) => {
    setTimeout(function () {
      if (new Date().getTime() - formDataChangeTime.value > 500) {
        formDataChangeTime.value = new Date().getTime()
        queryData(false)
        queryData(true)
      }
    }, 600)
  }
)
const formDataChangeTime = ref()
onMounted(() => {
  formDataChangeTime.value = new Date().getTime()
  queryData(false, {})
  queryData(true, {})
})

const validate = () => {
  if (props.selectUserNodeId) {
    for (const k of props.selectUserNodeId as any) {
      const d = nodeUser.value[k]
      // if (d && d.length > 0) {
      // } else {
      //   return false
      // }
      if (!d || d.length <= 0) {
        return false
      }
    }
  }
  return true
}

const nodeUser = ref<{ [key: string]: any }>({})
const activeName = ref('currentProcess')

const formatSelectNodeUser = () => {
  const obj: { [key: string]: any } = {}
  if (props.selectUserNodeId) {
    for (const k of props.selectUserNodeId as any) {
      const d = nodeUser.value[k]
      obj[k + '_assignee_select'] = d
    }
  }

  return obj
}

const handleTabChange = () => {
  if (activeName.value === 'currentProcess') {
    queryData(false)
  } else {
    queryData(true)
  }
}

defineExpose({ validate, formatSelectNodeUser })
</script>

<template>
  <el-tabs v-model="activeName" class="demo-tabs" @tab-change="handleTabChange">
    <el-tab-pane label="当前流程" name="currentProcess"
      ><flow-node-format
        ref="flowNodeFormatRef"
        :row="row"
        :node-user="nodeUser"
        :disable-select="disableSelect"
      ></flow-node-format
    ></el-tab-pane>
    <el-tab-pane label="流程设置" name="allProcess"
      ><flow-node-format
        ref="flowNodeFormatRef"
        :row="allProcess"
        :node-user="nodeUser"
        :disable-select="disableSelect"
      ></flow-node-format></el-tab-pane
  ></el-tabs>
</template>

<style scoped lang="scss"></style>
