<!--
 * @Author: suny
 * @Date: 2024-03-16 16:19:49
 * @LastEditTime: 2024-03-18 17:55:45
 * @LastEditors: suny
 * @Description: 佣金计算tabs
-->
<template>
  <div>
    <el-tabs v-model="activeName" class="demo-tabs">
      <el-tab-pane name="CommissionCalcu" label="佣金计算">
        <CommissionCalcu></CommissionCalcu>
      </el-tab-pane>
      <el-tab-pane name="CommissionAdd" label="佣金查询">
        <CommissionAdd></CommissionAdd>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script setup lang="ts">
import CommissionAdd from './CommissionAdd.vue'
import CommissionCalcu from './CommissionCalcu.vue'
const activeName = ref('CommissionCalcu')
</script>
<style lang="scss" scoped></style>
