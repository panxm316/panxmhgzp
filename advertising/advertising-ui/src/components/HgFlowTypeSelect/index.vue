<!--
 * @Author: wanghl
 * @Date: 2023-10-26 10:11:02
 * @LastEditTime: 2023-12-12 10:07:30
 * @LastEditors: wanghl
 * @Description:查看客户详细信息
-->
<template>
  <div>
    <el-select
      v-model="FlowType.id"
      placeholder="请选择审核流程"
      clearable
      style="width: 100%"
      @change="getFlowPageList()"
      @clear="loaddata"
    >
      <el-option v-for="item in FlowTypeList" :key="item.id" :label="item.name" :value="item.id" />
    </el-select>
  </div>
</template>

<script setup lang="ts">
import { getFlowlistComboByFlowTypeApi } from '@/api/flow/index'
import { Tadindustrylist } from '@/types/components/hgindustry'
import { getCustomerByIdApi } from '@/api/customer/index'
import type { IAxios } from 'axios'
defineOptions({
  name: 'HgFlowTypeSelect'
})
type THSelectCustomer = {
  /**
   * 传入流程类型名称
   */
  flowtypename?: string
  /**
   * 流程id时参数变化
   */
  flowid?: string
}
const props = withDefaults(defineProps<THSelectCustomer>(), {
  flowtypename: '',
  flowid: ''
})
const emit = defineEmits<{
  selecflowtype: [data: Tadindustrylist]
}>()

/** 流程类型列表 */
const FlowTypeList = ref<Tadindustrylist[]>([])
/** 流程数据 */
const FlowType = ref<Tadindustrylist>({
  id: '',
  name: ''
})

onMounted(() => {
  loaddata()
})
/**
 * 监听传入流程类型变化加载页面
 */
watch(
  () => props.flowtypename,
  (newValue, oldValue) => {
    loaddata()
  }
)
/**
 * 监听传入流程id变化加载页面
 */
watch(
  () => props.flowid,
  (newValue, oldValue) => {
    FlowType.value.id = newValue
    loaddata()
  }
)
/**
 * 获取客户列表
 */
const loaddata = () => {
  getFlowlistComboByFlowTypeApi(props.flowtypename).then(
    ({ obj, total }: IAxios<Tadindustrylist[]>) => {
      FlowTypeList.value = obj
    }
  )
}
/**
 * 获取流程对象
 */
const getFlowPageList = () => {
  emit('selecflowtype', FlowType.value)
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
