<!--
 * @Author: caogd
 * @Date: 2023-10-17 10:56:15
 * @LastEditTime: 2024-03-11 08:35:33
 * @LastEditors: wanghl
 * @Description: 同意弹出条件处理
-->
<script setup lang="ts">
import { defineExpose } from 'vue'
import { completeTask } from '@/api/task'
import { ITaskDto } from '@/types/views/task/completed'

const dialogVisible = ref(false)

const submitDesc = ref('')

const currentData = ref<ITaskDto>()
const currentOpenFlowForm = ref()

const handle = (row: ITaskDto, formData: any) => {
  currentData.value = row

  currentOpenFlowForm.value = formData
  console.log(currentOpenFlowForm.value)
  dialogVisible.value = true
}

defineExpose({ handle })
const emit = defineEmits(['taskSubmitEvent'])

const submit = () => {
  const value = currentOpenFlowForm.value

  const formData: { [key: string]: any } = {}
  if (value) {
    for (const item of value) {
      formData[item.id] = item.props.value

      if (item.type === 'Layout') {
        const subList = item.props.value

        const d = []
        for (const array of subList) {
          const v: any = {}

          for (const subItem of array) {
            const value: any = subItem.props.value
            v[subItem.id] = value
          }
          d.push(v)
        }
        formData[item.id] = d
      }
    }
  }

  formData[currentData.value?.nodeId + '_approve_condition'] = true

  const param = {
    paramMap: formData,
    taskId: currentData.value?.taskId,
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

<template>
  <div>
    <el-dialog v-model="dialogVisible" title="提交审核" width="400px">
      <el-input
        v-model="submitDesc"
        type="textarea"
        maxlength="100"
        :rows="5"
        placeholder="审核意见"
        show-word-limit
        style="margin-bottom: 10px"
      />
      <HgOpinionsSelect @selectionztree="selectionztree" :data="queryInfo" />
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submit"> 确定 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="less"></style>
