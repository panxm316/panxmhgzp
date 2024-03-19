<!--
 * @Author: suny
 * @Date: 2023-10-17 10:53:29
 * @LastEditTime: 2023-10-18 13:21:00
 * @LastEditors: suny
 * @Description: 查看详情
-->

<template>
  <el-form ref="ruleFormRef" label-position="top" :rules="rules" :model="formValue">
    <template v-for="item in formList">
      <el-form-item
        v-if="item.perm != 'H'"
        :key="item.id"
        :label="item.name + (item.props.unit ? '(' + item.props.unit + ')' : '')"
        :prop="!item ? '' : item.id"
        :required="!item ? false : item.required"
      >
        <component
          :is="getFormWidget(item.type)"
          :ref="'form' + item.id"
          style="width: 100%"
          mode="RUN"
          :form="item"
          @addLayoutOneItem="addLayoutOneItem"
          @deleteLayoutOneItem="deleteLayoutOneItem"
        ></component>
      </el-form-item>
    </template>
  </el-form>
</template>

<script setup lang="ts">
import getFormName from '@/views/flow/utils/getFormWidget'
import { onMounted, reactive, ref } from 'vue'
import type { FormRules } from 'element-plus'
import { IFormItemVO } from '@/types/views/task/start'

const getFormWidget = (name: string) => {
  // 写的时候，组件的起名一定要与dragList中的element名字一模一样，不然会映射不上
  return getFormName[name]
}
const { proxy } = getCurrentInstance() as any

const props = defineProps({
  formList: {
    type: Object,
    default: () => {}
  }
})

onMounted(() => {
  const formList = props.formList as IFormItemVO[]
  for (var item of formList) {
    const id = item.id
    if (proxy.$refs['form' + id]?.length > 0) {
      const validateRule = proxy.$refs['form' + id][0].getValidateRule()
      rules[id] = validateRule
    }
  }
})
const emit = defineEmits(['addLayoutOneItem', 'deleteLayoutOneItem'])

const addLayoutOneItem = (id: string) => {
  emit('addLayoutOneItem', id)
}
const deleteLayoutOneItem = (id: string, index: number) => {
  emit('deleteLayoutOneItem', id, index)
}

const ruleFormRef = ref()

const rules = reactive<FormRules>({})

const validate = (f: any) => {
  ruleFormRef.value.validate((valid: any) => {
    f(valid)
  })
}
defineExpose({ validate })

const formValue = computed(() => {
  var obj = {}
  for (var item of props.formList as IFormItemVO[]) {
    obj[item.id] = item.props.value
    if (item.type === 'Layout') {
      const subList = item.props.value

      var d = []
      for (var array of subList) {
        var v = {}

        for (var subItem of array) {
          const value = subItem.props.value
          v[subItem.id] = value
        }
        d.push(v)
      }
      obj[item.id] = d
    }
  }
  return obj
})
</script>
<style scoped lang="scss"></style>
