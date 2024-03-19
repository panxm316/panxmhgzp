<!--
 * @Author: yanz
 * @Date: 2024-01-24 15:44:45
 * @LastEditors: suny suny@hgzp.com.cn
 * @LastEditTime: 2024-02-27 15:56:51
 * @Description: 个人中心 - 佣金统计
 *
-->
<template>
  <div id="HgMyCommission" class="app-container">
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
        <!-- 广告项目名称 -->
        <!-- String adprojectname -->
        <el-table-column
          label="广告项目名称"
          prop="adprojectname"
          width="120"
          show-overflow-tooltip
        />
        <!-- 媒体名称 -->
        <!-- String medianame -->
        <el-table-column label="媒体名称" prop="medianame" width="120" show-overflow-tooltip />
        <!-- 关联发票号(英文逗号分隔) -->
        <!-- String invoices -->
        <el-table-column label="关联发票号" prop="invoices" width="120" show-overflow-tooltip />
        <!-- 刊期编号(自增列,广告号) -->
        <!-- Long iitemcode -->
        <el-table-column label="刊期编号" prop="iitemcode" width="120" show-overflow-tooltip />
        <!-- 发布状态 -->
        <!-- Integer ipublishstatus -->
        <el-table-column prop="ipublishstatus" label="发布状态" sortable="custom" min-width="120">
          <template #default="scope">
            {{ publishStatusCombo.find((item) => item.id === scope.row.ipublishstatus)?.name }}
          </template>
        </el-table-column>
        <!-- 折扣降点 -->
        <!-- discountreduce?: number -->
        <el-table-column label="折扣降点" prop="discountreduce" min-width="100" align="center">
          <template #default="scope">
            <span v-if="scope.row.discountreduce">{{ scope.row.discountreduce }}%</span>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <!-- 计提金额 -->
        <!-- ncommission?: number -->
        <el-table-column label="计提金额" prop="ncommission" min-width="100" align="center">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.ncommission, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 计提比例 -->
        <!-- ncommissionrate?: number -->
        <el-table-column label="计提比例" prop="ncommissionrate" min-width="100" align="center">
          <template #default="scope">
            <span>{{ scope.row.ncommissionrate }}%</span>
          </template>
        </el-table-column>
        <!-- 风险金比例% -->
        <!-- nrateofrisk?: number -->
        <el-table-column label="风险金比例" prop="nrateofrisk" min-width="100" align="center">
          <template #default="scope">
            <span>{{ scope.row.nrateofrisk }}%</span>
          </template>
        </el-table-column>
        <!-- 风险金 -->
        <!-- riskfund?: number -->
        <el-table-column label="风险金" prop="riskfund" min-width="100" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.riskfund, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 标记(0-计算1-确认 2-发放 3-撤销) -->
        <!-- icommissionstatus?: number -->
        <el-table-column label="标记" prop="icommissionstatus" min-width="100" align="center">
          <template #default="scope">
            <span>{{ scope.row.icommissionstatus }}</span>
          </template>
        </el-table-column>
        <!-- 确认说明 -->
        <!-- sconfirmremark?: string -->
        <el-table-column label="确认说明" prop="sconfirmremark" width="120" show-overflow-tooltip />
        <!-- 发放说明 -->
        <!-- spayremark?: string -->
        <el-table-column label="发放说明" prop="spayremark" width="120" show-overflow-tooltip />
        <!-- 业绩比例 -->
        <!-- archievementrate?: number -->
        <!-- <el-table-column label="业绩比例" prop="archievementrate" min-width="100" align="center">
          <template #default="scope">
            <span>{{ scope.row.archievementrate }}%</span>
          </template>
        </el-table-column> -->
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
        <!-- 合同号 -->
        <!-- String scontractnum -->
        <el-table-column label="合同号" prop="scontractnum" width="120" show-overflow-tooltip />
        <!-- 业务员名称 -->
        <!-- String employname -->
        <el-table-column label="业务员" prop="employname" width="120" show-overflow-tooltip />
        <!-- 客户名称 -->
        <!-- String customername -->
        <el-table-column label="客户名称" prop="customername" width="120" show-overflow-tooltip />
        <!-- 代理公司名称 -->
        <!-- String agencyname -->
        <el-table-column label="代理公司" prop="agencyname" width="120" show-overflow-tooltip />
        <!-- 内容生产方 -->
        <!-- String agentname -->
        <el-table-column label="内容生产方" prop="agentname" width="120" show-overflow-tooltip />
        <!-- 行业名称 -->
        <!-- String adindustryname -->
        <el-table-column label="行业名称" prop="adindustryname" width="120" show-overflow-tooltip />
        <!-- 媒体类型名称 -->
        <!-- String mediatypename -->
        <el-table-column label="媒体类型" prop="mediatypename" width="120" show-overflow-tooltip />
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
        <!-- 广告标题 -->
        <!-- String sadtitle -->
        <el-table-column label="广告标题" prop="sadtitle" width="120" show-overflow-tooltip />
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
        <!-- 部门名称 -->
        <!-- deptname?: string -->
        <el-table-column
          prop="deptname"
          label="部门名称"
          sortable="custom"
          min-width="120"
        ></el-table-column>
        <!-- 业务员类型 -->
        <!-- employtype?: number -->
        <el-table-column label="业务员类型" prop="employtype" width="120" show-overflow-tooltip
          ><template #default="scope">
            {{ customerTypeCombo.find((item) => item.id === scope.row.employtype)?.name }}
          </template></el-table-column
        >

        <!-- <el-table-column
          label="操作"
          align="center"
          width="220"
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
        </el-table-column> -->
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
  </div>
</template>

<script setup lang="ts">
import { computTableHeight, formatMoney, tableHeaderColor } from '@/utils'
import { dayjs } from 'element-plus'
import { ECustomerType, EHgDeptZtreeUrl, EPublishstatus } from '@/types/common/enumindex'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { TSelectZtree } from '@/types/common'
import { getMyCommissionCountApi, getMyCommissionPageListApi } from '@/api/personal/PersonalStat'
import { IAxios } from 'axios'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common'
import useUserStore from '@/store/modules/user'
import {
  TOrderItemCommissionDTO,
  TOrderItemCommissionVO
} from '@/types/views/commission/commissioncalcu'

const global = getCurrentInstance()?.appContext.config.globalProperties
const user = useUserStore().user

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
const queryParams = reactive<TOrderItemCommissionVO>({
  sort: 'dcreatetime',
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
const resultList = ref<TOrderItemCommissionDTO[]>([])
/**
 * 业绩汇总
 */
const sumResult = ref<TOrderItemCommissionDTO>()
/** 刊发状态列表 */
const publishStatusCombo: IDataCombo[] = getEnumCombo(EPublishstatus)
/** 客户类型下拉 */
const customerTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
onMounted(() => {
  if (user?.blead || user?.adminflag !== 0) {
    queryParams.bcurflag = false
  } else {
    queryParams.bcurflag = true
  }
  handleQuery()
  nextTick(() => {
    /**
     * 计算表格高度 - 50为页面上方导航条Navbar的高度
     */
    tableheight.value = computTableHeight() - 50
  })
})
/**
 * @description: 表格合计
 */
import type { TableColumnCtx } from 'element-plus'
interface SummaryMethodProps<T = TOrderItemCommissionDTO> {
  columns: TableColumnCtx<T>[]
  data: T[]
}
/**
 * 数据合计
 * @param param
 */
const getSummaries = (param: SummaryMethodProps) => {
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
    // if (column.property === 'amountachievement') {
    //   sums[index] = sumResult.value?.amountachievement
    //     ? '￥ ' + formatMoney(sumResult.value?.amountachievement, '2')
    //     : '￥ ' + '0'
    //   return
    // }
    if (column.property === 'namountcost') {
      sums[index] = sumResult.value?.namountcost
        ? '￥ ' + formatMoney(sumResult.value?.namountcost, '2')
        : '￥ ' + '0'
      return
    }
    if (column.property === 'ncommission') {
      sums[index] = sumResult.value?.ncommission
        ? '￥ ' + formatMoney(sumResult.value?.ncommission, '2')
        : '￥ ' + '0'
      return
    }
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
  getMyCommissionPageListApi(queryParams)
    .then((res: IAxios<TOrderItemCommissionDTO[]>) => {
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
  getMyCommissionCountApi(queryParams)
    .then((res: IAxios) => {
      if (res.success) {
        sumResult.value = res.obj
      }
    })
    .finally(() => {})
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
</script>

<style scoped lang="scss"></style>
