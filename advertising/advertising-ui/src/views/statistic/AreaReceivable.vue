<!--
 * @Author: lhl
 * @Date: 2024-02-3
 * @Description: 区域完成情况统计表
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
            <td style="border: 0; width: 20%">{{ reportdata.company }}</td>
            <td style="border: 0; width: 10%"></td>
            <td style="border: 0; width: 10%"></td>
            <td style="border: 0; width: 10%">{{ reportdata.reportDate }}</td>
            <td style="border: 0; width: 10%"></td>
            <td style="border: 0; width: 10%"></td>
            <td style="border: 0; width: 10%">单位：万元</td>
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
        <el-table-column prop="name" label="区域"></el-table-column>
        <el-table-column prop="amounttotal" :label="reportdata.thisYear + peapertotal">
          <template #default="scope">
            {{ toThousandsFormates(scope.row.amounttotal) }}
          </template>
        </el-table-column>
        <el-table-column prop="amountmonthtotal" :label="reportdata.thisYear + nftotal">
          <template #default="scope">
            {{ toThousandsFormates(scope.row.amountmonthtotal) }}
          </template>
        </el-table-column>
        <el-table-column :label="reportdata.thisYearReportDate + headName" align="center">
          <el-table-column label="平面、多元" prop="adtotal" align="center">
            <template #default="scope">
              {{ toThousandsFormates(scope.row.adtotal / 10000) }}
            </template>
          </el-table-column>
          <el-table-column label="南方+" prop="optotal" align="center">
            <template #default="scope">
              {{ toThousandsFormates(scope.row.optotal / 10000) }}
            </template>
          </el-table-column>
          <el-table-column label="报端合计" align="center">
            <template #default="scope">
              {{ sumcolumn(scope.row, '0') }}
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column :label="reportdata.thisYear + peapertask">
          <template #default="scope">
            {{
              topercent(
                (scope.row.adtotal / 10000 + scope.row.optotal / 10000) / scope.row.amounttotal,
                2
              )
            }}
          </template>
        </el-table-column>
        <el-table-column :label="reportdata.thisYear + nftask">
          <template #default="scope">
            {{ topercent(scope.row.optotal / 10000 / scope.row.amountmonthtotal, 2) }}
          </template>
        </el-table-column>
        <el-table-column :label="reportdata.lastYearReportDate + headName" align="center">
          <el-table-column label="平面、多元" prop="admonthtotal" align="center">
            <template #default="scope">
              {{ toThousandsFormates(scope.row.admonthtotal) }}
            </template>
          </el-table-column>
          <el-table-column label="南方+" prop="opmonthtotal" align="center">
            <template #default="scope">
              {{ toThousandsFormates(scope.row.opmonthtotal) }}
            </template>
          </el-table-column>
          <el-table-column label="合计" align="center">
            <template #default="scope">
              {{ toThousandsFormates(scope.row.opmonthtotal + scope.row.admonthtotal) }}
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="同比增长值" align="center">
          <el-table-column label="平面、多元" align="center">
            <template #default="scope">
              {{ toThousandsFormates(scope.row.adtotal / 10000 - scope.row.admonthtotal / 10000) }}
            </template>
          </el-table-column>
          <el-table-column label="南方+" align="center">
            <template #default="scope">
              {{ toThousandsFormates(scope.row.optotal / 10000 - scope.row.opmonthtotal / 10000) }}
            </template>
          </el-table-column>
          <el-table-column label="报端合计" align="center">
            <template #default="scope">
              {{
                toThousandsFormates(
                  scope.row.optotal / 10000 +
                    scope.row.adtotal / 10000 -
                    (scope.row.admonthtotal / 10000 + scope.row.opmonthtotal / 10000)
                )
              }}
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="同比增长率" align="center">
          <el-table-column label="平面、多元" align="center">
            <template #default="scope">
              {{ topercent(scope.row.adtotal / scope.row.admonthtotal - 1, 2) }}
            </template>
          </el-table-column>
          <el-table-column label="南方+" align="center">
            <template #default="scope">
              {{ topercent(scope.row.optotal / scope.row.opmonthtotal - 1, 2) }}
            </template>
          </el-table-column>
          <el-table-column label="合计" align="center">
            <template #default="scope">
              {{
                topercent(
                  (scope.row.optotal + scope.row.adtotal) /
                    (scope.row.opmonthtotal + scope.row.admonthtotal) -
                    1,
                  2
                )
              }}
            </template>
          </el-table-column>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup lang="ts">
import { IAxios } from 'axios'
import {
  exportAreaReceivableApi,
  exportAreaReceivableExcelApi
} from '@/api/statistic/financereport'
import { computTableHeight, required, tableHeaderColor, formatMoney } from '@/utils'
defineOptions({
  name: 'Department'
})
const peapertask = ref('年报端总任务完成进度')
const nftask = ref('年南方+完成进度')
const peapertotal = ref('年报端总任务数')
const nftotal = ref('年南方+任务数')
/** 广告应收框 */
const headName = ref('实际完成数')
/** 任务完成情况 */
const taskName = ref('年任务完成情况')
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
  exportAreaReceivableApi().then((res) => {
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
  exportAreaReceivableExcelApi()
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
const sumcolumn = (row: any, type: string) => {
  if (type === '0') {
    return toThousandsFormates(row.adtotal / 10000 + row.optotal / 10000)
  }
}
// 转换会计金额格式
const toThousandsFormates = (num: any) => {
  // 判断传进来的数字是否为非空数字
  if (!isNaN(parseFloat(num))) {
    var newNum = Number(Number(num).toFixed(2)).toLocaleString('zh', { minimumFractionDigits: 2 })
    return newNum
  } else {
    return ''
  }
}

// 转换百分数
const topercent = (v: any, n: any) => {
  if (typeof v === 'number') {
    return (v * 100).toFixed(n) + '%'
  }
}
</script>
<style lang="scss" scoped></style>
