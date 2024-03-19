<!--
 * @Author: suny
 * @Date: 2023-11-16 08:50:08
 * @LastEditTime: 2024-03-13 15:09:37
 * @LastEditors: suny
 * @Description: 广告资源预审详情查看
-->
<template>
  <div>
    <el-row>
      <el-col :span="showflag ? 16 : 24">
        <el-form
          ref="adResourceApplicationFormRef"
          :model="adResourceApplicationForm"
          label-width="140px"
          :disabled="true"
          class="demo-workReportForm"
          status-icon
        >
          <el-form-item label="客户类型">
            <template></template>
            <span>{{ ECustomerType[adResourceApplicationForm.icusttype as number] }}</span>
          </el-form-item>
          <el-form-item label="日期范围">
            <el-row :span="24">
              <el-col :span="11">
                <el-form-item prop="dstartdate">
                  <span style="width: 120px">{{
                    dayjs(adResourceApplicationForm.dstartdate).format('YYYY-MM-DD')
                  }}</span>
                </el-form-item>
              </el-col>
              <el-col class="text-center" :span="2">至</el-col>
              <el-col :span="11">
                <el-form-item prop="denddate">
                  <span style="width: 120px">{{
                    adResourceApplicationForm.denddate === null
                      ? ''
                      : dayjs(adResourceApplicationForm.denddate).format('YYYY-MM-DD')
                  }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="客户选择" prop="customerid">
            <el-row style="width: 100%">
              <el-col :span="23">
                <span>{{ adResourceApplicationForm.customername }}</span>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="广告标题" prop="sadtitle">
            <span>{{ adResourceApplicationForm.sadtitle }}</span>
          </el-form-item>
          <el-form-item label="广告内容" prop="sadcontent">
            <span>{{ adResourceApplicationForm.sadcontent }}</span>
          </el-form-item>
          <el-form-item label="广告文件">
            <el-row style="width: 100%">
              <el-col :span="24">
                <el-table
                  ref="fileTableRef"
                  style="width: 90%"
                  :data="adResourceApplicationForm.filelist"
                  highlight-current-row
                  border
                >
                  <el-table-column
                    label="类型"
                    prop="ifilecategory"
                    min-width="100"
                    width="120"
                    show-overflow-tooltip
                  >
                    <template #default="scope">
                      <span
                        v-if="scope.row.ifilecategory === EFileCategory.广告合同"
                        style="color: #409eff"
                        >广告合同</span
                      >
                      <span
                        v-if="scope.row.ifilecategory === EFileCategory.广告资源"
                        style="color: #67c23a"
                        >广告资源</span
                      >
                      <span
                        v-if="scope.row.ifilecategory === EFileCategory.证明"
                        style="color: #b7768c"
                        >证明</span
                      >
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="媒体"
                    prop="medianame"
                    min-width="100"
                    width="120"
                    show-overflow-tooltip
                  />
                  <el-table-column
                    label="文件名"
                    prop="soriginalfile"
                    min-width="100"
                    show-overflow-tooltip
                  >
                    <template #default="scope">
                      <el-link type="primary" :underline="false" @click="previewFile(scope.row)">
                        <el-icon><Document /></el-icon> {{ scope.row.soriginalfile }}</el-link
                      >
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="说明"
                    prop="sdescription"
                    min-width="100"
                    show-overflow-tooltip
                  />
                </el-table>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="快速上版" prop="bquickly">
            <el-checkbox v-model="adResourceApplicationForm.bquickly"></el-checkbox>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col v-if="showflag" :span="8">
        <flow-node-format
          ref="flowFormatRef"
          :disable-select="true"
          :form-data="currentData.formData"
          :process-instance-id="currentData.processInstanceId"
          :flow-id="currentData.flowId"
          :customer-process-instance="historyList"
        ></flow-node-format>
        <div
          v-if="historyList.length >= 2"
          style="width: 100%; text-align: left; padding-left: 50px"
        >
          <li v-for="(item, index) in historyList" :key="item.flowId">
            <el-button
              v-if="item.result === false"
              style="color: red"
              type="text"
              :class="{ activeOrder: index == isActiveOrder }"
              @click="Seelistdetail(item, index)"
              >{{ item.createTime }}提交审核</el-button
            >
            <el-button
              v-if="item.result === true"
              style="color: green"
              type="text"
              :class="{ activeOrder: index == isActiveOrder }"
              @click="Seelistdetail(item, index)"
              >{{ item.createTime }}提交审核</el-button
            >
            <el-button
              v-if="item.result === null"
              style="color: #409eff"
              type="text"
              :class="{ activeOrder: index == isActiveOrder }"
              @click="Seelistdetail(item, index)"
              >{{ item.createTime }}提交审核</el-button
            >
          </li>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import FlowNodeFormat from '@/components/Flow/FlowNodeFormatData.vue'
import { IAdResourceApplicationDTO } from '@/types/views/ad/adresourceapplication'
import { ref } from 'vue'
import { EApproveStatus, ECustomerType, EFileCategory } from '@/types/common/enumindex'
import { IAdResourceFilesDTO } from '@/types/views/ad/adresourcefiles'
import {
  getAdResourceApplicationByIdApi,
  getProcessInstanceByIdApi
} from '@/api/ad/adresourceapplication'
import { IAxios } from 'axios'
import { dayjs } from 'element-plus'
import { TypeCurrentData } from '@/types/components/hgindustry'

defineOptions({
  name: 'HgAdSourceDetail'
})

const props = defineProps({
  /** 申请表id */
  id: {
    type: String,
    default: ''
  },
  /** 显示审批列表状态 */
  showflag: {
    type: Boolean,
    default: false
  }
})

const global = getCurrentInstance()?.appContext.config.globalProperties
const adResourceApplicationInfo: IAdResourceApplicationDTO = {
  id: '',
  icusttype: ECustomerType.直接客户,
  deptid: '',
  deptname: '',
  employid: '',
  employname: '',
  dstartdate: '',
  denddate: '',
  sadtitle: '',
  sadcontent: '',
  sremark: '',
  customerid: '',
  customername: '',
  bquickly: false,
  iapprovestatus: EApproveStatus.待审,
  filelist: []
}
/** 编辑实体 */
const adResourceApplicationForm = ref<IAdResourceApplicationDTO>({ ...adResourceApplicationInfo })
/** 当前数据历史列表 */
const historyList = ref<TypeCurrentData[]>([])
/** 当前历史详情 */
const currentData = ref<TypeCurrentData>({
  flowId: '',
  processInstanceId: '',
  formData: {}
})
watch(
  () => props.id,
  (val) => {
    console.log(val)
    if (val) {
      getAdResourceApplicationInfo(val)
      if (props.showflag) {
        onHistory(val)
      }
    }
  }
)

onMounted(() => {
  const id = props.id
  console.log(id)
  if (id) {
    getAdResourceApplicationInfo(id)
    if (props.showflag) {
      onHistory(id)
    }
  }
})
/**
 * 获取广告资源预审详情
 * @param id
 */
const getAdResourceApplicationInfo = (id: string) => {
  const data = {
    id: id
  }
  getAdResourceApplicationByIdApi(data).then((res: IAxios) => {
    if (res.success) {
      adResourceApplicationForm.value = { ...res.obj }
    }
  })
}
/**
 * 查看历史按钮事件
 * @param row
 */
const onHistory = (id: string) => {
  const data = {
    id: id
  }
  getProcessInstanceByIdApi(data).then((res: IAxios<TypeCurrentData[]>) => {
    if (res.success) {
      if (res.obj.length > 0) {
        historyList.value = res.obj
        Seelistdetail(historyList.value[0])
      } else {
        /**
         * 提示没有审核历史
         */
        global?.$message({
          message: '没有审核历史,审核自动通过！',
          type: 'warning'
        })
      }
    }
  })
}
/**
 * 查看每条历史详情
 * @param val
 */
const Seelistdetail = (val: TypeCurrentData, number?: number) => {
  isActiveOrder.value = number as number

  currentData.value.processInstanceId = val.processInstanceId
  currentData.value.flowId = val.flowId
  currentData.value.formData = val.formData
}
/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: IAdResourceFilesDTO) => {
  console.log(file)
  if (file.sfiletype === 'Office') {
    const url = `${global?.officePreviewUrl}?src=${file.durl}`
    window.open(url)
  } else {
    window.open(file.durl)
  }
}
const isActiveOrder = ref(0)
</script>

<style lang="scss" scoped>
.active {
  padding: 10px;
  font-size: 16px;
}
.activeOrder {
  padding: 10px;
  font-size: 16px;
}
</style>
