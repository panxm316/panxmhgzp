<!--
 * @Author: songly
 * @Date: 2023-10-17 10:53:32
 * @LastEditTime: 2023-10-18 11:08:19
 * @LastEditors: songly
 * @Description:
-->
<template>
  <div>
    <el-button v-if="mode === 'D'" :disabled="true" circle :icon="Plus"></el-button>

    <select-show
      v-else
      v-model:orgList="form.props.value"
      :disabled="form.perm === 'R'"
      type="dept"
      :multiple="form.props.multi"
    ></select-show>
  </div>
</template>
<script lang="ts" setup>
import selectShow from '@/views/flow/workflow/components/dialog/selectAndShow.vue'
import { Plus } from '@element-plus/icons-vue'

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

// const { proxy } = getCurrentInstance()
const getValidateRule = () => {
  var item = props.form

  var checkConfig = (rule: any, value: any, callback: any) => {
    if (item.required) {
      if (value.length === 0) {
        return callback(new Error('请选择' + item.name))
      }
    }
    if (value.length === 0) {
      return callback()
    }
    return callback()
  }
  const ruleArray = [
    {
      required: false,
      message: '',
      trigger: 'change'
    }
  ]

  if (item.required) {
    ruleArray.push({ required: true, message: '请选择' + item.name, trigger: 'change' })
  }
  return ruleArray
}
defineExpose({ getValidateRule })
</script>
<style scoped lang="scss"></style>
