<!--
 * @Author: suny
 * @Date: 2023-11-22 13:39:44
 * @LastEditTime: 2024-03-12 08:28:56
 * @LastEditors: suny
 * @Description: 欠款原因查询
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-checkbox
          v-model="btime"
          inline-prompt
          style="margin-top: 5px; margin-right: 10px"
          label="自定义时间"
          @change="handleQuery"
        />
        <HgDateIndex v-if="btime" :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <el-text v-if="!btime" style="margin-left: -10px; margin-right: 5px"
          ><el-input-number
            v-model="idebtmonthlimit"
            style="width: 60px"
            :controls="false"
            :min="1"
            :max="100"
          />个月之前</el-text
        >
        <el-input
          v-model="queryParams.scontractnum"
          clearable
          placeholder="请输入合同号"
          class="input-with-select"
          style="width: 160px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-input
          v-model="queryParams.invoice"
          clearable
          placeholder="请输入发票号"
          class="input-with-select"
          style="width: 160px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-input
          v-model="queryParams.queryKey"
          clearable
          placeholder="请输入客户关键字"
          class="input-with-select"
          style="width: 200px"
          @keyup.enter="handleQuery"
          @clear="handleQuery"
        >
        </el-input>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </div>
    </div>
    <div class="table_box">
      <el-table
        ref="dataTableRef"
        :data="debtReasonList"
        highlight-current-row
        :height="tableheight"
        :header-cell-style="tableHeaderColor"
        :border="true"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" :align="'center'" />
        <el-table-column label="部门" prop="deptname" min-width="120" show-overflow-tooltip />
        <el-table-column label="客户" prop="customername" min-width="120" show-overflow-tooltip />
        <el-table-column label="代理公司" prop="agencyname" width="120" show-overflow-tooltip />
        <el-table-column label="内容生产方" prop="agentname" width="120" />
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
        <el-table-column label="主业务员" prop="employname" width="100" />
        <el-table-column label="代理公司主业务员" prop="agencyemployname" width="160" />
        <el-table-column
          label="内容生产方业务员"
          prop="agentemployname"
          min-width="160"
          show-overflow-tooltip
        />
        <el-table-column label="广告标题" prop="sadtitle" width="160" show-overflow-tooltip />
        <el-table-column label="刊发日期" prop="dpublishdate" width="100">
          <template #default="scope">
            <span>{{ dayjs(scope.row.dpublishdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="应收"
          prop="amountreceivable"
          min-width="120"
          show-overflow-tooltip
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="已收"
          prop="amountreceived"
          min-width="120"
          show-overflow-tooltip
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="欠款"
          prop="amountarrearage"
          min-width="120"
          show-overflow-tooltip
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <!-- <el-table-column
          label="发票金额"
          prop="namountinvoice"
          min-width="120"
          show-overflow-tooltip
          align="right"
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountinvoice, '2') }}</span>
          </template>
        </el-table-column> -->
        <el-table-column label="发票号" prop="invoice" width="160" show-overflow-tooltip />
        <el-table-column label="催告情况" prop="snoticecontent" width="160" show-overflow-tooltip />
        <el-table-column label="欠款原因" prop="sdebtreason" width="160" show-overflow-tooltip />
        <el-table-column label="归类原因" prop="scategory" width="160" show-overflow-tooltip />
        <el-table-column
          label="计划回款时间"
          prop="srepaymentdate"
          width="160"
          show-overflow-tooltip
        />
        <el-table-column label="风险等级" prop="srisklevel" width="160" show-overflow-tooltip />
        <el-table-column label="法务介入" prop="blegal" width="160" align="center">
          <template #default="scope">
            <el-checkbox v-model="scope.row.blegal" disabled></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="140"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              v-if="!scope.row.bconfirm"
              link
              type="primary"
              icon="Top"
              size="small"
              title="填报数据"
              @click="onEdit(scope.row)"
              >填报</el-button
            >
            <el-button
              v-if="!scope.row.bconfirm && scope.row.debtreasonid !== null"
              link
              type="success"
              icon="Top"
              size="small"
              title="提交数据"
              @click="onPush(scope.row)"
              >提交</el-button
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
    <!-- 编辑 -->
    <el-dialog
      v-model="dialogEditVisible"
      title="欠款原因编辑"
      width="700"
      append-to-body
      :destroy-on-close="true"
    >
      <div style="width: 92%">
        <el-form
          ref="debtReasonFormRef"
          :model="debtReasonForm"
          label-width="120px"
          class="demo-workReportForm"
          status-icon
        >
          <el-form-item label="广告标题" prop="sadtitle">
            <el-input v-model="debtReasonForm.sadtitle" disabled class="inputwindht" />
          </el-form-item>
          <el-form-item label="催告情况" prop="snoticecontent">
            <el-input
              v-model="debtReasonForm.snoticecontent"
              :rows="3"
              type="textarea"
              class="inputwindht"
              resize="none"
              maxlength="100"
              show-word-limit
              placeholder="请输入..."
            />
          </el-form-item>
          <el-form-item label="欠款原因" prop="sdebtreason">
            <el-input
              v-model="debtReasonForm.sdebtreason"
              :rows="3"
              type="textarea"
              class="inputwindht"
              resize="none"
              maxlength="100"
              show-word-limit
              placeholder="请输入..."
            />
          </el-form-item>
          <el-form-item label="归类原因" prop="scategory">
            <el-input
              v-model="debtReasonForm.scategory"
              :rows="3"
              type="textarea"
              class="inputwindht"
              resize="none"
              maxlength="100"
              show-word-limit
              placeholder="请输入..."
            />
          </el-form-item>
          <el-form-item label="计划回款时间" prop="srepaymentdate">
            <el-input
              v-model="debtReasonForm.srepaymentdate"
              clearable
              placeholder="请输入回款时间"
              class="inputwindht"
            >
            </el-input>
          </el-form-item>
          <el-form-item label="风险等级" prop="srisklevel">
            <el-select
              v-model="debtReasonForm.srisklevel"
              filterable
              allow-create
              default-first-option
              :reserve-keyword="false"
              placeholder="请选择风险等级"
              class="inputwindht"
            >
              <el-option label="高风险" value="高风险" />
              <el-option label="中风险" value="中风险" />
              <el-option label="低风险" value="低风险" />
              <el-option label="无法回款" value="无法回款" />
            </el-select>
          </el-form-item>
          <el-form-item label="法务介入" prop="blegal">
            <el-checkbox v-model="debtReasonForm.blegal"></el-checkbox>
          </el-form-item>
          <el-form-item>
            <div style="position: absolute; right: -25px">
              <el-checkbox v-model="debtReasonForm.bconfirm" style="margin-right: 10px"
                >同时提交(提交后不允许修改)</el-checkbox
              >
              <el-button type="primary" icon="Check" @click="updateDebtReason">保存</el-button>
              <el-button icon="Close" @click="dialogEditVisible = false">取消</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>

<script setup lang="ts">
import { getDebtReasonPageListApi, updateDebtReasonApi } from '@/api/business/debtreason'
import useUserStore from '@/store/modules/user'
import { TSelectZtree } from '@/types/common'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { ITwdebtreason, TDebtReasonVO } from '@/types/views/business/debtreason'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { IAxios } from 'axios'
import { FormInstance, dayjs } from 'element-plus'
import { getOrderDebtPageListApi } from '@/api/finance/orderdebtstat'
import { getMonthNumDate } from '@/utils/getdate'

const user = useUserStore().user
/** 编辑窗口显示状态 */
const dialogEditVisible = ref(false)
/** 是否自定义时间 */
const btime = ref(false)
const total = ref(0)
/** 表格高度 */
const tableheight = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])

const debtReasonFormRef = ref<FormInstance>()
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
/** 查询条件 */
const queryParams = reactive<TDebtReasonVO>({
  sort: 'createtime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  deptid: '',
  scontractnum: '',
  invoice: '',
  bfinance: false // 非财务人员
})
const debtReasonInfo: ITwdebtreason = {
  id: '',
  snoticecontent: '',
  sdebtreason: '',
  scategory: '',
  srepaymentdate: '',
  srisklevel: '',
  blegal: false,
  orderitemid: ''
  // bconfirm: false
}
/** 编辑实体 */
const debtReasonForm = ref<ITwdebtreason>({ ...debtReasonInfo })
/** 列表对象 */
const debtReasonList = ref<IOrderDebtDTO[]>([])
/** Form选中的行 */
const multipleSelection = ref<IOrderDebtDTO[]>([])
/** 查询欠款月限 */
const idebtmonthlimit = ref(0)

onMounted(() => {
  getDebtMonthLimit()
  handleQuery()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/**
 * 获取默认欠款月限
 */
const getDebtMonthLimit = () => {
  const data = {
    key: 'debtMonthLimit'
  }
  getParameterByKeyApi(data)
    .then((res: IAxios) => {
      if (res.success) {
        idebtmonthlimit.value = res.obj === '' ? 0 : Number(res.obj)
      }
    })
    .finally(() => {})
}
/**
 * 查询
 */
const handleQuery = () => {
  if (!btime.value) {
    const date = getMonthNumDate(idebtmonthlimit.value)
    queryParams.startTime = ''
    queryParams.endTime = date.end
  }
  getOrderDebtPageListApi(queryParams)
    // getDebtReasonPageListApi(queryParams)
    .then((res: IAxios<IOrderDebtDTO[]>) => {
      if (res.success) {
        debtReasonList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: IOrderDebtDTO) => {
  dialogEditVisible.value = true
  debtReasonInfo.id = row.debtreasonid
  debtReasonInfo.sadtitle = row.sadtitle
  debtReasonInfo.snoticecontent = row.snoticecontent
  debtReasonInfo.sdebtreason = row.sdebtreason
  debtReasonInfo.scategory = row.scategory
  debtReasonInfo.srepaymentdate = row.srepaymentdate
  debtReasonInfo.srisklevel = row.srisklevel
  debtReasonInfo.blegal = row.blegal
  debtReasonInfo.orderitemid = row.orderitemid
  debtReasonForm.value = { ...debtReasonInfo }
}
/**
 * 更新保存欠款原因
 */
const updateDebtReason = () => {
  updateDebtReasonApi(debtReasonForm.value)
    .then((res: IAxios<IOrderDebtDTO>) => {
      if (res.success) {
        ElMessage.success('保存成功')
        dialogEditVisible.value = false
        debtReasonForm.value = { ...debtReasonInfo }
        debtReasonFormRef.value?.resetFields()
        handleQuery()
      }
    })
    .finally(() => {})
}
/**
 * 提交确认信息
 * @param row
 */
const onPush = (row: IOrderDebtDTO) => {
  debtReasonInfo.id = row.id
  ElMessageBox.confirm('是否提交选择的数据，提交后将不允许修改？', '提示', {
    type: 'warning'
  })
    .then(() => {
      row.bconfirm = true
      updateDebtReasonApi(debtReasonInfo)
        .then((res: IAxios<IOrderDebtDTO>) => {
          if (res.success) {
            ElMessage.success('提交成功')
            handleQuery()
          }
        })
        .finally(() => {})
    })
    .catch(() => {})
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
const handleSelectionChange = (val: IOrderDebtDTO[]) => {
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
import { IOrderDebtDTO } from '@/types/views/finance/orderdebtstat'
import { de } from 'element-plus/es/locale'
import { getParameterByKeyApi } from '@/api/system/parameter'
const viewImageRef = ref()
const handleSee = (row?: any, number?: number) => {
  viewImageRef.value.view(row.scontractnum)
}
// ---------------------------------------------------------------------
</script>

<style scoped></style>
