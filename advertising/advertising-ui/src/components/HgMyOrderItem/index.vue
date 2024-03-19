<!--
 * @Author: yanz
 * @Date: 2024-01-24 15:44:45
 * @LastEditors: wanghl
 * @LastEditTime: 2024-03-16 11:16:44
 * @Description: 个人中心 - 我的订单
 *
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <!-- <el-button type="warning" icon="close" @click="stopOrderitemlist()">停刊</el-button> -->
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <HgZtreeSelect
          v-if="user?.blead || user?.adminflag !== 0"
          :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
          :selectids="queryParams.deptid ? [queryParams.deptid] : []"
          style="width: 120px"
          :filterable="true"
          clearable
          :treeparams="{ showInactiveDepts: false }"
          placeholder="请选择部门"
          @selectionztree="onSelectionDept"
        ></HgZtreeSelect>
        <HgZtreeSelect
          v-if="user?.blead || user?.adminflag !== 0"
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
          :selectids="queryParams.employid ? [queryParams.employid] : []"
          style="width: 120px"
          :filterable="true"
          clearable
          :treeparams="{ bIgnorePermissions: false }"
          placeholder="请选择业务员"
          @selectionztree="onSelectionEmp"
          @change="handleQuery"
        ></HgZtreeSelect>
        <el-input
          v-model="queryParams.queryKey"
          placeholder="合同号或项目名"
          clearable
          title="支持广告合同号检索条件"
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        />
        <el-select
          v-model="queryParams.ipublishstatus"
          placeholder="请选择发布状态"
          clearable
          style="width: 140px; margin-right: 5px"
          @change="handleQuery"
          @clear="handleQuery"
        >
          <el-option
            v-for="t in publishStatusCombo"
            :key="t.id"
            :label="t.name"
            :value="t.id"
          ></el-option>
        </el-select>
        <el-checkbox
          v-model="queryParams.barrearsflag"
          inline-prompt
          style="margin-top: 5px; margin-right: 10px"
          label="仅欠款"
          @change="handleQuery"
        />
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="resultList"
        show-summary
        :summary-method="getSummaries"
        highlight-current-row
        :height="tableheight"
        @selection-change="handleSelectionChange"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <!-- <el-table-column type="selection" prop="ischeck" :width="50" align="center">
        </el-table-column> -->
        <!-- 广告项目名称 -->
        <!-- String adprojectname -->
        <el-table-column
          label="广告项目名称"
          prop="adprojectname"
          width="200"
          show-overflow-tooltip
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="view"
              size="small"
              @click="handleSeeadprojectname(scope.row.adprojectid)"
              >{{ scope.row.adprojectname }}</el-button
            >
          </template>
        </el-table-column>
        <el-table-column
          label="项目编号"
          prop="projectcode"
          width="130"
          show-overflow-tooltip
          align="center"
        />
        <el-table-column
          label="广告号"
          prop="iitemcode"
          width="100"
          show-overflow-tooltip
          align="center"
        />
        <!-- 合同号 -->
        <!-- String scontractnum -->
        <el-table-column
          label="合同号"
          prop="scontractnum"
          min-width="140"
          align="center"
          show-overflow-tooltip
        >
          <template #default="scope">
            <el-button
              v-if="scope.row.scontractnum"
              link
              type="primary"
              icon="view"
              size="small"
              @click="handleSee(scope.row, scope.$index)"
              >{{ scope.row.scontractnum }}</el-button
            >
          </template>
        </el-table-column>
        <!-- 广告标题 -->
        <!-- String sadtitle -->
        <el-table-column label="广告标题" prop="sadtitle" width="200" show-overflow-tooltip />
        <!-- 发布状态 -->
        <!-- Integer ipublishstatus -->
        <el-table-column
          prop="ipublishstatus"
          label="发布状态"
          sortable="custom"
          min-width="100"
          align="center"
        >
          <template #default="scope">
            {{ publishStatusCombo.find((item) => item.id === scope.row.ipublishstatus)?.name }}
          </template>
        </el-table-column>
        <!-- 订单状态 -->
        <el-table-column label="加刊状态" min-width="80" align="center">
          <template #default="scope">
            <span v-if="scope.row.iaddapprovestatus === 0">正常</span>
            <span v-if="scope.row.iaddapprovestatus === 1" style="color: #409eff">待审</span>
            <span v-if="scope.row.iaddapprovestatus === 2" style="color: #67c23a">已加刊</span>
            <span v-if="scope.row.iaddapprovestatus === 3" style="color: #f56c6c">拒绝</span>
          </template>
        </el-table-column>
        <el-table-column label="改刊状态" min-width="80" align="center">
          <template #default="scope">
            <span v-if="scope.row.ichangeapprovestatus === 0">正常</span>
            <span v-if="scope.row.ichangeapprovestatus === 1" style="color: #409eff">待审</span>
            <span v-if="scope.row.ichangeapprovestatus === 2" style="color: #67c23a">已改刊</span>
            <span v-if="scope.row.ichangeapprovestatus === 3" style="color: #f56c6c">拒绝</span>
          </template>
        </el-table-column>
        <el-table-column label="停刊状态" min-width="80" align="center">
          <template #default="scope">
            <span v-if="scope.row.istopapprovestatus === 0">正常</span>
            <span v-if="scope.row.istopapprovestatus === 1" style="color: #409eff">待审</span>
            <span v-if="scope.row.istopapprovestatus === 3" style="color: #67c23a">拒绝</span>
            <span v-if="scope.row.istopapprovestatus === 2" style="color: #f56c6c">已停刊</span>
          </template>
        </el-table-column>
        <!-- 业务员名称 -->
        <!-- String employname -->
        <el-table-column label="业务员" prop="employname" width="160" show-overflow-tooltip />
        <!-- 客户名称 -->
        <!-- String customername -->
        <el-table-column label="客户名称" prop="customername" width="160" show-overflow-tooltip />
        <!-- 代理公司名称 -->
        <!-- String agencyname -->
        <el-table-column label="代理公司" prop="agencyname" width="160" show-overflow-tooltip />
        <!-- 内容生产方 -->
        <!-- String agentname -->
        <el-table-column label="内容生产方" prop="agentname" width="160" show-overflow-tooltip />
        <!-- 行业名称 -->
        <!-- String adindustryname -->
        <el-table-column label="行业名称" prop="adindustryname" width="120" show-overflow-tooltip />
        <!-- 媒体类型名称 -->
        <!-- String mediatypename -->
        <el-table-column label="媒体类型" prop="mediatypename" width="120" show-overflow-tooltip />
        <!-- 媒体名称 -->
        <!-- String medianame -->
        <el-table-column label="媒体名称" prop="medianame" width="120" show-overflow-tooltip />
        <!-- 预见报开始日期（刊发日期） -->
        <!-- Date prestartdate -->
        <el-table-column label="预见报开始日期" prop="prestartdate" width="120">
          <template #default="scope">
            <span v-if="scope.row.prestartdate">{{
              dayjs(scope.row.prestartdate).format('YYYY-MM-DD')
            }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <!-- 应收金额 -->
        <!-- BigDecimal amountreceivable -->
        <el-table-column label="应收金额" prop="amountreceivable" align="right" width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 已收金额 -->
        <!-- BigDecimal amountreceived -->
        <el-table-column label="已收金额" prop="amountreceived" align="right" width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 欠款金额 -->
        <!-- BigDecimal amountarrearage -->
        <el-table-column label="欠款金额" prop="amountarrearage" align="right" width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 成本金额 -->
        <!-- BigDecimal namountcost -->
        <el-table-column label="成本金额" prop="namountcost" align="right" width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountcost, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 业绩金额 -->
        <!-- BigDecimal amountachievement -->
        <!-- <el-table-column label="业绩金额" prop="amountachievement" align="right" width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountachievement, '2') }}</span>
          </template>
        </el-table-column> -->
        <!-- 业绩时间(用于统计) -->
        <!-- Date dachievementdate -->
        <!-- <el-table-column label="业绩时间" prop="dachievementdate" width="120">
          <template #default="scope">
            <span v-if="scope.row.dachievementdate">{{
              dayjs(scope.row.dachievementdate).format('YYYY-MM-DD')
            }}</span>
            <span v-else>--</span>
          </template>
        </el-table-column> -->
        <!-- 叠次名称 -->
        <!-- String foldname -->
        <el-table-column label="叠次名称" prop="foldname" width="120" show-overflow-tooltip />
        <!-- 叠次版本名称 -->
        <!-- String foldareavername -->
        <el-table-column
          label="叠次版本名称"
          prop="foldareavername"
          width="120"
          show-overflow-tooltip
        />
        <!-- 广告形式名称 -->
        <!-- String addisplayformname -->
        <el-table-column
          label="广告形式名称"
          prop="addisplayformname"
          width="120"
          show-overflow-tooltip
        />
        <!-- 版面类别名称 -->
        <!-- String pagesortname -->
        <el-table-column
          label="版面类别名称"
          prop="pagesortname"
          width="120"
          show-overflow-tooltip
        />
        <!-- 色彩名称 -->
        <!-- String adcolorname -->
        <el-table-column label="色彩名称" prop="adcolorname" width="120" show-overflow-tooltip />
        <!-- 规格名称 -->
        <!-- String adspecname -->
        <el-table-column label="规格名称" prop="adspecname" width="120" show-overflow-tooltip />
        <!-- 宽 -->
        <!-- BigDecimal nwidth -->
        <el-table-column label="宽" prop="nwidth" width="120" show-overflow-tooltip />
        <!-- 高 -->
        <!-- BigDecimal nheight -->
        <el-table-column label="高" prop="nheight" width="120" show-overflow-tooltip />
        <!-- 星期名称 -->
        <!-- String weeksettingname -->
        <el-table-column
          label="星期名称"
          prop="weeksettingname"
          width="120"
          show-overflow-tooltip
        />

        <!-- 版面名称 -->
        <!-- String foldpageplanname -->
        <el-table-column
          label="版面名称"
          prop="foldpageplanname"
          width="120"
          show-overflow-tooltip
        />
        <!-- 经营主体名称（关联发票表找到） -->
        <!-- String businessentityname -->
        <el-table-column
          label="经营主体名称"
          prop="businessentityname"
          width="120"
          show-overflow-tooltip
        />
        <!-- 关联发票号(英文逗号分隔) -->
        <!-- String invoices -->
        <el-table-column label="关联发票号" prop="invoices" width="120" show-overflow-tooltip />
        <!-- 刊期编号(自增列,广告号) -->
        <!-- Long iitemcode -->
        <el-table-column label="刊期编号" prop="iitemcode" width="120" show-overflow-tooltip />

        <el-table-column
          label="操作"
          align="left"
          width="180"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <div v-if="scope.row.istopapprovestatus !== 2">
              <el-button
                link
                title="详情"
                type="primary"
                size="small"
                icon="plus"
                style="margin: 0 5px"
                :disabled="
                  scope.row.iorderaddapprovestatus === 1 ||
                  scope.row.iorderchangeapprovestatus === 1 ||
                  scope.row.iorderstopapprovestatus === 1
                "
                @click="changeorderlistsubmit(scope.row, 'add')"
                >加刊
              </el-button>
              <el-button
                v-if="scope.row.ipublishstatus == '1'"
                link
                title="详情"
                type="success"
                size="small"
                icon="edit"
                style="margin: 0 5px"
                :disabled="
                  scope.row.iorderaddapprovestatus === 1 ||
                  scope.row.iorderchangeapprovestatus === 1 ||
                  scope.row.iorderstopapprovestatus === 1
                "
                @click="changeorderlistsubmit(scope.row, 'change')"
                >改刊
              </el-button>
              <el-button
                v-if="scope.row.ipublishstatus == '1'"
                link
                title="详情"
                type="warning"
                size="small"
                icon="close"
                style="margin: 0 5px"
                :disabled="
                  scope.row.iorderaddapprovestatus === 1 ||
                  scope.row.iorderchangeapprovestatus === 1 ||
                  scope.row.iorderstopapprovestatus === 1
                "
                @click="changeorderlistsubmit(scope.row, 'stop')"
                >停刊
              </el-button>
            </div>
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
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
    <!-- 项目详情 -->
    <AdProjectdetails ref="AdProjectdetailsref"></AdProjectdetails>
    <!-- 选择流程 -->
    <HgFlowTypeSelectSecond
      ref="HgFlowTypeSelectSecondref"
      @selecflowtype="selecflowtype"
    ></HgFlowTypeSelectSecond>
    <!--  -->
    <HgFlowChangeOrder
      ref="viewFlowChangeOrderRef"
      @closedialogVisiblesubmit="closedialogVisiblesubmitdata()"
    ></HgFlowChangeOrder>
  </div>
</template>

<script setup lang="ts">
import { computTableHeight, formatMoney, tableHeaderColor } from '@/utils'
import { dayjs } from 'element-plus'
import { EHgDeptZtreeUrl, EPublishstatus } from '@/types/common/enumindex'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IOrderAndItemDTO, TOrderAndItemVO } from '@/types/views/business/orderitemcost'
import { TSelectZtree } from '@/types/common'
import { getMyOrderItemCountApi, getMyOrderItemPageListApi } from '@/api/personal/PersonalStat'
import { IAxios } from 'axios'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common'
import useUserStore from '@/store/modules/user'
import AdProjectdetails from '@/views/ad/AdProjectdetails.vue'
import HgFlowTypeSelectSecond from '../HgFlowTypeSelectSecond/index.vue'
import {
  TOrderitemsupplementpublishSearch,
  TStopType
} from '@/types/views/schedule/orderitemsupplementpublish'
const global = getCurrentInstance()?.appContext.config.globalProperties
const user = useUserStore().user
/** 广告详情 */
const AdProjectdetailsref = ref()
/** 表格高度 */
const tableheight = ref(0)
/** 查询总页数 */
const pageTotal = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([15, 20, 30, 40])
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
/** 查询参数 */
const queryParams = reactive<TOrderAndItemVO>({
  /**
   * 预见报开始日期
   */
  sort: 'prestartdate',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  // 是否只查询当前人员（0：否 1：是，如果是则表示只查询当前人员）
  bcurflag: undefined
})
/**
 * 查询结果列表对象
 */
const resultList = ref<IOrderAndItemDTO[]>([])
/**
 * 业绩汇总
 */
const sumResult = ref<IOrderAndItemDTO>()
/** 刊发状态列表 */
const publishStatusCombo: IDataCombo[] = getEnumCombo(EPublishstatus)
onMounted(() => {
  if (user?.blead || user?.adminflag !== 0) {
    queryParams.bcurflag = false
  } else {
    queryParams.bcurflag = true
  }
  handleQuery()
  getFlowlistCombo()
  getFlowlistCombofloworderchange()
  nextTick(() => {
    /**
     * 计算表格高度
     */
    tableheight.value = computTableHeight() - 50
  })
})
/**
 * @description: 页码总数变更
 * @param {*} val
 * @return {*}
 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  handleQuery()
}

/**
 * @description: 页码变更
 * @param {*} val
 * @return {*}
 */
const handleCurrentChange = (val: number) => {
  queryParams.page = val
  handleQuery()
}
/**
 * 查询
 */
const handleQuery = () => {
  getMyOrderItemPageListApi(queryParams)
    .then((res: IAxios<IOrderAndItemDTO[]>) => {
      if (res.success) {
        resultList.value = res.obj
        pageTotal.value = res.total
      }
    })
    .finally(() => {})
  handleQueryCount()
}
/**
 * 查询汇总
 */
const handleQueryCount = () => {
  getMyOrderItemCountApi(queryParams)
    .then((res: IAxios) => {
      if (res.success) {
        sumResult.value = res.obj
      }
    })
    .finally(() => {})
}
/**
 * @description: 表格合计
 */
import type { TableColumnCtx } from 'element-plus'
interface SummaryMethodProps<T = IOrderAndItemDTO> {
  columns: TableColumnCtx<T>[]
  data: T[]
}
/**
 * 数据合计
 * @param param
 */
const getSummaries = (param: SummaryMethodProps) => {
  // console.log('param', param.data)
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column, index) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    if (column.property === 'amountreceivable') {
      sums[index] = sumResult.value?.amountreceivable
        ? '￥ ' + formatMoney(sumResult.value?.amountreceivable, '2')
        : '￥ ' + '0'
      return
    }
    if (column.property === 'amountreceived') {
      sums[index] = sumResult.value?.amountreceived
        ? '￥ ' + formatMoney(sumResult.value?.amountreceived, '2')
        : '￥ ' + '0'
      return
    }
    if (column.property === 'amountarrearage') {
      sums[index] = sumResult.value?.amountarrearage
        ? '￥ ' + formatMoney(sumResult.value?.amountarrearage, '2')
        : '￥ ' + '0'
      return
    }
    if (column.property === 'amountachievement') {
      sums[index] = sumResult.value?.amountachievement
        ? '￥ ' + formatMoney(sumResult.value?.amountachievement, '2')
        : '￥ ' + '0'
      return
    }
    if (column.property === 'namountcost') {
      sums[index] = sumResult.value?.namountcost
        ? '￥ ' + formatMoney(sumResult.value?.namountcost, '2')
        : '￥ ' + '0'
      return
    }
    // console.log('sumResult', sumResult.value)
    // if (column.property === 'ncommission') {
    //   sums[index] = sumResult.value?.ncommission
    //     ? '￥ ' + formatMoney(sumResult.value?.ncommission, '2')
    //     : '￥ ' + '0'
    //   return
    // }
  })
  /**
   * 遍历数组保留两位小数
   */
  sums.forEach(function (element) {
    Number(element).toFixed(2)
  })
  return sums
}
/**
 * 选择部门
 * @param val
 */
const onSelectionDept = (val: TSelectZtree[]) => {
  const deptid = val.map((item) => item.id).join(',')
  queryParams.deptid = deptid
}
/**
 * 选择人员
 * @param val
 */
const onSelectionEmp = (val: TSelectZtree[]) => {
  const employid = val.map((item) => item.id).join(',')
  queryParams.employid = employid
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
 * 项目详情
 * @param row
 */
const handleSeeadprojectname = (row: string) => {
  AdProjectdetailsref.value.view(row)
}
/**
 * 根据合同号查看订单项目--------------------------------------------------
 * @param row
 * @param index
 */
import AdTworderdetails from '@/views/ad/AdTworderdetails.vue'
const viewImageRef = ref()
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
}
// 改，加，停刊---------------------------------------------------------------------
import {
  addOrderitemByItemIdApi,
  changeOrderitemByItemIdApi,
  stopOrderitemByItemIdApi
} from '@/api/schedule/orderitemsupplementpublish'
import modal from '@/plugins/modal'
/**
 * 加刊
 * @param row
 */
const addOrderitem = (row?: IOrderAndItemDTO) => {
  changeFlowData.value.ids = row!.orderitemid
  orderitemData.value = 0
  if (flowTypeList.value.length > 1) {
    HgFlowTypeSelectSecondref.value.view(EFlowType.电子认刊书审批流程)
  } else if (flowTypeList.value.length == 1) {
    changeFlowData.value.flowId = flowTypeList.value[0].id
    stopsubmit()
  } else {
    modal.msgError('请先配置加刊流程')
  }
}
/**
 * 改刊
 * @param row
 */
const changeOrderitem = (row?: IOrderAndItemDTO) => {
  changeFlowData.value.ids = row!.orderitemid
  orderitemData.value = 1
  if (flowTypeList.value.length > 1) {
    HgFlowTypeSelectSecondref.value.view(EFlowType.电子认刊书审批流程)
  } else if (flowTypeList.value.length == 1) {
    changeFlowData.value.flowId = flowTypeList.value[0].id
    stopsubmit()
  } else {
    modal.msgError('请先配置改刊流程')
  }
}
/**
 * 停刊
 * @param row
 */
const stopOrderitem = (row?: IOrderAndItemDTO) => {
  stopFlow.value.ids = row!.orderitemid
  orderitemData.value = 2
  if (flowTypeListfloworderchange.value.length > 1) {
    HgFlowTypeSelectSecondref.value.view(EFlowType.改加停刊审批流程)
  } else if (flowTypeListfloworderchange.value.length == 1) {
    stopFlow.value.flowId = flowTypeListfloworderchange.value[0].id
    stopsubmit()
  } else {
    modal.msgError('请先配置停刊流程')
  }
}
/**
 * 批量停刊
 */
const stopOrderitemlist = () => {
  if (OrderSelection.value.length == 0) {
    modal.msgError('请选择要停刊的项目')
    return
  }
  let ids = ''
  OrderSelection.value.forEach((item) => {
    ids += item.orderitemid + ','
  })
  stopFlow.value.ids = ids
  orderitemData.value = 2
  if (flowTypeListfloworderchange.value.length > 1) {
    HgFlowTypeSelectSecondref.value.view(EFlowType.改加停刊审批流程)
  } else if (flowTypeListfloworderchange.value.length == 1) {
    stopFlow.value.flowId = flowTypeListfloworderchange.value[0].id
    stopsubmit()
  } else {
    modal.msgError('请先配置停刊流程')
  }
}
/**
 * 提交
 */
const stopsubmit = () => {
  if (orderitemData.value == 2) {
    modal.confirm('是否要提交停刊申请？').then(() => {
      stopOrderitemByItemIdApi(stopFlow.value)
        .then((res: IAxios) => {
          console.log('res', res)
          if (res.success) {
            handleQuery()
            modal.msgSuccess('停刊成功')
          }
        })
        .finally(() => {})
    })
  }
  if (orderitemData.value == 1) {
    modal.confirm('是否要提交改刊申请？').then(() => {
      changeOrderitemByItemIdApi(changeFlowData.value)
        .then((res: IAxios) => {
          console.log('res', res)
          if (res.success) {
            modal.msgSuccess('改刊成功')
          }
        })
        .finally(() => {})
    })
  }
  if (orderitemData.value == 0) {
    modal.confirm('是否要提交加刊申请？').then(() => {
      addOrderitemByItemIdApi(changeFlowData.value)
        .then((res: IAxios) => {
          console.log('res', res)
          if (res.success) {
            modal.msgSuccess('加刊成功')
          }
        })
        .finally(() => {})
    })
  }
}
/**
 * 流程赋值
 */
const selecflowtype = (row?: Tadindustrylist) => {
  stopFlow.value.flowId = row!.id
  changeFlowData.value.flowId = row!.id
  stopsubmit()
}
/**
 * 获取审批流程下拉列表------------------------------------------------
 */
import { getFlowlistComboByFlowTypeApi } from '@/api/flow'
import { EApproveStatus, ECustomerType, EFlowType } from '@/types/common/enumindex'
import { Tadindustrylist, TypeCurrentData } from '@/types/components/hgindustry'
/** 电子认刊书审批流程流程类型列表 */
const flowTypeList = ref<Tadindustrylist[]>([])
const getFlowlistCombo = () => {
  getFlowlistComboByFlowTypeApi(EFlowType.电子认刊书审批流程).then(
    (res: IAxios<Tadindustrylist[]>) => {
      console.log('res', res)
      if (res.success) {
        flowTypeList.value = res.obj
      }
    }
  )
}
/** 改加停刊审批流程审批流程流程类型列表 */
const flowTypeListfloworderchange = ref<Tadindustrylist[]>([])
const getFlowlistCombofloworderchange = () => {
  getFlowlistComboByFlowTypeApi(EFlowType.改加停刊审批流程).then(
    (res: IAxios<Tadindustrylist[]>) => {
      console.log('res', res)
      if (res.success) {
        flowTypeListfloworderchange.value = res.obj
      }
    }
  )
}
/**选择流程组件 */
const HgFlowTypeSelectSecondref = ref()
/**
 * 判断是改刊0还是加刊1还是停刊2
 */
const orderitemData = ref<number>(2)
/**
 * 改刊，加刊数据
 */
const changeFlowData = ref<any>({
  ids: '',
  flowId: ''
})
/**
 * 停刊数据
 */
const stopFlow = ref<TStopType>({
  ids: '',
  flowId: ''
})

const OrderSelection = ref<IOrderAndItemDTO[]>([])
/**
 * 选择
 * @param rows
 */
const handleSelectionChange = (rows: IOrderAndItemDTO[]) => {
  OrderSelection.value = rows
}
/**
 * 订单详情审批流程是查案修改订单书--------------------------------------------------
 * @param row
 * @param index
 */
import HgFlowChangeOrder from '../HgFlowChangeOrder/index.vue'
const viewFlowChangeOrderRef = ref()

/**
 *订单详情审批流程传值
 * @param row
 */
type Typechangeorderlistdata = {
  orderid?: string // 订单id
  orderitemid?: string // 订单明细id
  changetype?: string // 并传入审批类型add,change,stop
}
const changeorderlistdata = ref<Typechangeorderlistdata>({
  orderid: '', // 订单id
  orderitemid: '', // 订单明细id
  changetype: '' // 并传入审批类型add,change,stop
})
const changeorderlistsubmit = (row?: IOrderAndItemDTO, Flowtypedata?: string) => {
  rowdata.value = row
  Flowtype.value = Flowtypedata as string
  changeorderlistdata.value.orderid = row!.orderid
  changeorderlistdata.value.orderitemid = row!.orderitemid
  changeorderlistdata.value.changetype = Flowtypedata as string
  viewFlowChangeOrderRef.value.view(changeorderlistdata.value)
}
/**
 * 订单数据
 */
const rowdata = ref<IOrderAndItemDTO>()
/**
 * 订单类型
 */
const Flowtype = ref<string>('')
/**
 * 选择的订单
 */
const closedialogVisiblesubmitdata = () => {
  if (Flowtype.value == 'add') {
    addOrderitem(rowdata.value)
  }
  if (Flowtype.value == 'change') {
    changeOrderitem(rowdata.value)
  }
  if (Flowtype.value == 'stop') {
    stopOrderitem(rowdata.value)
  }
}
</script>

<style scoped lang="scss"></style>
