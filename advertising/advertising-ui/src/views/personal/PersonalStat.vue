<!--
 * @Author: yanz
 * @Date: 2024-01-23 16:33:32
 * @LastEditors: yanz
 * @LastEditTime: 2024-03-14 14:12:14
 * @Description:个人中心 - 我的数据
-->
<template>
  <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
    <el-tab-pane label="业绩统计" name="MyAchievement">
      <HgMyAchievement v-if="activeName === 'MyAchievement'"></HgMyAchievement>
    </el-tab-pane>
    <el-tab-pane label="任务统计" name="MyTaskStat">
      <HgMyTaskStat v-if="activeName === 'MyTaskStat'"></HgMyTaskStat>
    </el-tab-pane>
    <el-tab-pane label="我的客户" name="MyCustomer">
      <HgMyCustomer v-if="activeName === 'MyCustomer'"></HgMyCustomer>
    </el-tab-pane>
    <el-tab-pane label="佣金统计" name="MyCommission">
      <HgMyCommission v-if="activeName === 'MyCommission'"></HgMyCommission>
    </el-tab-pane>
    <el-tab-pane label="我的订单" name="MyOrderItem">
      <HgMyOrderItem v-if="activeName === 'MyOrderItem'"></HgMyOrderItem>
    </el-tab-pane>
    <el-tab-pane label="我的发票" name="MyInvoice">
      <HgMyInvoice v-if="activeName === 'MyInvoice'"></HgMyInvoice>
    </el-tab-pane>
    <el-tab-pane label="我的实收" name="MyReceived">
      <HgMyReceived v-if="activeName === 'MyReceived'"></HgMyReceived>
    </el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">
// import { IMyTaskDTO } from '@/types/views/business/tasks'
// import { IInvoiceDTO } from '@/types/views/finance/invoice'
// import { TOrderItemCommissionDTO } from '@/types/views/commission/commissioncalcu'
// import { TAdCustomer } from '@/types/views/customer'
// import { IOrderAndItemDTO } from '@/types/views/business/orderitemcost'
import { TabsPaneContext } from 'element-plus'
import { computTableHeight } from '@/utils'

const activeName = ref('MyAchievement')
const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}
const tableHeight = ref(0)
onMounted(() => {
  /**
   * 计算表格高度
   */
  tableHeight.value = computTableHeight()
})
</script>

<style scoped>
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>
