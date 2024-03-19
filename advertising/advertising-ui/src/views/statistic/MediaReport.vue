<!--
 * @Author: lhl
 * @Date: 2024-01-16
 * @Description: 广告实收明细表（按业务汇总）
-->
<template>
  <div class="app-container">
    <div class="table_box">
      <!--
      工具条
     -->
      <div class="search_box">
        <el-button type="primary" @click="exportExcel">导出EXCEL</el-button>
      </div>
      <!--
      <table style="width: 100%" border="1" cellspacing="0" cellpadding="10">
        <tr align="center" style="border: 0">
          <td colspan="7" style="border: 0; font-size: 22px; font-weight: bolder; height: 90px">
            {{ reportdata.title }}
          </td>
        </tr>
        <tr align="center" style="border: 0">
          <td style="border: 0; width: 20%">编制单位：{{ reportdata.company }}</td>
          <td style="border: 0; width: 10%"></td>
          <td style="border: 0; width: 10%"></td>
          <td style="border: 0; width: 10%">{{ reportdata.reportDate }}</td>
          <td style="border: 0; width: 10%"></td>
          <td style="border: 0; width: 10%"></td>
          <td style="border: 0; width: 10%">单位：元</td>
        </tr>
        <tr align="center">
          <td rowspan="2">名称</td>
          <td colspan="2">经营公司</td>
          <td colspan="2">广告公司</td>
          <td colspan="2">合计</td>
        </tr>
        <tr align="center">
          <td>本月数</td>
          <td>累计数</td>
          <td>本月数</td>
          <td>累计数</td>
          <td>本月数</td>
          <td>累计数</td>
        </tr>
        <tr v-for="item in reportdata.itemList" :key="item.id" align="center">
          <td>{{ item.name }}</td>
          <td>{{ item.opmonthtotal }}</td>
          <td>{{ item.optotal }}</td>
          <td>{{ item.admonthtotal }}</td>
          <td>{{ item.admonthtotal }}</td>
          <td>{{ item.amountmonthtotal }}</td>
          <td>{{ item.amounttotal }}</td>
        </tr>
        <tr align="center">
          <td>合计</td>
          <td>{{ totalOpmonthtotal }}</td>
          <td>{{ totalOptotal }}</td>
          <td>{{ totalAdmonthtotal }}</td>
          <td>{{ totalADtotal }}</td>
          <td>{{ totalamountmonthtotal }}</td>
          <td>{{ totalamounttotal }}</td>
        </tr>
      </table>
     -->
      <div>
        <table style="width: 100%" border="0" cellspacing="0" cellpadding="10">
          <tr align="center" style="border: 0">
            <td colspan="7" style="border: 0; font-size: 22px; font-weight: bolder; height: 90px">
              {{ reportdata.title }}
            </td>
          </tr>
          <tr align="center" style="border: 0">
            <td style="border: 0; width: 20%">编制单位：{{ reportdata.company }}</td>
            <td style="border: 0; width: 10%"></td>
            <td style="border: 0; width: 10%"></td>
            <td style="border: 0; width: 10%">{{ reportdata.reportDate }}</td>
            <td style="border: 0; width: 10%"></td>
            <td style="border: 0; width: 10%"></td>
            <td style="border: 0; width: 10%">单位：元</td>
          </tr>
        </table>
      </div>
      <el-table
        id="table"
        ref="exportTable"
        :data="reportdata.itemList"
        highlight-current-row
        :height="tableheight"
        :span-method="arraySpanMethod"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column prop="companyName" label="名称"></el-table-column>
        <el-table-column prop="year" label="见报年份"></el-table-column>
        <el-table-column label="本月数" align="center">
          <el-table-column prop="monthBMTotal" label="版面广告"></el-table-column>
          <el-table-column prop="monthNFTotal" label="南方+"></el-table-column>
          <el-table-column prop="monthWZTotal" label="网站"></el-table-column>
          <el-table-column prop="monthSWTotal" label="双微"></el-table-column>
          <el-table-column prop="monthHDTotal" label="活动"></el-table-column>
          <el-table-column prop="monthCHTotal" label="策划制作"></el-table-column>
          <el-table-column prop="monthTotal" label="本月合计"></el-table-column>
        </el-table-column>
        <el-table-column label="本年数" align="center">
          <el-table-column prop="yearBMTotal" label="版面广告"></el-table-column>
          <el-table-column prop="yearNFTotal" label="南方+"></el-table-column>
          <el-table-column prop="yearWZTotal" label="网站"></el-table-column>
          <el-table-column prop="yearSWTotal" label="双微"></el-table-column>
          <el-table-column prop="yearHDTotal" label="活动"></el-table-column>
          <el-table-column prop="yearCHTotal" label="策划制作"></el-table-column>
          <el-table-column prop="yearTotal" label="本年累计"></el-table-column>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup lang="ts">
import { IAxios } from 'axios'
import {
  exportAdvertisingYearMediaApi,
  exportAdvertisingYearMediaExcelApi
} from '@/api/statistic/financereport'
import { computTableHeight, required, tableHeaderColor, formatMoney } from '@/utils'
import type { TableColumnCtx } from 'element-plus'
defineOptions({
  name: 'Department'
})

/** 经营公司本月数合计 */
const totalOpmonthtotal = ref(0)
/** 经营公司累计数合计 */
const totalOptotal = ref(0)
/** 广告公司本月数合计 */
const totalAdmonthtotal = ref(0)
/** 广告公司累计数合计 */
const totalADtotal = ref(0)
/** 本月合计数 */
const totalamountmonthtotal = ref(0)
/** 本月累计数 */
const totalamounttotal = ref(0)
/** 表格高度 */
const tableheight = ref(0)
const mimeMap = {
  xlsx: 'application/vnd.ms-excel',
  zip: 'application/zip'
}
/** 汇总数据 */
const reportdata = ref<any>({
  title: '',
  company: '',
  reportDate: '',
  itemList: []
})
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
onBeforeMount(() => {
  getReport()
})
/**
 * 汇总
 */
const getReport = () => {
  exportAdvertisingYearMediaApi().then((res: any) => {
    if (res.success) {
      console.log(res.obj)
      reportdata.value = res.obj
      amountData()
    }
  })
}
/**
 * 汇总经营公司本月累计
 */
const amountData = () => {
  totalOpmonthtotal.value = 0
  totalOptotal.value = 0
  reportdata.value.itemList.forEach((item: any) => {
    totalOpmonthtotal.value += item.opmonthtotal
    totalOptotal.value += item.optotal
    totalAdmonthtotal.value += item.admonthtotal
    totalADtotal.value += item.adtotal
    totalamountmonthtotal.value += item.amountmonthtotal
    totalamounttotal.value += item.amounttotal
  })
}
const exportExcel = () => {
  exportAdvertisingYearMediaExcelApi()
    .then((res: IAxios) => {
      console.log(res)
      downExcel(res, mimeMap.xlsx)
    })
    .catch(() => {})
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
interface User {
  id: string
  name: string
  amount1: string
  amount2: string
  amount3: number
}

interface SpanMethodProps {
  row: User
  column: TableColumnCtx<User>
  rowIndex: number
  columnIndex: number
}

const arraySpanMethod = ({ row, column, rowIndex, columnIndex }: SpanMethodProps) => {
  /* if (rowIndex === 0) {
    if (columnIndex === 0) return [4, 1]
  }*/
}
</script>
<style lang="scss" scoped></style>
