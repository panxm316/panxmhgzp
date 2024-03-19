<!--
 * @Author: wanghl
 * @Date: 2023-10-26 10:11:02
 * @LastEditTime: 2024-03-16 13:42:09
 * @LastEditors: wanghl
 * @Description:改，加。停刊流程表
-->
<template>
  <el-dialog
    v-model="dialogVisible"
    title="订单修改"
    :width="fenbianlv() > 1440 ? '60%' : '94%'"
    :z-index="9999"
    @close="dialogVisible = false"
  >
    <div id="printMe" class="app-container" style="padding: 0px 40px 0px">
      <table border="0" width="100%" style="margin: 0px auto 0 !important">
        <thead>
          <tr>
            <th colspan="8">广东省南方广告有限公司</th>
          </tr>
          <tr>
            <th colspan="8">广告系统数据调整申请表（试行）</th>
          </tr>
        </thead>
        <tr style="height: 60px">
          <td style="width: 100px">客户名称</td>
          <td style="width: 400px; text-align: left !important">
            <el-input placeholder="请输入合同号" title="合同号" style="width: 360px"></el-input>
          </td>
          <td style="width: 100px">合同码</td>
          <td style="width: 400px; text-align: left !important">
            <el-input placeholder="请输入合同号" title="合同号" style="width: 200px"></el-input>
          </td>
        </tr>
        <tr style="height: 60px">
          <td style="width: 100px">经办部门</td>
          <td style="width: 500px"></td>
          <td style="width: 100px">经办人</td>
          <td style="width: 300px">1</td>
        </tr>
        <tr style="height: 140px">
          <td colspan="1">调整类型</td>
          <td colspan="7" style="text-align: left !important">
            <el-checkbox-group v-model="checkList" style="margin: 0; padding: 0">
              <el-checkbox label="Option A" value="Value A" />
              <el-checkbox label="Option B" value="Value B" />
              <el-checkbox label="Option C" value="Value C" />
              <el-checkbox label="disabled" value="Value disabled" disabled />
              <el-checkbox
                label="selected and disabled"
                value="Value selected and disabled"
                disabled
              />
            </el-checkbox-group>
          </td>
        </tr>
        <tr style="height: 140px">
          <td colspan="1">调整要求</td>
          <td colspan="7"></td>
        </tr>
        <tr style="height: 180px">
          <td>调整意见</td>
          <td colspan="7">
            <div class="right">
              <div class="top">
                <span class="leader">领导签字:{{}}</span>
                <span class="official_seal">(公章)</span>
              </div>
              <div class="bottom">
                <span class="year">{{}}年</span>
                <span class="month">{{}}2月</span>
                <span class="day">{{}}日</span>
              </div>
            </div>
          </td>
        </tr>
        <tr style="height: 140px">
          <td colspan="1">调整要求</td>
          <td colspan="7"></td>
        </tr>
        <tr style="height: 140px">
          <td colspan="1">调整要求</td>
          <td colspan="7"></td>
        </tr>
        <tr style="height: 140px">
          <td colspan="1">调整要求</td>
          <td colspan="7"></td>
        </tr>
      </table>
      <div style="width: 100%; text-align: right; margin-top: 20px">
        <el-button type="danger" icon="close" @click="dialogVisible = false">关闭</el-button>
        <el-button type="primary" icon="Check" @click="handleSave()">保存</el-button>
        <el-button icon="Top" type="success" @click="submit()">保存并提交审核</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup lang="ts">
import {
  TAddFlowOrder,
  TDEditFlowOrder,
  TDeleteFlowOrder,
  TQFlowOrderdata,
  TQFlowOrderlist,
  TQFlowOrderlistpage
} from '@/types/views/flow/floworder'
import {
  saveapplicationRelationsApi,
  deleteapplicationRelationsApi,
  updateapplicationRelationsApi,
  getapplicationRelationsApi,
  getapplicationRelationslistApi,
  getapplicationRelationsPageListApi
} from '@/api/flow/floworder'
import type { IAxios } from 'axios'
import useUserStore from '@/store/modules/user'
import {
  required,
  computTableHeight,
  isNumberStr,
  formatDate,
  tableHeaderColor,
  validatePhone,
  computTreeHeight,
  fenbianlv
} from '@/utils/index'
const global = getCurrentInstance()?.appContext.app.config.globalProperties
const userStore = useUserStore()
//  loginUserId: userStore.user?.userid.toString(), // 登录id
defineOptions({
  name: 'HgFlowChangeOrder'
})
type THSelectCustomer = {
  /**
   * 传入用于遍历的客户id
   */
  selfdetailid?: string
}
const props = withDefaults(defineProps<THSelectCustomer>(), {
  selfdetailid: ''
})
/** 保存提交审核 */
const emit = defineEmits<{
  closedialogVisiblesubmit: [data: '']
}>()
/**
 * 审核中修改显示隐藏
 */
const dialogVisible = ref(false)
/** 订单流程信息 */
const FlowOrderdata = ref<TAddFlowOrder>({
  flowtypename: '',
  approvetypename: '',
  id: '',
  createempid: '',
  createempname: '',
  flowtype: '',
  approvetype: '',
  dcreatetime: '',
  mainid: '',
  childid: '',
  iapprovestatus: 0,
  applicationid: '',
  sapprovalopinions: '',
  dlastmodifydate: '',
  newcustomerid: '',
  newbusinessentityid: '0',
  bchangebelong: true,
  bchangeitem: '',
  bstopitem: '',
  schangecontent: '',
  schangereason: ''
})
import { defineExpose } from 'vue'
const checkList = ref(['Value selected and disabled', 'Value A'])
/** 点击弹出，并传入审批类型add,change,stop */
type Typechangeorderlistdata = {
  orderid?: string // 订单id
  orderitemid?: string // 订单明细id
  changetype?: string // 并传入审批类型add,change,stop
}
const view = (row: Typechangeorderlistdata, data?: string) => {
  console.log(row)
  dialogVisible.value = true
}
defineExpose({ view })
onMounted(() => {})
/**
 * 获取客户列表
 */
const loaddata = () => {
  getapplicationRelationsApi(FlowOrderdata.value).then(
    ({ obj, total }: IAxios<TQFlowOrderdata>) => {
      FlowOrderdata.value = obj
      console.log(obj)
    }
  )
}
/**
 * 保存
 */
const handleSave = () => {
  updateapplicationRelationsApi(FlowOrderdata.value).then(
    ({ obj, total }: IAxios<TDEditFlowOrder>) => {
      global!.$message.success('保存成功')
      dialogVisible.value = false
    }
  )
}
/**
 * 提交
 */
const submit = () => {
  // updateapplicationRelationsApi(FlowOrderdata.value).then(
  //   ({ obj, total }: IAxios<TDEditFlowOrder>) => {
  //     global!.$message.success('提交成功')
  //     dialogVisible.value = false
  //   }
  // )
  dialogVisible.value = false
  emit('closedialogVisiblesubmit', '')
}
</script>

<style scoped>
ul {
  padding-left: 10px;
}
.showSearch span {
  color: #606266;
  font-size: 14px;
}
:deep(.el-form-item__label) {
  font-weight: bold !important;
}
.el-form-item {
  margin-bottom: 15px;
}
#printMe table {
  border-collapse: collapse;
}
#printMe table thead th {
  font-size: 20px;
  padding: 10px;
}
#printMe table tr {
  height: 30px;
  font-size: 14px;
}
#printMe table td {
  border: 1px solid black;
  text-align: center !important;
}
#printMe table td span {
  margin-right: 20px;
}
</style>
