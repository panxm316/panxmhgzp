<script setup lang="ts">
const props = defineProps({
  id: {
    type: String,
    default: ''
  }
})
import employeesDialog from '@/views/flow/workflow/components/dialog/employeesDialog.vue'
import orgItem from '@/views/flow/workflow/components/dialog/orgItem.vue'

import { useFlowStore } from '../../workflow/stores/flow'
import { Plus } from '@element-plus/icons-vue'
import { ref } from 'vue'

const flowStore = useFlowStore()

var config = computed(() => {
  const step2 = flowStore.step2
  var idObjList = step2.filter((res) => res.id === props.id)
  if (idObjList.length > 0) {
    return idObjList[0]
  }

  const list = step2.filter((res) => res.type === 'Layout')
  for (var item of list) {
    const value = item.props.value
    var valueList = value.filter((res) => res.id === props.id)
    if (valueList.length > 0) {
      return valueList[0]
    }
  }

  return undefined
})

var defaultValue = computed({
  get: () => {
    return config.value.props.value
  },
  set: (s: any[]) => {
    config.value.props.value = s
  }
})

const { proxy } = getCurrentInstance()

// 校验
const validate = () => {
  return true
}
defineExpose({ validate })

const selectUserDialogVisible = ref(false)
const afterSelectUser = (data) => {
  defaultValue.value = data
  selectUserDialogVisible.value = false
}
</script>

<template>
  <div>
    <employees-dialog
      v-model:visible="selectUserDialogVisible"
      :data="defaultValue"
      type="dept"
      :multiple="config.props.multi"
      @change="afterSelectUser"
    />

    <el-form-item label="默认值">
      <el-button circle :icon="Plus" @click.stop="selectUserDialogVisible = true"></el-button>

      <div style="width: 100%">
        <org-item v-model:data="defaultValue" />
      </div>
    </el-form-item>
  </div>
</template>

<style scoped lang="scss"></style>
