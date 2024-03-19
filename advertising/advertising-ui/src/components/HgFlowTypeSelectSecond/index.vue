<!--
 * @Author: wanghl
 * @Date: 2023-10-26 10:11:02
 * @LastEditTime: 2024-03-12 18:07:02
 * @LastEditors: wanghl
 * @Description:获取审批流程组件
-->
<template>
  <div>
    <el-dialog
      v-model="dialogVisible"
      title="提交审批"
      width="500"
      align-center
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <el-select
        v-model="FlowType.id"
        placeholder="请选择审核流程"
        clearable
        style="width: 80%; margin-left: 10%; margin-bottom: 20px"
        @change="getFlowPageList()"
      >
        <el-option
          v-for="item in flowTypeList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { getFlowlistComboByFlowTypeApi } from '@/api/flow/index'
import { Tadindustrylist } from '@/types/components/hgindustry'
import { EApproveStatus, ECustomerType, EFlowType } from '@/types/common/enumindex'
import { getCustomerByIdApi } from '@/api/customer/index'
import type { IAxios } from 'axios'
defineOptions({
  name: 'HgFlowTypeSelectSecond'
})
const emit = defineEmits<{
  selecflowtype: [data: Tadindustrylist]
}>()
const dialogVisible = ref(false)
/** 流程类型列表 */
const flowTypeList = ref<Tadindustrylist[]>([])
import { defineExpose } from 'vue'
/** 根据合同号获取订单详情 */
const view = (row: string) => {
  dialogVisible.value = true
  getFlowlistComboByFlowTypeApi(row).then((res: IAxios<Tadindustrylist[]>) => {
    console.log('res', res)
    if (res.success) {
      flowTypeList.value = res.obj
    }
  })
}
defineExpose({ view })
/** 流程数据 */
const FlowType = ref<Tadindustrylist>({
  id: '',
  name: ''
})

/**
 * 获取流程对象
 */
const getFlowPageList = () => {
  emit('selecflowtype', FlowType.value)
  dialogVisible.value = false
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.showSearch span {
  color: #606266;
  font-size: 14px;
}
:deep(.el-form-item__label) {
  font-weight: bold !important;
}
</style>
