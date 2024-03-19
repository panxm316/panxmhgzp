<!--
 * @Author: songly
 * @Date: 2023-10-17 10:53:32
 * @LastEditTime: 2023-10-18 11:23:52
 * @LastEditors: songly
 * @Description:查看流程图
-->
<template>
  <div>
    <el-dialog v-model="dialogVisible" title="查看流程图" width="80%">
      <img :src="imgBase64" style="width: 100%" />
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { showImage } from '@/api/task/index'
import { defineExpose } from 'vue'
import { ITaskDto } from '@/types/views/task/completed'

const dialogVisible = ref(false)

const view = (row: ITaskDto) => {
  showImage(row.processInstanceId).then((res) => {
    imgBase64.value = 'data:image/png;base64,' + res.obj
    dialogVisible.value = true
  })
}
defineExpose({ view })
const imgBase64 = ref()
</script>
<style scoped lang="less"></style>
