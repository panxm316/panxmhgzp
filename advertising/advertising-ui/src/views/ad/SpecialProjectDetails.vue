<!--
 * @Author: lhl
 * @Date: 2024-03-14
 * @Description: 特刊项目详细组件
-->

<template>
  <div>
    <!-- 特刊项目信息组件 -->
    <el-dialog
      v-model="dialogVisible"
      :title="'项目详情'"
      width="700"
      align-center
      :destroy-on-close="true"
      @close="handleDetailCancel"
    >
      <el-form ref="AdProjectFormRef" :model="adprojectData" label-width="150px">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="名称" prop="sname">
              <el-input v-model="adprojectData.sname" disabled />
            </el-form-item>
            <el-form-item label="序号" prop="isort">
              <el-input-number
                v-model="adprojectData.isort"
                :min="1"
                controls-position="right"
                style="width: 220px"
                disabled
              />
            </el-form-item>
            <el-form-item label="开始日期" prop="dstartdate">
              <el-date-picker
                v-model="adprojectData.dstartdate"
                :editable="false"
                :picker-options="pickerOptions"
                type="datetime"
                placeholder="选择日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                :clearable="false"
                disabled
              ></el-date-picker>
            </el-form-item>
            <el-form-item label="结束日期" prop="denddate">
              <el-date-picker
                v-model="adprojectData.denddate"
                :editable="false"
                :picker-options="pickerOptions"
                type="datetime"
                placeholder="选择日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                :clearable="false"
                disabled
              ></el-date-picker>
            </el-form-item>

            <el-form-item label="项目说明" prop="sprojectcontent">
              <el-input v-model="adprojectData.sprojectcontent" disabled />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {
  computTableHeight,
  formatDate,
  formatMoney,
  tableHeaderColor,
  fenbianlv,
  convertCurrency
} from '@/utils'
import { getBySpecialProjectIdApi } from '@/api/ad/specialproject'
const dialogVisible = ref(false)
import { defineExpose } from 'vue'
/** 根据合同号获取订单详情 */
const view = (row: string) => {
  console.log(row)
  dialogVisible.value = true
  getBySpecialProjectIdApi(row).then((res) => {
    console.log(res)
    adprojectData.value = res.obj
  })
}
defineExpose({ view })
import type { TSpecialProject } from '@/types/views/ad/specialproject'
/** 广告项目 */
const adprojectData = ref<TSpecialProject>({
  id: undefined,
  sname: '',
  dstartdate: '',
  denddate: '',
  createempid: '',
  screatename: '',
  dcreatedate: '',
  sprojectcontent: '',
  bprojectcomplete: 0,
  sremark: '',
  isort: 0
})
const pickerOptions = ref({
  disabledDate: function (time: Date) {
    return time.getTime() < Date.now() - 8.64e7
  }
})
/**
 * 关闭弹窗
 */
const handleDetailCancel = () => {
  dialogVisible.value = false
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.el-form-item {
  margin-bottom: 15px;
}
</style>
