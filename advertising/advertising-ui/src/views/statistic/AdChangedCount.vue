<template>
  <div class="app-container">
    <div class="search_box">
      <el-date-picker
        v-model="startTime"
        :editable="false"
        type="month"
        placeholder="选择月度"
        :clearable="false"
        style="width: 130px; margin-right: 5px"
        @change="onresponse"
      >
      </el-date-picker>
      <el-button type="primary" @click="exportExcel">导出EXCEL</el-button>
    </div>
    <!--
      查询列表
    -->
    <div class="table_box">
      <el-table
        :data="dataList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column
          type="index"
          label="序号"
          :index="table_index"
          width="60"
          align="center"
        ></el-table-column>
        <el-table-column label="登记时间" prop="createtime" min-width="100" align="center">
          <template #default="scope">
            <span>{{ dayjs(scope.row.createtime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="合同号" prop="scontractnum" min-width="160" align="center" />
        <el-table-column label="代理公司" prop="agencyname" min-width="160" align="center" />
        <el-table-column label="客户名称" prop="customername" min-width="160" align="center" />
        <el-table-column label="见报日期" prop="prestartdate" min-width="100" align="center">
          <template #default="scope">
            <span>{{ dayjs(scope.row.createtime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="应收金额" prop="amountreceivable" min-width="120" align="center">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="变动金额" prop="amountreceivable" min-width="120" align="center">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业务员" prop="employname" min-width="160" align="center" />
        <el-table-column label="叠次" prop="foldname" min-width="160" align="center" />
        <el-table-column
          label="经营主体"
          prop="businessentityname"
          min-width="160"
          align="center"
        />
        <el-table-column
          label="调整要求"
          prop="ibooktype"
          show-overflow-tooltip
          min-width="100"
          align="center"
        >
          <template #default="scope">
            <span v-if="scope.row.ibooktype === 3" style="color: #e6a23c">补刊</span>
          </template>
        </el-table-column>
        <el-table-column
          label="变动后应收金额"
          prop="changeAmountreceivable"
          min-width="120"
          align="center"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.changeAmountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="修改后发布时间" prop="prestartdate" min-width="100" align="center">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="sremark" min-width="260" align="center" />
      </el-table>
      <el-pagination
        v-model:current-page="queryvo.pageNum"
        class="pagination"
        style="margin-left: 10px; width: 100%"
        :page-sizes="pageSizes"
        :page-size="queryvo.pageSize"
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

<script setup lang="ts">
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  tableHeaderColor,
  formatMoney
} from '@/utils'
import type { IAxios } from 'axios'
import modal from '@/plugins/modal'
import HgDateIndex from '@/components/HgDateIndex/index.vue'
import { TDateType, TDateTimeType } from '@/types/components/hgdateindex'
import dayjs from 'dayjs'
import { add } from 'lodash'
defineOptions({
  name: 'AdChangedCount'
})
import { useRouter } from 'vue-router'
import { ITaskReportsDTO } from '@/types/views/business/tasks'
import { queryChangeOrderApi, exportChangeOrderExcelApi } from '@/api/statistic/financereport'
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 查询记录总条数 */
const total = ref(0)

/** 列表对象 */
const dataList = ref<any>([])
const router = useRouter()
const startTime = ref<Date | string>('')
const mimeMap = {
  xlsx: 'application/vnd.ms-excel',
  zip: 'application/zip'
}
const dateTime = ref('')
/** 请求对象 */
const queryvo = ref({
  dateTime: '',
  pageNum: 0,
  pageSize: 20
})

/** 表格高度 */
const tableheight = ref(0)
/**
 * 时间初始化赋值
 */
const inittime = () => {
  startTime.value = new Date()
  dateTime.value = dayjs(startTime.value).format('YYYY-MM-DD 00:00:00')
}
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
  inittime()
  getChaneOrderList()
})
/**
 * 日期回调
 */
const onresponse = () => {
  dateTime.value = dayjs(startTime.value).format('YYYY-MM-DD 00:00:00')
  getChaneOrderList()
}

/**
 * 订单变动
 */
const getChaneOrderList = () => {
  queryvo.value.dateTime = dateTime.value
  queryChangeOrderApi(queryvo.value).then((res) => {
    if (res.success) {
      dataList.value = res.obj
      total.value = res.total
      console.log(dataList.value)
    }
  })
}
/**
 * 改变页码总数调用
 */
const handleSizeChange = (val: number) => {
  queryvo.value.pageSize = val
  getChaneOrderList()
}
/**
 * 改变页数调用
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryvo.value.pageNum = val
  getChaneOrderList()
}
const table_index = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    queryvo.value.pageSize! * (queryvo.value.pageNum === 0 ? 0 : queryvo.value.pageNum! - 1) + 1
  return val + pagenum
}
/**
 * 下载EXCEL
 */
 const downExcel = (res: any, mimeType: any) => {
  // 创建a标签，并处理二进制数据
  const aLink = document.createElement('a')
  const blob = new Blob([res.data], { type: mimeType })
  // 生成下载链接
  const URL = window.URL || window.webkitURL
  aLink.href = URL.createObjectURL(blob)
  // 设置下载文件名称
  let fileName = ''
  if (res.headers['content-disposition']) fileName = res.headers['content-disposition']
  if (res.headers['Content-Disposition']) fileName = res.headers['Content-Disposition']
  aLink.setAttribute('download', fileName)
  // 下载
  document.body.appendChild(aLink)
  aLink.click()
  // 释放URL对象
  window.URL.revokeObjectURL(aLink.href)
  document.body.removeChild(aLink)
}
const exportExcel = () => {
  queryvo.value.dateTime = dateTime.value
  exportChangeOrderExcelApi(queryvo.value)
    .then((res: IAxios) => {
      console.log(res)
      downExcel(res, mimeMap.xlsx)
    })
    .catch(() => {})
}
</script>
<style scoped>
ul {
  padding-left: 10px;
}
</style>
