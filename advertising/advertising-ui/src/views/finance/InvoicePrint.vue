<!--
 * @Author: suny
 * @Date: 2023-12-19 09:50:44
 * @LastEditTime: 2024-01-17 09:48:23
 * @LastEditors: wanghl
 * @Description: 预开发票打印
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="preInvoiceTableRef"
        :data="preInvoiceList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <el-table-column label="部门" prop="deptname" min-width="150" show-overflow-tooltip />
        <el-table-column label="业务员" prop="employname" min-width="100" show-overflow-tooltip />
        <el-table-column label="申请日期" prop="dapplytime" min-width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dapplytime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="客户名称"
          prop="customername"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column label="客户属性" prop="icusttype" min-width="100" show-overflow-tooltip>
          <template #default="scope">
            <span v-if="scope.row.icusttype == ECustomerType.直接客户"> 直接客户 </span>
            <span v-if="scope.row.icusttype == ECustomerType.代理公司"> 代理公司 </span>
            <span v-if="scope.row.icusttype == ECustomerType.内容生产方"> 内容生产方 </span>
          </template>
        </el-table-column>
        <el-table-column label="申请金额" prop="namountapply" width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="经营主体"
          prop="businessentityname"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column
          label="开票项目"
          prop="printitemname"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column label="发票类型" prop="iinvoicetype" width="100" :align="'center'">
          <template #default="scope">
            <span v-if="scope.row.iinvoicetype == EPreinvoiceStyle.数电专票"> 数电专票 </span>
            <span v-if="scope.row.iinvoicetype == EPreinvoiceStyle.数电普票"> 数电普票 </span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          :align="'left'"
          width="180"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              title="详情"
              type="primary"
              size="small"
              icon="View"
              @click="onDetail(scope.row)"
              >详情</el-button
            >
            <el-button
              v-if="!scope.row.invoice"
              link
              type="primary"
              icon="Printer"
              size="small"
              title="打印"
              @click="handlePrint(scope.row)"
              >打印</el-button
            >
            <el-button
              v-if="!scope.row.invoice"
              link
              type="warning"
              icon="Back"
              size="small"
              title="退回"
              @click="handleBack(scope.row)"
              >退回</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryParams.page"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryParams.pageSize"
        small
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- 查看详情 -->
    <el-dialog
      v-model="dialogDetailshow"
      title="查看详情"
      width="1000"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <HgPreInvoiceApplicationDetail
        :selfdetailid="curPreInvioce!.id"
      ></HgPreInvoiceApplicationDetail>
    </el-dialog>
    <!-- 打印 -->
    <el-dialog
      v-model="dialogPrintshow"
      title="发票打印"
      width="1100"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <HgInvoicePrint :invoiceid="invoiceID"></HgInvoicePrint>
    </el-dialog>
    <!-- 退回 -->
    <el-dialog
      v-model="dialogBackshow"
      title="退回意见"
      width="800"
      append-to-body
      destroy-on-close
      draggable
      :close-on-click-modal="false"
    >
      <el-row>
        <el-col :span="24">
          <el-form label-width="100px" label-position="right">
            <el-form-item label="退回原因">
              <el-input
                v-model="backreason"
                type="textarea"
                :rows="4"
                maxlength="200"
                show-word-limit
                placeholder="请输入退回原因"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <div style="position: absolute; right: 5px">
                <el-button type="primary" icon="Check" @click="saveBackForm">退回</el-button>
                <el-button icon="Close" @click="dialogBackshow = false">取消</el-button>
              </div>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import HgInvoicePrint from '@/components/HgInvoicePrint/index.vue'
import useUserStore from '@/store/modules/user'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IAxios } from 'axios'
import { ECustomerType, EPreinvoiceStyle } from '@/types/common/enumindex'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { dayjs } from 'element-plus'
import {
  TPreInvoiceQuery,
  IPreInvoiceApplicationDTO
} from '@/types/views/business/preinvoiceapplication'
import {
  getPreinvoicePageListApi,
  saveInvoiceByPreInvoApplyIdApi
} from '@/api/finance/invoiceprint'
import { rejectInvoiceApi } from '@/api/business/preinvoiceapplication'

const user = useUserStore().user
/** 详情窗口显示状态 */
const dialogDetailshow = ref(false)
/** 打印窗口显示状态 */
const dialogPrintshow = ref(false)
/** 预开发票退回编辑窗口显示状态 */
const dialogBackshow = ref(false)
/** 当前选择的预开发票对象 */
const curPreInvioce = ref<IPreInvoiceApplicationDTO>()
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 时间 */
const timedata = reactive<TDateType>({
  timetype: '6',
  timeoption: [
    {
      label: '今天',
      value: '0'
    },
    {
      label: '三天内',
      value: '2'
    },
    {
      label: '一周内',
      value: '6'
    },
    {
      label: '二周内',
      value: '13'
    },
    {
      label: '一月内',
      value: '30'
    },
    {
      label: '自定义',
      value: '00'
    }
  ],
  startTime: '',
  endTime: ''
})
/** 查询条件 */
const queryParams = reactive<TPreInvoiceQuery>({
  sort: 'dapplytime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: ''
})
/** 列表对象 */
const preInvoiceList = ref<IPreInvoiceApplicationDTO[]>([])
/** 选择打印时返回的发票表id */
const invoiceID = ref('')
/** 退回原因 */
const backreason = ref('')
onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 查询
 */
const handleQuery = () => {
  getPreinvoicePageListApi(queryParams)
    .then((res: IAxios<IPreInvoiceApplicationDTO[]>) => {
      if (res.success) {
        preInvoiceList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 打印按钮事件，先保存数据然后显示打印窗口
 * @param row
 */
const handlePrint = (row: IPreInvoiceApplicationDTO) => {
  const data = {
    preinvoiceapplicationId: row!.id!
  }
  saveInvoiceByPreInvoApplyIdApi(data).then((res: IAxios) => {
    if (res.success) {
      invoiceID.value = res.obj
      dialogPrintshow.value = true
      row.invoice = '临时'
    }
  })
}
/**
 * 撤销退回
 * @param row
 */
const handleBack = (row: IPreInvoiceApplicationDTO) => {
  invoiceID.value = row.id!
  backreason.value = ''
  dialogBackshow.value = true
}
/**
 * 显示详情
 * @param val
 */
const onDetail = (val?: any) => {
  curPreInvioce.value = val
  dialogDetailshow.value = true
}
/**
 * 保存退回意见
 */
const saveBackForm = () => {
  const data = {
    preinvoiceapplicationId: invoiceID.value,
    rejectReason: backreason.value
  }
  rejectInvoiceApi(data).then((res: IAxios) => {
    if (res.success) {
      invoiceID.value = ''
      backreason.value = ''
      dialogBackshow.value = false
      handleQuery()
    }
  })
}
/**
 * 时间选择
 * @param val
 */
const ontime = (val: TDateTimeType) => {
  let timev = false
  if (queryParams.startTime) {
    timev = true
  }
  queryParams.startTime = val.startTime
  queryParams.endTime = val.endTime
  if (timev) {
    handleQuery()
  }
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  handleQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryParams.page = val
  handleQuery()
}
</script>

<style lang="scss" scoped></style>
