<!--
 * @Author: caogd
 * @Date: 2023-10-23 09:07:03
 * @LastEditTime: 2023-10-24 11:12:34
 * @LastEditors: caogd
 * @Description: 自定义条件组件
-->
<template>
  <div>
    <el-card class="box-card" style="margin-bottom: 20px">
      <!-- <template #header>
        <div class="card-header">
          <span>条件组{{ index + 1 }}</span>
        </div>
      </template> -->
      <template v-if="conditionConfigList.length > 0">
        <div v-for="(item1, index1) in conditionConfigList" :key="index1">
          <div style="display: flex; flex-direction: row; justify-content: space-between">
            <div>
              {{ index1 === 0 ? '当' : '且' }}
            </div>
            <div>
              <el-button
                v-if="conditionConfigList?.length > 1"
                text
                :icon="'Delete'"
                @click="deleteCondition(index1)"
              ></el-button>
            </div>
          </div>

          <condition :condition="item1" :condition-relation="true"></condition>
        </div>
      </template>
      <el-button dark type="success" style="margin-top: 20px" @click="addOneCondition()"
        >添加条件</el-button
      >
    </el-card>
  </div>
</template>
<script setup lang="ts">
import { useStore } from '@/views/flow/workflow/stores/index'
import Condition from '@/views/flow/workflow/components/drawer/components/condition.vue'
import { TCondition } from '@/types/views/flow'

defineOptions({
  name: 'HgCondition'
})
export type THGCondition = { hgcondition: TCondition[] }

const props = withDefaults(defineProps<THGCondition>(), {
  hgcondition: () => [
    {
      key: '',
      keyType: '',
      expression: '',
      value: [{}]
    }
  ]
})
const emits = defineEmits(['update:hgcondition'])

const conditionConfigList = ref(props.hgcondition)
watch(conditionConfigList.value, (val: any) => {
  emits('update:hgcondition', val)
})
const store = useStore()

// 刪除单个条件
const deleteCondition = (index1: number) => {
  conditionConfigList.value.splice(index1, 1)
}
// 添加组内一个条件
const addOneCondition = () => {
  conditionConfigList.value.push({
    key: '',
    keyType: '',
    expression: '',
    value: [{}]
  })
}
</script>
<style scoped lang="scss"></style>
