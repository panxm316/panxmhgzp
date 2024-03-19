<!--
 * @Author: yanz
 * @Date: 2024-01-24 15:44:45
 * @LastEditors: wanghl
 * @LastEditTime: 2024-03-12 09:32:12
 * @Description: 个人中心 - 业绩统计
 *
-->
<template>
  <div id="HgMyAchievement" class="app-container">
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
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
        show-summary
        :summary-method="getSummaries"
      >
        <!-- 广告项目名称 -->
        <!-- String adprojectname -->
        <el-table-column
          label="广告项目名称"
          prop="adprojectname"
          min-width="200"
          show-overflow-tooltip
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
        <!-- 刊期编号(自增列,广告号) -->
        <!-- Long iitemcode -->
        <el-table-column
          label="刊期编号"
          prop="iitemcode"
          width="120"
          align="center"
          show-overflow-tooltip
        />
        <!-- 广告标题 -->
        <!-- String sadtitle -->
        <el-table-column label="广告标题" prop="sadtitle" min-width="200" show-overflow-tooltip />
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
        <!-- 业务员名称 -->
        <!-- String employname -->
        <el-table-column label="业务员" prop="employname" width="120" show-overflow-tooltip />
        <!-- 业务员类型 -->
        <!-- Integer employtype -->
        <el-table-column prop="employtype" label="业务员类型" sortable="custom" min-width="120">
          <template #default="scope">
            {{ employeeTypeList.find((item) => item.id === scope.row.employtype)?.name }}
          </template>
        </el-table-column>
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
        <el-table-column label="行业名称" prop="adindustryname" width="160" show-overflow-tooltip />
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
        <!-- :width="sumResult!.amountreceivable! > 100000 ? '180px' : '120px'" -->
        <el-table-column label="应收金额" width="140" prop="amountreceivable" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 已收金额 -->
        <!-- BigDecimal amountreceived -->
        <el-table-column label="已收金额" prop="amountreceived" align="right" width="140">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 欠款金额 -->
        <!-- BigDecimal amountarrearage -->
        <el-table-column label="欠款金额" prop="amountarrearage" align="right" width="140">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 成本金额 -->
        <!-- BigDecimal namountcost -->
        <el-table-column label="成本金额" prop="namountcost" align="right" width="140">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountcost, '2') }}</span>
          </template>
        </el-table-column>
        <!-- 业绩金额 -->
        <!-- BigDecimal amountachievement -->
        <!-- <el-table-column label="业绩金额" prop="amountachievement" align="right" width="140">
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
        <el-table-column label="媒体" prop="medianame" width="120" show-overflow-tooltip />
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
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>

<script setup lang="ts">
import { computTableHeight, formatMoney, tableHeaderColor } from '@/utils'
import { dayjs } from 'element-plus'
import { EEmployeeType, EHgDeptZtreeUrl, EPublishstatus } from '@/types/common/enumindex'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IOrderAndItemDTO, TOrderAndItemVO } from '@/types/views/business/orderitemcost'
import { TSelectZtree } from '@/types/common'
import { getMyAchievementCountApi, getMyAchievementPageListApi } from '@/api/personal/PersonalStat'
import { IAxios } from 'axios'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common'
import useUserStore from '@/store/modules/user'
import { Tworderitem } from '@/types/views/ad/adtworder'

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
const queryParams = reactive<TOrderAndItemVO>({
  sort: 'createtime',
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
const sumResult = ref<Tworderitem>()

/** 刊发状态列表 */
const publishStatusCombo: IDataCombo[] = getEnumCombo(EPublishstatus)
/** 客户类型下拉列表 */
const employeeTypeList: IDataCombo[] = getEnumCombo(EEmployeeType)
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
  getMyAchievementPageListApi(queryParams)
    .then((res: IAxios<IOrderAndItemDTO[]>) => {
      if (res.success) {
        resultList.value = res.obj
        pageTotal.value = res.total
      }
    })
    .finally(() => {})
  handleQueryCount()
}
const sumResultdata = reactive<Tworderitem>({
  amountreceivable: 0,
  amountreceived: 0,
  amountarrearage: 0,
  amountachievement: 0,
  namountcost: 0
})
/**
 * 查询汇总
 */
const handleQueryCount = () => {
  getMyAchievementCountApi(queryParams)
    .then((res: IAxios) => {
      if (res.success) {
        sumResult.value = res.obj
        sumResultdata.amountreceivable = res.obj.amountreceivable || 0
        sumResultdata.amountreceived = res.obj.amountreceived || 0
        sumResultdata.amountarrearage = res.obj.amountarrearage || 0
        // sumResultdata.amountachievement = res.obj.amountachievement || 0
        sumResultdata.namountcost = res.obj.namountcost || 0
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
 * 根据合同号查看订单项目--------------------------------------------------
 * @param row
 * @param index
 */
import AdTworderdetails from '@/views/ad/AdTworderdetails.vue'
const viewImageRef = ref()
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
}
// ---------------------------------------------------------------------
</script>

<style scoped lang="scss"></style>
