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
        <el-table-column prop="name" label="区域"></el-table-column>
        <el-table-column :label="reportdata.thisYearReportDate" align="center">
          <el-table-column label="收入明细 " align="center">
            <el-table-column label="版面" prop="adtotal" align="center">
              <template #default="scope">
                {{ toThousandsFormates(scope.row.adtotal) }}
              </template>
            </el-table-column>
            <el-table-column label="新媒体" prop="optotal" align="center">
              <template #default="scope">
                {{ toThousandsFormates(scope.row.optotal) }}
              </template>
            </el-table-column>
            <el-table-column label="其中：南方+" prop="optotal" align="center">
              <template #default="scope">
                {{ toThousandsFormates(scope.row.admonthtotal) }}
              </template>
            </el-table-column>
            <el-table-column label="多元化" prop="optotal" align="center">
              <template #default="scope">
                {{ toThousandsFormates(scope.row.dytotal) }}
              </template>
            </el-table-column>
            <el-table-column label="合计" align="center">
              <template #default="scope">
                {{ toThousandsFormates(scope.row.adtotal + scope.row.optotal + scope.row.dytotal) }}
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="其中：展馆投放" align="center">
            <el-table-column label="版面" align="center"> </el-table-column>
            <el-table-column label="南方+" align="center"> </el-table-column>
          </el-table-column>
        </el-table-column>
        <el-table-column :label="reportdata.lastYearReportDate" align="center">
          <el-table-column label="收入明细 " align="center">
            <el-table-column label="版面" prop="admonthtotal" align="center">
              <template #default="scope">
                {{ toThousandsFormates(scope.row.admonthtotal) }}
              </template>
            </el-table-column>
            <el-table-column label="新媒体" prop="opmonthtotal" align="center">
              <template #default="scope">
                {{ toThousandsFormates(scope.row.opmonthtotal) }}
              </template>
            </el-table-column>
            <el-table-column label="其中：南方+" prop="amountmonthtotal" align="center">
              <template #default="scope">
                {{ toThousandsFormates(scope.row.amountmonthtotal) }}
              </template>
            </el-table-column>
            <el-table-column label="多元化" prop="predytotal" align="center">
              <template #default="scope">
                {{ toThousandsFormates(scope.row.predytotal) }}
              </template>
            </el-table-column>
            <el-table-column label="合计" align="center">
              <template #default="scope">
                {{
                  toThousandsFormates(
                    scope.row.admonthtotal + scope.row.opmonthtotal + scope.row.predytotal
                  )
                }}
              </template>
            </el-table-column>
          </el-table-column>
          <el-table-column label="其中：展馆投放" align="center">
            <el-table-column label="版面" align="center"> </el-table-column>
            <el-table-column label="南方+" align="center"> </el-table-column>
          </el-table-column>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script setup lang="ts">
import { IAxios } from 'axios'
import {
  exportAreaIncomeReceivableApi,
  exportAreaIncomeReceivableExcelApi
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
  exportAreaIncomeReceivableApi().then((res) => {
    if (res.success) {
      console.log(res.obj)
      reportdata.value = res.obj
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
  exportAreaIncomeReceivableExcelApi()
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
