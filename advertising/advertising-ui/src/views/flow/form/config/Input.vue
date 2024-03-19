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
</script>

<template>
  <div>
    <el-form-item label="最小长度">
      <el-input-number
        v-model="config.props.minLength"
        :step="1"
        step-strictly
        style="width: 100%"
        controls-position="right"
        :min="1"
        :max="100"
      />
    </el-form-item>
    <el-form-item label="最大长度">
      <el-input-number
        v-model="config.props.maxLength"
        :step="1"
        step-strictly
        style="width: 100%"
        controls-position="right"
        :min="1"
        :max="100"
      />
    </el-form-item>
    <el-form-item label="正则表达式">
      <el-input v-model="config.props.regex" placeholder="^\d+$" />
    </el-form-item>
    <el-form-item label="正则表达式提示语">
      <el-input v-model="config.props.regexDesc" placeholder="表单值不符合正则表达式" />
    </el-form-item>
    <el-form-item label="默认值">
      <el-input v-model="config.props.value" />
    </el-form-item>
  </div>
</template>

<style scoped lang="scss"></style>
