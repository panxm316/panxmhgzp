<!--
 * @Author: suny
 * @Date: 2024-01-11 08:39:54
 * @LastEditTime: 2024-03-06 10:41:22
 * @LastEditors: wanghl
 * @Description: 业绩归属
-->
<template>
  <div class="app-container">
    <div class="search_box">
      <div class="flex">
        <HgDateIndex :initdate="timedata" @onresponse="ontime"></HgDateIndex>
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
        <el-select
          v-model="queryParams.ipublishstatus"
          placeholder="请选择刊发状态"
          clearable
          style="width: 160px"
          @change="handleQuery"
          @clear="handleQuery"
        >
          <el-option
            v-for="item in publishStatusCombo"
            :key="item.id"
            :label="item.name"
            :value="item.id"
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
        <el-table-column
          label="合同号"
          prop="scontractnum"
          min-width="140"
          show-overflow-tooltip
          align="center"
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
        <el-table-column label="主业务员" prop="employname" width="120" show-overflow-tooltip />
        <el-table-column label="代理公司" prop="agencyname" width="140" show-overflow-tooltip />
        <el-table-column label="内容生产方" prop="agentname" width="140" show-overflow-tooltip />
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
        <el-table-column label="" prop="foldname" min-width="100" show-overflow-tooltip />
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
        <el-table-column label="成本金额" prop="costamount" min-width="120" align="right">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.costamount, '2') }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="80"
          align="center"
          class-name="small-padding fixed-width"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              v-if="scope.row.bEditFlag"
              link
              type="success"
              icon="Edit"
              size="small"
              title="编辑"
              @click="onEdit(scope.row)"
              >编辑</el-button
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
      title="业绩分配"
      align-center
      draggable
      width="1000"
      :close-on-click-modal="false"
      :destroy-on-close="true"
    >
      <div style="width: 900px; margin: 0 auto">
        <el-form ref="EditFormRef" :model="editForm" :inline="true" label-width="80px" status-icon>
          <el-form-item label="业绩金额" required>
            <el-input-number
              v-model="editForm.amountachievement"
              :precision="2"
              :step="0.1"
              :min="0"
              :controls="false"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="业绩日期" required>
            <el-date-picker
              v-model="editForm.dachievementdate"
              type="date"
              placeholder="选择日期"
              style="width: 100%"
              size="default"
            />
          </el-form-item>
          <div style="padding: 0 20px; margin: 20px 0">
            <el-divider content-position="center">
              <span style="color: #409eff">归属详情</span>
            </el-divider>
          </div>
          <div style="width: 900px; position: relative">
            <el-form ref="belongFormRef" :model="belongEditForm" :inline="true" label-width="80px">
              <el-form-item label="业务员" prop="employid">
                <span style="width: 200px">{{ belongEditForm.employname }}</span>
              </el-form-item>
              <el-form-item prop="archievementrate" label="业绩比例">
                <div style="width: 100%">
                  <el-input-number
                    v-model="belongEditForm.archievementrate"
                    :disabled="!belongEditForm.id"
                    :precision="2"
                    :step="0.1"
                    :min="0"
                    :max="100"
                    :controls="false"
                    style="width: 170px"
                  />
                  <span style="margin-left: 10px; width: 10px">%</span>
                </div>
              </el-form-item>
              <div style="position: absolute; right: 100px; z-index: 11111; top: 0px">
                <el-button
                  :disabled="!belongEditForm.id"
                  type="primary"
                  icon="Check"
                  @click="handleSave"
                  >确定</el-button
                >
                <el-button icon="Close" @click="handleCancel">取消</el-button>
              </div>
            </el-form>
          </div>

          <div style="width: 900px">
            <el-table
              ref="fileTableRef"
              :data="editForm.orderitembelongList"
              highlight-current-row
              style="width: 900px"
              :header-cell-style="tableHeaderColor"
              :border="true"
            >
              <el-table-column prop="employname" label="业务员"> </el-table-column>
              <el-table-column prop="employtype" label="类型">
                <template #default="scope">
                  {{ customerTypeCombo.find((item) => item.id === scope.row.employtype)?.name }}
                </template>
              </el-table-column>
              <el-table-column prop="archievementrate" label="业绩比例" sortable="custom">
                <template #default="scope">
                  <span>{{ parseFloat(scope.row.archievementrate).toFixed(2) }}%</span>
                </template>
              </el-table-column>
              <el-table-column prop="bprimary" label="是否主业务员" align="center">
                <template #default="scope">
                  <el-checkbox v-model="scope.row.bprimary" disabled></el-checkbox>
                  <!-- <span>{{ scope.row.bprimary }}%</span> -->
                </template>
              </el-table-column>
              <el-table-column prop="deptname" label="所属部门" show-overflow-tooltip>
              </el-table-column>
              <el-table-column
                label="操作"
                :align="'left'"
                width="120"
                class-name="small-padding fixed-width"
                fixed="right"
              >
                <template #default="scope">
                  <el-button
                    link
                    type="success"
                    icon="Edit"
                    size="small"
                    @click="onBelongEdit(scope.$index, scope.row)"
                    >修改</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div style="text-align: right; width: 900px; margin-top: 20px">
            <el-button type="primary" icon="Check" @click="saveForm">保存</el-button>
            <el-button icon="Close" @click="dialogEditVisible = false">取消</el-button>
          </div>
        </el-form>
      </div>
    </el-dialog>
    <!-- 根据合同号查看详情 -->
    <AdTworderdetails ref="viewImageRef"></AdTworderdetails>
  </div>
</template>
<script setup lang="ts">
import useUserStore from '@/store/modules/user'
import { TDateTimeType, TDateType } from '@/types/components/hgdateindex'
import { IAxios } from 'axios'
import { EPublishstatus, ECustomerType } from '@/types/common/enumindex'
import { computTableHeight, tableHeaderColor, formatMoney } from '@/utils'
import { dayjs } from 'element-plus'
import { TOrderAndItemVO } from '@/types/views/business/orderitemcost'
import { IOrderAndItemDTO } from '@/types/views/business/orderitemcost'
import { IDataCombo } from '@/types/common/DataCombo'
import { getMediaDataComboApi } from '@/api/media/media'
import { getEnumCombo } from '@/api/common/index'
import {
  getPerformanceRatioApi,
  getOrderBelongListByOrderitemIdApi,
  saveCommisstionDataApi
} from '@/api/commission/achievebelong'
import { Tworderitembelong } from '@/types/views/ad/adtworder'

/** 刊发状态列表 */
const publishStatusCombo: IDataCombo[] = getEnumCombo(EPublishstatus)
/** 客户类型列表 */
const customerTypeCombo: IDataCombo[] = getEnumCombo(ECustomerType)
const user = useUserStore().user
const total = ref(0)
/** 查看详情窗口显示状态 */
const dialogDetailVisible = ref(false)
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
/** 查询条件 */
const queryParams = reactive<TOrderAndItemVO>({
  sort: 'createtime',
  order: 'desc',
  page: 1,
  pageSize: 20,
  startTime: '',
  endTime: '',
  customername: ''
})
/** 列表对象 */
const orderItemList = ref<IOrderAndItemDTO[]>([])
/** Form选中的行 */
const multipleSelection = ref<IOrderAndItemDTO[]>([])
/** 详情对象 */
const editForm = ref<IOrderAndItemDTO>({})
/** 媒体列表 */
const mediaCombo = ref<IDataCombo[]>([])
/** 业绩归属 */
const belongEditForm = ref<Tworderitembelong>({})
/** 归属编辑index */
const handleUpdateRowindex = ref(0)

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
  getPerformanceRatioApi(queryParams)
    .then((res: IAxios<IOrderAndItemDTO[]>) => {
      if (res.success) {
        orderItemList.value = res.obj
        total.value = res.total
      }
    })
    .finally(() => {})
}
/**
 * 编辑按钮事件
 * @param row
 */
const onEdit = (row: IOrderAndItemDTO) => {
  editForm.value = { ...row }
  const data = {
    orderitemId: row.orderitemid!
  }
  getOrderBelongListByOrderitemIdApi(data)
    .then((res: IAxios<Tworderitembelong[]>) => {
      if (res.success) {
        dialogEditVisible.value = true
        editForm.value.orderitembelongList = res.obj
      }
    })
    .finally(() => {})
}
/**
 * 修改归属比例
 * @param row
 */
const onBelongEdit = (index: number, row?: Tworderitembelong) => {
  handleUpdateRowindex.value = index
  belongEditForm.value = { ...row }
}
/**
 * 保存业绩分配
 */
const saveForm = () => {
  if (editForm.value.amountachievement! <= 0) {
    ElMessage.error('业绩金额必须大于0')
    return
  }
  if (editForm.value.dachievementdate === '' || editForm.value.dachievementdate === null) {
    ElMessage.error('业绩日期不能为空')
    return
  }
  saveCommisstionDataApi(editForm.value)
    .then((res: IAxios) => {
      if (res.success) {
        dialogEditVisible.value = false
        handleQuery()
      }
    })
    .catch(() => {})
}
/**
 * 保存业务员的业绩比例
 */
const handleSave = () => {
  editForm.value.orderitembelongList![handleUpdateRowindex.value].archievementrate =
    belongEditForm.value.archievementrate
  belongEditForm.value = {}
}
/**
 * 取消业务员的业绩比例
 */
const handleCancel = () => {
  belongEditForm.value = {}
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
const handleSelectionChange = (val: IOrderAndItemDTO[]) => {
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
