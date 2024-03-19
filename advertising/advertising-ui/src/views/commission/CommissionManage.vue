<!--
 * @Author: yanz
 * @Date: 2024-01-16 17:07:57
 * @LastEditors: suny
 * @LastEditTime: 2024-03-16 14:02:07
 * @Description:佣金确认
 *
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <el-button type="primary" icon="Edit" title="确认" @click="onConfirm(undefined)"
          >确认</el-button
        >
        <el-button type="success" icon="Check" title="发放" @click="onPay(undefined)"
          >发放</el-button
        >
        <el-button type="danger" icon="Delete" title="删除" @click="onDelete(undefined)"
          >删除</el-button
        >
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
        <HgZtreeSelect
          :scopeflag="EHgDeptZtreeUrl.DEPT_GETDEPTTREE"
          :selectids="queryParams.deptid ? [queryParams.deptid] : []"
          style="width: 120px"
          :filterable="true"
          placeholder="请选择部门"
          :treeparams="{ showInactiveDepts: false }"
          clearable
          @selectionztree="onSelectionDept"
        ></HgZtreeSelect>
        <HgZtreeSelect
          :scopeflag="EHgDeptZtreeUrl.EMP_GETDEPTEMPLOYPERSON"
          :selectids="queryParams.employid ? [queryParams.employid] : []"
          style="width: 120px"
          :filterable="true"
          placeholder="请选择人员"
          clearable
          :treeparams="{ bIgnorePermissions: true }"
          @selectionztree="onSelectionEmp"
        ></HgZtreeSelect>
        <el-select
          v-model="queryParams.icommissionstatus"
          placeholder="请选择计提状态"
          clearable
          style="width: 150px; margin-right: 5px"
          @change="handleQuery"
          @clear="handleQuery"
        >
          <el-option
            v-for="t in commissionStatusCombo"
            :key="t.id"
            :label="t.name"
            :value="t.id"
          ></el-option>
        </el-select>
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
        <el-table-column type="selection" prop="ischeck" :width="50" align="center" />
        <el-table-column
          label="合同号"
          prop="scontractnum"
          align="center"
          min-width="140"
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
        <el-table-column label="广告号" prop="iitemcode" width="100" show-overflow-tooltip />
        <el-table-column
          label="经营主体"
          prop="businessentityname"
          min-width="140"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="客户" prop="customername" min-width="160" show-overflow-tooltip />
        <el-table-column label="代理公司" prop="agencyname" width="140" show-overflow-tooltip />
        <el-table-column label="内容生产方" prop="agentname" width="140" show-overflow-tooltip />
        <el-table-column label="部门" prop="deptname" width="120" show-overflow-tooltip />
        <el-table-column label="主业务员" prop="employname" width="120" show-overflow-tooltip />
        <el-table-column label="业务员类型" prop="employtype" width="120" show-overflow-tooltip
          ><template #default="scope">
            {{ customerTypeCombo.find((item) => item.id === scope.row.employtype)?.name }}
          </template></el-table-column
        >
        <el-table-column label="刊发日期" prop="prestartdate" width="120">
          <template #default="scope">
            <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="刊发状态" prop="ipublishstatus" width="100" align="center">
          <template #default="scope">
            {{ publishStatusCombo.find((item) => item.id === scope.row.ipublishstatus)?.name }}
          </template>
        </el-table-column>
        <el-table-column label="广告标题" prop="sadtitle" min-width="160" show-overflow-tooltip />
        <el-table-column label="媒体" prop="medianame" min-width="120" show-overflow-tooltip />
        <el-table-column label="叠次" prop="foldname" min-width="100" show-overflow-tooltip />
        <el-table-column
          label="叠次版本"
          prop="foldareavername"
          width="100"
          show-overflow-tooltip
        />
        <el-table-column label="版面类别" prop="pagesortname" width="100" show-overflow-tooltip />
        <el-table-column
          label="广告形式"
          prop="addisplayformname"
          width="100"
          show-overflow-tooltip
        />
        <el-table-column label="广告规格" prop="adspecname" width="140" show-overflow-tooltip />
        <el-table-column
          label="应收"
          prop="amountreceivable"
          align="right"
          width="120"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="已收"
          prop="amountreceived"
          align="right"
          width="120"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="欠款"
          prop="amountarrearage"
          align="right"
          width="120"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业绩金额" prop="amountachievement" align="right" width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amountachievement, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="业绩比例" prop="archievementrate" align="center" width="100">
          <template #default="scope">
            <span>{{ scope.row.archievementrate }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="成本金额" prop="namountcost" align="right" min-width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.namountcost, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险金比例" prop="nrateofrisk" align="center" min-width="100">
          <template #default="scope">
            <span>{{ scope.row.nrateofrisk }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="风险金" prop="riskfund" align="right" min-width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.riskfund, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="计提比例" prop="ncommissionrate" align="center" min-width="140">
          <template #default="scope">
            <span>{{ scope.row.ncommissionrate }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="计提金额" prop="ncommission" align="right" min-width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.ncommission, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="计提状态" prop="icommissionstatus" width="120">
          <template #default="scope">
            {{
              commissionStatusCombo.find((item) => item.id === scope.row.icommissionstatus)?.name
            }}
          </template>
        </el-table-column>
        <el-table-column label="确认日期" prop="dconfirmdate" width="120">
          <template #default="scope">
            <span>{{
              scope.row.dconfirmdate === null
                ? ''
                : dayjs(scope.row.dconfirmdate).format('YYYY-MM-DD')
            }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发放日期" prop="dpaydate" width="120">
          <template #default="scope">
            <span>{{
              scope.row.dpaydate === null ? '' : dayjs(scope.row.dpaydate).format('YYYY-MM-DD')
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="120"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              v-if="scope.row.icommissionstatus == ECommissionStatus.计算"
              link
              type="primary"
              icon="Edit"
              size="small"
              title="确认"
              @click="onConfirm(scope.row)"
              >确认</el-button
            >
            <el-button
              v-if="scope.row.icommissionstatus == ECommissionStatus.确认"
              link
              type="success"
              icon="Check"
              size="small"
              title="发放"
              @click="onPay(scope.row)"
              >发放</el-button
            >
            <el-button
              v-if="
                scope.row.icommissionstatus == ECommissionStatus.计算 ||
                scope.row.icommissionstatus == ECommissionStatus.确认
              "
              link
              type="danger"
              icon="Delete"
              size="small"
              title="删除"
              @click="onDelete(scope.row)"
              >删除</el-button
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
  </div>
  <!-- 确认 -->
  <el-dialog
    v-model="dialogConfirmVisible"
    title="填写确认说明"
    width="1000"
    append-to-body
    destroy-on-close
    :close-on-click-modal="false"
    @close="onCancelConfirm"
  >
    <el-input
      v-model="formDTO.sconfirmremark"
      type="textarea"
      placeholder="请输入确认说明"
      :autosize="{ minRows: 2, maxRows: 4 }"
    ></el-input>
    <el-button type="primary" icon="Edit" @click="onSubmit()">确 认</el-button>
    <el-button icon="Close" type="info" @click="onCancelConfirm">取 消</el-button>
  </el-dialog>
  <!-- 发放 -->
  <el-dialog
    v-model="dialogPayVisible"
    title="填写发放说明"
    width="1000"
    append-to-body
    destroy-on-close
    :close-on-click-modal="false"
    @close="onCancelPay"
  >
    <el-input
      v-model="formDTO.spayremark"
      type="textarea"
      placeholder="请输入发放说明"
      :autosize="{ minRows: 2, maxRows: 4 }"
    ></el-input>
    <el-button type="success" icon="Check" @click="onSubmit()">发 放</el-button>
    <el-button icon="Close" type="info" @click="onCancelPay">取 消</el-button>
  </el-dialog>
  <!-- 根据合同号查看详情 -->
  <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
</template>
<script setup lang="ts">
import useUserStore from '@/store/modules/user'
import modal from '@/plugins/modal'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IAxios } from 'axios'
import {
  ECommissionStatus,
  EPublishstatus,
  ECustomerType,
  EHgDeptZtreeUrl
} from '@/types/common/enumindex'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { dayjs } from 'element-plus'
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaDataComboApi } from '@/api/media/media'
import { getEnumCombo } from '@/api/common/index'
// import { Tworderitembelong } from '@/types/views/ad/adtworder'
import { TSelectZtree } from '@/types/common'
import {
  getCommissionListApi,
  saveCommissionApi,
  deleteCommissionByIdApi
} from '@/api/commission/commissionmanage'
// import { getOrderAndItemDTOListForCommissionApi } from '@/api/commission/commissioncalcu'
import {
  TOrderItemCommissionVO,
  TOrderItemCommissionDTO
} from '@/types/views/commission/commissioncalcu'

/** 刊发状态列表 */
const publishStatusCombo: IDataCombo[] = getEnumCombo(EPublishstatus)
/** 计提状态下拉列表 */
const commissionStatusCombo: IDataCombo[] = getEnumCombo(ECommissionStatus)
/** 客户类型列表 */
const customerTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
// const user = useUserStore().user
const total = ref(0)
/** 确认窗口显示状态 */
const dialogConfirmVisible = ref(false)
/** 发放窗口显示状态 */
const dialogPayVisible = ref(false)
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
/** 查询条件 */
const queryParams = reactive<TOrderItemCommissionVO>({
  sort: 'dcreatetime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  deptid: '',
  employid: '',
  icommissionstatus: undefined
})
/** 列表对象 */
const orderItemList = ref<TOrderItemCommissionDTO[]>([])
/** Form选中的行 */
const multipleSelection = ref<TOrderItemCommissionDTO[]>([])
const forminfo: TOrderItemCommissionDTO = {
  scontractnum: '',
  iitemcode: 0,
  businessentityname: '',
  customername: '',
  agencyname: '',
  agentname: '',
  deptname: '',
  employname: '',
  employtype: 0,
  prestartdate: '',
  ipublishstatus: 0,
  sadtitle: '',
  medianame: '',
  foldname: '',
  foldareavername: '',
  pagesortname: '',
  addisplayformname: '',
  adspecname: '',
  amountreceivable: 0,
  amountreceived: 0,
  amountarrearage: 0,
  amountachievement: 0,
  archievementrate: 0,
  namountcost: 0,
  nrateofrisk: 0,
  riskfund: 0,
  ncommissionrate: 0,
  ncommission: 0,
  icommissionstatus: 0,
  dconfirmdate: '',
  dpaydate: '',
  sconfirmremark: '',
  spayremark: ''
} as TOrderItemCommissionDTO
/** Form对象 */
const formDTO = ref<TOrderItemCommissionDTO>({ ...forminfo })
/** 媒体列表 */
const mediaCombo = ref<IDataCombo[]>([])
// /** 业绩归属 */
// const belongEditForm = ref<Tworderitembelong>({})
// /** 计提比例 */
// const ncommissionrate = ref(0)
// /** 计提金额 */
// const ncommission = ref(0)
// /** 当前选择行 */
// const curIndex = ref(0)

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
  getCommissionListApi(queryParams)
    .then((res: IAxios<TOrderItemCommissionDTO[]>) => {
      if (res.success) {
        orderItemList.value = res.obj
        console.log('orderItemList', orderItemList.value)
        total.value = res.total
      }
    })
    .finally(() => {})
  tableheight.value = computTableHeight()
}
/**
 * 确认/发放 处理
 */
const handleAction = (status: ECommissionStatus, dialogVisible: Ref<boolean>) => {
  // formDTO.value = row
  let blag = false
  let message = ''
  multipleSelection.value.forEach((item) => {
    if (
      status === ECommissionStatus.确认 &&
      (item.icommissionstatus === ECommissionStatus.发放 ||
        item.icommissionstatus === ECommissionStatus.确认)
    ) {
      message = '已发放或者已确认的数据不能确认'
      blag = true
      return false
    }
    if (status === ECommissionStatus.发放 && item.icommissionstatus !== ECommissionStatus.确认) {
      message = '只有已确认的数据才允许发放'
      blag = true
      return false
    }
  })
  if (blag) {
    modal.msgWarning(message)
    return false
  }
  formDTO.value.icommissionstatus = status
  dialogVisible.value = true
}
/**
 * 计提确认事件
 * @param row
 */
const onConfirm = (row: TOrderItemCommissionDTO | undefined) => {
  if (row) {
    multipleSelection.value = []
    multipleSelection.value.push(row)
  }
  handleAction(ECommissionStatus.确认, dialogConfirmVisible)
}
/**
 * 计提发放事件
 * @param row
 */
const onPay = (row: TOrderItemCommissionDTO | undefined) => {
  if (row) {
    multipleSelection.value = []
    multipleSelection.value.push(row)
  }
  handleAction(ECommissionStatus.发放, dialogPayVisible)
}
/**
 * 计提删除事件
 * @param row
 */
const onDelete = (row: TOrderItemCommissionDTO | undefined) => {
  if (row) {
    multipleSelection.value = []
    multipleSelection.value.push(row)
  }
  const ids = multipleSelection.value.map((item) => item.twcommissionid).join(',')
  modal.confirm('是否删除选中数据？提交后无法修改').then(() => {
    deleteCommissionByIdApi(ids).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('提交成功')
        handleQuery()
      }
    })
  })
}
/**
 * 提交
 */
const onSubmit = () => {
  multipleSelection.value.forEach((item) => {
    item.icommissionstatus = formDTO.value.icommissionstatus
    if (formDTO.value.icommissionstatus === ECommissionStatus.确认) {
      item.sconfirmremark = formDTO.value.sconfirmremark
    } else if (formDTO.value.icommissionstatus === ECommissionStatus.发放) {
      item.spayremark = formDTO.value.spayremark
    }
  })
  modal.confirm('是否提交选中数据？提交后无法修改').then(() => {
    saveCommissionApi(multipleSelection.value).then(({ success }: IAxios) => {
      if (success) {
        modal.msgSuccess('提交成功')
        dialogConfirmVisible.value = false
        dialogPayVisible.value = false
        handleQuery()
      }
    })
  })
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
 * 取消确认说明
 */
const onCancelConfirm = () => {
  formDTO.value!.sconfirmremark = ''
  formDTO.value!.icommissionstatus = ECommissionStatus.计算
  // console.log('onCancelConfirm', formDTO.value)
  dialogConfirmVisible.value = false
  formDTO.value = { ...forminfo }
}
/**
 * 取消发放说明
 */
const onCancelPay = () => {
  formDTO.value!.spayremark = ''
  formDTO.value!.icommissionstatus = ECommissionStatus.确认
  // console.log('onCancelPay', formDTO.value)
  dialogPayVisible.value = false
  formDTO.value = { ...forminfo }
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
