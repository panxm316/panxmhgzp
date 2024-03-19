<template>
  <el-tooltip class="item" effect="dark" content="帮助" placement="top">
    <el-button class="help" type="text" @click="showHelp(`${props.path}`)">?</el-button>
  </el-tooltip>
</template>

<script setup lang="ts">
import { showHelp } from '@/utils/help'
import { defineProps } from 'vue/dist/vue'

const props = defineProps({
  path: {
    required: true,
    type: String
  }
})
</script>

<style scoped lang="scss">
.item {
  margin-bottom: 0;
}
.help {
  position: absolute;
  right: 10px !important;
  width: 32px;
}
</style>
