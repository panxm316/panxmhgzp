<template>
  <div>
    <el-input-number
      v-if="mode === 'D'"
      style="width: 100%"
      controls-position="right"
      :disabled="true"
      :placeholder="!form ? '' : form.placeholder"
    />
    <el-input-number
      v-else
      v-model="form.props.value"
      style="width: 100%"
      controls-position="right"
      :precision="form.props.radixNum"
      :disabled="form.perm === 'R'"
      :placeholder="form.placeholder"
    />
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
import { getNumberRadixNum } from '@/utils/index'
const getValidateRule = () => {
  var item = props.form
  var itemProps = item.props
  var checkConfig = (rule: any, value: any, callback: any) => {
    if (item.required) {
      if (!value) {
        return callback(new Error('请填写' + item.name))
      }
    }
    if (!value) {
      return callback()
    }

    if (itemProps.radixNum) {
      if (getNumberRadixNum(value) > itemProps.radixNum) {
        return callback(new Error('小数位数不能大于' + itemProps.radixNum))
      }
    }

    if (itemProps.min) {
      if (value < itemProps.min) {
        return callback(new Error('数值不能小于' + itemProps.min))
      }
    }

    if (itemProps.max) {
      if (value > itemProps.max) {
        return callback(new Error('数值不能大于' + itemProps.max))
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
