<!--
 * @Author: lhl
 * @Date: 2024-01-31
 * @Description: 区域实收明细表（广告公司）
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
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column label="本月数" prop="stradmonthtotal" align="center"></el-table-column>
        <el-table-column label="累计数" prop="stradtotal" align="center"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup lang="ts">
import { IAxios } from 'axios'
import { exportAreaIncomeOpApi, exportAreaIncomeOpExcelApi } from '@/api/statistic/financereport'
import { computTableHeight, required, tableHeaderColor, formatMoney } from '@/utils'
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
  exportAreaIncomeOpApi({ reportFormId: '5' }).then((res) => {
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
  exportAreaIncomeOpExcelApi({ reportFormId: '5' })
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
</script>
<style lang="scss" scoped></style>
