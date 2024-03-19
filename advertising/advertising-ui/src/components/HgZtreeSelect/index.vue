<!--
 * @Author: caogd
 * @Date: 2023-08-28 17:14:13
 * @LastEditTime: 2024-03-12 15:10:39
 * @LastEditors: yanz
 * @Description: 树下拉
-->
<template>
  <el-popover
    placement="bottom"
    :width="300"
    :visible="popoverVisible"
    @show="popovershow"
    @hide="popoverhide"
  >
    <template #reference>
      <el-input
        v-model="selectNames"
        :placeholder="placeholder ?? '请选择'"
        readonly
        :style="props.style"
        @click="popoverVisible = !popoverVisible"
      >
        <template v-if="props.clearable" #suffix>
          <el-icon class="el-input__icon el-input__clear" @click="onDeptClose">
            <CircleClose />
          </el-icon>
        </template>
      </el-input>
    </template>
    <div v-click-outside="onClickOutside">
      <HgZtree
        :filterable="props.filterable"
        :selectids="ztreeSelectIds"
        :checkboxtype="props.checkboxtype"
        :operate="props.operate"
        :scopeflag="props.scopeflag"
        :treeheight="props.treeheight"
        :treeparams="props.treeparams"
        @selectionztree="onSelectionZtree"
      ></HgZtree>
    </div>
  </el-popover>
</template>
<script setup lang="ts">
defineOptions({
  name: 'HgZtreeSelect'
})
import type { THgZtreeProps } from '@/types/components/hgztree'
import type { TSelectZtree } from '@/types/common/index'
import { CSSProperties } from 'vue'
import { ClickOutside as vClickOutside } from 'element-plus'
/**
 * 下拉传入类型 labelname input显示的名字,分隔 只显示不做其他对应
 */
type THgSelectZtreeProps = THgZtreeProps & {
  style?: CSSProperties
  clearable?: boolean
  placeholder?: string // 提示语
}
const props = defineProps<THgSelectZtreeProps>()

/** 导出返回数据 */
const emit = defineEmits<{
  selectionztree: [data: TSelectZtree[]]
}>()
const ztreeSelectIds = ref(props.selectids)

watch(
  () => props.selectids,
  (newValue, oldValue) => {
    nextTick(() => {
      ztreeSelectIds.value = newValue
    })
  }
)
/** 下拉显示隐藏 */
const popoverVisible = ref(false)
/**
 * 显示名称 多选，分隔
 */
const selectNames = ref('')
/**
 * 树选择返回值
 * @param val
 */
const onSelectionZtree = (val: TSelectZtree[]) => {
  if (val) {
    selectNames.value = val.map((item) => item.name).join(',')
  } else {
    selectNames.value = ''
  }
  emit('selectionztree', val)
  if (val.length > 0) {
    const originalValue = ztreeSelectIds.value!.slice()
    const selectIds = val.map((item) => item.id)
    if (JSON.stringify(selectIds) !== JSON.stringify(originalValue)) {
      popoverVisible.value = false
    }
  }
}
/**
 * @description: 清空
 * @return {*}
 */
const onDeptClose = (event: Event) => {
  ztreeSelectIds.value = []
  event.stopPropagation()
}
/**
 * @description: 下拉显示时
 * @return {*}
 */
const popovershow = () => {
  popoverVisible.value = true
}
/**
 * @description: 下拉隐藏时
 * @return {*}
 */
const popoverhide = () => {
  popoverVisible.value = false
}
/**
 * @description: 点击外部隐藏
 * @return {*}
 */
const onClickOutside = () => {
  // console.log('onClickOutside')
  if (popoverVisible.value) {
    popoverVisible.value = false
  }
}
</script>
<style scoped lang="scss"></style>
