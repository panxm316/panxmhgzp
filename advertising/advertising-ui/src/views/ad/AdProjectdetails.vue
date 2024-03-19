<!--
 * @Author: wanghl
 * @Date: 2024-02-22 15:56:36
 * @LastEditTime: 2024-03-12 12:53:49
 * @LastEditors: wanghl
 * @Description: 项目详细组件
-->

<template>
  <div>
    <!-- 客户工作报告组件 -->
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
            <el-form-item label="项目编码" prop="projectcode" v-show="adprojectData.sname !== ''">
              <el-input v-model="adprojectData.projectcode" disabled />
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
            <el-form-item label="项目预算" prop="nprojectbudget">
              <el-input-number
                v-model="adprojectData.nprojectbudget"
                :precision="2"
                :min="1"
                style="width: 220px"
                controls-position="right"
                disabled
              />
            </el-form-item>
            <el-form-item label="">
              <el-text> 大写： </el-text>
              <el-text type="info">
                {{ convertCurrency(adprojectData.nprojectbudget) }}
              </el-text>
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

            <el-form-item label="项目成本" prop="nprojectbudget">
              <el-input-number
                v-model="adprojectData.nprojectcost"
                :precision="2"
                :min="1"
                style="width: 220px"
                controls-position="right"
                disabled
              />
            </el-form-item>
            <el-form-item label="">
              <el-text> 大写： </el-text>
              <el-text type="info">
                {{ convertCurrency(adprojectData.nprojectcost) }}
              </el-text>
            </el-form-item>
            <!-- <el-form-item label="结项" prop="bprojectcomplete">
              <el-checkbox v-model="adprojectData.bprojectcomplete"></el-checkbox>
            </el-form-item> -->
            <!-- <el-form-item v-show="!addFlag" label="审核状态">
              <el-radio-group v-model="adprojectData.iapprovestatus" disabled>
                <el-radio :label="0">待审</el-radio>
                <el-radio :label="1">在审</el-radio>
                <el-radio :label="2">通过</el-radio>
                <el-radio :label="3">否决</el-radio>
                <el-radio :label="4">撤销</el-radio>
                <el-radio :label="5">无效</el-radio>
              </el-radio-group>
            </el-form-item> -->
            <el-form-item label="项目说明" prop="sprojectcontent">
              <el-input v-model="adprojectData.sprojectcontent" disabled />
            </el-form-item>

            <el-form-item prop="sremark" label="备注">
              <el-input v-model="adprojectData.sremark" disabled />
            </el-form-item>
            <!-- <el-form-item label="项目资料" prop="sprojectcontent">
              <HgFileUpload
                :server="hgfileuploadparam.server"
                :accept="hgfileuploadparam.accept"
                :multiple="true"
                :storytypes="hgfileuploadparam.storytypes"
                @getupfile="getUpFile"
              ></HgFileUpload>
            </el-form-item> -->
            <el-form-item label="是否需要审核" prop="sprojectcontent">
              <el-checkbox v-model="adprojectData.iapprovestatus" disabled />
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
import { getByAdProjectIdApi } from '@/api/ad/adproject'
const dialogVisible = ref(false)
import { defineExpose } from 'vue'
/** 根据合同号获取订单详情 */
const view = (row: string) => {
  console.log(row)
  dialogVisible.value = true
  getByAdProjectIdApi(row).then((res) => {
    console.log(res)
    adprojectData.value = res.obj
  })
}
defineExpose({ view })
import type { TAdProject } from '@/types/views/ad/adproject'
/** 广告项目 */
const adprojectData = ref<TAdProject>({
  id: undefined,
  sname: '',
  projectcode: '',
  dstartdate: '',
  denddate: '',
  createempid: '',
  screatename: '',
  dcreatedate: '',
  sprojectcontent: '',
  nprojectbudget: 0,
  nprojectcost: 0,
  bprojectcomplete: 0,
  iapprovestatus: 0,
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
