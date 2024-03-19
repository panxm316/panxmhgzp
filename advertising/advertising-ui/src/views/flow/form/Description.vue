<template>
  <div>
    <text>{{ !form ? '' : form.placeholder }}</text>
  </div>
</template>
<script lang="ts" setup>
const props = defineProps({
  mode: {
    type: String,
    default: 'D'
  },

  form: {
    type: Object,
    default: () => {}
  }
})
const { proxy } = getCurrentInstance()
const getValidateRule = () => {
  var checkConfig = (rule: any, value: any, callback: any) => {
    return callback()
  }
  const ruleArray = [
    {
      validator: checkConfig,
      trigger: 'blur'
    }
  ]

  return ruleArray
}
defineExpose({ getValidateRule })
</script>
<style scoped lang="scss"></style>
