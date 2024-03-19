<!--
 * @Author: yanz
 * @Date: 2024-03-14 14:11:23
 * @LastEditors: yanz
 * @LastEditTime: 2024-03-18 17:03:30
 * @Description: 个人中心 - 我的实收
 *
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
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
        <el-button type="warning" icon="Search" @click="showSum">查看汇总</el-button>
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
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <el-table-column type="selection" prop="ischeck" :width="50" align="center">
        </el-table-column>
        <el-table-column label="序号" type="index" />
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
              link
              type="primary"
              icon="view"
              size="small"
              @click="handleSee(scope.row, scope.$index)"
              >{{ scope.row.scontractnum }}</el-button
            >
          </template>
        </el-table-column>
        <el-table-column label="经营主体" prop="businessentityname" show-overflow-tooltip />
        <el-table-column label="直接客户" prop="customername" show-overflow-tooltip />
        <el-table-column label="代理公司" prop="agencyname" show-overflow-tooltip />
        <el-table-column
          label="内容生产方"
          prop="agentname"
          min-width="120"
          show-overflow-tooltip
        />
        <el-table-column label="预定日期" prop="prestartdate" show-overflow-tooltip>
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="收费日期" prop="dpaydate(customercharge表)" show-overflow-tooltip>
          <template #default="scope">
            <span>{{ dayjs(scope.row.dpaydate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="分摊日期" prop="dapportiondate" show-overflow-tooltip>
          <template #default="scope">
            <span>{{ dayjs(scope.row.dapportiondate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="employname" label="业务员" min-width="90" align="center" />
        <el-table-column label="签订金额" prop="amountreceivable" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已收金额" prop="amountreceived" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="欠款金额" prop="amountarrearage" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="分摊金额" prop="namountapportion" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountapportion, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="广告行业"
          prop="adindustryname"
          show-overflow-tooltip
          min-width="160"
          align="center"
        />
        <el-table-column prop="foldname" label="叠次版本" min-width="100" show-overflow-tooltip />
        <el-table-column
          prop="foldpageplanname"
          label="版面"
          min-width="60"
          show-overflow-tooltip
        />
        <el-table-column label="发票号" prop="invoices" show-overflow-tooltip />
        <el-table-column label="最后操作员" prop="lastoperator" width="140" show-overflow-tooltip />
        <el-table-column label="媒体" prop="medianame" show-overflow-tooltip />
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
    <el-dialog
      id="modelDialogRef"
      v-model="showSummary"
      title="汇总"
      width="900"
      :modal="false"
      :show-close="true"
      :close-on-click-modal="false"
      :draggable="true"
      left
    >
      <div class="app-container" style="margin-top: -30px">
        <el-row :gutter="5">
          <el-col :span="24">
            <div class="table_box">
              <el-table
                :data="resultSumList"
                row-key="id"
                :border="true"
                show-summary
                :summary-method="getSummaries"
                stripe
                :height="500"
                :header-cell-style="tableHeaderColor"
              >
                <el-table-column prop="employname" label="业务员" min-width="90" align="center" />
                <el-table-column label="分摊金额" prop="namountapportion" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountapportion, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="操作"
                  align="center"
                  class-name="small-padding fixed-width"
                  fixed="right"
                >
                  <template #default="scope">
                    <el-button
                      link
                      title="详情"
                      type="primary"
                      size="small"
                      icon="View"
                      style="margin: 0 5px"
                      @click="onDetail(scope.row)"
                      >详情
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-row>
                <el-pagination
                  v-model:current-page="queryInfoSum.page"
                  class="pagination"
                  style="margin-left: 10px; width: 100%"
                  :page-sizes="pageSizes4Sum"
                  :page-size="queryInfoSum.pageSize"
                  small
                  background
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="pageTotal4Sum"
                  @size-change="handleSizeChange4Sum"
                  @current-change="handleCurrentChange4Sum"
                >
                </el-pagination>
              </el-row>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computTableHeight, formatDatesm, formatMoney, tableHeaderColor } from '@/utils'
import { dayjs } from 'element-plus'
import { EHgDeptZtreeUrl, EPublishstatus } from '@/types/common/enumindex'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IOrderAndItemDTO, TOrderAndItemVO } from '@/types/views/business/orderitemcost'
import { TSelectZtree } from '@/types/common'
import {
  getMyOrderItemCountApi,
  getMyOrderItemPageListApi,
  getMyReceivedPageListApi,
  getMyReceivedSummariesPageListApi
} from '@/api/personal/PersonalStat'
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
/** 展示汇总表 */
const showSummary = ref(false)
/** 展示明细 */
const onDetail = (val?: any, number?: number) => {
  queryParams.employid = val.employid
  handleQuery()
}
const queryInfoSum = reactive<any>({
  sort: 'syear',
  order: 'desc',
  syear: '',
  pageSize: 20,
  page: 1,
  queryKey: '' // 查询关键字
})
/** 广告详情 */
const AdProjectdetailsref = ref()
/** 表格高度 */
const tableheight = ref(0)
/** 查询总页数 */
const pageTotal = ref(0)
/** 查询总页数 */
const pageTotal4Sum = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([15, 20, 30, 40])
/** 每页选择下拉 */
const pageSizes4Sum = ref<number[]>([15, 20, 30, 40])
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
  pageSize: 15,
  startTime: '',
  endTime: ''
  // 是否只查询当前人员（0：否 1：是，如果是则表示只查询当前人员）
  // bcurflag: undefined
})
/**
 * 查询结果列表对象
 */
const resultList = ref<IOrderAndItemDTO[]>([])
const resultSumList = ref<IOrderAndItemDTO[]>([])
/**
 * 业绩汇总
 */
const sumResult = ref<IOrderAndItemDTO>()
onMounted(() => {
  if (user?.blead || user?.adminflag !== 0) {
    queryParams.bcurflag = false
  } else {
    queryParams.bcurflag = true
  }
  handleQuery()
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
 * @description: 页码总数变更-汇总窗
 * @param {*} val
 * @return {*}
 */
const handleSizeChange4Sum = (val: number) => {
  queryInfoSum.pageSize = val
  handleSumQuery()
}

/**
 * @description: 页码变更-汇总窗
 * @param {*} val
 * @return {*}
 */
const handleCurrentChange4Sum = (val: number) => {
  queryInfoSum.page = val
  handleSumQuery()
}
/**
 * 查询
 */
const handleQuery = () => {
  getMyReceivedPageListApi(queryParams)
    .then((res: IAxios<IOrderAndItemDTO[]>) => {
      if (res.success) {
        resultList.value = res.obj
        pageTotal.value = res.total
      }
    })
    .finally(() => {})
  // handleQueryCount()
}
/**
 * 查询汇总信息
 */
const handleSumQuery = () => {
  getMyReceivedSummariesPageListApi(queryParams)
    .then((res: IAxios<IOrderAndItemDTO[]>) => {
      if (res.success) {
        resultSumList.value = res.obj
        pageTotal4Sum.value = res.total
      }
    })
    .finally(() => {})
  // handleQueryCount()
}
/**
 * 展示汇总
 */
const showSum = () => {
  showSummary.value = true
  handleSumQuery()
}
// /**
//  * 查询汇总
//  */
// const handleQueryCount = () => {
//   getMyOrderItemCountApi(queryParams)
//     .then((res: IAxios) => {
//       if (res.success) {
//         sumResult.value = res.obj
//       }
//     })
//     .finally(() => {})
// }
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
      sums[index] = '本页合计'
      return
    }
    if (column.property !== 'namountapportion') {
      sums[index] = ''
      return
    }
    const values = data.map((item: any) => Number(item[column.property]))
    if (!values.every((value) => Number.isNaN(value))) {
      sums[index] =
        '￥ ' +
        formatMoney(
          `${values.reduce((prev: any, curr: any) => {
            const value = Number(curr)
            if (!Number.isNaN(value)) {
              return Math.floor((prev + curr) * 100) / 100
            } else {
              return Math.floor(prev * 100) / 100
            }
          }, 0)}`,
          '2'
        )
    } else {
      sums[index] = ''
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
 * 根据合同号查看订单项目--------------------------------------------------
 * @param row
 * @param index
 */
import AdTworderdetails from '@/views/ad/AdTworderdetails.vue'
const viewImageRef = ref()
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
}
</script>

<style scoped lang="scss"></style>
