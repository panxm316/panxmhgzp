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
const { proxy } = getCurrentInstance()

const placeHolder = computed(() => {
  return config.value.placeholder
})

watch(
  () => placeHolder.value,
  (f) => {
    config.value.props.value = f
  },
  { deep: true }
)
</script>

<template>
  <div></div>
</template>

<style scoped lang="scss"></style>
