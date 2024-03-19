<!--
 * @Author: wanghl
 * @Date: 2023-08-10 14:57:39
 * @LastEditTime: 2023-08-21 15:17:02
 * @LastEditors: wanghl
 * @Description:
-->
<template>
  <div>
    <el-dropdown trigger="click" @command="handleSetSize">
      <div class="size-icon--style">
        <svg-icon class-name="size-icon" icon-class="size" />
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item
            v-for="item of sizeOptions"
            :key="item.value"
            :disabled="size === item.value"
            :command="item.value"
          >
            {{ item.label }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, getCurrentInstance } from 'vue'
import useAppStore from '@/store/modules/app'
import { TSize } from '@/store/modules/type'

const appStore = useAppStore()
const size = computed(() => appStore.size)
const { proxy } = getCurrentInstance() as any
const sizeOptions = ref([
  { label: '大', value: 'large' },
  { label: '默认', value: 'default' },
  { label: '小', value: 'small' }
])

function handleSetSize(size: TSize) {
  proxy.$modal.loading('正在切换请稍后...')
  appStore.setSize(size)
  setTimeout('window.location.reload()', 1000)
}
</script>

<style lang="scss" scoped>
.size-icon--style {
  font-size: 18px;
  line-height: 50px;
  padding-right: 7px;
  color: #fff;
}
</style>
