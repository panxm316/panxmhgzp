<!--
 * @Author: wanghl
 * @Date: 2024-01-09 14:12:03
 * @LastEditTime: 2024-02-18 16:07:46
 * @LastEditors: wanghl
 * @Description: 
-->
<template>
  <el-dialog v-model="approvalDialogVisible" title="审核" width="700" style="padding-right: 20px">
    <el-form :model="form">
      <el-form-item label="审核结果" label-width="100">
        <el-select v-model="form.status" placeholder="请选择审核结果">
          <el-option label="同意" value="2" />
          <el-option label="不同意" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="审核意见" label-width="100">
        <el-input v-model="form.comment" autocomplete="off" type="textarea" />
        <span style="color: gray; font-size: 0.8rem">提示: 审核不通过需要填写审核意见</span>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button
          type="primary"
          @click="(_: any) => {
            approvePreOrderApi(form.orderId, form.status, form.comment).then(({ success }: AxiosResponse) => {
              if (success) {
                modal.msgSuccess('操作成功')
                emit('onCloseDialog')
                approvalDialogVisible = false
              } // 报错时,request自动抛出msg,不需要再手动处理
            })
          }"
        >
          保存
        </el-button>
        <el-button @click="(_: any) => approvalDialogVisible = false"> 取消 </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { approvePreOrderApi } from '@/api/ad/adtworder'
import { AxiosResponse } from 'axios'
import modal from '@/plugins/modal'

defineOptions({
  name: 'PreOrderApprovalDialog'
})
const props = withDefaults(
  defineProps<{
    bookType: number
  }>(),
  {
    bookType: 1 // 默认 1-广告预约, 快速预约传入2
  }
)
/** 关闭弹出框时,调用父组件的方法 */
const emit = defineEmits(['onCloseDialog'])

const approvalDialogVisible = ref(false)
const initialValue = {
  orderId: '',
  comment: '',
  status: '2' // 2-通过 3-否决
}
const form = reactive({ ...initialValue })

/**
 * 初始化页面
 * @param orderId 订单id
 * @param bookType 1-广告预约 2-快速预约
 */
const init = (orderId: string) => {
  Object.assign(form, { ...initialValue, orderId })
  approvalDialogVisible.value = true
}

defineExpose({ init })
</script>

<style scoped></style>
