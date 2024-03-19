<!--
 * @Author: lhl
 * @Date: 2024-03-06
 * @Description: 查看项目所有合同
-->
<template>
  <el-table
    :data="dataList"
    highlight-current-row
    :height="tableheight"
    :header-cell-style="tableHeaderColor"
    border
  >
    <el-table-column type="index" label="序号" :index="table_index" width="60" align="center">
    </el-table-column>
    <el-table-column label="合同号" prop="scontractnum" min-width="160" align="center" />
    <el-table-column label="订单编号" prop="sordernum" min-width="160" align="center" />
    <el-table-column label="客户名称" prop="customername" min-width="160" show-overflow-tooltip />
    <el-table-column label="代理公司名称" prop="agencyname" min-width="160" show-overflow-tooltip />
    <el-table-column label="内容生产方" prop="agentname" min-width="160" show-overflow-tooltip />
    <el-table-column label="行业名称" prop="adindustryname" min-width="160" show-overflow-tooltip />
    <el-table-column label="主业务员名称" prop="employname" min-width="160" show-overflow-tooltip />
    <el-table-column label="应收金额" prop="amountreceivable" min-width="120" align="center">
      <template #default="scope">
        <span>{{ formatMoney(scope.row.amountreceivable, '2') }}</span>
      </template>
    </el-table-column>
    <el-table-column label="已收金额" prop="amountreceived" min-width="120" align="center">
      <template #default="scope">
        <span>{{ formatMoney(scope.row.amountreceived, '2') }}</span>
      </template>
    </el-table-column>
    <el-table-column label="欠款金额" prop="amountarrearage" min-width="120" align="center">
      <template #default="scope">
        <span>{{ formatMoney(scope.row.amountarrearage, '2') }}</span>
      </template>
    </el-table-column>
    <el-table-column
      label="操作"
      align="left"
      width="120"
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
          title="订单明细"
          @click="showContract(scope.row)"
          >订单明细</el-button
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
  <!--
      订单明细窗口
  -->
  <el-dialog
    v-model="dialogOrderItem"
    title="订单明细"
    :width="1200"
    append-to-body
    :destroy-on-close="true"
  >
    <el-table
      :data="orderItemList"
      highlight-current-row
      :header-cell-style="tableHeaderColor"
      border
    >
      <el-table-column label="广告标题" prop="sadtitle" min-width="160" align="center" />
      <el-table-column label="广告类型" prop="addisplayformname" min-width="160" align="center" />
      <el-table-column label="叠次" prop="foldname" min-width="160" align="center" />
      <el-table-column label="叠次版本" prop="foldareavername" min-width="160" align="center" />
      <el-table-column label="见报日期" prop="prestartdate" min-width="160" align="center">
        <template #default="scope">
          <span>{{ dayjs(scope.row.prestartdate).format('YYYY-MM-DD') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="刊发状态"
        prop="ipublishstatus"
        show-overflow-tooltip
        min-width="100"
        align="center"
      >
        <template #default="scope">
          <span v-if="scope.row.ipublishstatus === 0">预约</span>
          <span v-if="scope.row.ipublishstatus === 1">预定</span>
          <span v-if="scope.row.ipublishstatus === 2">待发布</span>
          <span v-if="scope.row.ipublishstatus === 3">安排</span>
          <span v-if="scope.row.ipublishstatus === 4">见报</span>
          <span v-if="scope.row.ipublishstatus === 5">已发布</span>
          <span v-if="scope.row.ipublishstatus === 6">上架</span>
          <span v-if="scope.row.ipublishstatus === 7">下架</span>
        </template>
      </el-table-column>
      <el-table-column
        label="核查状态"
        prop="ipublishcheckstatus"
        show-overflow-tooltip
        min-width="100"
        align="center"
      >
        <template #default="scope">
          <span v-if="scope.row.ipublishcheckstatus === 0">正常</span>
          <span v-if="scope.row.ipublishcheckstatus === 1">未刊发</span>
          <span v-if="scope.row.ipublishcheckstatus === 2">刊发错误</span>
          <span v-if="scope.row.ipublishcheckstatus === 3">已解决</span>
        </template>
      </el-table-column>
      <el-table-column label="应收金额" prop="amountreceivable" min-width="120" align="right">
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
    </el-table>
    <el-pagination
      v-model:current-page="queryOrderItemvo.pageNum"
      class="pagination"
      style="margin-left: 10px; width: 100%"
      :page-sizes="pageSizes"
      :page-size="queryOrderItemvo.pageSize"
      small
      background
      layout="total, sizes, prev, pager, next, jumper"
      :total="ordertotal"
      @size-change="handleOrderSizeChange"
      @current-change="handleOrderCurrentChange"
    />
    <div style="text-align: right; margin-top: 60px; width: 100%">
      <el-button icon="Close" @click="dialogOrderItem = false">关闭</el-button>
    </div>
  </el-dialog>
</template>
<script setup lang="ts">
import { computTableHeight, required, tableHeaderColor, formatMoney } from '@/utils'
import { getAdProjectContractApi, getAdOrderItemApi } from '@/api/ad/adprojectcount'
import { dayjs } from 'element-plus'
defineOptions({
  name: 'ProjectContract'
})
const props = defineProps<{ projectid: string; projectType: number }>()
watch(
  () => props.projectid,
  () => {
    initproject()
  },
  { deep: true }
)
onMounted(() => {
  getProjectContractList()
  /**
   * 计算表格高度
   */
  tableheight.value = computTableHeight()
})
/** 查询条件 */
/** 请求对象 */
const queryvo = ref({
  adProjectId: '',
  projectType: 0,
  pageNum: 0,
  pageSize: 20
})
/** 订单明细请求对象 */
const queryOrderItemvo = ref({
  adOrderId: '',
  pageNum: 0,
  pageSize: 20
})
/** 明细订单ID */
const orderId = ref('')
/** 查询记录总条数 */
const total = ref(0)
/** 订单明细查询记录总条数 */
const ordertotal = ref(0)
/** 每页选择下拉 */
const pageSizes = ref<number[]>([10, 20, 30, 40])
/** 列表 */
const dataList = ref<any>([])
/** 订单明细列表 */
const orderItemList = ref<any>([])
/** 表格高度 */
const tableheight = ref(0)
/** 明细订单窗口 */
const dialogOrderItem = ref(false)
const table_index = (val: number) => {
  const pagenum =
    queryvo.value.pageSize! * (queryvo.value.pageNum === 0 ? 0 : queryvo.value.pageNum! - 1) + 1
  return val + pagenum
}
/**
 * 获取项目相关合同
 */
const getProjectContractList = () => {
  queryvo.value.adProjectId = props.projectid
  queryvo.value.projectType = props.projectType
  getAdProjectContractApi(queryvo.value).then((res) => {
    if (res.success) {
      dataList.value = res.obj
      total.value = res.total
    }
  })
}
/**
 * 获取订单明细
 */
const getOrderItemList = () => {
  queryOrderItemvo.value.adOrderId = orderId.value
  getAdOrderItemApi(queryOrderItemvo.value).then((res) => {
    if (res.success) {
      orderItemList.value = res.obj
      ordertotal.value = res.total
    }
  })
}
/**
 * 改变页码总数调用
 */
const handleSizeChange = (val: number) => {
  queryvo.value.pageSize = val
  getProjectContractList()
}
/**
 * 改变页数调用
 * @param val
 */
const handleCurrentChange = (val: number) => {
  queryvo.value.pageNum = val
  getProjectContractList()
}
/**
 * 改变页码总数调用
 */
const handleOrderSizeChange = (val: number) => {
  queryOrderItemvo.value.pageSize = val
  getOrderItemList()
}
/**
 * 改变页数调用
 * @param val
 */
const handleOrderCurrentChange = (val: number) => {
  queryOrderItemvo.value.pageNum = val
  getOrderItemList()
}
/**
 * 初始化项目
 */
const initproject = () => {
  getProjectContractList()
}
/**
 * 查看订单明细
 */
const showContract = (row: any) => {
  orderId.value = row.id
  getOrderItemList()
  dialogOrderItem.value = true
}
</script>
