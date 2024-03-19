<!--
 * @Author: yanz
 * @Date: 2023-12-09 10:36:30
 * @LastEditors: wanghl
 * @LastEditTime: 2024-01-17 13:42:43
 * @Description:预开发票申请流程信息
 *
-->

<template>
  <!-- <div class="app-container" style="padding: 20px 0 0 60px; background-color: rgb(249, 252, 254)"> -->
  <el-form ref="preinvoApplyRef" :model="preinvoApplyForm" label-width="120px">
    <el-row :gutter="10">
      <el-col :span="12">
        <el-form-item label="业务员" prop="employname">
          {{ preinvoApplyForm.employname }}
        </el-form-item>
        <el-form-item label="开票名称" sortable="custom" min-width="150">
          {{ preinvoApplyForm.sprintname }}
        </el-form-item>
        <el-form-item label="开票项目">
          {{ preinvoApplyForm.printitemname }}
        </el-form-item>
        <el-form-item label="经营主体">
          {{ preinvoApplyForm.businessentityname }}
        </el-form-item>
        <el-form-item label="开票金额">
          {{ formatMoney(preinvoApplyForm.namountapply, '2') }}元
        </el-form-item>
        <el-form-item label="申请类型">
          {{ preinvoApplyForm.iapplytype === 1 ? '预开' : '挂开' }}
        </el-form-item>
        <el-form-item label="开票类型">
          <!-- {{ preinvoApplyForm.iinvoicetype. === 81 ? '数电专票' : '数电普票' }} -->>
          {{
            preinvoApplyForm.iinvoicetype !== undefined
              ? EPreinvoiceStyle[preinvoApplyForm.iinvoicetype]
              : ''
          }}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="项目名称">
          {{ preinvoApplyForm.adprojectname }}
        </el-form-item>
        <el-form-item label="客户名称">
          {{ preinvoApplyForm.customername }}
        </el-form-item>
        <el-form-item label="客户识别号">
          {{ preinvoApplyForm.spayercreditcode }}
        </el-form-item>
        <el-form-item label="客户地址电话">
          {{ preinvoApplyForm.spayeraddr }}
        </el-form-item>
        <el-form-item label="客户银行及账户">
          {{ preinvoApplyForm.spayerbank }}
        </el-form-item>
        <el-form-item label="申请日期">
          {{ preinvoApplyForm.dapplytime }}
        </el-form-item>
        <el-form-item label="备注">
          {{ preinvoApplyForm.sremark }}
        </el-form-item>
      </el-col>
    </el-row>

    <el-form-item label="关联合同"></el-form-item>
    <el-table
      :data="preinvoApplyForm.contractVos"
      row-key="id"
      :border="true"
      stripe
      style="width: 90%; margin-left: 50px"
    >
      <el-table-column
        label="合同编号"
        sortable="custom"
        width="120"
        prop="scontractnum"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        label="广告标题"
        sortable="custom"
        min-width="150"
        prop="sadtitle"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column label="广告分类" width="100" prop="adtypename"> </el-table-column>
      <el-table-column
        label="欠款金额"
        sortable="custom"
        min-width="150"
        prop="amountarrearage"
        align="right"
      >
        <template #default="scope">
          <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="广告行业"
        sortable="custom"
        width="120"
        prop="adindustryname"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column label="经营主体" min-width="160" prop="businessentityname">
      </el-table-column>
      <el-table-column
        label="客户名称"
        sortable="custom"
        min-width="150"
        prop="customername"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        label="代理公司名称"
        sortable="custom"
        min-width="150"
        prop="agencyname"
        show-overflow-tooltip
      >
      </el-table-column>
      <el-table-column
        label="内容生产方名称"
        sortable="custom"
        min-width="150"
        prop="agentname"
        show-overflow-tooltip
      >
      </el-table-column>
    </el-table>

    <el-form-item label="相关材料" style="margin-top: 10px">
      <el-row style="width: 100%">
        <el-col :span="24">
          <div v-for="file in preinvoApplyForm.applyfiles" :key="file.sfileid">
            <el-link
              v-if="file.sfiletype === 'Office'"
              type="primary"
              :underline="false"
              @click="previewFile(file)"
            >
              <el-icon><Document /></el-icon> {{ file.soriginalfile }}</el-link
            >
            <img
              v-if="file.sfiletype === 'Photo'"
              style="width: 50%"
              :src="file.durl"
              @click="previewFile(file)"
            />
          </div>
        </el-col>
      </el-row>
    </el-form-item>
  </el-form>
  <!-- </div> -->
</template>

<script setup lang="ts">
import { defineExpose } from 'vue'
import { deepClone, formatMoney } from '@/utils'
import { IAxios } from 'axios'
import { FormInstance, FormRules } from 'element-plus'
import { getPreinvoiceapplicationByIdApi } from '@/api/business/preinvoiceapplication'
import type {
  IPreInvoiceApplicationDTO,
  IPreInvoiceApplicationVO,
  IPreInvoiceApplicationFile
} from '@/types/views/business/preinvoiceapplication'
import { EPreinvoiceStyle } from '@/types/common/enumindex'
type THSelectPreInvoiceApplication = {
  selfdetailid: string
}
const global = getCurrentInstance()?.appContext.config.globalProperties
const props = withDefaults(defineProps<THSelectPreInvoiceApplication>(), {
  selfdetailid: ''
})
/** 申请ID */
const preinvoApplyId = ref(props.selfdetailid)
/** Form名称 */
const preinvoApplyForm = ref<IPreInvoiceApplicationDTO>({
  namountapply: 0 // Add the missing property with its appropriate value
})
/**
 * 获取开票类型
 */
const preinvoiceStyleCombo = ref()
const filteredEPreinvoiceStyle = computed(() => {
  return Object.entries(EPreinvoiceStyle)
    .filter(([name, value]) => typeof value === 'number')
    .reduce((acc, [name, value]) => ({ ...acc, [name]: value }), {})
})
/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: IPreInvoiceApplicationFile) => {
  console.log('file', file)
  if (file.sfiletype === 'Photo') {
    window.open(file.durl)
  } else if (file.sfiletype === 'Office') {
    const url = `${global?.officePreviewUrl}?src=${file.durl}`
    window.open(url)
  }
}
/**
 * 查看申请详情
 */
const onDetail = () => {
  // preinvoApplyForm.value = { ...row }
  getPreinvoiceapplicationByIdApi(preinvoApplyId.value).then(
    ({ success, obj }: IAxios<IPreInvoiceApplicationVO>) => {
      if (success) {
        preinvoApplyForm.value = deepClone(obj) as IPreInvoiceApplicationVO
        // preinvoApplyForm.value = obj
        console.log(preinvoApplyForm.value)
      }
    }
  )
}
onMounted(() => {
  onDetail()
  console.log('preinvoApplyForm.value', preinvoApplyForm.value)
})
/**
 * 监听传入id
 */
watch(
  () => props.selfdetailid,
  (newValue, oldValue) => {
    console.log('newValue', newValue)
    if (newValue !== '') {
      preinvoApplyId.value = newValue
      onDetail()
    }
  }
)
// defineExpose({
//   previewFile,
//   preinvoiceStyleCombo
// })
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
.el-form-item {
  margin-bottom: 15px;
}
</style>
