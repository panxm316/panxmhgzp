<!--
 * @Author: lhl
 * @Date: 2024-01-30
 * @Description: 区域实收报表
-->
<template>
  <div class="app-container">
    <el-tabs v-model="activeName" class="demo-tabs">
      <el-tab-pane v-if="departmentName !== ''" :label="departmentName" name="department">
        <Department
          v-if="activeName === 'department'"
          :typename="departmentName"
          :tableheight="tableHeight"
        ></Department>
      </el-tab-pane>
      <el-tab-pane v-if="mediaName !== ''" :label="mediaName" name="media">
        <MediaReport
          v-if="activeName === 'media'"
          :typename="mediaName"
          :tableheight="tableHeight"
        ></MediaReport>
      </el-tab-pane>
      <el-tab-pane v-if="dmName !== ''" :label="dmName" name="dmreport">
        <DepartmentMedia
          v-if="activeName === 'dmreport'"
          :typename="dmName"
          :tableheight="tableHeight"
        ></DepartmentMedia>
      </el-tab-pane>
      <el-tab-pane v-if="setupName !== ''" :label="setupName" name="setupreport">
        <ReportSetup
          v-if="activeName === 'setupreport'"
          :typename="setupName"
          :tableheight="tableHeight"
        ></ReportSetup>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script setup lang="ts">
import Department from './AreaIncomeCombin.vue'
import MediaReport from './AreaIncomeOp.vue'
import DepartmentMedia from './AreaIncomeAd.vue'
import ReportSetup from './ReportSetup.vue'
import { computTableHeight } from '@/utils/index'
const activeName = ref('department')
const departmentName = ref('合并(广告+经营)')
const mediaName = ref('日报经营')
const dmName = ref('广告公司')
const setupName = ref('报表配置')
const tableHeight = ref(0)
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableHeight.value = computTableHeight()
})
</script>
<style lang="scss" scoped></style>
