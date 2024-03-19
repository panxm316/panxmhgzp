<!--
 * @Author: yanz
 * @Date: 2024-01-24 15:44:45
 * @LastEditors: suny suny@hgzp.com.cn
 * @LastEditTime: 2024-02-01 10:44:13
 * @Description: 个人中心  - 我的发票
 *
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
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
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
      >
        <!-- 这个表格展示的 IInvoiceDTO 属性不全，缺少入账、已用、剩余金额 3项 -->
        <!-- 完整 IInvoiceDTO 展示的在 InvoiceLedger.vue -->
        <el-table-column label="发票号码" prop="invoice" width="140" show-overflow-tooltip />
        <el-table-column label="发票代码" prop="invoicecode" width="160" show-overflow-tooltip />
        <el-table-column label="开票日期" prop="dcreatetime" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发票类型" prop="itype" width="100" :align="'center'">
          <template #default="scope">
            {{ preinvoiceApplyTypeCombo.find((item) => item.id === scope.row.itype)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="开票项目" prop="printitemname" width="120" show-overflow-tooltip />
        <el-table-column label="发票金额" prop="namount" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namount, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="欠款金额" prop="amountarrearage" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="客户" prop="customername" min-width="160" show-overflow-tooltip />
        <el-table-column label="客户类型" prop="icusttype" min-width="100" show-overflow-tooltip>
          <template #default="scope">
            {{ customerTypeCombo.find((item) => item.id === scope.row.icusttype)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="打印名称" prop="sprintname" min-width="120" />
        <el-table-column label="主业务员" prop="employname" width="100" show-overflow-tooltip />
        <el-table-column label="发票样式" prop="istype" min-width="100" show-overflow-tooltip>
          <template #default="scope">
            {{ preinvoiceStyleCombo.find((item) => item.id === scope.row.istype)?.name }}
          </template>
        </el-table-column>
        <el-table-column
          label="经营主体"
          prop="businessentityname"
          min-width="140"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="发票状态" prop="istatus" min-width="100" show-overflow-tooltip>
          <template #default="scope">
            {{ invoiceStatusCombo.find((item) => item.id === scope.row.istatus)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="sremark" min-width="120"></el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="80"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              icon="view"
              size="small"
              title="查看详情"
              @click="onDetail(scope.row)"
              >详情</el-button
            >
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
    <!-- 发票详情 -->
    <el-dialog
      v-model="dialogDetailVisible"
      title="发票详情"
      align-center
      draggable
      width="1100"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div style="width: 900px">
        <el-form
          ref="orderDetailFormRef"
          :model="invoiceDetailForm"
          :inline="true"
          label-width="100px"
          class="demo-workReportForm"
          status-icon
        >
          <el-form-item label="预开信息">
            <div style="width: 100%">
              <el-table
                ref="fileTableRef"
                :data="invoiceDetailForm.preInvoiceApplicationDTOList"
                highlight-current-row
                :header-cell-style="tableHeaderColor"
                :border="true"
              >
                <el-table-column
                  label="部门"
                  prop="deptname"
                  min-width="150"
                  show-overflow-tooltip
                />
                <el-table-column
                  label="业务员"
                  prop="employname"
                  min-width="100"
                  show-overflow-tooltip
                />
                <el-table-column label="申请日期" prop="dapplytime" width="100">
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.dapplytime).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="开票日期" prop="realInvoiceDate" width="100">
                  <template #default="scope">
                    <span v-if="scope.row.realInvoiceDate">{{
                      dayjs(scope.row.realInvoiceDate).format('YYYY-MM-DD')
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="发票号" prop="invoice" width="150" />
                <el-table-column label="开票金额" prop="namountapply" width="150" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="已还金额" prop="namountreceived" width="150" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-form-item>
          <el-form-item label="预收款信息">
            <el-table
              ref="advanceChargeTableRef"
              :data="invoiceDetailForm.customerChargeBankDTOList"
              highlight-current-row
              :header-cell-style="tableHeaderColor"
              :border="true"
              style="width: 900px"
            >
              <el-table-column label="客户编号" prop="icode" width="100" />
              <el-table-column label="客户名称" prop="customername" show-overflow-tooltip />
              <el-table-column label="客户属性" prop="customeritype" show-overflow-tooltip>
                <template #default="scope">
                  {{ customerTypeCombo.find((item) => item.id === scope.row.customeritype)?.name }}
                </template>
              </el-table-column>
              <el-table-column label="经营主体" prop="businessentityname" show-overflow-tooltip />
              <el-table-column label="业务员" prop="employname" width="100" show-overflow-tooltip />
              <el-table-column label="发票号" prop="invoice" show-overflow-tooltip />
              <el-table-column label="收款金额" prop="namountreceived" width="150" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="剩余金额" prop="namountbalance" width="150" align="right">
                <template #default="scope">
                  <span>{{ formatMoney(scope.row.namountbalance, '2') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="开票名称" prop="printitemname" width="100" />
              <el-table-column label="开票日期" prop="dcreatetime" width="120">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="收费日期" prop="dpaydate" width="120">
                <template #default="scope">
                  <span>{{ dayjs(scope.row.dpaydate).format('YYYY-MM-DD') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="状态" prop="istatus" width="120">
                <template #default="scope">
                  {{ statusCombo.find((item) => item.id === scope.row.istatus)?.name }}
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
          <el-form-item label="核销数据">
            <div style="width: 900px">
              <el-table
                ref="orderApportiondetailTableRef"
                :data="invoiceDetailForm.orderApportiondetailDTOList"
                show-summary
                :summary-method="getSummaries"
                highlight-current-row
                :header-cell-style="tableHeaderColor"
                :border="true"
              >
                <el-table-column label="合同号" prop="scontractnum" show-overflow-tooltip />
                <el-table-column label="经营主体" prop="businessentityname" show-overflow-tooltip />
                <el-table-column label="直接客户" prop="customername" show-overflow-tooltip />
                <el-table-column label="代理公司" prop="agencyname" show-overflow-tooltip />
                <el-table-column
                  label="内容生产方"
                  prop="agentname"
                  min-width="120"
                  show-overflow-tooltip
                />
                <el-table-column label="刊发日期" prop="prestartdate" show-overflow-tooltip>
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="媒体" prop="medianame" show-overflow-tooltip />
                <el-table-column label="广告标题" prop="sadtitle" show-overflow-tooltip />
                <el-table-column
                  label="签订金额"
                  prop="amountreceivable"
                  min-width="120"
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="已收金额"
                  prop="amountreceived"
                  min-width="120"
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="欠款金额"
                  prop="amountarrearage"
                  min-width="120"
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  label="分摊金额"
                  prop="namountapportion"
                  min-width="120"
                  align="right"
                >
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountapportion, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="发票号" prop="invoice" show-overflow-tooltip />
                <el-table-column label="收费日期" prop="prestartdate" show-overflow-tooltip>
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="分摊日期" prop="dapportiondate" show-overflow-tooltip>
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.dapportiondate).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="收款种类" prop="sapportiontype" show-overflow-tooltip>
                  <template #default="scope">
                    {{ stypeCombo.find((item) => item.id === scope.row.sapportiontype)?.name }}
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-form-item>
          <el-form-item label="分配明细">
            <div style="width: 900px">
              <el-table
                ref="bankAccountChargeTableRef"
                :data="invoiceDetailForm.allocationDetails"
                highlight-current-row
                :header-cell-style="tableHeaderColor"
                :border="true"
              >
                <el-table-column label="客户名称" prop="customername" />
                <el-table-column label="业务员" prop="employname" />
                <el-table-column label="发票号" prop="invoice" />
                <el-table-column label="发票金额" prop="namountapply" width="120" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountapply, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="分配金额" prop="namountreceived" width="120" align="right">
                  <template #default="scope">
                    <span>{{ formatMoney(scope.row.namountreceived, '2') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="合同号" prop="scontractnum" width="150" />
                <el-table-column label="收款类型" prop="stype" width="150" />
                <el-table-column label="付款方式" prop="paymethodname" width="150" />
                <el-table-column label="创建者" prop="createempname" width="100" />
                <el-table-column label="创建日期" prop="dcreatetime" width="120">
                  <template #default="scope">
                    <span>{{ dayjs(scope.row.dcreatetime).format('YYYY-MM-DD') }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="状态" prop="istatus" width="120">
                  <template #default="scope">
                    {{ statusCombo.find((item) => item.id === scope.row.istatus)?.name }}
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computTableHeight, formatMoney, tableHeaderColor } from '@/utils'
import { dayjs } from 'element-plus'
import {
  ECustomerType,
  EHgDeptZtreeUrl,
  EInvoiceStatus,
  EPreinvoiceApplyType,
  EPreinvoiceStyle,
  EStatus,
  ESType
} from '@/types/common/enumindex'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { TSelectZtree } from '@/types/common'
import { getMyInvoicePageListApi } from '@/api/personal/PersonalStat'
import { IAxios } from 'axios'
import { IDataCombo } from '@/types/common/DataCombo'
import { getEnumCombo } from '@/api/common'
import useUserStore from '@/store/modules/user'
import { IInvoiceDTO, TInvoiceQuery } from '@/types/views/finance/invoice'
import { getInvoiceDetailsByInvoiceApi } from '@/api/finance/dataledger'
import { InvoiceDetailsDTO } from '@/types/views/finance/dataledger'

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
const queryParams = reactive<TInvoiceQuery>({
  sort: 'dcreatetime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  // 是否只查询当前人员（0：否 1：是，如果是则表示只查询当前人员）
  bcurflag: ''
})
/**
 * 查询结果列表对象
 */
const resultList = ref<IInvoiceDTO[]>([])
/**
 * 业绩汇总
 */
const sumResult = ref(0)
/** 收款类型下拉 */
const stypeCombo: IDataCombo[] = getEnumCombo(ESType)
/** 预开发票类型下拉 */
const preinvoiceApplyTypeCombo: IDataCombo[] = getEnumCombo(EPreinvoiceApplyType)
/** 客户类型下拉 */
const customerTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
/** 开票样式下拉 */
const preinvoiceStyleCombo: IDataCombo[] = getEnumCombo(EPreinvoiceStyle)
/** 发票状态下拉 */
const invoiceStatusCombo: IDataCombo[] = getEnumCombo(EInvoiceStatus)
/** 状态下拉 */
const statusCombo: IDataCombo[] = getEnumCombo(EStatus)
/** 查看详情窗口显示状态 */
const dialogDetailVisible = ref(false)
/** 详情对象 */
const invoiceDetailForm = ref<InvoiceDetailsDTO>({})

onMounted(() => {
  if (user?.blead || user?.adminflag !== 0) {
    queryParams.bcurflag = 'false'
  } else {
    queryParams.bcurflag = 'true'
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
 * 合计
 * @param param
 */
const getSummaries = (param: any) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column: any, index: number) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    if (
      column.property !== 'amountreceivable' &&
      column.property !== 'amountreceived' &&
      column.property !== 'amountarrearage' &&
      column.property !== 'namountapportion'
    ) {
      sums[index] = ''
      return
    }
    const values = data.map((item: any) => Number(item[column.property]))
    if (!values.every((value: any) => Number.isNaN(value))) {
      sums[index] =
        '￥ ' +
        formatMoney(
          `${values.reduce((prev: any, curr: any) => {
            const value = Number(curr)
            if (!Number.isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0)}`,
          '2'
        )
    } else {
      sums[index] = ''
    }
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
  if (!JSON.parse(queryParams.bcurflag as string) && !queryParams.employid) {
    // modal.alert('查全部人员时，部门和业务员至少要选择一项')
    // queryParams.bcurflag = true
    // return
    queryParams.employid = user.userid
  }
  getMyInvoicePageListApi(queryParams)
    .then((res: IAxios<IInvoiceDTO[]>) => {
      if (res.success) {
        resultList.value = res.obj
        pageTotal.value = res.total
      }
    })
    .finally(() => {})
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
 * 查看发票详情按钮事件
 * @param row
 */
const onDetail = (row: IInvoiceDTO) => {
  const data = {
    invoiceid: row.id!
  }
  getInvoiceDetailsByInvoiceApi(data)
    .then((res: IAxios<InvoiceDetailsDTO>) => {
      if (res.success) {
        dialogDetailVisible.value = true
        invoiceDetailForm.value = res.obj
      }
    })
    .finally(() => {})
}
</script>

<style scoped lang="scss"></style>
