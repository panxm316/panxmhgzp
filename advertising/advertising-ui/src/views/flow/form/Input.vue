<template>
  <div>
    <el-input v-if="mode === 'D'" :disabled="true" :placeholder="!form ? '' : form.placeholder" />
    <el-input
      v-else
      v-model="form.props.value"
      :disabled="form.perm === 'R'"
      :placeholder="form.placeholder"
    />
  </div>
</template>
<script lang="ts" setup>
import { isBlank, isNotBlank } from '@/utils/index'

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
  var item = props.form
  var itemProps = item.props
  var checkConfig = (rule: any, value: any, callback: any) => {
    if (item.required) {
      if (isBlank(value)) {
        return callback(new Error('请填写' + item.name))
      }
    }
    if (isBlank(value)) {
      return callback()
    }

    if (itemProps.minLength) {
      if (value.length < itemProps.minLength) {
        return callback(new Error('长度不能小于' + itemProps.minLength))
      }
    }

    if (itemProps.maxLength) {
      if (value.length > itemProps.maxLength) {
        return callback(new Error('长度不能大于' + itemProps.maxLength))
      }
    }
    if (isNotBlank(itemProps.regex)) {
      const regExp = new RegExp(itemProps.regex)
      if (!regExp.test(value)) {
        return callback(new Error(itemProps.regexDesc))
      }
    }
    return callback()
  }
  const ruleArray = [
    {
      validator: checkConfig,
      trigger: 'blur'
    }
  ]
  if (item.required) {
    ruleArray.push({ required: true, message: '请填写' + item.name, trigger: 'blur' })
  }
  return ruleArray
}
defineExpose({ getValidateRule })
</script>
<style scoped lang="scss"></style>
