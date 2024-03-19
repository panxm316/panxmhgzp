<script setup lang="ts">
const props = defineProps({
  id: {
    type: String,
    default: ''
  }
})

import { useFlowStore } from '../../workflow/stores/flow'

const flowStore = useFlowStore()

var config = computed(() => {
  const step2 = flowStore.step2
  const filterElement = step2.filter((res) => res.id === props.id)[0]
  return filterElement
})
const { proxy } = getCurrentInstance()
</script>

<template>
  <div>
    <el-form-item label="最小值">
      <el-time-picker
        v-model="config.props.min"
        arrow-control
        size="default"
        class="formDate"
        value-format="HH:mm:ss"
      />
    </el-form-item>
    <el-form-item label="最大值">
      <el-time-picker
        v-model="config.props.max"
        arrow-control
        size="default"
        class="formDate"
        value-format="HH:mm:ss"
      />
    </el-form-item>

    <el-form-item label="默认值">
      <el-time-picker
        v-model="config.props.value"
        arrow-control
        size="default"
        class="formDate"
        value-format="HH:mm:ss"
      />
    </el-form-item>
  </div>
</template>

<style scoped lang="scss">
:deep(.formDate div.el-input__wrapper) {
  width: 100% !important;
}
:deep(.formDate) {
  width: 100% !important;
}
</style>
