<!--
 * @Author: lhl
 * @Date: 2024-03-12
 * @Description: 客户来源
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <el-select v-model="taskQuotaForm.level" placeholder="来源级别">
        <el-option
          v-for="item in baseTask.sourceLevel"
          :key="item.id"
          :label="item.value"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
      <el-button type="primary" @click="onQuery">查询</el-button>
    </div>
    <div class="table_box">
      <el-table
        :data="dataList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column label="客户来源" prop="areaname" min-width="200" show-overflow-tooltip />
        <el-table-column label="客户数量" prop="totalcount" min-width="80" show-overflow-tooltip />
        <el-table-column
          label="操作"
          align="left"
          width="200"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              style="margin-left: 10px"
              type="success"
              icon="Edit"
              size="small"
              title="查看客户明细"
              @click="onShowDetail(scope.row)"
              >查看客户明细</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--
      订单明细窗口
  -->
    <el-dialog
      v-model="customerinfodlg"
      title="客户明细"
      :width="1200"
      append-to-body
      :destroy-on-close="true"
    >
      <el-table
        :data="customerdataList"
        highlight-current-row
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column label="客户名称" prop="sname" min-width="160" align="center" />
        <el-table-column label="是否大客户" prop="employname" min-width="160" align="center">
          <template #default="scope">
            <span v-if="scope.row.bvip === true">是</span>
            <span v-if="scope.row.bvip === false">否</span>
          </template>
        </el-table-column>
        <el-table-column label="简码" prop="sbrevitycode" min-width="160" align="center" />
        <el-table-column label="客户类型" prop="itype" min-width="160" align="center">
          <template #default="scope">
            <span v-if="scope.row.ipublishstatus === 0">直接客户</span>
            <span v-if="scope.row.ipublishstatus === 1">代理公司</span>
            <span v-if="scope.row.ipublishstatus === 2">内容生产方</span>
          </template>
        </el-table-column>
        <el-table-column
          label="客户分类"
          prop="customercategoryname"
          min-width="160"
          align="center"
        />
        <el-table-column label="行业" prop="adindustryname" min-width="160" align="center" />
        <el-table-column label="业务员" prop="employname" min-width="160" align="center" />
        <el-table-column label="地址" prop="saddress" min-width="160" align="center" />
        <el-table-column label="创建日期" prop="dcreatetime" min-width="160" align="center">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: right; margin-top: 60px; width: 100%">
        <el-button icon="Close" @click="customerinfodlg = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { IAxios } from 'axios'
import { computTableHeight, tableHeaderColor } from '@/utils'
import baseTask, { ISynQueryItem } from '@/types/views/statistic/reportforms'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import dayjs from 'dayjs'
import { customerResourceQueryApi, getCustomerDetailApi } from '@/api/statistic/financereport'
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
  getStartTime()
  getEndTime()
})
/** 请求对象 */
const taskQuotaForm = ref({
  level: '1',
  startTime: '',
  endTime: ''
})
/** 客户明细请求对象 */
const coustomerVO = ref({
  id: '',
  startTime: '',
  endTime: ''
})
/** 初始化时间 */
/** 时间 */
const timedata = reactive<TDateType>({
  timetype: '30',
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

/** 是否显示表格 */
const showtable = ref(0)
/** 客户明细窗口 */
const customerinfodlg = ref(false)
/** 级别 */
const sourcelevel = ref('1')
/** 日期 */
const startTime = ref('')
/** 日期 */
const endTime = ref('')
/** 表格高度 */
const tableheight = ref(0)
/** 列表 */
const dataList = ref<any>([])
/** 客户列表 */
const customerdataList = ref<any>([])
/**
 * 查询
 */
const onQuery = () => {
  taskQuotaForm.value.startTime = startTime.value
  taskQuotaForm.value.endTime = endTime.value
  customerResourceQueryApi(taskQuotaForm.value)
    .then((res: IAxios) => {
      if (res.success) {
        dataList.value = res.obj
        showtable.value = 1
      }
    })
    .catch(() => {})
}
const initDate = () => {
  var startYear = new Date().getFullYear()
  var endYear = new Date().getFullYear()
  taskQuotaForm.value.startTime = startYear.toString()
  taskQuotaForm.value.endTime = endYear.toString()
}

/**
 * 时间选择
 * @param val
 */
const ontime = (val: TDateTimeType) => {
  startTime.value = val.startTime
  endTime.value = val.endTime
}

const getStartTime = () => {
  var base = new Date().getTime()
  var oneDay = 24 * 3600 * 1000
  var starttime = new Date((base += oneDay))
  var month = starttime.getMonth() + 1
  timedata.startTime = starttime.getFullYear() + '-' + month + '-' + starttime.getDate()
  startTime.value = timedata.startTime
}

const getEndTime = () => {
  var base = new Date().getTime()
  var oneDay = 24 * 3600 * 1000
  var starttime = new Date((base += oneDay * 7))
  var month = starttime.getMonth() + 1
  timedata.endTime = starttime.getFullYear() + '-' + month + '-' + starttime.getDate()
  endTime.value = timedata.endTime
}

/**
 * 查看客户明细
 * @param row
 */
const onShowDetail = (row: any) => {
  coustomerVO.value.id = row.id
  coustomerVO.value.startTime = startTime.value
  coustomerVO.value.endTime = endTime.value
  getCustomerDetailApi(coustomerVO.value)
    .then((res: IAxios) => {
      if (res.success) {
        customerdataList.value = res.obj
        customerinfodlg.value = true
      }
    })
    .catch(() => {})
}
</script>
