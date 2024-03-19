<!--
 * @Author: caogd
 * @Date: 2023-10-17 10:56:15
 * @LastEditTime: 2024-03-08 16:32:59
 * @LastEditors: wanghl
 * @Description: 拒绝弹出审核处理
-->
<template>
  <div>
    <el-dialog v-model="dialogVisible" title="拒绝审核" width="400px">
      <el-input
        v-model="submitDesc"
        type="textarea"
        maxlength="100"
        :rows="5"
        placeholder="审核意见"
        show-word-limit
        style="margin-bottom: 10px"
      />
      <HgOpinionsSelect :data="queryInfo" @selectionztree="selectionztree" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { defineExpose } from 'vue'
import { completeTask } from '@/api/task'
import modal from '@/plugins/modal'

const dialogVisible = ref(false)
const submitDesc = ref('')
const currentData = ref()
const currentOpenFlowForm = ref()
onMounted(() => {
  submitDesc.value = ''
})
const handle = (row: any, formData: any) => {
  currentData.value = row
  currentOpenFlowForm.value = formData
  dialogVisible.value = true
}

defineExpose({ handle })
const emit = defineEmits(['taskSubmitEvent'])
const submit = () => {
  if (submitDesc.value.trim() === '') {
    modal.msgWarning('审核意见不能为空')
    return
  }
  const value = currentOpenFlowForm.value
  const formData: { [key: string]: any } = {}
  if (value) {
    for (const item of value) {
      formData[item.id] = item.props.value

      if (item.type === 'Layout') {
        const subList = item.props.value

        const d = []
        for (const array of subList) {
          const v: { [key: string]: any } = {}

          for (const subItem of array) {
            const value = subItem.props.value
            v[subItem.id] = value
          }
          d.push(v)
        }
        formData[item.id] = d
      }
    }
  }

  formData[currentData.value.nodeId + '_approve_condition'] = false
  const param = {
    paramMap: formData,
    taskId: currentData.value.taskId,
    taskLocalParamMap: {
      approveDesc: submitDesc.value
    }
  }

  completeTask(param).then((res) => {
    dialogVisible.value = false
    emit('taskSubmitEvent')
    submitDesc.value = ''
  })
}
/**
 * 选择意见-----------------------------------
 */
const selectionztree = (data: string) => {
  submitDesc.value = submitDesc.value.concat(data + ';')
}
import {
  TwOpinionsPageList,
  TwOpinionsList,
  TwsaveOpinions,
  TwopinionsList
} from '@/types/views/task/opinions'
/** 审批意见 */
const queryInfo = ref<TwOpinionsList>({
  cacheDTOKey: '',
  endTime: '',
  ipasstype: '',
  loginUserId: '',
  queryKey: '',
  sflowtype: '',
  startTime: ''
})
const props = defineProps<{ data?: TwOpinionsList }>()
/** 监听传值变化 */
watch(
  () => props.data,
  (newValue, oldValue) => {
    submitDesc.value = ''
    queryInfo.value = {
      cacheDTOKey: '',
      endTime: '',
      ipasstype: newValue!.ipasstype,
      loginUserId: '',
      queryKey: '',
      sflowtype: newValue!.sflowtype,
      startTime: ''
    }
  },
  { deep: true }
)
onMounted(() => {
  submitDesc.value = ''
  queryInfo.value = {
    cacheDTOKey: '',
    endTime: '',
    ipasstype: props.data!.ipasstype,
    loginUserId: '',
    queryKey: '',
    sflowtype: props.data!.sflowtype,
    startTime: ''
  }
})
</script>

<style scoped lang="scss"></style>
