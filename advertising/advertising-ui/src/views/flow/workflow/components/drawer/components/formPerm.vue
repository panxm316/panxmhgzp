<script setup lang="ts">
import { computed } from 'vue'
import { useFlowStore } from '@/views/flow/workflow/stores/flow'

const props = defineProps({
  formPerm: {
    type: Object,
    default: () => {}
  },
  hideKey: {
    type: Array,
    default: () => []
  }
})
const flowStore = useFlowStore()

const { proxy } = getCurrentInstance()
const step2FormList = computed(() => {
  const step2 = flowStore.step2

  return step2
})
</script>

<template>
  <div>
    <div
      style="display: flex; flex-direction: row; background-color: var(--el-fill-color-light)"
      effect="dark"
    >
      <div class="f1">表单字段</div>
      <div class="f2">只读</div>
      <div class="f3">编辑</div>
      <div class="f4">隐藏</div>
    </div>

    <div v-if="step2FormList.length == 0">
      <el-empty description="暂无表单" />
    </div>
    <template v-for="item in step2FormList" :key="item.id">
      <div style="display: flex; flex-direction: row">
        <div class="f1">
          <span>{{ item.name }}</span>
          <span v-if="item.required" style="color: #c75450"> * </span>
        </div>

        <el-radio-group v-model="formPerm[item.id]" size="large">
          <div class="f2">
            <el-radio size="large" label="R"><span></span></el-radio>
          </div>
          <div class="f3">
            <el-radio
              :disabled="!(hideKey.length == 0 || hideKey.indexOf('E') < 0)"
              size="large"
              label="E"
              ><span></span
            ></el-radio>
          </div>
          <div class="f4">
            <el-radio size="large" label="H"><span></span></el-radio>
          </div>
        </el-radio-group>
      </div>
      <template v-if="item.type === 'Layout'">
        <template v-for="item1 in item.props.value" :key="item1.id">
          <div style="display: flex; flex-direction: row">
            <div class="f1">
              <span> {{ '\u00A0\u00A0\u00A0\u00A0-' + item1.name }}</span>
              <span v-if="item1.required" style="color: #c75450"> * </span>
            </div>

            <el-radio-group v-model="formPerm[item1.id]" size="large">
              <div class="f2">
                <el-radio size="large" label="R"><span></span></el-radio>
              </div>
              <div class="f3">
                <el-radio
                  :disabled="!(hideKey.length == 0 || hideKey.indexOf('E') < 0)"
                  size="large"
                  label="E"
                  ><span></span
                ></el-radio>
              </div>
              <div class="f4">
                <el-radio size="large" label="H"><span></span></el-radio>
              </div>
            </el-radio-group>
          </div>
        </template>
      </template>
    </template>
  </div>
</template>

<style scoped lang="scss">
$width2: 80px;
$width3: 80px;
$width4: 80px;

.f1 {
  width: calc(100% - $width2 - $width3 - $width4);
  padding: 10px;
}

.f2 {
  width: $width2;
  padding: 10px;
}

.f3 {
  width: $width3;
  padding: 10px;
}

.f4 {
  width: $width4;
  padding: 10px;
}
</style>
