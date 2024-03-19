<!--
 * @Author: suny
 * @Date: 2023-12-23 15:24:34
 * @LastEditTime: 2023-12-27 14:36:45
 * @LastEditors: suny
 * @Description: 发票核销：包括预开发票核销，预收款核销，核销回退三个功能
-->
<template>
  <div>
    <el-tabs v-model="activeName" class="demo-tabs">
      <el-tab-pane name="InvoiceCharge" label="预开发票核销">
        <InvoiceCharge></InvoiceCharge>
      </el-tab-pane>
      <el-tab-pane name="AdvanceCharge" label="预收款核销">
        <AdvanceCharge></AdvanceCharge>
      </el-tab-pane>
      <el-tab-pane name="InvoiceChargeBack" label="核销回退">
        <InvoiceChargeBack></InvoiceChargeBack>
      </el-tab-pane>
      <el-tab-pane name="ChargeBackHistory" label="回退历史">
        <ChargeBackHistory></ChargeBackHistory>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script setup lang="ts">
import InvoiceCharge from './InvoiceCharge.vue'
import InvoiceChargeBack from './InvoiceChargeBack.vue'
import AdvanceCharge from './AdvanceCharge.vue'
import ChargeBackHistory from './ChargeBackHistory.vue'
const activeName = ref('InvoiceCharge')
</script>
<style lang="scss" scoped></style>
