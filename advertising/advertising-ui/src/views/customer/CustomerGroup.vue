<!--
 * @Author: wanghl
 * @Date: 2023-11-24 14:43:40
 * @LastEditTime: 2023-11-24 17:14:38
 * @LastEditors: wanghl
 * @Description:客户属性设置
-->
<template>
  <el-tabs v-model="activeName" class="demo-tabs">
    <el-tab-pane name="customergatepory" label="客户分类">
      <CustomerCategory></CustomerCategory>
    </el-tab-pane>
    <el-tab-pane name="customerstatus" label="客户状态">
      <CustomerStatus></CustomerStatus>
    </el-tab-pane>
    <el-tab-pane name="customercredit" label="客户信誉度">
      <customercredit></customercredit>
    </el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">
import CustomerCategory from './CustomerCategory.vue'
import CustomerStatus from './CustomerStatus.vue'
import customercredit from './CustomerCredit.vue'
const activeName = ref('customergatepory')
</script>

<style scoped></style>
