<!--
 * @Author: wanghl
 * @Date: 2023-10-26 10:11:02
 * @LastEditTime: 2024-03-07 16:49:59
 * @LastEditors: wanghl
 * @Description:查看客户详细信息
-->
<template>
  <div class="app-container" style="padding: 20px 0 0 60px; background-color: rgb(249, 252, 254)">
    <el-row :gutter="40">
      <el-col :span="11">
        <el-form-item label="名称" prop="sname">
          {{ CustomerData.sname }}
        </el-form-item>
        <el-form-item label="地址" prop="saddress">
          {{ CustomerData.saddress }}
        </el-form-item>
        <el-form-item label="办公电话" prop="sphone">
          {{ CustomerData.sphone }}
        </el-form-item>
        <el-form-item label="联系人" prop="scontacts">
          {{ CustomerData.scontacts }}
        </el-form-item>
        <el-form-item label="移动电话" prop="smobilephone">
          {{ CustomerData.smobilephone }}
        </el-form-item>

        <el-form-item label="统一社会信用代码" prop="screditcode">
          {{ CustomerData.screditcode }}
        </el-form-item>
        <el-form-item label="邮政编码" prop="spostcode">
          {{ CustomerData.spostcode }}
        </el-form-item>

        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item label="是否有效" prop="buse">
              <el-checkbox v-model="CustomerData.buse" disabled></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人" prop="bindividual">
              <el-checkbox v-model="CustomerData.bindividual" disabled></el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="大客户" prop="bvip">
              <el-checkbox v-model="CustomerData.bvip" disabled></el-checkbox>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="上级客户" prop="parentName">
          {{ CustomerData.parentName }}
        </el-form-item>
        <!-- <el-form-item label="上级客户" prop="parentName">
          {{ CustomerData.sapprovalopinions }}
        </el-form-item> -->
      </el-col>
      <el-col :span="11">
        <el-form-item label="简码（拼音）" prop="sbrevitycode">
          {{ CustomerData.sbrevitycode }}
        </el-form-item>
        <el-form-item label="编号" prop="icode">
          {{ CustomerData.icode }}
        </el-form-item>

        <el-form-item label="开户行及账号" prop="sbankaccount">
          {{ CustomerData.sbankaccount }}
        </el-form-item>
        <el-form-item prop="sremark" label="备注">
          {{ CustomerData.sremark }}
        </el-form-item>
        <!-- <el-form-item label="序号" prop="isort">
          {{ CustomerData.isort }}
        </el-form-item> -->
        <el-form-item label="客户来源" prop="adfromname">
          {{ CustomerData.adfromname }}
        </el-form-item>
        <el-form-item label="业务员" prop="employid"> {{ CustomerData.employname }} </el-form-item>

        <el-form-item label="行业名称" prop="adindustryid">
          {{ CustomerData.adindustryname }}
        </el-form-item>
        <el-form-item label="客户类型" prop="itype">
          <span v-if="CustomerData.itype == 0">直接客户</span>
          <span v-if="CustomerData.itype == 1">代理公司</span>
          <span v-if="CustomerData.itype == 2">内容生产方</span>
        </el-form-item>
        <el-form-item label="客户状态" prop="sstatus">
          <span v-if="CustomerData.sstatus == '活跃'">活跃</span>
          <span v-if="CustomerData.sstatus == '不活跃'">不活跃</span>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="22">
        <el-form-item label="客户归属" prop="customerbelong">
          <el-table :data="CustomerData.customerbelong" row-key="id" :border="true" stripe>
            <!-- <el-table-column type="selection" prop="ischeck" :width="50" align="center">
          </el-table-column> -->
            <el-table-column prop="customerName" label="名称" sortable="custom" min-width="150">
            </el-table-column>
            <el-table-column prop="bprimary" label="业务员" min-width="160">
              <template #default="scope">
                <span v-if="scope.row.bprimary === false"> 普通业务员 </span>
                <span v-if="scope.row.bprimary === true"> 主业务员 </span>
              </template>
            </el-table-column>
            <el-table-column
              prop="deptname"
              label="所属部门"
              sortable="custom"
              min-width="140"
              show-overflow-tooltip
            >
            </el-table-column>
            <el-table-column prop="dcreatetime" label="添加时间" min-width="160">
              <template #default="scope">
                <span>{{ formatDate(scope.row.dcreatetime) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>

        <el-form-item label="客户资料" prop="sstatus">
          <div v-for="file in CustomerData.customerfiles" :key="file.sfileid">
            <el-link
              v-if="file.sfiletype === 'Office'"
              type="primary"
              :underline="false"
              @click="previewFile(file)"
            >
              <el-icon><Document /></el-icon> {{ file.soriginalfile }}</el-link
            >
            <img
              v-if="file.sfiletype === 'Photo'"
              style="width: 50%"
              :src="file.durl"
              @click="previewFile(file)"
            />
          </div>
        </el-form-item>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import type { TAdCustomer, TQueryCustomer, Twcustomerfiles } from '@/types/views/customer'
import { getCustomerByIdApi } from '@/api/customer/index'
import type { IAxios } from 'axios'
import { formatDate } from '@/utils/index'
import useUserStore from '@/store/modules/user'

const global = getCurrentInstance()?.appContext.app.config.globalProperties
const userStore = useUserStore()
//  loginUserId: userStore.user?.userid.toString(), // 登录id
defineOptions({
  name: 'Customer'
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
/** 客户列表 */
const CustomerData = ref<TAdCustomer>({})
/** 客户id */
const customerid = ref(props.selfdetailid)
/** 客户信息 */
const CustomerDatal = ref<TAdCustomer>({
  id: '',
  sname: '',
  /**  创建日期  */
  dcreatetime: '',
  /** 简码（拼音）    */
  sbrevitycode: '',
  /** 编号（默认是自增列） */
  icode: 1,
  /** 客户类型（0直接客户、1代理公司、3内容生产方）    */
  itype: 0,
  /** 地址    */
  saddress: '',
  /** 电话     */
  sphone: '',
  /** 统一社会信用代码/纳税人识别号     */
  screditcode: '',
  /** 开户行及账号     */
  sbankaccount: '',
  /** 联系人  */
  scontacts: '',
  /** 移动电话 */
  smobilephone: '',
  /** 邮政编码 */
  spostcode: '',
  /** 行业ID */
  adindustryid: '',
  /** 行业名称 */
  adindustryname: '',
  /** 归属主业务员ID */
  employid: '',
  /** 父级名称*/
  parentName: '',
  /** 归属主业务员名称 */
  employname: '',
  /** 父ID */
  parentid: '',
  /** 是否大客户 0-否 1-是 */
  bvip: false,
  /** 客户状态（活跃，不活跃）。。待定 */
  sstatus: '活跃',
  /** 审批状态 0-待审 1-在审 2-通过 3-否决  4-撤销 5-无效 ,2和3的是可以编辑的*/
  iapprovestatus: 2,
  /** 是否有效 0-无效 1-有效 */
  buse: true,
  /** 是否个人 0-否 1-是 */
  bindividual: true,
  /** 是否删除 0-否false 1-是true */
  bdelete: false,
  /** 序号 */
  isort: 1,
  /** 备注 */
  sremark: '',
  /** 客户归属*/
  customerbelong: [],
  /** 客户资料*/
  customerfiles: [],
  /** 并发标志 */
  version: '0',
  /** 最后操作员id */
  lastoperatorid: '',
  /** 最后操作员*/
  lastoperator: ''
})
onMounted(() => {
  loaddata()
})
/**
 * 监听传入id
 */
watch(
  () => props.selfdetailid,
  (newValue, oldValue) => {
    console.log(newValue)
    if (newValue !== '') {
      customerid.value = newValue
      loaddata()
    }
  }
)
/**
 * 获取客户列表
 */
const loaddata = () => {
  getCustomerByIdApi(customerid.value).then(({ obj, total }: IAxios<TAdCustomer>) => {
    CustomerData.value = obj
    console.log(obj)
  })
}
/**
 * 预览上传的文件
 * @param file
 */
const previewFile = (file: Twcustomerfiles) => {
  console.log(file)
  if (file.sfiletype === 'Photo') {
    window.open(file.durl)
  } else if (file.sfiletype === 'Office') {
    const url = `${global?.officePreviewUrl}?src=${file.durl}`
    window.open(url)
  }
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
</style>
