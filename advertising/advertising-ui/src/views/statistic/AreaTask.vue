<!--
 * @Author: lhl
 * @Date: 2024-02-2
 * @Description: 部门、区域完成情况表
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
      <el-tab-pane v-if="analysisName !== ''" :label="analysisName" name="ReceivableAnalysisName">
        <ReceivableAnalysis
          v-if="activeName === 'ReceivableAnalysisName'"
          :typename="analysisName"
          :tableheight="tableHeight"
        ></ReceivableAnalysis>
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
ReceivableAnalysis
import ReceivableAnalysis from './ReceivableAnalysis.vue'
import Department from './DeptReceivable.vue'
import MediaReport from './AreaReceivable.vue'
import DepartmentMedia from './AreaIncomeReceivable.vue'
import ReportSetup from './ReportSetup.vue'
import { computTableHeight } from '@/utils/index'
const activeName = ref('department')
const departmentName = ref('部门完成统计表')
const mediaName = ref('区域完成统计表')
const dmName = ref('区域应收统计表')
const analysisName = ref('广告应收分析')
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
