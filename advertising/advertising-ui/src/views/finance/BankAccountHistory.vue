<!--
 * @Author: suny
 * @Date: 2023-10-24 10:53:29
 * @LastEditTime: 2023-11-15 15:19:25
 * @LastEditors: peij
 * @Description: 银行流水导入历史
-->
<template>
  <div class="search_box">
    <div class="flex">
      <div>
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入对方账户关键字"
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
        ref="dataTableRef"
        :data="BankAccountHistoryList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column label="源文件名" prop="soriginalfile" />
        <el-table-column label="创建日期" prop="dcreatetime" width="200" />
        <el-table-column label="操作员名称" prop="employname" width="120" />
        <el-table-column label="文件格式" prop="sfileformat" width="120" />
        <el-table-column label="文件ID" prop="sfileid" />
        <el-table-column label="文件大小" prop="sfilesize" width="120" /><el-table-column
          label="操作"
          align="center"
          width="210"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button link type="success" icon="search" @click="originalDetail(scope.row)"
              >查看源文件</el-button
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
  </div>
</template>

<script lang="ts" setup>
import { getBankAccountHistoryPageListApi } from '@/api/finance'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { ITwbankaccounthistory, TBankAccountHistoryQuery } from '@/types/views/finance'
import { computTableHeight, tableHeaderColor } from '@/utils'
import { IAxios } from 'axios'

const global = getCurrentInstance()?.appContext.config.globalProperties
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
const queryParams = reactive<TBankAccountHistoryQuery>({
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  queryKey: ''
})

/** 银行流水列表 */
const BankAccountHistoryList = ref<ITwbankaccounthistory[]>([])
onMounted(() => {
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight() - 200
})
/**
 * 查询
 */
const handleQuery = () => {
  getBankAccountHistoryPageListApi(queryParams)
    .then((res: IAxios<ITwbankaccounthistory[]>) => {
      if (res.success) {
        BankAccountHistoryList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
const originalDetail = (row: ITwbankaccounthistory) => {
  const url = `${global?.officePreviewUrl}?src=${row.durl}`
  window.open(url, 'newwin')
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
