<!--
 * @Author: suny
 * @Date: 2024-01-12 13:18:55
 * @LastEditTime: 2024-03-06 14:34:44
 * @LastEditors: wanghl
 * @Description: 佣金计算
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <span style="margin-top: 5px">截止日期：</span>
        <el-date-picker
          v-model="queryParams.endTime"
          :editable="false"
          type="date"
          placeholder="选择日期"
          :clearable="false"
          style="width: 130px; margin-left: 5px"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="handleQuery"
        />
        <el-select
          v-model="queryParams.mediaid"
          placeholder="请选择媒体"
          clearable
          style="width: 160px"
          @change="handleQuery"
        >
          <el-option
            v-for="item in mediaCombo"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <HgZtreeSelect
          :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
          :selectids="queryParams.deptid ? [queryParams.deptid] : []"
          style="width: 160px; height: 32px"
          :filterable="true"
          clearable
          :treeparams="{ showInactiveDepts: false }"
          @selectionztree="onSelectionDept"
        ></HgZtreeSelect>
        <el-checkbox
          v-model="queryParams.bcountflag"
          label="包含已计算"
          size="large"
          style="margin-right: 20px; margin-top: -5px"
          @change="handleQuery"
        />
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="orderItemList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
        @selection-change="handleSelectionChange"
      >
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
        <el-table-column label="广告号" prop="iitemcode" min-width="100" show-overflow-tooltip />
        <el-table-column
          label="经营主体"
          prop="businessentityname"
          min-width="140"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="客户" prop="customername" min-width="160" show-overflow-tooltip />
        <el-table-column label="部门" prop="deptname" width="120" show-overflow-tooltip />
        <el-table-column label="主业务员" prop="employname" width="120" show-overflow-tooltip />
        <el-table-column label="业务员类型" prop="employtype" width="120" show-overflow-tooltip
          ><template #default="scope">
            {{ customerTypeCombo.find((item) => item.id === scope.row.employtype)?.name }}
          </template></el-table-column
        >
        <el-table-column label="代理公司" prop="agencyname" width="120" show-overflow-tooltip />
        <el-table-column label="内容生产方" prop="agentname" width="120" show-overflow-tooltip />
        <el-table-column label="刊发日期" prop="prestartdate" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="刊发状态" prop="ipublishstatus" width="100">
          <template #default="scope">
            {{ publishStatusCombo.find((item) => item.id === scope.row.ipublishstatus)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="广告标题" prop="sadtitle" width="160" show-overflow-tooltip />
        <el-table-column label="媒体" prop="medianame" width="140" show-overflow-tooltip />
        <el-table-column label="" prop="foldname" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="foldareavername" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="pagesortname" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="addisplayformname" width="100" show-overflow-tooltip />
        <el-table-column label="" prop="adspecname" width="140" show-overflow-tooltip />
        <el-table-column
          label="应收"
          prop="amountreceivable"
          min-width="120"
          align="right"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="已收"
          prop="amountreceived"
          min-width="120"
          align="right"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="欠款"
          prop="amountarrearage"
          min-width="120"
          align="right"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业绩金额" prop="amountachievement" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountachievement, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业绩比例" prop="archievementrate" min-width="120" align="center">
          <template #default="scope">
            <span>{{ scope.row.archievementrate }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="成本金额" prop="namountcost" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountcost, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险金比例" prop="nrateofrisk" min-width="100" align="center">
          <template #default="scope">
            <span>{{ scope.row.nrateofrisk }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="风险金" prop="riskfund" min-width="100" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.riskfund, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="折扣降点" prop="discountreduce" min-width="100" align="center">
          <template #default="scope">
            <span>{{ scope.row.discountreduce }}%</span>
          </template>
        </el-table-column>
        <el-table-column
          label="计提比例"
          prop="ncommissionrate"
          min-width="140"
          align="center"
          fixed="right"
        >
          <template #default="scope">
            <div v-if="!dialogEditVisible">{{ scope.row.ncommissionrate }}%</div>
            <div v-if="curIndex === scope.$index && dialogEditVisible" style="width: 100%">
              <el-input-number
                v-model="ncommissionrate"
                :precision="2"
                :step="0.1"
                :min="0"
                :controls="false"
                style="width: 80%"
                @change="onCountByRatio(scope.row)"
              />
              <span style="margin-left: 10px; width: 10px">%</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="计提金额"
          prop="ncommission"
          min-width="120"
          align="right"
          fixed="right"
        >
          <template #default="scope">
            <span v-if="!dialogEditVisible">{{ formatMoney(scope.row.ncommission, '2') }}</span>
            <el-input-number
              v-if="curIndex === scope.$index && dialogEditVisible"
              v-model="ncommission"
              :precision="2"
              :step="0.1"
              :min="0"
              :controls="false"
              style="width: 100%"
              @blur="onCountByAmount(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="140"
          fixed="right"
          class-name="small-padding fixed-width"
        >
          <template #default="scope">
            <el-button
              v-if="!dialogEditVisible || curIndex !== scope.$index"
              link
              type="success"
              icon="Edit"
              size="small"
              title="修改"
              @click="onEdit(scope.$index, scope.row)"
              >修改</el-button
            >
            <el-button
              v-if="
                (curIndex !== scope.$index &&
                  dialogEditVisible &&
                  scope.row.twcommissionid === null) ||
                (curIndex === scope.$index && dialogEditVisible) ||
                (curIndex !== scope.$index &&
                  !dialogEditVisible &&
                  scope.row.twcommissionid === null)
              "
              link
              type="primary"
              icon="Check"
              size="small"
              title="保存"
              @click="onSave(scope.row)"
              >保存</el-button
            >
            <el-button
              v-if="curIndex === scope.$index && dialogEditVisible"
              link
              type="danger"
              icon="Close"
              size="small"
              title="取消"
              @click="onEditClose"
              >取消</el-button
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
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>
<script setup lang="ts">
import useUserStore from '@/store/modules/user'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IAxios } from 'axios'
import { EPublishstatus, ECustomerType, EHgDeptZtreeUrl } from '@/types/common/enumindex'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { dayjs } from 'element-plus'
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaDataComboApi } from '@/api/media/media'
import { getEnumCombo } from '@/api/common/index'
import { Tworderitembelong } from '@/types/views/ad/adtworder'
import { TSelectZtree } from '@/types/common'
import {
  getOrderAndItemDTOListForCommissionApi,
  saveOrUpdateCommissionApi
} from '@/api/commission/commissioncalcu'
import {
  TOrderItemCommissionVO,
  TOrderItemCommissionDTO
} from '@/types/views/commission/commissioncalcu'
import { getMonthDates } from '@/utils/getdate'

/** 刊发状态列表 */
const publishStatusCombo: IDataCombo[] = getEnumCombo(EPublishstatus)
/** 客户类型列表 */
const customerTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
const user = useUserStore().user
const total = ref(0)
/** 编辑窗口显示状态 */
const dialogEditVisible = ref(false)
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 时间 */
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
/** 本月的第一天和最后一天 */
const dateList = getMonthDates()
/** 查询条件 */
const queryParams = reactive<TOrderItemCommissionVO>({
  sort: 'createtime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: dayjs(dateList.start).add(-1, 'day').format('YYYY-MM-DD'),
  mediaid: '',
  deptid: '',
  bcountflag: false
})
/** 列表对象 */
const orderItemList = ref<TOrderItemCommissionDTO[]>([])
/** Form选中的行 */
const multipleSelection = ref<TOrderItemCommissionDTO[]>([])
/** 媒体列表 */
const mediaCombo = ref<IDataCombo[]>([])
/** 业绩归属 */
const belongEditForm = ref<Tworderitembelong>({})
/** 计提比例 */
const ncommissionrate = ref(0)
/** 计提金额 */
const ncommission = ref(0)
/** 当前选择行 */
const curIndex = ref(-1)

onMounted(() => {
  getMediaDataCombo()
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 获取媒体下拉数据
 */
const getMediaDataCombo = () => {
  getMediaDataComboApi().then((res: IAxios<IDataCombo[]>) => {
    if (res.success) {
      mediaCombo.value = res.obj
    }
  })
}
/**
 * 查询
 */
const handleQuery = () => {
  getOrderAndItemDTOListForCommissionApi(queryParams)
    .then((res: IAxios<TOrderItemCommissionDTO[]>) => {
      if (res.success) {
        orderItemList.value = res.obj
        total.value = res.total
        onEditClose()
      }
    })
    .finally(() => {})
}
/**
 * 计提比例修改事件
 * @param row
 */
const onEdit = (index: number, row: TOrderItemCommissionDTO) => {
  ncommissionrate.value = row.ncommissionrate!
  ncommission.value = row.ncommission!
  dialogEditVisible.value = true
  curIndex.value = index
}
/**
 * 编辑取消事件
 */
const onEditClose = () => {
  dialogEditVisible.value = false
  curIndex.value = -1
}
/**
 * 修改比例计算金额
 */
const onCountByRatio = (row: TOrderItemCommissionDTO) => {
  ncommission.value =
    (row.amountachievement! *
      (row.archievementrate! / 100) *
      (ncommissionrate.value - row.discountreduce!)) /
    100
}
/**
 * 修改金额计算比例
 * @param row
 */
const onCountByAmount = (row: TOrderItemCommissionDTO) => {
  ncommissionrate.value =
    (ncommission.value * 100) / (row.amountachievement! * (row.archievementrate! / 100)) +
    row.discountreduce!
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
 * 保存计算结果
 * @param row
 */
const onSave = (row: TOrderItemCommissionDTO) => {
  if (dialogEditVisible.value) {
    row.ncommissionrate = ncommissionrate.value
    row.ncommission = ncommission.value
  }
  saveOrUpdateCommissionApi(row)
    .then((res: IAxios<TOrderItemCommissionDTO>) => {
      if (res.success) {
        dialogEditVisible.value = false
        curIndex.value = -1
        handleQuery()
      }
    })
    .finally(() => {})
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
 * 行选择事件
 * @param val
 */
const handleSelectionChange = (val: TOrderItemCommissionDTO[]) => {
  multipleSelection.value = val
}
/**
 * 改变页码总数时
 */
const handleSizeChange = (val: number) => {
  queryParams.pageSize = val
  handleQuery()
}
/**
 * 改变页数时
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryParams.page = val
  handleQuery()
}
/**
 * 根据合同号查看订单项目--------------------------------------------------
 * @param row
 * @param index
 */
import AdTworderdetails from '../ad/AdTworderdetails.vue'
const viewImageRef = ref()
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
}
// ---------------------------------------------------------------------
</script>
<style lang="scss" scoped></style>
