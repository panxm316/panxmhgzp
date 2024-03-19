<!--
 * @Author: lhl
 * @Date: 2024-01-03
 * @Description: 项目汇总
-->
<template>
  <div class="app-container">
    <!--
        项目汇总工具栏
      -->
    <div v-if="contenttype === 0" class="search_box">
      <!--
        <el-select v-model="adprojectid" clearable placeholder="广告项目" @change="handleMedia()">
          <el-option
            v-for="item in AdProjectList"
            :key="item.id"
            :label="item.sname"
            :value="item.id!"
          />
        </el-select>
        -->
      <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
      <!-- <el-date-picker
          v-model="startTime"
          :editable="false"
          type="date"
          placeholder="选择日期"
          :clearable="false"
          style="width: 130px; margin-right: 5px"
          @change="ontimechange"
        >
        </el-date-picker>
        <span style="margin-left: 0px; margin-right: 10px">至</span>
        <el-date-picker
          id="endTime"
          v-model="endTime"
          :editable="false"
          type="date"
          placeholder="选择日期"
          :clearable="false"
          style="width: 130px; margin-left: 5px"
          @change="ontimechange"
        >
        </el-date-picker> -->

      <el-input
        v-model="queryKey"
        placeholder="请输入关键字"
        clearable
        style="width: 160px"
        @clear="handleQuery"
      />
      <el-checkbox v-model="orderpublish" @change="handleQuery">仅已刊发</el-checkbox>
      <el-checkbox v-model="projectend" @change="handleQuery">包含已结项</el-checkbox>
      <span style="margin-left: 30px"></span>
      <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
    </div>
    <!--
        相关合同工具栏
      -->
    <div v-show="contenttype === 3" class="search_box">
      <el-row>
        <el-col :span="16"> </el-col>
        <el-col :span="8" style="text-align: right">
          <el-button type="primary" @click="closeorder()">返回</el-button>
        </el-col>
      </el-row>
    </div>
    <!--
        查询列表
      -->
    <div v-show="contenttype === 0" class="table_box">
      <el-table
        :data="dataList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        border
      >
        <el-table-column type="index" label="序号" :index="table_index" width="60" align="center">
        </el-table-column>
        <el-table-column label="项目名称" prop="sname" min-width="200" show-overflow-tooltip />
        <el-table-column label="应收金额" prop="amountreceivable" min-width="160" align="center" />
        <el-table-column label="已收金额" prop="amountreceived" min-width="160" align="center" />
        <el-table-column label="欠款金额" prop="amountarrearage" min-width="160" align="center" />
        <el-table-column label="业绩金额" prop="amountachievement" min-width="160" align="center" />
        <el-table-column label="项目说明" prop="sremark" min-width="300" show-overflow-tooltip />
        <el-table-column label="创建日期" prop="dcreatedate" min-width="160">
          <template #default="scope">
            <span>{{ parseTime(scope.row.dcreatedate) }}</span>
          </template>
        </el-table-column>

        <el-table-column label="开始日期" prop="dstartdate" min-width="160">
          <template #default="scope">
            <span>{{ parseTime(scope.row.dstartdate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结束日期" prop="denddate" min-width="160">
          <template #default="scope">
            <span>{{ parseTime(scope.row.denddate) }}</span>
          </template>
        </el-table-column>

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
              icon="view"
              size="small"
              title="相关合同"
              @click="showContract(scope.row)"
              >相关合同</el-button
            >
          </template>
        </el-table-column>
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
    <!--
        相关合同
      -->
    <div v-show="contenttype === 3" class="table_box" style="border: 0">
      <ProjectContract :projectid="projectId" :project-type="1"></ProjectContract>
    </div>
  </div>
</template>

<script setup lang="ts">
import { required, computTableHeight, isNumberStr, formatDate, tableHeaderColor } from '@/utils'
import { parseTime } from '@/utils/filters'
import type { IAxios } from 'axios'
import { getSpecialProjectCountListApi } from '@/api/ad/specialproject'
import modal from '@/plugins/modal'
import HgDateIndex from '@/components/HgDateIndex/index.vue'
import { TDateType, TDateTimeType } from '@/types/components/hgdateindex'
import type { TAdProject } from '@/types/views/ad/adproject'
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaDataComboApi } from '@/api/media/media'
import dayjs from 'dayjs'
import { add } from 'lodash'

defineOptions({
  name: 'AdProjectCount'
})
import { useRouter } from 'vue-router'
const router = useRouter()
onMounted(() => {
  inittime()
  handleQuery()
  /* getadprojectlist()*/
  getMediaDataCombo()
  tableheight.value = computTableHeight()
})
/** 内容类型 0：项目汇总 1：订单详情 2：成本分析 */
const contenttype = ref(0)
/** 广告项目id */
const adprojectid = ref('')
/** 起始日期 */
const startTime = ref('')
/** 成本列表 */
const costdataList = ref<any>([])
/** 列表 */
const dataList = ref<any>([])
/** 去年已完成 */
const sourthdataList = ref<any>([])
/** 去年未完成 */
const nfdataList = ref<any>([])
/** 今年已完成 */
const wxdataList = ref<any>([])
/** 今年未完成 */
const otherdataList = ref<any>([])
/** 明年未完成 */
const nextyeardataList = ref<any>([])
/** 历史业绩 */
const historydataList = ref<any>([])
/** 媒体 */
const mediaId = ref('')
/** 项目ID */
const projectId = ref('')
/** 结束日期 */
const endTime = ref('')
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 成本记录总条数 */
const costtotal = ref(0)
/** 查询记录总条数 */
const total = ref(0)
/** 查询记录总条数 */
const total0 = ref(0)
const total1 = ref(0)
const total2 = ref(0)
const total3 = ref(0)
const total4 = ref(0)
const total5 = ref(0)
/** 查询关键词 */
const queryKey = ref('')
/** 媒体下拉列表 */
const mediaCombo = ref<IDataCombo[]>([])
/** 刊发 */
const orderpublish = ref(false)
/** 结项 */
const projectend = ref(false)
/** 报刊 */
const checkpaper = ref(true)
/** 南方+ */
const checksouth = ref(true)
/** 两微 */
const checkwx = ref(true)
/** 多元化 */
const checkmany = ref(true)
/** 成本 */
const checkcost = ref(true)
/** 存储起始日期 */
const historystartTime = ref('')
/** 存储结束日期 */
const historyendTime = ref('')
/** 查询时间 */
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
/** 广告项目列表 */
const AdProjectList = ref<TAdProject[]>([])
/** 请求对象 */
const queryvo = ref({
  stratTime: '',
  endTime: '',
  adProjectId: '',
  pageNum: 0,
  pageSize: 20,
  publistStatus: '',
  queryKey: '',
  projectEnd: ''
})
/** 去年已完成任务 */
const detailqueryvo0 = ref({
  adProjectId: '',
  mediaId: '',
  pageNum: 0,
  pageSize: 20,
  detailtype: '0'
})
/** 去年未完成任务 */
const detailqueryvo1 = ref({
  adProjectId: '',
  mediaId: '',
  pageNum: 0,
  pageSize: 20,
  detailtype: '0'
})
/** 今年已完成任务 */
const detailqueryvo2 = ref({
  adProjectId: '',
  mediaId: '',
  pageNum: 0,
  pageSize: 20,
  detailtype: '0'
})
/** 今年未完成任务 */
const detailqueryvo3 = ref({
  adProjectId: '',
  mediaId: '',
  pageNum: 0,
  pageSize: 20,
  detailtype: '0'
})
/** 明年未完成任务 */
const detailqueryvo4 = ref({
  adProjectId: '',
  mediaId: '',
  pageNum: 0,
  pageSize: 20,
  detailtype: '0'
})
/** 历史业绩 */
const detailqueryvo5 = ref({
  adProjectId: '',
  mediaId: '',
  pageNum: 0,
  pageSize: 20,
  detailtype: '0'
})
/** 成本请求对象 */
const costqueryvo = ref({
  adProjectId: '',
  pageNum: 0,
  pageSize: 20
})
/**
 * 时间选择
 * @param val
 */
const ontime = (val: TDateTimeType) => {
  let timev = false
  if (queryvo.value.stratTime) {
    timev = true
  }
  queryvo.value.stratTime = val.startTime
  queryvo.value.endTime = val.endTime
  if (timev) {
    handleQuery()
  }
}
/**
 * 获取序号
 */
const table_index = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    queryvo.value.pageSize! * (queryvo.value.pageNum === 0 ? 0 : queryvo.value.pageNum! - 1) + 1
  return val + pagenum
}
/**
 * 获取成本序号
 */
const table_index_cost = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    costqueryvo.value.pageSize! *
      (costqueryvo.value.pageNum === 0 ? 0 : costqueryvo.value.pageNum! - 1) +
    1
  return val + pagenum
}

/**
 * 显示相关合同
 */
const showContract = async (row: any) => {
  projectId.value = row.id
  contenttype.value = 3
}

/**
 * 改变页码总数调用
 */
const handleSizeChange = (val: number) => {
  queryvo.value.pageSize = val
  handleQuery()
}
/**
 * 改变页数调用
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryvo.value.pageNum = val
  handleQuery()
}
/**
 * 查询
 */
const handleQuery = () => {
  // queryvo.value.stratTime = startTime.value
  // queryvo.value.endTime = endTime.value
  queryvo.value.adProjectId = adprojectid.value
  queryvo.value.queryKey = queryKey.value
  if (orderpublish.value) queryvo.value.publistStatus = '1'
  else {
    queryvo.value.publistStatus = ''
  }
  if (projectend.value) queryvo.value.projectEnd = '1'
  else {
    queryvo.value.projectEnd = '0'
  }
  getSpecialProjectCountListApi(queryvo.value).then((res) => {
    if (res.success) {
      dataList.value = res.obj
      total.value = res.total
      console.log(dataList.value)
    }
  })
}

/**
 * 关闭订单明细
 */
const closeorder = () => {
  contenttype.value = 0
  startTime.value = historystartTime.value
  endTime.value = historyendTime.value
}

/**
 * 获取媒体下拉列表
 */
const getMediaDataCombo = () => {
  getMediaDataComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
      console.log('媒体')
      console.log(mediaCombo.value)
    }
  })
}
const table_index0 = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    detailqueryvo0.value.pageSize! *
      (detailqueryvo0.value.pageNum === 0 ? 0 : detailqueryvo0.value.pageNum! - 1) +
    1
  return val + pagenum
}
const table_index1 = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    detailqueryvo1.value.pageSize! *
      (detailqueryvo1.value.pageNum === 0 ? 0 : detailqueryvo1.value.pageNum! - 1) +
    1
  return val + pagenum
}
const table_index2 = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    detailqueryvo2.value.pageSize! *
      (detailqueryvo2.value.pageNum === 0 ? 0 : detailqueryvo2.value.pageNum! - 1) +
    1
  return val + pagenum
}
const table_index3 = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    detailqueryvo3.value.pageSize! *
      (detailqueryvo3.value.pageNum === 0 ? 0 : detailqueryvo3.value.pageNum! - 1) +
    1
  return val + pagenum
}
const table_index4 = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    detailqueryvo4.value.pageSize! *
      (detailqueryvo4.value.pageNum === 0 ? 0 : detailqueryvo4.value.pageNum! - 1) +
    1
  return val + pagenum
}
const table_index5 = (val: number) => {
  // eslint-disable-next-line eqeqeq
  const pagenum =
    detailqueryvo5.value.pageSize! *
      (detailqueryvo5.value.pageNum === 0 ? 0 : detailqueryvo5.value.pageNum! - 1) +
    1
  return val + pagenum
}
const ontimechange = () => {
  const curDate = new Date(dayjs(startTime.value).format('YYYY-MM-DD 00:00:00')).getTime()
  const curendTime = new Date(dayjs(endTime.value).format('YYYY-MM-DD 00:00:00')).getTime()
  if (curDate > curendTime) {
    modal.msgWarning('开始日期不能大于结束日期')
    return false
  }
  startTime.value = dayjs(startTime.value).format('YYYY-MM-DD')
  endTime.value = dayjs(endTime.value).format('YYYY-MM-DD')
  historystartTime.value = startTime.value
  historyendTime.value = endTime.value
  handleQuery()
}
/**
 * 时间初始化赋值
 */
const inittime = () => {
  const end = new Date()
  const start = new Date()
  start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
  startTime.value = dayjs(start).format('YYYY-MM-DD')
  endTime.value = dayjs(end).format('YYYY-MM-DD')
}

/**
 * 项目选择
 */
const handleMedia = () => {
  historystartTime.value = startTime.value
  historyendTime.value = endTime.value
  handleQuery()
}
</script>
<style scoped>
ul {
  padding-left: 10px;
}
</style>
