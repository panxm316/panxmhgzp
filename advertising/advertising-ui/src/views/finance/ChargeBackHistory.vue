<!--
 * @Author: suny
 * @Date: 2023-12-27 12:47:18
 * @LastEditTime: 2024-03-06 13:38:07
 * @LastEditors: wanghl
 * @Description: 分摊退回历史
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex :initdate="timedata" @onresponse="ontimeCharge"></HgDateIndex>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入合同号或者发票号"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="historyTableRef"
        :data="historyList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <el-table-column label="操作者" prop="createempname" width="120" align="center" />
        <el-table-column label="回退日期" prop="dcreatetime" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="合同号"
          prop="scontractnum"
          width="140"
          align="center"
          show-overflow-tooltip
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="view"
              size="small"
              @click="handleSee(scope.row, scope.$index)"
              >{{ scope.row.scontractnum }}</el-button
            >
          </template>
        </el-table-column>
        <el-table-column label="发票号" prop="invoice" width="150" />
        <el-table-column label="分摊日期" prop="dapportiondate" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dapportiondate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="收款种类" prop="sapportiontype" width="120">
          <template #default="scope">
            {{ stypeCombo.find((item) => item.id === scope.row.sapportiontype)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="回退金额" prop="namountapportion" width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountapportion, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="刊发日期" prop="prestartdate" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="说明" prop="sdescription" min-width="120" />

        <el-table-column label="媒体" prop="medianame" min-width="120" show-overflow-tooltip />
        <el-table-column label="广告标题" prop="sadtitle" min-width="140" show-overflow-tooltip />
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
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>
<script setup lang="ts">
import { ESType } from '@/types/common/enumindex'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import {
  TInvoiceBackHistoryVO,
  IInvoiceBackHistoryDTO
} from '@/types/views/finance/invoicebackhistory'
import { IAxios } from 'axios'
import { reactive, ref } from 'vue'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { FormInstance, dayjs, ElTable } from 'element-plus'
import { getInvoiceBackHistoryPageListApi } from '@/api/finance/advancecharge'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common/index'
/** 收款类型下拉 */
const stypeCombo: IDataCombo[] = getEnumCombo(ESType)
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])

/** 查询条件 */
const queryParams = reactive<TInvoiceBackHistoryVO>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  queryKey: ''
})
/** 当前已分摊列表详情数组 */
const historyList = ref<IInvoiceBackHistoryDTO[]>([])

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

onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 50
})
/**
 * 分配结果查询
 */
const handleQuery = () => {
  getInvoiceBackHistoryPageListApi(queryParams)
    .then((res: IAxios<IInvoiceBackHistoryDTO[]>) => {
      if (res.success) {
        historyList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 时间选择
 * @param val
 */
const ontimeCharge = (val: TDateTimeType) => {
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
/**
 * 根据合同号查看订单项目--------------------------------------------------
 * @param row
 * @param index
 */
import AdTworderdetails from '../ad/AdTworderdetails.vue'
const viewImageRef = ref()
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
}
// ---------------------------------------------------------------------
</script>
<style lang="scss" scoped></style>
