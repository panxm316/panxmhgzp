<!--
 * @Author: suny
 * @Date: 2024-01-04 15:23:55
 * @LastEditTime: 2024-01-05 09:34:39
 * @LastEditors: suny
 * @Description: 数据台账
-->
<template>
  <div>
    <el-tabs v-model="activeName" class="demo-tabs">
      <el-tab-pane name="OrderLedger" label="订单台账">
        <OrderLedger></OrderLedger>
      </el-tab-pane>
      <el-tab-pane name="InvoiceLedger" label="发票台账">
        <InvoiceLedger></InvoiceLedger>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script setup lang="ts">
import OrderLedger from './OrderLedger.vue'
import InvoiceLedger from './InvoiceLedger.vue'
const activeName = ref('OrderLedger')
</script>
<style lang="scss" scoped></style>
