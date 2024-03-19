<!--
 * @Author: caogd
 * @Date: 2023-09-02 15:31:22
 * @LastEditTime: 2023-09-04 12:49:08
 * @LastEditors: caogd
 * @Description: 多选组件（v-model绑定值string[]；data数据参数默认以id和name字段展示，可根据panelProps配置；panelProps:可自定义id值和name值）
-->
<template>
  <div class="el-transfer el-transfer-panel" style="width: 100%">
    <p class="el-transfer-panel__header">
      <el-checkbox
        v-model="panelCheckAll"
        :indeterminate="isIndeterminate"
        label="全选"
        size="large"
        @change="handleCheckAllChange"
      >
        <template #default>
          全选
          <span>{{ `${checkedCities.length}/${data.length}` }}</span>
        </template>
      </el-checkbox>
    </p>
    <el-scrollbar class="el-transfer-panel__body" style="margin-top: 10px">
      <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
        <el-checkbox
          v-for="item in props.data"
          :key="item[props.panelProps.id]"
          class="el-transfer-panel__item"
          :label="item[props.panelProps.id]"
          >{{ item[props.panelProps.name] }}</el-checkbox
        >
      </el-checkbox-group>
    </el-scrollbar>
  </div>
</template>
<script setup lang="ts" generic="T extends TPanelData">
import { CheckboxValueType } from 'element-plus'
/** 展示数据对象 */
export type TPanelData = {
  [key: string]: any
}
/** 传入参数 */
type TPanel = {
  /** 展示数据 */
  data: T[]
  /** 绑定数据 string[]类型 */
  modelValue: string[]
  /** 可自定义展示节点id和name */
  panelProps?: { id: string; name: string }
}
defineOptions({
  name: 'HgTransferPanel'
})

const props = withDefaults(defineProps<TPanel>(), {
  panelProps: () => {
    return { id: 'id', name: 'name' }
  }
})
const emit = defineEmits<{
  'update:modelValue': [data: string[]]
  /** 返回整个对象 */
  change: [data: T[]]
}>()
/** 监听绑定值 */
watch(
  () => props.modelValue,
  () => {
    checkedCities.value = props.modelValue
    isIndeterminate.value =
      checkedCities.value.length > 0 && checkedCities.value.length < props.data.length
  }
)
/** 全选按钮 */
const panelCheckAll = ref(false)
/** 半选状态 */
const isIndeterminate = ref(false)
/** 当前选中数据id */
const checkedCities = ref<string[]>([])
/**
 * @description: 全选勾选
 * @return {*}
 */
const handleCheckAllChange = () => {
  checkedCities.value = panelCheckAll.value
    ? props.data.map((item) => item[props.panelProps.id])
    : []
  isIndeterminate.value = false
  const checkedData = props.data.filter(
    (item) => checkedCities.value.indexOf(item[props.panelProps.id]) > -1
  )
  emit('update:modelValue', checkedCities.value)
  emit('change', checkedData)
}
/**
 * @description: 勾选数据
 * @param {*} value
 * @return {*}
 */
const handleCheckedCitiesChange = (value: CheckboxValueType[]) => {
  const checkedCount = value.length
  panelCheckAll.value = checkedCount === props.data.length
  isIndeterminate.value = checkedCount > 0 && checkedCount < props.data.length
  const checkedData = props.data.filter(
    (item) => checkedCities.value.indexOf(item[props.panelProps.id]) > -1
  )
  emit('update:modelValue', checkedCities.value)
  emit('change', checkedData)
}
</script>
<style scoped lang="scss"></style>
